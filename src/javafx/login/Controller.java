package javafx.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {

  @FXML
  AnchorPane anchorLogin;

  @FXML
  TextField usuario;

  @FXML
  PasswordField contrasenia;

  @FXML
  Label errorInicio;

  @FXML
  private void inicioSesion() throws SQLException {
    String SSQL="SELECT * FROM usuario WHERE usuario='"+usuario.getText()+"' AND contrasenia = '"+contrasenia.getText()+"'";

    Connection conn = connector.Connector.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(SSQL);
    if (rs.next()){
      try {
        loadInicio();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    else {
      errorInicio.setVisible(true);
      usuario.clear();
      contrasenia.clear();
    }
  }

  private void loadInicio() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/inicio/sample.fxml"));
    Stage primaryStage = (Stage) anchorLogin.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }
}
