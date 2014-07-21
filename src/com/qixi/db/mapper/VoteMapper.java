package com.qixi.db.mapper;

import com.qixi.db.entity.Vote;
import com.qixi.db.entity.VoteExample;
import com.qixi.db.entity.extend.VoteExtend;
import com.qixi.db.entity.extend.VoteItemExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VoteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int countByExample(VoteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int deleteByExample(VoteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int insert(Vote record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int insertSelective(Vote record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    List<Vote> selectByExample(VoteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    Vote selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int updateByExampleSelective(@Param("record") Vote record, @Param("example") VoteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int updateByExample(@Param("record") Vote record, @Param("example") VoteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int updateByPrimaryKeySelective(Vote record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table vote
     *
     * @mbggenerated Sun Jul 13 17:35:52 CST 2014
     */
    int updateByPrimaryKey(Vote record);

    List<VoteExtend>  getOpenVoteList(Map<String, Object> parameterMap);

    List<VoteItemExtend>  getVoteSelectStat(Map<String, Object> parameterMap);
}