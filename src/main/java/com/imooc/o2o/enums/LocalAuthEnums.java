package com.imooc.o2o.enums;

import com.imooc.o2o.entity.Product;

public enum LocalAuthEnums {
	LOGINFAIL(-1, "密码或帐号输入有误"), SUCCESS(0, "操作成功"), NULL_AUTH_INFO(-1006,
			"注册信息为空"), ONLY_ONE_ACCOUNT(-1007,"最多只能绑定一个本地帐号"),HAS_EXISTED(-1008,"用户名已存在");
	private LocalAuthEnums(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	private Product product;
	
	private int state;
	
	private String stateInfo;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	private LocalAuthEnums(LocalAuthEnums productEnums, Product product) {
		this.product = product;
		this.state = productEnums.getState();
		this.stateInfo = productEnums.getStateInfo();
	}

	private LocalAuthEnums() {
		
	}
	
	
}
