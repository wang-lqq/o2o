package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.enums.ProductEnums;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	//最大商品详情图上传数量
	private static final int IMAGEMAXCOUNT = 6;
	
	@RequestMapping(value="/addproduct",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//判断验证码
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		//接收前端参数的变量的初始化,包括商品,商品缩略图,商品详情图列表实体类
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		//商品信息JSON字符串
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		//商品缩略图
		ImageHolder thumbnail=null;
		//商品详情图列表
		List<ImageHolder> productImageHolderList=new ArrayList<ImageHolder>();
		//从request.getSession()获取到文件流
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			//判断request是否有请求的文件流
			if (commonsMultipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImageHolderList);
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		try {
			product=mapper.readValue(productStr, Product.class);
			//从Session里获取currentShop,若为null则从定向到shoplist.html
			Object object=request.getSession().getAttribute("currentShop");
			if(object==null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
				return modelMap;
			}
			Shop currentShop =(Shop)object;
			product.setShop(currentShop);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		if(product!=null && thumbnail!=null && productImageHolderList.size()>0) {
			try {
				ProductExecution productExecution =productService.addProduct(product, thumbnail, productImageHolderList);
				if(productExecution.getState()==ProductEnums.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","请输入商品信息");
		}
		return modelMap;
	}
	/**
	 * 
	 * @param request
	 * @param thumbnail
	 * @param productImageHolderList
	 * @return
	 * @throws IOException
	 */
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail,
			List<ImageHolder> productImageHolderList) throws IOException {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		//构建商品缩略图ImageHolder  thumbnailFile可能为null
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
		if(thumbnailFile!=null) {
			thumbnail=new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
		}
		//详情图最大上传6张图片
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			//"productImg"+i 前端约定好的key
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
			if(productImgFile!=null) {
				ImageHolder productImg = new ImageHolder(productImgFile.getInputStream(), productImgFile.getOriginalFilename());
				productImageHolderList.add(productImg);
			}else {
				break;
			}
		}
		return thumbnail;
	}
	
	@RequestMapping(value = "getproductlistbyshop",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProductListByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		//从Session里获取currentShop,若为null则从定向到shoplist.html
		Object object=request.getSession().getAttribute("currentShop");
		if(object==null) {
			modelMap.put("redirect", true);
			modelMap.put("url", "/o2o/shopadmin/shoplist");
			return modelMap;
		}
		Shop currentShop =(Shop)object;
		if((pageIndex>-1) && (pageSize>-1) && currentShop!=null && currentShop.getShopId()!=null) {
			//商品类别ID
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
			//商品名
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = compactProductCondition(currentShop,productCategoryId,productName);
			ProductExecution productExecution=productService.getProductList(productCondition, pageIndex, pageSize);
			if(productExecution.getState()==ProductEnums.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("count", productExecution.getCount());
				modelMap.put("productList",  productExecution.getProductList());
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ProductEnums.EMP_PRODUCT.getState());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg",ProductEnums.EMPTY_MESSAGE.getState());
			return modelMap;
		}
		return modelMap;
	}
	/**
	 * 返回currentShop
	 * @param currentShop
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductCondition(Shop currentShop, Long productCategoryId, String productName) {
		Product productCondition = new Product();
		//将currentShop添加进productCondition
		productCondition.setShop(currentShop);
		//将商品类比添加进productCondition
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(productCategoryId);
		productCondition.setProductCategory(productCategory);
		//将商品名称添加进productCondition
		productCondition.setProductName(productName);
		return productCondition;
	}
	@RequestMapping(value="getproductbyid")
	@ResponseBody
	public Map<String, Object> getProductById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		//从Session里获取currentShop,若为null则从定向到shoplist.html
		Object object=request.getSession().getAttribute("currentShop");
		if(object==null) {
			modelMap.put("redirect", true);
			modelMap.put("url", "/o2o/shopadmin/shoplist");
			return modelMap;
		}
		Shop currentShop =(Shop)object;
		if(productId>-1) {
			try {
				ProductExecution productExecution = productService.getProductById(productId);
				ProductCategoryExecution productCategoryExecution= productCategoryService.getProductCategoryList(currentShop.getShopId());
				modelMap.put("product",productExecution.getProduct());
				modelMap.put("productcategorylist",productCategoryExecution.getProductCategoryList());
				modelMap.put("success", true);
				return modelMap;
			} catch (Exception e) {
				modelMap.put("success", false);
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			return modelMap;
		}
		
	}
	
	
	@RequestMapping(value = "modifyproduct",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
		//Long productId = HttpServletRequestUtil.getLong(request, "productId");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//判断验证码
		if(!CodeUtil.checkVerifyCode(request) && !HttpServletRequestUtil.getBoolean(request, "statusChange")) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		//接收前端参数的变量的初始化,包括商品,商品缩略图,商品详情图列表实体类
		ObjectMapper mapper=new ObjectMapper();
		Product product=null;
		//商品信息JSON字符串
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		//商品缩略图
		ImageHolder thumbnail=null;
		//商品详情图列表
		List<ImageHolder> productImageHolderList=new ArrayList<ImageHolder>();
		//从request.getSession()获取到文件流
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			//判断request是否有请求的文件流
			if (commonsMultipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImageHolderList);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		try {
			product=mapper.readValue(productStr, Product.class);
			//product.setProductId(productId);
			//从Session里获取currentShop,若为null则从定向到shoplist.html
			Object object=request.getSession().getAttribute("currentShop");
			if(object==null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
				return modelMap;
			}
			Shop currentShop =(Shop)object;
			product.setShop(currentShop);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		if(product!=null) {
			try {
				ProductExecution productExecution =productService.modifyProduct(product, thumbnail, productImageHolderList);
				if(productExecution.getState()==ProductEnums.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productExecution.getState());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","请输入商品信息");
		}
		return modelMap;
	}
	
}
