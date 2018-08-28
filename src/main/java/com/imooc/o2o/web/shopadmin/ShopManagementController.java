package com.imooc.o2o.web.shopadmin;

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
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	ShopService shopService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	ShopCategoryService shopCategoryService;
	
	/**
	 * 注册店铺
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		ObjectMapper mapper=new ObjectMapper();
		//判断验证码
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		Shop shop =null;
		//接收参数,shop shopImg
		//jsonStr--null
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		try {
			shop= mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {//判断request是否有上传的文件流
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			//shopImg前端约定的变量
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		//添加店铺
		if(shop!=null && shopImg!=null) {
			//用户登录时,保存用户信息在session中
			PersonInfo owner =(PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			// -1.不可用 0.审核中 1.可用,注册默认为0
			shop.setEnableStatus(0);
			//CommonsMultipartFile--shopImg
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getInputStream(), shopImg.getOriginalFilename());
				ShopExecution se = shopService.addShop(shop, imageHolder);
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
					//第二次添加店铺信息
					if(shopList==null || shopList.size()==0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
					return modelMap;
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "添加店铺失败");
					return modelMap;
				}
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	//初始化店铺类别和区域
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String,Object>();
		List<Area> areaList=null;
		List<ShopCategory> shopCategoryList=null;
		try {
			areaList = areaService.getAreaList();
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			modelMap.put("success", true);
			modelMap.put("areaList", areaList);
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch (Exception e) {
			modelMap.put("success",false );
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	@RequestMapping(value="getShopById",method=RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//  url=/o2o/shopadmin/getShopById/shopId=1
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId>-1) {
			//只要涉及数据库操作,则就进行try catch
			try {
				Shop shop = shopService.getShopById(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("success", true);
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				return modelMap;
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "shopId is empty");
			return modelMap;
		}
	}
	/**
	 * 修改店铺
	 * @param request
	 * @return
	 */
	@RequestMapping(value="modifyshop",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyshop(HttpServletRequest request) {
		ImageHolder thumbnail = new ImageHolder();
		Map<String,Object> modelMap = new HashMap<String,Object>();
		ObjectMapper mapper=new ObjectMapper();
		//判断验证码
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		Shop shop =null;
		//接收参数,shop shopImg
		//jsonStr--null
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		try {
			shop= mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {//判断request是否有上传的文件流
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			//shopImg前端约定的变量
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		//修改店铺,用户可能没用传入图片,不是必须的,修改时前端会传入shopId
		if(shop!=null && shop.getShopId()!=null) {
			ShopExecution se=null;
			//CommonsMultipartFile--shopImg
			try {
				
				if(shopImg == null) {
					se = shopService.modifyShop(shop, thumbnail);
				}else {
					
					thumbnail.setImage(shopImg.getInputStream());
					thumbnail.setImageName(shopImg.getOriginalFilename());
					se = shopService.modifyShop(shop, thumbnail);
				}
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					return modelMap;
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg",se.getStateInfo());
					return modelMap;
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	/**
	 * 获取店铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getshoplist",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
		try {
			//默认查询用户ID为1的用户创建的店铺
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition,0, 100);
			modelMap.put("shopList", se.getShopList());
			// 列出店铺成功之后，将店铺放入session中作为权限验证依据，即该帐号只能操作它自己的店铺
			request.getSession().setAttribute("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	/**
	 * 店铺管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0) {//传入的shopId错误
			//直接通过url访问时
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj==null) {
				//重定向到/o2o/shopadmingetshoplist
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop)currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			//shopId从URL路径里获取的shopId=1
			currentShop.setShopId(shopId);
			//在shoplist.html点击进入时,会在session里设置一个店铺信息
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
			modelMap.put("shopId", shopId);
		}
		return modelMap;
	}
	
	
}
