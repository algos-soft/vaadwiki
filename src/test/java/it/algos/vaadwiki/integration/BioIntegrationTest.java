package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 02-apr-2020
 * Time: 18:50
 * Classe di test per raggruppare tutti i test del template Bio
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BioIntegrationTest extends ATest {


    @Autowired
    protected LibBio libBio;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(libBio);
    }// end of method


    /**
     * Nell'ordine:
     * 0) type del parametro (enumeration) <br>
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<Object[]> getAll() {
        List<Object[]> lista = new ArrayList<>();

        Object[] listaParametroNome = {ParBio.nome, getListaParametroNome()};
        lista.add(listaParametroNome);

        Object[] listaParametroCognome = {ParBio.cognome, getListaParametroCognome()};
        lista.add(listaParametroCognome);

        Object[] listaParametroSesso = {ParBio.sesso, getListaParametroSesso()};
        lista.add(listaParametroSesso);

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroNome() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[Mario]]", "[[Mario]]", "Mario", "Mario", "Mario", "", "Mario"});
        lista.add(new String[]{"[[Mario]]{{forse}}", "[[Mario]]", "Mario", "Mario", "Mario{{forse}}", "", "Mario{{forse}}"});
        lista.add(new String[]{"[[Mario]]<ref>Pippoz</ref>", "[[Mario]]", "Mario", "Mario", "Mario<ref>Pippoz</ref>", "", "Mario<ref>Pippoz</ref>"});
        lista.add(new String[]{"Mario{{template:pippoz}}", "Mario", "Mario", "Mario", "Mario{{template:pippoz}}", "", "Mario{{template:pippoz}}"});
        lista.add(new String[]{"Crystle Danae?", "Crystle Danae", "Crystle Danae", "Crystle Danae", "Crystle Danae", "Pluto Paperino", "Pluto Paperino"});
        lista.add(new String[]{"[[Giovanna]]<ref>Pippoz</ref>", "[[Giovanna]]", "Giovanna", "Giovanna", "Giovanna<ref>Pippoz</ref>", "Maria", "Maria<ref>Pippoz</ref>"});
        lista.add(new String[]{"Mario?", "Mario", "Mario", "Mario", "Mario", "", "Mario"});
        lista.add(new String[]{"?", "", "", "", "", "", ""});
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroCognome() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[Rossi]]", "[[Rossi]]", "Rossi", "Rossi", "Rossi", "", "Rossi"});
        lista.add(new String[]{"[[Rossi]]{{forse}}", "[[Rossi]]", "Rossi", "Rossi", "Rossi{{forse}}", "", "Rossi{{forse}}"});
        lista.add(new String[]{"[[Rossi]]<ref>Pippoz</ref>", "[[Rossi]]", "Rossi", "Rossi", "Rossi<ref>Pippoz</ref>", "", "Rossi<ref>Pippoz</ref>"});
        lista.add(new String[]{"Rossi{{template:pippoz}}", "Rossi", "Rossi", "Rossi", "Rossi{{template:pippoz}}", "", "Rossi{{template:pippoz}}"});
        lista.add(new String[]{"Rovagnati?", "Rovagnati", "Rovagnati", "Rovagnati", "Rovagnati", "Fisichella", "Fisichella"});
        lista.add(new String[]{"[[Beretta]]<ref>Pippoz</ref>", "[[Beretta]]", "Beretta", "Beretta", "Beretta<ref>Pippoz</ref>", "Mancuso", "Mancuso<ref>Pippoz</ref>"});
        lista.add(new String[]{"Rossi?", "Rossi", "Rossi", "Rossi", "Rossi", "", "Rossi"});
        lista.add(new String[]{"?", "", "", "", "", "", ""});
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroSesso() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"m", "m", "M", "M", "M", "", "M"});
        lista.add(new String[]{"m?", "m", "M", "M", "M", "", "M"});
        lista.add(new String[]{"M?", "M", "M", "M", "M", "", "M"});
        lista.add(new String[]{"maschio", "maschio", "M", "M", "M", "", "M"});
        lista.add(new String[]{"Maschio", "Maschio", "M", "M", "M", "", "M"});
        lista.add(new String[]{"uomo", "uomo", "M", "M", "M", "", "M"});
        lista.add(new String[]{"Uomo", "Uomo", "M", "M", "M", "", "M"});
        lista.add(new String[]{"maschio<ref>forse<ref>", "maschio", "M", "M", "M<ref>forse<ref>", "", "M<ref>forse<ref>"});
        lista.add(new String[]{"Maschio<ref>forse<ref>", "Maschio", "M", "M", "M<ref>forse<ref>", "", "M<ref>forse<ref>"});
        lista.add(new String[]{"f", "f", "F", "F", "F", "", "F"});
        lista.add(new String[]{"f?", "f", "F", "F", "F", "", "F"});
        lista.add(new String[]{"F?", "F", "F", "F", "F", "", "F"});
        lista.add(new String[]{"femmina", "femmina", "F", "F", "F", "", "F"});
        lista.add(new String[]{"Femmina", "Femmina", "F", "F", "F", "", "F"});
        lista.add(new String[]{"donna", "donna", "F", "F", "F", "", "F"});
        lista.add(new String[]{"Donna", "Donna", "F", "F", "F", "", "F"});
//        lista.add(new String[]{"trans", "trans", "", "", "", "", ""});
//        lista.add(new String[]{"incerto", "incerto", "", "", "", "", ""});
//        lista.add(new String[]{"dubbio", "dubbio", "", "", "", "", ""});
//        lista.add(new String[]{"non si sa", "non si sa", "", "", "", "", ""});
//        lista.add(new String[]{"?", "", "", "", "", "", ""});
//        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    @Test
    public void estraeValoreInizialeGrezzo() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("estraeValoreInizialeGrezzo");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[1];
                        ottenuto = parBio.estraeValoreInizialeGrezzo(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore troncato: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore valido troncato dopo alcuni tag chiave ed elaborato <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Elabora il valore grezzo (minuscole, quadre, ecc.)
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore valido troncato ed elaborato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    @Test
    public void estraeValore() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("estraeValoreInizialeValido");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[2];
                        ottenuto = parBio.estraeValoreInizialeValido(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore valido: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore valido del parametro <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Elabora il valore grezzo (minuscole, quadre, ecc.)
     * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Se manca la corrispondenza, restituisce VUOTA <br>
     * La differenza con estraeValoreInizialeValido() riguarda solo i parametri Giorno, Anno, Attivita, Nazionalita <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore valido del parametro
     */
    @Test
    public void estraeParametro() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("estraeValoreParametro");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[3];
                        ottenuto = parBio.estraeValoreParametro(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Parametro valido: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore finale per upload del valore valido elaborato e con la 'coda' <br>
     * <p>
     * MANTIENE gli eventuali contenuti IN CODA che vengono reinseriti dopo aver elaborato il valore valido del parametro <br>
     * Usato per Upload sul server
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore finale completo col valore valido elaborato e la 'coda' dalla property di tmplBioServer
     */
    @Test
    public void elaboraParteValida() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("elaboraParteValida");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[4];
                        ottenuto = parBio.elaboraParteValida(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore elaborato: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore finale per upload merged del parametro mongoDB e con la 'coda' <br>
     * <p>
     * Elabora un valore valido del parametro, utilizzando quello del mongoDB <br>
     * MANTIENE gli eventuali contenuti IN CODA che vengono reinseriti dopo aver elaborato il valore valido del parametro <br>
     * Usato per Upload sul server
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     * @param valoreMongoDB               da sostituire al posto del valore valido dalla property di tmplBioServer
     *
     * @return valore finale completo col parametro mongoDB e la 'coda' dalla property di tmplBioServer
     */
    @Test
    public void sostituisceParteValida() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        String valoreMongoDB = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato (minuscole, quadre, ecc.), non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
        System.out.println("");
        System.out.println("******************");
        System.out.println("sostituisceParteValida");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        valoreMongoDB = valori[5];
                        previsto = valori[6];
                        ottenuto = parBio.sostituisceParteValida(testoOriginale,valoreMongoDB);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore del mongoDB: " + valoreMongoDB+ " Valore per upload: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


}// end of test class
