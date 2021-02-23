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
 * @version 6.5.2019
 *
 */
public class Artistit implements Iterable<Artisti> {
    private static final int MAX_ARTISTEJA = 7;
    private int lkm = 0;
    private String tiedostonPerusNimi = "artistit";
    private Artisti alkiot[] = new Artisti[MAX_ARTISTEJA];
    private boolean muutettu = false;


    /**
     * Artistien alustaminen
     */
    public Artistit() {
        //
    }


    /**
     * Lisää uuden artistin tietorakenteeseen.
     * @param artisti lisättävän artistin viite
     * @example
     * <pre name="test">
     * Artistit artistit = new Artistit();
     * Artisti art1 = new Artisti(), art2 = new Artisti();
     * artistit.getLkm() === 0;
     * artistit.lisaa(art1); artistit.getLkm() === 1;
     * artistit.lisaa(art2); artistit.getLkm() === 2;
     * artistit.lisaa(art1); artistit.getLkm() === 3;
     * artistit.anna(0) === art1;
     * artistit.anna(1) === art2;
     * artistit.anna(2) === art1;
     * artistit.anna(1) == art1 === false;
     * artistit.anna(1) == art2 === true;
     * artistit.lisaa(art1); artistit.getLkm() === 4;
     * artistit.lisaa(art1); artistit.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Artisti artisti) {
        if (lkm >= alkiot.length) {
            int koko = alkiot.length + 7;
            Artisti kopioalkiot[] = new Artisti[koko];

            int j = 0;

            for (int i = 0; i < alkiot.length; i++) {
                kopioalkiot[j] = alkiot[i];
                j++;
            }

            alkiot = kopioalkiot;
        }

        alkiot[lkm++] = artisti;
        muutettu = true;
    }


    /**
     * Korvaa artistin tietorakenteessa.  Ottaa artistin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva artisti.  Jos ei löydy,
     * niin lisätään uutena artistina.
     * @param artisti lisättävän artistin viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws IOException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS IOException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Artistit artistit = new Artistit(); 
     * Artisti JVG1 = new Artisti(), JVG2 = new Artisti();  
     * JVG1.rekisteroi(); JVG2.rekisteroi();
     * artistit.getLkm() === 0;
     * artistit.korvaaTaiLisaa(JVG1); artistit.getLkm() === 1;
     * artistit.korvaaTaiLisaa(JVG2); artistit.getLkm() === 2;
     * Artisti JVG3 = JVG1.clone();
     * JVG3.aseta(3,"kkk");
     * Iterator<Artisti> it = artistit.iterator();
     * it.next() == JVG1 === true;
     * artistit.korvaaTaiLisaa(JVG3); artistit.getLkm() === 2;
     * it = artistit.iterator();
     * Artisti j0 = it.next();
     * j0 === JVG3;
     * j0 == JVG3 === true;
     * j0 == JVG1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Artisti artisti) throws IOException {
        int id = artisti.getArId();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getArId() == id) {
                alkiot[i] = artisti;
                muutettu = true;
                return;
            }
        }
        lisaa(artisti);
    }


    /**
     * Käy artistien tietorakenteen läpi ja vertaa sitä merkkijonoon. 
     * Jos löytyy samanniminen artisti palauttaa sen ID:n, jos ei löydy
     * niin palauttaa 0.
     * @param artisti minkä nimisen artistin ID halutaan
     * @return Artistin Id
     * @example
     * <pre name="test">
     * Artistit artistit = new Artistit();
     * Artisti ar1 = new Artisti();  
     * ar1.rekisteroi();
     * ar1.aseta(0, Integer.toString(11));
     * ar1.aseta(1, "Sanni");
     * artistit.lisaa(ar1);
     * artistit.palautaArtistinId("Sanni") === 11;
     * artistit.palautaArtistinId("SaNNi") === 11;
     * artistit.palautaArtistinId("hohohoo") === 0;
     * </pre>
     */
    public int palautaArtistinId(String artisti) {
        int id = 0;
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getNimi().equalsIgnoreCase(artisti)) {
                id = alkiot[i].getArId();
                break;
            }
        }

        return id;
    }


    /**
     * Poistaa Artistin jolla on valittu tunnusnumero 
     * @param id poistettavan artistin tunnusnumero
     * @return 1 jos poistettiin, 0 jos ei löydy
     * @example
     * <pre name="test">
     * #THROWS IOException 
     * Artistit artistit = new Artistit();
     * Artisti JVG1 = new Artisti(), JVG2 = new Artisti(), JVG3 = new Artisti();
     * JVG1.rekisteroi(); JVG2.rekisteroi(); JVG3.rekisteroi();
     * int id1 = JVG1.getArId();
     * artistit.lisaa(JVG1); artistit.lisaa(JVG2); artistit.lisaa(JVG3);
     * artistit.poista(id1+1) === 1;
     * artistit.anna(id1+1); #THROWS IndexOutOfBoundsException 
     * artistit.getLkm() === 2;
     * artistit.poista(id1) === 1; artistit.getLkm() === 1;
     * artistit.poista(id1+3) === 0; artistit.getLkm() === 1;
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
     * Etsii artistin id:n perusteella
     * @param id tunnusnumero, jonka mukaan etsitään
     * @return löytyneen artistin indeksi tai -1 jos ei löydy
     * <pre name="test">
     * #THROWS IOException 
     * Artistit artistit = new Artistit();
     * Artisti JVG1 = new Artisti(), JVG2 = new Artisti(), JVG3 = new Artisti();
     * JVG1.rekisteroi(); JVG2.rekisteroi(); JVG3.rekisteroi();
     * int id1 = JVG1.getArId();
     * artistit.lisaa(JVG1); artistit.lisaa(JVG2); artistit.lisaa(JVG3);
     * artistit.etsiId(id1+1) === 1;
     * artistit.etsiId(id1+2) === 2;
     * </pre>
     */
    public int etsiId(int id) {
        for (int i = 0; i < lkm; i++)
            if (id == alkiot[i].getArId())
                return i;
        return -1;
    }


    /**
     * Palauttaa viitteen i:een artistiin
     * @param i artistin paikka taulukossa
     * @return i:en artistin viitteen
     * @throws IndexOutOfBoundsException jos indeksiä ei ole 
     */
    public Artisti anna(int i) throws IndexOutOfBoundsException {
        if (i == -1)
            return null; // i = -1 jos etsiID:llä ei löydy artistia.
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Sopimaton indeksi: " + i);
        return alkiot[i];
    }


    /**
     * Lukee artistit tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws IOException jos lukeminen epäonnistuu
     *
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.File;
     * #import java.io.IOException;
     * #import java.util.Iterator;
     *  Artistit artistit = new Artistit();
     *  Artisti JVG21 = new Artisti(); JVG21.taytaArtisti();
     *  Artisti JVG11 = new Artisti(); JVG11.taytaArtisti();
     *  Artisti JVG02 = new Artisti(); JVG02.taytaArtisti();
     *  Artisti JVG23 = new Artisti(); JVG23.taytaArtisti();
     *  Artisti JVG33 = new Artisti(); JVG33.taytaArtisti();
     *  String tiedNimi = "testiartistit";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  artistit.lueTiedostosta(tiedNimi); #THROWS IOException
     *  artistit.lisaa(JVG21);
     *  artistit.lisaa(JVG11);
     *  artistit.lisaa(JVG02);
     *  artistit.lisaa(JVG23);
     *  artistit.lisaa(JVG33);
     *  artistit.tallenna();
     *  artistit = new Artistit();
     *  artistit.lueTiedostosta(tiedNimi);
     *  Iterator<Artisti> i = artistit.iterator();
     *  i.next().toString() === JVG21.toString();
     *  i.next().toString() === JVG11.toString();
     *  i.next().toString() === JVG02.toString();
     *  i.next().toString() === JVG23.toString();
     *  i.next().toString() === JVG33.toString();
     *  i.hasNext() === false;
     *  artistit.lisaa(JVG23);
     *  artistit.tallenna();
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
                Artisti ar = new Artisti();
                ar.parse(rivi); // voisi olla virhekäsittely
                lisaa(ar);
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
     * Tallentaa artistit tiedostoon.
     * @throws IOException virheilmoitus jos talletus epäonnistuu
     * <pre>
     * 1|artisti011
     * 2|artisti023
     * 3|artisti035
     * </pre>
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
            for (Artisti ar : this) {
                fo.println(ar.toString());
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
     * Palauttaa artistien lukumäärän
     * @return artistien lukumäärä
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
     * Luokka artistien iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     * Artistit artistit = new Artistit();
     * Artisti JVG1 = new Artisti(), JVG2 = new Artisti();
     * JVG1.rekisteroi(); JVG2.rekisteroi();
     *
     * artistit.lisaa(JVG1);
     * artistit.lisaa(JVG2);
     * artistit.lisaa(JVG1);
     *
     * StringBuffer ids = new StringBuffer(30);
     * for (Artisti artisti:artistit)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+artisti.getArId());           
     *
     * String tulos = " " + JVG1.getArId() + " " + JVG2.getArId() + " " + JVG1.getArId();
     *
     * ids.toString() === tulos;
     *
     * ids = new StringBuffer(30);
     * for (Iterator<Artisti>  i=artistit.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Artisti artisti = i.next();
     *   ids.append(" "+artisti.getArId());           
     * }
     *
     * ids.toString() === tulos;
     *
     * Iterator<Artisti>  i=artistit.iterator();
     * i.next() == JVG1  === true;
     * i.next() == JVG2  === true;
     * i.next() == JVG1  === true;
     *
     * i.next();  #THROWS NoSuchElementException
     * 
     * </pre>
     */
    public class ArtistitIterator implements Iterator<Artisti> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa artistia
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä artisteja
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava artisti
         * @return seuraava artisti
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Artisti next() throws NoSuchElementException {
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
     * Palautetaan iteraattori artisteistaan.
     * @return artisti iteraattori
     */
    @Override
    public Iterator<Artisti> iterator() {
        return new ArtistitIterator();

    }


    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien artistien viitteet
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä artisteista
     * @example
     * <pre name="test">
     * #THROWS IOException 
     *   Artistit artistit = new Artistit();
     *   Artisti artisti1 = new Artisti(); artisti1.parse("1|JVG");
     *   Artisti artisti2 = new Artisti(); artisti2.parse("2|Lordi");
     *   Artisti artisti3 = new Artisti(); artisti3.parse("3|Eminem");
     *   Artisti artisti4 = new Artisti(); artisti4.parse("4|PMMP");
     *   Artisti artisti5 = new Artisti(); artisti5.parse("5|Cheek");
     *   artistit.lisaa(artisti1); artistit.lisaa(artisti2); artistit.lisaa(artisti3); artistit.lisaa(artisti4); artistit.lisaa(artisti5);
     *   Collection<Integer> loytyneet; 
     *   loytyneet = artistit.etsi("*e*",1); 
     *   loytyneet.size() === 2; 
     *     
     *   loytyneet = artistit.etsi("*d*",1); 
     *   loytyneet.size() === 1; 
     *     
     *   loytyneet = artistit.etsi(null,-1); 
     *   loytyneet.size() === 5; 
     * </pre>
     */
    public Collection<Integer> etsi(String hakuehto, int k) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0)
            ehto = hakuehto;
        int hk = k;
        List<Integer> loytyneet = new ArrayList<Integer>();
        for (Artisti artisti : this) {
            if (WildChars.onkoSamat(artisti.anna(hk), ehto))
                loytyneet.add(artisti.getArId());
        }
        return loytyneet;
    }


    /**
     * Testiohjelma artisteille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Artistit artistit = new Artistit();
        Artisti JVG1 = new Artisti();
        JVG1.rekisteroi();
        JVG1.taytaArtisti();
        Artisti JVG2 = new Artisti();
        JVG2.rekisteroi();
        JVG2.taytaArtisti();
        Artisti JVG3 = new Artisti();
        JVG3.rekisteroi();
        JVG3.taytaArtisti();
        Artisti JVG4 = new Artisti();
        JVG4.rekisteroi();
        JVG4.taytaArtisti();
        artistit.lisaa(JVG1);
        artistit.lisaa(JVG2);
        artistit.lisaa(JVG3);
        artistit.lisaa(JVG2);
        artistit.lisaa(JVG4);
        System.out.println("============= Artistit testi =================");

        for (int i = 0; i < artistit.getLkm(); i++) {
            Artisti artisti = artistit.anna(i);

            artisti.tulosta(System.out);
            System.out.println("======================");
        }
    }

}
