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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Requests {

	public String makePostRequest(String url, String jsonString) {
		try{			
			HttpClient httpClient =  HttpClientBuilder.create().build(); //Use this instead 	
			HttpPost request = new HttpPost(url);
			
			if (jsonString == null) {
				return "";
			}
			if (jsonString != null) {
				StringEntity requestEntity = new StringEntity(
					    jsonString,
					    ContentType.APPLICATION_JSON);
				
				request.setEntity(requestEntity);
			}
			
			HttpResponse rawResponse = httpClient.execute(request);
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
	
	public String makeGetRequest(String url) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
	
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (Exception e) {
			String retVal = "";
			return retVal;
		}

	}
	
	public void makePutRequest(String url) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPut request = new HttpPut(url);
			client.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void makeDeleteRequest(String urlString) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpDelete request = new HttpDelete(urlString);
			client.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
