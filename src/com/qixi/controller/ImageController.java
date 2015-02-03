package com.qixi.controller;

import com.mongodb.gridfs.GridFSDBFile;
import com.qixi.business.service.IImageService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-31
 * Time: 下午11:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ImageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    IImageService imageService;

    @RequestMapping("/image/{objectId}")
    public void sendEmail(@PathVariable String objectId,HttpServletRequest req, HttpServletResponse res) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        try {
            GridFSDBFile gridFSDBFile = imageService.findFileById(objectId);
            OutputStream os = res.getOutputStream() ;
            long osLength = imageService.wirteOutputStream(os,gridFSDBFile);
            res.setContentType(gridFSDBFile.getContentType());
            res.setContentLength((int)gridFSDBFile.getLength());
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "Send Email Failure");
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res, "Send Email Failure");
        }
        resMap.put("imgId",objectId);
        logger.info("获取图片:"+objectId);
        this.successResponse(res,resMap);
    }

    @RequestMapping("/image/upload")
    public void uploadImage(HttpServletRequest req, HttpServletResponse res) {
        String picFrom = "G://Program Files//ImageMagick-6.9.0-Q16/12345.jpg";
        String picTo = "c://new.jpg";
        ConvertCmd cmd = new ConvertCmd();
        cmd.setSearchPath("G://Program Files//ImageMagick-6.9.0-Q16");

        IMOperation op = new IMOperation();
        op.addImage(picFrom);
        op.resize(600, null);
        //op.font("Arial").fill("grey").draw("text 500,10 www.94yiju.com");
        op.font("Arial").gravity("southeast").pointsize(18).fill("#9c9c9c") .draw("text 5,5 www.94YiJu.com");
        op.addImage(picTo);

        try {
            cmd.run(op);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IM4JavaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.successResponse(res,null);
    }
}
