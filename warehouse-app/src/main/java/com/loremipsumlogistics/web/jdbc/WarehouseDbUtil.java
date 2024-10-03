package com.loremipsumlogistics.web.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


public class WarehouseDbUtil {

private DataSource dataSource;
	
	public WarehouseDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Item> searchItems (String searchTerm) throws Exception{
		
		List<Item> items = new ArrayList<>();
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		// check searchTerm wildcard
		if(searchTerm.contains("*")) {
			searchTerm = searchTerm.replace("*", "%");
		}
		
		try {
			myConn = dataSource.getConnection();
			//create SQL statements
				String sql = "select * from products where ProductName like ?";
				myStmt = myConn.prepareStatement(sql);
				myStmt.setString(1, "%" + searchTerm + "%");
				// execute SQL
				myRs = myStmt.executeQuery();
			// Process the result
				while (myRs.next()) {
					//retrieve data from Rs row
					int productNumber = myRs.getInt("ProductNumber");
					String productName = myRs.getString("ProductName");
					String productDescription = myRs.getString("ProductDescription");
					double retailPrice = myRs.getInt("RetailPrice");
					int quantityOnHand = myRs.getInt("QuantityOnHand");
					int categoryID = myRs.getInt("CategoryID");
					
					//create new Student bean
					Item tempItem = new Item(productNumber, productName, productDescription, retailPrice, quantityOnHand, categoryID);
					//add bean to List<Student>
					items.add(tempItem);
				}
			
			return items;
		} 
		 finally {
			 close(myConn, myStmt, myRs); 
		 }
		
	}
	
	public void addItem(Item newItem) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//get db conn
			myConn = dataSource.getConnection();
			//create SQL statement
			String sql = "insert into products" +
							"(ProductNumber, ProductName, ProductDescription, RetailPrice, QuantityOnHand, CategoryID) " +
							"values (?, ?, ?, ?, ?, ?)";
			myStmt = myConn.prepareStatement(sql);
			//set param values for item
			myStmt.setInt(1, newItem.getProductNumber());
			myStmt.setString(2, newItem.getProductName());
			myStmt.setString(3, newItem.getProductDescription());
			myStmt.setDouble(4, newItem.getRetailPrice());
			myStmt.setInt(5, newItem.getQuantityOnHand());
			myStmt.setInt(6, newItem.getCategoryID());
			
			// execute SQL
			myStmt.execute();
			
		} finally {
			close(myConn, myStmt, null); 
		}
		
	}
	
	
	
	public void deleteItem(int productNumber) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//get db conn
			myConn = dataSource.getConnection();
		
			//create SQL statement
			String sqlVendors = "delete from product_vendors where ProductNumber =?";
			myStmt = myConn.prepareStatement(sqlVendors);
			//set param values for item
			myStmt.setInt(1, productNumber);
			// execute SQL
			myStmt.execute();
			
			//create SQL statement
			String sqlOrders = "delete from order_details where ProductNumber =?";
			myStmt = myConn.prepareStatement(sqlOrders);
			//set param values for item
			myStmt.setInt(1, productNumber);
			// execute SQL
			myStmt.execute();
			
			//create SQL statement
			String sqlProducts = "delete from Products where ProductNumber=?";
			myStmt = myConn.prepareStatement(sqlProducts);
			//set param values for item
			myStmt.setInt(1, productNumber);
			// execute SQL
			myStmt.execute();
			
		} finally {
			close(myConn, myStmt, null); 
		}
		
	}
	
//	
	
	public void updateItem(Item itemInfo) throws Exception{ 
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//get db conn
			myConn = dataSource.getConnection();
			//create SQL statement
			//update products set ProductName="bob", ProductDescription="bob", RetailPrice=1, QuantityOnHand=1, CategoryID=1 where productNumber=42;
			String sql = "update products set ProductName=?, ProductDescription=?, RetailPrice=?, QuantityOnHand=? where productNumber=?";
			myStmt = myConn.prepareStatement(sql);
			//set param values for item
			myStmt.setString(1, itemInfo.getProductName());
			myStmt.setString(2, itemInfo.getProductDescription());
			myStmt.setDouble(3, itemInfo.getRetailPrice());
			myStmt.setInt(4, itemInfo.getQuantityOnHand());
			myStmt.setInt(5, itemInfo.getProductNumber());
			
			// execute SQL
			myStmt.execute();
			
		} finally {
			close(myConn, myStmt, null); 
		}
	}
	
	public int fetchProductNumber() throws Exception{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();
			//create SQL statements
				String sql = "select ProductNumber from Products order by ProductNumber DESC limit 1";
				myStmt = myConn.createStatement();
			// Execute SQL query
				myRs = myStmt.executeQuery(sql);
			// Process the result
				myRs.next();
				int newProductNumber = myRs.getInt(1) +1;
				
				
			return newProductNumber;
		} 
		 finally {
			 close(myConn, myStmt, myRs); 
		 }
	}
	
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs!=null){
				myRs.close();
			}
			
			if (myStmt!=null){
				myStmt.close();
			}
			
			if (myConn!=null){
				myConn.close();
			}
			
			
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
}
