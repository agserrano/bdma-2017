package exercise2;

import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.joda.time.chrono.AssembledChronology.Fields;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.FindIterable;
//import com.mongodb.client.model.Projections.include;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.company.Company;

public class Exercise_2_model_3 {
	
	public static void dataGenerator(int num_persons, int num_companies) {
		Fairy fairy = Fairy.create();
		
		System.out.println("Connecting to DB");
		MongoClient client = new MongoClient("localhost");
		MongoDatabase database = client.getDatabase("test");
		MongoCollection<Document> collection_persons = database.getCollection("exercise2_model_3_person");
		MongoCollection<Document> collection_company = database.getCollection("exercise2_model_3_company");
		
		// lets blanck the collection before inserting
		collection_company.drop();
		String oneName = "";
		Company[] companies = new Company[num_companies];
		String[] company_emails = new String[num_companies];
		String[] company_names = new String[num_companies];
		Integer[] company_employees_age = new Integer[num_companies];
		Integer[] company_num_employees = new Integer[num_companies];
		for (int i = 0; i < num_companies; ++i) {
			company_num_employees[i] = 0;
			company_employees_age[i] = 0;
		}

		System.out.println("Creating company info");
		for (int i = 0; i < num_companies; ++i) {
			Company company = fairy.company();
			companies[i] = company;
			
			if (i == 0) oneName = company.domain();
			
			company_emails[i] = company.email();
			company_names[i] = company.name();
		}
		
		// lets blanck the collection before inserting
		System.out.println("Creating persons info and adding them to the collection");
		collection_persons.drop();
		oneName = "";
		for (int i = 0; i < num_persons; ++i) {
			Person person = fairy.person();
			
			if (i == 0) oneName = person.firstName();
			Random rand = new Random();
			int  n = rand.nextInt(num_companies);
			company_num_employees[n]++;
			
			Document random = new Document();
			random.put("firstName", person.firstName());
			random.put("passportNumber", person.passportNumber());
			random.put("companyEmail", company_emails[n]);
			random.put("companyName", company_names[n]);
			random.put("age", person.age());
			company_employees_age[n] += person.age();
			
			collection_persons.insertOne(random);
		}
		
		// update the companies
		System.out.println("Adding companies to the collection");
		for (int i = 0; i < num_companies; ++i) {
			Company company = companies[i];
			
			Document random = new Document();
			random.put("domain", company.domain());
			company_emails[i] = company.email();
			random.put("email", company.email());
			random.put("name", company.name());
			random.put("num_employees", company_num_employees[i]);
			random.put("avg_age_employees", (float)company_employees_age[i] / company_num_employees[i]);
			
			collection_company.insertOne(random);
		}

//		Q1
		System.out.println("Q1 ---------------");
//		db.exercise2_model_3_person.find({}, {"firstName": 1, "companyName": 1})	
		printResults(collection_persons.find().projection(Projections.include("firstName", "companyName")));		

//		Q2
		System.out.println("Q2 ---------------");
//		db.exercise2_model_3_company.find({}, {"name": 1, "num_employees": 1})
		printResults(collection_company.find().projection(Projections.include("name", "num_employees")));

//		Q3
		System.out.println("Q3 ---------------");
//		db.exercise2_model_3_company.find({}, {"name": 1, "avg_age_employees": 1})
		printResults(collection_company.find().projection(Projections.include("name", "avg_age_employees")));
		
		client.close();	
	}
	
	public static void printResults(FindIterable<Document> res) {
		for (Document document : res) {
		    System.out.println(document.toJson());
		}
	}
}
