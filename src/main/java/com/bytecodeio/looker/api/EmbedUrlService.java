package com.bytecodeio.looker.api;

public interface EmbedUrlService {
	public String getDashboardEmbedUrl(String firstName, String lastName, String externalUserId, String dashboardId)throws Exception;
}
