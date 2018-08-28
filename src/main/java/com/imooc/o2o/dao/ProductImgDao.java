package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 批量添加商品详情图
	 * 
	 * @param productImg
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImg);
	
	/**
	 * 根据productId查询商品详情图列表
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(Long productId);
	
	/**
	 * 删除详情图----productId
	 * @param productId
	 * @return
	 */
	int deleteProductImg(Long productId);
}
