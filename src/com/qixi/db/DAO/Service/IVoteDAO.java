package com.qixi.db.DAO.Service;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.*;
import com.qixi.db.entity.extend.VoteCommentExtend;
import com.qixi.db.entity.extend.VoteExtend;
import com.qixi.db.entity.extend.VoteItemExtend;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-7
 * Time: 下午10:08
 * To change this template use File | Settings | File Templates.
 */
public interface IVoteDAO {

    public int createVote(Vote vote) throws BusinessException;

    public ResultInfoEntity changeVoteState(int voteId,byte state ) throws BusinessException;

    public int addVoteSelect(VoteSelect voteSelect) throws  BusinessException;

    public int addVoteComment(VoteComment voteComment) throws  BusinessException;

    public ResultInfoEntity updateVoteComment(VoteComment voteComment) throws BusinessException;

    public List<VoteExtend> getOpenVoteList(int start ,int size) throws BusinessException;

    public List<VoteItemExtend> getVoteResult (int voteId) throws  BusinessException;

    public List<VoteCommentExtend> getVoteComment(int voteId ,int start , int size) throws BusinessException;

    public VoteExtend getVoteExtendById(int voteId) throws BusinessException;

    public int addVoteItem(VoteItem voteItem) throws BusinessException;

    public ResultInfoEntity updateVoteItem(VoteItem voteItem) throws  BusinessException;

    public VoteItem getVoteItemById(int voteItemId) throws BusinessException;

    public Vote getVoteById(int id) throws BusinessException;

    public List<VoteSelect> getVoteSelectByUid(int uid,int voteId) throws BusinessException;

    public List<Vote> getVoteByUid(int uid) throws BusinessException;

    public int getVoteSelectCount( int voteId) throws BusinessException;

    public int getVoteCommentCount(int voteId) throws BusinessException;

    public List<VoteItem> getVoteItemsById(int voteId) throws BusinessException;

    public int addUserVoteSelect(VoteSelect voteSelect) throws BusinessException;





}
