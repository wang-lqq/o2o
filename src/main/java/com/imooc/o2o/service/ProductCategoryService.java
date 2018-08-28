package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 根据店铺ID获取店铺下的商品类别列表
	 * @param shopId
	 * @return
	 */
	ProductCategoryExecution getProductCategoryList(long shopId);
	
	
	/**
	 * 批量添加商品类别
	 * @param ProductCategoryList
	 * @return ProductCategoryExecution
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList);
	/**
	 * 将此类别下的商品的类别Id置为空,在删除该商品类别
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return ProductCategoryExecution
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
