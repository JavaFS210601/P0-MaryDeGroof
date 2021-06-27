package main;
import java.sql.*;
import java.util.*;
import org.apache.logging.log4j.*;

public class Main {
	public static void main(String[] args) {
		
		String url = "jdbc:postgresql://localhost:5432/shoporders";
		Properties info = new Properties();
		info.put("user", "postgres");
		info.put("password", "password");
		
		Scanner scan = new Scanner(System.in);
		
		final Logger log = LogManager.getLogger(Main.class);
		
		try (Connection connection = DriverManager.getConnection(url,info); 
				Statement statement = connection.createStatement();){
			
			int menu = 0;
			while (menu != -1) {
				System.out.println("Enter Menu:\n"
						+ "0: Create New Entry\n"
						+ "1: Read Data\n"
						+ "2: Update Existing Entry\n"
						+ "3: Delete Entry\n"
						+ "-1: Exit Program\n");
				menu = scan.nextInt();
				scan.nextLine();
				switch (menu) {
					//Exit
					case -1:
						log.info("User Exiting Program.");
						System.out.println("Exiting...");
						break;
					//Create
					case 0:
						while (menu != -1) {
							log.info("User Entering New Data.");
							System.out.println("Select New Entry Type:\n"
									+ "0: Customer\n"
									+ "1: Order\n"
									+ "-1: Back\n");
							menu = scan.nextInt();
							scan.nextLine();
							switch(menu) {
								case 0:
									System.out.println("Enter Customer First Name:");
									String first = scan.nextLine();
									System.out.println("Enter Customer Last Name:");
									String last = scan.nextLine();
									System.out.println("Enter Customer Affiliation:");
									String aff = scan.nextLine();
									int id = getFirstAvailable(statement, 0);
									String cmd = "INSERT INTO customers (customer_id, first_name, last_name, affiliation) VALUES (" + id + ", '" + first + "', '" + last + "', '" + aff + "');";
									statement.execute(cmd);
									log.info("User Entered New Customer.");
									System.out.println("Adding new customer: " + first + " " + last + " with customer ID: " + id + ", affiliated with: " + aff);
									break;
								case 1:
									System.out.println("Enter Item Name:");
									String item = scan.nextLine();
									System.out.println("Enter Amount:");
									int amount = scan.nextInt();
									scan.nextLine();
									System.out.println("Enter Customer ID:");
									int cid = scan.nextInt();
									System.out.println("Enter Total Cost:");
									int cost = scan.nextInt();
									id = getFirstAvailable(statement, 1);
									cmd = "INSERT INTO orders (order_id, item, amount, customer_id, price_total) VALUES (" + id + ", '" + item + "', " + amount + ", " + cid + ", " + cost + ");";
									statement.execute(cmd);
									log.info("User Entered New Order.");
									System.out.println("Adding new order with ID: " + id + " of " + amount + " " + item + " for customer with ID: " + cid + ", for a total cost of: " + cost);
									break;
								case -1:
									break;
								default:
									System.out.println("Command Not Recognized.");
									log.warn("User Entered Incorrect Command.");
							}
						}
						menu = 0;
						break;
					//Read
					case 1:
						while (menu != -1) {
							log.info("User Reading Data.");
							System.out.println("Select Read Data Type:\n"
									+ "0: Customer\n"
									+ "1: Order\n"
									+ "-1: Back\n");
							menu = scan.nextInt();
							scan.nextLine();
							switch(menu) {
								case 0:
									System.out.println("Enter Customer ID or 0 for whole table:");
									int id = scan.nextInt();
									scan.nextLine();
									if (id > 0) {
										String cmd = "SELECT * FROM customers WHERE customer_id = " + id + ";";
										ResultSet res = statement.executeQuery(cmd);
										while (res.next()) {
											System.out.println("ID: " + res.getInt(1) + "\t| Name: " + res.getString(2) + " " + res.getString(3) + "\t| Affiliated With: " + res.getString(4));
										}
									}
									else {
										ResultSet res = statement.executeQuery("Select * FROM customers;");
										while (res.next()) {
											System.out.println("ID: " + res.getInt(1) + "\t| Name: " + res.getString(2) + " " + res.getString(3) + "\t| Affiliated With: " + res.getString(4));
										}
									}
									break;
								case 1:
									System.out.println("Enter Order ID or 0 for whole table:");
									id = scan.nextInt();
									scan.nextLine();
									if (id > 0) {
										String cmd = "SELECT * FROM orders WHERE order_id = " + id + ";";
										ResultSet res = statement.executeQuery(cmd);
										while (res.next()) {
											System.out.println("ID: " + res.getInt(1) + "\t| Item: " + res.getString(2) + "\t| Amount: " + res.getInt(3) + "\t| Customer ID: " + res.getInt(4) + "\t| Price Total: " + res.getInt(5));
										}
									}
									else {
										ResultSet res = statement.executeQuery("Select * FROM orders;");
										while (res.next()) {
											System.out.println("ID: " + res.getInt(1) + "\t| Item: " + res.getString(2) + "\t| Amount: " + res.getInt(3) + "\t| Customer ID: " + res.getInt(4) + "\t| Price Total: " + res.getInt(5));
										}
									}
									break;
								case -1:
									break;
								default:
									System.out.println("Command Not Recognized.");
									log.warn("User Entered Incorrect Command.");
							}
						}
						menu = 0;
						break;
					//Update
					case 2:
						while (menu != -1) {
							log.info("User Editing Data.");
							System.out.println("Select Entry Type To Edit:\n"
									+ "0: Customer\n"
									+ "1: Order\n"
									+ "-1: Back\n");
							menu = scan.nextInt();
							scan.nextLine();
							switch(menu) {
								case 0:
									System.out.println("Enter Customer ID:");
									int id = scan.nextInt();
									scan.nextLine();
									String cmd = "SELECT * FROM customers WHERE customer_id = " + id + ";";
									ResultSet res = statement.executeQuery(cmd);
									while (res.next()) {
										System.out.println("ID: " + res.getInt(1) + "\t| Name: " + res.getString(2) + " " + res.getString(3) + "\t| Affiliated With: " + res.getString(4));
									}
									System.out.println("Enter New First Name (- to skip):");
									String upd = scan.nextLine();
									if(!upd.equals("-")) {
										cmd = "UPDATE customers SET first_name = '" + upd + "' WHERE customer_id = " + id + ";";
										statement.execute(cmd);
									}
									System.out.println("Enter New Last Name (- to skip):");
									upd = scan.nextLine();
									if(!upd.equals("-")) {
										cmd = "UPDATE customers SET last_name = '" + upd + "' WHERE customer_id = " + id + ";";
										statement.execute(cmd);
									}
									System.out.println("Enter New Affiliation (- to skip):");
									upd = scan.nextLine();
									if(!upd.equals("-")) {
										cmd = "UPDATE customers SET affiliation = '" + upd + "' WHERE customer_id = " + id + ";";
										statement.execute(cmd);
									}
									cmd = "SELECT * FROM customers WHERE customer_id = " + id + ";";
									res = statement.executeQuery(cmd);
									while (res.next()) {
										System.out.println("Entry updated to:\n"
												+ "ID: " + res.getInt(1) + "\t| Name: " + res.getString(2) + " " + res.getString(3) + "\t| Affiliated With: " + res.getString(4) + "\n");
									}
									break;
								case 1:
									System.out.println("Enter Order ID:");
									id = scan.nextInt();
									scan.nextLine();
									cmd = "SELECT * FROM orders WHERE order_id = " + id + ";";
									res = statement.executeQuery(cmd);
									while (res.next()) {
										System.out.println("ID: " + res.getInt(1) + "\t| Item: " + res.getString(2) + "\t| Amount: " + res.getInt(3) + "\t| Customer ID: " + res.getInt(4) + "\t| Price Total: " + res.getInt(5));
									}
									System.out.println("Enter New Item Name (- to skip):");
									upd = scan.nextLine();
									if(!upd.equals("-")) {
										cmd = "UPDATE customers SET item = '" + upd + "' WHERE order_id = " + id + ";";
										statement.execute(cmd);
									}
									System.out.println("Enter New Item Amount (- to skip):");
									upd = scan.nextLine();
									if(!upd.equals("-")) {
										Integer parse = tryParse(upd);
										if(parse != null) {
											cmd = "UPDATE customers SET amount = '" + (int)parse + "' WHERE order_id = " + id + ";";
											statement.execute(cmd);
										}
										else {
											System.out.println("Number not parsed, skipping entry.");
										}
									}
									System.out.println("Enter New Customer ID (- to skip):");
									upd = scan.nextLine();
									if(!upd.equals("-")) {
										Integer parse = tryParse(upd);
										if(parse != null) {
											cmd = "UPDATE customers SET customer_id = '" + (int)parse + "' WHERE order_id = " + id + ";";
											statement.execute(cmd);
										}
										else {
											System.out.println("Number not parsed, skipping entry.");
										}
									}
									System.out.println("Enter New Price Total (- to skip):");
									upd = scan.nextLine();
									if(!upd.equals("-")) {
										Integer parse = tryParse(upd);
										if(parse != null) {
											cmd = "UPDATE customers SET price_total = '" + (int)parse + "' WHERE order_id = " + id + ";";
											statement.execute(cmd);
										}
										else {
											System.out.println("Number not parsed. Skipping entry.");
										}
									}
									cmd = "SELECT * FROM orders WHERE order_id = " + id + ";";
									res = statement.executeQuery(cmd);
									while (res.next()) {
										System.out.println("Entry Changed To:\n"
												+ "ID: " + res.getInt(1) + "\t| Item: " + res.getString(2) + "\t| Amount: " + res.getInt(3) + "\t| Customer ID: " + res.getInt(4) + "\t| Price Total: " + res.getInt(5) + "\n");
									}
									break;
								case -1:
									break;
								default:
									System.out.println("Command Not Recognized");
									log.warn("User Entered Incorrect Command.");
							}
						}
						menu = 0;
						break;
					//Delete
					case 3:
						while (menu != -1) {
							log.info("User Deleting Data.");
							System.out.println("Select Entry Type To Delete:\n"
									+ "0: Customer\n"
									+ "1: Order\n"
									+ "-1: Back\n");
							menu = scan.nextInt();
							scan.nextLine();
							switch(menu) {
								case 0:
									System.out.println("This will also delete all orders by this customer.\n"
											+ "Enter Customer ID:\n");
									int id = scan.nextInt();
									scan.nextLine();
									String cmd = "DELETE FROM customers WHERE customer_id = " + id + ";";
									statement.execute(cmd);
									cmd = "DELETE FROM orders WHERE customer_id = " + id + ";";
									statement.execute(cmd);
									System.out.println("Deleting customer with ID " + id + " and all related orders.");
									log.info("User Deleted Customer and Associated Orders.");
									break;
								case 1:
									System.out.println("Enter Order ID:\n");
									id = scan.nextInt();
									scan.nextLine();
									cmd = "DELETE FROM orders WHERE order_id = " + id + ";";
									statement.execute(cmd);
									System.out.println("Deleting order with ID " + id + ".");
									log.info("User Deleted Order.");
									break;
								case -1:
									break;
								default:
									System.out.println("Command not recognized.");
									log.warn("User Entered Incorrect Command.");
							}
						}
						menu = 0;
						break;
					default:
						System.out.println("Menu Not Recognized");
						log.warn("User Entered Incorrect Command.");
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		scan.close();
	}
	
	//Finds first available index
	public static int getFirstAvailable(Statement stmt, int table) {
		ResultSet res = null;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		try {
			//idea: get column 1 and convert to array, use java to sort
			if(table == 0) {
				res = stmt.executeQuery("SELECT customer_id FROM customers");
				while (res.next()) {
					ids.add(res.getInt(1));
				}
			}
			else if (table == 1) {
				res = stmt.executeQuery("SELECT order_id FROM orders");
				while (res.next()) {
					ids.add(res.getInt(1));
				}
			}
			else {
				return -1;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return findMissing(ids);
	}
	
	//Finds any missing numbers in an arraylist
	public static int findMissing(ArrayList<Integer> ids) {
		//Sort the list
		Collections.sort(ids);
		//Step through the numbers and if they don't match the current cycle, that's the answer
		for(int i = 1; i < ids.size(); i++) {
			if(i != ids.get(i-1)) {
				return i;
			}
		}
		//Otherwise, add 1 to the max;
		return ids.size()+1;
	}
	
	public static Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e){
			return null;
		}
	}
}
