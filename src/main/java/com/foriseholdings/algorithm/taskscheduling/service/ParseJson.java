package com.foriseholdings.algorithm.taskscheduling.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.foriseholdings.algorithm.taskscheduling.model.ResultModer;

public class ParseJson {

	static String busCode = null;
	static String execState = null;
	static Date job_time = null;
	static Integer timespan = null;

//	public static void main(String[] args) {
//		String orgJson = "{	\"@type\":\"com.spencer.dap.common.result.BaseResult\",	\"code\":\"000000\",	\"message\":\"业务处理成功！\",	\"result\":\"DAP1001,BC1006\"}";
//		String orgJsonStr = orgJson.replace("@", "\\");
//		getbusCodeInfo(orgJsonStr);
//	}

	public static ResultModer getBaseInfo(String orgJson) {

		JSONObject jsonObj = JSONObject.parseObject(orgJson);
		// [{"jobTime":"2018-01-01","busCode":"BC1006","ftpPath":"/BC1006/d5log/201801/01","id":3006}]
		String successFlag = jsonObj.getString("code");
		JSONArray array = jsonObj.getJSONArray("result");
		
		if (array == null || array.isEmpty()) {
			return null;
		}
		
		// 如果有任務
		List<ResultModer> jobs = JSON.parseArray(array.toString(), ResultModer.class);
		for (ResultModer job : jobs) {
			System.out.println(job.getFtpPath());
			job.setCode(successFlag);
			return job;
		}
		return null;
	}

	// { "@type":"com.spencer.dap.common.result.BaseResult", "code":"000000",
	// "message":"业务处理成功！", "result":"DAP1001,BC1006"}
	public static List<String> getbusCodeInfo(String orgJson) {
		List<String> busList = new ArrayList<String>();
		JSONObject jsonObj = JSONObject.parseObject(orgJson);
		// [{"jobTime":"2018-01-01","busCode":"BC1006","ftpPath":"/BC1006/d5log/201801/01","id":3006}]
		String successFlag = jsonObj.getString("code");
		String busCodes = jsonObj.getString("result");

		if (!successFlag.equals("000000")) {
			return null;
		}
		if (busCodes.contains(",")) {
			for (String busCode : busCodes.split(",")) {
				busList.add(busCode);
			}
		} else {
			busList.add(busCodes);
		}

		return busList;
	}


}
