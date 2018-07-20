package com.foriseholdings.algorithm.write2hive;

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
import com.foriseholdings.algorithm.write2hive.mapper.WriteToHiveMapper;
import com.foriseholdings.algorithm.write2hive.reducer.WriteToHiveReducer;

/**
 * @describe 将 解析后文本放到 hive 的hdfs 目录下 。 在hive 表中即可用select 语句查询
 * @author DELL
 */

public class WriteToHiveRunMain {

//	public static void main(String[] args) {
//		WriteToHiveRunMain wr = new WriteToHiveRunMain();
//	}

	public int runTask(String inPath, String outPath) {
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
			job.setMapperClass(WriteToHiveMapper.class);
			job.setCombinerClass(WriteToHiveReducer.class);
			job.setReducerClass(WriteToHiveReducer.class);
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
