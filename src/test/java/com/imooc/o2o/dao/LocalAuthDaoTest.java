package com.imooc.o2o.dao;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;

public class LocalAuthDaoTest extends BaseTest{
	@Autowired
	LocalAuthDao localAuthDao;
	@Test
	public void testQueryLocalByUserNameAndPwd() {
		LocalAuth localAuth=localAuthDao.queryLocalByUserNameAndPwd("wll", "234");
		System.out.println(localAuth);
	}

	@Test
	public void testQueryLocalByUserId() {
		LocalAuth localAuth=localAuthDao.queryLocalByUserId(1l);
		
		System.out.println(localAuth);
		
	}

	@Test
	public void testInsertLocalAuth() {
		LocalAuth localAuth=new LocalAuth();
		localAuth.setCreateTime(new Date());
		localAuth.setLastEditTime(new Date());
		localAuth.setUsername("wll");
		localAuth.setPassword("234");
		PersonInfo personInfo=new PersonInfo();
		personInfo.setUserId(1l);
		personInfo.setEnableStatus(1);
		personInfo.setUserType(1);
		localAuth.setPersonInfo(personInfo);
		int count=localAuthDao.insertLocalAuth(localAuth);
	}

	@Test
	public void testUpdateLocalAuth() {
		localAuthDao.updateLocalAuth(1l, "wll", "234", "2345", new Date());
	}
	
	@Test
	public void testVerification() {
		int effectedNum=localAuthDao.verification("wlll");
		System.out.println(effectedNum);
	}
}
