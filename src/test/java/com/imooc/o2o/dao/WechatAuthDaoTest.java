package com.imooc.o2o.dao;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.WechatAuth;

public class WechatAuthDaoTest extends BaseTest{
	@Autowired
	WechatAuthDao wechatAuthDao;
	
	@Test
	public void testQueryWechatInfoByOpenId() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertWechatAuth() {
	}

}
