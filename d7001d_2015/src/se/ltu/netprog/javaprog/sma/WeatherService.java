package se.ltu.netprog.javaprog.sma;

import java.io.IOException;


public class WeatherService implements Deliverable {

	public static final int WEATHER_SERVICE_MESSAGE = 222;
	public static final int WEATHER_SERVICE_PORT = 2222;
	private OpenWeatherMap mWeather; 
	@Override
	public Message send(Message m) {
		String city = m.getParam("City");
		String country = m.getParam("Country");
		
		if(city == null){ city = "lulea"; }
		if(country == null){ country = "se"; }
		
		mWeather = new OpenWeatherMap(city , country);
		StringBuilder str = new StringBuilder();
		String temp = null;
		try {
			temp = mWeather.getWeather();
		} catch (IOException e) {
			e.printStackTrace();
		}
		str.append("Weather at ")
			.append(city)
			.append(" ")
			.append(temp);
		m.setParam("Weather", str.toString());
		return m;
	}
	public static void main(String args[]) {
		
		WeatherService ws = new WeatherService();
		MessageServer ms;
		try {
			ms = new MessageServer(WEATHER_SERVICE_PORT);
		} catch(Exception e) {
			System.err.println("Could not start service " + e);
			return;
		}
		Thread msThread = new Thread(ms);
		ms.subscribe(WEATHER_SERVICE_MESSAGE, ws);
		msThread.start();
	}
}
