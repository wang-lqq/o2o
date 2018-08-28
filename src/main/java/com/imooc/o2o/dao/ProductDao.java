package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Product;

public interface ProductDao {
	/**
	 * 添加商品
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);
	
	/**
	 * 查询商品列表
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSizse
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/**
	 * 同等查询条件下商品总数
	 * 
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	
	/**
	 * 根据productId查询商品
	 * @param productId
	 * @return
	 */
	Product queryProduct(long productId);
	
	int updateProduct(Product product);
	
	int updateProductCategoryToNull(long productCategoryId);
}
