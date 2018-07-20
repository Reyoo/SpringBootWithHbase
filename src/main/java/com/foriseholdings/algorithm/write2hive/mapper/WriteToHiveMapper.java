package com.foriseholdings.algorithm.write2hive.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WriteToHiveMapper extends Mapper<Object, Text, Text, IntWritable> {

	protected Text outKey = new Text();
	private final static IntWritable one = new IntWritable(1);
	String userId = null;
	String shopSn = null;
	String prodIdAndScores = null;
	String type = null;
	String busCode = null;
	String prodId = null;
	String score = null;

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		// StringTokenizer itr = new StringTokenizer(value.toString());
		// while (itr.hasMoreTokens()) {

		String[] values = value.toString().split("\t");
		userId = values[0].split("_")[0];
		shopSn = values[0].split("_")[1];

		prodIdAndScores = values[1];
		String[] prodIdAndScore = prodIdAndScores.split(",");
		type = values[2];
		busCode = values[3];

		for (String info : prodIdAndScore) {
			prodId = info.split("_")[0];
			score = info.split("_")[1];
			outKey.set(userId + "\t" + shopSn + "\t" + prodId + "\t" + score + "\t" + type + "\t" + busCode);
			context.write(outKey, one);
		}

	}
}