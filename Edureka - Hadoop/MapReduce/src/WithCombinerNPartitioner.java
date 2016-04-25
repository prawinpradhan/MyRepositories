import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;

public class WithCombinerNPartitioner {

	/**
	 * @param args
	 */
	public static class WordMapper extends Mapper<LongWritable,Text,Text,IntWritable> {
		public void map (LongWritable key, Text value,Context context) throws InterruptedException, IOException{
			String line=value.toString();
			StringTokenizer words=new StringTokenizer(line);
			while(words.hasMoreTokens()){
				//value.set(words.nextToken());
				context.write(new Text(words.nextToken()),new IntWritable(1) );	
			}
		}
	}
	
	public static class WordCombiner extends Reducer<Text,IntWritable,Text,IntWritable>{
		public void reduce(Text key,Iterable<IntWritable> value,Context context)throws IOException,InterruptedException{
			int sum=0;
			for(IntWritable a:value){
			   sum = sum + a.get();				
			}
			context.write(key, new IntWritable(sum));
		}
	}
	
	public static class WordReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
		public void reduce(Text key,Iterable<IntWritable> value,Context context)throws IOException,InterruptedException{
			int sum=0;
			for(IntWritable a:value){
			   sum = sum + a.get();				
			}
			context.write(key, new IntWritable(sum));
		}
	}
	
	public static class WordPartitioner extends Partitioner<Text, IntWritable>{
		@Override
		public int getPartition(Text key, IntWritable value, int numOfPartitioner) {
			// TODO Auto-generated method stub
			String myKey = key.toString();
			if(myKey.equals("Hi"))
				return 0;
			else if(myKey.equals("How"))
				return 1;
			else 
				return 2;
			
		}
	}
	

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf);
		job.setNumReduceTasks(3);
		job.setJarByClass(WithCombinerNPartitioner.class);
		job.setMapperClass(WordMapper.class);
		job.setReducerClass(WordReducer.class);
		job.setCombinerClass(WordReducer.class);
		job.setPartitionerClass(WordPartitioner.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		Path path=new Path(args[1]); //Delete output directory if present
		path.getFileSystem(conf).delete(path, true);
		FileInputFormat.setInputPaths(job,args[0]);
		FileOutputFormat.setOutputPath(job,path);
		System.exit(job.waitForCompletion(true)?0:1);
		}

}
