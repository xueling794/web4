package com.qixi.db.DAO.Service;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.Exception.DBException;
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

    public int createVote(Vote vote) throws DBException;

    public ResultInfoEntity changeVoteState(int voteId,byte state ) throws DBException;

    public int addVoteSelect(VoteSelect voteSelect) throws  DBException;

    public int addVoteComment(VoteComment voteComment) throws  DBException;

    public ResultInfoEntity updateVoteComment(VoteComment voteComment) throws DBException;

    public List<VoteExtend> getOpenVoteList(int start ,int size) throws DBException;

    public List<VoteItemExtend> getVoteResult (int voteId) throws  DBException;

    public List<VoteCommentExtend> getVoteComment(int voteId ,int start , int size) throws DBException;

    public VoteExtend getVoteExtendById(int voteId) throws DBException;

    public int addVoteItem(VoteItem voteItem) throws DBException;

    public ResultInfoEntity updateVoteItem(VoteItem voteItem) throws  DBException;

    public VoteItem getVoteItemById(int voteItemId) throws DBException;

    public Vote getVoteById(int id) throws DBException;

    public List<VoteSelect> getVoteSelectByUid(int uid,int voteId) throws DBException;

    public List<Vote> getVoteByUid(int uid) throws DBException;

    public int getVoteSelectCount( int voteId) throws DBException;

    public int getVoteCommentCount(int voteId) throws DBException;

    public List<VoteItem> getVoteItemsById(int voteId) throws DBException;

    public int addUserVoteSelect(VoteSelect voteSelect) throws DBException;

    public VoteCommentExtend getVoteCommentById(int voteCommentId) throws DBException;




}
