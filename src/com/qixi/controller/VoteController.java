package com.qixi.controller;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IUserService;
import com.qixi.business.service.IVoteService;
import com.qixi.common.BaseController;
import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.VoteItem;
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
            String data = this.getData(req);
            Map<String,Object> map = this.getModel(data,Map.class);
            int uid = this.getUserBase(req).getId();
            String voteTitle =  this.getString(map,"title");
            String voteRemark =  this.getString(map,"remark");
            String endDateStr = this.getString(map, "endDate");
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
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
                    int voteCommentCount = voteService.getVoteCommentCOunt(voteId);

                    voteExtendList.get(i).setVoteCount(voteSelectCount);
                    voteExtendList.get(i).setCommentCount(voteCommentCount);
                }
            }


            map.put("voteList",voteExtendList);
            this.successResponse(res,map);
            return;
        }catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            this.failResponse(res,"获取投票信息失败");
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            this.failResponse(res,"获取投票信息失败");
        }

        /*VoteExtend voteExtend = new VoteExtend();
        voteExtend.setUid(uid);*/

    }
}
