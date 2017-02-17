package bdma.labos.lambda.exercises;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;
import twitter4j.JSONObject;
import bdma.labos.lambda.utils.Utils;
import bdma.labos.lambda.writers.WriterClient;
import bdma.labos.lambda.writers.WriterServer;

public class Exercise2 {

	@SuppressWarnings("serial")
	public static void run(String twitterFile) throws Exception {
		WriterServer writerServer = new WriterServer();
		writerServer.start();
		
		SparkConf conf = new SparkConf().setAppName("LambdaArchitecture").setMaster("spark://master:7077");
		JavaSparkContext context = new JavaSparkContext(conf);
		JavaStreamingContext streamContext = new JavaStreamingContext(context, new Duration(1000));
		
		JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = Utils.getKafkaStream(streamContext, twitterFile);
		
		kafkaStream.mapPartitions(messages -> {
			WriterClient writer = new WriterClient();
			List<String> tweets = new ArrayList<String>();

			/*************************/
			// CODE HERE
			/************************/			
			while(messages.hasNext()) {
			    String str_message = messages.next().value();
			    byte[] message_bytes = (str_message+ "\n").getBytes();
			    writer.write(message_bytes);
			    tweets.add(str_message);
			}
            
			writer.close();
			return tweets.iterator();
		}).foreachRDD(rdd -> {
			rdd.foreachPartition(tweets -> {
				Configuration config = HBaseConfiguration.create();
				Connection connection = ConnectionFactory.createConnection(config);
				Table table = connection.getTable(TableName.valueOf("lambda"));

				/*************************/
				// CODE HERE
				/************************/			
				while (tweets.hasNext()) {
					JSONObject json_tweet = new JSONObject(tweets.next());
					
					Iterable<String> words = Arrays.asList(json_tweet.getString("text").split(" "));
					for (String word : words){
						if (word.startsWith("#")){
	                        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HHmmss z yyyy");
	                        Date d = format.parse(json_tweet.getString("created"));
	                        long timestamp = d.getTime();
	                        
							Put put = new Put(word.getBytes());
							put.addColumn("tweets".getBytes(), 
									json_tweet.getString("id").getBytes(), 
									Bytes.toBytes(timestamp));
							table.put(put);
						}	
					}
				}
				
				table.close();
				connection.close();
			});
		});

		streamContext.start();
		streamContext.awaitTermination();
		
		writerServer.finish();
	}
	
}
