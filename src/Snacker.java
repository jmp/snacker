import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Snacker extends Application {
    private static final String TITLE = "Snacker";
    private static final String MAIN_FXML = "main.fxml";
    private static final String STYLESHEET = "style.css";

    @Override
    public void start(Stage stage) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource(MAIN_FXML));
        final Scene scene = new Scene(root);
        scene.getStylesheets().add(Snacker.class.getResource(STYLESHEET).toExternalForm());
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
