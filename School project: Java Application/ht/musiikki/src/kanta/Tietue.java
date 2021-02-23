package kanta;

/**
 * Raja-pinta tietueelle
 * @author Tatu Niskanen ja Taina Patrikainen
 * @version 2.5.2019
 *
 */
public interface Tietue {

    /**
     * @return tietueen kenttien lukumäärä
     * @example
     * <pre name="test">
     *   #import rekisteri.Kappale;
     *   #import rekisteri.Kappaleet;
     *   Kappale ka = new Kappale();
     *   ka.getKenttia() === 7;
     * </pre>
     */
    public abstract int getKenttia();


    /**
     * @return ensimmäinen kentän indeksi, jonka käyttäjä syöttää
     * @example
     * <pre name="test">
     *   Kappale kap = new Kappale();
     *   kap.ekaKentta() === 1;
     * </pre>
     */
    public abstract int ekaKentta();


    /**
     * @param k ,minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     * @example
     * <pre name="test">
     *   Kappale ka1 = new Kappale();
     *   ka1.getKysymys(1) === "Kappale";
     * </pre>
     */
    public abstract String getKysymys(int k);


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Kappale ka2 = new Kappale();
     *   ka2.parse("   3  |  Punainen bändi   | 12 | 3 | 120 | 2015 | 3 |");
     *   ka2.anna(0) === "3";   
     *   ka2.anna(1) === "Punainen bändi";   
     *   ka2.anna(2) === "12";   
     *   ka2.anna(3) === "3";   
     *   ka2.anna(4) === "120";
     *   ka2.anna(5) === "2015";
     *   ka2.anna(6) === "3";   
     * </pre>
     */
    public abstract String anna(int k);


    /**
     * Asetetaan valitun kentän sisältö.  Palauttaa null, jos asettaminen onnistuu, jos ei niin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Kappale kapp = new Kappale();
     *   kapp.aseta(1,"JVG") === null;
     *   kapp.aseta(4,"210") === null;
     *   kapp.aseta(5,"2000")  === null;
     *   kapp.aseta(5,"kissa") === "julkaisuvuosi: Ei kokonaisluku (kissa)";
     *   kapp.aseta(6,"kissa") === "rank: Ei kokonaisluku (kissa)";
     * </pre>
     */
    public abstract String aseta(int k, String s);


    /**
     * Tehdään identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Kappale k1 = new Kappale();
     *   k1.parse("   2   |  Joulupuu  |  15  |  5  |  153  | 2015 | 2 ");
     *   Object kopio = k1.clone();
     *   kopio.toString() === k1.toString();
     *   k1.parse("   1   |  Joulupukki  |  16  |  6  |  153  | 2015 | 2 ");
     *   kopio.toString().equals(k1.toString()) === false;
     *   kopio instanceof Kappale === true;
     * </pre>
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Kappale kappale = new Kappale();
     *   kappale.parse("   2   |  Joutsenet  |  4  | 5 | 315 | 1949 | 1 ");
     *   kappale.toString()    =R= "2\\|Joutsenet\\|4\\|5\\|315\\|1949\\|1";
     * </pre>
     */
    @Override
    public abstract String toString();

}
