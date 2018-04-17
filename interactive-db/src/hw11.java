import java.sql.*;
import java.util.Scanner;

public class hw11 {
	private DBConnection dbc;
	private boolean run = true;
	int counter;
	private Scanner input;
	private String sql,opcode,table;
	private String [] cols,colGets;
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
	/*-------------------------------------------------------------------
	 *----------------------------Main Method----------------------------
	 *------------------------------------------------------------------*/
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
	/*-------------------------------------------------------------------
	 *----------------------------Menu Method----------------------------
	 *------------------------------------------------------------------*/
	private void menu() {
		switch (opcode.toLowerCase()) {
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
	/*-------------------------------------------------------------------
	 *-----------------------Menu Options Methods------------------------
	 *------------------------------------------------------------------*/
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
		newLine();
		System.out.println("Tables in Mall: ");
		printDivider(20);
		rs = dbc.query("SHOW TABLES", null);
		printRS();
	}
	/*------------------------*/
	private void selectTables() {
		showTables();
		System.out.print("Which table would you like to select: ");
		table = input.nextLine();
		rs = dbc.query("SELECT * FROM "+ table + ";", null);
		printColumnNames();
		printRS();

		System.out.print("Would you like to perform a query and return values in these columns? (Y/N): ");
		String decision = input.nextLine();

		while(!decision.toLowerCase().equals("y") && !decision.toLowerCase().equals("n")) {
			System.out.print("Enter y or n to select columns: ");
			decision = input.nextLine();
		}
		newLine();

		if(decision.equals("y")) {
			colGets = cols;
			search();
			return;
		}


		System.out.print("Would you like to choose specific columns to narrow down table? (Y/N): ");
		decision = input.nextLine();

		while(!decision.toLowerCase().equals("y") && !decision.toLowerCase().equals("n")) {
			System.out.print("Enter y or n to select columns: ");
			decision = input.nextLine();
		}
		newLine();

		if(decision.equals("y")) {
			selectColumns();
		}


	}
	/*------------------------*/
	private void selectColumns() {
		boolean done = false;
		counter = 0;
		String col,getThese = "";
		int colNum;
		try {
			colNum = rs.getMetaData().getColumnCount();
		} catch (SQLException e) {colNum = 0;}
		colGets = new String[colNum];

		System.out.println("Enter the names of the columns, one at a time. Type nothing and press enter to finish.");
		while(!done) {
			if(counter == colNum) {
				System.out.println("Selected every column in table");
				break;
			}

			System.out.print("Enter a column name: ");
			col = input.nextLine();

			if(col.equals("")) {
				done = true;
			}else if(searchForColumn(col)) {
				colGets[counter] = col;
				counter++;
			}else {
				System.out.println("Column doesnt exist...");
			}
		}

		for(int i = 0; i < counter; i++) {
			if(i > 0) getThese += ",";
			getThese += colGets[i] + " ";
		}
		rs = dbc.query("SELECT " + getThese +  " FROM "+ table + ";", null);
		printColumnNames();
		printRS();

		System.out.print("Would you like to perform a query and return values in these columns? (Y/N): ");
		String decision = input.nextLine();

		while(!decision.toLowerCase().equals("y") && !decision.toLowerCase().equals("n")) {
			System.out.print("Enter y or n to select columns: ");
			decision = input.nextLine();
		}
		newLine();

		if(decision.equals("y")) {
			search();
			return;
		}
		if(counter > 1) {
			System.out.print("Would you like to choose specific columns to narrow down table? (Y/N): ");
			decision = input.nextLine();

			while(!decision.toLowerCase().equals("y") && !decision.toLowerCase().equals("n")) {
				System.out.print("Enter y or n to select columns: ");
				decision = input.nextLine();
			}
			newLine();

			if(decision.equals("y")) {
				selectColumns();
			}
		}

	}
	/*------------------------*/
	private void search() {
		String col,name;
		boolean done = false;
		boolean first = true;
		String getThese = "";
		for(int i = 0; i < colGets.length; i++) {
			if(colGets[i] == null) break;
			if(i > 0) getThese += ",";
			getThese += colGets[i] + " ";
		}
		String sql = "SELECT "+ getThese +  " FROM "+ table + " WHERE ";
		newLine();
		System.out.println("Choose one or more columns to search.\nSearch for specific strings or for values in a certain range.\nEntering more than one column will produce a query to match all criteria.\nEnter an empty column to finish");
		newLine();
		while(!done) {
			System.out.print("Enter a column to search: ");
			col = input.nextLine();
			if(col.equals("")){
				done = true;
				sql += ";";
			}else if(searchForColumn(col)) {
				if(first) {first = false;}
				else {
					sql += " AND ";
				}
				String type = getColumnType(col);
				String lowerbound,upperbound;
				if(type.toLowerCase().contains("int") || type.toLowerCase().contains("double") || type.toLowerCase().contains("real")) {
					System.out.print("Enter lower bound of search range for column " + col + ": ");
					lowerbound = input.nextLine();
					System.out.print("Enter upper bound of search range for column " + col + ": ");
					upperbound = input.nextLine();
					sql += col + " >= " + lowerbound + " AND " + col + " <= " + upperbound;
					newLine();
				}else {
					String findMe;
					System.out.print("Enter a value to search for in the column "+ col + ": ");
					findMe = input.nextLine();
					sql += col + " = '" + findMe + "'";
				}
				//System.out.println(sql);
			}else {
				System.out.println("Choose a column from the table...");
			}
		}
		newLine();
		System.out.println("What we are about to do: ");
		System.out.println(sql);
		newLine();
		rs = dbc.query(sql, null);
		printColumnNames();
		printRS();
	}
	/*------------------------*/
	private boolean searchForColumn(String colName) {
		boolean found= false;
		for(int i =0; i < cols.length; i++) {
			if(cols[i].equals(colName)) found = true;
		}
		return found;

	}
	/*------------------------*/
	private String getColumnType(String col) {
		try { 
			rs.beforeFirst();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				if(rsmd.getColumnName(i).equals(col)) {
					return rsmd.getColumnTypeName(i);
				}
			}
		}
		catch ( SQLException sqle ) {}
		return "";
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
	/*-------------------------------------------------------------------
	 *---------------------------Tool Methods----------------------------
	 *------------------------------------------------------------------*/
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
			cols = new String[rsmd.getColumnCount()];
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String col = rsmd.getColumnName(i);
				cols[i-1] = col;
				System.out.print(col);
				for(int j = col.length(); j < longest[i-1] + 2; j++ ) {
					System.out.print(" ");
				}
			}
			newLine();
			printDivider(50);

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
					if(rsmd.getColumnName(i).length() > longest[i-1]) longest[i-1] = rsmd.getColumnName(i).length();
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
