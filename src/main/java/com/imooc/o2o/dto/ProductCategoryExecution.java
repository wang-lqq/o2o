package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;

/**
 * 店铺类别服务执行后返回的状态
 * @author Administrator
 *
 */
public class ProductCategoryExecution {
	private int state;
	
	private String stateInfo;
	
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	private ProductCategory productCategory;
	
	private List<ProductCategory> productCategoryList;
	
	public ProductCategoryExecution() {
		
	}

	public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum) {
		this.state = productCategoryStateEnum.getState();
		this.stateInfo = productCategoryStateEnum.getStateInfo();
	}
	
	public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum,
			List<ProductCategory> productCategoryList) {
		this.state = productCategoryStateEnum.getState();
		this.stateInfo = productCategoryStateEnum.getStateInfo();
		this.productCategoryList = productCategoryList;
	}
	
	public ProductCategoryExecution(int state, String stateInfo, ProductCategory productCategory) {
		this.state = state;
		this.stateInfo = stateInfo;
		this.productCategory = productCategory;
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

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
}
