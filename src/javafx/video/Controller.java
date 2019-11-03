package javafx.video;


import connector.Connector;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Controller implements Initializable {

  @FXML
  AnchorPane anchorVideo;

  @FXML
  private MediaView video;

  private MediaPlayer pVideo;
  private Media ideo;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    String path = new File("src/images/video.mp4").getAbsolutePath();
    ideo = new Media(new File(path).toURI().toString());
    pVideo = new MediaPlayer(ideo);
    video.setMediaPlayer(pVideo);
    pVideo.setAutoPlay(true);
  }

  @FXML
  private void loadInicio() throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/javafx/inicio/sample.fxml"));
    Stage primaryStage = (Stage) anchorVideo.getScene().getWindow();
    primaryStage.setTitle("Vitacura");
    primaryStage.setScene(new Scene(root, 1366, 768));
    primaryStage.show();
  }
}
