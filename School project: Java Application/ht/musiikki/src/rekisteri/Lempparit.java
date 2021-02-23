package rekisteri;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import fi.jyu.mit.fxgui.Dialogs;

/**
 * Luokka lempikappaleille
 * 
 * @author Taina Patrikainen ja Tatu Niskanen
 * @version 10.6.2019
 *
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 *  private Lempparit lempparit;
 *  
 *  private Kappale ka1;
 *  private Kappale ka2;
 *  private Albumi al1;
 *  private Albumi al2;
 *  private Artisti ar1;
 *  private Artisti ar2;
 *  private int kid1;
 *  private int kid2;
 * 
 *  public void alustaLempparit() {
 *    lempparit = new Lempparit();
 *    
 *    ar1 = new Artisti();  ar1.rekisteroi();
 *    ar1.aseta(0, Integer.toString(11));
 *    ar1.aseta(1, "Sanni");
 *    ar2 = new Artisti(); ar2.taytaArtisti(); ar2.rekisteroi();
 *    ar2.aseta(0, Integer.toString(12));
 *    ar2.aseta(1, "Cheeck");
 *    lempparit.lisaa(ar1);
 *    lempparit.lisaa(ar2);
 *    
 *    al1 = new Albumi(); al1.rekisteroi();
 *    al1.aseta(0, Integer.toString(21));
 *    al1.aseta(1, "JEA");
 *    al2 = new Albumi(); al2.rekisteroi();
 *    al2.aseta(0, Integer.toString(22));
 *    al2.aseta(1, "JEE");
 *    lempparit.lisaa(al1);
 *    lempparit.lisaa(al2);    
 *    
 *    ka1 = new Kappale();  ka1.rekisteroi();
 *    ka1.parse("   5  |  Laulu   | 11 | 21 | 330 | 2001 | 3");
 *    ka2 = new Kappale();  ka2.rekisteroi();
 *    ka2.parse("   6  |  Viisu   | 12 | 0 | | 2002 | 3");
 *    kid1 = ka1.getTunnusNro();
 *    kid2 = ka2.getTunnusNro();
 *    lempparit.lisaa(ka1);
 *    lempparit.lisaa(ka2);
 *  }
 * </pre>
 */
public class Lempparit {
    private Kappaleet kappaleet = new Kappaleet();
    private Artistit artistit = new Artistit();
    private Albumit albumit = new Albumit();


    /**
     * Poistaa tämän kappaleen
     * @param kappale poistettava kappale
     * @example
     * <pre name="test">
     *   alustaLempparit();
     *   lempparit.annaKappaleet().size() === 2;
     *   lempparit.poistaKappale(ka1);
     *   lempparit.annaKappaleet().size() === 1;
     */
    public void poistaKappale(Kappale kappale) {
        kappaleet.poista(kappale);
    }


    /**
     * Poistaa artistin kaikki kappaleet
     * @param artId artistin id
     * @example
     * <pre name="test">
     *   alustaLempparit();
     *   lempparit.annaKappaleet().size() === 2;
     *   lempparit.poistaArtistinKappaleet(11);
     *   lempparit.annaKappaleet().size() === 1;
     *   lempparit.poistaArtistinKappaleet(12);
     *   lempparit.annaKappaleet().size() === 0;
     */
    public void poistaArtistinKappaleet(int artId) {
        kappaleet.poistaArtistinKappaleet(artId);
    }


    /**
     * Poistaa artistin tiedot
     * @param arid poistettavan artistin Id
     * @example
     * <pre name="test">
     * alustaLempparit();
     * String[] tiedot1 = lempparit.muokkaaKappaleRiveiksi(ka1);
     * tiedot1 [0] === "Laulu";
     * tiedot1 [1] === "Sanni";
     * tiedot1 [2] === "JEA";
     *
     * lempparit.poistaArtisti(lempparit.palautaArtistinId("Sanni"));
     * String[] tiedot2 = lempparit.muokkaaKappaleRiveiksi(ka1);
     * tiedot2 [0] === "Laulu";
     * tiedot2 [1] === "ei artistia";
     * tiedot2 [2] === "JEA";
     * </pre>
     */
    public void poistaArtisti(int arid) {
        if (arid < 0)
            return;
        artistit.poista(arid);
    }


