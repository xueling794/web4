package com.qixi.controller;

import com.qixi.business.service.IBlogService;
import com.qixi.business.service.IEmailService;
import com.qixi.business.service.IUserService;
import com.qixi.common.BaseController;
import com.qixi.common.EmailConfig;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.UserBase;
import com.qixi.common.constant.EmailAuthenticator;
import com.qixi.common.constant.EmailConst;
import com.qixi.common.constant.ResultInfo;
import com.qixi.common.util.FileUtil;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;
import com.qixi.db.entity.extend.BlogCommentExtend;
import com.qixi.db.entity.extend.BlogExtend;
import com.qixi.db.entity.qq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午11:15
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class BlogController extends BaseController {
    private static final Logger logger = Logger.getLogger(BlogController.class);

    @Autowired
    IUserService userService;

    @Autowired
    IBlogService blogService;

    @Autowired
    IEmailService emailService;

    @RequestMapping("/blog/{blogId}/getBlog")
    public void getBlog(@PathVariable int blogId , HttpServletRequest req, HttpServletResponse res) {
        try {
            Map<String,Object> map = new HashMap<String, Object>();
            List<BlogExtend> blogExtendList = blogService.getBlogExtend(blogId, 0, 1);
            if(blogExtendList != null && blogExtendList.size()>0){
                BlogExtend blogExtend = blogExtendList.get(0);
                Blog blog = new Blog();
                blog.setId(blogExtend.getId());
                blog.setReadCount(blogExtend.getReadCount()+1);
                blogService.updateBlog(blog);
                map.put("blog",blogExtendList.get(0));
                logger.info("获取话题信息"+blogId+"成功");
                this.successResponse(res,map);
            } else{
                map.put("blog",null);
                logger.warn("未能获取话题信息"+blogId);
                this.failResponse(res, "未能获取话题信息");
            }

            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "未能获取话题信息");
        }
    }

    @RequestMapping("/blog/getBlogList")
    public void getBlogList( HttpServletRequest req, HttpServletResponse res) {
        try {
            String data = this.getData(req);

            Map<String,Object> map = this.getModel(data,Map.class);
            int start = this.getInt(map,"start");
            int size = this.getInt(map,"size");
            List<BlogExtend> blogExtendList = blogService.getBlogExtend(-1, start, size);
            if(blogExtendList != null && blogExtendList.size()>0){

                for(int i=0 ,j=blogExtendList.size();i<j;i++){
                    BlogExtend blogExtend = blogExtendList.get(i);
                     List<BlogCommentExtend> blogLastCommentExtend = blogService.getBlogLastComment(blogExtend.getId());
                    if(blogLastCommentExtend != null && blogLastCommentExtend.size()>0 && blogLastCommentExtend.get(0) != null){
                        blogExtend.setCommentAvatar(blogLastCommentExtend.get(0).getAvatar());
                        blogExtend.setCommentNickName(blogLastCommentExtend.get(0).getNickName());
                        blogExtend.setCommentGender(blogLastCommentExtend.get(0).getGender());
                        blogExtend.setCommentUid(blogLastCommentExtend.get(0).getUid());
                        blogExtend.setCommentDate(blogLastCommentExtend.get(0).getCreateDate());
                    }else{
                        blogExtend.setCommentAvatar(blogExtend.getAvatar());
                        blogExtend.setCommentNickName(blogExtend.getNickName());
                        blogExtend.setCommentGender(blogExtend.getGender());
                        blogExtend.setCommentUid(blogExtend.getUid());
                        blogExtend.setCommentDate(blogExtend.getCreateDate());
                    }
                }
            }

            map.put("blogList",blogExtendList);
            logger.info("获取话题信息列表成功");
            this.successResponse(res,map);

            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "未能获取话题信息列表");
        }
    }


    @RequestMapping("/blog/{blogId}/getBlogComment")
    public void getBlogCommentList( @PathVariable int blogId , HttpServletRequest req, HttpServletResponse res) {
        try {
            String data = this.getData(req);

            Map<String,Object> map = this.getModel(data,Map.class);
            int start = this.getInt(map,"start");
            int size = this.getInt(map,"size");
            List<BlogCommentExtend> blogCommentExtendList = blogService.getBlogCommentExtend(blogId, start, size);


            map.put("blogCommentList",blogCommentExtendList);
            logger.info("获取话题"+blogId+"评论信息成功");
            this.successResponse(res,map);

            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "未能获取话题评论信息");
        }
    }

    @RequestMapping("/blog/createBlog")
    public void createBlog(HttpServletRequest req, HttpServletResponse res) {
        try{
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login

            String data = this.getPostData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            //验证验证码
            String authCode = this.getString(map,"authCode");
            String sessionCaptcha = (String)req.getSession().getAttribute("captcha");
            if(sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(authCode)){
                map.put("result",false);
                map.put("resultMsg", ResultInfo.REG_CAPTCHA_ERROR);
                logger.warn(ResultInfo.REG_CAPTCHA_ERROR);
                this.successResponse(res,map);
                return;
            }
            int uid = this.getUserBase(req).getId();
            String blogTitle =  this.getString(map,"title");
            String blogContent =  this.getString(map,"content");
            Blog blog = new Blog();
            blog.setUid(uid);
            blog.setTitle(blogTitle);
            blog.setContent(blogContent);
            blog.setStatus(true);
            blog.setCreateDate(new Date());
            blog.setReadCount(1);

           int blogId = blogService.addBlog(blog);

            map.put("result",true);
            map.put("blogId",blogId);
            logger.info("创建话题信息成功"+blogId);
            this.successResponse(res,map);
            return;
        }catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"创建话题失败");
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"创建话题失败");
        }
    }

    @RequestMapping("/blog/{blogId}/createBlogComment")
    public void createBlogComment(@PathVariable int blogId ,HttpServletRequest req, HttpServletResponse res) {
        try{
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login
            String data = this.getPostData(req);
            Map<String,Object> map = this.getModel(data, Map.class);
            String authCode = this.getString(map,"authCode");
            //验证验证码
            String sessionCaptcha = (String)req.getSession().getAttribute("captcha");
            if(sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(authCode)){
                map.put("result",false);
                map.put("resultMsg", ResultInfo.REG_CAPTCHA_ERROR);
                logger.warn(ResultInfo.REG_CAPTCHA_ERROR);
                this.successResponse(res, map);
                return;
            }
            int uid = this.getUserBase(req).getId();
            String commentContent =  this.getString(map,"content");
            BlogComment blogComment = new BlogComment();
            blogComment.setUid(uid);
            blogComment.setBlogId(blogId);
            blogComment.setContent(commentContent);
            blogComment.setCreateDate(new Date());
            int blogCommentId = blogService.addBlogComment(blogComment);
            BlogCommentExtend blogCommentExtend = new BlogCommentExtend();
            blogCommentExtend.setUid(uid);
            blogCommentExtend.setBlogId(blogId);
            blogCommentExtend.setContent(commentContent);
            blogCommentExtend.setCreateDate(new Date());
            blogCommentExtend.setId(blogCommentId);
            blogCommentExtend.setAvatar(this.getUserBase(req).getAvatar());
            blogCommentExtend.setNickName(this.getUserBase(req).getNickName());
            map.put("result",blogCommentExtend);
            logger.info("创建话题评论成功"+blogId);
            this.successResponse(res,map);
            return;
        }catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"创建话题的评论失败");
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"创建话题的评论失败");
        }
    }

    @RequestMapping("/qq/update")
    public void updateQq(HttpServletRequest req, HttpServletResponse res) {
        try{
            String data = this.getPostData(req);

            Map<String,Object> map = this.getModel(data,Map.class);
            String q = this.getString(map,"qq");
            qq qEntity = new qq();
            qEntity.setQq(q);
            qEntity.setFlag(true);

            blogService.updateQq(qEntity);

            logger.info("更新qq状态:"+q);
            map.put("qq",q);
            this.successResponse(res,map);
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"更新qq状态");
        }

    }

    @RequestMapping("/qq/send")
    public void send(HttpServletRequest req, HttpServletResponse res) {
        String data = this.getData(req);
        Map<String,Object> map = new HashMap<String,Object>();
        EmailConfig emailConfig1 = new EmailConfig();
        emailConfig1.setAccount("dalianyg@126.com");
        emailConfig1.setAccountPswd("yg2013");
        emailConfig1.setMailServer("smtp.126.com");
        emailConfig1.setMailServerPort("25");
        EmailConfig emailConfig2 = new EmailConfig();
        emailConfig2.setAccount("jiushiyiju@126.com");
        emailConfig2.setAccountPswd("yg2013");
        emailConfig2.setMailServer("smtp.126.com");
        emailConfig2.setMailServerPort("25");
        EmailConfig emailConfig3 = new EmailConfig();
        emailConfig3.setAccount("yiju94@126.com");
        emailConfig3.setAccountPswd("yg2013");
        emailConfig3.setMailServer("smtp.126.com");
        emailConfig3.setMailServerPort("25");
        EmailConfig emailConfig4 = new EmailConfig();
        emailConfig4.setAccount("jiushiyiju@yeah.net");
        emailConfig4.setAccountPswd("yg2013");
        emailConfig4.setMailServer("smtp.126.com");
        emailConfig4.setMailServerPort("25");
        EmailConfig emailConfig5 = new EmailConfig();
        emailConfig5.setAccount("yiju94@yeah.net");
        emailConfig5.setAccountPswd("yg2013");
        emailConfig5.setMailServer("smtp.126.com");
        emailConfig5.setMailServerPort("25");
        EmailConfig emailConfig6 = new EmailConfig();
        emailConfig6.setAccount("dalianyg@yeah.net");
        emailConfig6.setAccountPswd("yg2013");
        emailConfig6.setMailServer("smtp.126.com");
        emailConfig6.setMailServerPort("25");
        List<EmailConfig> emailConfigs = new ArrayList<EmailConfig>();
        emailConfigs.add(emailConfig1);
        emailConfigs.add(emailConfig2);
        emailConfigs.add(emailConfig3);
        emailConfigs.add(emailConfig4);
        emailConfigs.add(emailConfig5);
        emailConfigs.add(emailConfig6);

        try {
            List<qq> qqList = blogService.getQq();
            //System.out.println(qqList.size());
            /*List<qq> qqList = new  ArrayList<qq>();
            qq q1 = new qq();
            q1.setId(1);
            q1.setQq("99217837");
            qqList.add(q1);
            qq q2 = new qq();
            q2.setId(2);
            q2.setQq("99217837");
            qqList.add(q2);
            qq q3 = new qq();
            q3.setId(3);
            q3.setQq("99217837");
            qqList.add(q3);
            qq q4 = new qq();
            q4.setId(4);
            q4.setQq("99217837");
            qqList.add(q4);
            qq q5 = new qq();
            q5.setId(5);
            q5.setQq("99217837");
            qqList.add(q5);
            qq q6 = new qq();
            q6.setId(6);
            q6.setQq("99217837");
            qqList.add(q6);*/
            for(int j=1559;j>=0 ;j--){
                String tempEmail = qqList.get(j).getQq();
                String fileContent = FileUtil.readFileByChars("/mailTemplate/inviteEmailTpl.vm", "utf8");
                fileContent = fileContent.replaceAll( "_qq_" , Long.toHexString(Long.parseLong(tempEmail.trim())));
                if(j%2 == 0){
                    sendEmail1(tempEmail + "@qq.com" , EmailConst.EMAIL_TITLE_INVITE,fileContent)  ;
                } else if(j%2 == 1){
                    sendEmail2(tempEmail + "@qq.com" , EmailConst.EMAIL_TITLE_INVITE,fileContent)  ;
                } /* else if(j%3 == 2){
                    sendEmail3(tempEmail + "@qq.com" , EmailConst.EMAIL_TITLE_INVITE,fileContent)  ;
                }*/
                //sendEmail1(tempEmail + "@qq.com" , EmailConst.EMAIL_TITLE_INVITE,fileContent)  ;
                logger.info(tempEmail+"-->"+j) ;


                Thread.sleep(30000L);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BusinessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        map.put("checkResult",true);
        this.successResponse(res,map);
    }

    public boolean sendEmail1(String email, String title, String content) throws BusinessException {
        try{
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setAccount("dalianyg@126.com");
            emailConfig.setAccountPswd("yg2013");
            emailConfig.setMailServer("smtp.126.com");
            emailConfig.setMailServerPort("25");
            logger.info("dalianyg@126.com");
            Properties p = new Properties();
            p.put("mail.smtp.host", emailConfig.getMailServer());
            p.put("mail.smtp.port", emailConfig.getMailServerPort());
            p.put("mail.smtp.auth", "true");

            EmailAuthenticator authenticator = new EmailAuthenticator(emailConfig.getAccount(), emailConfig.getAccountPswd());
            Session sendMailSession = Session.getInstance(p, authenticator);
            try {
                // 根据session创建一个邮件消息
                Message mailMessage = new MimeMessage(sendMailSession);
                // 创建邮件发送者地址
                Address from = new InternetAddress(emailConfig.getAccount());
                // 设置邮件消息的发送者
                mailMessage.setFrom(from);
                // 创建邮件的接收者地址，并设置到邮件消息中
                Address to = new InternetAddress(email);
                // Message.RecipientType.TO属性表示接收者的类型为TO
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                // 设置邮件消息的主题
                mailMessage.setSubject(title);
                // 设置邮件消息发送的时间
                mailMessage.setSentDate(new Date());
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(content, "text/html; charset=utf-8");
                mainPart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                mailMessage.setContent(mainPart);
                // 发送邮件
                Transport.send(mailMessage);
                return true;

            } catch(MessagingException e){
                logger.error(e.getMessage(),e);
                return false;
            }
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }

    }



    public boolean sendEmail2(String email, String title, String content) throws BusinessException {
        try{
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setAccount("dalianyg@yeah.net");
            emailConfig.setAccountPswd("yg2013");
            emailConfig.setMailServer("smtp.yeah.net");
            emailConfig.setMailServerPort("25");
            logger.info("dalianyg@yeah.net");
            Properties p = new Properties();
            p.put("mail.smtp.host", emailConfig.getMailServer());
            p.put("mail.smtp.port", emailConfig.getMailServerPort());
            p.put("mail.smtp.auth", "true");

            EmailAuthenticator authenticator = new EmailAuthenticator(emailConfig.getAccount(), emailConfig.getAccountPswd());
            Session sendMailSession = Session.getInstance(p, authenticator);
            try {
                // 根据session创建一个邮件消息
                Message mailMessage = new MimeMessage(sendMailSession);
                // 创建邮件发送者地址
                Address from = new InternetAddress(emailConfig.getAccount());
                // 设置邮件消息的发送者
                mailMessage.setFrom(from);
                // 创建邮件的接收者地址，并设置到邮件消息中
                Address to = new InternetAddress(email);
                // Message.RecipientType.TO属性表示接收者的类型为TO
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                // 设置邮件消息的主题
                mailMessage.setSubject(title);
                // 设置邮件消息发送的时间
                mailMessage.setSentDate(new Date());
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(content, "text/html; charset=utf-8");
                mainPart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                mailMessage.setContent(mainPart);
                // 发送邮件
                Transport.send(mailMessage);
                return true;

            } catch(MessagingException e){
                logger.error(e.getMessage(),e);
                return false;
            }
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }

    }


    public boolean sendEmail4(String email, String title, String content) throws BusinessException {
        try{
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setAccount("yiju94@tom.com");
            emailConfig.setAccountPswd("yg2013");
            emailConfig.setMailServer("smtp.tom.com");
            emailConfig.setMailServerPort("25");
            logger.info("yiju94@tom.com");
            Properties p = new Properties();
            p.put("mail.smtp.host", emailConfig.getMailServer());
            p.put("mail.smtp.port", emailConfig.getMailServerPort());
            p.put("mail.smtp.auth", "true");

            EmailAuthenticator authenticator = new EmailAuthenticator(emailConfig.getAccount(), emailConfig.getAccountPswd());
            Session sendMailSession = Session.getInstance(p, authenticator);
            try {
                // 根据session创建一个邮件消息
                Message mailMessage = new MimeMessage(sendMailSession);
                // 创建邮件发送者地址
                Address from = new InternetAddress(emailConfig.getAccount());
                // 设置邮件消息的发送者
                mailMessage.setFrom(from);
                // 创建邮件的接收者地址，并设置到邮件消息中
                Address to = new InternetAddress(email);
                // Message.RecipientType.TO属性表示接收者的类型为TO
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                // 设置邮件消息的主题
                mailMessage.setSubject(title);
                // 设置邮件消息发送的时间
                mailMessage.setSentDate(new Date());
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(content, "text/html; charset=utf-8");
                mainPart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                mailMessage.setContent(mainPart);
                // 发送邮件
                Transport.send(mailMessage);
                return true;

            } catch(MessagingException e){
                logger.error(e.getMessage(),e);
                return false;
            }
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }

    }


    public boolean sendEmail3(String email, String title, String content) throws BusinessException {
        try{
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setAccount("mpdalian@yeah.net");
            emailConfig.setAccountPswd("missionpublic");
            emailConfig.setMailServer("smtp.yeah.net");
            emailConfig.setMailServerPort("25");
            logger.info("mpdalian@yeah.net");
            Properties p = new Properties();
            p.put("mail.smtp.host", emailConfig.getMailServer());
            p.put("mail.smtp.port", emailConfig.getMailServerPort());
            p.put("mail.smtp.auth", "true");

            EmailAuthenticator authenticator = new EmailAuthenticator(emailConfig.getAccount(), emailConfig.getAccountPswd());
            Session sendMailSession = Session.getInstance(p, authenticator);
            try {
                // 根据session创建一个邮件消息
                Message mailMessage = new MimeMessage(sendMailSession);
                // 创建邮件发送者地址
                Address from = new InternetAddress(emailConfig.getAccount());
                // 设置邮件消息的发送者
                mailMessage.setFrom(from);
                // 创建邮件的接收者地址，并设置到邮件消息中
                Address to = new InternetAddress(email);
                // Message.RecipientType.TO属性表示接收者的类型为TO
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                // 设置邮件消息的主题
                mailMessage.setSubject(title);
                // 设置邮件消息发送的时间
                mailMessage.setSentDate(new Date());
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(content, "text/html; charset=utf-8");
                mainPart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                mailMessage.setContent(mainPart);
                // 发送邮件
                Transport.send(mailMessage);
                return true;

            } catch(MessagingException e){
                logger.error(e.getMessage(),e);
                return false;
            }
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }

    }
}
