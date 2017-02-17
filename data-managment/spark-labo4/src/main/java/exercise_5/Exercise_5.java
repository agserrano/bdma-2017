package exercise_5;

import java.util.ArrayList;
import java.util.List;


import com.google.common.collect.Lists;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import static org.apache.spark.sql.functions.*;

public class Exercise_5 {

	private static List<String> countries = Lists.newArrayList("Albania", "Andorra", "Armenia", "Austria", "Azerbaijan",
			"Belarus", "Belgium", "Bosnia and Herzegovina", "Bulgaria", "Croatia", "Cyprus", "Czech Republic",
			"Denmark", "Estonia", "Finland", "France", "Georgia", "Germany", "Greece", "Hungary", "Iceland", "Ireland",
			"Italy", "Kosovo", "Latvia", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Malta", "Moldova",
			"Monaco", "Montenegro", "Netherlands", "Norway", "Poland", "Portugal", "Romania", "Russia", "San Marino",
			"Serbia", "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "Turkey", "Ukraine", "United Kingdom",
			"Vatican City (Holy See)"

	);

	
	public static void sparkSQLwithList(SparkSession spark, String pathIn, String pathOut) {
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
			winesDataFrame.createOrReplaceTempView("wines");
			Dataset<Row> results = spark.sql("SELECT region, AVG(hue) FROM wines GROUP BY region");
			results.show();

			JavaSparkContext ctx = new JavaSparkContext(spark.sparkContext());
			Integer position = 0;
			String wholeString = "";
			for (String country : countries) {
				wholeString = position + "," + country + "\n";
				position++;
			}
			Dataset<Row> x2 = spark.read().text(wholeString);

			List<StructField> fieldsCountries = new ArrayList<>();
			StructField fieldCountry = DataTypes.createStructField("id", DataTypes.IntegerType, true);
			fieldsCountries.add(fieldCountry);
			fieldCountry = DataTypes.createStructField("country_label", DataTypes.StringType, true);
			fieldsCountries.add(fieldCountry);
			StructType schemaCountries = DataTypes.createStructType(fieldsCountries);

			RDD<Row> rows = new Row(1, "a");
			Dataset<Row> countriesDS = spark.createDataFrame(rows, schemaCountries);
			countriesDS.rdd().zipWithIndex()
			//results.join(countries.to);
			/*JavaPairRDD<String, Double> rddOut = rowRDD.mapToPair(linea -> {
				String[] splits = linea.split(",");
				String key = splits[0];
				Double value = Double.parseDouble(splits[4]);
				return new Tuple2<String, Double>(key, value);
			}).reduceByKey((f1,f2) -> f1+f2)
			.foreach(f -> {
				System.out.println(f._1 + " - " + f._2);
			});*/
			
			// possible solution to map ids with their labels
			// Column newCol = when(col("region").equalTo("1"), "Albania");
			// winesDataFrame.toDF().withColumn("region_name", newCol).show();
			
			
			
		} catch (Exception ex) {
			System.out.println("Oops! Something went wrong");
			System.out.println(ex.getMessage());
		}

		
		
	}
}

