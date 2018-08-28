/**
 * 
 */
$(function(){
	//店铺修改
	var editShopUrl="/o2o/shopadmin/modifyshop";
	//获取店铺初始化信息,包括店铺区域和店铺类别
	var initUrl="/o2o/shopadmin/getshopinitinfo";
	//注册店铺
	var submitUrl="/o2o/shopadmin/registershop";
	// 从URL里获取shopId参数的值
	var shopId = getQueryString('shopId');
	//根据店铺ID获取店铺信息和店铺区域
	var shopInfoUrl="/o2o/shopadmin/getShopById?shopId="+shopId;
	var isEdit = shopId?true:false;
	//若传入shopId=?,则进行修改店铺操作
	if(isEdit){
		getShopInfo();
	}else{//添加店铺操作
		getShopInitInfo();
	}
	//据shopId获取店铺信息
	function getShopInfo(){
		$.getJSON(shopInfoUrl,function(data){
			if(data.success){
				//添加店铺区域列表
				var tempAreaHtml='';
				data.areaList.map(function(item,index){
					tempAreaHtml+='<option value="'+item.areaId+'">'+item.areaName+'</option>'
				})
				$("#shop-area").html(tempAreaHtml)
				//默认选中店铺区域
				$("#shop-area").find("option[value='"+data.shop.area.areaId+"']").attr("selected",true);
				//添加店铺类别
				var shopCategory = '<option value="'+data.shop.shopCategory.shopCategoryId+'">'+data.shop.shopCategory.shopCategoryName+'</option>';
				$('#shop-category').html(shopCategory);
				//不允许修改店铺类别
				$('#shop-category').attr('disabled','disabled');
				$("#shop-name").val(data.shop.shopName);
				$("#shop-name").attr('disabled','disabled');
				$("#shop-addr").val(data.shop.shopAddr);
				$("#shop-phone").val(data.shop.phone);
				$("#shop-desc").val(data.shop.shopDesc);
			}
		})
	};
	function getShopInitInfo(){
		$.getJSON(initUrl,function(data){
			if(data.success){
				var tempHtml="";
				var tempAreaHtml="";
				data.areaList.map(function(item,index){
					tempAreaHtml+='<option value="'+item.areaId+'">'+item.areaName+'</option>';
				});
				data.shopCategoryList.map(function(item,index){
					tempHtml+='<option value="'+item.shopCategoryId+'">'+item.shopCategoryName+'</option>';
				});
				$("#shop-category").html(tempHtml);
				$("#shop-area").html(tempAreaHtml);
			}
		});
	}
	$("#submit").click(function(){
		var formData = new FormData();
		var shop={};
		//若属于修改店铺
		if(isEdit){
			shop.shopId=shopId;
		}
		shop.shopName = $("#shop-name").val();
		shop.shopAddr = $("#shop-addr").val();
		shop.phone = $("#shop-phone").val();
		shop.shopDesc = $("#shop-desc").val();
		shop.shopCategory ={
				shopCategoryId:$("#shop-category").val()
		}
		shop.area ={
				areaId:$("#shop-area").val()
		}
		//获取页面文件流
		var shopImg = $("#shop-img")[0].files[0];
		//获取验证码输入框
		var verifyCodeActual = $("#j_captcha").val();
		if(!verifyCodeActual){
			$.toast("请输入验证码");
			return;
		}
		formData.append("shopStr",JSON.stringify(shop));
		formData.append("shopImg",shopImg);
		formData.append("verifyCodeActual",verifyCodeActual);
		$.ajax({  
			type:"POST",
			url:(isEdit?editShopUrl:submitUrl),
			data:formData,
			processData : false,  //必须false才会避开jQuery对 formdata 的默认处理   
	        contentType : false,  //必须false才会自动加上正确的Content-Type 
			cache:false,
			success:function(data){
				if(data.success){
					$.toast("提交成功");
				}else{
					$.toast("提交失败");
				}
				$("#captcha_img").click();
			}
		});
	});
})