package com.qixi.db.entity;

public class VoteItem {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column voteitem.id
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column voteitem.voteId
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    private Integer voteId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column voteitem.connent
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    private String connent;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column voteitem.id
     *
     * @return the value of voteitem.id
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column voteitem.id
     *
     * @param id the value for voteitem.id
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column voteitem.voteId
     *
     * @return the value of voteitem.voteId
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    public Integer getVoteId() {
        return voteId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column voteitem.voteId
     *
     * @param voteId the value for voteitem.voteId
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column voteitem.connent
     *
     * @return the value of voteitem.connent
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    public String getConnent() {
        return connent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column voteitem.connent
     *
     * @param connent the value for voteitem.connent
     *
     * @mbggenerated Mon Jul 07 22:03:04 CST 2014
     */
    public void setConnent(String connent) {
        this.connent = connent == null ? null : connent.trim();
    }
}