    /**
     * Palauttaa annettua nimeä vastaavan artistin id:n.
     * @param nimi artistin nimi
     * @return artistin id:n numeron
     * @example
     * <pre name="test">
     * alustaLempparit();
     * lempparit.palautaArtistinId("Sanni") === 11;
     * lempparit.palautaArtistinId("SaNNi") === 11;
     * lempparit.palautaArtistinId("hohohoo") === 0;
     * </pre>
     */
    public int palautaArtistinId(String nimi) {
        return artistit.palautaArtistinId(nimi);
    }


    /**
     * Poistaa albumin tiedot
     * @param alid albumin id, mikä poistetaan
     * @return montako albumia poistettiin
     * @example
     * <pre name="test">
     * alustaLempparit();
     * String[] tiedot1 = lempparit.muokkaaKappaleRiveiksi(ka1);
     * tiedot1 [0] === "Laulu";
     * tiedot1 [1] === "Sanni";
     * tiedot1 [2] === "JEA";
     *
     * lempparit.poistaAlbumi(21);
     * String[] tiedot2 = lempparit.muokkaaKappaleRiveiksi(ka1);
     * tiedot2 [0] === "Laulu";
     * tiedot2 [1] === "Sanni";
     * tiedot2 [2] === "ei albumia";
     * </pre>
     */
    public int poistaAlbumi(int alid) {
        if (alid < 0)
            return 0;
        int ret = albumit.poista(alid);
        return ret;
    }


    /**
     * Korvaa kappaleen tietorakenteessa.  Ottaa kappaleen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva kappale.  Jos ei löydy,
     * niin lisätään uutena kappaleena.
     * @param kappale lisättävän kappaleen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws IOException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS IOException 
     *  alustaLempparit();
     *  lempparit.etsi("*",0).size() === 2;
     *  lempparit.korvaaTaiLisaa(ka1);
     *  lempparit.etsi("*",0).size() === 2;
     * </pre>
     */
    public void korvaaTaiLisaa(Kappale kappale) throws IOException {
        kappaleet.korvaaTaiLisaa(kappale);
    }


    /**
     * Korvaa artistin tietorakenteessa.  Ottaa artistin omistukseensa.
     * Etsitään samalla artisti-id:llä oleva artisti.  Jos ei löydy,
     * niin lisätään uutena artistina.
     * @param artisti lisättävän artistin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws IOException jos tietorakenne on jo täynnä
     */
    public void korvaaTaiLisaa(Artisti artisti) throws IOException {
        artistit.korvaaTaiLisaa(artisti);
    }


    /**
     * Korvaa albumin tietorakenteessa.  Ottaa albumin omistukseensa.
     * Etsitään samalla albumi-id:llä oleva artisti.  Jos ei löydy,
     * niin lisätään uutena albumina.
     * @param albumi lisättävän albumin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws IOException jos tietorakenne on jo täynnä
     */
    public void korvaaTaiLisaa(Albumi albumi) throws IOException {
        albumit.korvaaTaiLisaa(albumi);
    }


    /**
     * Lisää kappaleen tietorakenteeseen.
     * @param kappale lisättävä kappale
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.IOException;
     * #import java.util.Collection;
     * #import java.util.Iterator;
     * Lempparit lempparit = new Lempparit();
     * Kappale laulu1 = new Kappale(), laulu2 = new Kappale();
     * lempparit.lisaa(laulu1);
     * lempparit.lisaa(laulu2);
     * lempparit.lisaa(laulu1);
     * Collection<Kappale> loytyneet = lempparit.etsi("",-1);
     * Iterator<Kappale> it = loytyneet.iterator();
     * it.next() === laulu1;
     * it.next() === laulu2;
     * it.next() === laulu1;
     * </pre>
     */
    public void lisaa(Kappale kappale) {
        kappaleet.lisaa(kappale);
    }


