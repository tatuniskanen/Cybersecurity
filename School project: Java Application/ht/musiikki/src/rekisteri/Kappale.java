package rekisteri;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;

/**
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 20.5.2019
 * Luokka kappaleille
 */
public class Kappale implements Cloneable, Tietue {

    private int tunnusNro;
    private String nimi;
    private int alid;
    private int arid;
    private int kesto; // kesto sekunteina
    private int julkVuosi;
    private int rank;

    private static int seuraavaNro = 1;

    private static int nro = 6; // tämä vain testaamista varten

    /**
     * Kappaleiden vertailija
     */
    public static class Vertailija implements Comparator<Kappale> {
        private int k;


        @SuppressWarnings("javadoc")
        public Vertailija(int k) {
            this.k = k;
        }


        @Override
        public int compare(Kappale kappale1, Kappale kappale2) {
            return kappale1.getAvain(k)
                    .compareToIgnoreCase(kappale2.getAvain(k));
        }
    }


    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenennko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String getAvain(int k) {
        switch (k) {
        case 0:
            return "" + tunnusNro;
        case 1:
            return "" + nimi;
        case 2:
            return "" + arid;
        case 3:
            return "" + alid;
        case 4:
            return "" + kesto;
        case 5:
            return "" + julkVuosi;
        case 6:
            return "" + rank;
        default:
            return "Virhe";
        }
    }


    /**
     * Palauttaa kappaleen kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 7;
    }


    /**
     * Eka kenttä, jota järkevää "muokata"
     * @return ekan kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }


    /**
     * Alustetaan kappale
     */
    public Kappale() {
        //
    }


    /**
     * Muodostaja
     * @param artisti kappaleen artistin id
     * @param albumi kappaleeen albumin id
     */
    public Kappale(int artisti, int albumi) {
        this.alid = albumi;
        this.arid = artisti;
    }


    /**
     * Apu-metodi, joka täyttää kappaleen tiedot testaamista varten.
     * @param artId artistin nro
     * @param albId albumin nro
     */
    public void taytaKappale(int artId, int albId) {
        arid = artId;
        alid = albId;
        nimi = "Laulu" + String.format("%03d", nro);
        kesto = 120;
        julkVuosi = 2012;
        rank = 3;

        nro += 13;
    }


    /**
     * Annetaan kappaleelle seuraava tunnusnumero.
     * @return kappaleen tunnusnumeron
     * @example
     * <pre name="test">
     * Kappale k1 = new Kappale();
     * k1.rekisteroi();
     * k1.getTunnusNro() === 5;
     * Kappale k2 = new Kappale();
     * k2.rekisteroi();
     * k2.getTunnusNro() === 6;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palauttaa kappaleen tunnusnumeron.
     * @return kappaleen tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Palauttaa kappaleen artistin id:n
     * @return kappaleen artistin id:n
     */
    public int getArId() {
        return arid;
    }


    /**
     * Palauttaa kappaleen albumin id:n.
     * @return kappaleen albumin id:n
     */
    public int getAlId() {
        return alid;
    }


