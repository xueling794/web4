package com.qixi.controller;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IUserService;
import com.qixi.business.service.IVoteService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.UserBase;
import com.qixi.common.constant.ResultInfo;
import com.qixi.db.entity.VoteComment;
import com.qixi.db.entity.VoteItem;
import com.qixi.db.entity.VoteSelect;
import com.qixi.db.entity.extend.VoteCommentExtend;
import com.qixi.db.entity.extend.VoteExtend;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-13
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class VoteController extends BaseController {
    private Logger logger = Logger.getLogger(VoteController.class);

    @Autowired
    IUserService userService;

    @Autowired
    IVoteService voteService;

    @RequestMapping("/vote/createVote")
    public void createVote(HttpServletRequest req, HttpServletResponse res) {
        try{
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            int uid = this.getUserBase(req).getId();
            String voteTitle =  this.getString(map,"title");
            String voteRemark =  this.getString(map,"remark");
            String endDateStr = this.getString(map, "endDate")+" 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endDate = sdf.parse(endDateStr);
            short maxItem = this.getShort(map, "maxItem");
            List<String> itemList = (ArrayList<String>) map.get("itemArray");
            List<VoteItem> voteItemList = new ArrayList<VoteItem>();
            for(int i=0 ,j=itemList.size();i<j; i++){
                VoteItem voteItemTemp = new VoteItem();
                voteItemTemp.setConnent(itemList.get(i));
                voteItemList.add(voteItemTemp);
            }
            VoteExtend voteExtend = new VoteExtend();
            voteExtend.setUid(uid);
            voteExtend.setTitle(voteTitle);
            voteExtend.setRemark(voteRemark);

            voteExtend.setMaxItem(maxItem);
            voteExtend.setVoteItemList(voteItemList);
            voteExtend.setEndDate(endDate);
            voteExtend.setCreateDate(new Date());
            ResultInfoEntity resultInfoEntity = voteService.createVote(voteExtend);

            map.put("result",resultInfoEntity.isResultFlag());
            map.put("resultMsg",resultInfoEntity.getResultInfo());
            logger.info(resultInfoEntity.getResultInfo()+userBase.getEmail());
            this.successResponse(res,map);
            return;
        }catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"创建投票失败");
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"创建投票失败");
        }
    }

    @RequestMapping("/vote/getActiveVote")
    public void getActiveVote(HttpServletRequest req, HttpServletResponse res) {
        try{
            String data = this.getData(req);

            Map<String,Object> map = this.getModel(data,Map.class);
            int start = this.getInt(map,"start");
            int size = this.getInt(map,"size");
            List<VoteExtend> voteExtendList = voteService.getActiveVoteInfo(start,size);
            if(voteExtendList != null && voteExtendList.size()>0){
                for(int i=0,j=voteExtendList.size(); i<j ; i++){
                    int voteId = voteExtendList.get(i).getId();
                    int voteSelectCount = voteService.getVoteSelectCount(voteId);
                    int voteCommentCount = voteService.getVoteCommentCount(voteId);

                    voteExtendList.get(i).setVoteCount(voteSelectCount);
                    voteExtendList.get(i).setCommentCount(voteCommentCount);
                }
            }


            map.put("voteList",voteExtendList);
            logger.info("获取投票列表信息成功:");
            this.successResponse(res,map);
            return;
        }catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,ResultInfo.VOTE_GET_VOTE_ERROR);
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,ResultInfo.VOTE_GET_VOTE_ERROR);
        }

        /*VoteExtend voteExtend = new VoteExtend();
        voteExtend.setUid(uid);*/

    }

    @RequestMapping("/vote/getVoteDetail")
    public void getVoteDetail(HttpServletRequest req, HttpServletResponse res) {
        try{
            String data = this.getData(req);

            Map<String,Object> map = this.getModel(data,Map.class);
            int voteId = this.getInt(map,"voteId");
            VoteExtend voteExtend = voteService.getVoteExtendById(voteId);
            if(voteExtend == null){
                logger.warn(ResultInfo.VOTE_GET_VOTE_ERROR);
                this.failResponse(res,ResultInfo.VOTE_GET_VOTE_ERROR);
                return;
            }else{
                map.put("voteExtend" ,voteExtend);
            }
            int uid = this.getUserBase(req).getId();
            if(this.getUserBase(req) != null){
                List<VoteSelect> voteSelectList = voteService.getVoteSelectByUid(uid,voteId) ;
                if(voteSelectList != null && voteSelectList.size()>0){
                    map.put("voteSelectFlag" ,true);
                    map.put("voteSelectList" ,voteSelectList);
                    map.put("voteResult",voteService.getVoteResult(voteId));
                }else{
                    map.put("voteSelectFlag" , false);
                }

            }
            logger.info(uid+"获取投票信息成功"+voteId);
            this.successResponse(res,map);
            return;

        }catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,ResultInfo.VOTE_GET_VOTE_ERROR);
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,ResultInfo.VOTE_GET_VOTE_ERROR);
        }
    }

    @RequestMapping("/vote/addUserVoteSelect")
    public void addUserVoteSelect(HttpServletRequest req, HttpServletResponse res) {
        try{
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            int voteId = this.getInt(map, "voteId");
            int uid = this.getUserBase(req).getId();
            List<Double> itemIdList = (ArrayList<Double>) map.get("itemIdArray");
            for(int i=0 ,j=itemIdList.size(); i<j ; i++){
                int itemIdTemp = Double.valueOf(itemIdList.get(i)).intValue(); ;
                VoteSelect voteSelect = new VoteSelect();
                voteSelect.setUid(uid);
                voteSelect.setVoteId(voteId);
                voteSelect.setItemId(itemIdTemp);
                ResultInfoEntity resultInfoEntity = voteService.addUserVoteSelect(voteSelect);
                if(!resultInfoEntity.isResultFlag()){
                    logger.warn(resultInfoEntity.getResultInfo());
                    this.failResponse(res,resultInfoEntity.getResultInfo());
                    return;
                }
            }
            map.put("voteResult",voteService.getVoteResult(voteId));
            logger.info(uid+"参与投票成功"+voteId);
            this.successResponse(res,map);
            return;

        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"数据错误，本次投票失败");
        }
    }

    @RequestMapping("/vote/addVoteComment")
    public void addVoteComment(HttpServletRequest req, HttpServletResponse res) {
        try{
            UserBase userBase = this.getUserBase(req);
            if(userBase == null){
                logger.warn(ResultInfo.USER_NO_LOGIN_ERROR);
                this.failResponse(res,ResultInfo.USER_NO_LOGIN_ERROR);
                return;
            }//Validate  user login
            String data = this.getPostData(req);
            Map<String,Object> map = this.getModel(data, Map.class);
            String authCode = this.getString(map,"authCode");
            //验证验证码
            String sessionCaptcha = (String)req.getSession().getAttribute("captcha");
            if(sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(authCode)){
                map.put("result",false);
                map.put("resultMsg", ResultInfo.REG_CAPTCHA_ERROR);
                logger.warn(ResultInfo.REG_CAPTCHA_ERROR);
                this.successResponse(res,map);
                return;
            }
            int voteId = this.getInt(map, "voteId");
            int uid = this.getUserBase(req).getId();
            String comment = this.getString(map,"comment");
            VoteComment voteComment = new VoteComment();
            voteComment.setUid(uid);
            voteComment.setVoteId(voteId);
            voteComment.setCreateTime(new Date());
            voteComment.setComment(comment);
            voteComment.setState(true);
            ResultInfoEntity resultInfoEntity = voteService.addVoteComment(voteComment);
            int voteCommentId = Integer.parseInt(resultInfoEntity.getResultInfo());
            if(voteCommentId >0){
                VoteCommentExtend voteCommentExtend = voteService.getVoteCommentExtendById(voteCommentId);
                map.put("voteCommentExtend",voteCommentExtend);
                logger.info(uid+"添加投票评论成功"+voteId);
                this.successResponse(res,map);
            } else{
                this.failResponse(res,"数据错误，添加投票评论失败");
            }

            return;

        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"数据错误，添加投票评论失败");
        }
    }


    @RequestMapping("/vote/getVoteComment")
    public void getVoteComment(HttpServletRequest req, HttpServletResponse res) {
        try{
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data, Map.class);

            int voteId = this.getInt(map, "voteId");
            int start = this.getInt(map,"start");
            int size = this.getInt(map,"size");
            List<VoteCommentExtend> voteCommentExtendList = voteService.getVoteCommentById(voteId,start ,size);
            map.put("voteCommentList",voteCommentExtendList);
            logger.info("获取投票评论成功"+voteId);
            this.successResponse(res,map);
            return;

        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"数据错误，获取投票评论失败");
        }
    }

    @RequestMapping("/vote/getVoteResult")
    public void getVoteResult(HttpServletRequest req, HttpServletResponse res) {
        try{
            String data = this.getPostData(req);
            Map<String,Object> map = this.getModel(data, Map.class);

            int voteId = this.getInt(map, "voteId");
            map.put("voteResult",voteService.getVoteResult(voteId));
            logger.info("查询投票结果成功:"+voteId);
            this.successResponse(res,map);
            return;

        }catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"数据错误，获取投票结果失败");
        }
    }
}
