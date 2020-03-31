package it.algos.vaadwiki.service;


import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;

/**
 * Created by gac on 28 set 2015.
 * .
 */
public enum ParBio {


    titolo("Titolo", "Titolo", false, false, false, false, false) {
    },// end of single enumeration
    nome("Nome", "nome", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setNome(value.equals("") ? null : libBio.fixNomeValido(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getNome() != null ? bio.getNome() : "";
        }// end of method
    },// end of single enumeration
    cognome("Cognome", "cognome", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setCognome(value.equals("") ? null : libBio.fixCognomeValido(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getCognome() != null ? bio.getCognome() : "";
        }// end of method
    },// end of single enumeration
    cognomePrima("CognomePrima", "CognomePrima", false, false, true, false, false) {
    },// end of single enumeration
    pseudonimo("Pseudonimo", "Pseudonimo", false, false, true, false, false) {
    },// end of single enumeration
    postPseudonimo("PostPseudonimo", "PostPseudonimo", false, false, true, false, false) {
    },// end of single enumeration
    postCognome("PostCognome", "PostCognome", false, false, true, false, false) {
    },// end of single enumeration
    postCognomeVirgola("PostCognomeVirgola", "PostCognomeVirgola", false, false, true, false, false) {
    },// end of single enumeration
    forzaOrdinamento("ForzaOrdinamento", "ForzaOrdinamento", false, false, false, false, false) {
    },// end of single enumeration
    preData("PreData", "PreData", false, false, false, false, false) {
    },// end of single enumeration
    sesso("Sesso", "sesso", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setSesso(value.equals("") ? null : libBio.fixSessoValido(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixSessoValido(valoreGrezzo);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixSessoValido(valoreGrezzo);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getSesso() != null ? bio.getSesso() : "";
        }// end of method
    },// end of single enumeration
    luogoNascita("LuogoNascita", "luogoNato", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoNato(value.equals("") ? null : libBio.fixLuogoValido(value));
        }// end of method


        /**
         * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
         * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param testoOriginario in entrata da elaborare
         *
         * @return testoGrezzo troncato
         */
        public String troncaParteFinale(String testoOriginario) {
            return libBio.troncaParteFinalePuntoInterrogativo(testoOriginario);
        } // fine del metodo


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoNato() != null ? bio.getLuogoNato() : "";
        }// end of method
    },// end of single enumeration
    luogoNascitaLink("LuogoNascitaLink", "luogoNatoLink", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoNatoLink(value.equals("") ? null : libBio.fixLuogoValido(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoNatoLink() != null ? bio.getLuogoNatoLink() : "";
        }// end of method
    },// end of single enumeration
    luogoNascitaAlt("LuogoNascitaAlt", "luogoNatoAlt", false, false, true, false, false) {
    },// end of single enumeration
    giornoMeseNascita("GiornoMeseNascita", "giornoNascita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setGiornoNascita(value.equals("") ? null : libBio.fixGiornoLink(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixGiornoValido(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixGiornoValido(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Giorno giorno = bio.getGiornoNascita();
            return giorno != null ? giorno.titolo : "";
        }// end of method
    },// end of single enumeration
    annoNascita("AnnoNascita", "annoNascita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAnnoNascita(value.equals("") ? null : libBio.fixAnnoLink(value));
        }// end of method


        /**
         * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
         * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param testoOriginario in entrata da elaborare
         *
         * @return testoGrezzo troncato
         */
        public String troncaParteFinale(String testoOriginario) {
            return libBio.troncaParteFinalePuntoInterrogativo(testoOriginario);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixAnnoValido(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixAnnoValido(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Anno anno = bio.getAnnoNascita();
            return anno != null ? anno.titolo : "";
        }// end of method
    },// end of single enumeration
    noteNascita("NoteNascita", "NoteNascita", false, false, true, false, false) {
    },// end of single enumeration
    luogoMorte("LuogoMorte", "luogoMorto", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoMorto(value.equals("") ? null : libBio.fixLuogoValido(value));
        }// end of method


        /**
         * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
         * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param testoOriginario in entrata da elaborare
         *
         * @return testoGrezzo troncato
         */
        public String troncaParteFinale(String testoOriginario) {
            return libBio.troncaParteFinalePuntoInterrogativo(testoOriginario);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return valoreGrezzo;
        }// end of method


        @Override
        public String fix(String value, LibBio libBio) {
            return libBio.fixLuogoValido(value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoMorto() != null ? bio.getLuogoMorto() : "";
        }// end of method
    },// end of single enumeration
    luogoMorteLink("LuogoMorteLink", "luogoMortoLink", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoMortoLink(value.equals("") ? null : libBio.fixLuogoValido(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return valoreGrezzo;
        }// end of method


        @Override
        public String fix(String value, LibBio libBio) {
            return libBio.fixLuogoValido(value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoMortoLink() != null ? bio.getLuogoMortoLink() : "";
        }// end of method
    },// end of single enumeration
    luogoMorteAlt("LuogoMorteAlt", "luogoMortoAlt", false, false, true, false, false) {
    },// end of single enumeration
    giornoMeseMorte("GiornoMeseMorte", "giornoMorte", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setGiornoMorte(value.equals("") ? null : libBio.fixGiornoLink(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixGiornoValido(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixGiornoValido(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Giorno giorno = bio.getGiornoMorte();
            return giorno != null ? giorno.titolo : "";
        }// end of method
    },// end of single enumeration
    annoMorte("AnnoMorte", "AnnoMorte", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAnnoMorte(value.equals("") ? null : libBio.fixAnnoLink(value));
        }// end of method


        /**
         * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
         * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param testoOriginario in entrata da elaborare
         *
         * @return testoGrezzo troncato
         */
        public String troncaParteFinale(String testoOriginario) {
            return libBio.troncaParteFinalePuntoInterrogativo(testoOriginario);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixAnnoValido(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixAnnoValido(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Anno anno = bio.getAnnoMorte();
            return anno != null ? anno.titolo : "";
        }// end of method
    },// end of single enumeration
    noteMorte("NoteMorte", "NoteMorte", false, false, true, false, false) {
    },// end of single enumeration
    epoca("Epoca", "Epoca", false, false, true, false, false) {
    },// end of single enumeration
    epoca2("Epoca2", "Epoca2", false, false, true, false, false) {
    },// end of single enumeration
    preAttivita("PreAttività", "PreAttività", false, false, true, false, false) {
    },// end of single enumeration
    attivita("Attività", "attivita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAttivita(value.equals("") ? null : libBio.fixAttivitaLink(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixAttivitaValida(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixAttivitaValida(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Attivita attivita = bio.getAttivita();
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivita2("Attività2", "attivita2", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAttivita2(value.equals("") ? null : libBio.fixAttivitaLink(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixAttivitaValida(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixAttivitaValida(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Attivita attivita = bio.getAttivita2();
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivita3("Attività3", "attivita3", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAttivita3(value.equals("") ? null : libBio.fixAttivitaLink(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixAttivitaValida(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixAttivitaValida(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Attivita attivita = bio.getAttivita3();
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivitaAltre("AttivitàAltre", "AttivitàAltre", false, false, false, false, false) {
    },// end of single enumeration
    nazionalita("Nazionalità", "nazionalita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setNazionalita(value.equals("") ? null : libBio.fixNazionalitaLink(value));
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValore(String valoreGrezzo) {
            return libBio.fixNazionalitaValida(valoreGrezzo, false);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixParametro(String valoreGrezzo) {
            return libBio.fixNazionalitaValida(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Nazionalita nazionalita = bio.getNazionalita();
            return nazionalita != null ? nazionalita.singolare : "";
        }// end of method
    },// end of single enumeration
    nazionalitaNaturalizzato("NazionalitàNaturalizzato", "NazionalitàNaturalizzato", false, false, true, false, false) {
    },// end of single enumeration
    cittadinanza("Cittadinanza", "Cittadinanza", false, false, true, false, false) {
    },// end of single enumeration
    postNazionalita("PostNazionalità", "PostNazionalità", false, false, true, false, false) {
    },// end of single enumeration
    categorie("Categorie", "Categorie", false, false, true, false, false) {
    },// end of single enumeration
    fineIncipit("FineIncipit", "FineIncipit", false, false, true, false, false) {
    },// end of single enumeration
    punto("Punto", "Punto", false, false, true, false, false) {
    },// end of single enumeration
    immagine("Immagine", "Immagine", false, false, true, false, false) {
    },// end of single enumeration
    didascalia("Didascalia", "Didascalia", false, false, true, false, false) {
    },// end of single enumeration
    didascalia2("Didascalia2", "Didascalia2", false, false, true, false, false) {
    },// end of single enumeration
    dimImmagine("DimImmagine", "DimImmagine", false, false, true, false, false) {
    };// end of single enumeration

    private static String VUOTA = "";

    // @Autowired nella classe statica interna  @Component ParBioServiceInjector
    protected LibBio libBio;

    // @Autowired nella classe statica interna  @Component ParBioServiceInjector
    protected ATextService text;

    private String tag = "";

    private String dbName = "";

    private boolean visibileLista = false;

    private boolean campoNormale = false;

    private boolean campoSignificativo = false;

    private SingularAttribute<Bio, String> attributo;

    private boolean campoValido = false;

    private boolean campoPunta = false;


    ParBio(String tag, String dbName, boolean visibileLista, boolean campoNormale, boolean campoSignificativo, boolean campoValido, boolean campoPunta) {
        this.setTag(tag);
        this.setDbName(dbName);
        this.setVisibileLista(visibileLista);
        this.setCampoNormale(campoNormale);
        this.setCampoSignificativo(campoSignificativo);
        this.setCampoValido(campoValido);
        this.setCampoPunta(campoPunta);
    }// end of general constructor


    public static Attribute<?, ?>[] getCampiLista() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

        for (ParBio par : ParBio.values()) {
            if (par.isVisibileLista()) {
                lista.add(par.getAttributo());
            }// fine del blocco if
        } // fine del ciclo for-each

        matrice = lista.toArray(new Attribute[lista.size()]);
        return matrice;
    }// end of method


    public static Attribute<?, ?>[] getCampiForm() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

        for (ParBio par : ParBio.values()) {
            lista.add(par.getAttributo());
        } // fine del ciclo for-each

        matrice = lista.toArray(new Attribute[lista.size()]);
        return matrice;
    }// end of method


    public static Attribute<?, ?>[] getCampiValida() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoSignificativo()) {
                lista.add(par.getAttributo());
            }// fine del blocco if
        } // fine del ciclo for-each

        matrice = lista.toArray(new Attribute[lista.size()]);
        return matrice;
    }// end of method


    public static ArrayList<ParBio> getCampiSignificativi() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoSignificativo()) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    public static ArrayList<ParBio> getCampiNormali() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoNormale()) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    public static ArrayList<ParBio> getCampiValidi() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoValido()) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    public static ArrayList<ParBio> getCampiPunta() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoPunta()) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore grezzo troncato del parametro
     */
    public String troncaParteFinale(String valoreOriginarioDelServer) {
        return libBio.troncaParteFinale(valoreOriginarioDelServer);
    } // fine del metodo


    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValore(String valoreGrezzo) {
        return libBio.fixValoreGrezzo(valoreGrezzo);
    }// end of method


    /**
     * Elabora un valore GREZZO e restituisce un parametro VALIDO <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixParametro(String valoreGrezzo) {
        return libBio.fixValoreGrezzo(valoreGrezzo);
    }// end of method


    /**
     * Restituisce un valore valido <br>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String estraeValore(String valoreOriginarioDelServer) {
        String valoreGrezzo = troncaParteFinale(valoreOriginarioDelServer);
        return fixValore(valoreGrezzo);
    }// end of method


    /**
     * Restituisce un valore valido del parametro <br>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Se manca la corrispondenza, restituisce VUOTA <br>
     * La differenza con estraeValore() riguarda solo i parametri Giorno, Anno, Attivita, Nazionalita <br>
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String estraeParametro(String valoreOriginarioDelServer) {
        String valoreGrezzo = troncaParteFinale(valoreOriginarioDelServer);
        return fixParametro(valoreGrezzo);
    }// end of method


    /**
     * Restituisce un valore valido del parametro <br>
     * MANTIENE gli eventuali contenuti IN CODA che vengono reinseriti dopo aver elaborato il valore valido del parametro <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     * Usato per Upload sul server
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore finale valido completo del parametro
     */
    public String sostituisceParteValida(String valoreOriginarioDelServer) {
        String valoreFinale = VUOTA;
        String tag = "?";
        String testa = troncaParteFinale(valoreOriginarioDelServer).trim();
        String coda = valoreOriginarioDelServer.substring(testa.length()).trim();
        String valoreValido = fixValore(testa);
        String parametroValido = fixParametro(testa);

        if (text.isValid(parametroValido)) {
            valoreFinale = valoreValido;
            if (valoreValido.length() > 0 && !coda.equals(tag)) {
                valoreFinale += coda;
            }// end of if cycle
        } else {
            if (valoreOriginarioDelServer.equals(tag)) {
                valoreFinale = VUOTA;
            } else {
                valoreFinale = valoreOriginarioDelServer;
            }// end of if/else cycle
        }// end of if/else cycle

        return valoreFinale.trim();
    }// end of method


    /**
     * Elabora un valore valido <br>
     * Non serve la entity Bio <br>
     * Con perdita di informazioni <br>
     * NON deve essere usato per sostituire tout-court il valore del template ma per elaborarlo <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     *
     * @param value valore in ingresso da elaborare
     *
     * @return valore finale valido
     */
    @Deprecated
    public String fix(String value, LibBio libBio) {
        return value;
    }// end of method


    /**
     * Inserisce nell'istanza il valore passato come parametro
     * La property dell'istanza ha lo stesso nome della enumeration
     * DEVE essere sovrascritto (implementato)
     *
     * @param bio   istanza da regolare
     * @param value valore da inserire
     *
     * @return istanza regolata
     */
    public Bio setBio(Bio bio, Object value) {
        return null;
    }// end of method


    /**
     * Inserisce nell'istanza il valore passato come parametro
     * La property dell'istanza ha lo stesso nome della enumeration
     * DEVE essere sovrascritto (implementato)
     *
     * @param bio    istanza da regolare
     * @param value  valore da inserire
     * @param libBio libreria coi metodi di convalida
     */
    public void setValue(Bio bio, String value, LibBio libBio) {
    }// end of method


    /**
     * Recupera il valore del parametro da Originale
     * Inserisce il valore del parametro in Valida
     * La property dell'istanza ha lo stesso nome della enumeration
     * DEVE essere sovrascritto (implementato) per i campi campoSignificativo=true
     *
     * @param originale istanza da cui estrarre (elaborare) il valore del parametro
     * @param valida    istanza in cui inserire il valore del parametro
     */
    public void setBioValida(Bio istanza) {
    }// end of method


    /**
     * Recupera dall'istanza il valore
     * La property dell'istanza ha lo stesso nome della enumeration
     * DEVE essere sovrascritto (implementato)
     *
     * @param bio istanza da elaborare
     *
     * @return value sotto forma di text
     */
    public String getValue(Bio bio) {
        return "";
    }// end of method


    /**
     * Recupera dall'istanza la key ed il valore
     * La property dell'istanza ha lo stesso nome della enumeration
     * DEVE essere sovrascritto (implementato)
     *
     * @param bio istanza da elaborare
     *
     * @return testo della coppia key e value
     */
    public String getKeyValue(Bio bio) {
        String value = getValue(bio);

        if (!value.equals("") || this.isCampoNormale()) {
            return "|" + tag + " = " + value + "\n";
        } else {
            return "";
        }// end of if/else cycle
    }// end of method


    /**
     * Recupera dall'istanza la key ed il valore
     * La property dell'istanza ha lo stesso nome della enumeration
     *
     * @param value del parametro
     *
     * @return testo della coppia key e value
     */
    public String getKeyValue(String value) {
        if (!value.equals("") || this.isCampoNormale()) {
            return "|" + tag + " = " + value + "\n";
        } else {
            return "";
        }// end of if/else cycle
    }// end of method


    /**
     * Recupera il valore del parametro sesso da Originale
     * Inserisce il valore del parametro sesso in Valida
     *
     * @param originale istanza da cui estrarre (elaborare) il valore del parametro
     * @param valida    istanza in cui inserire il valore del parametro
     */
    public void setBioValidaSesso(Bio istanza) {
    }// end of method


    public String getRiga(String value) {
        String riga = VUOTA;
        String ini = "|";

        riga = ini;
        riga += getTag();
        riga += " = ";
        riga += value != null ? value : VUOTA;
        riga += A_CAPO;

        return riga;
    }// end of getter method


    public String getRiga(Bio bio) {
        String riga = VUOTA;

        if (bio != null) {
            riga = getRiga(getValue(bio));
        }// end of if cycle

        return riga;
    }// end of getter method


    public String getTag() {
        return tag;
    }// end of getter method


    public void setTag(String tag) {
        this.tag = tag;
    }//end of setter method


    public String getDbName() {
        return dbName;
    }// end of getter method


    public void setDbName(String dbName) {
        this.dbName = dbName;
    }//end of setter method


    public boolean isVisibileLista() {
        return visibileLista;
    }// end of getter method


    public void setVisibileLista(boolean visibileLista) {
        this.visibileLista = visibileLista;
    }//end of setter method


    public boolean isCampoSignificativo() {
        return campoSignificativo;
    }// end of getter method


    public void setCampoSignificativo(boolean campoSignificativo) {
        this.campoSignificativo = campoSignificativo;
    }//end of setter method


    public SingularAttribute<Bio, String> getAttributo() {
        return attributo;
    }// end of getter method


    public void setAttributo(SingularAttribute<Bio, String> attributo) {
        this.attributo = attributo;
    }//end of setter method


    public boolean isCampoNormale() {
        return campoNormale;
    }// end of getter method


    public void setCampoNormale(boolean campoNormale) {
        this.campoNormale = campoNormale;
    }//end of setter method


    public boolean isCampoValido() {
        return campoValido;
    }// end of getter method


    public void setCampoValido(boolean campoValido) {
        this.campoValido = campoValido;
    }//end of setter method


    public boolean isCampoPunta() {
        return campoPunta;
    }// end of getter method


    public void setCampoPunta(boolean campoPunta) {
        this.campoPunta = campoPunta;
    }//end of setter method


    public void setLibBio(LibBio libBio) {
        this.libBio = libBio;
    }// end of method


    public void setText(ATextService text) {
        this.text = text;
    }// end of method


    @Component
    public static class ParBioServiceInjector {

        @Autowired
        private ATextService text;

        @Autowired
        private LibBio libBio;


        @PostConstruct
        public void postConstruct() {
            for (ParBio parBio : ParBio.values()) {
                parBio.setText(text);
                parBio.setLibBio(libBio);
            }// end of for cycle
        }// end of method

    }// end of internal class

}// end of enumeration
