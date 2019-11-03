package javafx.inicio;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {

  @FXML
  AnchorPane anchorInicio;

  @FXML
  private void loadProductos() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/productos/sample.fxml"));
    Stage primaryStage = (Stage) anchorInicio.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  private void loadReportes() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/reportes/sample.fxml"));
    Stage primaryStage = (Stage) anchorInicio.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  private void loadtipoProductos() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/tipoProductos/sample.fxml"));
    Stage primaryStage = (Stage) anchorInicio.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  private void loadRegistroRiego() throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/javafx/registroRiego/sample.fxml"));
    Stage primaryStage = (Stage) anchorInicio.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  private void loadCalendarioImagenes() throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/javafx/calendarioImagenes/sample.fxml"));
    Stage primaryStage = (Stage) anchorInicio.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }

  @FXML
  private void loadVideo() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/video/sample.fxml"));
    Stage primaryStage = (Stage) anchorInicio.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 800, 500));
    primaryStage.show();
  }
}
