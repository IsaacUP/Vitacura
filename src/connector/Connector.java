package connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {

  public static Connection getConnection() {
    Connection conn;
    try {
      conn = DriverManager.getConnection(
          "jdbc:mysql://localhost/vivero?useUnicode=true&useJDBCCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
          "root", "password");
      return conn;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
