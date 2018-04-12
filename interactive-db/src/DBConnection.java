import java.sql.*;

class DBConnection {

  private static final String JDBC_DRIVER = "org.mysql.jdbc.Driver";
  private static final String LOCAL_HOST = "jdbc:mariadb://localhost:3306/";

  private String db;
  private String username;
  private String password;
  // private Statement statement;
  private PreparedStatement preparedStatement;
  private Connection connection;

  public DBConnection() {
    this.db = "festive";
    this.username = "root";
    this.password = "root";
  }

  public DBConnection( String db, String username, String password ) {
    this.db = db;
	this.username = username;
	this.password = password;
  }

  public boolean openConnection( ) {
	  boolean result = false;

	try {
      connection = DriverManager.getConnection( LOCAL_HOST + db, username, password );
      System.out.println( db + " connected." );
      System.out.println();
      
      // statement = connection.createStatement();
      
      result = true;
    }
    catch ( SQLException sqle ) {
      sqle.printStackTrace();
    }
    
    return result;
  }
  public ResultSet query( String sql, String args[] ) {
    ResultSet rs = null;
    int i;

    try {
      // rs = statement.executeQuery( sql );
      
      preparedStatement = connection.prepareStatement( sql );
      
      if ( args != null ) {
        for ( i = 0; i < args.length; i++ ) {
          preparedStatement.setString( ( i + 1 ), args[i] );
	    }
	  }
      
      rs = preparedStatement.executeQuery();
    }
    catch ( SQLException sqle ) {
      sqle.printStackTrace();
    }

    return rs;
  }
 
  public int update( String sql ) {
    int result = 0;

    try {
      //result = statement.executeUpdate( sql );
  
      preparedStatement = connection.prepareStatement( sql );
      result = preparedStatement.executeUpdate();
    }
    catch ( SQLException sqle ) {
      sqle.printStackTrace();
    }

    return result;
  }
  public void closeConnection() {
    try {
      if ( connection != null ) {
        connection.close();
      }
    }
    catch ( SQLException sqle ) {
      sqle.printStackTrace();
    }
  }
}