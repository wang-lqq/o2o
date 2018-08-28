package com.imooc.o2o.dao;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.PersonInfo;

public class PersonInfoDaoTest extends BaseTest{
	@Autowired
	PersonInfoDao personInfoDao;
	
	
	@Test
	public void testQueryPersonInfoById() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPersonInfo() {
		PersonInfo personInfo=new PersonInfo();
		personInfo.setName("王龙龙");
		personInfo.setProfileImg("https://");
		personInfo.setGender("男");
		personInfo.setEnableStatus(1);
		personInfo.setUserType(1);
		personInfo.setCreateTime(new Date());
		personInfo.setLastEditTime(new Date());
		personInfoDao.addPersonInfo(personInfo);
	}

}
