package com.imooc.o2o.enums;

public enum ShopStateEnum {
	CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"),NULL_SHOP(-1003,"shop信息为空"),INNER_ERROR(-1004,"错误");
	
	/**
	 * 使用CHECK(0, "审核中")来构造ShopStateEnum
	 * 
	 * @param state
	 * @param stateInfo
	 */
	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	/**
	 * 根据传入的状态ID返回状态描述
	 * @param state
	 * @return
	 */
	public static ShopStateEnum stateOf(int state) {
		for (ShopStateEnum shopStateEnum : ShopStateEnum.values()) {
			if(state==shopStateEnum.getState()) {
				return shopStateEnum;
			}
		}
		return null;
	}
	
	//状态
	private int state;
	
	//状态描述
	private String stateInfo;

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
}
