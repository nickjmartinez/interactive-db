import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class hw10 {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String sql,opcode,cols,table = "";
		ResultSet rs;
		boolean run = true;

		DBConnection dbc = new DBConnection("Mall","root","root");

		if(dbc.openConnection()){

			String instructions = "Enter the operation you would like to perform:\n"
					+ "(1) Enter SQL Query\n"
					+ "(2) Enter SQL Update\n"
					+ "(3) Show Tables\n"
					+ "(4) Select Table\n"
					+ "(5) Show Columns\n"
					+ "(6) Select Columns\n"
					+ "(7) Help\n"
					+ "(8) Quit";
			System.out.println(instructions);
			System.out.println();

			while(run) {
				System.out.print("Enter opcode: ");
				opcode = input.nextLine();

				switch (opcode) {
				case "1":
					System.out.print("Enter SQL query to submit: ");
					sql = input.nextLine();
					rs = dbc.query(sql, null);
					printResultSet(rs);
					break;
				case "2":
					System.out.print("Enter SQL update to submit: ");
					sql = input.nextLine();
					dbc.update(sql);
					break;
				case "3" :
					System.out.println();
					System.out.println("Tables in Mall: ");
					System.out.println("---------------");
					rs = dbc.query("SHOW TABLES", null);
					printResultSet(rs);
					break;
				case "4":
					System.out.print("Enter a table: ");
					table = input.nextLine();
					rs = dbc.query("SELECT * FROM "+ table + ";", null);
					//System.out.println("Columns in "+ table + ": ");
					System.out.println();
					printColumns(rs);
					System.out.println("-----------------------");
					printResultSet(rs);
					System.out.println();
					System.out.println("Hint: if you would like specific columns from this table, enter 6 below...");
					break;
				case "5":
					if(table.equals("")) {
						System.out.print("Enter a table: ");
						table = input.nextLine();
					}
					rs = dbc.query("SELECT * FROM "+ table + ";", null);
					System.out.println("Columns in "+ table + ": ");
					System.out.println();
					printColumns(rs);
					break;

				case "6":
					if(table.equals("")) {
						System.out.print("Enter a table: ");
						table = input.nextLine();
					}
					System.out.print("Enter the columns you would like from "+ table + " (separate using commas): ");
					cols = input.nextLine();
					rs = dbc.query("SELECT " +cols + " FROM "+ table + ";", null);
					System.out.println();
					printColumns(rs);
					System.out.println("-----------------------");
					printResultSet(rs);
					break;
				case "menu":
					System.out.println();
					System.out.println(instructions);
					break;
				case "7":
				case "help":
					System.out.println();
					
					System.out.println("You can type in opcodes from the main menu at any time.\n"
							+ "Some helpful commands:\n"
							+ "- 1,2..8 \t opcodes from the commands from the main menu\n"
							+ "- menu \t\t display the main menu\n"
							+ "- help \t\t come right back here!\n"
							+ "- quit \t\t exit the program");
					break;
				case "8":
				case "quit":
					System.out.println("Bye");
					run = false;
					break;
				case "etc":
					System.out.println("Funny...");
					break;
				default:
					System.out.println("Please enter a valid opcode (1, 2, help, etc...)");
					break;
				}
				System.out.println("-----------------------------------------------------");
			}

			dbc.closeConnection();
		}else {
			System.out.print("Something went wrong...");
		}
		input.close();
	}
	private static void printResultSet(ResultSet rs) {
		try { 
			rs.beforeFirst();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(", ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
				}
				System.out.println("");
			}
			System.out.println("");
		}
		catch ( SQLException sqle ) {}
	}
	private static void printColumns(ResultSet rs) {
		try { 
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(", ");
					String columnValue = rs.getString(i);
					System.out.print(rsmd.getColumnName(i));
				}
				System.out.println("");
				break;
			}
			
		}
		catch ( SQLException sqle ) {}
	}
}
