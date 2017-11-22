package sysu.CommScope.tool;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CopyVerifyTable {
	
	public static void main(String[] args) {
		MongoClient client = new MongoClient("192.168.1.60",27017);
		MongoCollection<Document> commentwords = client.getDatabase("sourcebase").getCollection("commentword");
		
		
		MongoCollection<Document> commentwords6 = client.getDatabase("sourcebase").getCollection("commentword6");
		
		MongoCursor<Document> cursor = commentwords6.find().iterator();
		while(cursor.hasNext()) {
			commentwords.insertOne(cursor.next());
		}
	}
}
