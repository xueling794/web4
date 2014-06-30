package com.qixi.db.mapper;

import com.qixi.db.entity.AreaInfo;
import com.qixi.db.entity.AreaInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AreaInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int countByExample(AreaInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int deleteByExample(AreaInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int insert(AreaInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int insertSelective(AreaInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    List<AreaInfo> selectByExample(AreaInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    AreaInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int updateByExampleSelective(@Param("record") AreaInfo record, @Param("example") AreaInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int updateByExample(@Param("record") AreaInfo record, @Param("example") AreaInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int updateByPrimaryKeySelective(AreaInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table areainfo
     *
     * @mbggenerated Wed Feb 26 00:22:44 CST 2014
     */
    int updateByPrimaryKey(AreaInfo record);
}