import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

//
// Here are the dbcp-specific classes.
// Note that they are only used in the setupDataSource
// method. In normal use, your classes interact
// only with the standard JDBC API
//
import org.apache.commons.dbcp.BasicDataSource;

//
// Here's a simple example of how to use the BasicDataSource.
//
// For example:
//  java -Djdbc.drivers=oracle.jdbc.driver.OracleDriver \
//       -classpath commons-pool-1.5.6.jar:commons-dbcp-1.4.jar:j2ee.jar:oracle-jdbc.jar:. \
//       PoolingDataSourceExample
//       "jdbc:oracle:thin:scott/tiger@myhost:1521:mysid"
//       "SELECT * FROM DUAL"
//
public class BasicDataSourceExample {

	public static void main(String[] args) {
		// First we set up the BasicDataSource.
		// Normally this would be handled auto-magically by
		// an external configuration, but in this example we'll
		// do it manually.
		//
		System.out.println("Setting up data source.");
		DataSource dataSource = setupDataSource(null);
		System.out.println("Done.");

		//
		// Now, we can use JDBC DataSource as we normally would.
		//
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		try {
			System.out.println("Creating connection.");
			conn = dataSource.getConnection();
			System.out.println("Creating statement.");
			stmt = conn.createStatement();
			System.out.println("Executing statement.");
			rset = stmt.executeQuery("select now()");
			System.out.println("Results:");
			int numcols = rset.getMetaData().getColumnCount();
			while (rset.next()) {
				for (int i = 1; i <= numcols; i++) {
					System.out.print("\t" + rset.getString(i));
				}
				System.out.println("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null)
					rset.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
	}

	public static void printDataSourceStats(DataSource ds) {
		BasicDataSource bds = (BasicDataSource) ds;
		System.out.println("NumActive: " + bds.getNumActive());
		System.out.println("NumIdle: " + bds.getNumIdle());
	}

	public static DataSource setupDataSource(String connectURI) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("123@abc");
		ds.setUrl("jdbc:mysql://localhost:3306");
		return ds;
	}

	public static void shutdownDataSource(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		bds.close();
	}
}