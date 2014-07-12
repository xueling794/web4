package com.qixi.business.imp;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.business.service.IVoteService;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.constant.ResultInfo;
import com.qixi.common.constant.VoteConst;
import com.qixi.db.DAO.Service.IUserDAO;
import com.qixi.db.DAO.Service.IVoteDAO;
import com.qixi.db.entity.*;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-12
 * Time: 下午8:57
 * To change this template use File | Settings | File Templates.
 */
public class VoteServiceImp implements IVoteService{
    private IVoteDAO voteDAO ;
    private IUserDAO userDAO ;


    public IUserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public IVoteDAO getVoteDAO() {
        return voteDAO;
    }

    public void setVoteDAO(IVoteDAO voteDAO) {
        this.voteDAO = voteDAO;
    }

    @Override
    public ResultInfoEntity createVote(VoteExtend voteExtend) throws BusinessException {
        try{
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            if(voteExtend.getVoteItemList() == null || voteExtend.getVoteItemList().size()< VoteConst.VOTE_ITEM_MIN || voteExtend.getVoteItemList().size()>VoteConst.VOTE_ITEM_MAX ){
                resultInfoEntity.setResultFlag(false);
                String resultMsg = voteExtend.getVoteItemList().size() > VoteConst.VOTE_ITEM_MAX ?  ResultInfo.VOTE_ITEM_SIZE_MORE : ResultInfo.VOTE_ITEM_SIZE_LESS ;
                resultInfoEntity.setResultInfo(resultMsg);
                return resultInfoEntity;

            }
            if(voteExtend.getMaxItem() <1 || voteExtend.getMaxItem() >(VoteConst.VOTE_ITEM_MAX -1)){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_MAX_ITEM_ERROR);
                return resultInfoEntity;
            }

            if(StringUtils.isEmpty(voteExtend.getTitle())){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_TITLE_EMPTY_ERROR);
                return resultInfoEntity;
            }
            if(StringUtils.isEmpty(voteExtend.getRemark())){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_REMARK_EMPTY_ERROR);
                return resultInfoEntity;
            }
            Vote vote = new Vote();
            vote.setTitle(voteExtend.getTitle());
            vote.setRemark(voteExtend.getRemark());
            vote.setMaxItem(voteExtend.getMaxItem());
            vote.setEndDate(voteExtend.getEndDate());
            vote.setUid(voteExtend.getUid());
            vote.setCreateDate(new Date());
            int voteId = voteDAO.createVote(vote);
            if(voteId <0){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_ADD_EXCEPTION);
                return resultInfoEntity;
            }
            for(int i=0 , j=voteExtend.getVoteItemList().size(); i<j; i++){
                   VoteItem voteItemTemp = new VoteItem();
                   int voteItemId = voteDAO.addVoteItem(voteItemTemp);
                if(voteItemId <0){
                    resultInfoEntity.setResultFlag(false);
                    resultInfoEntity.setResultInfo(ResultInfo.VOTE_ADD_ITEM_ERROR);
                    return resultInfoEntity;
                }
            }
            resultInfoEntity.setResultFlag(true);
            resultInfoEntity.setResultInfo(voteId+"");
            return resultInfoEntity;
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public ResultInfoEntity addVoteSelect(VoteSelect voteSelect) throws BusinessException {
         try{
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            int uid = voteSelect.getUid();
            int voteId = voteSelect.getVoteId();
            List<VoteSelect> voteSelectList = voteDAO.getVoteSelectByUid(uid,voteId);
            if(voteSelectList != null && voteSelectList.size()>0){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_ADD_SELECT_DUPLICATE);
                return resultInfoEntity;
            }
            int voteSelectId = voteDAO.addVoteSelect(voteSelect);
            if(voteSelectId <0){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_ADD_SELECT_ERROR);
                return resultInfoEntity;
            }else{
                resultInfoEntity.setResultFlag(true);
                resultInfoEntity.setResultInfo(voteSelectId+"");
                return resultInfoEntity;
            }

        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public List<VoteSelect> getVoteSelectByUid(int uid, int voteId) throws BusinessException {
        try{
            return voteDAO.getVoteSelectByUid(uid,voteId);
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public List<Vote> getVoteByUid(int uid) throws BusinessException {
        try{
            return voteDAO.getVoteByUid(uid);
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public List<VoteCommentExtend> getVoteCommentById(int voteId, int start, int size) throws BusinessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResultInfoEntity addVoteComment(VoteComment voteComment) throws BusinessException {
        try{
            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            int voteCommentId = voteDAO.addVoteComment(voteComment);
            if(voteCommentId<0){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_ADD_COMMENT_ERROR);
                return resultInfoEntity;
            }else{
                resultInfoEntity.setResultFlag(true);
                resultInfoEntity.setResultInfo(voteCommentId+"");
                return resultInfoEntity;
            }
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }


}
