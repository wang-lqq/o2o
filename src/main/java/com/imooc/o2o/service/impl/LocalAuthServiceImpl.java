package com.imooc.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthEnums;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.MD5;
@Service
public class LocalAuthServiceImpl implements LocalAuthService {
	@Autowired
	LocalAuthDao localAuthDao;
	
	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(userName,MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}
	/**
	 * 将用户信息与平台帐号绑定
	 */
	@Override
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		if(localAuth==null || localAuth.getPersonInfo()==null || localAuth.getPassword()==null ||
				localAuth.getPersonInfo().getUserId()==null || localAuth.getUsername()==null) {
			return new LocalAuthExecution(LocalAuthEnums.NULL_AUTH_INFO);
		}else {
			LocalAuth tempAuth=localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
			//该用户已经绑定了用户名和密码
			if(tempAuth!=null) {
				return new LocalAuthExecution(LocalAuthEnums.ONLY_ONE_ACCOUNT);
			}
			try {
				localAuth.setCreateTime(new Date());
				localAuth.setLastEditTime(new Date());
				//密码进行MD5加密
				localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
				int effectedNum=localAuthDao.insertLocalAuth(localAuth);
				if(effectedNum<=0) {
					throw new LocalAuthOperationException("绑定平台账号失败");
				}else {
					return new LocalAuthExecution(LocalAuthEnums.SUCCESS,localAuth);
				}
			} catch (Exception e) {
				throw new LocalAuthOperationException("errMsg"+e.getMessage());
			}
		}
	}

	@Override
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException {
		if(userId!=null && username!=null && password!=null && newPassword!=null && !password.equals(newPassword)) {
			try {
				int effectedNum=localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password),  MD5.getMd5(newPassword), new Date());
				if(effectedNum<=0) {
					throw new LocalAuthOperationException("修改密码失败");
				}else {
					return new LocalAuthExecution(LocalAuthEnums.SUCCESS);
				}
			} catch (Exception e) {
				throw new LocalAuthOperationException("修改密码失败");
			}
		}else {
			return new LocalAuthExecution(LocalAuthEnums.NULL_AUTH_INFO);
		}
	}

	@Override
	public int verification(String username) {
		return localAuthDao.verification(username);
	}

}
