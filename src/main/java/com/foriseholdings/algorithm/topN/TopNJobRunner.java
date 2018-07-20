package com.foriseholdings.algorithm.topN;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foriseholdings.algorithm.topN.mapper.TopNMapper;
import com.foriseholdings.algorithm.topN.mapper.TopNResultMapper;
import com.foriseholdings.algorithm.topN.reducer.TopNReducer;
import com.foriseholdings.algorithm.topN.reducer.TopNResultReducer;
import com.foriseholdings.algorithm.write2hive.ResultWriteHive;
import com.foriseholdings.algorithm.write2hive.WriteToHiveRunMain;
import com.foriseholdings.common.common.BaseMapper;
import com.foriseholdings.common.common.BaseReducer;
import com.foriseholdings.common.common.BaseRunner;

@Component
public class TopNJobRunner extends BaseRunner {

	// @Autowired
	// AboutAddressModel addr;

	protected BaseMapper stepMapper;
	protected BaseReducer stepReducer;
	@Autowired
	ResultWriteHive writeHive;
	// public static void main(String[] args) {
	// TopNJobRunner uj = new TopNJobRunner();
	// }

	public String baseStart(String busCode, String hdfs) {
//		step1(busCode);
		String  path = step2(busCode,hdfs);
		System.out.println("程序正确执行");
		return path;
	}

	public boolean step1(String busCode) {
		String input = "/foriseholdings/Algorithm/parselogs/1006/parselog";
		String output = "/foriseholdings/Algorithm/" + busCode + "/step1_output";
		stepMapper = new TopNMapper();
		stepReducer = new TopNReducer();
		runMain.runTask(stepMapper, stepReducer, "", "step1", input, output, "topn", "topn", "");
		return true;
	}

	public String  step2(String busCode,String hdfs) {

		String inputPath = "/foriseholdings/Algorithm/" + busCode + "/step1_output";
		String outputPath = "/foriseholdings/Algorithm/topN/" + busCode;
		String dstPath = "/user/hive/warehouse/hive_prod_commend";
		stepMapper = new TopNResultMapper();
		stepReducer = new TopNResultReducer();
		runMain.runTask(stepMapper, stepReducer, "", "topNResult", inputPath, outputPath, busCode, "topn", "");
		WriteToHiveRunMain writeHive = new WriteToHiveRunMain();
		writeHive.runTask(outputPath, "/foriseholdings/Algorithm/result/1006/usercf/write2hive");
		// 移动到hive 表中
//			MoveHdfsTool.move(outputPath, dstPath, "hdfs://192.168.92.215:8020");
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
	public boolean step3(String buss_code) {
		return false;
	}

}