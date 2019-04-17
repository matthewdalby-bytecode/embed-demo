package com.bytecodeio.demo.embed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytecodeio.looker.api.EmbedUrlService;

@Service("DashboardService")
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired
	private EmbedUrlService embedUrlService;
	
	public String getDashboardEmbedUrl(String dashboardId)throws Exception{
		User user = getCurrentUser();
		return embedUrlService.getDashboardEmbedUrl(user.firstName, user.lastName, user.externalUserId, dashboardId);
	}
	
	User getCurrentUser(){
		User user = new User();
		user.firstName = "Jack";
		user.lastName = "Sparrow";
		user.externalUserId = "1234567890";
		return user;
	}
	
	class User{
		String firstName;
		String lastName;
		String externalUserId;
		
	}
}
