package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 添加商品
	 * 1.商品
	 * 2.商品缩略图
	 * 3.商品详情图片列表
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
				throws ProductOperationException;
	
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
	
	/**
	 * 通过商品Id查询唯一的商品信息
	 * 
	 * @param productId
	 * @return
	 */
	ProductExecution getProductById(Long productId);
	
	/**
	 * 修改商品
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgLis
	 * @return
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;
}
	
