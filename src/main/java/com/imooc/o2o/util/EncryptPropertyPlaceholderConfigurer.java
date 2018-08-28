package com.imooc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	// 需要加密的字段数组
	private String[] encryptPropNames = { "jdbc.username", "jdbc.password" };
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(isEncryptProp(propertyName)) {
			String decryptValue=DESUtil.getDecryptString(propertyValue);
			return decryptValue;
		}else {
			return propertyValue;
		}
	}
	/**
	 * 该属性是否已加密
	 * 
	 * @param propertyName
	 * @return
	 */
	private boolean isEncryptProp(String propertyName) {
		for (String encryptpropertyName : encryptPropNames) {
			if(encryptpropertyName.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}
	
	
}
