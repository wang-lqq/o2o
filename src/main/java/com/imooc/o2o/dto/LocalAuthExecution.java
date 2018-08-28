package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthEnums;

public class LocalAuthExecution {
	

	//结果状态
	private int state;
	
	//状态描述
	private String stateInfo;
	
	//店铺数量
	private int count;
	
	//操作的Shop店铺(增删改店铺时用到)
	private LocalAuth localAuth;
	
	//shop列表(查询店铺列表时用到
	private List<LocalAuth> localAuthList;
	
	/**
	 * 店铺操作失败使用的构造器
	 * 
	 * @param localAuthEnums
	 */
	public LocalAuthExecution(LocalAuthEnums localAuthEnums) {
		this.state = localAuthEnums.getState();
		this.stateInfo = localAuthEnums.getStateInfo();
	}
	
	/**
	 * 店铺操作成功使用的构造器
	 * @param localAuthEnums
	 * @param localAuth
	 */
	public LocalAuthExecution(LocalAuthEnums localAuthEnums,LocalAuth localAuth) {
		this.state = localAuthEnums.getState();
		this.stateInfo = localAuthEnums.getStateInfo();
		this.localAuth=localAuth;
	}
	
	/**
	 * 店铺操作成功使用的构造器
	 * @param localAuthEnums
	 * @param localAuthList
	 */
	public LocalAuthExecution(LocalAuthEnums localAuthEnums,List<LocalAuth> localAuthList) {
		this.state = localAuthEnums.getState();
		this.stateInfo = localAuthEnums.getStateInfo();
		this.localAuthList=localAuthList;
	}
	
	public LocalAuthExecution() {
	}

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

	public LocalAuth getLocalAuth() {
		return localAuth;
	}

	public void setLocalAuth(LocalAuth localAuth) {
		this.localAuth = localAuth;
	}

	public List<LocalAuth> getLocalAuthList() {
		return localAuthList;
	}

	public void setLocalAuthList(List<LocalAuth> localAuthList) {
		this.localAuthList = localAuthList;
	}
}
