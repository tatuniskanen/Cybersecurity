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
public class SoittoaikaController implements ModalControllerInterface<String> {

    @FXML
    private TextField textAikaMin;
    private String vastaus = null;


    @FXML
    void handleOK() {
        vastaus = textAikaMin.getText();
        ModalController.closeStage(textAikaMin);
    }


    @Override
    public String getResult() {
        return vastaus;
    }


    @Override
    public void handleShown() {
        textAikaMin.requestFocus();
    }


    @Override
    public void setDefault(String oletus) {
        textAikaMin.setText(oletus);
    }


    // ======================= tän alle muu osa koodista ======================

    /**
     * Luodaan ajankysymisdialogi ja palautetaan siihen kirjoitettu aika tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä aikaa näytetään oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
    public static String kysyAika(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                SoittoaikaController.class.getResource("soittoaika.fxml"),
                "Lempparit", modalityStage, oletus);
    }

}
