package bdma.labos.lambda.exercises;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.LanguageProfile;
import bdma.labos.lambda.exercises.StopWords;
import bdma.labos.lambda.utils.Utils;
import bdma.labos.lambda.writers.WriterClient;
import scala.Tuple2;
import twitter4j.JSONObject;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

public class Exercise3 {

	private static String HDFS = "hdfs://master:27000/user/bdma10";
	
	@SuppressWarnings("serial")
	public static void run() throws Exception {
		SparkConf conf = new SparkConf().setAppName("LambdaArchitecture").setMaster("spark://master:7077");
		JavaSparkContext context = new JavaSparkContext(conf);
		
		/*************************/
		// CODE HERE
		/*************************/
		JavaRDD<String> tweetsRDD = context.textFile(HDFS + "/lambda/1484415845021");		
		
//		JavaRDD<Row> rowRDD = tweetsRDD.map(
//				  new Function<String, Row>() {
//				    public Row call(String line) throws Exception {
//				      return RowFactory.create(line);
//				    }
//				  });
//		
//		
//		Configuration config = HBaseConfiguration.create();
//		Connection connection = ConnectionFactory.createConnection(config);
//		Table table = connection.getTable(TableName.valueOf("lambda"));
//		
//		rowRDD.foreachPartition(tweets -> {
//			while (tweets.hasNext()) {
//				JSONObject json_tweet = new JSONObject(tweets.next());
//				
//				String cleanedText = json_tweet.getString("text");
//				if (Exercise3.isEnglish(cleanedText) && cleanedText.trim().length() > 0) {
//					cleanedText = cleanedText.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
//					json_tweet.put("text", cleanedText);
//										
//                    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HHmmss z yyyy");
//                    Date d = format.parse(json_tweet.getString("created"));
//                    long timestamp = d.getTime();
//					
//					Put put = new Put(json_tweet.toString().getBytes());
//					put.addColumn("tweets".getBytes(), 
//							json_tweet.getString("id").getBytes(), 
//							Bytes.toBytes(timestamp));
//					table.put(put);
//				}
//			}
//			
//			table.close();
//			connection.close();
//		});
//		
//		context.close();
		
		
		
		
		JavaRDD<String> tweets_filteredRDD = tweetsRDD
			.filter(x -> Exercise3.isEnglish((new JSONObject(x)).getString("text")))
			.filter(x -> ((new JSONObject(x)).getString("text")).trim().length() > 0);
		
		tweets_filteredRDD.foreachPartition(x -> {
				Configuration config = HBaseConfiguration.create();
				Connection connection = ConnectionFactory.createConnection(config);
				Table table = connection.getTable(TableName.valueOf("lambda"));
				while (x.hasNext()) {
					JSONObject json_tweet = new JSONObject(x.next());
					String cleanedText = json_tweet.getString("text").replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
					json_tweet.put("text", cleanedText);
										
	                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HHmmss z yyyy");
	                Date d = format.parse(json_tweet.getString("created"));
	                long timestamp = d.getTime();
					
					Put put = new Put(json_tweet.toString().getBytes());
					put.addColumn("tweets".getBytes(), 
							json_tweet.getString("id").getBytes(), 
							Bytes.toBytes(timestamp));
					table.put(put);
				}
			});
		
		context.close();
		
		
		
//		tweetsRDD.foreachPartition(tweets -> {
//			Configuration config = HBaseConfiguration.create();
//			Connection connection = ConnectionFactory.createConnection(config);
//			Table table = connection.getTable(TableName.valueOf("lambda"));
//		
//			while (tweets.hasNext()) {
//				JSONObject json_tweet = new JSONObject(tweets.next());
//				
//				String cleanedText = json_tweet.getString("text");
//				if (Exercise3.isEnglish(cleanedText) && cleanedText.trim().length() > 0) {
//					cleanedText = cleanedText.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
//					json_tweet.put("text", cleanedText);
//										
//                    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HHmmss z yyyy");
//                    Date d = format.parse(json_tweet.getString("created"));
//                    long timestamp = d.getTime();
//					
//					Put put = new Put(json_tweet.toString().getBytes());
//					put.addColumn("tweets".getBytes(), 
//							json_tweet.getString("id").getBytes(), 
//							Bytes.toBytes(timestamp));
//					table.put(put);
//				}
//			}
//			
//			table.close();
//			connection.close();
//		});
//
//		context.close();
	}

	static boolean isEnglish(String s) {
	    LanguageIdentifier identifier = new LanguageIdentifier(s);
	    return identifier.getLanguage().equals("en");
	}
		
}
