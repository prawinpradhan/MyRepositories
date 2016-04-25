import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;


public class SubPatentCount {

	public static class SubPatentMapper extends Mapper<LongWritable,Text,IntWritable,IntWritable> {
		public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException {
			String line=value.toString();
			StringTokenizer words=new StringTokenizer(line);
			int patent = new Integer(words.nextToken());
			context.write(new IntWritable(patent),new IntWritable(1) );
		}	
	}
	
	public static class SubPatentReducer extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{
		public void reduce(IntWritable key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException{
			int sum = 0;
			for(IntWritable i: values){
				sum = sum + i.get();			
			}
			context.write(key, new IntWritable(sum));	
			}
		}
		
	public static void main(String[] args) throws IOException,Exception{
		// TODO Auto-generated method stub
		Configuration conf=new Configuration();
		Job job = new Job(conf,"Sub-Patent Counter");
		job.setJarByClass(SubPatentCount.class);
		job.setMapperClass(SubPatentMapper.class);
		job.setReducerClass(SubPatentReducer.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		Path outputPath = new Path(args[1]);
		outputPath.getFileSystem(conf).delete(outputPath,true);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    		
		System.exit(job.waitForCompletion(true) ? 0 :1);
	}
	
}
