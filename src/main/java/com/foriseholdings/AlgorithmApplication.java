package com.foriseholdings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.foriseholdings.model.AboutAddressModel;
import com.foriseholdings.service.IRecommend;
import com.foriseholdings.sqoop.SqoopLoad;

/**
 * @describe 项目启动入口; 服务启动入口在ScheduledTasks;
 * @author qisun
 * @date 2018年7月4日13:35:57
 */
@SpringBootApplication
// @EnableScheduling
public class AlgorithmApplication implements CommandLineRunner {

	@Autowired
	IRecommend recommend;

	@Autowired
	AboutAddressModel address;

	@Autowired
	SqoopLoad sqoop;	
	
	/**
	 * 服务在定时任务中 ScheduledTasks
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(AlgorithmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		recommend.getTask(address.getBusCode());
//		sqoop.hive2Mysql();
		System.out.println("开始");
	}

}
