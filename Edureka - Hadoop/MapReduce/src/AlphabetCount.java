import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;


public class AlphabetCount {

	/**
	 * @param args
	 */
	public static class AlphaMapper extends Mapper <LongWritable,Text,IntWritable,Text> {
		public void map(LongWritable key,Text value,Context context) throws InterruptedException,IOException{
			int wordLength=0;
			String word;
			String line= value.toString();
			StringTokenizer words = new StringTokenizer(line);
			while(words.hasMoreTokens()){
				word=words.nextToken();
				wordLength=word.length();
				context.write(new IntWritable(wordLength), new Text(word));
				wordLength=0;
			}
		}
	}
	
	public static class AlphaReducer extends Reducer<IntWritable, Text, IntWritable , Text>{
		public void reduce(IntWritable key,Iterable<Text> value,Context context)throws InterruptedException,IOException{
			int count = 0;
			String s = "";
			for (Text t: value){
				count++;
				s = s + "," + t.toString() ;
			}
			s = count + " " + s;
			context.write(key,new Text(s));
		}
		
		
			}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		Job job = new Job(conf,"Alphabet Count");
		job.setJarByClass(AlphabetCount.class);
		job.setMapperClass(AlphaMapper.class);
		job.setReducerClass(AlphaReducer.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		
		Path outputPath = new Path(args[1]);
		outputPath.getFileSystem(conf).delete(outputPath,true);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    		
		System.exit(job.waitForCompletion(true) ? 0 :1);
		
	}

}
