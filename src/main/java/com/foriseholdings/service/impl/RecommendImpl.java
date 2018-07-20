package com.foriseholdings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foriseholdings.algorithm.Itemcf.ItemCFJobRunner;
import com.foriseholdings.algorithm.topN.TopNJobRunner;
import com.foriseholdings.algorithm.usercf.UserCFJobRunner;
import com.foriseholdings.algorithm.write2hive.ResultWriteHive;
import com.foriseholdings.common.common.BaseRunner;
import com.foriseholdings.model.AboutAddressModel;
import com.foriseholdings.service.IRecommend;

@Component
public class RecommendImpl implements IRecommend {

	// @Autowired
	// ClearDataTable clearDataTable;

	@Autowired
	AboutAddressModel addr;

	@Autowired
	ResultWriteHive writeHive;
	// @Autowired
	// TopNJobRunner topn;

	// public static void main(String[] args) {
	// RecommendImpl rm = new RecommendImpl();
	// rm.getTask("1006");
	// }
	String dstPath = "/user/hive/warehouse/hive_prod_commend";
	BaseRunner baseRun = new BaseRunner();

	@Override
	public boolean getTask(String codes) {
		System.out.println(new Date());
		BaseRunner base;
		List<String> list = new ArrayList<String>();
		if (codes.contains(",")) {
			list = java.util.Arrays.asList(codes.split(","));
		} else {
			list.add(codes);
		}

		String resultflag = "true";
		boolean flag = false;
		for (String busCode : list) {
			// System.out.println(addr.getHdfs());

			// System.out.println("测试输出");
			// 解析日志 解析输出结果 是所有算法的输入， 解析输入 是上传路径
			// clearDataTable.clearAdsTb(busCode);
			// topN算法
			if (resultflag.equals("true")) {
				baseRun = new TopNJobRunner();
				String outputPath = baseRun.baseStart(busCode, addr.getHdfs());
				writeHive.writeFinalHiveTable(outputPath, dstPath, addr.getHdfs());
				flag = true;
			}
			// 基于物品
			if (true) {
				baseRun = new ItemCFJobRunner();
				String outputPath = baseRun.baseStart(busCode, addr.getHdfs());
				writeHive.writeFinalHiveTable(outputPath, dstPath, addr.getHdfs());
				flag = true;
			}
			if (true) {
				baseRun = new UserCFJobRunner();
				String outputPath = baseRun.baseStart(busCode, addr.getHdfs());
				writeHive.writeFinalHiveTable(outputPath, dstPath, addr.getHdfs());
				flag = true;
			}
			// sqoopLoad.hive2Mysql(addr.getHdfs(), addr.getTable(), addr.getMysqlPath(),
			// addr.getUsername(),

			// addr.getPassword());
			// SqoopLoad.hive2Mysql();
			break;
		}
		// clearDataTable.clearAdsTb();

		return flag;
	}
}
