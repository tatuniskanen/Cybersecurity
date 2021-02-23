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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rekisteri.Artisti;
import rekisteri.Lempparit;

/**
 * @author Taina ja Tatu
 * @version 30.5.2019
 *
 */
public class ArtistiDialogController
        implements ModalControllerInterface<Artisti>, Initializable {
    @FXML
    private GridPane gridArtisti;
    @FXML
    private TextField textVastaus;
    @FXML
    private Label labelVirhe;


    @FXML
    void handleLisaaArtisti() {
        if (artistiKohdalla != null
                && artistiKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        if (lempparit.palautaArtistinId(artistiKohdalla.getNimi()) > 0) {
            naytaVirhe("Artisti on jo olemassa");
            artistiKohdalla = null;
            return;
        }
        ModalController.closeStage(labelVirhe);
    }


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();

    }

    private Artisti artistiKohdalla;
    private static Lempparit lempparit;
    private static Artisti apuartisti = new Artisti(); // Artisti jolta voidaan
                                                       // kysellä tietoja.

    private TextField[] edits;
    private int kentta = 0;


    /**
     * Luodaan GridPaneen jäsenen tiedot
     * @param gridArtisti mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridArtisti) {
        gridArtisti.getChildren().clear();
        TextField[] edits = new TextField[apuartisti.getKenttia()];

        for (int i = 0, k = apuartisti.ekaKentta(); k < apuartisti
                .getKenttia(); k++, i++) {
            Label label = new Label(apuartisti.getKysymys(k));
            gridArtisti.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e" + k);
            gridArtisti.add(edit, 1, i);
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
        edits = luoKentat(gridArtisti);
        for (TextField edit : edits)
            if (edit != null)
                edit.setOnKeyReleased(e -> kasitteleMuutosArtistiin(
                        (TextField) (e.getSource())));
    }


    @Override
    public void setDefault(Artisti oletus) {
        artistiKohdalla = oletus;
        naytaArtisti(edits, artistiKohdalla);
    }


    @Override
    public Artisti getResult() {
        return artistiKohdalla;
    }


    private void setKentta(int kentta) {
        this.kentta = kentta;
    }


    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apuartisti.ekaKentta(),
                Math.min(kentta, apuartisti.getKenttia() - 1));
        edits[kentta].requestFocus();
    }


    /**
     * Näyttää virheilmoituksen
     * @param Virhe virheilmoitus
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }


    /**
     * Käsitellään jäseneen tullut muutos
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosArtistiin(TextField edit) {
        if (artistiKohdalla == null)
            return;
        int k = getFieldId(edit, apuartisti.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = artistiKohdalla.aseta(k, s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }


    /**
     * Näytetään jäsenen tiedot TextField komponentteihin
     * @param edits taulukko TextFieldeistä johon näytetään
     * @param artisti näytettävä artisti
     */
    public static void naytaArtisti(TextField[] edits, Artisti artisti) {
        if (artisti == null)
            return;
        for (int k = artisti.ekaKentta(); k < artisti.getKenttia(); k++) {
            edits[k].setText(artisti.anna(k));
        }
    }


    /**
     * Luodaan jäsenen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @param lmp lempparit
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Artisti kysyArtisti(Stage modalityStage, Artisti oletus,
            int kentta, Lempparit lmp) {
        lempparit = lmp;
        return ModalController.<Artisti, ArtistiDialogController> showModal(
                ArtistiDialogController.class.getResource(
                        "ArtistiDialogView.fxml"),
                "Artistin lisääminen", modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta));
    }

}
