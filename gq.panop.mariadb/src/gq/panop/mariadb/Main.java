package gq.panop.mariadb;

import gq.panop.mariadb.DBConnection;

public class Main {

	public static void main(String[] args) throws Exception {
		DBConnection connect = new DBConnection();
		connect.connectDatabase();
		connect.getAllUsers();
	}

}
