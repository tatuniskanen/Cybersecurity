package rekisteri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 10.6.2019
 *
 */
public class Kappaleet implements Iterable<Kappale> {

    private boolean muutettu = false;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "kappaleet";
    private final List<Kappale> alkiot = new ArrayList<Kappale>();


    /**
     * Alustetaan kappaleet.
     */
    public Kappaleet() {
        //
    }


    /**
     * Lisätään uusi kappale tietorakenteeseen.
     * @param kappale lisättävän kappaleen viite
     * @example
     * <pre name="test">
     * Kappaleet kappaleet = new Kappaleet();
     * Kappale laulu1 = new Kappale();
     * Kappale laulu2 = new Kappale();
     * kappaleet.getLkm() === 0;
     * kappaleet.lisaa(laulu1);
     * kappaleet.getLkm() === 1;
     * kappaleet.lisaa(laulu2);
     * kappaleet.getLkm() === 2;
     * </pre>
     */
    public void lisaa(Kappale kappale) {
        alkiot.add(kappale);
        muutettu = true;
    }


    /**
     * Korvaa kappaleen tietorakenteessa.  Ottaa kappaleen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva kappale.  Jos ei löydy,
     * niin lisätään uutena kappaleena.
     * @param kappale lisättävän kappaleen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws IOException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS IOException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * #import java.io.IOException;
     * #import java.util.Iterator;
     * Kappaleet kappaleet = new Kappaleet();
     * Kappale k1 = new Kappale(), k2 = new Kappale();
     * k1.rekisteroi(); k2.rekisteroi();
     * kappaleet.getLkm() === 0;
     * kappaleet.korvaaTaiLisaa(k1); kappaleet.getLkm() === 1;
     * kappaleet.korvaaTaiLisaa(k2); kappaleet.getLkm() === 2;
     * Kappale k3 = k1.clone();
     * k3.aseta(2,"kkk");
     * Iterator<Kappale> i2 = kappaleet.iterator();
     * i2.next() === k1;
     * kappaleet.korvaaTaiLisaa(k3); kappaleet.getLkm() === 2;
     * i2 = kappaleet.iterator();
     * Kappale k = i2.next();
     * k === k3;
     * k == k3 === true;
     * k == k1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Kappale kappale) throws IOException {
        int id = kappale.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, kappale);
                muutettu = true;
                return;
            }
        }
        lisaa(kappale);
    }


    /**
     * Poistaa valitun kappaleen
     * @param kappale poistettava kappale
     * @return tosi jos löytyi poistettava tietue
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.File;
     *  Kappaleet kappaleet = new Kappaleet();
     *  Kappale ka1 = new Kappale(); ka1.taytaKappale(2, 3);
     *  Kappale ka2 = new Kappale(); ka2.taytaKappale(5, 6);
     *  Kappale ka3 = new Kappale(); ka3.taytaKappale(7, 8);
     *  Kappale ka4 = new Kappale(); ka4.taytaKappale(11, 15);
     *  Kappale ka5 = new Kappale(); ka5.taytaKappale(18, 20);
     *  kappaleet.lisaa(ka1);
     *  kappaleet.lisaa(ka2);
     *  kappaleet.lisaa(ka3);
     *  kappaleet.lisaa(ka4);
     *  kappaleet.poista(ka5) === false ; kappaleet.getLkm() === 4;
     *  kappaleet.poista(ka3) === true;   kappaleet.getLkm() === 3;
     *  List<Kappale> k = kappaleet.annaKappaleet();
     *  k.size() === 3;
     *  k.get(0) === ka1;
     * </pre>
     */
    public boolean poista(Kappale kappale) {
        boolean ret = alkiot.remove(kappale);
        if (ret)
            muutettu = true;
        return ret;
    }


