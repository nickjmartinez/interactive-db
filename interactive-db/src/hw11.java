import java.sql.*;
import java.util.Scanner;

public class hw11 {
	private DBConnection dbc;
	private boolean run = true;
	private Scanner input;
	private String sql,opcode;
	private ResultSet rs;
	String instructions = "Enter the operation you would like to perform:\n"
			+ "(1) Enter SQL Statement\n"
			+ "(2) Show Tables\n"
			+ "(3) Select Table\n"
			+ "(4) Help\n"
			+ "(5) Quit";

	public hw11() {
		dbc = new DBConnection("Mall","root","root");
		input = new Scanner(System.in);
	}
	/*----------------------------Main Method----------------------------*/
	public static void main(String[] args) {

		hw11 dbinterface = new hw11();

		if(dbinterface.dbc.openConnection()) {
			System.out.println(dbinterface.instructions);
			dbinterface.printDivider(40);
			while(dbinterface.run) {
				System.out.print("Enter opcode: ");
				dbinterface.opcode = dbinterface.input.nextLine();
				dbinterface.menu();
				dbinterface.printDivider(40);
			}
			dbinterface.dbc.closeConnection();
		}else {
			System.out.println("Failed to open database connection");
		}
		dbinterface.input.close();
	}
	/*----------------------------Menu Method----------------------------*/
	private void menu() {
		switch (opcode) {
		case "1":
			sqlStatement();
			break;
		case "2":
			showTables();
			break;
		case "3":
			selectTables();
			break;
		case "4":
		case "help":
			help();
			break;
		case "5":
		case "quit":
			run = false;
			System.out.println("Bye");
			break;
		case "etc":
			System.out.println("Funny...");
			break;
		case "menu":
			newLine();
			System.out.println(instructions);
			break;
		default:
			System.out.println("Please enter a valid opcode (1, 2, help, etc...)");
			break;
		}
	}
	/*-----------------------Menu Options Methods------------------------*/
	private void sqlStatement() {
		System.out.print("Is this statement a query (1) or an update (2): ");
		opcode = input.nextLine();

		while(!opcode.equals("1") && !opcode.equals("2")){
			newLine();
			System.out.print("Please enter a valid code (1 or 2): ");
			opcode = input.nextLine();
		}
		newLine();
		System.out.print("Enter SQL statement: ");
		sql = input.nextLine();

		if(opcode.equals("1")) {
			rs = dbc.query(sql, null);
			printColumnNames();
			printRS();		
		}else {
			System.out.println("Updated " + dbc.update(sql) + " rows.");
		}
	}
	/*------------------------*/
	private void showTables() {

	}
	/*------------------------*/
	private void selectTables() {

	}
	/*------------------------*/
	private void help() {
		System.out.println();	
		System.out.println("You can type in opcodes from the main menu at any time.\n"
				+ "Some helpful commands:\n"
				+ "- 1,2..8 \t opcodes from the commands from the main menu\n"
				+ "- menu \t\t display the main menu\n"
				+ "- help \t\t come right back here!\n"
				+ "- quit \t\t exit the program");
	}
	/*---------------------------Tool Methods----------------------------*/
	private void printRS() {
		int[] longest = getLongest();
		try { 
			rs.beforeFirst();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String st = rs.getString(i);
					System.out.print(st);
					for(int j = st.length(); j < longest[i-1] + 2; j++ ) {
						System.out.print(" ");
					}
				}
				newLine();
			}
			newLine();
		}
		catch ( SQLException sqle ) {}
	}
	/*------------------------*/
	private void printColumnNames() {

		newLine();
		int longest[] = getLongest();
		try { 
			rs.beforeFirst();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String col = rsmd.getColumnName(i);
				System.out.print(col);
				for(int j = col.length(); j < longest[i-1] + 2; j++ ) {
					System.out.print(" ");
				}
			}
			newLine();
			printDivider(50);
			newLine();
		}
		catch ( SQLException sqle ) {}
	}
	/*------------------------*/
	private int[] getLongest() {
		int[] longest;

		try { 
			rs.beforeFirst();
			ResultSetMetaData rsmd = rs.getMetaData();
			longest= new int[rsmd.getColumnCount()];
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if(rs.getString(i).length() > longest[i-1]) longest[i-1] = rs.getString(i).length();
				}
			}
		}
		catch ( SQLException sqle ) {longest = null;}

		return longest;
	}
	/*------------------------*/
	private void newLine() {
		System.out.println();
	}
	/*------------------------*/
	private void printDivider(int length) {
		for(int i = 0; i < length; i++) {
			System.out.print("-");
		}
		newLine();
	}
}
