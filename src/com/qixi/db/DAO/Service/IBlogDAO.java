package com.qixi.db.DAO.Service;

import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午10:50
 * To change this template use File | Settings | File Templates.
 */
public interface IBlogDAO {

    public Blog getBlogById(int blogId) throws BusinessException;

    public List<BlogComment> getBlogComment(int blogId) throws BusinessException;

    public int addBlog(Blog blog) throws BusinessException;

    public int addBlogComment(BlogComment blogComment) throws BusinessException;

    public int updateBlog(Blog blog) throws BusinessException ;
}
