package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.collection.Seq;
import scala.collection.Traversable;
import scala.runtime.AbstractFunction1;
import scala.xml.Node;
import scala.xml.NodeSeq;
import scala.xml.XML;
import scala.Tuple2;

import com.databricks.spark.xml.XmlInputFormat;

public class UtilsXML {

	public static JavaRDD<Seq<Node>> loadMajorTopicsSequence (JavaSparkContext jsc, String filepath) {
		/*
		 * Configuring the parts of the input XML file to read. 
		 * Focusing on the content of the MedlineCitation tags.
		 */
		Configuration conf = new Configuration();
		conf.set(XmlInputFormat.START_TAG_KEY(), "<MedlineCitation ");
		conf.set(XmlInputFormat.END_TAG_KEY(), "</MedlineCitation>");
	    
		/*
		 * Reading the input file, using the XmlInputFormat, and given tags configuration.  
		 */
		JavaPairRDD<LongWritable,Text> medlineRDD = jsc.newAPIHadoopFile(filepath, XmlInputFormat.class, LongWritable.class, Text.class, conf);
		
		/*
		 * Loading input raw XML text into a scala.xml.Node structure.
		 *  		 
		 */
		JavaRDD<Node> mxml = medlineRDD.map(xml_raw -> XML.loadString(xml_raw._2().toString()));

	    /*
	     * Extracting major topics from the citation, i.e., within "DescriptorName" tags, with "MajorTopicYN" attribute set to "Y"
	     */
	    return mxml.map(node -> {
	    	NodeSeq ns = node.$bslash$bslash("DescriptorName");
	    	Traversable<Node> tn = ns.toTraversable().filter(new AbstractFunction1<Node, Object>() {
	    		public Object apply(Node node) {
	    			return node.$bslash("@MajorTopicYN").text().equalsIgnoreCase("Y");
	    		}
	    	});
	    	return tn.toSeq();
	    });
	}
}















