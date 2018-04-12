
import java.sql.*;

public class TestJDBC {
	public static void main(String[] args) {
	    DBConnection dbc = new DBConnection();
	    String sql, list[];
	    ResultSet rs;

	    dbc.openConnection();

		dbc.update( "DELETE FROM Beers;" );

	    rs = dbc.query( "SELECT * FROM Beers;", null );
	    try {
	      while ( rs.next() ) {
	        System.out.println( "( " + rs.getString( "name" ) + ", " + rs.getInt( "Calories" ) + " ) " );
	      }
	      System.out.println( "That's all the Beers folks!" );
	      System.out.println();
		}
	    catch ( SQLException sqle ) {}

	    sql = "INSERT INTO Beers ( name, brewer, calories ) " +
	          "VALUES " +
	          "(          'Bud', 'Anheuser', 180 ), " +
	          "(    'Bud Light', 'Anheuser', 125 ), " +
	          "(        'Busch', 'Anheuser', 200 ), " +
	          "(       'Corona',    'Crown', 175 ), " +
	          "(       'Modela',    'Crown', 225 ), " +
	          "(          'MGDA',   'Miller', 220 ), " +
	          "( 'Miller Light',   'Miller', 110 ), " +
	          "(   'Wicked Ale',  'Pete\\'s', 200 );";

	    dbc.update( sql );

	    rs = dbc.query( "SELECT * FROM Beers;", null );
	    try {
	      while ( rs.next() ) {
	        System.out.println( "( " + rs.getString( "name" ) + ", " + rs.getInt( "Calories" ) + " ) " );
	      }
	      System.out.println( "That's all the Beers folks!" );
	      System.out.println();
		}
	    catch ( SQLException sqle ) {}

	    sql = "SELECT * FROM Beers WHERE brewer = ?";
	    list = new String[1];
	    list[0] = "Miller";
	    
	    rs = dbc.query( sql, list );
	    try {
	      while ( rs.next() ) {
	        System.out.println( "( " + rs.getString( "name" ) + ", " + rs.getInt( "Calories" ) + " ) " );
	      }
	      System.out.println( "That's all the Beers folks!" );
	      System.out.println();
	    }
	    catch ( SQLException sqle ) {}


	    dbc.closeConnection();
	  }

}
