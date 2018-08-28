package com.imooc.o2o.enums;

public enum WechatAuthEnums {
	SUCCESS(1,"添加成功"),NULL_AUTH_INFO(-1000,"wechatAuth is null"),FAIL_PERSONINFO(-1002,"添加用户失败"),
	FAIL_WECHATAUTH(-1003,"创建账号失败");
	private WechatAuthEnums(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
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
}
