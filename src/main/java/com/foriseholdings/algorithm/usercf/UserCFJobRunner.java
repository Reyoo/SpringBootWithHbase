package com.foriseholdings.algorithm.usercf;



import com.foriseholdings.algorithm.Itemcf.mapper.ItemcfMapper6;
import com.foriseholdings.algorithm.Itemcf.reducer.ItemcfReducer6;
import com.foriseholdings.algorithm.usercf.mapper.UsercfMapper2;
import com.foriseholdings.algorithm.usercf.mapper.UsercfMapper3;
import com.foriseholdings.algorithm.usercf.mapper.UsercfMapper4;
import com.foriseholdings.algorithm.usercf.mapper.UsercfMapper5;
import com.foriseholdings.algorithm.usercf.reducer.UsercfReducer2;
import com.foriseholdings.algorithm.usercf.reducer.UsercfReducer3;
import com.foriseholdings.algorithm.usercf.reducer.UsercfReducer4;
import com.foriseholdings.algorithm.usercf.reducer.UsercfReducer5;
import com.foriseholdings.algorithm.write2hive.WriteToHiveRunMain;
import com.foriseholdings.common.common.BaseRunner;

public class UserCFJobRunner extends BaseRunner {

	// @Autowired
	// RunMethod runMain;
	//
	// protected BaseMapper stepMapper;
	// protected BaseReducer stepReducer;

	public static void main(String[] args) {
		UserCFJobRunner uj = new UserCFJobRunner();
		uj.baseStart("1006", "");
	}

	@Override
	public String baseStart(String busCode, String hdfs) {
		boolean flag = false;
		String path = null;
		flag = step2(busCode);

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

		// WriteToHiveRunMain write2hive = new WriteToHiveRunMain();
		// String inPath = "/foriseholdings/Algorithm/result/" + busCode +
		// "/usercf/step6_output";
		// String outPath = "/foriseholdings/Algorithm/result/" + busCode +
		// "/usercf/write2hive";
		// write2hive.runTask(inPath, outPath);
		//
		// String dstPath = "/user/hive/warehouse/hive_prod_commend";
		// try {
		// MoveHdfsTool.move(outPath, dstPath,hdfs);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	// @Override
	// public boolean step1() {
	//
	// // String input = ReturnFileFormat.getPathAddr(userCF_base_Path,
	// // userCF_s1_outPath);
	// String output = ReturnFileFormat.getPathAddr(userCF_base_Path,
	// userCF_s2_outPath);
	// String cache = ReturnFileFormat.getPathAddr(userCF_base_Path, s2_5_cache);
	//
	// stepMapper = new UsercfMapper1();
	// stepReducer = new UsercfReducer1();
	// state = runMain.runTask(stepMapper, stepReducer, "", "step1",
	// userCF_s1_inPath, userCF_s1_outPath);
	//
	// if (state == 1) {
	// System.out.println("Usercf_step1执行成功");
	// }
	// return true;
	// }

	/**
	 * 第二步： 用户相似度矩阵 ----------------相似度
	 */
	@Override
	public boolean step2(String busCode) {

		// String input = ReturnFileFormat.getPathAddr(base_path, parse_outPath,
		// buss_code);
		// String output = ReturnFileFormat.getPathAddr(base_path, userCF_s2_outPath,
		// buss_code);
		// String cache = ReturnFileFormat.getPathAddr(base_path, step2_cache,
		// buss_code);
		try {
			String inPath = "/foriseholdings/Algorithm/parselogs/" + busCode + "/parselog";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step2_output";
			String cache = "/foriseholdings/Algorithm/parselogs/" + busCode + "/parselog/part-r-00000";
			stepMapper = new UsercfMapper2();
			stepReducer = new UsercfReducer2();
			runMain.runTask(stepMapper, stepReducer, cache, "step2", inPath, outPath, busCode, "usercf", "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 第三步： 将评分矩阵转置 输入:步骤1 的输出 输出 :用户ID(行)
	 * ------------物品ID(ID)列---------------------分值
	 */

	@Override
	public boolean step3(String busCode) {
		// String input = ReturnFileFormat.getPathAddr(base_path, parse_outPath,
		// buss_code);
		// String output = ReturnFileFormat.getPathAddr(base_path, userCF_s3_outPath,
		// buss_code);
		try {
			String inPath = "/foriseholdings/Algorithm/parselogs/" + busCode + "/parselog";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step3_output";
			stepMapper = new UsercfMapper3();
			stepReducer = new UsercfReducer3();
			runMain.runTask(stepMapper, stepReducer, "", "step3", inPath, outPath, busCode, "usercf", "");
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
		// String input = ReturnFileFormat.getPathAddr(base_path, userCF_s2_outPath,
		// buss_code);
		// String output = ReturnFileFormat.getPathAddr(base_path, userCF_s4_outPath,
		// buss_code);
		// String cache = ReturnFileFormat.getPathAddr(base_path, s4_cache, buss_code);
		try {
			String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step2_output";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step4_output";
			String cache = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step3_output/part-r-00000";

			stepMapper = new UsercfMapper4();
			stepReducer = new UsercfReducer4();
			runMain.runTask(stepMapper, stepReducer, cache, "step4", inPath, outPath, busCode, "usercf", "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 得到推荐列表
	 * 
	 * @return
	 */
	public boolean step5(String busCode) {
		// String input = ReturnFileFormat.getPathAddr(base_path, userCF_s4_outPath,
		// buss_code);
		// String output = ReturnFileFormat.getPathAddr(base_path, userCF_s5_outPath,
		// buss_code);
		// String cache = ReturnFileFormat.getPathAddr(base_path, step2_cache,
		// buss_code);
		try {
			String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step4_output";
			String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step5_output";
			String cache = "/foriseholdings/Algorithm/parselogs/" + busCode + "/parselog/part-r-00000";

			stepMapper = new UsercfMapper5();
			stepReducer = new UsercfReducer5();
			runMain.runTask(stepMapper, stepReducer, cache, "step5", inPath, outPath, busCode, "usercf", "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public String step6(String busCode) {
		// String input = ReturnFileFormat.getPathAddr(base_path, userCF_s5_outPath,
		// buss_code);
		// String output = ReturnFileFormat.getPathAddr(base_path, userCF_s6_outPath,
		// buss_code);

		String inPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step5_output";
		String outPath = "/foriseholdings/Algorithm/result/" + busCode + "/usercf/step6_output";

		stepMapper = new ItemcfMapper6();
		stepReducer = new ItemcfReducer6();
		runMain.runTask(stepMapper, stepReducer, "", "step6", inPath, outPath, busCode, "usercf", "");

		WriteToHiveRunMain writeHive = new WriteToHiveRunMain();
		writeHive.runTask(outPath, "/foriseholdings/Algorithm/result/1006/usercf/write2hive");
		// 移动到hive 表中
		// MoveHdfsTool.move(outputPath, dstPath, "hdfs://192.168.92.215:8020");
		// MoveHdfsTool.move(outputPath, dstPath,addr.getHdfs());
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

	@Override
	public boolean step1(String buss_code) {
		return false;
	}

}
