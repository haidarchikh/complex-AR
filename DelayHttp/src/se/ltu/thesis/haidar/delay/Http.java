package se.ltu.thesis.haidar.delay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Http {
	public static int delayCount = 0;
	public static int throughputCount = 0;
	public static final int PORT = 50500;
	public static final String CPU = "cpu";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/delay", new MyDelayHandler());
        server.createContext("/throughput", new MyThroughputHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server is connected to port :"+ PORT);
    }

    public static class MyDelayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
        	delayCount++;
        	String res = "";
            Headers h = t.getResponseHeaders();
            h.add("Content-Type", "text/plain");
            try {
				 res = String.valueOf(getProcessCpuLoad());
				 h.add(CPU, res);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        	// add response header
            t.sendResponseHeaders(200, res.length());
            OutputStream os = t.getResponseBody();
            os.write(res.getBytes());
            os.close();
            System.out.println("Got "+ delayCount +" delay requests");
        }
    }
    public static class MyThroughputHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
        	throughputCount++;
        	
        	// get the file
            File file = new File ("/100meg.test");
            byte [] bytearray  = new byte [(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);
            
            //response 
            Headers h = t.getResponseHeaders();
            h.add("Content-Type", "throughput/test");
            
            try {
				h.add(CPU , String.valueOf(getProcessCpuLoad()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            // send the response.
            t.sendResponseHeaders(200, file.length());
            OutputStream os = t.getResponseBody();
            os.write(bytearray,0,bytearray.length);
            os.close();
            bis.close();
            System.out.println("Got "+ throughputCount +" throughput requests");
        }
    }
    
    public static int getProcessCpuLoad() throws Exception {
    	int cpu = 0 ;
    	OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    	  for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
    	    method.setAccessible(true);
    	    // starts with "get" to get more info
    	    if (method.getName().startsWith("getSystemCpuLoad") 
    	        && Modifier.isPublic(method.getModifiers())) {
    	            Object value= null;
    	        try {
    	            value = method.invoke(operatingSystemMXBean);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	        double temp = ((Double) value) * 100; 
    	        cpu = (int) temp;
    	        //System.out.println(method.getName() + " = " + cpu);
    	        }
    	    }
    	  return cpu;
    	  }
    }