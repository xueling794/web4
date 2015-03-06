package com.qixi.business.imp;

import com.qixi.business.service.IBlogService;
import com.qixi.common.Exception.BusinessException;
import com.qixi.db.DAO.Service.IBlogDAO;
import com.qixi.db.DAO.Service.IUserDAO;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午11:13
 * To change this template use File | Settings | File Templates.
 */
public class BlogServiceImp implements IBlogService {

    private IBlogDAO blogDAO ;
    private IUserDAO userDAO ;

    public IUserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public IBlogDAO getBlogDAO() {
        return blogDAO;
    }

    public void setBlogDAO(IBlogDAO blogDAO) {
        this.blogDAO = blogDAO;
    }

    @Override
    public Blog getBlogById(int blogId) throws BusinessException {
        return blogDAO.getBlogById(blogId);
    }

    @Override
    public List<BlogComment> getBlogComment(int blogId) throws BusinessException {
        return blogDAO.getBlogComment(blogId);
    }

    @Override
    public int addBlog(Blog blog) throws BusinessException {
        return blogDAO.addBlog(blog);
    }

    @Override
    public int addBlogComment(BlogComment blogComment) throws BusinessException {
        return blogDAO.addBlogComment(blogComment);
    }


}
