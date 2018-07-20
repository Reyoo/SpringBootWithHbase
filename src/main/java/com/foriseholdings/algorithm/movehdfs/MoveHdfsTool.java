package com.foriseholdings.algorithm.movehdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.springframework.stereotype.Component;

@Component
public class MoveHdfsTool {

	public  void move(String srcPath, String dstPath,String hdfs) throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
		conf.set("fs.defaultFS", hdfs);
		FileSystem fs = FileSystem.get(conf);
		Path srct = new Path(srcPath + "/part-r-00000");
		Path dst = new Path(dstPath + "/" + String.valueOf(Math.random()));
		fs.rename(srct, dst);
		System.out.println("success");
	}

}
