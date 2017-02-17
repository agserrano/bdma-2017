package bdma.labos.neo4j;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Launch {
	
	private final static File DB = new File("/home/bdma10/data/neo4j/graph.db");
	
	private static Random random = new Random();

	enum NodeType implements Label {
		 Customer, Website
	}
	
	enum RelationTypes implements RelationshipType {
	     calls, texts, visits
	}
	
	public static void generate(int nodes) {
		GraphDatabaseService graph = null;
		Transaction tx = null;
		try {
			FileUtils.deleteDirectory(DB);
			graph = new GraphDatabaseFactory().newEmbeddedDatabase(DB);
			tx = graph.beginTx();
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost/labo8?user=root&password=MyNewPass");
			Statement statement = connection.createStatement();
			statement.execute("DROP TABLE customers;");
			statement.execute("DROP TABLE websites;");
			statement.execute("DROP TABLE calls;");
			statement.execute("DROP TABLE texts;");
			statement.execute("DROP TABLE visits;");
			
			statement.execute("CREATE TABLE customers("+
				"id CHAR(9), "+
				"number CHAR(9), "+
				"age INT, "+
				"phone VARCHAR(10));");

			statement.execute("CREATE TABLE websites("+
				"id VARCHAR(25), "+
				"url VARCHAR(25));");
					
			statement.execute("CREATE TABLE calls("+
				"customer1 CHAR(9), "+
				"customer2 CHAR(9), "+
				"price FLOAT(3,2), "+
				"duration FLOAT(4,2), "+
				"day VARCHAR(10));");
					
			statement.execute("CREATE TABLE texts("+
				"customer1 CHAR(9), "+
				"customer2 CHAR(9), "+
				"price FLOAT(3,2), "+
				"length FLOAT(5,2), "+
				"day VARCHAR(10));");
					
			statement.execute("CREATE TABLE visits("+
				"customer CHAR(9), "+
				"website VARCHAR(25), "+
				"day VARCHAR(10));");
			
			HashMap<String, Node> websites = new HashMap<String, Node>();
			HashMap<String, Node> customers = new HashMap<String, Node>();
			for (int i = 0; i < nodes; i++) {
				Node node = null;
				if (i%3 > 0) {
					String number = Values.getPhoneNumber();
					if (!customers.containsKey(number)) {
						int age = Values.getAge();
						String phone = Values.getPhone();
						
						node = graph.createNode(NodeType.Customer);
						node.setProperty("id", number);
						node.setProperty("number", number);
						node.setProperty("age", age);
						node.setProperty("phone", phone);
						
						statement.addBatch("INSERT INTO customers VALUES("+
							"'"+number+"', "+
							"'"+number+"', "+
							age+", "+
							"'"+phone+"');");

						customers.put(number, node);
					}
				} else {
					String url = Values.getURL();
					if (!websites.containsKey(url)) {
						node = graph.createNode(NodeType.Website);
						node.setProperty("id", url);
						node.setProperty("url", url);
						
						statement.addBatch("INSERT INTO websites VALUES("+
							"'"+url+"', "+
							"'"+url+"');");
						
						websites.put(url, node);
					}
				}
			}
			
			ArrayList< ArrayList<Node> > families = new ArrayList< ArrayList<Node> >();
			int nFamilies = random.nextInt(5)+nodes/5;
			for (int i = 1; i <= nFamilies; i++) {
				families.add(new ArrayList<Node>());
			}
			for (Node customer : customers.values()) {
				int family = random.nextInt(nFamilies);
				families.get(family).add(customer);
			}
			for (ArrayList<Node> family : families) {
				for (int i = 0; i < family.size(); i++) {
					int edges = random.nextInt(family.size()*3);
					for (int j = 1; j <= edges; j++) {
						int action = random.nextInt(2);
						int edgeTo = random.nextInt(family.size());
						if (i != edgeTo) {
							if (action == 0) {
								double price = Values.getCallPrice();
								double duration = Values.getCallDuration();
								String day = Values.getDay();
								
								Relationship call = family.get(i).createRelationshipTo(family.get(edgeTo), RelationTypes.calls);
								call.setProperty("price", price);
								call.setProperty("duration", duration);
								call.setProperty("day", day);

								statement.addBatch("INSERT INTO calls VALUES("+
									"'"+family.get(i).getProperty("number")+"', "+
									"'"+family.get(edgeTo).getProperty("number")+"', "+
									price+", "+
									duration+", "+
									"'"+day+"\');");
							} else {
								double price = Values.getTextPrice();
								double length = Values.getTextLength();
								String day = Values.getDay();
								
								Relationship text = family.get(i).createRelationshipTo(family.get(edgeTo), RelationTypes.texts);
								text.setProperty("price", price);
								text.setProperty("length", length);
								text.setProperty("day", day);
								
								statement.addBatch("INSERT INTO texts VALUES("+
									"'"+family.get(i).getProperty("number")+"', "+
									"'"+family.get(edgeTo).getProperty("number")+"', "+
									price+", "+
									length+", "+
									"'"+day+"');");
							}
						}
					}
					edges = random.nextInt(nodes/5);
					for (int j = 1; j <= edges; j++) {
						int edgeTo = random.nextInt(websites.size());
						for (Node website : websites.values()) {
							if (edgeTo == 0) {
								String day = Values.getDay();
								
								Relationship visit = family.get(i).createRelationshipTo(website, RelationTypes.visits);
								visit.setProperty("day", day);
								
								statement.addBatch("INSERT INTO visits VALUES("+
									"'"+family.get(i).getProperty("number")+"', "+
									"'"+website.getProperty("url")+"', "+
									"'"+Values.getDay()+"');");
								break;
							} else {
								edgeTo--;
							}
						}
					}
				}
			}
			statement.executeBatch();
			tx.success();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tx.close();
		}
	}
	
	public static void main(String[] args) {
		generate(Integer.parseInt(args[0]));
	}
	
}