    /**
     * Lisää artistin tietorakenteeseen.
     * @param artisti lisättävä artisti
     */
    public void lisaa(Artisti artisti) {
        artistit.lisaa(artisti);
    }


    /**
     * Lisää albumin tietorakenteeseen.
     * @param albumi lisättävä albumi
     */
    public void lisaa(Albumi albumi) {
        albumit.lisaa(albumi);
    }


    /**
     * Palauttaa kappaleiden lukumäärän.
     * @return kappaleiden lukumäärä
     */
    public int getKappaleita() {
        return kappaleet.getLkm();
    }


    /**
     * Palauttaa i:n kappaleen
     * @param i monesko kappale palautetaan
     * @return "viite" i:teen kappaleeseen
     * @throws IOException virheilmoitus
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Kappale annaKappale(int i) throws IOException {
        return kappaleet.anna(i);
    }


    /**
     * Palauttaa i:n artistin
     * @param i "viite" i:teen artistiin
     * @return palauttaa viitteen mukaisen artistin
     * @throws IOException virheilmoitus, jos viite ei käy
     */
    public Artisti annaArtisti(int i) throws IOException {
        return artistit.anna(i);

    }


    /**
     * Palauttaa i:n albumin
     * @param i "viite" i:teen albumiin
     * @return palauttaa viitteen mukaisen albumin
     * @throws IOException virheilmoitus, viite ei käy
     */
    public Albumi annaAlbumi(int i) throws IOException {
        return albumit.anna(i);
    }


    /**
     * Etsii artisti-id:n perusteella artistin
     * @param id artistin id
     * @return palauttaa id:tä vastaavan artistin
     * @throws IOException virheilmoitus, jos viite ei käy
     */
    public Artisti etsiArtistiIdlla(int id) throws IOException {
        int i = artistit.etsiId(id);
        return annaArtisti(i);
    }


    /**
     * Etsii albumi-id:n perusteella albumin
     * @param id albumin id
     * @return palauttaa id:tä vastaavan albumin
     * @throws IOException virheilmoitus, jos id ei käy
     */
    public Albumi etsiAlbumiIdlla(int id) throws IOException {
        int i = albumit.etsiId(id);
        return annaAlbumi(i);
    }


    /**
     * Palauttaa kaikki kappaleet
     * @return tietorakenteen, jossa kaikki kappaleet
     */
    public Collection<Kappale> annaKappaleet() {
        return kappaleet.annaKappaleet();
    }


    /**
     * Palauttaa annetun kentän mukaan "etsityt" hakuehtoa vastaavat kappaleet tietorakenteessa.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenne löytyneistä kappaleista
     * @throws IOException virheilmoitus
     * @example
     * <pre name="test">
     * #THROWS IOException
     * alustaLempparit();
     * Collection<Kappale> loyto = lempparit.etsi("*", 1);
     * loyto.size() === 2;
     * Collection<Kappale> loyto2 = lempparit.etsi("Laulu", 1);
     * loyto2.size() === 1;
     * Collection<Kappale> loyto3 = lempparit.etsi("LAULU", 1);
     * loyto3.size() === 1;
     * </pre>
     */
    public Collection<Kappale> etsi(String hakuehto, int k) throws IOException {
        return kappaleet.etsi(hakuehto, k);
    }


