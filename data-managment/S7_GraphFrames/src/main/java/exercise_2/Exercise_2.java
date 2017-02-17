package exercise_2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.MetadataBuilder;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.graphframes.GraphFrame;
import com.google.common.collect.Lists;

public class Exercise_2 {

	public static void basicGraphframes(JavaSparkContext ctx, SQLContext sqlCtx) {
		Function2 removeHeader= new Function2<Integer, Iterator<String>, Iterator<String>>(){
		    @Override
		    public Iterator<String> call(Integer ind, Iterator<String> iterator) throws Exception {
		        if(ind==0 && iterator.hasNext()){
		            iterator.next();
		            return iterator;
		        }else
		            return iterator;
		    }
		};

		/*************************************************
		 *************** 2.1 Load Graph ******************
		 *************************************************/
		java.util.List<Row> vertices_list = new ArrayList<Row>();
		
		String path = "src/main/resources/people.txt";
		JavaRDD<String> file = ctx.sc().textFile(path, 1).toJavaRDD();
		String firstLine = file.first();
		JavaRDD<String> fileNoHeaders = file.mapPartitionsWithIndex(removeHeader, false);
		
		JavaRDD<Row> parts = fileNoHeaders.map(record -> {
	              String[] attributes = record.split(",");
	              return RowFactory.create(
	            		  Integer.parseInt(attributes[0]),
	            		  attributes[1],
	            		  Integer.parseInt(attributes[2]));
		});
			
		StructType vertices_schema = new StructType(new StructField[]{
			new StructField("id", DataTypes.IntegerType, true, new MetadataBuilder().build()),
			new StructField("name", DataTypes.StringType, true, new MetadataBuilder().build()),
			new StructField("age", DataTypes.IntegerType, true, new MetadataBuilder().build())
		});

		Dataset<Row> vertices = sqlCtx.createDataFrame(parts, vertices_schema);
		
		java.util.List<Row> edges_list = new ArrayList<Row>();
		
		path = "src/main/resources/likes.txt";
		JavaRDD<String> fileEdges = ctx.sc().textFile(path, 1).toJavaRDD();
		String firstLineEdges = fileEdges.first();
			
		JavaRDD<String> fileNoHeaderEdges = fileEdges.mapPartitionsWithIndex(removeHeader, false);

		JavaRDD<Row> partsEdges = fileNoHeaderEdges.map(record -> {
            String[] attributes = record.split(",");
            return RowFactory.create(
            		Integer.parseInt(attributes[0]),
            		Integer.parseInt(attributes[1]),
            		Integer.parseInt(attributes[2]));
		});
			
		StructType edges_schema = new StructType(new StructField[]{
			new StructField("src", DataTypes.IntegerType, true, new MetadataBuilder().build()),
			new StructField("dst", DataTypes.IntegerType, true, new MetadataBuilder().build()),
			new StructField("relationship", DataTypes.IntegerType, true, new MetadataBuilder().build())
		});

		Dataset<Row> edges = sqlCtx.createDataFrame(partsEdges, edges_schema);
		
		GraphFrame gf = GraphFrame.apply(vertices, edges);
		
		System.out.println(gf);
		
		/* 
		 * Load the graphs
		 */
		gf.edges().show();
		gf.vertices().show();
		/*
+---+---+------------+
|src|dst|relationship|
+---+---+------------+
|  2|  1|           7|
|  2|  4|           2|
|  3|  2|           4|
|  3|  6|           3|
|  4|  1|           1|
|  5|  2|           2|
|  5|  3|           8|
|  5|  6|           3|
+---+---+------------+

+---+-------+---+
| id|   name|age|
+---+-------+---+
|  1|  Alice| 28|
|  2|    Bob| 27|
|  3|Charlie| 65|
|  4|  David| 42|
|  5|     Ed| 55|
|  6|   Fran| 50|
+---+-------+---+
		 */

		/*************************************************
		 ************** 2.2 Graph Views ******************
		 *************************************************/
		/* 
		 * Users at least 30 years old
		 */
		gf.vertices().filter(v -> v.getInt(2) > 30).toJavaRDD().
			map(r -> r.getString(1) + " is " + r.getInt(2)).
			collect().
			forEach(var -> System.out.println(var));
		/*
Charlie is 65
David is 42
Ed is 55
Fran is 50
		 */
		
		/*
		 * Who likes who?
		 */
		// gf.triplets().show();

		gf.triplets().toJavaRDD().
			map(r -> r.getStruct(0).getString(1) + " likes " + r.getStruct(2).
			getString(1)).
			collect().
			forEach(var -> System.out.println(var));
		/*
David likes Alice
Bob likes Alice
Charlie likes Fran
Ed likes Fran
Ed likes Charlie
Bob likes David
Charlie likes Bob
Ed likes Bob
		 */
		
		/*
		 * Find the lovers
		 */
		Dataset<Row> relation = gf.find("(a)-[e]->(b)");
        
		relation.groupBy("a.name","b.name").
        	sum("e.relationship").
        	filter("sum(e.relationship)>=5").toJavaRDD().
        	map(r -> r.getString(0) + " loves " + r.getString(1)).
        	collect().
    		forEach(var -> System.out.println(var));
		/*
Ed loves Charlie
Bob loves Alice
		 */
		
		/*************************************************
		 *********** 2.3 Querying the Graph **************
		 *************************************************/
		/*
		 * Youngest user in the graph
		 */	
		vertices.orderBy("age").limit(1).show();
		/*
+---+----+---+
| id|name|age|
+---+----+---+
|  2| Bob| 27|
+---+----+---+
		 */
		
		/*
		 * Number of likes received and given
		 */
		Dataset<Row> a_relation_b = gf.find("(a)-[e]->(b)");
		a_relation_b.groupBy("a.name").
			sum("e.relationship").toJavaRDD().
			map(r -> r.getString(0) + " likes " + r.getLong(1) + " people").
			collect().
			forEach(var -> System.out.println(var));
		a_relation_b.groupBy("b.name").
			sum("e.relationship").toJavaRDD().
			map(r -> r.getString(0) + " is liked by " + r.getLong(1) + " people").
			collect().
			forEach(var -> System.out.println(var));
		/*
Charlie likes 7 people
Bob likes 9 people
David likes 1 people
Ed likes 13 people
Charlie is liked by 8 people
Bob is liked by 6 people
Alice is liked by 8 people
David is liked by 2 people
Fran is liked by 6 people
		 */

		/*************************************************
		 ************* 2.4 Motif Finding *****************
		 *************************************************/
		Dataset<Row> followAll = gf.find("(a)-[]->(b); (b)-[]->(c); (a)-[]->(c)");
		gf.find("(a)-[]->(b); (b)-[]->(c)").
			except(followAll).toJavaRDD().
			map(r -> r.getStruct(0).getString(1) + " should follow to " + r.getStruct(2).getString(1)).
			collect().
			forEach(var -> System.out.println(var));
		/*
Charlie should follow to David
Charlie should follow to Alice
Ed should follow to David
Ed should follow to Alice
		 */
	}

}
