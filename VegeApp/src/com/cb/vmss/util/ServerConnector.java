package com.cb.vmss.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

@SuppressWarnings("deprecation")
public class ServerConnector {
	String TAG="ServerConnector";
   
	public JSONObject getServerResponse(String hostUrl){
		try {
			URL url=new URL(hostUrl);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			//conn.setDoOutput(true);
			
			InputStream is=conn.getInputStream();
			BufferedReader rd=new BufferedReader(new InputStreamReader(is));
			
			StringBuffer response = new StringBuffer(); 
			String line;
			while((line=rd.readLine())!=null){
				 response.append(line);
			}
			rd.close();
			
			return new JSONObject(response.toString());
						
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.i(TAG,e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(TAG,e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public JSONArray getServerResponseArray(String hostUrl){
		//String host_url=HOST_URL+operationName;
		try {
			URL url=new URL(hostUrl);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			
			InputStream is=conn.getInputStream();
			BufferedReader rd=new BufferedReader(new InputStreamReader(is));
			
			StringBuffer response = new StringBuffer(); 
			String line;
			while((line=rd.readLine())!=null){
				 response.append(line);
			}
			rd.close();
			
			return new JSONArray(response.toString());
						
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.i(TAG,e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(TAG,e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	

	public JSONObject getServerResponse1(String hostUrl){
		String response = null;
		//String host_url=HOST_URL+operationName;
		HttpClient httpClient;
		HttpGet httpGet=new HttpGet(hostUrl);
		httpClient=new DefaultHttpClient();
		
		try {
			HttpResponse httpResponse=httpClient.execute(httpGet);
			
			int statusCode =httpResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
		    	   }else{
			    	response=EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
			    	Log.d("Data in Response", response);
			    	return new JSONObject(response);
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e(TAG, response);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, response);
		}
		return null;
	}
	
	
	public JSONObject getProductList(String hostUrl){
		String response=null;
		HttpClient httpClient=null;
		 try {
			 
//			 String urls=hostURL+operationName;
//			 String loginstr=Constant.AUTH_USERID+":"+Constant.AUTH_PASSWORD; 
//			 String encodedCredentials="Basic "+ Base64.encodeBytes(loginstr.getBytes());
			 
			 httpClient=new DefaultHttpClient();
			 HttpPost httpPost=new HttpPost(hostUrl);
			 //httpPost.setHeader("Authorization",encodedCredentials);
			 
			 //StringEntity entity=new StringEntity(params.toString());
			 //entity.setContentType("application/json");
			 //httpPost.setEntity(entity);
			 
			 HttpResponse httpResponse=httpClient.execute(httpPost);
			 
			 int statusCode =httpResponse.getStatusLine().getStatusCode();
			 
			 if (statusCode == HttpStatus.SC_OK) {
				 response=EntityUtils.toString(httpResponse.getEntity());
				 Log.d("Return Data:",response);
				 return new JSONObject(response.toString());
			  }
//			 else{
//				  response="false";
//		          Log.d("ServerConnector","status code false");
//		          Log.d("Return Data:",response);
//			    }
			 
		} catch (Exception e) {
			 Log.d("Error: ",e.getMessage());
			 e.printStackTrace();
		}
		return null;
	}
	
	
	
	public int submitOrder(String hostUrl,JSONObject params){
		int statusCode=0;
		
		String line;
	  	URL url;
	  	try {
	        url = new URL(hostUrl);   
	    } catch (MalformedURLException e) {
	        throw new IllegalArgumentException("invalid url : " + hostUrl);
	    }
	    String body = params.toString();
	    //String loginstr=Constant.AUTH_USERID+":"+Constant.AUTH_PASSWORD; 
	    //String encodedCredentials="Basic "+ Base64.encodeBytes(loginstr.getBytes());
	    //System.out.println("[ Authorization ]" + encodedCredentials);
	    HttpURLConnection conn = null;	    
	    try {
	    	
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setDoOutput(true);
	         conn.setDoInput(true);
	         conn.setUseCaches(false);
	         conn.setRequestProperty("Content-length",String.valueOf (body.length()));
	         conn.setRequestProperty("content-type","application/json; charset=utf-8"); 
	       //  conn.setRequestProperty ("Authorization",encodedCredentials);
	         conn.setRequestMethod("POST");
	      
	         
	         //send datat
	         OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	         writer.write(body);
	         writer.flush();
			 writer.close();
	         
			 statusCode = conn.getResponseCode();
	         
//	         if (statusCode != 200) {
//	              throw new IOException("Post failed with error code " + statusCode);
//	            }else{
//	            	 InputStream is = conn.getInputStream();
//	            	 
//	                 BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//	                 StringBuffer response = new StringBuffer(); 
//	                 while((line = rd.readLine()) != null) {
//	                   response.append(line);
//	                   response.append('\r');
//	                 }
//	                 rd.close();
//	                 Log.d("Data......",response.toString());            
//	            }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	    
	    return statusCode;
	    
	 }

	
	public JSONObject getTodayOrderInfo(String hostUrl){
		int statusCode=0;
        JSONObject responseObj=null;
		String line;
	  	URL url;
	  	try {
	        url = new URL(hostUrl);   
	    } catch (MalformedURLException e) {
	        throw new IllegalArgumentException("invalid url : " + hostUrl);
	    }
	    
	    HttpURLConnection conn = null;	    
	    try {
	    	
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setDoOutput(true);
	         conn.setDoInput(true);
	         conn.setUseCaches(false);
	        // conn.setRequestProperty("Content-length",String.valueOf (body.length()));
	         conn.setRequestProperty("content-type","application/json; charset=utf-8"); 
	       //  conn.setRequestProperty ("Authorization",encodedCredentials);
	         conn.setRequestMethod("POST");
	         
			 statusCode = conn.getResponseCode();
	         
	         if (statusCode != 200) {
	              //throw new IOException("Post failed with error code " + statusCode);
	        	 responseObj=null;
	            }else{
	            	 InputStream is = conn.getInputStream();
	            	 
	                 BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	                 StringBuffer response = new StringBuffer(); 
	                 while((line = rd.readLine()) != null) {
	                   response.append(line);
	                   response.append('\r');
	                 }
	                 rd.close();
	                 responseObj=new JSONObject(response.toString());
	                 Log.d("Data......",response.toString());            
	            }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	    return responseObj;
	 }
	
	
	public JSONObject getDataFromServer(String hostUrl,String parameter){
		 JSONObject responseObj=null;
		 
		int statusCode=0;
		String line;
	  	URL url;
	  	try {
	        url = new URL(hostUrl);   
	    } catch (MalformedURLException e) {
	        throw new IllegalArgumentException("invalid url : " + hostUrl);
	    }
	    //String body = parameter.toString();
	    HttpURLConnection conn = null;	    
	    try {
	    	
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setDoOutput(true);
	         conn.setDoInput(true);
	         conn.setUseCaches(false);
	         //conn.setRequestProperty("Content-length",String.valueOf (body.length()));
	         conn.setRequestProperty("content-type","application/x-www-form-urlencoded; charset=utf-8"); 
	         conn.setRequestMethod("POST");
	      
	         //send datat
	         OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	         writer.write(parameter);
	         writer.flush();
			 writer.close();
	         
			 statusCode = conn.getResponseCode();
	         
	         if (statusCode != 200) {
	              throw new IOException("Post failed with error code " + statusCode);
	            }else{
	            	 InputStream is = conn.getInputStream();
	            	 
	                 BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	                 StringBuffer response = new StringBuffer(); 
	                 while((line = rd.readLine()) != null) {
	                   response.append(line);
	                   response.append('\r');
	                 }
	                 rd.close();
	                 responseObj=new JSONObject(response.toString());
	                 Log.d("Data......",response.toString());            
	            }
	         
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	    
	    return responseObj;
	    
	 }
	public JSONObject submitOrder(String hostUrl,String parameter){
		 JSONObject responseObj=null;
		 
		int statusCode=0;
		String line;
		String responsStr="";
		URL url;
	  
	  	try {
	        url = new URL(hostUrl);   
	    } catch (MalformedURLException e) {
	        throw new IllegalArgumentException("invalid url : " + hostUrl);
	    }
	    //String body = parameter.toString();
	    HttpURLConnection conn = null;	    
	    try {
	    	
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setDoOutput(true);
	         conn.setDoInput(true);
	         conn.setUseCaches(false);
	         //conn.setRequestProperty("Content-length",String.valueOf (body.length()));
	         conn.setRequestProperty("content-type","application/x-www-form-urlencoded; charset=utf-8"); 
	         conn.setRequestMethod("POST");
	      
	         //send datat
	         OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	         writer.write(parameter);
	         writer.flush();
			 writer.close();
	         
			 statusCode = conn.getResponseCode();
	         
	         if (statusCode != 200) {
	              throw new IOException("Post failed with error code " + statusCode);
	            }else{
	            	 InputStream is = conn.getInputStream();
	            	 
	                 BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	                 StringBuffer response = new StringBuffer(); 
	                 while((line = rd.readLine()) != null) {
	                   response.append(line);
	                   response.append('\r');
	                 }
	                 rd.close();
	                 
	                 if(response.toString().contains("Warning: Invalid argument supplied for foreach()")){
	                	 int startPosition=response.indexOf("{");
	                	 responsStr=response.substring(startPosition);
	                	 
	                 }else{
	                	 responsStr=response.toString();
	                 }
	                 responseObj=new JSONObject(responsStr.toString());
	                 Log.d("Data......",response.toString());            
	            }
	         
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	    
	    return responseObj;
	    
	 }
	
	
	
}
