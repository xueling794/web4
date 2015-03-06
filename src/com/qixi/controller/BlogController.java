package com.qixi.controller;

import com.qixi.business.service.IBlogService;
import com.qixi.business.service.IUserService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.UserBase;
import com.qixi.common.constant.ResultInfo;
import com.qixi.db.entity.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
            Blog blog = blogService.getBlogById(blogId);
            map.put("blog",blog);
            logger.info("获取话题信息成功");
            this.successResponse(res,map);
            return;
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "加载图片失败");
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
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            int uid = this.getUserBase(req).getId();
            String blogTitle =  this.getString(map,"title");
            String blogContent =  this.getString(map,"content");
            Blog blog = new Blog();
            blog.setUid(uid);
            blog.setTitle(blogTitle);
            blog.setContent(blogContent);
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
}
