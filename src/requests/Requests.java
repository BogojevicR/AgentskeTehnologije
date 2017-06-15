package requests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Requests {
	/**
	 * This method is used for all post requests
	 * @param url - location or rest resource
	 * @param jsonObject - object that is going to be send via post request
	 * @return returns string of whatever rest resource returns
	 */
	public String makePostRequest(String url, String jsonString) {
		try{
			
			HttpClient httpClient =  HttpClientBuilder.create().build(); //Use this instead 	
			HttpPost postMethod = new HttpPost(url);
			
			
			if (jsonString == null) {
				return "";
			}
			if (jsonString != null) {
				StringEntity requestEntity = new StringEntity(
					    jsonString,
					    ContentType.APPLICATION_JSON);
				
				postMethod.setEntity(requestEntity);
			}
			
			RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(3000).setConnectTimeout(3000).setConnectionRequestTimeout(3000).build();

			
			postMethod.setConfig(requestConfig);
			
			HttpResponse rawResponse = httpClient.execute(postMethod);
			InputStream inputStream = rawResponse.getEntity().getContent();
		
			
			StringBuilder result = new StringBuilder();  
			
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        result.append(line + "\n");  
		    }
		    br.close();
			
			return result.toString();
		} catch (Exception e) {
			String retVal = "";
			return retVal;
		}

	}
}
