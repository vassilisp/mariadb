package gq.panop.mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

public class DBConnection {
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	public void connectDatabase() throws Exception {
		
		//connection details
		String mysql_server = "192.168.15.202";
		String mysql_database = "testdata";
		String mysql_user = "vasilisp";
		String mysql_pass = "a1b2c3456";
		
		try {
			//load the SQL driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//setup the connection 
			// it will be open until we close it 
			connect = DriverManager.getConnection("jdbc:mysql://" + mysql_server + "/" + mysql_database + "?" + "user=" + mysql_user + "&password=" + mysql_pass);
			
		} catch (Exception e) {
			close();
			throw e;
		}
	}
	
	public void getAllUsers() throws SQLException{
		//statements to issue queries to the database
		
		try {
			//reuse the connection

			statement = connect.createStatement();
		
			//get results in the result set
			resultSet = statement.executeQuery("SELECT DISTINCT userId FROM AuditLog");
		
			writeResultSet(resultSet);
		
			//PreparedStatements can use variables and are faster
			//TODO to be filled later
		
			printRS_columns(resultSet);
		}catch (SQLException e){
			close();
			throw e;
		}
	}
	
	public void getAllUserTransactions(String user) {
		
		try {
			PreparedStatement pst = connect.prepareStatement("SELECT * FROM AccessLog WHERE transactionId IN (SELECT transactionId FROM AuditLog WHERE userId=?");
			
			pst.setString(0, user);
			resultSet = pst.executeQuery();
			writeResultSet(resultSet);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void printRS_columns(ResultSet resultSet) throws SQLException {
		
		//get some general results from the table
		System.out.println("The columns in the table are: ");
		
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i =1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}
	
	
	private void writeResultSet(ResultSet resultSet) throws SQLException {
		while (resultSet.next()){
			for (int i=1; i<=resultSet.getMetaData().getColumnCount(); i++){
				System.out.print(resultSet.getObject(i).toString() + " //   ");
			}
			System.out.println("");
		}
	}
	
	private void writeResultSet__original(ResultSet resultSet) throws SQLException {
		//The cursor of the resultSet is initially before the first data set --> next()
		
		while (resultSet.next()) {
			//get columns either via name or via number, first column starts at 1
			String user = resultSet.getString("MYUSER");
			String webpage = resultSet.getString("webpage");
			String email = resultSet.getString("email");
			
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			
			System.out.println("user : " + user + " || webpage: " + webpage + "  || comment: " + comment);
		}
	}
	
	private void close(){
		try{
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null){
				statement.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e){

		}
	}

}
