package exercise_2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

public class Exercise_2 {

	public static String groupByAndAgg(SparkSession spark) {
		String out = "";
		
		JavaRDD<String> winesRDD = spark.read().textFile("src/main/resources/wines.10m.txt").toJavaRDD();
		
		/*out += "The file has "+winesRDD.count()+" lines\n";
		out += "#################################\n";
		
		winesRDD.groupBy(f ->new Tuple2<String,List<String>>(f.split(",")[0],Lists.newArrayList()) )
			.foreach(f -> {
			System.out.println(f._1 + " - " + f._2.toString().split(",")[5]);
		});*/
		
		/*
		 * 1st solution, NOT complete. 1 line function
		winesRDD.mapToPair(linea -> new Tuple2 <String, Double>(linea.split(",")[0], Double.parseDouble(linea.split(",")[4])));
		*/
		
		/*
		 * 2nd solution. several lines function
		winesRDD.mapToPair(linea -> {
			String[] splits = linea.split(",");
			String key = splits[0];
			Double value = Double.parseDouble(splits[4]);
			return new Tuple2<String, Double>(key, value);
		}).reduceByKey((f1,f2) -> f1+f2)
		.foreach(f -> {
			System.out.println(f._1 + " - " + f._2);
		});
		*/
		
		/*
		 * 3rd solution.
		 */
		JavaPairRDD<String, Double> rddOut = winesRDD.mapToPair(linea -> {
			String[] splits = linea.split(",");
			String key = splits[0];
			Double value = Double.parseDouble(splits[4]);
			return new Tuple2<String, Double>(key, value);
		}).reduceByKey((f1,f2) -> f1+f2);
		
		for (Tuple2<String, Double> f: rddOut.collect()) {
			out += f._1 + ", " + f._2 + "\n";
		}
						
		return out;
	}

}

