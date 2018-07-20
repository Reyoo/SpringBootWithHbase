package com.foriseholdings.write2mysql.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foriseholdings.common.util.GetJdbcConn;
import com.foriseholdings.model.AboutAddressModel;

/*
 * 后期版本应去掉清空功能，
 * target :动态解析数据,存储数据,更新数据记录
 */
@Component
public class ClearDataTable {

	@Autowired
	AboutAddressModel addr;

	public boolean clearAdsTb() {

		Statement st = null;
		Connection conn = GetJdbcConn.getConnection(addr.getMysqlPath(), addr.getUsername(), addr.getPassword());
		String isNull = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 执行SQL
			String sql2 = "SELECT count(1) as total FROM elep_shopsn_userid";
			pstmt = conn.prepareStatement(sql2.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				isNull = rs.getString("total");
			}

			if (!isNull.equals("0")) {
				st = conn.createStatement();
				String sql = "TRUNCATE TABLE elep_shopsn_userid ";
				int result = st.executeUpdate(sql);
				// 处理结果
				if (result > 0) {
					System.out.println("刪除操作成功");
					return true;
				} else {
					System.out.println("刪除操作失败");
					return false;
				}
			} else {
				System.out.println("无可删除内容");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}