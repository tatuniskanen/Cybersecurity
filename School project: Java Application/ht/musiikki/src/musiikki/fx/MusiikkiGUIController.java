package musiikki.fx;

import static musiikki.fx.KappaleDialogController.getFieldId;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import rekisteri.Albumi;
import rekisteri.Artisti;
import rekisteri.Kappale;
import rekisteri.Lempparit;

/**
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 10.6.2019
 * Luokka käyttöliittymän tapahtumien hoitamiselle
 */
public class MusiikkiGUIController implements Initializable {

    @FXML
    private TextField textHakuehto;

    @FXML
    private ToggleGroup groupHakuehto;
    @FXML
    private RadioButton rb1;
    @FXML
    private RadioButton rb2;
    @FXML
    private RadioButton rb3;

    @FXML
    private ScrollPane panelKappale;
    @FXML
    private GridPane gridKappale;
    @FXML
    private StringGrid<Kappale> gridKappaleet;


    @FXML
    void handleApua() {
        avustus();
    }


    @FXML
    void handleAvaa() {
        avaa();
    }


    @FXML
    void handleHakuehto() {
        hae(0);
    }


    @FXML
    void handleNaytaKaikki() {
        naytaKaikkiKappaleet();
    }


    @FXML
    void handleLisaaKappale() throws IOException {
        lisaaKappale();
    }


    @FXML
    void handleLisaaArtisti() {
        lisaaArtisti();
    }


    @FXML
    void handleLopeta() {
        tallenna();
        Platform.exit();
    }


    @FXML
    void handleNaytaKappale() {
        naytaKappale();
    }


    @FXML
    void handleMuokkaa() {
        muokkaa(kentta);
    }


    @FXML
    void handlePoistaKappale() {
        poistaKappale();
    }


    @FXML
    void handlePoistaArtisti() {
        poistaArtisti();
    }


    @FXML
    void handlePoistaAlbumi() {
        poistaAlbumi();
    }


    @FXML
    void handleTallenna() {
        tallenna();
    }


    @FXML
    void handleTietoja() {
        ModalController.showModal(
                MusiikkiGUIController.class.getResource("Tietoja.fxml"),
                "musiikki", null, "");
    }


    @FXML
    void handleTulosta() {
        TulostusController tulostusCtrl = TulostusController.tulosta(null);
        tulostaValitut(tulostusCtrl.getTextArea());
    }


