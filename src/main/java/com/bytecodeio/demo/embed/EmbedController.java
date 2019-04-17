package com.bytecodeio.demo.embed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@RequestMapping("/looker")
public class EmbedController {

	@Autowired
	DashboardService dashboardService;
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, value="/looker/dashboard/{dashboardId}", produces = "text/plain")
	public ResponseEntity getDashboardEmbedUrl(@PathVariable String dashboardId)throws Exception{
		System.out.println("Servicing request for dashboard "+ dashboardId);
		String response = dashboardService.getDashboardEmbedUrl(dashboardId);
		System.out.println(response);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
}