    /**
     * Palauttaa kappaleen nimen.
     * @return kappaleen nimi
     * @example
     * <pre name="test">
     * Kappale k4 = new Kappale();
     * k4.taytaKappale(3, 8);
     * k4.getNimi() === "Laulu006";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }


    /**
     * Palauttaa kappaleen keston.
     * @return kappaleen kesto
     */
    public int getKesto() {
        return kesto;
    }


    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monennenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    @Override
    public String anna(int k) {
        switch (k) {
        case 0:
            return "" + tunnusNro;
        case 1:
            return "" + nimi;
        case 2:
            return "" + arid;
        case 3:
            return "" + alid;
        case 4:
            return "" + kesto;
        case 5:
            return "" + julkVuosi;
        case 6:
            return "" + rank;
        default:
            return "Virhe";
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
     *   Kappale kap = new Kappale();
     *   kap.aseta(1,"kissa") === null;
     *   kap.aseta(3,"19")  === null;
     *   kap.aseta(4,"kissa") === "kesto: Ei kokonaisluku (kissa). Anna kesto sekunteina (esim. 120)";
     *   kap.aseta(5,"2013")    === null;
     * </pre>
     */
    @Override
    public String aseta(int k, String s) {
        String jono = s.trim();
        StringBuffer sb = new StringBuffer(jono);
        switch (k) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
            return null;
        case 1:
            nimi = jono;
            return null;
        case 2:
            arid = Mjonot.erota(sb, '$', arid);
            return null;
        case 3:
            alid = Mjonot.erota(sb, '$', alid);
            return null;
        case 4:
            try {
                kesto = Mjonot.erotaEx(sb, '§', kesto);
            } catch (NumberFormatException ex) {
                return "kesto: Ei kokonaisluku (" + jono
                        + "). Anna kesto sekunteina (esim. 120)";
            }
            return null;
        case 5:
            try {
                julkVuosi = Mjonot.erotaEx(sb, '§', julkVuosi);
            } catch (NumberFormatException ex) {
                return "julkaisuvuosi: Ei kokonaisluku (" + jono + ")";
            }
            return null;
        case 6:
            try {
                rank = Mjonot.erotaEx(sb, '§', rank);
            } catch (NumberFormatException ex) {
                return "rank: Ei kokonaisluku (" + jono + ")";
            }
            return null;
        default:
            return "Väärä kentän indeksi";
        }
    }


    /**
     * Palauttaa k:n kappaleen kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta vastaata kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
        case 0:
            return "tunNro";
        case 1:
            return "Kappale";
        case 2:
            return "Artisti";
        case 3:
            return "Albumi";
        case 4:
            return "Kesto (sekunteina)";
        case 5:
            return "Julkaisuvuosi";
        case 6:
            return "Rank (1-5)";
        default:
            return "???";
        }
    }


    /**
     * Tulostaa kappaleen tiedot.
     * @param out tietovirta, johon tulostetaan.
     */
    public void tulosta(PrintStream out) { // tulosta siirtynyt lemppareihin,
                                           // että
                                           // saadaan albumien ja artistien
                                           // nimet
        out.println("Kappaleen nimi on: " + nimi);
        out.println("Albumin id on: " + alid);
        out.println("Artistin id on: " + arid);
        out.println("Kappaleen kesto on: " + kesto
                + " ja sen julkaisuvuosi on: " + julkVuosi);
        out.println("Olet arvostellut kappaleen luokkaan(1-5): " + rank);
    }


    /**
     * Tulostaa kappaleen tiedot.
     * @param os tietovirta, johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Asettaa tunnusnumeron ja varmistaa, että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nro asetettava tunnusnumero
     */
    private void setTunnusNro(int nro) {
        tunnusNro = nro;
        if (tunnusNro >= seuraavaNro)
            seuraavaNro = tunnusNro + 1;
    }


    /**
     * Palauttaa kappaleen tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return kappale, tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     * Kappale laulu = new Kappale();
     * laulu.parse("   3  |  Hard Rock Halleluja ");
     * laulu.toString().startsWith("3|Hard Rock Halleluja|") === true;
     * </pre>
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }


    /**
     * Selvittää kappaleen tiedot merkkijonosta, jossa tiedot eroteltu | -merkillä.
     * @param rivi , jolta kappaleen tiedot otetaan
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        for (int k = 0; k < getKenttia(); k++) {
            aseta(k, Mjonot.erota(sb, '|'));
        }
    }


    /**
     * Tehdään identtinen klooni kappaleesta
     * @return Object kloonattu kappale
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Kappale kappale = new Kappale();
     *   kappale.parse("   3  |  Punainen bändi   | 12");
     *   Kappale kopio = kappale.clone();
     *   kopio.toString() === kappale.toString();
     *   kappale.parse("   4  |  Keltainen bändi   | 12");
     *   kopio.toString().equals(kappale.toString()) === false;
     * </pre>
     */
    @Override
    public Kappale clone() throws CloneNotSupportedException {
        Kappale uusi;
        uusi = (Kappale) super.clone();
        return uusi;
    }


    /**
     * Tutkii onko kappaleen tiedot samat kuin parametrina tuodun kappaleen tiedot
     * @param kappale kappale, johon verrataan
     * @return true, jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Kappale kappale1 = new Kappale();
     *   kappale1.parse("   3  |  Bändi   | 330");
     *   Kappale kappale2 = new Kappale();
     *   kappale2.parse("   3  |  Bändi   | 330");
     *   Kappale kappale3 = new Kappale();
     *   kappale3.parse("   3  |  Bändi   | 220");
     *   
     *   kappale1.equals(kappale2) === true;
     *   kappale2.equals(kappale1) === true;
     *   kappale1.equals(kappale3) === false;
     *   kappale3.equals(kappale2) === false;
     * </pre>
     */
    public boolean equals(Kappale kappale) {
        if (kappale == null)
            return false;
        for (int k = 0; k < getKenttia(); k++)
            if (!anna(k).equals(kappale.anna(k)))
                return false;
        return true;
    }


    @Override
    public boolean equals(Object kappale) {
        if (kappale instanceof Kappale)
            return equals((Kappale) kappale);
        return false;
    }


    @Override
    public int hashCode() {
        return tunnusNro;
    }


    /**
     * Testiohjelma kappaleelle.
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Kappale k1 = new Kappale();
        k1.taytaKappale(1, 2);
        k1.tulosta(System.out);

        Kappale k2 = new Kappale();
        k2.taytaKappale(1, 3);
        k2.tulosta(System.out);
        System.out.println(k2);
    }
}