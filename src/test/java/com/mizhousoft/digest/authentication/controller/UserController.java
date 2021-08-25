package com.mizhousoft.digest.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mizhousoft.commons.web.ActionResponse;

/**
 * TestController
 * 
 * @version
 */
@RestController
public class UserController
{
	@RequestMapping(value = "/user.action", method = RequestMethod.GET)
	public ActionResponse test()
	{
		ActionResponse response = new ActionResponse();
		response.setOkey(true);

		return response;
	}
}
