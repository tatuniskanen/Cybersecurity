package musiikki.fx;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Tatu Niskanen ja Taina Patrikainen
 * @version 6.5.2019
 *
 */
public class LempparitNimiController
        implements ModalControllerInterface<String> {

    @FXML
    private TextField textVastaus;
    private String vastaus = null;


    @FXML
    private void handleOk() {
        vastaus = textVastaus.getText();
        ModalController.closeStage(textVastaus);
    }


    @Override
    public String getResult() {
        return vastaus;
    }


    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
    }


    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        textVastaus.requestFocus();
    }


    /**
     * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä nimeä näytetään oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                LempparitNimiController.class.getResource("aloitusikkuna.fxml"),
                "Lempparit", modalityStage, oletus);
    }

}
