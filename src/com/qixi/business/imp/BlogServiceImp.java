package com.qixi.business.imp;

import com.qixi.business.service.IBlogService;
import com.qixi.common.Exception.BusinessException;
import com.qixi.db.DAO.Service.IBlogDAO;
import com.qixi.db.DAO.Service.IQqDAO;
import com.qixi.db.DAO.Service.IUserDAO;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;
import com.qixi.db.entity.extend.BlogCommentExtend;
import com.qixi.db.entity.extend.BlogExtend;
import com.qixi.db.entity.qq;

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

    public IQqDAO getQqDAO() {
        return qqDAO;
    }

    public void setQqDAO(IQqDAO qqDAO) {
        this.qqDAO = qqDAO;
    }

    private IQqDAO qqDAO ;

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
    public List<BlogExtend> getBlogExtend(Integer blogId , Integer start , Integer size) throws BusinessException {
        try{
            return blogDAO.getBlogExtend(blogId, start, size);
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public List<BlogCommentExtend> getBlogCommentExtend(int blogId , int start , int size) throws BusinessException {
        try{
            return blogDAO.getBlogCommentExtend(blogId, start, size);
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public int addBlog(Blog blog) throws BusinessException {
        try{
            return blogDAO.addBlog(blog);
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public int addBlogComment(BlogComment blogComment) throws BusinessException {
        try{
            return blogDAO.addBlogComment(blogComment);
        }catch (Exception e) {
                throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public int updateBlog(Blog blog) throws BusinessException {
        try{
            return blogDAO.updateBlog(blog);
        }catch (Exception e) {
                throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public List<BlogCommentExtend> getBlogLastComment(int blogId) throws BusinessException {
        try{
            return  blogDAO.getBlogLastComment(blogId);
        }catch (Exception e) {
                throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public List<qq> getQq() throws BusinessException {
        try{
            return  qqDAO.geQq();
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public int updateQq(qq q) throws BusinessException {
        try{
            return  qqDAO.updateQq(q);
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }


}
