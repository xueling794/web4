package com.qixi.controller;

import com.qixi.business.service.IBlogService;
import com.qixi.business.service.IUserService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.UserBase;
import com.qixi.common.constant.ResultInfo;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;
import com.qixi.db.entity.extend.BlogCommentExtend;
import com.qixi.db.entity.extend.BlogExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午11:15
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class BlogController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    IUserService userService;

    @Autowired
    IBlogService blogService;

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
                logger.info("获取话题信息成功");
                this.successResponse(res,map);
            } else{
                map.put("blog",null);
                logger.info("为获取话题信息");
                this.failResponse(res, "为获取话题信息");
            }

            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "为获取话题信息");
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
            logger.info("获取话题信息成功");
            this.successResponse(res,map);

            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "为获取话题信息");
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
            logger.info("获取话题信息成功");
            this.successResponse(res,map);

            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "为获取话题信息");
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

            map.put("result",blogId);
            //map.put("resultMsg",resultInfoEntity.getResultInfo());
            //logger.info(resultInfoEntity.getResultInfo());
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
            //map.put("resultMsg",resultInfoEntity.getResultInfo());
            //logger.info(resultInfoEntity.getResultInfo());
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
}
