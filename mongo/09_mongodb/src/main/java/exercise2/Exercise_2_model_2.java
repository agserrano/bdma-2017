package exercise2;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;

import com.mongodb.client.model.Aggregates.*;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.person.Person;

public class Exercise_2_model_2 {

	public static void dataGenerator(int num_persons, int num_companies) {
		// as it doesn't say which should have the nested documents. i choose to have companies with nested employees
		Fairy fairy = Fairy.create();
		
		MongoClient client = new MongoClient("localhost");
		MongoDatabase database = client.getDatabase("test");
		MongoCollection<Document> collection_company = database.getCollection("exercise2_model_2_company");
		
		// lets blanck the collection before inserting
		collection_company.drop();
		String oneName = "";
		Company[] companies = new Company[num_companies];

		ArrayList<ArrayList<Document>> personsO = new ArrayList<ArrayList<Document>>();
		for (int i = 0; i < num_companies; i++) {
			//persons.add(new ArrayList<String>);
			ArrayList<String> test = new ArrayList<String>();
			ArrayList<Document> testO = new ArrayList<Document>();
			personsO.add(testO);
		}
		
		String[] company_emails = new String[num_companies];
		Integer[] company_num_employees = new Integer[num_companies];
		for (int i = 0; i < num_companies; ++i) {
			company_num_employees[i] = 0;
		}
		for (int i = 0; i < num_companies; ++i) {
			Company company = fairy.company();
			companies[i] = company;
			
			if (i == 0) oneName = company.domain();
			
			company_emails[i] = company.email();
		}
		Document queryc = new Document();
		queryc.put("domain", oneName);
		
		// lets blanck the collection before inserting
		oneName = "";
		for (int i = 0; i < num_persons; ++i) {
			Person person = fairy.person();
			
			if (i == 0) oneName = person.firstName();
			Random rand = new Random();
			int  n = rand.nextInt(num_companies);
			company_num_employees[n]++;
			
			JSONObject obj = new JSONObject();
			obj.put("firstName", person.firstName());
			obj.put("age", person.age());
			
			Document random = new Document();
			random.put("firstName", person.firstName());
			random.put("passportNumber", person.passportNumber());
			random.put("companyEmail", company_emails[n]);
			random.put("age", person.age());
//			System.out.println("Person: " + i + " Company: " + n + " size: ");
			try {
				personsO.get(n).add(random);
			} catch (Exception e) {
				System.out.println("Ooooooops: " + e.toString());
			}
		}
		
		// update the companies
		for (int i = 0; i < num_companies; ++i) {
			Company company = companies[i];
			
			Document random = new Document();
			random.put("domain", company.domain());
			company_emails[i] = company.email();
			random.put("email", company.email());
			random.put("name", company.name());
			random.put("num_employees", company_num_employees[i]);
			//random.put("employees", persons.get(i));
			random.put("employees", personsO.get(i));
			
			collection_company.insertOne(random);
		}
		System.out.println("Finished!");

//		Q1
		System.out.println("Q1 ---------------");
//		db.exercise2_model_2_company.aggregate({
//	        $unwind: "$employees"
//	    }, { $project: {"name": 1, "employees.firstName": 1} }
		Document q1 = new Document();
		q1.put("name", 1);
		q1.put("employees.firstName", 1);
		java.util.List<Bson> pipelineq1 = Arrays.asList(
				Aggregates.unwind("$employees"),
				Aggregates.project(q1)
				);
		AggregateIterable<Document> resq1 = collection_company.aggregate(pipelineq1);
		for (Document document : resq1) {
			System.out.println(document.toJson());
		}
		
//		Q2
		System.out.println("Q2 ---------------");
//		db.exercise2_model_2_company.find({}, { "name": 1, "num_employees": 1})
		printResults(collection_company.find().projection(Projections.include("name", "num_employees")));
		
//		Q3
		System.out.println("Q3 ---------------");
//		db.exercise2_model_2_company.aggregate({
//	        $unwind: "$employees"
//	    }, {
//	        $group: {
//	            _id: "$name",
//	            average_age: { $avg: "$employees.age" }
//	        }
//	    }, { $project: {"name": 1, "average_age": 1} }
		
		Document projection3 = new Document();
		projection3.put("name", 1);
		projection3.put("average_age", 1);
		
		AggregateIterable<Document> resq3 = collection_company.aggregate(Arrays.asList(
				Aggregates.unwind("$employees"),
                Aggregates.group("$name", Accumulators.avg("average_age", "$employees.age")),
                Aggregates.project(projection3)
                ));
		
		for (Document document : resq3) {
			System.out.println(document.toJson());
		}
		
		client.close();	
	}
	
	public static void printResults(FindIterable<Document> res) {
		for (Document document : res) {
		    System.out.println(document.toJson());
		}
	}
}
