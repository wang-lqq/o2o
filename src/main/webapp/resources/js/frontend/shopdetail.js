/**
 * 
 */
$(function(){
	var loading=false;
	//允许返回的最大条数,超过禁止访问后台
	var maxItems=20;
	//获取商品列表
	var listProductUrl="/o2o/frontend/listproductbyshop"
	//一页返回的商品数
	var pageSize=10;
	//默认页码
	var pageIndex=1;
	//从地址栏获取shopId
	var shopId=getQueryString("shopId");
	//查询商品列表条件
	var productCategoryId='';
	var productName='';
	//获取本店铺信息及店铺下的所有商品类别列表
	var ProdutInfoUrl="/o2o/frontend/shopwithproductcategory?shopId="+shopId
	//渲染出店铺基本信息及该店铺下的所有商品类别列表
	getMessageInfo();
	//
	addItems(pageSize,pageIndex);
	function getMessageInfo(){
		$.getJSON(ProdutInfoUrl,function(data){
			if(data.success){
				//店铺详情
				var shop=data.shop;
				//商品类别列表
				var productCategoryList=data.productCategoryList;
				//渲染店铺详情
				$("#shop-cover-pic").attr('src',shop.shopImg);
				$("#shop-update-time").html(new Date(shop.lastEditTime).Format('yyyy-MM-dd'));
				$("#shop-desc").html(shop.shopDesc);
				$("#shop-addr").html(shop.shopAddr);
				$("#shop-phone").html(shop.phone);
				$("#shop-name").html(shop.shopName);
				var Html='';
				productCategoryList.map(function(item,index){
					Html+='<a href="#" class="button" data-productCategory-id="'
						+item.productCategoryId+'">'
						+item.productCategoryName+'</a>';
				})
				$("#shopdetail-button-div").html(Html);
			}
		})
	}
	function addItems(pageSize,pageIndex){
		//拼接出查询条件url,商品名称或者商品类别查询
		var url=listProductUrl+'?'+'pageSize='+pageSize+'&'
		+'pageIndex='+pageIndex+'&'
		+'shopId='+shopId+'&'+'productName='+productName+'&'
		+'productCategoryId='+productCategoryId
		//设定加载符
		loading=true;
		$.getJSON(url,function(data){
			if(data.success){
				//查询条件下的商品列表(分页)
				var productList=data.productList;
				//同等查询条件下查询的所有商品列表总数
				maxItems=data.count;
				var html='';
				//遍历商品列表
				productList.map(function(item,index){
					html += '' + '<div class="card" data-product-id='
					+ item.productId + '>'
					+ '<div class="card-header">' + item.productName
					+ '</div>' + '<div class="card-content">'
					+ '<div class="list-block media-list">' + '<ul>'
					+ '<li class="item-content">'
					+ '<div class="item-media">' + '<img src="'
					+ item.imgAddr + '" width="44">' + '</div>'
					+ '<div class="item-inner">'
					+ '<div class="item-subtitle">' + item.productDesc
					+ '</div>' + '</div>' + '</li>' + '</ul>'
					+ '</div>' + '</div>' + '<div class="card-footer">'
					+ '<p class="color-gray">'
					+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
					+ '更新</p>' + '<span>点击查看</span>' + '</div>'
					+ '</div>';
				})
				//将卡片集合添加到目标HTML组件里
				$(".list-div").append(html);
				//目前已显示的商品数
				var total=$(".list-div .card").length;
				// 若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) {
					// 隐藏提示符
					$('.infinite-scroll-preloader').hide();
				} else {
					$('.infinite-scroll-preloader').show();
				}
				// 否则页码加1，继续load出新的店铺
				pageIndex += 1;
				// 加载结束，可以再次加载了
				loading = false;
				// 刷新页面，显示新加载的店铺
				$.refreshScroller();
			}
		})
	}
	//点击某个具体商品时
	$(".list-div").on('click','.card',function(e){
		var productId=e.currentTarget.dataset.productId;
		window.location.href="/o2o/frontend/productdetail?productId="+productId;
	})
	
	$(document).on('infinite','.infinite-scroll-bottom',function(){
		if(loading){
			return;
		}
		addItems(pageSize,pageIndex);
	})
	//给每个商品类别绑定click方法
	$("#shopdetail-button-div").on('click','.button',function(e){
		//获取商品类别ID
		productCategoryId=e.currentTarget.dataset.productcategoryId;
		if(productCategoryId){
			if($(e.target).hasClass("button-fill")){
    			$(e.target).removeClass("button-fill");
    			productCategoryId='';
    		}else{
    			$(e.target).addClass("button-fill").siblings().removeClass("button-fill");
    		}
			$(".list-div").empty();
			//页码为1
			pageIndex=1;
			addItems(pageSize,pageIndex);
		}
	})
	//搜索框内容发生改变
	$("#search").on('change',function(e){
		productName=e.target.value;
		$(".list-div").empty();
		//页码为1
		pageIndex=1;
		addItems(pageSize,pageIndex);
	})
	
	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
})