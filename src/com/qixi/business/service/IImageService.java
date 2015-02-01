package com.qixi.business.service;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.qixi.common.Exception.BusinessException;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-27
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public interface IImageService {
    public String saveImage( FileInputStream fs, String filename, String contenttype, DBObject metadata) throws BusinessException,FileNotFoundException;

    public GridFSDBFile findFileById(String id) throws BusinessException;

    public long wirteOutputStream(OutputStream out ,GridFSDBFile gsFile) throws BusinessException,IOException;
}