    /**
     * Palauttaa annetun artistin nimen (=hakuehdon) mukaan löydetyt kappaleet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenne löytyneistä kappaleista
     * @throws IOException virheilmoitus
     * @example
     * <pre name="test">
     * #THROWS IOException
     * alustaLempparit();
     * Collection<Kappale> loydot = lempparit.etsiArtistinPerusteella("Sanni", 1);
     * loydot.size() === 1;
     * Collection<Kappale> loydot2 = lempparit.etsiArtistinPerusteella("SaNnI", 1);
     * loydot2.size() === 1;
     * Collection<Kappale> loydot3 = lempparit.etsiArtistinPerusteella("VilleGalle", 1);
     * loydot3.size() === 0;
     * </pre>
     */
    public Collection<Kappale> etsiArtistinPerusteella(String hakuehto, int k)
            throws IOException {
        Collection<Integer> tunnusnrot = artistit.etsi(hakuehto, k);
        Collection<Kappale> loytyneet = new ArrayList<Kappale>();
        for (int nro : tunnusnrot) {
            loytyneet.addAll(kappaleet.annaArtistinKappaleet(nro));
        }
        return loytyneet;
    }


    /**
     * Palauttaa annetun albumin nimen (=hakuehdon) mukaan löydetyt kappaleet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenne löytyneistä kappaleista
     * @throws IOException virheilmoitus
     * @example
     * <pre name="test">
     * #THROWS IOException
     * alustaLempparit();
     * Collection<Kappale> loyetyt = lempparit.etsiAlbuminPerusteella("*", 1);
     * loyetyt.size() === 1;
     * Collection<Kappale> loyetyt2 = lempparit.etsiAlbuminPerusteella("Pitkä nimi", 1);
     * loyetyt2.size() === 0;
     * </pre>
     */
    public Collection<Kappale> etsiAlbuminPerusteella(String hakuehto, int k)
            throws IOException {
        Collection<Integer> tunnusnrot = albumit.etsi(hakuehto, k);
        Collection<Kappale> loytyneet = new ArrayList<Kappale>();
        for (int nro : tunnusnrot) {
            loytyneet.addAll(kappaleet.annaAlbuminKappaleet(nro));
        }
        return loytyneet;
    }


