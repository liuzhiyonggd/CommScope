package sysu.CommScope.tool;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CopyVerifyTable {
	
	public static void main(String[] args) {
		MongoClient client = new MongoClient("39.108.99.24",27017);
		MongoCollection<Document> verifys = client.getDatabase("sourcebase").getCollection("endline_verify");
		
		MongoClient client2 = new MongoClient("192.168.2.168",27017);
		MongoCollection<Document> verifys2 = client2.getDatabase("sourcebase").getCollection("endline_verify");
		
		MongoCursor<Document> cursor = verifys.find().iterator();
		while(cursor.hasNext()) {
			verifys2.insertOne(cursor.next());
		}
	}

}
