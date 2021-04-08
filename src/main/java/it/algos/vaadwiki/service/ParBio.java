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
            bio.nome = value.equals("") ? null : libBio.fixNomeValido(value);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
            return libBio.fixMaiuscola(valoreGrezzo);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.nome != null ? bio.nome : "";
        }// end of method
    },// end of single enumeration
    cognome("Cognome", "cognome", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.cognome = value.equals("") ? null : libBio.fixCognomeValido(value);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
            return libBio.fixMaiuscola(valoreGrezzo);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.cognome != null ? bio.cognome : "";
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
            bio.sesso = value.equals("") ? null : libBio.fixSessoValido(value, true);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
            return libBio.fixSessoValido(valoreGrezzo, false);
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
            return libBio.fixSessoValido(valoreGrezzo, true);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.sesso != null ? bio.sesso : "";
        }// end of method
    },// end of single enumeration
    luogoNascita("LuogoNascita", "luogoNato", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.luogoNato = value.equals("") ? null : libBio.fixLuogoValido(value);
        }// end of method


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
        public String estraeValoreInizialeGrezzo(String valorePropertyTmplBioServer) {
            return libBio.estraeValoreInizialeGrezzoPuntoAmmesso(valorePropertyTmplBioServer);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
            return libBio.fixMaiuscola(valoreGrezzo);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.luogoNato != null ? bio.luogoNato : "";
        }// end of method
    },// end of single enumeration
    luogoNascitaLink("LuogoNascitaLink", "luogoNatoLink", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.luogoNatoLink = value.equals("") ? null : libBio.fixLuogoValido(value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.luogoNatoLink != null ? bio.luogoNatoLink : "";
        }// end of method
    },// end of single enumeration
    luogoNascitaAlt("LuogoNascitaAlt", "luogoNatoAlt", false, false, true, false, false) {
    },// end of single enumeration
    giornoMeseNascita("GiornoMeseNascita", "giornoNascita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.giornoNascita = value.equals("") ? null : libBio.fixGiornoLink(value);
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
        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return libBio.troncaParteFinaleGiornoAnno(testoOriginario);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Giorno giorno = bio.giornoNascita;
            return giorno != null ? giorno.titolo : "";
        }// end of method
    },// end of single enumeration
    annoNascita("AnnoNascita", "annoNascita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.annoNascita = value.equals("") ? null : libBio.fixAnnoLink(value);
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
        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return libBio.troncaParteFinaleGiornoAnno(testoOriginario);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Anno anno = bio.annoNascita;
            return anno != null ? anno.titolo : "";
        }// end of method
    },// end of single enumeration
    noteNascita("NoteNascita", "NoteNascita", false, false, true, false, false) {
    },// end of single enumeration
    luogoMorte("LuogoMorte", "luogoMorto", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.luogoMorto = value.equals("") ? null : libBio.fixLuogoValido(value);
        }// end of method


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
        public String estraeValoreInizialeGrezzo(String valorePropertyTmplBioServer) {
            return libBio.estraeValoreInizialeGrezzoPuntoAmmesso(valorePropertyTmplBioServer);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
            return libBio.fixMaiuscola(valoreGrezzo);
        }// end of method


        @Override
        public String fix(String value, LibBio libBio) {
            return libBio.fixLuogoValido(value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.luogoMorto != null ? bio.luogoMorto : "";
        }// end of method
    },// end of single enumeration
    luogoMorteLink("LuogoMorteLink", "luogoMortoLink", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.luogoNatoLink = value.equals("") ? null : libBio.fixLuogoValido(value);
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
            return bio.luogoMortoLink != null ? bio.luogoMortoLink : "";
        }// end of method
    },// end of single enumeration
    luogoMorteAlt("LuogoMorteAlt", "luogoMortoAlt", false, false, true, false, false) {
    },// end of single enumeration
    giornoMeseMorte("GiornoMeseMorte", "giornoMorte", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.giornoMorte = value.equals("") ? null : libBio.fixGiornoLink(value);
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
        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return libBio.troncaParteFinaleGiornoAnno(testoOriginario);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Giorno giorno = bio.giornoMorte;
            return giorno != null ? giorno.titolo : "";
        }// end of method
    },// end of single enumeration
    annoMorte("AnnoMorte", "AnnoMorte", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.annoMorte = value.equals("") ? null : libBio.fixAnnoLink(value);
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
        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return libBio.troncaParteFinaleGiornoAnno(testoOriginario);
        } // fine del metodo


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Anno anno = bio.annoMorte;
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
            bio.attivita = value.equals("") ? null : libBio.fixAttivitaLink(value);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Attivita attivita = bio.attivita;
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivita2("Attività2", "attivita2", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.attivita2 = value.equals("") ? null : libBio.fixAttivitaLink(value);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Attivita attivita = bio.attivita2;
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivita3("Attività3", "attivita3", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.attivita3=value.equals("") ? null : libBio.fixAttivitaLink(value);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Attivita attivita = bio.attivita3;
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivitaAltre("AttivitàAltre", "AttivitàAltre", false, false, false, false, false) {
    },// end of single enumeration
    nazionalita("Nazionalità", "nazionalita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.nazionalita=value.equals("") ? null : libBio.fixNazionalitaLink(value);
        }// end of method


        /**
         * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
         * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
         * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
         *
         * @param valoreGrezzo in entrata da elaborare
         *
         * @return valore finale valido del parametro
         */
        public String fixValoreGrezzo(String valoreGrezzo) {
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
            Nazionalita nazionalita = bio.nazionalita;
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


    public static ParBio getType(String tag) {
        ParBio[] types = values();

        for (ParBio type : values()) {
            if (type.getTag().equals(tag)) {
                return type;
            }// end of if cycle
        }// end of for cycle

        return null;
    }// end of static method


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
    public String estraeValoreInizialeGrezzo(String valorePropertyTmplBioServer) {
        return libBio.estraeValoreInizialeGrezzoPuntoEscluso(valorePropertyTmplBioServer);
    } // fine del metodo


    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValoreGrezzo(String valoreGrezzo) {
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
     * Restituisce un valore valido troncato dopo alcuni tag chiave ed elaborato <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Elabora il valore grezzo (minuscole, quadre, ecc.), che diventa valido e restituibile al server <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore valido troncato ed elaborato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String regolaValoreInizialeValido(String valorePropertyTmplBioServer) {
        String valoreGrezzo = estraeValoreInizialeGrezzo(valorePropertyTmplBioServer);
        return fixValoreGrezzo(valoreGrezzo);
    }// end of method


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
    public String estraeValoreParametro(String valorePropertyTmplBioServer) {
        String valoreGrezzo = estraeValoreInizialeGrezzo(valorePropertyTmplBioServer);
        valoreGrezzo = fixValoreGrezzo(valoreGrezzo);
        return fixParametro(valoreGrezzo);
    }// end of method


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
    public String elaboraParteValida(String valorePropertyTmplBioServer) {
        String valoreFinale = VUOTA;
        String tag = "?";
        String testa = estraeValoreInizialeGrezzo(valorePropertyTmplBioServer).trim();
        String coda = valorePropertyTmplBioServer.trim().substring(testa.length());
        String valoreValido = fixValoreGrezzo(testa);

        if (text.isValid(valoreValido)) {
            valoreFinale = valoreValido;
            if (valoreValido.length() > 0 && !coda.trim().equals(tag)) {
                valoreFinale = valoreFinale + coda;
            }// end of if cycle
        }
        else {
            if (valorePropertyTmplBioServer.equals(tag)) {
                valoreFinale = VUOTA;
            }
            else {
                valoreFinale = valorePropertyTmplBioServer;
            }// end of if/else cycle
        }// end of if/else cycle

        return valoreFinale.trim();
    }// end of method


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
    public String sostituisceParteValida(String valorePropertyTmplBioServer, String valoreMongoDB) {
        String valoreFinale = VUOTA;
        String tag = "?";
        String testa = estraeValoreInizialeGrezzo(valorePropertyTmplBioServer).trim();
        String coda = valorePropertyTmplBioServer.trim().substring(testa.length());
        String valoreValido = fixValoreGrezzo(testa);
        String parametroValido = fixParametro(testa);

        if (text.isValid(valoreMongoDB)) {
            valoreValido = valoreMongoDB;
        }// end of if cycle

        if (text.isValid(parametroValido) || text.isValid(valoreMongoDB)) {
            valoreFinale = valoreValido;
            if (valoreValido.length() > 0 && !coda.trim().equals(tag)) {
                valoreFinale = valoreFinale + coda;
            }// end of if cycle
        }
        else {
            if (valorePropertyTmplBioServer.equals(tag)) {
                valoreFinale = VUOTA;
            }
            else {
                valoreFinale = valorePropertyTmplBioServer;
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
        }
        else {
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
        }
        else {
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
