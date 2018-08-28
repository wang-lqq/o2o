package com.imooc.o2o.service;

import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
	/**
	 * WechatAuth
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthById(String openId);
	
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}
