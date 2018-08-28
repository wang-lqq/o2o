package com.imooc.o2o.enums;

import com.imooc.o2o.entity.Product;

public enum ProductEnums {
	SUCCESS(1,"添加成功"),INNER_ERROR(-1000,"商品图片添加失败"),EMPTY_IMG(-1002,"商品图片为null"),EMP_PRODUCT(-1003,"商品信息为空"),EMPTY_MESSAGE(-1004,"信息为空"),
	ERROR(-1005,"操作失败"),UPDATE_ERROR(-1006,"更新商品失败");
	private ProductEnums(int state, String stateInfo) {
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

	private ProductEnums(ProductEnums productEnums, Product product) {
		this.product = product;
		this.state = productEnums.getState();
		this.stateInfo = productEnums.getStateInfo();
	}

	private ProductEnums() {
		
	}
	
	
}
