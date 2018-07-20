package com.foriseholdings.algorithm.Itemcf.runmain;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.foriseholdings.algorithm.runMain.RunMethod;
import com.foriseholdings.wordcount.IntSumReducer;
import com.foriseholdings.wordcount.TokenizerMapper;
import com.foriseholdings.wordcount.WordCount;

public class ItemCfJobRunner1 {

	public static void main(String[] args) {
		WordCount wc = new WordCount();
		TokenizerMapper tm = new TokenizerMapper();
		IntSumReducer ir = new IntSumReducer();
		String inPath = "/foriseholdings/wordcount";
		String outPath = "/foriseholdings/test";
		// wc.runTask(tm, ir, inPath, outPath);
		wc.runTask(tm, ir, inPath, outPath);

	}

	public int runTask(TokenizerMapper mapClass, IntSumReducer reduceClass, String inPath, String outPath) {
		try {
			// 创建Configutation 对象，用于设置其他选项
			Configuration conf = new Configuration();
			conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
//			conf.set("fs.defaultFS", "hdfs://192.168.92.215:8020");
			conf.set("fs.defaultFS", "hdfs://192.168.92.215:8020");
			// 创建作业对象
			Job job = Job.getInstance(conf, "wordcount");
			// job.setJobName(name);

			// 设置作业jarfile中 主类的名字
			job.setJarByClass(RunMethod.class);
			// 设置job的Mapper类和Reducer类
			job.setMapperClass(mapClass.getClass());
			job.setCombinerClass(reduceClass.getClass());
			job.setReducerClass(reduceClass.getClass());
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			// 设置Reducer 的输出类型
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);

			FileSystem fs = FileSystem.get(conf);

			// 设置输入和输出路径
			Path inputPath = new Path(inPath);
			if (fs.exists(inputPath)) {
				FileInputFormat.addInputPath(job, inputPath);
			}
			Path outputPath = new Path(outPath);
			fs.delete(outputPath, true);
			FileOutputFormat.setOutputPath(job, outputPath);
			return job.waitForCompletion(true) ? 1 : -1;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}
	}