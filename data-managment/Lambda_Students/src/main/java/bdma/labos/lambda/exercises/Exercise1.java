package bdma.labos.lambda.exercises;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;
import twitter4j.JSONObject;
import bdma.labos.lambda.utils.Utils;

public class Exercise1 {

	@SuppressWarnings("serial")
	public static void run(String twitterFile) throws Exception {
		SparkConf conf = new SparkConf().setAppName("LambdaArchitecture").setMaster("spark://master:7077");
		JavaSparkContext context = new JavaSparkContext(conf);
		JavaStreamingContext streamContext = new JavaStreamingContext(context, new Duration(1000));
		
		JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = Utils.
				getKafkaStream(streamContext, twitterFile);
		/*********************/
		kafkaStream.map(
		  new Function<ConsumerRecord<String, String>, Tuple2<String, String>>() {
		    @Override
		    public Tuple2<String, String> call(ConsumerRecord<String, String> record) {
		      return new Tuple2<>(record.key(), record.value());
		    }
		  }).foreachRDD(row -> {
			if (!row.isEmpty()) {
				row.collect().forEach(elem -> System.out.println(elem._2));
			}
		  });
		
		/********************/
		
		streamContext.start();
		streamContext.awaitTermination();
	}
	
}
