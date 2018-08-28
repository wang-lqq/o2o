package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/local")
public class LocalController {
	/**
	 * 账号绑定路由
	 * @return
	 */
	@RequestMapping("/accountbind")
	public String accountbind() {
		return "local/accountbind";
	}
	
	/**
	 * 修改密码路由
	 * @return
	 */
	@RequestMapping("/changepsw")
	public String changepsw() {
		return "local/changepsw";
	}
	
	/**
	 * 登录
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		return "local/login";
	}
}
