package com.foriseholdings.sqoop;

import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.sqoop.Sqoop;
import org.apache.sqoop.tool.ExportTool;
import org.apache.sqoop.tool.SqoopTool;
import org.apache.sqoop.util.OptionsFileUtil;
import org.springframework.stereotype.Component;

@Component
public class SqoopLoad {

	// public static void main(String[] arg) {
	// if (arg[0].equals("1")) {
	// mysql2Hive();
	// } else {
	// hive2Mysql();
	// }
	// }
	// @Autowired
	// AboutAddressModel addr;

	// public static void main(String[] args) {
	// hive2Mysql();
	// }

//	sqoop export  --connect "jdbc:mysql://49.4.69.101:3506/dev_advs?useSSL=false" --update-key "ids,shopsn,prodid,buscode,type" --update-mode allowinsert --lines-terminated-by "\\n" --username root  --password Fuhua_1234  --table elep_commend   --columns "ids,shopsn,prodid,score,type,buscode" --export-dir '/user/hive/warehouse/hive_final_commend/000000_0'
	
	
	@SuppressWarnings("deprecation")
	private  void mysql2Hive(String path, String driver, String username, String password, String table) {
		try {
			String[] args = new String[] { "--connect", path, "--driver", driver, "-username", username, "-password",
					password, "--table", table, "-m", "1", "--target-dir", "java_import_user" };

			String[] expandArguments = OptionsFileUtil.expandArguments(args);

			SqoopTool tool = SqoopTool.getTool("import");

			Configuration conf = new Configuration();
//			conf.set("fs.default.name", "hdfs://192.168.92.215:8020");// 设置HDFS服务地址
			conf.set("fs.default.name", "hdfs://192.168.92.215:8020");// 设置HDFS服务地址
			Configuration loadPlugins = SqoopTool.loadPlugins(conf);

			Sqoop sqoop = new Sqoop((com.cloudera.sqoop.tool.SqoopTool) tool, loadPlugins);
			System.out.println(Sqoop.runSqoop(sqoop, expandArguments));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void hive2Mysql(String hdfs, String table, String mysqlPath, String
	// username, String password) {
	public  void hive2Mysql() {

		Configuration conf = new Configuration();
		// conf.set("fs.default.name", hdfs);
//		conf.set("fs.default.name", "hdfs://192.168.92.215:8020");
		conf.set("fs.default.name", "hdfs://192.168.92.215:8020");
		conf.set("hadoop.job.ugi", "hadooper,hadoopgroup");
		// conf.set("mapred.job.tracker", "master:9001");
		ArrayList<String> list = new ArrayList<String>(); // 定义一个list
		list.add("--table");
		// list.add(table); // mysql中的表。将来数据要导入到这个表中。
		list.add("elep_commend"); // mysql中的表。将来数据要导入到这个表中。
		list.add("--export-dir");
		// list.add("java_import_user"); // hdfs上的目录。这个目录下的数据要导入到a_baat_client这个表中。
		list.add("/user/hive/warehouse/hive_final_commend/"); // hdfs上的目录。这个目录下的数据要导入到a_baat_client这个表中。
		list.add("--connect");
		// list.add(mysqlPath); // mysql的链接
//		list.add("jdbc:mysql://49.4.69.101:3506/dev_advs?useSSL=false"); // mysql的链接
		list.add("jdbc:mysql://192.168.152.5:3506/dev_advs?useSSL=false"); // mysql的链接

		list.add("--username");
		list.add("root"); // mysql的用户名
		list.add("--password");
		list.add("Fuhua_1234"); // mysql的密码

		// list.add("--fields-terminated-by");
		// list.add("\\t"); // 数据的换行符号
		list.add("--driver");
		list.add("com.mysql.jdbc.Driver");
		
		list.add("--lines-terminated-by");
		list.add("\\n"); // 数据的换行符号

		list.add("-m");
		list.add("1");// 定义mapreduce的数量。

		String[] arg = new String[1];
		ExportTool exporter = new ExportTool();
		Sqoop sqoop = new Sqoop(exporter);
		sqoop.setConf(conf);
		arg = list.toArray(new String[0]);
		Sqoop.runSqoop(sqoop, arg);

	}
}
