package exercise2;

import java.util.Arrays;
import java.util.Random;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.company.Company;

public class Exercise_2_model_1 {
	
	public static void dataGenerator(int num_persons, int num_companies) {
		Fairy fairy = Fairy.create();
		
		MongoClient client = new MongoClient("localhost");
		MongoDatabase database = client.getDatabase("test");
		MongoCollection<Document> collection_persons = database.getCollection("exercise2_model_1_person");
		MongoCollection<Document> collection_company = database.getCollection("exercise2_model_1_company");
		
		// lets blanck the collection before inserting
		collection_company.drop();
		String oneName = "";
		Company[] companies = new Company[num_companies];
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
		
		// lets blanck the collection before inserting
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
			random.put("age", person.age());
			
			collection_persons.insertOne(random);
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
			
			collection_company.insertOne(random);
		}
		
//		Q1
		System.out.println("Q1 ---------------");
//		db.exercise2_model_1_person.aggregate([
//           {
//               $lookup: {
//                   from: "exercise2_model_1_company",
//                   localField: "companyEmail",
//                   foreignField: "email",
//                   as: "company"
//               }
//           }, {
//               $unwind: "$company"
//           }, {
//               $project : {
//                   "firstName": 1,
//                   "company.name": 1
//               }
//           }
//       ])
		Document projection1 = new Document();
		projection1.put("firstName", 1);
		projection1.put("company.name", 1);
		
		AggregateIterable<Document> resq1 = collection_persons.aggregate(Arrays.asList(
				Aggregates.lookup("exercise2_model_1_company", "companyEmail", "email", "company"),
				Aggregates.unwind("$company"),
                Aggregates.project(projection1)
                ));
		printResults(resq1);
		
//		Q2
		System.out.println("Q2 ---------------");
//		db.exercise2_model_1_company.aggregate([
//            {
//                $lookup: {
//                    from: "exercise2_model_1_person",
//                    localField: "email",
//                    foreignField: "companyEmail",
//                    as: "employees"
//                }
//            },
//            {
//                $project : {"name": 1, "num_employees": 1, "employees.firstName": 1}
//            }
//        ])
		Document projection2 = new Document();
		projection2.put("name", 1);
		projection2.put("num_employees", 1);
		projection2.put("employees.firstName", 1);
		
		AggregateIterable<Document> resq2 = collection_company.aggregate(Arrays.asList(
				Aggregates.lookup("exercise2_model_1_person", "email", "companyEmail", "employees"),
				Aggregates.unwind("$employees"),
                Aggregates.project(projection2)
                ));
		printResults(resq2);
		
//		Q3
		System.out.println("Q3 ---------------");
//		db.exercise2_model_1_company.aggregate([
//            {
//                $lookup: {
//                    from: "exercise2_model_1_person",
//                    localField: "email",
//                    foreignField: "companyEmail",
//                    as: "employees"
//                }
//            }, {
//                $unwind: "$employees"
//            }, {
//                $group: {
//                    _id: "$name",
//                    average_age: { $avg: "$employees.age" }
//                }
//            }, {
//                $project: {"name": 1, "average_age": 1}
//            }
//        ])
		Document projection3 = new Document();
		projection3.put("name", 1);
		projection3.put("average_age", 1);
		
		AggregateIterable<Document> resq3 = collection_company.aggregate(Arrays.asList(
				Aggregates.lookup("exercise2_model_1_person", "email", "companyEmail", "employees"),
				Aggregates.unwind("$employees"),
				Aggregates.group("$name", Accumulators.avg("average_age", "$employees.age")),
                Aggregates.project(projection3)
                ));
		printResults(resq3);
		
		client.close();	
	}
	
	public static void printResults(AggregateIterable<Document> res) {
		for (Document document : res) {
		    System.out.println(document.toJson());
		}
	}
}
