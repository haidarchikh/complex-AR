package wordcount;

import se.ltu.d7031e.DB;
import se.ltu.d7031e.DBCursor;
import se.ltu.d7031e.File;
import se.ltu.d7031e.GridFS;
import se.ltu.d7031e.GridFSDBFile;
import se.ltu.d7031e.GridFSInputFile;
import se.ltu.d7031e.IOException;
import se.ltu.d7031e.MongoClient;
import se.ltu.d7031e.MongoDB;

public class sfdfg {

}
public class MongoDB {
	
	private DB db;
	private String dbName;
	private String mIP;
	private int mPort;
	
	public MongoDB(String dbName, String mIP , int mPort){
		this.dbName = dbName;
		this.mIP = mIP;
		this.mPort = mPort;
		
	}
	private void connect(){
		MongoClient mongo = new MongoClient(mIP, mPort);
        db = (DB) mongo.getDB(dbName);
	}
	private void getSingleFile(String mFile) throws IOException {
        GridFS gfsFile = new GridFS(db);
        GridFSDBFile mFileForOutout = gfsFile.findOne(mFile);
        //System.out.println(mFileForOutout.get("filename"));
		mFileForOutout.writeTo("/home/haidar/testjava1.arff");
        System.out.println(mFileForOutout);
    }
	private  DBCursor listAllFiles() {
        GridFS gfsPhoto = new GridFS(db);
        DBCursor cursor = gfsPhoto.getFileList();
        while (cursor.hasNext()) {
            System.out.println(cursor.next().get("filename"));
        }
        return cursor;
    }
	
	private void deleteFile(String mFile) {
        GridFS gfsFile = new GridFS(db);
        gfsFile.remove(gfsFile.findOne(mFile));
    }
	
	private void saveImageIntoMongoDB() throws IOException {
        String dbFileName = "DemoImage";
        File imageFile = new File("c:\\DemoImage.png");
        GridFS gfsPhoto = new GridFS(db);
        GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
        gfsFile.setFilename(dbFileName);
        gfsFile.save();
    }
	
	public static void main(String[] args) {

		MongoDB db = new MongoDB("accelerometer", "127.0.0.1", 27017);
		db.connect();
		try {
			db.getSingleFile("1448995739984.arff");
			db.listAllFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}