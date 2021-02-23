package musiikki.fx;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;

/**
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 6.5.2019
 * controller tulostusikkunalle
 */
public class TulostusController implements ModalControllerInterface<String> {

    @FXML
    private TextArea tulostusAlue;


    @FXML
    void handleTulosta() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            WebEngine webEngine = new WebEngine();
            webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
            webEngine.print(job);
            job.endJob();
        }
    }


    @Override
    public String getResult() {
        return null;
    }


    @Override
    public void handleShown() {
        //
    }


    @Override
    public void setDefault(String oletus) {
        tulostusAlue.setText(oletus);
    }


    /**
     * @return alue johon tulostetaan
     */
    public TextArea getTextArea() {
        return tulostusAlue;
    }


    /**
    * Näyttää tulostusalueessa tekstin
    * @param tulostus tulostettava teksti
    * @return kontrolleri, jolta voidaan pyytää lisää tietoa
    */
    public static TulostusController tulosta(String tulostus) {
        TulostusController tulostusCtrl = ModalController.showModeless(
                TulostusController.class.getResource("tulostus.fxml"),
                "Tulostus", tulostus);
        return tulostusCtrl;
    }

}
