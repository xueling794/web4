package com.qixi.business.service;

import com.qixi.business.model.ResultInfoEntity;
import com.qixi.common.Exception.BusinessException;
import com.qixi.db.entity.*;
import com.qixi.db.entity.extend.VoteCommentExtend;
import com.qixi.db.entity.extend.VoteExtend;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-12
 * Time: 下午8:56
 * To change this template use File | Settings | File Templates.
 */
public interface IVoteService {

    public ResultInfoEntity createVote(VoteExtend voteExtend)throws BusinessException;

    public ResultInfoEntity addVoteSelect(VoteSelect voteSelect) throws BusinessException;

    public List<VoteSelect> getVoteSelectByUid(int uid , int voteId) throws BusinessException;

    public List<Vote> getVoteByUid(int uid) throws BusinessException;

    public List<VoteCommentExtend> getVoteCommentById(int voteId ,int start , int size) throws BusinessException;

    public ResultInfoEntity addVoteComment(VoteComment voteComment) throws BusinessException;

    public List<VoteExtend> getActiveVoteInfo(int start ,int size) throws BusinessException;

    public int getVoteSelectCount(int voteId) throws BusinessException;

    public int getVoteCommentCOunt(int voteId) throws BusinessException;
}
