package com.qixi.db.mapper;

import com.qixi.db.entity.qq;
import com.qixi.db.entity.qqExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface qqMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int countByExample(qqExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int deleteByExample(qqExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int insert(qq record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int insertSelective(qq record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    List<qq> selectByExample(qqExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    qq selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int updateByExampleSelective(@Param("record") qq record, @Param("example") qqExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int updateByExample(@Param("record") qq record, @Param("example") qqExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int updateByPrimaryKeySelective(qq record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq
     *
     * @mbggenerated Sat Apr 25 11:46:11 CST 2015
     */
    int updateByPrimaryKey(qq record);
}