    /**
     * Palauttaa annetun minimiajan "mittaisen" tietorakenteen satunnaisesti valikoiduista kappaleista.
     * @param aika aikaraja, johon asti haetaan kappaleita
     * @return tietorakenne löydetyistä kappaleista
     * @throws IOException virheilmoitus
     */
    public Collection<Kappale> haeAjanPerusteella(int aika) throws IOException {
        return kappaleet.haeAjanPerusteella(aika);
    }


    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if (!nimi.isEmpty())
            hakemistonNimi = nimi + "/";
        kappaleet.setTiedostonPerusNimi(hakemistonNimi + "kappaleet");
        artistit.setTiedostonPerusNimi(hakemistonNimi + "artistit");
        albumit.setTiedostonPerusNimi(hakemistonNimi + "albumit");
    }


    /**
     * Lukee lempparien tiedot tiedostosta
     * @param nimi luettavan tiedoston nimi
     * @throws IOException virheilmoitus
     * @example
     * <pre name="test">
     * #THROWS IOException
     * #import java.io.*;
     * #import java.util.*;
     *
     *  String hakemisto = "testilempparit";
     *  File dir = new File(hakemisto);
     *  File fktied  = new File(hakemisto+"/kappaleet.dat");
     *  File fartied = new File(hakemisto+"/artistit.dat");
     *  File faltied = new File(hakemisto+"/albumit.dat");
     *  dir.mkdir(); 
     *  fktied.delete();
     *  fartied.delete();
     *  faltied.delete();
     *  lempparit = new Lempparit(); // tiedostoja ei ole, tulee poikkeus
     *  lempparit.lueTiedostosta(hakemisto); #THROWS IOException
     *  alustaLempparit();
     *  lempparit.setTiedosto(hakemisto); // nimi annettava koska uusi poisti sen
     *  lempparit.tallenna();
     *  lempparit = new Lempparit();
     *  lempparit.lueTiedostosta(hakemisto);
     *  Collection<Kappale> kaikki = lempparit.etsi("",-1);
     *  Iterator<Kappale> it = kaikki.iterator();
     *  it.next() === ka1;
     *  it.next() === ka2;
     *  it.hasNext() === false;
     *  String [] tiedot1 = {"Hell yea", "Lordi", "", "120", "2012", "3"};
     *  lempparit.muokkaaRiveistaKappale(ka1, tiedot1);
     *  String [] tiedot2 = {"Fire", "Lordi", "Hard Rock", "120", "2012", "3"};
     *  lempparit.muokkaaRiveistaKappale(ka2, tiedot2);
     *  lempparit.lisaa(ka1);
     *  lempparit.lisaa(ka2);
     *  lempparit.tallenna(); // tekee kaikista .bak
     *  fktied.delete()  === true;
     *  fartied.delete() === true;
     *  faltied.delete() === true;
     *  File fkbak = new File(hakemisto+"/kappaleet.bak");
     *  File farbak = new File(hakemisto+"/artistit.bak");
     *  File falbak = new File(hakemisto+"/albumit.bak");
     *  fkbak.delete() === true;
     *  farbak.delete() === true;
     *  falbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws IOException {
        kappaleet = new Kappaleet(); // jos luetaan olemassa olevaan niin
                                     // helpoin tyhjentää näin
        artistit = new Artistit();
        albumit = new Albumit();

        setTiedosto(nimi);

        kappaleet.lueTiedostosta();
        artistit.lueTiedostosta();
        albumit.lueTiedostosta();
    }


    /**
     * Tallentaa lempparien tiedot tiedostoon.
    * @throws IOException virheilmoitus
    */
    public void tallenna() throws IOException {

        String virhe = "";
        try {
            kappaleet.tallenna();
        } catch (IOException ex) {
            virhe = ex.getMessage();
        }
        try {
            artistit.tallenna();
        } catch (IOException ex) {
            virhe += ex.getMessage();
        }
        try {
            albumit.tallenna();
        } catch (IOException ex) {
            virhe += ex.getMessage();
        }
        if (!"".equals(virhe))
            throw new IOException(virhe);
    }


    /**
     * Tekee kappaleen tiedoista merkkijonotaulukon
     * Hakee albumin ja artistin tunnusnumeroita vastaavat nimet
     * @param kappale kappale, jonka tiedot halutaan merkkijonoiksi
     * @return kappaleen tiedot merkkijonoina
     * @example
     * <pre name="test">
     * alustaLempparit();
     * String[] tiedot1 = lempparit.muokkaaKappaleRiveiksi(ka1);
     * tiedot1 [0] === "Laulu";
     * tiedot1 [1] === "Sanni";
     * tiedot1 [2] === "JEA";
     * tiedot1 [3] === "330";
     * tiedot1 [4] === "2001";
     * tiedot1 [5] === "3";
     * String[] tiedot2 = lempparit.muokkaaKappaleRiveiksi(ka2);
     * tiedot2 [0] === "Viisu";
     * tiedot2 [1] === "Cheeck";
     * tiedot2 [2] === "ei albumia";
     * tiedot2 [3] === "0";
     * tiedot2 [4] === "2002";
     * tiedot2 [5] === "3";
     * </pre>
     */
    public String[] muokkaaKappaleRiveiksi(Kappale kappale) {
        int kenttia = kappale.getKenttia();
        String[] rivit = new String[kenttia - kappale.ekaKentta()];
        for (int i = 0, k = kappale.ekaKentta(); k < kenttia; i++, k++) {
            if (k != 2 && k != 3) {
                String lisays = "";
                if (!kappale.anna(k).trim().equals("0"))
                    rivit[i] = kappale.anna(k);
                else
                    rivit[i] = lisays;
            }
            if (k == 2) {
                int tunnusNr = Integer.parseInt(kappale.anna(k));
                try {
                    Artisti ar = etsiArtistiIdlla(tunnusNr);
                    if (ar != null)
                        rivit[i] = ar.getNimi();
                    else
                        rivit[i] = "ei artistia";
                } catch (IOException ex) {
                    Dialogs.showMessageDialog(
                            "Artistin hakemisessa ongelma " + ex.getMessage());
                }
            }

            if (k == 3) {
                int tunnusNr = Integer.parseInt(kappale.anna(k));
                try {
                    Albumi al = etsiAlbumiIdlla(tunnusNr);
                    if (al != null)
                        rivit[i] = al.getNimi();
                    else
                        rivit[i] = "ei albumia";
                } catch (IOException ex) {
                    Dialogs.showMessageDialog(
                            "Artistin hakemisessa ongelma " + ex.getMessage());
                }
            }
        }
        return rivit;
    }


    /**
     * Tekee käyttäjältä saadusta merkkijono taulukosta kappaleen.
     * Lisää artisteihin/albumeihin uuden artistin/albumin, jos nimeä vastaavaa ei ennalta löydy.
     * @param kappale kappale, jonka tiedot halutaan asettaa
     * @param rivit käyttäjän täyttämät kappaleen tiedot merkkijono taulukossa
     * @return virheen tiedot
     * @example
     * <pre name="test">
     * alustaLempparit();
     * String [] tiedot2 = {"Pienissä häissä", "JVG", "", "120", "2012", "3"};
     * lempparit.muokkaaRiveistaKappale(ka2, tiedot2);
     * ka2.getNimi() === "Pienissä häissä";
     * ka2.getArId() === lempparit.palautaArtistinId("JVG");
     * </pre>
     */
    public String muokkaaRiveistaKappale(Kappale kappale, String[] rivit) {
        String virhe = "";

        for (int i = 0, k = kappale.ekaKentta(); k < kappale
                .getKenttia(); i++, k++) {

            if (rivit[i] != null && !rivit[i].trim().equals("")) {
                switch (k) {
                case 1:
                    virhe += kappale.aseta(k, rivit[i]);
                    break;
                case 2:
                    int arNr = artistit.palautaArtistinId(rivit[i]);
                    if (arNr > 0)
                        kappale.aseta(k, Integer.toString(arNr));
                    else {
                        Artisti ar = new Artisti();
                        ar.rekisteroi();
                        lisaa(ar);
                        virhe += ar.aseta(1, rivit[i]);
                        kappale.aseta(k, Integer.toString(ar.getArId()));
                    }
                    break;
                case 3:
                    int alNr = albumit.palautaAlbuminId(rivit[i]);
                    if (alNr > 0)
                        kappale.aseta(k, Integer.toString(alNr));
                    else {
                        Albumi al = new Albumi();
                        al.rekisteroi();
                        lisaa(al);
                        virhe += al.aseta(1, rivit[i]);
                        kappale.aseta(k, Integer.toString(al.getAlId()));
                    }
                    break;
                case 4:
                    virhe += kappale.aseta(k, rivit[i]);
                    break;
                case 5:
                    virhe += kappale.aseta(k, rivit[i]);
                    break;
                case 6:
                    virhe += kappale.aseta(k, rivit[i]);
                    break;
                default:
                    return virhe += "Kappaleen luomisessa ongelmia";
                }
            } else
                kappale.aseta(k, "0");
        }
        return virhe;
    }


    /**
     * Tulostaa kappaleen tiedot, jossa artistin ja albumien nimet.
     * @param out tietovirta, johon tulostetaan.
     * @param kappale kappale, jonka tiedot tulostetaan
     */
    public void tulosta(PrintStream out, Kappale kappale) {
        String[] rivit = muokkaaKappaleRiveiksi(kappale);
        out.println("Kappale              :          " + rivit[0]);
        out.println("Artisti                  :          " + rivit[1]);
        out.println("Artisti                  :          " + rivit[2]);
        out.println("Kesto (sek)          :          " + rivit[3]);
        out.println("Julkaistu              :          " + rivit[4]);
        out.println("Rank (1-5)           :          " + rivit[5]);
    }


    /**
     * Tulostaa kappaleen tiedot, jossa artistin ja albumin nimet
     * @param os tietovirta, johon tulostetaan
     * @param kappale ,jonka tiedot tulostetaan
     */
    public void tulosta(OutputStream os, Kappale kappale) {
        tulosta(new PrintStream(os), kappale);
    }

}