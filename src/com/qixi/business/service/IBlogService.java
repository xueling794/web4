package com.qixi.business.service;

import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;
import com.qixi.db.entity.extend.BlogCommentExtend;
import com.qixi.db.entity.extend.BlogExtend;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public interface IBlogService {
    public List<BlogExtend> getBlogExtend(Integer blogId , Integer start , Integer size) throws BusinessException;

    public List<BlogCommentExtend> getBlogCommentExtend(int blogId , int start , int size) throws  BusinessException;

    public int addBlog(Blog blog) throws BusinessException;

    public int addBlogComment(BlogComment blogComment) throws BusinessException;

    public int updateBlog(Blog blog) throws BusinessException;

    public List<BlogCommentExtend> getBlogLastComment(int blogId ) throws  BusinessException;
}
