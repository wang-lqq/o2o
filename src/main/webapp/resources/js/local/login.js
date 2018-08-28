/**
 * 
 */
$(function(){
	var loginCount=0;
	//用户登录验证的url
	var loginUrl="/o2o/local/logincheck";
	//从地址栏里获取usertype
	var usertype=getQueryString("usertype");
	var loginCount=0;
	$("#submit").click(function(){
		var username=$("#username").val();
		var password=$("#psw").val();
		var verifyCodeActual=$("#j_captcha").val();
		// 是否需要验证码验证，默认为false,即不需要
		var needVerify = false;
		if(loginCount>=3){
			if(!verifyCodeActual){
				$.toast('请输入验证码！');
				return;
			}else{
				needVerify=true;
			}
		}
		$.ajax({
			url:loginUrl,
			type:'POST',
			async:false,
			cache:false,
			dataType:'json',
			data:{
				userName:username,
				password:password,
				verifyCodeActual:verifyCodeActual,
				needVerify:needVerify
			},
			success:function(data){
				if(data.success){
					$.toast('登录成功！');
					if (usertype == 1) {
						// 若用户在前端展示系统页面则自动链接到前端展示系统首页
						window.location.href = '/o2o/frontend/index';
					} else if(usertype=2){
						// 若用户是在店家管理系统页面则自动链接到店铺列表页中
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				}else{
					$.toast('登录失败！'+data.errMsg);
					loginCount++;
					if(loginCount>=3){
						$("#verifyPart").show();
					}
				}
			}
			
		})
		
	})
})