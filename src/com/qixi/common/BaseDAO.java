package com.qixi.common;

import org.apache.ibatis.session.SqlSession;

public class BaseDAO<T> {

	private SqlSession sqlSession;
	
	private Class<T> mapper;
	
	public void setMapper(Class<T> mapper) {
		this.mapper = mapper;
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public <T1> T1 getMapperClass(Class<T1> clazz) {
		return sqlSession.getMapper(clazz);
	}
	
	public T getMapperClass() {
		return sqlSession.getMapper(this.mapper);
	}
}
