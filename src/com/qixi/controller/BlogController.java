package com.qixi.controller;

import com.qixi.business.service.IBlogService;
import com.qixi.business.service.IUserService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
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
}