    @FXML
    void handleSoittolista() {
        soittolista();
    }


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }

    // =========tästä alaspäin "oma" koodi===================================

    private String kayttajanNimi = "tatu";
    private Lempparit lempparit;
    private Kappale kappaleKohdalla;
    private int kentta = 0;
    private static Kappale apuKappale = new Kappale();
    private TextField edits[];


    /**
     * Tekee tarvittavat alustukset. Samalla vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa jäsenten tiedot.
     * Alustetaan myös jäsenlistan kuuntelija
     */
    protected void alusta() {

        gridKappaleet.clear();
        gridKappaleet.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1)
                naytaKappale();
            if (e.getClickCount() > 1)
                muokkaa(0);
        });

        edits = KappaleDialogController.luoKentat(gridKappale, apuKappale);
        for (TextField edit : edits)
            if (edit != null) {
                edit.setEditable(false);
                edit.focusedProperty().addListener(
                        (a, o, n) -> kentta = getFieldId(edit, kentta));
                edit.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.F2)
                        muokkaa(kentta);
                });
            }
    }


    /**
     * Asetetaan otsikko
     * @param title otsikko
     */
    private void setTitle(String title) {
        ModalController.getStage(textHakuehto).setTitle(title);
    }


    /**
     * Alustaa lempparit rekisterin lukemalla sen valitun nimisestä tiedostosta.
     * @param nimi tiedosto, josta lempparien tiedot luetaan
     * @return null, jos onnistuu, muuten virheen tiedot
     */
    protected String lueTiedosto(String nimi) {
        kayttajanNimi = nimi;
        setTitle("Lempparit - " + kayttajanNimi);
        try {
            lempparit.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (IOException e) {
            hae(0);
            String virhe = e.getMessage();
            if (virhe != null)
                Dialogs.showMessageDialog(virhe);
            return virhe;
        }

    }


    /**
     * Asetetaan lempparit
     * @param lempparit Lempparit, jota käytetään tässä käyttöliittymässä
     */
    public void setLempparit(Lempparit lempparit) {
        this.lempparit = lempparit;
        naytaKappale();
    }


    /**
     * Näyttää listasta valitun kappaleen tiedot.
     */
    protected void naytaKappale() {
        kappaleKohdalla = gridKappaleet.getObject();
        if (kappaleKohdalla == null)
            return;

        KappaleDialogController.naytaKappale(edits, kappaleKohdalla,
                lempparit.muokkaaKappaleRiveiksi(kappaleKohdalla));
    }


    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }


    /**
     * Avataan uusi tiedosto
     * @return true jos onnistuu, false jos ei
     */
    public boolean avaa() {
        String uusinimi = LempparitNimiController.kysyNimi(null, kayttajanNimi);
        if (uusinimi == null)
            return false;
        lueTiedosto(uusinimi);
        return true;
    }


    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten antaa virhen tekstinä.
     */
    private String tallenna() {
        try {
            lempparit.tallenna();
            return null;
        } catch (IOException e) {
            Dialogs.showMessageDialog(
                    "Tallentamisessa ongelmia. " + e.getMessage());
            return e.getMessage();
        }
    }


    /**
     * Avaa ohjelman apua ikkunan.
     */
    private void avustus() {
        ModalController.showModal(
                MusiikkiGUIController.class.getResource("Apua.fxml"),
                "musiikki", null, "");
    }


    /**
     * Tulostaa listassa olevat kappaleet tekstialueeseen
     * @param text alue, johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println(
                    "-----Valitut kappaleet-------------------------------------------");

            Kappale kappale;
            int index = 0;
            while (index < gridKappaleet.getItems().size()) {
                kappale = gridKappaleet.getObject(index);
                tulosta(os, kappale);
                os.println("\n");
                index++;
            }
        }
    }


    /**
     * Tulostaa kappaleen tiedot
     * @param os tietovirta johon tulostetaan
     * @param kappale tulostettava kappale
     */
    public void tulosta(PrintStream os, final Kappale kappale) {
        os.println(
                "-------------------------------------------------------------------");
        lempparit.tulosta(os, kappale);
        os.println(
                "-------------------------------------------------------------------");
    }


    /**
     * Hakee kappaleiden tiedot listaan 
     * @param knr kappaleen tunnusnumero, joka aktivoidaan haun jälkeen
     */
    protected void hae(int knr) {
        int knro = knr; // knro jäsenen numero, joka aktivoidaan haun jälkeen
        if (knro <= 0) {
            Kappale kohdalla = kappaleKohdalla;
            if (kohdalla != null)
                knro = kohdalla.getTunnusNro();
        }

        String ehto = textHakuehto.getText();
        if (ehto.indexOf('*') < 0)
            ehto = "*" + ehto + "*";

        RadioButton rb = (RadioButton) groupHakuehto.getSelectedToggle();
        if (rb == null)
            return;
        String id = rb.getId();
        int k = Integer.parseInt(id.substring(2));

        gridKappaleet.clear();
        Collection<Kappale> kappaleet;

        switch (k) {
        case 0:
            return;
        case 1:
            try { // kappaleen mukaan
                kappaleet = lempparit.etsi(ehto, k);
                for (Kappale kappale : kappaleet) {
                    gridKappaleet.add(kappale,
                            lempparit.muokkaaKappaleRiveiksi(kappale));
                }

            } catch (IOException e) {
                Dialogs.showMessageDialog(
                        "Kappaleen hakemisessa ongelmia! " + e.getMessage());
            }
            break;
        case 2:
            try { // artistin mukaan
                k = 1;
                kappaleet = lempparit.etsiArtistinPerusteella(ehto, k);
                for (Kappale kappale : kappaleet) {
                    gridKappaleet.add(kappale,
                            lempparit.muokkaaKappaleRiveiksi(kappale));
                }

            } catch (IOException e) {
                Dialogs.showMessageDialog(
                        "Kappaleen hakemisessa ongelmia! " + e.getMessage());
            }
            break;
        case 3:
            try { // albumin mukaan
                k = 1;
                kappaleet = lempparit.etsiAlbuminPerusteella(ehto, k);
                for (Kappale kappale : kappaleet) {
                    gridKappaleet.add(kappale,
                            lempparit.muokkaaKappaleRiveiksi(kappale));
                }

            } catch (IOException e) {
                Dialogs.showMessageDialog(
                        "Kappaleen hakemisessa ongelmia! " + e.getMessage());
            }
            break;
        default:
            return;
        }

    }


    /**
     * Hakee käyttäjälle näkyvään "listaan" kaikki kappaleet
     */
    protected void naytaKaikkiKappaleet() {
        hae(0);
    }


    /**
     * Muokkaa kappaleen tietoja
     * Luo uuden kappaleen jota aletaan editoimaan
     * @throws IOException virheilmoitus
     */
    protected void lisaaKappale() throws IOException {
        Kappale kappale = new Kappale();
        String[] kentat = new String[kappale.getKenttia() - 1];
        kentat = KappaleDialogController.kysyTiedot(null, kappale, 0, kentat);
        if (kentat == null) // kentat[0] == null || kentat[0].trim().equals("")
            return;
        kappale.rekisteroi();
        String virhe = lempparit.muokkaaRiveistaKappale(kappale, kentat);
        if (virhe != null) {
            lempparit.lisaa(kappale);
            hae(kappale.getTunnusNro());
        } else
            Dialogs.showMessageDialog(virhe);

    }


    /**
     * Luo uuden artistin artisti tiedostoon.
     */
    protected void lisaaArtisti() {
        Artisti uusi = new Artisti();
        uusi = ArtistiDialogController.kysyArtisti(null, uusi, 0, lempparit);
        if (uusi == null)
            return;
        uusi.rekisteroi();
        lempparit.lisaa(uusi);
    }


    /**
     * Muokataan valitun kentän tietoja
     * @param k muokattavan kentän indeksi
     */
    private void muokkaa(int k) {
        if (kappaleKohdalla == null)
            return;
        try {
            String[] tiedot = new String[apuKappale.getKenttia() - 1];
            tiedot = KappaleDialogController.kysyTiedot(null, kappaleKohdalla,
                    k, lempparit.muokkaaKappaleRiveiksi(kappaleKohdalla));
            if (tiedot == null)
                return;
            lempparit.muokkaaRiveistaKappale(kappaleKohdalla, tiedot);
            lempparit.korvaaTaiLisaa(kappaleKohdalla);
            hae(kappaleKohdalla.getTunnusNro());
        } catch (IOException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }

    }


    /**
    * Poistetaan kohdalla oleva kappale
    */
    private void poistaKappale() {
        Kappale kappale = kappaleKohdalla;
        if (kappale == null)
            return;
        if (!Dialogs.showQuestionDialog("Poisto",
                "Poistetaanko kappale: " + kappale.getNimi(), "Kyllä", "Ei"))
            return;
        lempparit.poistaKappale(kappale);
        int index = gridKappaleet.getRowNr();
        hae(0);
        gridKappaleet.selectRow(index);
    }


    /**
     * Poistetaan valitussa kappaleessa esiintyvä artisti artisti"taulukosta" ja kaikki kappaleet, joihin artisti on merkattu.
     */
    private void poistaArtisti() {
        Kappale kappale = kappaleKohdalla;
        if (kappale == null)
            return;
        try {
            int tunnusNr = Integer.parseInt(kappale.anna(2));
            Artisti kohdalla = lempparit.etsiArtistiIdlla(tunnusNr);
            if (kohdalla == null)
                return;

            if (!Dialogs.showQuestionDialog("Poisto",
                    "Poistetaanko varmasti artisti: '" + kohdalla.getNimi()
                            + "'? HUOM! Artisti poistuu kaikista kappaleista.",
                    "Kyllä", "Ei"))
                return;
            if (Dialogs.showQuestionDialog(
                    "Poisto", "Poistetaanko myös artistin: '"
                            + kohdalla.getNimi() + "' kaikki kappaleet?",
                    "Kyllä", "Ei"))
                lempparit.poistaArtistinKappaleet(tunnusNr);
            lempparit.poistaArtisti(tunnusNr);
        } catch (IOException e) {
            Dialogs.showMessageDialog(
                    "Artistin poistamisessa ongelmia! " + e.getMessage());
        }
        hae(0);
    }


    /**
     * Poistetaan valitussa kappaleessa esiintyvä albumi albumi"taulukosta".
     */
    private void poistaAlbumi() {
        Kappale kappale = kappaleKohdalla;
        if (kappale == null)
            return;
        try {
            int tunnusNr = Integer.parseInt(kappale.anna(3));
            Albumi kohdalla = lempparit.etsiAlbumiIdlla(tunnusNr);
            if (kohdalla == null)
                return;

            if (!Dialogs.showQuestionDialog("Poisto",
                    "Poistetaanko varmasti albumi: '" + kohdalla.getNimi()
                            + "'? HUOM! Albumi poistuu kaikista kappaleista.",
                    "Kyllä", "Ei"))
                return;
            lempparit.poistaAlbumi(tunnusNr);
        } catch (IOException e) {
            Dialogs.showMessageDialog(
                    "Albumin poistamisessa ongelmia! " + e.getMessage());
        }
        hae(0);
    }


    /**
     * Tehdään kappalelista, käyttäjän antaman ajan perusteella.
     * @return true jos onnistuu, false jos ei
     */
    public boolean soittolista() {
        String aikaMerkkijonona = SoittoaikaController.kysyAika(null, "10");
        if (aikaMerkkijonona.trim().isEmpty())
            return false;
        int aikaMin = Integer.parseInt(aikaMerkkijonona);
        if (aikaMin > 0 && aikaMin <= 600) {
            haeAjanPerusteella(aikaMin);
            return true;
        }
        Dialogs.showMessageDialog("Keston täytyy olla välillä 0-600 minuuttia");
        return false;
    }


    /**
     * Hakee kappaleiden tiedot listaan, kunnes annettu aika "täysi"
     * @param aika aikaraja sekunteina
     */
    protected void haeAjanPerusteella(int aika) {
        gridKappaleet.clear();
        Collection<Kappale> kappaleet;
        try {
            kappaleet = lempparit.haeAjanPerusteella(aika);
            if (kappaleet.isEmpty() || kappaleet.size() == 0) {
                Dialogs.showMessageDialog(
                        "Kappaleiden kestoja ei ole merkitty!");
                return;
            }

            for (Kappale kappale : kappaleet) {
                gridKappaleet.add(kappale,
                        lempparit.muokkaaKappaleRiveiksi(kappale));
            }

        } catch (IOException e) {
            Dialogs.showMessageDialog(
                    "Kappaleen hakemisessa ongelmia! " + e.getMessage());
        }
    }
}