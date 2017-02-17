package exercise_3;

import com.clearspring.analytics.util.Lists;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.MetadataBuilder;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.functions;
import org.graphframes.GraphFrame;
import org.graphframes.lib.PageRank;

import java.util.ArrayList;
import java.util.List;

public class Exercise_3 {
	
	public static void wikipedia(JavaSparkContext ctx, SQLContext sqlCtx) {
		/*
		 * Loading the graph
		 */
		java.util.List<Row> vertices_list = new ArrayList<Row>();
		
		String path = "src/main/resources/wiki-vertices.txt";
		JavaRDD<String> file = ctx.sc().textFile(path, 1).toJavaRDD();
		
		JavaRDD<Row> parts = file.map(record -> {
	              String[] attributes = record.split("\t");
	              return RowFactory.create(
	            		  Long.parseLong(attributes[0]),
	            		  attributes[1]);
		});
			
		StructType vertices_schema = new StructType(new StructField[]{
			new StructField("id", DataTypes.LongType, true, new MetadataBuilder().build()),
			new StructField("title", DataTypes.StringType, true, new MetadataBuilder().build())
		});

		Dataset<Row> vertices = sqlCtx.createDataFrame(parts, vertices_schema);
		
		java.util.List<Row> edges_list = new ArrayList<Row>();
		
		path = "src/main/resources/wiki-edges.txt";
		JavaRDD<String> fileEdges = ctx.sc().textFile(path, 1).toJavaRDD();

		JavaRDD<Row> partsEdges = fileEdges.map(record -> {
            String[] attributes = record.split("\t");
            return RowFactory.create(
            		Long.parseLong(attributes[0]),
            		Long.parseLong(attributes[1]));
		});
			
		StructType edges_schema = new StructType(new StructField[]{
			new StructField("src", DataTypes.LongType, true, new MetadataBuilder().build()),
			new StructField("dst", DataTypes.LongType, true, new MetadataBuilder().build())
		});

		Dataset<Row> edges = sqlCtx.createDataFrame(partsEdges, edges_schema);
		
		GraphFrame gf = GraphFrame.apply(vertices, edges);
		
		//gf.edges().show();
		//gf.vertices().show();
		
		/*
		 * Calculate PageRank and show the top10
		 */
		// gf.inDegrees().show();
		long count = gf.edges().count();
		PageRank pRank = gf.pageRank().resetProbability(0.01).maxIter(5);
		List<Row> arrlst = pRank.run().vertices().
			select("id", "title", "pagerank").
			sort("pagerank").
			collectAsList();
		
		System.out.println("Top 10 sites with the higest PageRank:");
		int icount = arrlst.size();
		while (icount + 10 > arrlst.size()) {
			System.out.println("Site: " + arrlst.get(icount - 1).get(1) + " PageRank: " + arrlst.get(icount - 1).get(2));
			icount = icount - 1;
		}
		/*
Top 10 sites with the higest PageRank:
Site: University of California, Berkeley PageRank: 110.52712598634008
Site: Berkeley, California PageRank: 55.795392961628075
Site: Uc berkeley PageRank: 13.61525297265639
Site: Berkeley Software Distribution PageRank: 7.837238288625706
Site: George Berkeley PageRank: 6.980388873608562
Site: Lawrence Berkeley National Laboratory PageRank: 6.953616993312342
Site: Busby Berkeley PageRank: 4.1024762888279
Site: Berkeley Hills PageRank: 4.056837095963461
Site: Xander Berkeley PageRank: 2.598980728082923
Site: Berkeley County, South Carolina PageRank: 2.4300504074028977
		 */
	}
}
