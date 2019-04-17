package com.bytecodeio.looker.api;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("EmbedUrlService")
public class EmbedUrlServiceImpl implements EmbedUrlService{
	
	public static final String STRING_FORMAT =  "UTF-8";

	@Value("${looker.host}")
	private String lookerHost;
	
	@Value("${looker.models}")
	private String lookerModels;
	
	@Value("${looker.embedkey}")
	private String lookerEmbedKey;
	
	String jsonFormat(String val){
    	return "\""+ val +"\"";
    }

	String getUserAttributes(String accountId){
		if(accountId==null){
			return "{}";
		}
        return "{\"account_id\":\""+ accountId +"\"}";
    }

	String encodeString(String stringToEncode, String secret) throws Exception {
        //System.out.println(stringToEncode + " : " + secret);

        byte[] keyBytes = secret.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = Base64.getEncoder().encode(mac.doFinal(stringToEncode.getBytes("UTF-8")));
        return new String(rawHmac, "UTF-8");
    }

	public String getDashboardEmbedUrl(String firstName, String lastName, String externalUserId, String dashboardId)throws Exception{
		return getEmbedUrl(firstName, lastName, externalUserId, "/embed/dashboards/"+ dashboardId);
	}

	String jsonFormatArray(String val){
    	StringBuilder sb = new StringBuilder();
    	String[] splitValues = val.split(",");
    	for(int x=0;x<splitValues.length;x++){
    		if(x!=0){
    			sb.append(",");
    		}
    		sb.append("\""+ val +"\"");
    	}
    	return "["+ sb.toString() +"]";
    }

	String getEmbedUrl(String firstName, String lastName, String externalUserID, String embedURL)throws Exception{
        //TODO: Externalize permissions
		String permissions = "[\"see_user_dashboards\",\"see_lookml_dashboards\",\"access_data\",\"see_looks\",\"see_drill_overlay\",\"download_without_limit\",\"save_content\"]";
		String sessionLength = "600";
		String accessFilters = "{}";  // converted to JSON Object of Objects
		String groupIDs = "[]"; // converted to JSON array, can be set to null (value, not JSON) for no groups
		String externalGroupID = "\"\"";  // converted to JSON string
		String forceLoginLogout = "true"; // converted to JSON bool
		String userAttributes = getUserAttributes(externalUserID);

		Calendar cal = Calendar.getInstance();
		SecureRandom random = new SecureRandom();
		String nonce = "\"" + (new BigInteger(130, random).toString(32)) + "\"";
		String time = Long.toString(cal.getTimeInMillis() / 1000L);
		String fullEmbedPath = "/login/embed/" + java.net.URLEncoder.encode(embedURL, STRING_FORMAT);
        String userIDFormatted = "\"" + externalUserID.toString() + "\"";  // converted to JSON string
		//models = "[\"popular_names\"]"; // converted to JSON array

		String formattedModels = jsonFormatArray(lookerModels);

		//String urlToSign = formatUrlToSign(Config.CONFIG_HOST, fullEmbedPath, nonce, time, sessionLength, jsonFormat(externalUserID), permissions, jsonFormatArray(models), groupIDs, externalGroupID, userAttributes, accessFilters);
		//String signature = encodeString(urlToSign, Config.CONFIG_EMBED_SECRET_ID);

		//String signedURL = getSignedUrl(nonce, time, sessionLength, jsonFormat(externalUserID), permissions, jsonFormatArray(models), accessFilters, jsonFormat(firstName), jsonFormat(lastName), groupIDs, externalGroupID, userAttributes, forceLoginLogout, signature);


		//models = jsonFormatArray(models);

		String urlToSign = "";
        urlToSign += lookerHost + "\n";
        urlToSign += fullEmbedPath + "\n";
        urlToSign += nonce + "\n";
        urlToSign += time + "\n";
        urlToSign += sessionLength + "\n";
        urlToSign += userIDFormatted + "\n";
        urlToSign += permissions + "\n";
        urlToSign += formattedModels + "\n";
        urlToSign += groupIDs + "\n";
        urlToSign += externalGroupID + "\n";
        urlToSign += userAttributes +"\n";
        urlToSign += accessFilters;

        //String signature = encodeString(urlToSign, Config.getConfig().CONFIG_SECRET_KEY);
        String signature = encodeString(urlToSign, lookerEmbedKey);

		String signedURL = "nonce=" + java.net.URLEncoder.encode(nonce, "UTF-8") +
	            "&time=" + java.net.URLEncoder.encode(time, "UTF-8") +
	            "&session_length=" + java.net.URLEncoder.encode(sessionLength, "UTF-8") +
	            "&external_user_id=" + java.net.URLEncoder.encode(userIDFormatted,"UTF-8") +
	            "&permissions=" + java.net.URLEncoder.encode(permissions, "UTF-8") +
	            "&models=" + java.net.URLEncoder.encode(formattedModels, "UTF-8") +
	            "&group_ids=" + java.net.URLEncoder.encode(groupIDs, "UTF-8") +
	            "&external_group_id=" + java.net.URLEncoder.encode(externalGroupID, "UTF-8") +
	            "&user_attributes=" + java.net.URLEncoder.encode(userAttributes, "UTF-8") +
	            "&access_filters=" + java.net.URLEncoder.encode(accessFilters, "UTF-8") +
	            "&first_name=" + java.net.URLEncoder.encode(jsonFormat(firstName), "UTF-8") +
                "&last_name=" + java.net.URLEncoder.encode(jsonFormat(lastName), "UTF-8") +
                "&force_logout_login=" + java.net.URLEncoder.encode(forceLoginLogout, "UTF-8") +
	            "&signature=" + java.net.URLEncoder.encode(signature, "UTF-8");

        return "https://"+ lookerHost + fullEmbedPath + '?' + signedURL;
	}

}
