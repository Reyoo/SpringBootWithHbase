package com.foriseholdings.write2mysql.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class ShopAndUserWritable implements Writable, DBWritable {

	private String shopsn;
	private String ids;
	private String busCode;
	private String algorithm_type;
	private String algorithm_date;
	private String commend_prod_id;

	public ShopAndUserWritable() {
	}

	public ShopAndUserWritable(String shopsn, String ids, String busCode, String commend_prod_id,
			String algorithm_type, String algorithm_date) {

		this.shopsn = shopsn;
		this.ids = ids;
		this.busCode = busCode;
		this.commend_prod_id = commend_prod_id;
		this.algorithm_type = algorithm_type;
		this.algorithm_date = algorithm_date;

	}

	@Override
	public void write(PreparedStatement statement) throws SQLException {
		int i = 1;
		statement.setString(i++, this.shopsn);
		statement.setString(i++, this.ids);
		statement.setString(i++, this.busCode);
		statement.setString(i++, this.commend_prod_id);
		statement.setString(i++, this.algorithm_type);
		statement.setString(i++, this.algorithm_date);
	}

	@Override
	public void readFields(ResultSet resultSet) throws SQLException {
		int i = 1;
		this.shopsn = resultSet.getString(i++);
		this.ids = resultSet.getString(i++);
		this.busCode = resultSet.getString(i++);
		this.commend_prod_id = resultSet.getString(i++);
		this.algorithm_type = resultSet.getString(i++);
		this.algorithm_date = resultSet.getString(i++);

	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.shopsn);
		out.writeUTF(this.ids);
		out.writeUTF(this.busCode);
		out.writeUTF(this.commend_prod_id);
		out.writeUTF(this.algorithm_type);
		out.writeUTF(this.algorithm_date);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.shopsn = in.readUTF();
		this.ids = in.readUTF();
		this.busCode = in.readUTF();
		this.commend_prod_id = in.readUTF();
		this.algorithm_type = in.readUTF();
		this.algorithm_date = in.readUTF();

	}

	public String toString() {
		return new String(this.shopsn + " " + this.ids + " " + this.busCode + " " + this.commend_prod_id + " "
				+ this.algorithm_type + " " + this.algorithm_date);
	}

}
