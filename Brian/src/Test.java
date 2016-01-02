import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;






public class Test {

	//public static List mList ;
	public static int foo(int x , int y){ return x-y; }
	
	public static Hashtable<String, Integer> noFours(Hashtable <String , Integer> mHashtable){
		
		Hashtable<String, Integer> mNewht=  (Hashtable<String, Integer>) mHashtable.clone();
		mNewht.putAll(mHashtable);
		mNewht.values().removeAll(Collections.singleton(4));
		return mNewht;
	}
	public static boolean isPrime (int mNumber){
		for(int i = 2 ; i< mNumber ; i++){
			System.out.println(i);
			if(mNumber%i == 0){
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		//System.out.println(foo(1,3));
		List<Integer> mList= new ArrayList<Integer>();
		mList.add(5);
		mList.add(4);
		mList.add(3);
		mList.add(2);
		mList.add(1);
		mList.add(0);
		//List mNewList = CopyList(mList);
		//Iterator mIterator = mNewList.iterator();
		//while(mIterator.hasNext()){
		//	System.out.println(mIterator.next());}
		
		Hashtable <String , Integer> mHash = new Hashtable<>();
		mHash.put("Key 1", 1);
		mHash.put("Key 2", 2);
		mHash.put("Key first 4", 4);
		mHash.put("Key second 4", 4);
		System.out.println(noFours(mHash));
		System.out.println(mHash);
		System.out.println(isPrime(13));
	}	
}
