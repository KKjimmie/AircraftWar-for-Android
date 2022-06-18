import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteJDBC {
    public static void main( String args[] )
    {
        createTable();
    }

    public static void createTable(){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // 创建账户数据库
            c = DriverManager.getConnection("jdbc:sqlite:account.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE ACCOUNT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT        NOT NULL," +
                    "ACCOUNT          CHAR(50)  UNIQUE NOT NULL," +
                    "PASSWORD        CHAR(50)   NOT NULL," +
                    "CREDITS         INT        NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
