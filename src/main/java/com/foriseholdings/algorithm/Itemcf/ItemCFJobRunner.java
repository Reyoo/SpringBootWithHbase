package com.foriseholdings.algorithm.Itemcf;



import com.foriseholdings.algorithm.Itemcf.mapper.ItemcfMapper2;
import com.foriseholdings.algorithm.Itemcf.mapper.ItemcfMapper3;
import com.foriseholdings.algorithm.Itemcf.mapper.ItemcfMapper4;
import com.foriseholdings.algorithm.Itemcf.mapper.ItemcfMapper5;
import com.foriseholdings.algorithm.Itemcf.mapper.ItemcfMapper6;
import com.foriseholdings.algorithm.Itemcf.mapper.ItemcfMapper7;
import com.foriseholdings.algorithm.Itemcf.reducer.ItemcfReducer2;
import com.foriseholdings.algorithm.Itemcf.reducer.ItemcfReducer3;
import com.foriseholdings.algorithm.Itemcf.reducer.ItemcfReducer4;
import com.foriseholdings.algorithm.Itemcf.reducer.ItemcfReducer5;
import com.foriseholdings.algorithm.Itemcf.reducer.ItemcfReducer6;
import com.foriseholdings.algorithm.Itemcf.reducer.ItemcfReducer7;
import com.foriseholdings.algorithm.runMain.RunMethod;
import com.foriseholdings.algorithm.write2hive.WriteToHiveRunMain;
import com.foriseholdings.common.common.BaseRunner;

/*
 * 算法思想：
 * 给用户推荐那些和他们之前喜欢的物品相似的物品
 */

public class ItemCFJobRunner extends BaseRunner {

	/*
	 * @Autowired RunMethod runMain;
	 */

	// protected BaseMapper stepMapper;
	// protected BaseReducer stepReducer;

	RunMethod runMain = new RunMethod();

	int state = -1;

	public static void main(String[] args) {
		ItemCFJobRunner it = new ItemCFJobRunner();
		it.baseStart("1006","");

	}

