import java.sql.ResultSet;
import java.util.Scanner;

public class DBInputer {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String db;
		String sql, list[];
		ResultSet rs;
		
		System.out.print("Enter the database to connect to: ");
		db = input.next();
		
		while(true)
		{
			
			
			if(true) {
				break;
			}
		}
		DBConnection dbc = new DBConnection(db,"root","root");
		

		dbc.openConnection();
		    
		    
	}

}
