package com.qixi.db.DAO.imp;

import com.qixi.common.BaseDAO;
import com.qixi.common.Exception.DBException;
import com.qixi.db.DAO.Service.IBlogDAO;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;
import com.qixi.db.entity.BlogCommentExample;
import com.qixi.db.entity.BlogExample;
import com.qixi.db.entity.extend.BlogCommentExtend;
import com.qixi.db.entity.extend.BlogExtend;
import com.qixi.db.mapper.BlogCommentMapper;
import com.qixi.db.mapper.BlogMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午10:56
 * To change this template use File | Settings | File Templates.
 */
public class BlogDAOImp extends BaseDAO implements IBlogDAO {
    @Override
    public Blog getBlogById(int blogId) throws DBException {
        try{
            BlogMapper blogMapper = (BlogMapper)this.getMapperClass();
            BlogExample blogExample = new BlogExample();
            BlogExample.Criteria criteria = blogExample.createCriteria();
            criteria.andIdEqualTo(blogId);
            List<Blog> blogList = blogMapper.selectByExampleWithBLOBs(blogExample);
            if(blogList == null || blogList.size() <1){
                return null;
            }else{
                return blogList.get(0);
            }
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public List<BlogComment> getBlogComment(int blogId) throws DBException {
        try{
            BlogCommentMapper blogCommentMapper = (BlogCommentMapper)this.getMapperClass();
            BlogCommentExample blogCommentExample = new BlogCommentExample();
            BlogCommentExample.Criteria criteria = blogCommentExample.createCriteria();
            criteria.andBlogIdEqualTo(blogId);
            List<BlogComment> blogCommentList = blogCommentMapper.selectByExample(blogCommentExample);
            return blogCommentList;
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public int addBlog(Blog blog) throws DBException {
        try{
            BlogMapper blogMapper = (BlogMapper)this.getMapperClass();
            blogMapper.insert(blog);
            return blog.getId()  ;
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public int addBlogComment(BlogComment blogComment) throws DBException {
        try{
            BlogCommentMapper blogCommentMapper = (BlogCommentMapper)this.getMapperClass(BlogCommentMapper.class);
            return blogCommentMapper.insertSelective(blogComment);
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }

    }
    @Override
    public int updateBlog(Blog blog) throws DBException {
        try{
            BlogMapper blogMapper = (BlogMapper)this.getMapperClass();
            BlogExample blogExample = new BlogExample();
            BlogExample.Criteria criteria = blogExample.createCriteria();
            criteria.andIdEqualTo(blog.getId());
            return blogMapper.updateByExampleSelective(blog, blogExample);
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public int getBlogCommentCount(int blogId) throws DBException {
        try{
            BlogCommentMapper blogCommentMapper = (BlogCommentMapper) this.getMapperClass(BlogCommentMapper.class);
            BlogCommentExample blogCommentExample = new BlogCommentExample();
            BlogCommentExample.Criteria criteria = blogCommentExample.createCriteria();
            criteria.andBlogIdEqualTo(blogId);
            return blogCommentMapper.countByExample(blogCommentExample);
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public List<BlogExtend> getBlogExtend(Integer id, Integer start, Integer size) throws DBException {
        try{
            BlogMapper blogMapper = (BlogMapper) this.getMapperClass(BlogMapper.class);
            Map<String ,Object> paramMap = new HashMap<String,Object>();
            if(id != null && id>0){
                paramMap.put("id",id);
            }
            if(start != null && start>=0 && size != null && size>0)  {
                paramMap.put("start",start);
                paramMap.put("size",size);
            }
            return blogMapper.getBlogExtend(paramMap);
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public List<BlogCommentExtend> getBlogCommentExtend(Integer blogId, Integer start, Integer size) throws DBException {
        try{
            BlogCommentMapper blogCommentMapper = (BlogCommentMapper) this.getMapperClass(BlogCommentMapper.class);
            Map<String ,Object> paramMap = new HashMap<String,Object>();
            if(blogId != null && blogId>0){
                paramMap.put("blogId",blogId);
            }
            if(start != null && start>=0 && size != null && size>0)  {
                paramMap.put("start",start);
                paramMap.put("size",size);
            }
            return blogCommentMapper.getBlogCommentExtend(paramMap);
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public List<BlogCommentExtend> getBlogLastComment(Integer blogId) throws DBException {
        try{
            BlogCommentMapper blogCommentMapper = (BlogCommentMapper) this.getMapperClass(BlogCommentMapper.class);
            Map<String ,Object> paramMap = new HashMap<String,Object>();
            if(blogId != null && blogId>0){
                paramMap.put("blogId",blogId);
            }
            return blogCommentMapper.getBlogLastComment(paramMap);
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }
}
