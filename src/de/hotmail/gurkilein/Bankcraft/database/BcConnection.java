package de.hotmail.gurkilein.Bankcraft.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author
 * HarmO
 */
public class BcConnection {

	private Connection conn;
	private BcConnectionPool pool;

	public BcConnection(Connection conn, BcConnectionPool pool) {
		this.conn = conn;
		this.pool = pool;
	}

	public synchronized boolean isClosed() {
		try {
			return conn.isClosed();
		} catch (SQLException e) {
			return true;
		}
	}

	public synchronized boolean isValid(int timeout) throws SQLException {
		try {
			return conn.isValid(timeout);
		} catch (AbstractMethodError e) {
			return true;
		}
	}

	public synchronized void close() {
		pool.returnToPool(this);
	}

	public synchronized void closeConnection() throws SQLException {
		conn.close();
	}

	public synchronized Statement createStatement() throws SQLException {
		return conn.createStatement();
	}

	public synchronized PreparedStatement prepareStatement(String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}
}
