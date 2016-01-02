package se.ltu.netprog.javaprog.sma;

public class WeatherClient {
	public static void main(String[] args) {
		//System.out.println("please inter <country> <city> ");
		
		String host = "52.11.95.241";
		int port = WeatherService.WEATHER_SERVICE_PORT;
		String city , country ;
		try {
			country = args[0];
		} catch(Exception e) {
			country = "se";
		}
		try {
			city = args[1];
		} catch(Exception e) {
			city = "lulea";
		}
		MessageClient conn;
		try {
			conn = new MessageClient(host,port);
		} catch(Exception e) {
			System.err.println(e);
			return;
		}
		Message m = new Message();
		m.setType(WeatherService.WEATHER_SERVICE_MESSAGE);
		m.setParam("City",city);
		m.setParam("Country",country);
		m = conn.call(m);
		System.out.println(m.getParam("Weather"));
		conn.disconnect();
	}
}
