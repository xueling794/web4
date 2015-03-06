package com.qixi.db.DAO.imp;

import com.qixi.common.BaseDAO;
import com.qixi.common.Exception.BusinessException;
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
    public Blog getBlogById(int blogId) throws BusinessException {
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

    }

    @Override
    public List<BlogComment> getBlogComment(int blogId) throws BusinessException {
        BlogCommentMapper blogCommentMapper = (BlogCommentMapper)this.getMapperClass();
        BlogCommentExample blogCommentExample = new BlogCommentExample();
        BlogCommentExample.Criteria criteria = blogCommentExample.createCriteria();
        criteria.andBlogIdEqualTo(blogId);
        List<BlogComment> blogCommentList = blogCommentMapper.selectByExample(blogCommentExample);
        return blogCommentList;
    }

    @Override
    public int addBlog(Blog blog) throws BusinessException {
        BlogMapper blogMapper = (BlogMapper)this.getMapperClass();
        return blogMapper.insert(blog);
    }

    @Override
    public int addBlogComment(BlogComment blogComment) throws BusinessException {
        BlogCommentMapper blogCommentMapper = (BlogCommentMapper)this.getMapperClass();
        return blogCommentMapper.insert(blogComment);
    }
    @Override
    public int updateBlog(Blog blog) throws BusinessException {
        BlogMapper blogMapper = (BlogMapper)this.getMapperClass();
        return blogMapper.updateByPrimaryKey(blog);
    }

    @Override
    public int getBlogCommentCount(int blogId) throws BusinessException {
        BlogCommentMapper blogCommentMapper = (BlogCommentMapper) this.getMapperClass(BlogCommentMapper.class);
        BlogCommentExample blogCommentExample = new BlogCommentExample();
        BlogCommentExample.Criteria criteria = blogCommentExample.createCriteria();
        criteria.andBlogIdEqualTo(blogId);
        return blogCommentMapper.countByExample(blogCommentExample);
    }

    @Override
    public List<BlogExtend> getBlogExtend(Integer id, Integer start, Integer size) throws BusinessException {
        BlogMapper blogMapper = (BlogMapper) this.getMapperClass(BlogMapper.class);
        Map<String ,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("start",start);
        paramMap.put("size",size);
        return blogMapper.getBlogExtend(paramMap);
    }

    @Override
    public List<BlogCommentExtend> getBlogCommentExtend(Integer blogId, Integer start, Integer size) throws BusinessException {
        BlogCommentMapper blogCommentMapper = (BlogCommentMapper) this.getMapperClass(BlogCommentMapper.class);
        Map<String ,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("start",start);
        paramMap.put("size",size);
        return blogCommentMapper.getBlogCommentExtend(paramMap);
    }
}
