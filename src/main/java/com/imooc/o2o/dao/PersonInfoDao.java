package com.imooc.o2o.dao;
/**
 * 用户DAO
 * @author Administrator
 *
 */

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoDao {
	/**
	 * 用户ID查询用户信息
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);
	
	/**
	 * 添加用户信息
	 * @param personInfo
	 * @return
	 */
	int addPersonInfo(PersonInfo personInfo);
}
