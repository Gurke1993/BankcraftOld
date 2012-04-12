package de.hotmail.gurkilein.Bankcraft.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author
 * HarmO
 */
class BcConnectionPool {
	private LinkedList<BcConnection> pooledConnections;
	private String url;
	private String user;
	private String password;

	public BcConnectionPool(String driver, String url, String user, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.pooledConnections = new LinkedList<>();
		Class.forName(driver);
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public synchronized BcConnection getConnection() throws SQLException {
		while(!pooledConnections.isEmpty()) {
			BcConnection conn = pooledConnections.remove();
			if (conn.isClosed() || !conn.isValid(1)) {
				try {
					conn.closeConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				continue;
			}
			return conn;
		}
		Connection conn = DriverManager.getConnection(url, user, password);
		return new BcConnection(conn, this);
	}
	
	public synchronized void returnToPool(BcConnection conn){
		pooledConnections.add(conn);
	}

	void closeConnections() {
		while(!pooledConnections.isEmpty()) {
			BcConnection conn = pooledConnections.remove();
			try {
				conn.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
