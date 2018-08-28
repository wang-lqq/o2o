package com.imooc.o2o.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dao.WechatAuthDao;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthEnums;
import com.imooc.o2o.exceptions.WechatAuthOperationException;
import com.imooc.o2o.service.WechatAuthService;
@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	private static final Logger log=LoggerFactory.getLogger(WechatAuthServiceImpl.class);
	@Autowired
	WechatAuthDao wechatAuthDao;
	
	@Autowired
	PersonInfoDao personInfoDao;

	@Override
	public WechatAuth getWechatAuthById(String openId) {
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}
	
	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
		if(wechatAuth==null || wechatAuth.getOpenId()==null) {
			throw new WechatAuthOperationException(WechatAuthEnums.NULL_AUTH_INFO.getStateInfo());
		}
		try {
			wechatAuth.setCreateTime(new Date());
			//如果微信帐号里夹带着用户信息并且用户Id为空，则认为该用户第一次使用平台(且通过微信登录)
			//则自动创建用户信息
			if(wechatAuth.getPersonInfo()!=null && wechatAuth.getPersonInfo().getUserId()==null) {
				try {
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setLastEditTime(new Date());
					wechatAuth.getPersonInfo().setEnableStatus(1);
					PersonInfo personInfo=wechatAuth.getPersonInfo();
					//personInfo会返回user_id
					int effectedNum=personInfoDao.addPersonInfo(personInfo);
					if(effectedNum<=0) {
						throw new WechatAuthOperationException(WechatAuthEnums.FAIL_PERSONINFO.getStateInfo());
					}
					//吧返回的user_id set wechatAuth
					wechatAuth.setPersonInfo(personInfo);
				} catch (Exception e) {
					log.error("insertPersonInfo error:" + e.toString());
					throw new WechatAuthOperationException("insertPersonInfo error: " + e.getMessage());
				}
			}
			int effectedNum=wechatAuthDao.insertWechatAuth(wechatAuth);
			if(effectedNum<=0) {
				throw new WechatAuthOperationException(WechatAuthEnums.FAIL_WECHATAUTH.getStateInfo());
			}else {
				return new WechatAuthExecution(WechatAuthEnums.SUCCESS, wechatAuth);
			}
		} catch (Exception e) {
			log.error("insertWechatAuth error:" + e.toString());
			throw new WechatAuthOperationException("insertWechatAuth error:"+e.getMessage());
		}
	}

}
