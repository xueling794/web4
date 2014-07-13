package com.qixi.db.DAO.imp;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.BaseDAO;
import com.qixi.common.Exception.BusinessException;
import com.qixi.common.constant.ResultInfo;
import com.qixi.db.DAO.Service.IVoteDAO;
import com.qixi.db.entity.*;
import com.qixi.db.entity.extend.VoteCommentExtend;
import com.qixi.db.entity.extend.VoteExtend;
import com.qixi.db.entity.extend.VoteItemExtend;
import com.qixi.db.mapper.VoteCommentMapper;
import com.qixi.db.mapper.VoteItemMapper;
import com.qixi.db.mapper.VoteMapper;
import com.qixi.db.mapper.VoteSelectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-7
 * Time: 下午11:19
 * To change this template use File | Settings | File Templates.
 */
public class VoteDAOImp extends BaseDAO implements IVoteDAO {
    @Override
    public int createVote(Vote vote) throws BusinessException {
        try{


            VoteMapper voteMapper = (VoteMapper) this.getMapperClass(VoteMapper.class);
            int insertId = voteMapper.insert(vote);
            return insertId;
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }




    }

    @Override
    public ResultInfoEntity changeVoteState(int voteId, byte state) throws BusinessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int addVoteSelect(VoteSelect voteSElect) throws BusinessException {
        try{
            VoteSelectMapper voteSelectMapper = (VoteSelectMapper) this.getMapperClass(VoteSelectMapper.class);
            int insertId = voteSelectMapper.insert(voteSElect);
            return insertId;
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public int addVoteComment(VoteComment voteComment) throws BusinessException {
        try{
            VoteCommentMapper voteCommentMapper = (VoteCommentMapper) this.getMapperClass(VoteCommentMapper.class);
            int insertId = voteCommentMapper.insert(voteComment);
            return insertId;
        } catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public ResultInfoEntity updateVoteComment(VoteComment voteComment) throws BusinessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<VoteExtend> getOpenVoteList(int start, int size) throws BusinessException {
        VoteMapper voteMapper = (VoteMapper) this.getMapperClass(VoteMapper.class);
        Map<String ,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("start",start);
        paramMap.put("size",size);
        return voteMapper.getOpenVoteList(paramMap);
    }

    @Override
    public List<VoteItemExtend> getVoteResult(int voteId) throws BusinessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<VoteCommentExtend> getVoteComment(int voteId, int start, int size) throws BusinessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public VoteExtend getVoteExtendById(int voteId) throws BusinessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public VoteExtend getVoteById(int voteId) throws BusinessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<VoteSelect> getVoteSelectByUid(int uid, int voteId) throws BusinessException {
        try{
            VoteSelectMapper voteSelectMapper = (VoteSelectMapper) this.getMapperClass(VoteSelectMapper.class);
            VoteSelectExample voteSelectExample = new VoteSelectExample();
            VoteSelectExample.Criteria criteria = voteSelectExample.createCriteria();
            criteria.andUidEqualTo(uid);
            criteria.andVoteIdEqualTo(voteId);
            return voteSelectMapper.selectByExample(voteSelectExample);
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }

    }

    @Override
    public List<Vote> getVoteByUid(int uid) throws BusinessException {
        try{
            VoteMapper voteMapper = (VoteMapper) this.getMapperClass(VoteMapper.class);
            VoteExample voteExample = new VoteExample();
            VoteExample.Criteria criteria = voteExample.createCriteria();
            criteria.andUidEqualTo(uid);
            return voteMapper.selectByExample(voteExample);
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public int addVoteItem(VoteItem voteItem) throws BusinessException {
        try{

            VoteItemMapper voteItemMapper = (VoteItemMapper) this.getMapperClass(VoteItemMapper.class);
            int insertId = voteItemMapper.insert(voteItem);
            return insertId;
            /*if(insertId <0){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_ADD_ITEM_ERROR);
                return resultInfoEntity;
            }else{
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(insertId+"");
                return resultInfoEntity;
            }*/
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public ResultInfoEntity updateVoteItem(VoteItem voteItem) throws BusinessException {
        try{

            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            VoteItemMapper voteItemMapper = (VoteItemMapper) this.getMapperClass(VoteItemMapper.class);

            int insertId = voteItemMapper.updateByPrimaryKey(voteItem);

            if(insertId <0){
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(ResultInfo.VOTE_UPDATE_ITEM_ERROR);
                return resultInfoEntity;
            }else{
                resultInfoEntity.setResultFlag(false);
                resultInfoEntity.setResultInfo(insertId+"");
                return resultInfoEntity;
            }
        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }

    @Override
    public VoteItem getVoteItemById(int id) throws BusinessException {
        try{

            ResultInfoEntity resultInfoEntity = new ResultInfoEntity();
            VoteItemMapper voteItemMapper = (VoteItemMapper) this.getMapperClass(VoteItemMapper.class);
            VoteItemExample voteItemExample = new VoteItemExample();
            VoteItemExample.Criteria criterion = voteItemExample.createCriteria();
            criterion.andIdEqualTo(id);
            List<VoteItem> voteItemList=  voteItemMapper.selectByExample(voteItemExample);
            if(voteItemList == null || voteItemList.size()<1){
                return null;
            }else{
                return voteItemList.get(0);
            }

        }catch(Exception e){
            throw new BusinessException (e.getMessage() ,e);
        }
    }
}
