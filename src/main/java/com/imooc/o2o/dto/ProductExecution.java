package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.enums.ProductEnums;
import com.imooc.o2o.entity.Product;

public class ProductExecution {
		//结果状态
		private int state;
		
		//状态描述
		private String stateInfo;
		
		//单个商品
		private Product product;
		
		//商品列表
		private List<Product> productList;
		
		//商品数量
		private int count;

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

		public List<Product> getProductList() {
			return productList;
		}

		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public ProductExecution() {
		}

		public ProductExecution(ProductEnums productEnums) {
			this.state = productEnums.getState();
			this.stateInfo = productEnums.getStateInfo();
		}

		public ProductExecution(ProductEnums productEnums, Product product) {
			this.state = productEnums.getState();
			this.stateInfo = productEnums.getStateInfo();
			this.product = product;
		}

		public ProductExecution(ProductEnums productEnums, List<Product> productList) {
			this.state = productEnums.getState();
			this.stateInfo = productEnums.getStateInfo();
			this.productList = productList;
		}
		
		
		
}
