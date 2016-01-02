import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggerTest {
	
	private Handler mFileHandler;
	private static Logger mLog = null;
	private SimpleFormatter mFormatter;
	
	public static Logger getLogInstance(){
		if(mLog == null){
			mLog = Logger.getLogger("MyLogger");
			mLog.finest("Initialze a looger"+ mLog.getName());
			}
		return mLog;
	}
	
	public static void setLogLevel(String level){
		switch (level){
		case "error" : mLog.setLevel(Level.SEVERE); break;
		case "warning" : mLog.setLevel(Level.WARNING); break;
		case "info" : mLog.setLevel(Level.INFO); break;
		case "debug" : mLog.setLevel(Level.ALL); break;
		default : mLog.setLevel(Level.ALL); break;
		}
	}
	public LoggerTest(){
		try {
    		mLog = Logger.getLogger("my Logger");
			mFileHandler  = new FileHandler("./javalog.log");
			mLog.addHandler(mFileHandler);
			mFormatter = new SimpleFormatter();
			mFileHandler.setFormatter(mFormatter);
			mFileHandler.setLevel(Level.ALL);
		} catch (SecurityException | IOException e) {
			mLog.severe(e.getStackTrace().toString());
		}
	}
}
