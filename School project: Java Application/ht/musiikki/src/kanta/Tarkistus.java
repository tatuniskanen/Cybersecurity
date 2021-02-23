package kanta;

/**
 * Luokka tekstikenttien sisällön "oikeellisuuden" tarkistamiselle
 * @author Tatu Niskanen ja Taina Patrikainen
 * @version 10.6.2019
 *
 */
public class Tarkistus {
    /** Numeroita vastaavat kirjaimet */
    public static final String NUMEROT = "0123456789";
    private String sallitut = "";


    /**
     * Onko jonossa vain sallittuja merkkejä
     * @param jono      tutkittava jono
     * @param sallitut  merkit, joita sallitaan
     * @return true jos vain sallittuja, false muuten
     * @example
     * <pre name="test">
     *   onkoVain("123","12")   === false;
     *   onkoVain("123","1234") === true;
     *   onkoVain("","1234") === true;
     * </pre>
     */
    public static boolean onkoVain(String jono, String sallitut) {
        for (int i = 0; i < jono.length(); i++)
            if (sallitut.indexOf(jono.charAt(i)) < 0)
                return false;
        return true;
    }


    /**
     * @param jono tutkittava jono
     * @return palauttaa virhetekstin
     * @example
     * <pre name="test">
     * Tarkistus tar = new Tarkistus();
     * String testi = "15k";
     * tar.tarkistaVuosi(testi) === "Saa olla vain merkkejä: 0123456789";
     * testi = "15";
     * tar.tarkistaVuosi(testi) === "Julkaisuvuosi täytyy olla välillä 1500-2019";
     * testi = "2020";
     * tar.tarkistaVuosi(testi) === "Julkaisuvuosi täytyy olla välillä 1500-2019";
     * </pre>
     */
    public String tarkistaVuosi(String jono) {
        sallitut = NUMEROT;
        if (!onkoVain(jono, sallitut))
            return "Saa olla vain merkkejä: " + sallitut;
        if (Integer.parseInt(jono) < 1500 || Integer.parseInt(jono) > 2019)
            return "Julkaisuvuosi täytyy olla välillä 1500-2019";
        return "";
    }


    /**
     * @param jono tutkittava jono
     * @return palauttaa virhetekstin
     * @example
     * <pre name="test">
     * Tarkistus tar = new Tarkistus();
     * String testi = "15k";
     * tar.tarkistaRank(testi) === "Saa olla vain merkkejä: 0123456789";
     * testi = "15";
     * tar.tarkistaRank(testi) === "Rank täytyy olla välillä 1-5";
     * testi = "5";
     * tar.tarkistaRank(testi) === "";
     * </pre>
     */
    public String tarkistaRank(String jono) {
        sallitut = NUMEROT;
        if (!onkoVain(jono, sallitut))
            return "Saa olla vain merkkejä: " + sallitut;
        if (Integer.parseInt(jono) < 1 || Integer.parseInt(jono) > 5)
            return "Rank täytyy olla välillä 1-5";
        return "";
    }


    /**
     * @param jono tutkittava jono
     * @return palauttaa virhetekstin
     * @example
     * <pre name="test">
     * Tarkistus tar = new Tarkistus();
     * String testi = "15k";
     * tar.tarkistaKesto(testi) === "Saa olla vain merkkejä: 0123456789";
     * testi = "15";
     * tar.tarkistaKesto(testi) === "";
     * testi = "1200";
     * tar.tarkistaKesto(testi) === "Keston täytyy olla välillä 1-999 sekuntia";
     * testi = "0";
     * tar.tarkistaKesto(testi) === "Keston täytyy olla välillä 1-999 sekuntia";
     * </pre>
     */
    public String tarkistaKesto(String jono) {
        sallitut = NUMEROT;
        if (!onkoVain(jono, sallitut))
            return "Saa olla vain merkkejä: " + sallitut;
        if (Integer.parseInt(jono) < 1 || Integer.parseInt(jono) > 999)
            return "Keston täytyy olla välillä 1-999 sekuntia";
        return "";
    }


    /**
     * Tarkistaa että jono sisältää vain valittuja numeroita
     * @param jono tutkittava jono
     * @param k kentan indeksi
     * @return null jos vain valittujan merkkejä, muuten virheilmoitus
     * @example
     * <pre name="test">
     * tarkista("", 1) === "";
     * tarkista("", 5) === "";
     * tarkista("23", 5) === "Julkaisuvuosi täytyy olla välillä 1500-2019";
     * tarkista("2018", 5) === "";
     * tarkista("120", 4) === "";
     * tarkista("120,0", 4) === "Saa olla vain merkkejä: 0123456789";
     * tarkista("1000", 4) === "Keston täytyy olla välillä 1-999 sekuntia";
     * tarkista("1", 6) === "";
     * tarkista("1k", 6) === "Saa olla vain merkkejä: 0123456789";
     * tarkista("0", 6) === "Rank täytyy olla välillä 1-5";
     * </pre>
     */
    public static String tarkista(String jono, int k) {
        Tarkistus tar = new Tarkistus();
        String palaute = "";
        if (jono.trim().equals(""))
            return "";
        switch (k) {
        case 0:
            break;
        case 1:
            break;
        case 2:
            break;
        case 3:
            break;
        case 4:
            palaute = tar.tarkistaKesto(jono);
            break;
        case 5:
            palaute = tar.tarkistaVuosi(jono);
            break;
        case 6:
            palaute = tar.tarkistaRank(jono);
            break;
        default:
            return "Tarkastajassa ongelma";
        }
        return palaute;
    }
}
