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
public class Albumi implements Cloneable, Tietue {
    private int alId;
    private String nimi = "";

    private static int seuraavaNro = 1;

    private static int tunnus = 11; // tämä vain apuna testaamiseen,
                                    // "yksilöimässä albumien nimet"


    /**
     * Alustetaan albumi. Toistaiseksi ei tarvitse tehdä mitään
     */
    public Albumi() {
        //
    }


    /**
     * @return albumin kenttien lukumäärä
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
     *   Albumi al = new Albumi();
     *   al.parse("   2   |  Tämä päivä  ");
     *   al.anna(0) === "2";   
     *   al.anna(1) === "Tämä päivä";      
     *   
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
        case 0:
            return "" + alId;
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
     *   Albumi al = new Albumi();
     *   al.aseta(1,"kissa") === null;
     *   al.aseta(0,"19")  === null;
     * </pre>
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
        case 0:
            setAlId(Mjonot.erota(sb, '$', getAlId()));
            return null;
        case 1:
            nimi = st;
            return null;
        default:
            return "Väärä kentän indeksi";
        }
    }


    /**
     * Tehdään identtinen klooni albumista
     * @return Object kloonattu albumi
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Albumi al = new Albumi();
     *   al.parse("   2   |  Tämä päivä ");
     *   Albumi kopio = al.clone();
     *   kopio.toString() === al.toString();
     *   al.parse("   1   |  Hiiohoi ");
     *   kopio.toString().equals(al.toString()) === false;
     * </pre>
     */
    @Override
    public Albumi clone() throws CloneNotSupportedException {
        return (Albumi) super.clone();
    }


    /**
     * Palauttaa albumin nimen.
     * @return albumin nimi
     * @example
     * <pre name="test">
     * Albumi a5 = new Albumi();
     * a5.getNimi() === "";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }


    /**
     * Palauttaa albumin tunnusnumeron.
     * @return albumin tunnusnumero
     * @example
     * <pre name="test">
     * Albumi a0 = new Albumi();
     * a0.getAlId() === 0;
     * </pre>
     */
    public int getAlId() {
        return alId;
    }


    /**
     * Asettaa tunnusnumeron ja varmistaa, että
     * seuraava numero on aina yhden isompi kuin tähän mennessä suurin.
     * @param nro asetettava tunnusnumero
     */
    private void setAlId(int nro) {
        alId = nro;
        if (alId >= seuraavaNro)
            seuraavaNro = alId + 1;
    }


    /**
     * Apu-metodi, jolla saadaan albumin tiedot täytettyä testaamista varten.
     */
    public void taytaAlbumi() {
        nimi = "albumi" + String.format("%03d", tunnus);
        tunnus += 12;
    }


    /**
     * Annetaan albumille seuraava tunnusnumero.
     * @return seuraava tunnusnumero albumille
     * @example
     * <pre name="test">
     * Albumi a2 = new Albumi();
     * a2.rekisteroi();
     * a2.getAlId() === 20;
     * Albumi a3 = new Albumi();
     * a3.rekisteroi();
     * a3.getAlId() === 21;
     * </pre>
     */
    public int rekisteroi() {
        alId = seuraavaNro;
        seuraavaNro++;

        return seuraavaNro;
    }


    /**
     * Tulostetaan albumin tiedot.
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) {
        out.println("Albumin nimi on: " + nimi);
        out.println("Albumin id on: " + alId);
    }


    /**
     * Tulostetaan albumin tiedot.
     * @param os tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


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
     * Selvittää albumin tiedot riviltä, jossa tiedot eroteltuna | -merkillä.
     * @param rivi rivi, jolta albumin tiedot luetaan.
     * @example
     * <pre name="test">
     * Albumi albumi = new Albumi();
     * albumi.parse(" 12   | Kauneimmat Joululaulut");
     * albumi.getAlId() === 12;
     * albumi.toString() === "12|Kauneimmat Joululaulut";
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
        return alId;
    }


    /**
     * "Testipääohjelma"
     * @param args ei käytössä
     */
    public static void main(String[] args) {

        Albumi al = new Albumi();
        al.taytaAlbumi();
        al.tulosta(System.out);
    }
}
