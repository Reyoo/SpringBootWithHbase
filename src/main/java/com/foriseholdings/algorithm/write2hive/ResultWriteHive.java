package com.foriseholdings.algorithm.write2hive;

import java.sql.Connection;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dap.core.hive.HiveDao;
import com.foriseholdings.algorithm.movehdfs.MoveHdfsTool;

@Component
public class ResultWriteHive {

//	static String inPath = "/foriseholdings/Algorithm/topN/1006/";
//	static String outPath = "/foriseholdings/Algorithm/result/1006/usercf/write2hive";
//	static String dstPath = "/user/hive/warehouse/hive_prod_commend";
	@Autowired
	MoveHdfsTool  moveTool;

	public void writeFinalHiveTable(String outPath ,String dstPath,String hdfs) {
		try {
//			WriteToHiveRunMain wc = new WriteToHiveRunMain();
//			wc.runTask(inPath, outPath);
			//hdfs://192.168.92.215:8020
			moveTool.move(outPath, dstPath, hdfs);
			Connection conn = HiveDao.getConnnection("jdbc:hive2://192.168.92.215:10000/default", "root", "fuhua_1234");
			Statement st = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT OVERWRITE TABLE hive_final_commend ");
			sb.append(" select a.userid,b.shopsn,b.prodid,a.score,a.type,b.buscode ");
			sb.append(" from hive_prod_commend a left join hive_shopsn_prod b ");
			sb.append(" on a.buscode = b.buscode and a.shopsn = b.shopsn and a.prodid = b.prodid ");
			sb.append(" where a.buscode = b.buscode and a.shopsn = b.shopsn and a.prodid = b.prodid ");

			st.executeUpdate(sb.toString());
			conn.close();
			System.out.println("插入成功");
			// 移到mysql
			// 先清空数据 再插入
//			sqoopLoad.hive2Mysql();
//			System.out.println("导入数据库成功");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
