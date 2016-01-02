package se.ltu.netprog.javaprog.sma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OpenWeatherMap {
	private String city    ;
	private String country  ;
	private StringBuilder url;
	private HttpClient client;
	private JSONObject mainObjext;
	private JSONObject weatherObject;
	private String weather;
	private double temperature ; 
	private final static String OPEN_WEATHER_API_KEY = "3dd0e5bb8aee599c66a6d054f958a967";
	
	
	public OpenWeatherMap(){
		this("Lulea", "se");
	}
	public OpenWeatherMap(String city, String country){
		this.city    = city;
		this.country = country;	
		url = new StringBuilder();
		// Build the Url
		// http://api.openweathermap.org/data/2.5/find?q=Lulea,se&units=metric,APPID=3dd0e5bb8aee599c66a6d054f958a967
		url.append("http://api.openweathermap.org/data/2.5/find?q=")
					 .append(this.city)
					 .append(",")
					 .append(this.country)
					 .append("&")
					 .append("units=metric")
					 .append("&")
					 .append("APPID="+OPEN_WEATHER_API_KEY);
	}
	public String getWeather() throws ClientProtocolException, IOException{
		client = HttpClients.custom().build();
		// Get the weather
		HttpUriRequest get = RequestBuilder
				.get()
				.setUri(url.toString())
				.setHeader(HttpHeaders.ACCEPT, "application/json")
				.build();
		HttpResponse response = client.execute(get);
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		 
		StringBuilder result = new StringBuilder();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		try {
			JSONObject JSONresponse  = new JSONObject(result.toString());
			JSONArray  listArray     = JSONresponse.getJSONArray("list");
			JSONObject listObject 	 = listArray.getJSONObject(0);
			JSONArray  weatherArray  = listObject.getJSONArray("weather");
			
			mainObjext    = listObject.getJSONObject("main");
			weatherObject = weatherArray.getJSONObject(0);
			weather       = (String) weatherObject.get("main");
			temperature   = mainObjext.getDouble("temp");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//System.out.println(weatherObject);
		//System.out.println(mainObjext);
		//System.out.println("OpenWeatherMap response : "+response.getStatusLine().getStatusCode());
		//System.out.println("Weather status  : "+weather);
		//System.out.println("Temperature     : "+temperature);
		return weather + " " +Double.toString(temperature);
	}
}
