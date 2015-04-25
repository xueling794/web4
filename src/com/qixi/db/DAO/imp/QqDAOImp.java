package com.qixi.db.DAO.imp;

import com.qixi.common.BaseDAO;
import com.qixi.common.Exception.DBException;
import com.qixi.db.DAO.Service.IQqDAO;
import com.qixi.db.entity.qq;
import com.qixi.db.entity.qqExample;
import com.qixi.db.mapper.qqMapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-4-25
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
public class QqDAOImp extends BaseDAO implements IQqDAO{
    public List<qq> geQq() throws DBException {
        try{
            qqMapper qMapper = (qqMapper)this.getMapperClass();
            qqExample qExample = new qqExample();
            return qMapper.selectByExample(qExample);
        }  catch (Exception e) {
            throw new DBException(e.getMessage(), e);
        }
    }
}
