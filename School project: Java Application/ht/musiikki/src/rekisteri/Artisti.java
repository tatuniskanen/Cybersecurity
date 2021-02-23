package rekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;

/**
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 6.5.2019
 *
 */
public class Artisti implements Cloneable, Tietue {
    private int arId;
    private String nimi = "";

    private static int seuraavaNro = 1;

    private static int tunnus = 11; // tämä vain apuna testaamiseen,
                                    // "yksilöimässä artistien nimet"


    /**
     * Alustetaan artisti. Toistaiseksi ei tarvitse tehdä mitään
     */
    public Artisti() {
        //
    }


    /**
     * @return artistin kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 2;
    }


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }


    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
        case 0:
            return "id";
        case 1:
            return "nimi";
        default:
            return "???";
        }
    }


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Artisti ar = new Artisti();
     *   ar.parse("   2   |  JVG  ");
     *   ar.anna(0) === "2";   
     *   ar.anna(1) === "JVG";        
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
        case 0:
            return "" + arId;
        case 1:
            return "" + nimi;
        default:
            return "???";
        }
    }


    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Artisti ar = new Artisti();
     *   ar.aseta(1,"kissa") === null;
     *   ar.aseta(0,"25")  === null;
     * </pre>
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
        case 0:
            setArId(Mjonot.erota(sb, '$', getArId()));
            return null;
        case 1:
            nimi = st;
            return null;
        default:
            return "Väärä kentän indeksi";
        }
    }


    /**
     * Tehdään identtinen klooni artistista
     * @return Object kloonattu artisti
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Artisti ar = new Artisti();
     *   ar.parse("   2   |  JVG ");
     *   Artisti kopio = ar.clone();
     *   kopio.toString() === ar.toString();
     *   ar.parse("   1   |  Sanni ");
     *   kopio.toString().equals(ar.toString()) === false;
     * </pre>
     */
    @Override
    public Artisti clone() throws CloneNotSupportedException {
        return (Artisti) super.clone();
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Harrastukselle.
     */
    public void vastaaJvg() {
        nimi = "JVG";
        arId = 1;
    }


    /**
     * Palauttaa artistin nimen.
     * @return artistin nimi
     * @example
     * <pre name="test">
     * Artisti a5 = new Artisti();
     * a5.getNimi() === "";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }


    /**
     * Palauttaa artistin tunnusnumeron.
     * @return artistin tunnusnumero
     * @example
     * <pre name="test">
     * Artisti a0 = new Artisti();
     * a0.getArId() === 0;
     * </pre>
     */
    public int getArId() {
        return arId;
    }


    /**
     * Asettaa tunnusnumeron ja varmistaa, että
     * seuraava numero on aina suurempi kuin tähän mennessä.
     * @param nro seuraava tunnusnumero
     */
    private void setArId(int nro) {
        arId = nro;
        if (arId >= seuraavaNro)
            seuraavaNro = arId + 1;

    }


    /**
     * Apu-metodi, jolla saadaan artistin tiedot täytettyä testaamista varten.
     */
    public void taytaArtisti() {
        nimi = "artisti" + String.format("%03d", tunnus);
        tunnus += 12;
    }


    /**
     * Annetaan jäsenelle seuraava tunnusnumero.
     * @return seuraava tunnusnumero artistille
     * @example
     * <pre name="test">
     * Artisti a2 = new Artisti();
     * a2.rekisteroi();
     * a2.getArId() === 26;
     * Artisti a3 = new Artisti();
     * a3.rekisteroi();
     * a3.getArId() === 27;
     * </pre>
     */
    public int rekisteroi() {
        arId = seuraavaNro;
        seuraavaNro++;

        return seuraavaNro;
    }


    /**
     * Tulostetaan artistin tiedot.
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
        out.println("Artistin nimi on: " + nimi);
        out.println("Artistin id on: " + arId);
    }


    /**
     * Tulostetaan artistin tiedot.
     * @param os tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Palauttaa artistin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return harrastus tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Artisti artisti = new Artisti();
     *   artisti.parse("   2   |  JVG ");
     *   artisti.toString()    === "2|JVG";
     * </pre>
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }


    /**
     * Selvittää artistin tiedot riviltä, jossa tiedot eroteltuna | -merkillä.
     * @param rivi , jolta artistin tiedot luetaan.
     * @example
     * <pre name="test">
     * Artisti artisti = new Artisti();
     * artisti.parse(" 6   | Elastinen");
     * artisti.getArId() === 6;
     * artisti.toString() === "6|Elastinen";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return this.toString().equals(obj.toString());
    }


    @Override
    public int hashCode() {
        return arId;
    }


    /**
     * "Testipääohjelma"
     * @param args ei käytössä
     */
    public static void main(String[] args) {

        Artisti ar = new Artisti();
        ar.vastaaJvg();
        ar.tulosta(System.out);
    }

}