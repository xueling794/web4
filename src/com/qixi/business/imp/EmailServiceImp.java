package com.qixi.business.imp;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IEmailService;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.constant.EmailAuthenticator;
import com.qixi.common.constant.EmailConst;
import com.qixi.common.constant.ResultInfo;
import com.qixi.common.util.FileUtil;
import com.qixi.db.DAO.Service.IUserDAO;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-4
 * Time: 下午10:24
 * To change this template use File | Settings | File Templates.
 */
public class EmailServiceImp implements IEmailService {
    private Logger logger = Logger.getLogger(EmailServiceImp.class);
    private String account;
    private String accountPswd;
    private String mailServer;
    private String mailServerPort;

    private IUserDAO userDAO;
    private String activeUserUrl ;



    private String resetPasswordUrl;



    public String getAccountPswd() {
        return accountPswd;
    }

    public void setAccountPswd(String accountPswd) {
        this.accountPswd = accountPswd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public IUserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String getActiveUserUrl() {
        return activeUserUrl;
    }

    public void setActiveUserUrl(String activeUserUrl) {
        this.activeUserUrl = activeUserUrl;
    }

    public String getResetPasswordUrl() {
        return resetPasswordUrl;
    }

    public void setResetPasswordUrl(String resetPasswordUrl) {
        this.resetPasswordUrl = resetPasswordUrl;
    }

    @Override
     public boolean sendEmail(String email, String title, String content) throws BusinessException {
        try{
            Properties p = new Properties();
            p.put("mail.smtp.host", mailServer);
            p.put("mail.smtp.port", mailServerPort);
            p.put("mail.smtp.auth", "true");

            EmailAuthenticator authenticator = new EmailAuthenticator(account , accountPswd);
            Session sendMailSession = Session.getDefaultInstance(p, authenticator);
            try {
                // 根据session创建一个邮件消息
                Message mailMessage = new MimeMessage(sendMailSession);
                // 创建邮件发送者地址
                Address from = new InternetAddress(account);
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

    @Override
    public ResultInfoEntity sendActiveEmail(String email , String data) throws BusinessException {
        try{
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            String fileContent = FileUtil.readFileByChars("/mailTemplate/activeEmailTpl.vm","utf8");
            String[] emailArray = email.split("@");
            String userName = email;
            if(emailArray != null && emailArray.length >=1){
                userName = emailArray[0];
            }
            fileContent = fileContent.replaceAll(EmailConst.EMAIL_REG_USERNAME ,userName);
            fileContent = fileContent.replaceAll(EmailConst.EMAIL_REG_ACTIVE_DATA , activeUserUrl+"?data="+data);
            if(fileContent == null || fileContent.equals("")) {
                  return resultInfoEntity ;
            }
            boolean resultFlag = sendEmail(email ,EmailConst.EMAIL_TITLE_ACTIVE ,fileContent);
            if(resultFlag){
                resultInfoEntity.setResultFlag(true);
                resultInfoEntity.setResultInfo(ResultInfo.EMAIL_ACTIVE_SEND_SUCCESS);
                return resultInfoEntity;
            }else{
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.EMAIL_ACTIVE_SEND_FAILURE);
                return resultInfoEntity;
            }
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }

    }

    @Override
    public ResultInfoEntity sendPasswordEmail(String email ,String userName ,String data) throws BusinessException {
       try{
           ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
           String fileContent = FileUtil.readFileByChars("/mailTemplate/passwordUrlTpl.vm","utf8");
           fileContent = fileContent.replaceAll(EmailConst.EMAIL_REG_USERNAME , userName);
           fileContent = fileContent.replaceAll(EmailConst.EMAIL_REG_PSWD_DATA,resetPasswordUrl+"?data="+data);
           Boolean resultFlag = sendEmail(email,EmailConst.EMAIL_TITLE_PASSWORD,fileContent);
            if(resultFlag){
                resultInfoEntity.setResultFlag(true);
                resultInfoEntity.setResultInfo(ResultInfo.EMAIL_PSWD_SEND_SUCCESS);
                return resultInfoEntity;
            }else{
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.EMAIL_PSWD_SEND_FAILURE);
                return resultInfoEntity;
            }
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }


}
