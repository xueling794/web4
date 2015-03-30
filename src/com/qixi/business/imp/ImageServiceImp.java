package com.qixi.business.imp;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.qixi.business.service.IImageService;
import com.qixi.common.Exception.BusinessException;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-27
 * Time: 下午11:25
 * To change this template use File | Settings | File Templates.
 */
public class ImageServiceImp  implements IImageService{

    public GridFsTemplate getGsTemplate() {
        return gsTemplate;
    }

    public void setGsTemplate(GridFsTemplate gsTemplate) {
        this.gsTemplate = gsTemplate;
    }

    private GridFsTemplate gsTemplate;



    @Override
    public String saveImage(FileInputStream fs, String fileName, String contentType, DBObject metaData)  throws BusinessException,FileNotFoundException {
        try{
            GridFSFile gsFile = gsTemplate.store(fs,fileName,contentType,metaData);
            return gsFile.getId().toString();
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public GridFSDBFile findFileById(String id) throws BusinessException {

        try{
            GridFSDBFile gsDbFile = gsTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(id))));
            return gsDbFile;
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }

    }

    @Override
    public long wirteOutputStream(OutputStream out, GridFSDBFile gsFile) throws BusinessException,IOException {
       try{
            return gsFile.writeTo(out);
        }catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }


}
