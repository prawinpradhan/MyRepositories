import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;


public class MyCounter {

	public enum MONTH{
		JAN,
		DEC,
		FEB
		
	}
	
	public static class MyMapper extends Mapper<LongWritable,Text,Text,Text>{
		private Text out = new Text();
		protected void map (LongWritable key,Text value, Context context) throws IOException,InterruptedException{
			String line = value.toString();
			String[] strts = line.split(",");
			long lts = Long.parseLong(strts[1]);
			Date d1 = new Date(lts);
			int m= d1.getMonth();
			if (m == 1 ){
				context.getCounter(MONTH.FEB).increment(1);				
			}
			if (m == 0 ){
				context.getCounter(MONTH.JAN).increment(1);				
			}
			if (m == 11){
				context.getCounter(MONTH.DEC).increment(1);
				}
			}
		}
	
	
	  public static void main(String[] args) 
	                  throws IOException, ClassNotFoundException, InterruptedException {
	    
		  
		Configuration conf = new Configuration();  
	    Job job = Job.getInstance(conf);
	    job.setJarByClass(MyCounter.class);
	    job.setJobName("CounterTest");
	    job.setNumReduceTasks(0);
	    job.setMapperClass(MyMapper.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    
	    job.waitForCompletion(true);
	    
	    Counters counters = job.getCounters();
	    
	    Counter c1 = counters.findCounter(MONTH.DEC);
	    System.out.println(c1.getDisplayName()+ " : " + c1.getValue());
	    c1 = counters.findCounter(MONTH.JAN);
	    System.out.println(c1.getDisplayName()+ " : " + c1.getValue());
	    c1 = counters.findCounter(MONTH.FEB);
	    System.out.println(c1.getDisplayName()+ " : " + c1.getValue());
	    
	  }
	
}
	

