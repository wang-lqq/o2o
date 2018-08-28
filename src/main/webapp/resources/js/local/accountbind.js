$(function() {
	//绑定帐号的controller url
	var verification = '/o2o/local/verification';
	//验证用户名是否存在
	var bindUrl = '/o2o/local/bindlocalauth';
	// 从地址栏的URL里获取usertype
	// usertype=1则为前端展示系统,其余为店家管理系统
	var usertype = getQueryString('usertype');
	$('#submit').click(function() {
		// 获取输入的帐号
		var userName = $('#username').val();
		// 获取输入的密码
		var password = $('#psw').val();
		// 获取输入的验证码
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		// 访问后台，绑定帐号
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('绑定成功！');
					if (usertype == 1) {
						// 若用户在前端展示系统页面则自动退回到前端展示系统首页
						window.location.href = '/o2o/frontend/index';
					} else {
						// 若用户是在店家管理系统页面则自动回退到店铺列表页中
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('提交失败！' + data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
	$("#username").blur(function(){
		var username=$(this).val();
		if(username==""){
			$("#usernameMessage").hide().html('<label class="label label-danger">用户名不能为空</label>').show(300);
	     }else{
	         // $.ajax方法实现
	         $.ajax({
	             url:verification,
	             type:"get",
	             data:{"username":username},
	             dataType:"json",
	             async:false,
	             success:function(data){
	            	 if(data.success){
	            		 $("#usernameMessage").hide().html("用户名可用").show(300);
	            	 }else{
	            		 $("#usernameMessage").hide().html(data.errMsg).show(300);
	            	 }
	             },
	         });
	     }
	})
	$("#username").focus(function(){
   	 $("#usernameMessage").empty();
    });
});