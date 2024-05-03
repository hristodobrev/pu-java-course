import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	static Connection connection = null;
	
	static Connection getConnection() {
		try	{
			Class.forName("org.h2.Driver");
			//connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/C:\\Users\\Daniela\\Desktop\\h2db\\db", "sa", "123456");
			connection = DriverManager.getConnection("jdbc:h2:~./../db/data", "sa", "123456");
		} catch(ClassNotFoundException e) {
			System.out.println("Driver could not connect to the DB:");
			System.out.println(e.getMessage());
		} catch(SQLException e) {
			System.out.println("Error while connecting to DB:");
			System.out.println(e.getMessage());
		}
		
		return connection;
	}
}
