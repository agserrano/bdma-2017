package exercise2;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;

public class Exercise_2_example {
	
	public static void dataGenerator(int N) {
		Fairy fairy = Fairy.create();
		
		MongoClient client = new MongoClient("localhost");
		MongoDatabase database = client.getDatabase("test");
		MongoCollection<Document> collection = database.getCollection("exercise2_example");
		
		String oneName = "";
		for (int i = 0; i < N; ++i) {
			Person person = fairy.person();
			
			if (i == 0) oneName = person.firstName();
			
			Document random = new Document();
			random.put("firstName", person.firstName());
			random.put("passportNumber", person.passportNumber());
			
			collection.insertOne(random);
		}
		
		Document query = new Document();
		query.put("firstName", oneName);
		System.out.println("Firstly inserted person: "+collection.find(query).first().toJson());
		
		client.close();	
	}
}
