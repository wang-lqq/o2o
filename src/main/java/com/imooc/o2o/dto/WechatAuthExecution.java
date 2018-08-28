package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthEnums;

public class WechatAuthExecution {
	private int state;
	
	private String stateInfo;
	
	private int count;
	
	private WechatAuth wechatAuth;
	
	private List<WechatAuth> wechatAuthList;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public WechatAuth getWechatAuth() {
		return wechatAuth;
	}

	public void setWechatAuth(WechatAuth wechatAuth) {
		this.wechatAuth = wechatAuth;
	}

	public List<WechatAuth> getWechatAuthList() {
		return wechatAuthList;
	}

	public void setWechatAuthList(List<WechatAuth> wechatAuthList) {
		this.wechatAuthList = wechatAuthList;
	}

	public WechatAuthExecution() {
		
	}

	public WechatAuthExecution(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public WechatAuthExecution(int state, WechatAuth wechatAuth, List<WechatAuth> wechatAuthList) {
		this.state = state;
		this.wechatAuth = wechatAuth;
		this.wechatAuthList = wechatAuthList;
	}

	public WechatAuthExecution(WechatAuthEnums wechatEnums, WechatAuth wechatAuth) {
		super();
		this.state = wechatEnums.getState();
		this.stateInfo = wechatEnums.getStateInfo();
		this.wechatAuth = wechatAuth;
	}

	
	
}
