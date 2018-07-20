package com.foriseholdings.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AboutAddressModel {

	@Value("${service.buscodes}")
	private String busCode;

	@Value("${hadoop.hdfs.path}")
	private String hdfs;

	@Value("${database.path.addr}")
	private String mysqlPath;

	@Value("${database.path.driver}")
	private String driver;

	@Value("${database.path.username}")
	private String username;

	@Value("${database.path.password}")
	private String password;

	@Value("${database.path.table}")
	private String table;



	public String getMysqlPath() {
		return mysqlPath;
	}

	public void setMysqlPath(String mysqlPath) {
		this.mysqlPath = mysqlPath;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getHdfs() {
		return hdfs;
	}

	public void setHdfs(String hdfs) {
		this.hdfs = hdfs;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

}
