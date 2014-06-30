package com.qixi.common.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-18
 * Time: 下午11:26
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {
    private static final Logger logger = Logger.getLogger(FileUtil.class);
    private static final String classPath = FileUtil.class.getResource("/").toString();

    public static String readFileByChars(String fileName ,String charSet) {
        StringBuffer fileConterBuffer = new StringBuffer();
        File file = new File(fileName);


        try{
            String absolutePath = new File(FileUtil.class.getResource("/").getPath()).getParent();
            InputStreamReader insReader = new InputStreamReader(new FileInputStream(absolutePath +file),charSet);
              BufferedReader bufReader = new BufferedReader(insReader);
            String line ;
            while ( (line = bufReader.readLine()) != null ){
                fileConterBuffer.append(line);
            }
            bufReader.close();
            return fileConterBuffer.toString();
        } catch(IOException e){
            logger.error("Error" ,e);
            return "";
        }


    }
}
