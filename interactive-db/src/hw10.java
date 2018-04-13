import java.sql.ResultSet;
import java.util.Scanner;

public class hw10 {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String db;
		String sql, list[],opcode;
		ResultSet rs;
		boolean run = true;

		DBConnection dbc = new DBConnection("Mall","root","root");
		dbc.openConnection();
		
		String instructions = "Enter the operation you would like to perform:\n"
				+ "(1) Enter SQL Statement\n"
				+ "(2) Show Tables\n"
				+ "(3) Select Table\n"
				+ "(4) Show Columns\n"
				+ "(5) Select Columns\n"
				+ "(6) Help\n"
				+ "(7) Quit";
		System.out.println(instructions);
		System.out.println();
		
		while(run) {
			System.out.print("Enter opcode: ");
			opcode = input.nextLine();
			
			switch (opcode) {
			case "1":
				
				break;
			case "menu":
				System.out.println(instructions);
				break;
			case "6":
			case "help":
				System.out.println();
				System.out.println();
				System.out.println("You can type in opcodes from the main menu at any time.\n"
						+ "Some helpful commands:\n"
						+ "- 1,2..7 \t opcodes from the commands from the main menu\n"
						+ "- menu \t\t display the main menu\n"
						+ "- help \t\t come right back here!\n"
						+ "- quit \t\t exit the program");
				break;
			case "7":
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
	}

}
