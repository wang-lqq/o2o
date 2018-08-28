package com.imooc.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.imooc.o2o.enums.ProductEnums;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductImgDao productImgDao;
	
	@Autowired
	ProductDao productDao;
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		//商品不能为null,且商品所属店铺也不为null
		if(product!=null && product.getShop()!=null && product.getShop().getShopId()!=null) {
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			product.setEnableStatus(1);
			if(thumbnail!=null) {
				addThumbnail(product,thumbnail);
				try {
					int effectedNum = productDao.insertProduct(product);
					//商品添加成功
					if(effectedNum>0) {
						if(productImgList==null || productImgList.size()==0) {
							throw new ProductOperationException(ProductEnums.EMPTY_IMG.getStateInfo());
						}
						addProductDetailsImgList(product,productImgList);
						return new ProductExecution(ProductEnums.SUCCESS);
					}else {
						throw new ProductOperationException(ProductEnums.INNER_ERROR.getStateInfo());
					}
				} catch (Exception e) {
					throw new ProductOperationException(ProductEnums.INNER_ERROR.getStateInfo()+e.getMessage());
				}
			}else {
				throw new ProductOperationException(ProductEnums.EMPTY_IMG.getStateInfo());
			}
		}else {
			throw new ProductOperationException("Product为null");
		}
	}
	/**
	 * 批量添加图片
	 * @param product
	 * @param productImgList
	 */
	private void addProductDetailsImgList(Product product, List<ImageHolder> imageHolderList) {
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		for (ImageHolder imageHolder : imageHolderList) {
			//dest===/upload/item/shop/"+shopId+"/
			String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
			String imgAddr = ImageUtil.generateNormalThumbnail(imageHolder,dest);
			ProductImg productImg=new ProductImg();
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImg.setImgAddr(imgAddr);
			productImgList.add(productImg);
		}
		try {
			if(productImgList.size()>0) {
				int effetedNum = productImgDao.batchInsertProductImg(productImgList);
				if(effetedNum<=0) {
					throw new ProductOperationException(ProductEnums.INNER_ERROR.getStateInfo());
				}
			}
		} catch (Exception e) {
			throw new ProductOperationException(ProductEnums.INNER_ERROR.getStateInfo());
		}
	}
	/**
	 * 添加商品缩略图
	 * 
	 * @param shop
	 * @param shopImg
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		//缩略图存储的目录shopId值得目录
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		//存储图片并返回相对路径
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		//向shop set图片相对路径
		product.setImgAddr(shopImgAddr);
	}
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		//将pageIndex 转换为 rowIndex
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList=productDao.queryProductList(productCondition, rowIndex, pageSize);
		//为查询到商品
		if(productList==null) {
			return new ProductExecution(ProductEnums.EMP_PRODUCT);
		}
		int count = productDao.queryProductCount(productCondition);
		if(count==0) {
			return new ProductExecution(ProductEnums.EMP_PRODUCT);
		}
		ProductExecution productExecution = new ProductExecution(ProductEnums.SUCCESS,productList);
		productExecution.setCount(count);
		return productExecution;
	}
	@Override
	public ProductExecution getProductById(Long productId) {
		if(productId<=0 || productId==null) {
			return new ProductExecution(ProductEnums.EMPTY_MESSAGE);
		}else {
			try {
				Product product = productDao.queryProduct(productId);
				if(product==null) {
					return new ProductExecution(ProductEnums.EMP_PRODUCT);
				}
				return new ProductExecution(ProductEnums.SUCCESS,product);
			} catch (Exception e) {
				return new ProductExecution(ProductEnums.ERROR);
			}
		}
		
	}
	@Override
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		//空值判断
		if(product!=null && product.getShop()!=null && product.getShop().getShopId()!=null) {
			//product设置默认值
			product.setLastEditTime(new Date());
			//若传入缩略图,则先删除磁盘缩略图
			if(thumbnail!=null) {
				Product tempProduct=productDao.queryProduct(product.getProductId());
				//原商品存在缩略图,则删除磁盘图片(仅仅删除原缩略图片,并没有持久到数据库)
				if(tempProduct.getImgAddr()!=null) {
					//删除原缩略图
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				//存储商品缩略图,并吧商品缩略图的相对路径set进product
				addThumbnail(product, thumbnail);
			}
			//若上传了商品详情图
			if(productImgList!=null && productImgList.size()>0) {
				//删除商品缩略图,
				deleteProductImgList(product.getProductId());
				addProductDetailsImgList(product,productImgList);
			}
			try {
				int effectedNum=productDao.updateProduct(product);
				if(effectedNum<=0) {
					return new ProductExecution(ProductEnums.UPDATE_ERROR);
				}
				return new ProductExecution(ProductEnums.SUCCESS,product);
			} catch (Exception e) {
				throw new ProductOperationException(e.getMessage());
			}
		}else {
			return new ProductExecution(ProductEnums.EMP_PRODUCT);
		}
	}
	/**
	 * 删除存储在磁盘商品详情图的同时,删除tb_product_img商品详情图记录(通过product_id删除)
	 * @param productId
	 */
	private void deleteProductImgList(Long productId) {
		//通过productId查询productId下的所有商品详情图列表
		List<ProductImg>  productImgList=productImgDao.queryProductImgList(productId);
		for (ProductImg productImg : productImgList) {
			//商品详情图的相对路径
			String storePath=productImg.getImgAddr();
			//删除磁盘上的商品详情图
			ImageUtil.deleteFileOrPath(storePath);
		}
		productImgDao.deleteProductImg(productId);
		
	}
}
