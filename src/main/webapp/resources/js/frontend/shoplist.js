/**
 * 
 */
$(function(){
	var loading=false;
	//分页允许返回的最大条数,超过则禁止访问后台
	var maxItems=999;
	
	//一页返回的最大条数
	var pageSize=10;
	
	//返回店铺列表
	var listUrl="/o2o/frontend/listshops"
		
	//页码,从第一页开始
	var pageIndex=1;
	
	var shopCategoryId='';
	var shopName='';
	var areaId='';
	
	//出书话区域和店铺类别信息
	var parentId=getQueryString("parentId");
	var listShopsPageInfo="/o2o/frontend/listshopspageinfo?parentId="+parentId;
	getAreaWithShopCategory();
	addItem(pageIndex,pageSize);
	function getAreaWithShopCategory(){
		$.getJSON(listShopsPageInfo,function(data){
			if(data.success){
				//店铺类别列表
				var shopCategoryList=data.shopCategoryList;
				var shopCategoryHtml='';
				shopCategoryHtml+='<a href="#" class="button" data-category-id="">全部类别</a>';
				shopCategoryList.map(function(item,index){
					shopCategoryHtml+='<a href="#" class="button" data-category-id='
						+item.shopCategoryId+'>'
						+item.shopCategoryName+'</a>';
				})
				$("#shoplist-search-div").html(shopCategoryHtml);
				//区域列表
				var areaList=data.areaList;
				var areaHtml='';
				areaHtml+='<option value="">全部街道</option>';
				areaList.map(function(item,index){
					areaHtml+='<option value="'
						+item.areaId+'">'
						+item.areaName
						+'</option>';
				})
				$("#area-search").html(areaHtml);
				
			}
		})
	}
	function addItem(pageIndex,pageSize){
		var url=listUrl+'?'+'pageIndex='+pageIndex+'&'
		+'pageSize='+pageSize+'&'
		+'shopCategoryId='+shopCategoryId+'&'
		+'shopName='+shopName+'&'
		+'areaId='+areaId+'&'+'parentId='+parentId;
		loading=true;
		$.getJSON(url,function(data){
			if(data.success){
				//当前查询条件下店铺的总数
				maxItems=data.count;
				//当前查询条件下店铺下的店铺列表
				var shopList=data.shopList;
				var shopHtml='';
				shopList.map(function(item,index){
					shopHtml += '' + '<div class="card" data-shop-id="'
					+ item.shopId + '">' + '<div class="card-header">'
					+ item.shopName + '</div>'
					+ '<div class="card-content">'
					+ '<div class="list-block media-list">' + '<ul>'
					+ '<li class="item-content">'
					+ '<div class="item-media">' + '<img src="'
					+ item.shopImg + '" width="44">' + '</div>'
					+ '<div class="item-inner">'
					+ '<div class="item-subtitle">' + item.shopDesc
					+ '</div>' + '</div>' + '</li>' + '</ul>'
					+ '</div>' + '</div>' + '<div class="card-footer">'
					+ '<p class="color-gray">'
					+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
					+ '更新</p>' + '<span>点击查看</span>' + '</div>'
					+ '</div>';
				})
				$(".list-div").append(shopHtml);
				
				// 获取目前为止已显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				// 若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) {
					// 隐藏提示符
					$('.infinite-scroll-preloader').hide();
				} else {
					//infinite-scroll-preloader
					$('.infinite-scroll-preloader').show();
				}
				// 否则页码加1，继续load出新的店铺
				pageIndex+=1;
				// 加载结束，可以再次加载了
				loading = false;
				// 刷新页面，显示新加载的店铺
				$.refreshScroller();
			}
		})
	}
	$(".shop-list").on('click','.card',function(e){
		var shopId=e.currentTarget.dataset.shopId;
		window.location.href="/o2o/frontend/shopdetail?shopId="+shopId;
	})
	
	// 注册'infinite'事件处理函数
    $(document).on('infinite', '.infinite-scroll-bottom',function() {
    	if(loading){
    		return
    	}
    	// 重置页码
    	pageIndex=1;
    	addItem(pageIndex,pageSize)
    })
    
    
    $("#shoplist-search-div").on('click','.button',function(e){
    	//若parentId有值,则是点击一级类别下的二级类别
    	if(parentId){
    		shopCategoryId=e.target.dataset.categoryId;
    		if($(e.target).hasClass("button-fill")){
    			$(e.target).removeClass("button-fill");
    			shopCategoryId='';
    		}else{
    			$(e.target).addClass("button-fill").siblings().removeClass("button-fill");
    		}
    		
    		// 由于查询条件改变，清空店铺列表再进行查询
			$('.list-div').empty();
			addItem(pageIndex,pageSize)
    	}else{//若点击全部店铺data-category-id
    		parentId=e.target.dataset.categoryId;
    		if($(e.target).hasClass("button-fill")){
    			$(e.target).removeClass("button-fill");
    			parentId='';
    		}else{
    			$(e.target).addClass("button-fill").siblings().removeClass("button-fill");
    		}
    		// 由于查询条件改变，清空店铺列表再进行查询
			$('.list-div').empty();
			// 重置页码
			pageNum = 1;
			addItem(pageIndex,pageSize);
			parentId = '';
    	}
    })
    
    $("#search").on('change',function(e){
    	shopName=e.target.value;
    	$('.list-div').empty();
		// 重置页码
		pageNum = 1;
		addItem(pageIndex,pageSize);
    })
    
    // 区域信息发生变化后，重置页码，清空原先的店铺列表，按照新的区域去查询
	$('#area-search').on('change', function() {
		areaId = $('#area-search').val();
		$('.list-div').empty();
		pageNum = 1;
		addItem(pageIndex,pageSize);
	});
    
    $("#me").click(function(){
    	$.openPanel("#panel-right-demo");
    })
});