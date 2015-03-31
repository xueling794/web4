package com.qixi.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.qixi.business.service.IImageService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.UserBase;
import com.qixi.common.constant.ImageConst;
import com.qixi.common.constant.ResultInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-31
 * Time: 下午11:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ImageController extends BaseController {

    private static final Logger logger = Logger.getLogger(ImageController.class);

    @Autowired
    IImageService imageService;

    @RequestMapping("/image/{objectId}")
    public void getImageByObjId(@PathVariable String objectId,HttpServletRequest req, HttpServletResponse res) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        try {
            GridFSDBFile gridFSDBFile = imageService.findFileById(objectId);
            if(gridFSDBFile == null){
                logger.warn("未找到图片"+objectId);
                this.failResponse(res, "加载图片失败");
                return;
            }
            OutputStream os = res.getOutputStream() ;
            long osLength = imageService.wirteOutputStream(os,gridFSDBFile);
            res.setContentType(gridFSDBFile.getContentType());
            res.setContentLength((int)gridFSDBFile.getLength());
            res.setStatus(HttpServletResponse.SC_OK);
        }  catch (IOException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "加载图片失败");
        }  catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "加载图片失败");
        }  catch (Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res, "加载图片失败");
        }
        resMap.put("imgId",objectId);
        logger.info("获取图片成功:"+objectId);
        this.successResponse(res,resMap);
    }

    @RequestMapping("/image/upload")
    public void uploadImage(HttpServletRequest req, HttpServletResponse res) {
        UserBase userBase = this.getUserBase(req);
        if(userBase == null){
            logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
            this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
            return;
        }//Validate  user login
        try{
            boolean isMultipart= ServletFileUpload.isMultipartContent(req);

            if(isMultipart){
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);

                Iterator items;

                //解析表单中提交的所有文件内容
                items=upload.parseRequest(req).iterator();
                List<String> imgUrlList = new ArrayList();
                while(items.hasNext()){
                    FileItem item = (FileItem) items.next();
                    if(!item.isFormField()){

                        //上传文件以后的存储路径
                        String uploadFilePath= ((DiskFileItem) item ).getStoreLocation().getPath();
                        File uploaderFile = new File(uploadFilePath);
                        item.write(uploaderFile);
                        Path path = Paths.get(uploadFilePath);
                        String fileType = Files.probeContentType(path);
                        //压缩文件并保存
                        String uuid = UUID.randomUUID().toString();
                        String absolutePath = new File(ImageController.class.getResource("/").getPath()).getParent();
                        String picTo = absolutePath+"/upload/avatar/"+uuid+".tmp";
                        ConvertCmd cmd = new ConvertCmd();
                        //remove it in linux
                        cmd.setSearchPath("G://Program Files//ImageMagick-6.9.0-Q16");

                        IMOperation op = new IMOperation();
                        op.addImage(uploadFilePath);
                        op.resize(ImageConst.IMAGE_CONVERT_WIDTH, null);
                        //添加水印文字
                        op.font("Arial").gravity("southeast").pointsize(18).fill("#9c9c9c").draw("text 20,20 www.94yiju.com");
                        op.addImage(picTo);
                        cmd.run(op);
                        //存储压缩后的文件到Mongodb
                        FileInputStream fis = new FileInputStream(picTo);
                        DBObject metaData = new BasicDBObject();
                        metaData.put("module","blog");
                        metaData.put("uid",userBase.getId());
                        String imageUrl = imageService.saveImage(fis,uuid+".png",fileType,metaData);
                        imgUrlList.add(imageUrl);

                        //删除上传时产生的临时文件
                        if (uploaderFile.isFile() && uploaderFile.exists()) {
                            uploaderFile.delete();
                        }
                        File convertImgFile = new File(picTo);
                        if (convertImgFile.isFile() && convertImgFile.exists()) {
                            convertImgFile.delete();
                        }
                        logger.info("添加图片成功 "+ imageUrl);
                    }
                }
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("result" ,true);
                map.put("imageUrl",imgUrlList);
                this.successResponse(res,map);
                return;
            } else{
                logger.warn("添加图片失败，图片格式不正确");
                this.failResponse(res,"图片格式不正确");
            }
        } catch (IOException e) {
            logger.warn("添加图片失败，文件操作失败");
            this.failResponse(res,ResultInfo.SYS_INTERNAL_ERROR);
        } catch (InterruptedException e) {
            logger.warn("添加图片失败，压缩文件操作失败");
            this.failResponse(res,ResultInfo.SYS_INTERNAL_ERROR);
        } catch (IM4JavaException e) {
            logger.warn("添加图片失败，压缩文件操作失败");
            this.failResponse(res,ResultInfo.SYS_INTERNAL_ERROR);
        } catch (FileUploadException e) {
            logger.warn("添加图片失败，文件上传操作失败");
            this.failResponse(res,ResultInfo.SYS_INTERNAL_ERROR);
        } catch (BusinessException e) {
            logger.warn("添加图片失败，内部错误");
            this.failResponse(res,ResultInfo.SYS_INTERNAL_ERROR);
        } catch (Exception e) {
            logger.warn("添加图片失败，写文件错误");
            this.failResponse(res,ResultInfo.SYS_INTERNAL_ERROR);
        }

    }
}
