package rekisteri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 20.5.2019
 *
 */
public class Albumit implements Iterable<Albumi> {
    private static final int MAX_ALBUMEJA = 7;
    private int lkm = 0;
    private String tiedostonPerusNimi = "";
    private Albumi alkiot[] = new Albumi[MAX_ALBUMEJA];
    private boolean muutettu = false;


    /**
     * Albumien alustaminen
     */
    public Albumit() {
        //
    }


    /**
     * Lisää uuden albumin tietorakenteeseen.
     * @param albumi lisättävän albumin viite
     * @example
     * <pre name="test">
     * Albumit albumit = new Albumit();
     * Albumi alb1 = new Albumi(), alb2 = new Albumi();
     * albumit.getLkm() === 0;
     * albumit.lisaa(alb1); albumit.getLkm() === 1;
     * albumit.lisaa(alb2); albumit.getLkm() === 2;
     * albumit.lisaa(alb1); albumit.getLkm() === 3;
     * albumit.anna(0) === alb1;
     * albumit.anna(1) === alb2;
     * albumit.anna(2) === alb1;
     * albumit.anna(1) == alb1 === false;
     * albumit.anna(1) == alb2 === true;
     * albumit.lisaa(alb1); albumit.getLkm() === 4;
     * albumit.lisaa(alb1); albumit.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Albumi albumi) {
        if (lkm >= alkiot.length) {
            int koko = alkiot.length + 7;
            Albumi kopioalkiot[] = new Albumi[koko];

            int j = 0;

            for (int i = 0; i < alkiot.length; i++) {
                kopioalkiot[j] = alkiot[i];
                j++;
            }

            alkiot = kopioalkiot;
        }

        alkiot[lkm++] = albumi;
        muutettu = true;
    }


    /**
     * Korvaa albumin tietorakenteessa.  Ottaa albumin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva albumi.  Jos ei löydy,
     * niin lisätään uutena albumina.
     * @param albumi lisättävän albumin viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws IOException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS IOException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Albumit albumit = new Albumit(); 
     * Albumi alb1 = new Albumi(), alb2 = new Albumi();  
     * alb1.rekisteroi(); alb2.rekisteroi();
     * albumit.getLkm() === 0;
     * albumit.korvaaTaiLisaa(alb1); albumit.getLkm() === 1;
     * albumit.korvaaTaiLisaa(alb2); albumit.getLkm() === 2;
     * Albumi alb3 = alb1.clone();
     * alb3.aseta(3,"kkk");
     * Iterator<Albumi> it = albumit.iterator();
     * it.next() == alb1 === true;
     * albumit.korvaaTaiLisaa(alb3); albumit.getLkm() === 2;
     * it = albumit.iterator();
     * Albumi alb0 = it.next();
     * alb0 === alb3;
     * alb0 == alb3 === true;
     * alb0 == alb1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Albumi albumi) throws IOException {
        int id = albumi.getAlId();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getAlId() == id) {
                alkiot[i] = albumi;
                muutettu = true;
                return;
            }
        }
        lisaa(albumi);
    }


    /**
     * Käy albumien tietorakenteen läpi ja vertaa sitä merkkijonoon. 
     * Jos löytyy samanniminen albumi palauttaa sen ID:n, jos ei löydy
     * niin palauttaa 0.
     * @param albumi minkä nimisen albumin ID halutaan
     * @return Albumin Id
     * @example
     * <pre name="test">
     * Albumit Albumit = new Albumit();
     * Albumi al1 = new Albumi();  
     * al1.rekisteroi();
     * al1.aseta(0, Integer.toString(11));
     * al1.aseta(1, "Joululaulut");
     * Albumit.lisaa(al1);
     * Albumit.palautaAlbuminId("Joululaulut") === 11;
     * Albumit.palautaAlbuminId("JouLULauLUT") === 11;
     * Albumit.palautaAlbuminId("gfgffgf") === 0;
     * </pre>
     */
    public int palautaAlbuminId(String albumi) {
        int id = 0;
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getNimi().equalsIgnoreCase(albumi)) {
                id = alkiot[i].getAlId();
                break;
            }
        }

        return id;
    }


    /**
     * Poistaa Albumin jolla on valittu tunnusnumero 
     * @param id poistettavan albumin tunnusnumero
     * @return 1 jos poistettiin, 0 jos ei löydy
     * @example
     * <pre name="test">
     * #THROWS IOException 
     * Albumit albumit = new Albumit();
     * Albumi al1 = new Albumi(), al2 = new Albumi(), al3 = new Albumi();
     * al1.rekisteroi(); al2.rekisteroi(); al3.rekisteroi();
     * int id1 = al1.getAlId();
     * albumit.lisaa(al1); albumit.lisaa(al2); albumit.lisaa(al3);
     * albumit.poista(id1+1) === 1;
     * albumit.anna(id1+1); #THROWS IndexOutOfBoundsException 
     * albumit.getLkm() === 2;
     * albumit.poista(id1) === 1; albumit.getLkm() === 1;
     * albumit.poista(id1+3) === 0; albumit.getLkm() === 1;
     * </pre>
     * 
     */
    public int poista(int id) {
        int ind = etsiId(id);
        if (ind < 0)
            return 0;
        lkm--;
        for (int i = ind; i < lkm; i++)
            alkiot[i] = alkiot[i + 1];
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }


    /**
     * Etsii albumin id:n perusteella
     * @param id tunnusnumero, jonka mukaan etsitään
     * @return löytyneen albumin indeksi tai -1 jos ei löydy
     * <pre name="test">
     * #THROWS IOException 
     * Albumit albumit = new Albumit();
     * Albumi a1 = new Albumi(), a2 = new Albumi(), a3 = new Albumi();
     * a1.rekisteroi(); a2.rekisteroi(); a3.rekisteroi();
     * int id1 = a1.getAlId();
     * albumit.lisaa(a1); albumit.lisaa(a2); albumit.lisaa(a3);
     * albumit.etsiId(id1+1) === 1;
     * albumit.etsiId(id1+2) === 2;
     * </pre>
     */
    public int etsiId(int id) {
        for (int i = 0; i < lkm; i++)
            if (id == alkiot[i].getAlId())
                return i;
        return -1;
    }


    /**
     * Palauttaa viitteen n:een albumiin
     * @param i albumin paikka taulukossa
     * @return i:en albumin viitteen
     * @throws IndexOutOfBoundsException jos indeksiä ei ole 
     */
    public Albumi anna(int i) throws IndexOutOfBoundsException {
        if (i == -1)
            return null; // i = -1 jos etsiID:llä ei löydy albumia.
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Sopimaton indeksi: " + i);
        return alkiot[i];
    }


    /**
     * Lukee albumit tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws IOException jos lukeminen epäonnistuu
     *
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.File;
     * #import java.io.IOException;
     * #import java.util.Iterator;
     *  Albumit albumit = new Albumit();
     *  Albumi al1 = new Albumi(); al1.taytaAlbumi();
     *  Albumi al2 = new Albumi(); al2.taytaAlbumi();
     *  Albumi al3 = new Albumi(); al3.taytaAlbumi();
     *  Albumi al4 = new Albumi(); al4.taytaAlbumi();
     *  Albumi al5 = new Albumi(); al5.taytaAlbumi();
     *  String tiedNimi = "testialbumit";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  albumit.lueTiedostosta(tiedNimi); #THROWS IOException
     *  albumit.lisaa(al1);
     *  albumit.lisaa(al2);
     *  albumit.lisaa(al3);
     *  albumit.lisaa(al4);
     *  albumit.lisaa(al5);
     *  albumit.tallenna();
     *  albumit = new Albumit();
     *  albumit.lueTiedostosta(tiedNimi);
     *  Iterator<Albumi> i = albumit.iterator();
     *  i.next().toString() === al1.toString();
     *  i.next().toString() === al2.toString();
     *  i.next().toString() === al3.toString();
     *  i.next().toString() === al4.toString();
     *  i.next().toString() === al5.toString();
     *  i.hasNext() === false;
     *  albumit.lisaa(al4);
     *  albumit.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws IOException {
        setTiedostonPerusNimi(tied);
        try (Scanner fi = new Scanner(
                new FileInputStream(new File(getTiedostonNimi())))) {

            while (fi.hasNext()) {
                String rivi = fi.nextLine();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Albumi al = new Albumi();
                al.parse(rivi); // voisi olla virhekäsittely
                lisaa(al);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new IOException(
                    "Tiedosto " + getTiedostonNimi() + " ei aukea");
        }

    }


    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws IOException jos tulee poikkeus
     */
    public void lueTiedostosta() throws IOException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Tallentaa albumit tiedostoon.
     * @throws IOException virheilmoitus jos talletus epäonnistuu
     */
    public void tallenna() throws IOException {
        if (!muutettu)
            return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if ... System.err.println("Ei voi nimetä");
        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            for (Albumi al : this) {
                fo.println(al.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new IOException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new IOException("Tiedoston " + ftied.getName()
                    + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }


    /**
     * Palauttaa albumien lukumäärän
     * @return albumien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    /**
     * Luokka albumien iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     * Albumit albumit = new Albumit();
     * Albumi JVG1 = new Albumi(), JVG2 = new Albumi();
     * JVG1.rekisteroi(); JVG2.rekisteroi();
     *
     * albumit.lisaa(JVG1);
     * albumit.lisaa(JVG2);
     * albumit.lisaa(JVG1);
     *
     * StringBuffer ids = new StringBuffer(30);
     * for (Albumi albumi:albumit)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+albumi.getAlId());           
     *
     * String tulos = " " + JVG1.getAlId() + " " + JVG2.getAlId() + " " + JVG1.getAlId();
     *
     * ids.toString() === tulos;
     *
     * ids = new StringBuffer(30);
     * for (Iterator<Albumi>  i=albumit.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Albumi albumi = i.next();
     *   ids.append(" "+albumi.getAlId());           
     * }
     *
     * ids.toString() === tulos;
     *
     * Iterator<Albumi>  i=albumit.iterator();
     * i.next() == JVG1  === true;
     * i.next() == JVG2  === true;
     * i.next() == JVG1  === true;
     *
     * i.next();  #THROWS NoSuchElementException
     * 
     * </pre>
     */
    public class AlbumitIterator implements Iterator<Albumi> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa albumia
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä albumeita
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava albumi
         * @return seuraava albumi
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Albumi next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }


    /**
     * Palautetaan iteraattori albumeistaan.
     * @return albumi iteraattori
     */
    @Override
    public Iterator<Albumi> iterator() {
        return new AlbumitIterator();

    }


    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien albumien viitteet
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä albumeista
     * @example
     * <pre name="test">
     * #THROWS IOException 
     *   Albumit albumit = new Albumit();
     *   Albumi albumi1 = new Albumi(); albumi1.parse("1|Moiiii");
     *   Albumi albumi2 = new Albumi(); albumi2.parse("2|Deeku");
     *   Albumi albumi3 = new Albumi(); albumi3.parse("3|Ejejej");
     *   Albumi albumi4 = new Albumi(); albumi4.parse("4|Laulut");
     *   Albumi albumi5 = new Albumi(); albumi5.parse("5|Kesä");
     *   albumit.lisaa(albumi1); albumit.lisaa(albumi2); albumit.lisaa(albumi3); albumit.lisaa(albumi4); albumit.lisaa(albumi5);
     *   Collection<Integer> loytyneet; 
     *   loytyneet = albumit.etsi("*e*",1); 
     *   loytyneet.size() === 3; 
     *     
     *   loytyneet = albumit.etsi("*d*",1); 
     *   loytyneet.size() === 1; 
     *     
     *   loytyneet = albumit.etsi(null,-1); 
     *   loytyneet.size() === 5; 
     * </pre>
     */
    public Collection<Integer> etsi(String hakuehto, int k) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0)
            ehto = hakuehto;
        int hk = k;
        List<Integer> loytyneet = new ArrayList<Integer>();
        for (Albumi albumi : this) {
            if (WildChars.onkoSamat(albumi.anna(hk), ehto))
                loytyneet.add(albumi.getAlId());
        }
        return loytyneet;
    }


    /**
     * Testiohjelma albumeille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Albumit albumit = new Albumit();
        Albumi al1 = new Albumi();
        al1.rekisteroi();
        al1.taytaAlbumi();
        Albumi al2 = new Albumi();
        al2.rekisteroi();
        al2.taytaAlbumi();
        Albumi al3 = new Albumi();
        al3.rekisteroi();
        al3.taytaAlbumi();
        Albumi al4 = new Albumi();
        al4.rekisteroi();
        al4.taytaAlbumi();
        albumit.lisaa(al1);
        albumit.lisaa(al2);
        albumit.lisaa(al3);
        albumit.lisaa(al4);
        albumit.lisaa(al4);
        System.out.println("============= albumit testi =================");

        for (int i = 0; i < albumit.getLkm(); i++) {
            Albumi albumi = albumit.anna(i);

            albumi.tulosta(System.out);
            System.out.println("======================");
        }
    }

}
