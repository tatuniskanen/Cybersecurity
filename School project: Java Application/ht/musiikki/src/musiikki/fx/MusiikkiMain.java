package musiikki.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import rekisteri.Lempparit;

/**
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 6.5.2019
 *
 */
public class MusiikkiMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(
                    getClass().getResource("MusiikkiGUIView.fxml"));
            final Pane root = ldr.load();
            final MusiikkiGUIController lempparitCtrl = (MusiikkiGUIController) ldr
                    .getController();
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("musiikki.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Lempparit");

            primaryStage.setOnCloseRequest((event) -> {
                if (!lempparitCtrl.voikoSulkea())
                    event.consume();
            });

            Lempparit lempparit = new Lempparit();
            lempparitCtrl.setLempparit(lempparit);

            primaryStage.show();

            Application.Parameters params = getParameters();
            if (params.getRaw().size() > 0)
                lempparitCtrl.lueTiedosto(params.getRaw().get(0));
            else if (!lempparitCtrl.avaa())
                Platform.exit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Käynnistetään käyttöliittymä
     * @param args komentorivin parametrit
     */
    public static void main(String[] args) {
        launch(args);
    }
}