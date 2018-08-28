package com.imooc.o2o.enums;

/**
 * 
 * @author Administrator
 *
 */
public enum ProductCategoryStateEnum {
	SUCCESS(1, "创建成功"), INNER_ERROR(-1001, "店铺类别操作失败"), EMPTY_LIST(-1002, "添加数少于1"),NULL_CURRENTSHOP(-1003,"currentShop信息为空");
	
	private int state;
	
	private String stateInfo;

	public int getState() {
		return state;
	}
	
	private ProductCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
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
