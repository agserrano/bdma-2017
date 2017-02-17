import java.util.Arrays;

import exercise_2.Exercise_2;
import exercise_2.Exercise_2_warmup;
import exercise_3.Exercise_3;
import exercise_4.Exercise_4;
import exercise_5.Exercise_5;
import org.apache.spark.sql.SparkSession;

public class S4_Spark {
	
	public static void main(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));

		SparkSession spark;
		
		if (args.length < 1) {
			throw new Exception("Wrong number of parameters, usage: (exercise2warmup,exercise2,exercise3,exercise4,exercise5); extra parameters specifics for the exercise");
		}
		if (args[0].equals("exercise2warmup")) {
			spark = SparkSession.builder().master("local[*]").appName("S4-Exercise2Warmup").getOrCreate();
			String out = Exercise_2_warmup.basicAnalysis(spark);
			spark.stop();
			System.out.println(out);
		}
		else if (args[0].equals("exercise2")) {
			spark = SparkSession.builder().master("local[*]").appName("S4-Exercise2").getOrCreate();
			String out = Exercise_2.groupByAndAgg(spark);
			spark.stop();
			System.out.println(out);
		}
		else if (args[0].equals("exercise3")) {
			if (args.length != 3) {
				throw new Exception("exercise3, extra parameters required: pathIn, pathOut");
			}
			spark = SparkSession.builder().master("local[*]").appName("S4-Exercise3").getOrCreate();
			Exercise_3.groupByAndAggCluster(spark, args[1],args[2]);
			spark.stop();
		}
		else if (args[0].equals("exercise4")) {
			if (args.length != 3) {
				//throw new Exception("exercise4, extra parameters required: pathIn, pathOut");
			}

			spark = SparkSession.builder().master("local[*]").appName("S4-Exercise4").getOrCreate();
			Exercise_4.SQLqueries(spark, args[1],args[2]);
			spark.stop();
		}
		else if (args[0].equals("exercise5")) {
			if (args.length != 3) {
				throw new Exception("exercise5, extra parameters required: pathIn, pathOut");
			}
			spark = SparkSession.builder().master("local[*]").appName("S4-Exercise5").getOrCreate();
			Exercise_5.sparkSQLwithList(spark, args[1],args[2]);
			spark.stop();
		}
		else {
			throw new Exception("Wrong number of exercise");
		}
	}
}