	@Override
	public String baseStart(String busCode,String hdfs) {
		boolean flag = false;
		String path = null;
		flag = step1(busCode);

		if (flag) {
			flag = step2(busCode);
		} else {
			System.out.println("itemCF 步骤1出错");
			return null;
		}

		if (flag) {
			flag = step3(busCode);
		} else {
			System.out.println("itemCF 步骤2出错");
			return null;
		}

		if (flag) {
			flag = step4(busCode);
		} else {
			System.out.println("itemCF 步骤3出错");
			return null;
		}

		if (flag) {
			flag = step5(busCode);
		} else {
			System.out.println("itemCF 步骤4出错");
			return null;
		}

		if (flag) {
			path = step6(busCode);
			return path;
		} else {
			System.out.println("itemCF 步骤5出错");
			return null;
		}

//		WriteToHiveRunMain write2hive = new WriteToHiveRunMain();
//		String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step6_output";
//		String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/write2hive";
//		write2hive.runTask(inPath, outPath);

//		String dstPath = "/user/hive/warehouse/hive_prod_commend";
//		try {
////			MoveHdfsTool.move(outPath, dstPath,hdfs);
//			MoveHdfsTool.move(outPath, dstPath,"hdfs://192.168.92.215:8020");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	/**
	 * 物品 用户_分值
	 */
	@Override
	public boolean step1(String busCode) {
		// String input = ReturnFileFormat.getPathAddr(base_path, parse_outPath,
		// busCode);
		// String output = ReturnFileFormat.getPathAddr(base_path, itemCF_s1_outPath,
		// busCode);

		try {

			String inPath = "/foriseholdings/Algorithm/parselogs/" + busCode + "/parselog";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step1_output";
			stepMapper = new ItemcfMapper7();
			stepReducer = new ItemcfReducer7();
			runMain.runTask(stepMapper, stepReducer, "", "step1", inPath, outPath, busCode, "itemcf", "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 第二步： 利用评分矩阵，构建物品与物品的相似度矩阵 输入:步骤1的输出 缓存：步骤1的输出 输出:物品ID(行)-------------物品ID(列)
	 * ----------------相似度矩阵
	 */
	@Override
	public boolean step2(String busCode) {
		try {
			String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step1_output";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step2_output";
			String cache = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step1_output/part-r-00000";
			stepMapper = new ItemcfMapper2();
			stepReducer = new ItemcfReducer2();
			runMain.runTask(stepMapper, stepReducer, cache, "step2", inPath, outPath, busCode, "itemcf", "");

			// MysqlSimilarProd.start(outPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 第三步： 将评分矩阵转置 输入:步骤1 的输出 输出 :用户ID(行)
	 * ------------物品ID(ID)列---------------------分值
	 */

	public boolean step3(String busCode) {
		try {
			String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step1_output";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step3_output";
			stepMapper = new ItemcfMapper3();
			stepReducer = new ItemcfReducer3();
			runMain.runTask(stepMapper, stepReducer, "", "step3", inPath, outPath, "", "itemcf", "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 第四步 物品与物品相似度矩阵 X 评分矩阵 (经过步骤3转置) 输入步骤2的输出 缓存:步骤3的输出 输出:物品ID(行)
	 * ----------用户ID(列)---------分值
	 */
	public boolean step4(String busCode) {
		try {
			String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step2_output";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step4_output";
			String cache = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step3_output/part-r-00000";

			stepMapper = new ItemcfMapper4();
			stepReducer = new ItemcfReducer4();
			runMain.runTask(stepMapper, stepReducer, cache, "step4", inPath, outPath, busCode, "itemcf", "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 得到推荐列表 ,用户已经有过行为的商品评分置0
	 * 
	 * @return
	 */
	public boolean step5(String busCode) {
		try {
			String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step4_output";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step5_output";
			String cache = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step1_output/part-r-00000";

			stepMapper = new ItemcfMapper5();
			stepReducer = new ItemcfReducer5();
			runMain.runTask(stepMapper, stepReducer, cache, "step5", inPath, outPath, busCode, "itemcf", "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 外层循环step(6) 前是毫无意义的 ，应该单独把这步拿出来 第六步相对与第五步 只是在输出格式上增加了 业务编码以及类型
	 * 
	 * @param bus_code
	 * @return
	 */
	public String step6(String busCode) {

		// String input = ReturnFileFormat.getPathAddr(base_path, itemCF_s5_outPath,
		// buss_code);
		// String output = ReturnFileFormat.getPathAddr(base_path, itemCF_s6_outPath,
		// buss_code);
		try {
			String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step5_output";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/itemcf/step6_output";
			stepMapper = new ItemcfMapper6();
			stepReducer = new ItemcfReducer6();
			runMain.runTask(stepMapper, stepReducer, "", "step6", inPath, outPath, busCode, "itemcf", "");
			WriteToHiveRunMain writeHive = new WriteToHiveRunMain();
			writeHive.runTask(outPath, "/foriseholdings/Algorithm/result/1006/usercf/write2hive");
			// MysqlShopSnAndUserDBOutput.start(addr.getHdfs() + outPath);

//			WriteToHiveRunMain writeHive = new WriteToHiveRunMain();
//			writeHive.runTask(outPath, "/foriseholdings/Algorithm/result/1006/usercf/write2hive");
			
		} catch (Exception e) {
			return null;
		}
		return "/foriseholdings/Algorithm/result/1006/usercf/write2hive";
	}

	@Override
	public boolean doAlgorithm() {
		return false;
	}

	@Override
	public boolean endPoint() {
		return false;
	}

	// public boolean step7(String busCode) {
	//
	// // String input = ReturnFileFormat.getPathAddr(base_path, itemCF_s5_outPath,
	// // buss_code);
	// // String output = ReturnFileFormat.getPathAddr(base_path, itemCF_s6_outPath,
	// // buss_code);
	// try {
	// String inPath = "/foriseholdings/Algorithm/result/" + busCode +
	// "/itemcf/step6_output";
	// String outPath = "/foriseholdings/Algorithm/result/" + busCode +
	// "/itemcf/step7_output";
	// stepMapper = new ItemcfMapper6();
	// stepReducer = new ItemcfReducer6();
	// runMain.runTask(stepMapper, stepReducer, "", "step7", inPath, outPath,
	// busCode, "itemcf", "");
	// MysqlShopSnAndUserDBOutput.start(hdfs + outPath);
	// } catch (Exception e) {
	// return false;
	// }
	// return true;
	// }

}
