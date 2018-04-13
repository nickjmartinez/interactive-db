import java.sql.*;
import java.util.Scanner;

public class DBInputer {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String db;
		String sql, list[];
		ResultSet rs;

		System.out.print("Enter the database to connect to: ");
		db = input.nextLine();

		DBConnection dbc = new DBConnection(db,"root","root");
		dbc.openConnection();

		String instructions = "Enter the operation you would like to perform:\n"
				+ "(1) Show tables\n"
				+ "(2) Insert SQL Statement\n"
				+ "(3) Insert into Tables\n"
				+ "(4) Quit"
				+ "(5) Commands\n";
		String table = "Employees";
		String task;

		boolean run = true;
		while(run)
		{
			//System.out.print(instructions);
			System.out.print("Enter task: ");
			task = input.nextLine();

			switch (task) {
			case "print":
				rs = dbc.query("SELECT * FROM "+table+";", null);
				try { 
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					while (rs.next()) {
						for (int i = 1; i <= columnsNumber; i++) {
							if (i > 1) System.out.print(",  ");
							String columnValue = rs.getString(i);
							System.out.print(columnValue);
						}
						System.out.println("");
					}
					System.out.println("");
				}
				catch ( SQLException sqle ) {}
				break;
			case "done":
				System.out.println("Bye");
				run = false;
				break;
			case "change":
				System.out.print("Which table? ");
				table = input.nextLine();
				break;
			case "where":
				System.out.println("In table: "+table);
				break;
			case "count":
				rs = dbc.query("SELECT * FROM "+table+";", null);
				int count = 0;
				try {
					while (rs.next()) {
						count++;
					}
					System.out.println("# in table: "+count);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "a":
				sql = "INSERT INTO " + table + " VALUES (";
				rs = dbc.query("SELECT * FROM "+table+";", null);
				try { 
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					String cnames[] = new String[columnsNumber];
					System.out.print("Entering values for these columns: ");
					while (rs.next()) {
						for (int i = 1; i <= columnsNumber; i++) {
							if (i > 1) System.out.print(",  ");
							String columnValue = rs.getString(i);
							System.out.print(rsmd.getColumnName(i));
							cnames[i-1] = rsmd.getColumnName(i);
						}
						break;
					}
					System.out.println();
					
					for(int i =0; i < columnsNumber; i++) {
						System.out.print(cnames[i]+ ": ");
						sql += "'" + input.nextLine() + "'";
						if(i != columnsNumber -1) {
							sql += ",";
						}
					}
					sql += ");";
					dbc.update(sql);
				}catch ( SQLException sqle ) {}
				
				break;
			default:
				System.out.println("Yeah go ahead and try that again, sport...");
				break;
			}
			System.out.println("-----------------------------------------------------");
		}



	}

}
