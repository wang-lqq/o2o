/**
 * 
 */
$(function(){
	//shopmanagement.html显示时自动加载shopmanagement.js
	//获取URL中的参数shopId=13;
	var shopId=getQueryString("shopId");//若url:/o2o/shopadmin/shopmanagement?shopI时则shopId获取不到
	var shopInfoUrl="/o2o/shopadmin/getshopmanagementinfo?shopId="+shopId+"";
	$.getJSON(shopInfoUrl,function(data){
		//data.redirect=true重定向到shoplist.html
		if(data.redirect){
			window.location.href=data.url;
		}else{
			if(data.shopId!=undefined && data.shopId!=null){
				shopId=data.shopId;
			}
			$("a:contains('商铺信息')").attr('href','/o2o/shopadmin/shopoperation?shopId='+shopId+'');
			$("a:contains('商品管理')").attr('href','/o2o/shopadmin/productmanagement?shopId='+shopId+'');
		}
	})
})