    /**
     * Lukee kappaleet tiedostosta.
     * @param tied tiedoston perusnimi
     * @throws IOException jos lukeminen epäonnistuu
     *
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.File;
     *  Kappaleet kappaleet = new Kappaleet();
     *  Kappale laulu1 = new Kappale(), laulu2 = new Kappale();
     *  laulu1.taytaKappale(1,1);
     *  laulu2.taytaKappale(1,1);
     *  String hakemisto = "testikappaleet";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  kappaleet.lueTiedostosta(tiedNimi); #THROWS IOException
     *  kappaleet.lisaa(laulu1);
     *  kappaleet.lisaa(laulu2);
     *  kappaleet.tallenna();
     *  kappaleet = new Kappaleet();            // Poistetaan vanhat luomalla uusi
     *  kappaleet.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<Kappale> i = kappaleet.iterator();
     *  i.next() === laulu1;
     *  i.next() === laulu2;
     *  i.hasNext() === false;
     *  kappaleet.lisaa(laulu2);
     *  kappaleet.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws IOException {
        setTiedostonPerusNimi(tied);
        try (Scanner fi = new Scanner(
                new FileInputStream(new File(getTiedostonNimi())))) {
            kokoNimi = fi.nextLine();
            if (kokoNimi == null)
                throw new IOException("Rekisterin nimi puuttuu");
            String koko = fi.nextLine();
            if (koko == null)
                throw new IOException("Maksimikoko puuttuu");
            // int maxKoko = Mjonot.erotaInt(rivi,10); // tehdään jotakin
            while (fi.hasNext()) {
                String rivi = fi.nextLine();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Kappale kappale = new Kappale();
                kappale.parse(rivi); // voisi olla virhekäsittely
                lisaa(kappale);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new IOException(
                    "Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch (IOException e) {
            throw new IOException(
                    "Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }


    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws IOException virheilmoitus
     */
    public void lueTiedostosta() throws IOException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Tallentaa kappaleet tiedostoon.
     * Tiedoston muoto:
     * Lempparit
     * 20
     * ; kommenttirivi
     * 1|Laulu006|1|1|2012|120|3
     * 2|Laulu006|1|1|2012|120|3
     * 3|Laulu019|2|2|2012|120|3
     * @throws IOException virheilmoitus
     */
    public void tallenna() throws IOException {
        if (!muutettu)
            return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");
        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            fo.println(getKokoNimi());
            fo.println(alkiot.size());
            for (Kappale kappale : this) {
                fo.println(kappale.toString());
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
     * Palauttaa rekisterin koko nimen
     * @return Kerhon koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }


    /**
     * Palauttaa kappaleiden lukumäärä.
     * @return kappaleide lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }


    /**
     * Palauttaa viitteen kappaleeseen, jonka tunnusnro on i.
     * @param i minkä kappaleen viite halutaan
     * @return viite jäseneen, jonka tunnusnro on i
     * @throws IOException ,jos viitettä ei ole
     * @example
     * <pre name="test">
     * #THROWS IOException
     * Kappale k1 = new Kappale();
     * Kappale k2 = new Kappale();
     * Kappale k3 = new Kappale();
     * k1.rekisteroi();
     * k2.rekisteroi();
     * k3.rekisteroi();
     * Kappaleet kt = new Kappaleet();
     * kt.lisaa(k1);
     * kt.lisaa(k2);
     * kt.lisaa(k3);
     * int testiNro = k1.getTunnusNro();
     * kt.anna(testiNro + 1) == k2 === true;
     * kt.anna(testiNro + 2) == k3 ===true;
     * </pre>
     */
    public Kappale anna(int i) throws IOException {

        Kappale loytynyt = new Kappale();
        try {
            for (Kappale kappale : alkiot) {
                if (i == kappale.getTunnusNro()) {
                    loytynyt = kappale;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            throw new IOException(ex.getMessage() + "Laiton tunnusnro: " + i);
        }
        return loytynyt;
    }


    /**
     * Palauttaa kaikki kappaleet
     * @return tietorakenteen jossa kaikki kappaleet.
     */
    public List<Kappale> annaKappaleet() {
        return alkiot;
    }


    /**
     * Palauttaa annettua artisti id:tä vastaavat kappaleet
     * @param tunnusnro minkä artistin kappaleet halutaan
     * @return tietorakenne löydetyistä kappaleista
     * @example
     * <pre name="test">
     * Kappale kap1 = new Kappale();
     * Kappale kap2 = new Kappale();
     * Kappale kap3 = new Kappale();
     * kap1.taytaKappale(1, 2);
     * kap2.taytaKappale(1, 5);
     * kap3.taytaKappale(3, 2);
     * Kappaleet kplt = new Kappaleet();
     * kplt.lisaa(kap1);
     * kplt.lisaa(kap2);
     * kplt.lisaa(kap3);
     * List<Kappale> loyetyt = kplt.annaArtistinKappaleet(1);
     * loyetyt.size() === 2;
     * loyetyt.get(0) == kap1 === true;
     * loyetyt.get(1) == kap2 === true;
     * List<Kappale> loyetyt2 = kplt.annaArtistinKappaleet(3);
     * loyetyt2.size() === 1;
     * loyetyt2.get(0) == kap3 === true;
     * </pre>
     */
    public List<Kappale> annaArtistinKappaleet(int tunnusnro) {
        List<Kappale> loydetyt = new ArrayList<Kappale>();
        for (Kappale kap : alkiot)
            if (kap.getArId() == tunnusnro)
                loydetyt.add(kap);
        return loydetyt;
    }


    /**
     * Poistaa artistin kaikki kappaleet
     * @param artId artistin id
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.File;
     *  Kappaleet kappaleet = new Kappaleet();
     *  Kappale ka1 = new Kappale(); ka1.taytaKappale(2, 3);
     *  Kappale ka2 = new Kappale(); ka2.taytaKappale(2, 6);
     *  Kappale ka3 = new Kappale(); ka3.taytaKappale(7, 8);
     *  Kappale ka4 = new Kappale(); ka4.taytaKappale(11, 15);
     *  Kappale ka5 = new Kappale(); ka5.taytaKappale(14, 20);
     *  kappaleet.lisaa(ka1);
     *  kappaleet.lisaa(ka2);
     *  kappaleet.lisaa(ka3);
     *  kappaleet.lisaa(ka4);
     *  kappaleet.poista(ka5) === false ; kappaleet.getLkm() === 4;
     *  kappaleet.poistaArtistinKappaleet(2);   
     *  kappaleet.getLkm() === 2;
     *  List<Kappale> k = kappaleet.annaKappaleet();
     *  k.size() === 2;
     *  k.get(0) === ka3;
     *  k.get(1) === ka4;
     * </pre>
     */
    public void poistaArtistinKappaleet(int artId) {
        List<Kappale> poistettavat = annaArtistinKappaleet(artId);
        for (Kappale poistettava : poistettavat) {
            poista(poistettava);
        }
    }


    /**
     * Palauttaa annettua albumi id:tä vastaavat kappaleet
     * @param tunnusnro minkä artistin kappaleet halutaan
     * @return tietorakenne löydetyistä kappaleista
     * @example
     * <pre name="test">
     * Kappale kapp1 = new Kappale();
     * Kappale kapp2 = new Kappale();
     * Kappale kapp3 = new Kappale();
     * kapp1.taytaKappale(1, 2);
     * kapp2.taytaKappale(1, 5);
     * kapp3.taytaKappale(3, 2);
     * Kappaleet kpplt = new Kappaleet();
     * kpplt.lisaa(kapp1);
     * kpplt.lisaa(kapp2);
     * kpplt.lisaa(kapp3);
     * List<Kappale> loyet = kpplt.annaAlbuminKappaleet(2);
     * loyet.size() === 2;
     * loyet.get(0) == kapp1 === true;
     * loyet.get(1) == kapp3 === true;
     * List<Kappale> loyet2 = kpplt.annaAlbuminKappaleet(5);
     * loyet2.size() === 1;
     * loyet2.get(0) == kapp2 === true;
     * </pre>
     */
    public List<Kappale> annaAlbuminKappaleet(int tunnusnro) {
        List<Kappale> loydetyt = new ArrayList<Kappale>();
        for (Kappale kap : alkiot)
            if (kap.getAlId() == tunnusnro)
                loydetyt.add(kap);
        return loydetyt;
    }


    /**
     * Palauttaa tietorakenteessa hakuehtoon vastaavien kappaleiden viitteet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenne löytyneistä kappaleista
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.util.List;
     * Kappaleet kappaleet = new Kappaleet();
     * Kappale ka1 = new Kappale(); ka1.parse("1|Autolaulu|12|5|120|");
     * Kappale ka2 = new Kappale(); ka2.parse("2|Finlandia|110|18|130");
     * Kappale ka3 = new Kappale(); ka3.parse("3|Oi maamme Suomi|17|3|222");
     * Kappale ka4 = new Kappale(); ka4.parse("4|Sata kesää|22|6|190");
     * Kappale ka5 = new Kappale(); ka5.parse("5|Moottoritie on kuuma|47|13|128");
     * kappaleet.lisaa(ka1); kappaleet.lisaa(ka2); kappaleet.lisaa(ka3); kappaleet.lisaa(ka4); kappaleet.lisaa(ka5);
     * List<Kappale> loytyneet; 
     * loytyneet = (List<Kappale>)kappaleet.etsi("*s*",1); 
     * loytyneet.size() === 2; 
     * loytyneet.get(0) == ka3 === true; 
     * loytyneet.get(1) == ka4 === true; 
     *     
     * loytyneet = (List<Kappale>)kappaleet.etsi("*7*",2); 
     * loytyneet.size() === 2; 
     * loytyneet.get(0) == ka3 === true; 
     * loytyneet.get(1) == ka5 === true;    
     * loytyneet = (List<Kappale>)kappaleet.etsi(null,-1); 
     * loytyneet.size() === 5;  
     * </pre>
     */
    public Collection<Kappale> etsi(String hakuehto, int k) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0)
            ehto = hakuehto;
        int hk = k;
        List<Kappale> loytyneet = new ArrayList<Kappale>();
        for (Kappale kappale : this) {
            if (WildChars.onkoSamat(kappale.anna(hk), ehto))
                loytyneet.add(kappale);
        }
        Collections.sort(loytyneet, new Kappale.Vertailija(hk));
        return loytyneet;
    }


    /**
     * Palauttaa annetun minimiajan "mittaisen" tietorakenteen satunnaisesti valikoiduista kappaleista.
     * @param aikaMin aikaraja minuutteina, mihin asti kappaleita vähintään haetaan.
     * @return tietorakenne löytyneistä kappaleista
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.util.List;
     * Kappaleet kappaleet = new Kappaleet();
     * Kappale ka1 = new Kappale(); ka1.parse("1|Autolaulu|12|5|120|");
     * Kappale ka2 = new Kappale(); ka2.parse("2|Finlandia|110|18|120");
     * Kappale ka3 = new Kappale(); ka3.parse("3|Oi maamme Suomi|17|3|120");
     * Kappale ka4 = new Kappale(); ka4.parse("4|Sata kesää|22|6|120");
     * Kappale ka5 = new Kappale(); ka5.parse("5|Moottoritie on kuuma|47|13|120");
     * kappaleet.lisaa(ka1); kappaleet.lisaa(ka2); kappaleet.lisaa(ka3); kappaleet.lisaa(ka4); kappaleet.lisaa(ka5);
     * List<Kappale> soittolista = (List<Kappale>)kappaleet.haeAjanPerusteella(4); 
     * soittolista.size() === 2;
     * List<Kappale> soittolista2 = (List<Kappale>)kappaleet.haeAjanPerusteella(7); 
     * soittolista2.size() === 4;
     * </pre>
     */
    public Collection<Kappale> haeAjanPerusteella(int aikaMin) {
        List<Kappale> loytyneet = new ArrayList<Kappale>();
        int aikaSek = aikaMin * 60;
        int aikaraja = 0;

        Random rand = new Random();

        if (kaikkienKesto() > 0) {
            while (aikaraja < aikaSek) {
                Kappale lisattava = alkiot.get(rand.nextInt(alkiot.size()));
                if (lisattava.getKesto() > 0) {
                    loytyneet.add(lisattava);
                    aikaraja = aikaraja + lisattava.getKesto();
                }
            }
        }
        return loytyneet;
    }


    /**
     * Palauttaa kaikkien kappaleiden yhteenlasketun keston.
     * @return kaikkien kappaleiden yhteenlaskettu kesto
     */
    public int kaikkienKesto() {
        int summa = 0;
        for (Kappale alkio : alkiot) {
            summa = summa + alkio.getKesto();
        }
        return summa;
    }


    @Override
    public Iterator<Kappale> iterator() {
        return alkiot.iterator();
    }


    /**
     * Testipääohjelma kappaleille.
     * @param args ei kaytossä
     */
    public static void main(String[] args) {
        Kappaleet kappaleet = new Kappaleet();
        Kappale k1 = new Kappale();
        k1.taytaKappale(1, 1);
        Kappale k2 = new Kappale();
        k2.taytaKappale(1, 2);
        Kappale k3 = new Kappale();
        k3.taytaKappale(2, 4);
        kappaleet.lisaa(k1);
        kappaleet.lisaa(k2);
        kappaleet.lisaa(k3);

        for (Kappale ka : kappaleet) {
            System.out.println("-------------------------------");
            ka.tulosta(System.out);
        }
    }
}
