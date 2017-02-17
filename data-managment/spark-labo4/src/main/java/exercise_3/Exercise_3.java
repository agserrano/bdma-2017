package exercise_3;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;


public class Exercise_3 {

	public static void groupByAndAggCluster(SparkSession jsc, String pathIn, String pathOut) {

		JavaRDD<String> winesRDD = jsc.read().textFile(pathIn).toJavaRDD();
		JavaRDD<String[]> wineAttributes = winesRDD.map(e -> e.split(","));
		
		JavaPairRDD<String, Double> wine1_5 = wineAttributes.
				mapToPair(s -> new Tuple2<String, Double>(s[0], Double.valueOf(s[4])));
		
		//wine1_5.cache();
		wine1_5.
		aggregateByKey(Double.valueOf(0), (a , b) -> a + b, (a , b) -> a + b).
		saveAsTextFile(pathOut);
		
		/*wineAttributes.mapToPair(linea -> {
			String[] splits = linea.split(",");
			String key = splits[0];
			Double value = Double.parseDouble(splits[4]);
			return new Tuple2<String, Double>(key, value);
		}).aggregateByKey(Double.valueOf(0), (a , b) -> a + b, (a , b) -> a + b).saveAsTextFile(pathOut);*/
		
		/* running with:
		 ./bin/spark-submit --class S4_Spark --master spark://master:7077 \
--deploy-mode cluster /home/bdma10/spark-labo4.jar exercise3 \
hdfs://master:27000/user/bdma10/wines.txt hdfs://master:27000/user/bdma10/winesGroupeAndAgg.txt
		 */
		
		/*
		 ./bin/spark-submit --class S4_Spark --master spark://master:6066 \
--deploy-mode cluster ../spark-labo4.jar exercise3 \
hdfs://master:27000/user/bdma10/wines.txt hdfs://master:27000/user/bdma10/winesGroupedAndAgg.txt
		 */
		
	}
}