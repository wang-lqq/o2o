package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
/**
 * 商品列表服务
 * @author Administrator
 *
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	ProductCategoryDao productCategoryDao;
	
	@Autowired
	ProductDao productDao;
	
	@Override
	public ProductCategoryExecution getProductCategoryList(long shopId) {
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategoryList(shopId);
		return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS,productCategoryList);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> ProductCategoryList) 
		throws ProductCategoryOperationException{
		if(ProductCategoryList!=null && ProductCategoryList.size()>0) {
			try {
				int insertCount = productCategoryDao.batchInsertProductCategory(ProductCategoryList);
				if(insertCount>0) {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS,ProductCategoryList);
				}else {//影响了0行
					throw new ProductCategoryOperationException(ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("error"+e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
		
	}

	@Override
	@Transactional//两个sql 语句操作
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//TODO 商品类别的下的商品的类别ID置为null
		try {
			productDao.updateProductCategoryToNull(productCategoryId);
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum>0){
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}else {
				throw new ProductCategoryOperationException("删除店铺类别失败");
			}
			
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error"+e.getMessage());
		}
	}

}
