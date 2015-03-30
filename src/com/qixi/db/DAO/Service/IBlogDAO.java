package com.qixi.db.DAO.Service;

import com.qixi.common.Exception.DBException;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;
import com.qixi.db.entity.extend.BlogCommentExtend;
import com.qixi.db.entity.extend.BlogExtend;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午10:50
 * To change this template use File | Settings | File Templates.
 */
public interface IBlogDAO {

    public Blog getBlogById(int blogId) throws DBException;

    public List<BlogComment> getBlogComment(int blogId) throws DBException;

    public int addBlog(Blog blog) throws DBException;

    public int addBlogComment(BlogComment blogComment) throws DBException;

    public int updateBlog(Blog blog) throws DBException ;

    public int getBlogCommentCount(int blogId) throws DBException ;

    public List<BlogExtend> getBlogExtend(Integer id,Integer start ,Integer size) throws DBException;

    public List<BlogCommentExtend> getBlogCommentExtend(Integer blogId,Integer start ,Integer size) throws DBException;

    public List<BlogCommentExtend> getBlogLastComment(Integer blogId) throws DBException;
}
