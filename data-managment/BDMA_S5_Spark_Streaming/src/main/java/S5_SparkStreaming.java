import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;


import com.google.common.io.Files;

import exercise_1.Exercise_1;
import exercise_2.Exercise_2;
import exercise_3.Exercise_3;
import twitter4j.Status;


public class S5_SparkStreaming {

	static String TWITTER_CONFIG_PATH = "/Users/tonet/Documents/posgrado/BDMA_S5_Spark_Streaming/twitter_configuration.txt";
	static String HADOOP_COMMON_PATH = "/Users/tonet/Documents/posgrado/BDMA_S5_Spark_Streaming/src/main/resources";


	public static void main(String[] args) throws Exception {
		System.setProperty("hadoop.home.dir", HADOOP_COMMON_PATH);

		SparkConf conf = new SparkConf().setAppName("S5_SparkStreaming").setMaster("local[*]");
		JavaSparkContext ctx = new JavaSparkContext(conf);
		JavaStreamingContext jsc = new JavaStreamingContext(ctx, new Duration(1000));
		LogManager.getRootLogger().setLevel(Level.ERROR);
		
		jsc.checkpoint(Files.createTempDir().getAbsolutePath());
		
		Utils.setupTwitter(TWITTER_CONFIG_PATH);
		
		JavaDStream<Status> tweets = TwitterUtils.createStream(jsc);
		
		//Exercise_1.displayAllTweets(tweets);
		Exercise_2.get10MostPopularHashtagsInLast5min(tweets);
		//Exercise_3.sentimentAnalysis(tweets);
		
		jsc.start();
		jsc.awaitTermination();
	}

}
