package cn.soyadokio.jrbbws.repository.dao.common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 获取Connection对象
 * @author SoyaDokio
 * @date   2020-05-04
 */
public class DbFactory {

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Properties props = loadProperties("/config/mysql-config.properties");

			// load the Driver class
			Class.forName(props.getProperty("DB_DRIVER_CLASS"));

			// create the connection now
			conn = DriverManager.getConnection(props.getProperty("DB_URL"), props.getProperty("DB_USERNAME"),
					props.getProperty("DB_PASSWORD"));
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	private static Properties loadProperties(String propertyPath) throws java.io.IOException {
		InputStream is = DbFactory.class.getResourceAsStream(propertyPath);
		if (is != null) {
			Properties props = new Properties();
			props.load(is);
			return props;
		} else {
			return null;
		}
	}

}
