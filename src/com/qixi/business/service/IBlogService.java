package com.qixi.business.service;

import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.Blog;
import com.qixi.db.entity.BlogComment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-1
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public interface IBlogService {
    public Blog getBlogById(int blogId) throws BusinessException;

    public List<BlogComment> getBlogComment(int blogId) throws  BusinessException;
}
