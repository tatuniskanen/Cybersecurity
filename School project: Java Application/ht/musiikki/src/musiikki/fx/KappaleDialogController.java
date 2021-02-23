package musiikki.fx;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kanta.Tarkistus;
import rekisteri.Kappale;

/**m
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 6.5.2019
 *
 */
public class KappaleDialogController
        implements ModalControllerInterface<String[]>, Initializable {
    @FXML
    private ScrollPane panelKappale;
    @FXML
    private GridPane gridKappale;
    @FXML
    private Label labelVirhe;


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }


    @FXML
    private void handleOK() {
        if (kentat[0] == null || kentat[0].trim().equals("")) {
            String virhe = "Kappaleen nimi ei saa olla tyhjä";
            Dialogs.setToolTipText(edits[1], virhe);
            edits[1].getStyleClass().add("virhe");
            naytaVirhe(virhe);
            return;
        }
        ModalController.closeStage(labelVirhe);
    }


    @FXML
    private void handlePoistu() {
        kappaleKohdalla = null;
        kentat = null;
        ModalController.closeStage(labelVirhe);
    }

    // ===========tästä alkaa "oma" koodi==========================
    private static Kappale kappaleKohdalla;
    private TextField[] edits;
    private String[] kentat;
    private int kentta = 0;


    /**
     * Luodaan GridPaneen kappaleen tiedot
     * @param gridKappale mihin tiedot luodaan
     * @param apuKappale malli, josta tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridKappale,
            Kappale apuKappale) {
        gridKappale.getChildren().clear();
        TextField[] edits = new TextField[apuKappale.getKenttia()];

        for (int i = 0, k = apuKappale.ekaKentta(); k < apuKappale
                .getKenttia(); k++, i++) {
            Label label = new Label(apuKappale.getKysymys(k));
            gridKappale.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e" + k);
            gridKappale.add(edit, 1, i);
        }
        return edits;
    }


    /**
     * Tyhjentään tekstikentät
     * @param edits tyhjennettävät kentät
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
            if (edit != null)
                edit.setText("");
    }


    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna
     */
    public static int getFieldId(Object obj, int oletus) {
        if (!(obj instanceof Node))
            return oletus;
        Node node = (Node) obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }


    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa jäsenten tiedot.
     */
    protected void alusta() {
        kentat = new String[kappaleKohdalla.getKenttia()];
        edits = luoKentat(gridKappale, kappaleKohdalla);
        for (TextField edit : edits)
            if (edit != null)
                edit.setOnKeyReleased(e -> kasitteleKenttienMuutos(
                        (TextField) (e.getSource())));
        panelKappale.setFitToHeight(true);
    }


    @Override
    public String[] getResult() {
        return kentat;
    }


    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(kappaleKohdalla.ekaKentta(),
                Math.min(kentta, kappaleKohdalla.getKenttia() - 1));
        edits[kentta].requestFocus();
    }


    @Override
    public void setDefault(String[] oletus) {
        kentat = oletus;
        ModalController.getStage(labelVirhe)
                .setOnCloseRequest(event -> handlePoistu());
        naytaKappale(edits, kappaleKohdalla, kentat);
    }


    /**
     * Näyttää virheilmoituksen
     * @param virhe virheilmoitus
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.trim().equals("")) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }


    private void setKentta(int kentta) {
        this.kentta = kentta;
    }


    /**
     * Käsitellään kenttiin tullut muutos
     * @param edit muuttunut kenttä
     */
    protected void kasitteleKenttienMuutos(TextField edit) {
        if (kappaleKohdalla == null)
            return;
        int k = getFieldId(edit, kappaleKohdalla.ekaKentta());
        String s = edit.getText();
        if (s != null) {
            String palaute = Tarkistus.tarkista(s, k);
            if (palaute.trim().equals("")) {
                kentat[k - 1] = s;
                Dialogs.setToolTipText(edit, "");
                edit.getStyleClass().removeAll("virhe");
                naytaVirhe(palaute);
            } else {
                Dialogs.setToolTipText(edit, palaute);
                edit.getStyleClass().add("virhe");
                naytaVirhe(palaute);
            }
        }
    }


    /**
     * Näytetään kappaleen tiedot TextField komponentteihin
     * @param edits taulukko TextFieldeistä, johon näytetään
     * @param kappale näytettävä kappale
     * @param tiedot kappaleen tiedot merkkijono-taulukossa
     */
    public static void naytaKappale(TextField[] edits, Kappale kappale,
            String[] tiedot) {
        if (kappale == null)
            return;
        int i = 0;
        for (int k = kappale.ekaKentta(); k < kappale.getKenttia(); k++) {
            edits[k].setText(tiedot[i]);
            i++;
        }
    }


    /**
     * Luodaan kappaleen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @param tiedot kappaleen tiedot merkkijonotalukossa
     * @return null jos painetaan Poistu, muuten täytetty tietue
     */
    public static String[] kysyTiedot(Stage modalityStage, Kappale oletus,
            int kentta, String[] tiedot) {
        kappaleKohdalla = oletus;
        return ModalController.<String[], KappaleDialogController> showModal(
                KappaleDialogController.class
                        .getResource("KappaleDialogView.fxml"),
                "Lempparit", modalityStage, tiedot,
                ctrl -> ctrl.setKentta(kentta));
    }
}
