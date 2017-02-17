package exercise_4;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;


public class Exercise_4 {

	public static void SQLqueries(SparkSession spark, String pathIn, String pathOut) {
		try {
			JavaRDD<String> winesRDD = spark.read().textFile("src/main/resources/wines.10m.txt").toJavaRDD();

			String schemaString = "type region alc m_acid ash alc_ash mgn t_phenols flav nonflav_phenols proant col hue od280od315 proline";
			List<StructField> fields = new ArrayList<>();
			for (String fieldName : schemaString.split(" ")) {
				  StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
				  fields.add(field);
				}
			StructType schema = DataTypes.createStructType(fields);
			
			JavaRDD<Row> rowRDD = winesRDD.map(new Function<String, Row>() {
				  @Override
				  public Row call(String record) throws Exception {
				    String[] attributes = record.split(",");
				    return RowFactory.create(
				    		attributes[0],
				    		attributes[1],
				    		attributes[2],
				    		attributes[3],
				    		attributes[4],
				    		attributes[5],
				    		attributes[6],
				    		attributes[7],
				    		attributes[8],
				    		attributes[9],
				    		attributes[10],
				    		attributes[11],
				    		attributes[12],
				    		attributes[13],
				    		attributes[14]);
				  }
				});
			
			Dataset<Row> winesDataFrame = spark.createDataFrame(rowRDD, schema);
			//winesDataFrame.show();
			winesDataFrame.createOrReplaceTempView("wines");
			
			// number of regions with alc > 11
			Dataset<Row> results = spark.sql("SELECT COUNT(DISTINCT region) FROM wines WHERE alc > 11");
			results.show();
			
			// count, mean, stddev, min and max
			Dataset<Row> results_math = spark.sql("SELECT COUNT(proline), STDDEV(proline), AVG(proline), MEAN(proline), MIN(proline), MAX(proline) FROM wines");
			results_math.show();
		} catch (Exception ex) {
			System.out.println("Oops! Something went wrong");
			System.out.println(ex.getMessage());
		}
	}	
}
