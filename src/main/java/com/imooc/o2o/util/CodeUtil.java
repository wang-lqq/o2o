package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//图片验证码的值
		String verifyCodeExpected = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//用户输入值
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		System.out.println(verifyCodeExpected+"---"+verifyCodeActual);
		if(verifyCodeActual==null || !verifyCodeExpected.equals(verifyCodeActual)) {
			return false;
		}
		return true;
	}

}
