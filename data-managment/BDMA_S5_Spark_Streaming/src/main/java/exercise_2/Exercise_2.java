package exercise_2;

import java.util.Arrays;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import scala.Tuple2;
import twitter4j.Status;

public class Exercise_2 {

	public static void get10MostPopularHashtagsInLast5min(
			JavaDStream<Status> statuses) {

		//Section 4.2.1
		JavaDStream<String> words = statuses
				.flatMap(x -> Arrays.asList(x.getText().split(" ")).iterator());
		JavaDStream<String> hashTags = words.filter( x -> x.startsWith("#"));
		//hashTags.print();

		//Section 4.2.2	
		JavaPairDStream<String, Integer> tuples = hashTags.
				mapToPair(x -> new Tuple2<String, Integer>(x, 1));

		JavaPairDStream<String, Integer> counts = tuples.reduceByKeyAndWindow(
				new Function2<Integer, Integer, Integer>() {
					private static final long serialVersionUID = 1L;

					public Integer call(Integer v1, Integer v2) {
						return v1 + v2;
					}
				},
				
				new Function2<Integer, Integer, Integer>() {
					private static final long serialVersionUID = 1L;

					public Integer call(Integer v1, Integer v2) {
						return v1 - v2;
					}
				}
				, new Duration(5 * 60 * 1000), new Duration(1000));

		//Section 4.2.3
		JavaPairDStream<Integer, String> swappedCounts = counts.
			mapToPair(
				new PairFunction<Tuple2<String, Integer>, Integer, String>() {
					private static final long serialVersionUID = 1L;

					public Tuple2<Integer,String> call(Tuple2<String, Integer> t) {
						return t.swap();
					}
			});

		JavaPairDStream<Integer, String> sortedCounts = swappedCounts.
				transformToPair(x -> x.sortByKey());

		sortedCounts.print();
		sortedCounts.foreachRDD(x -> {
			String out = "Top 10!";
			for (Tuple2<Integer, String> y: x.take(10)) {
				out = out + y.toString() + "\n";
			}
		});

	}

}
