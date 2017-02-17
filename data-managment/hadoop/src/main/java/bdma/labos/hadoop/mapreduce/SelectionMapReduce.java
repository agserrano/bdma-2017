package bdma.labos.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SelectionMapReduce extends JobMapReduce {
	
	public static class SelectionMapper extends Mapper<Text, Text, Text, Text> {
		
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
			// CODE HERE
		}
		
	}
	
	public SelectionMapReduce() {
		this.input = null;
		this.output = null;
	}
	
	public boolean run() throws IOException, ClassNotFoundException, InterruptedException {
		Configuration configuration = new Configuration();
		
		// Define the new job and the name it will be given
		Job job = Job.getInstance(configuration, "Selection");
		job.setJarByClass(SelectionMapReduce.class);
		
		// Set the mapper class it must use
		job.setMapperClass(SelectionMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		// No combiner or reducer classes for this little example
		
		// The output will be LongWritable and Text
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    // The files the job will read from/write to
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    FileInputFormat.addInputPath(job, new Path(this.input));
	    FileOutputFormat.setOutputPath(job, new Path(this.output));
	    
	    // These are the parameters that we are sending to the job
	    // CODE HERE
	    
	    // Let's run it!
	    return job.waitForCompletion(true);
	}
	
}
