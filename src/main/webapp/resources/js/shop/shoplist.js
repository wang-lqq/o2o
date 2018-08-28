/**
 * 
 */
$(function(){
	getList();
	function getList(){
		//异步获取店铺列表和店铺所有者用户PersonInfo user
		var listUrl ="/o2o/shopadmin/getshoplist";
		$.getJSON(listUrl,function(data){
			if(data.success){
				handleList(data);
				handleUser(data.user);
			}
		})
	}
	//渲染店铺列表到页面shoplist.html
	function handleList(data){
		var shopList = data.shopList;
		var trShopInfo = '';
		shopList.map(function(item,index){
			trShopInfo+='<div class="row row-product-category now">'+
			'<div class="col-33 product-category-name">'+item.shopName+'</div>'+
			'<div class="col-33">'+shopStatus(item.enableStatus)+'</div>'+
			'<div class="col-33">'+goShop(item.enableStatus, item.shopId)+'</div></div>';
		})
		console.log(trShopInfo);
		$(".category-wrap").append(trShopInfo);
	}
	//给shoplist.html设置用户名
	function handleUser(user){
		$("#user-name").text(user.name);
	}
	//将店铺状态转化为文字
	function shopStatus(enableStatus){
		if(enableStatus==0){
			return "审核中";
		}else if(enableStatus==-1){
			return "店铺非法";
		}else if(enableStatus==1){
			return "审核通过";
		}
	}
	function goShop(enableStatus,shopId){
		//店铺状态为审核通过时,则运行进行店铺的编辑
		if(enableStatus==1){
			return '<a href="/o2o/shopadmin/shopmanagement?shopId='+shopId+'">进入</a>';
		}else{
			return "";
		}
	}
})
