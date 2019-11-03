package clases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import connector.Connector;

public class Padre {

  private Connection conn = Connector.getConnection();

  public boolean existe(String tabla, String value, String where) {

    Statement st;
    ResultSet rs;
    boolean respuesta = false;

    String query = "select * from "+tabla+" where "+where+" = '"+value+"'";
    System.out.println(query);
    try {
      st = conn.createStatement ();
      rs = st.executeQuery(query);
      respuesta = rs.next();
    }catch(Exception e) {
    }
    return respuesta;
  }

  public void executeQuery(String query) {

    Statement st;

    try{
      st = conn.createStatement();
      st.executeUpdate(query);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}
