package com.qixi.db.DAO.Service;

import com.qixi.common.Exception.DBException;
import com.qixi.db.entity.qq;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-4-25
 * Time: 下午12:00
 * To change this template use File | Settings | File Templates.
 */
public interface IQqDAO {
    public List<qq> geQq() throws DBException;
}
