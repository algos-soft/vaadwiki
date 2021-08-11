package it.algos.vaadwiki.backend.service;


import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import javax.persistence.metamodel.*;
import java.util.*;

/**
 * Created by gac on 28 set 2015.
 * .
 */
public enum ParBio {


    titolo("Titolo", "titolo", false, false, false, false, false) {
    },// end of single enumeration

    //    nome("Nome", "nome", true, true, true, true, false) {
    //        @Override
    //        public void setValue(final Bio bio, final String value) {
    //            bio.nome = text.isValid(value) ? elabora.fixNomeValido(value) : null;
    //        }
    //
    //        @Override
    //        public String fixValoreGrezzo(final String valoreGrezzo) {
    //            return elabora.fixMinuscola(valoreGrezzo);
    //        }
    //
    //        @Override
    //        public String getValue(final Bio bio) {
    //            return bio != null && bio.nome != null ? bio.nome : VUOTA;
    //        }
    //    },// end of single enumeration

    nome("Nome", "nome", true, true, true, true, false) {
        @Override
        public void setValue(final Bio bio, final String value) {
            bio.nome = text.isValid(value) ? elabora.fixNomeValido(value) : null;
        }

        @Override
        public String getValue(final Bio bio) {
            return bio != null ? bio.nome : VUOTA;
        }
    },// end of single enumeration

    cognome("Cognome", "cognome", true, true, true, true, false) {
        @Override
        public void setValue(final Bio bio, final String value) {
            bio.cognome = text.isValid(value) ? elabora.fixCognomeValido(value) : null;
        }

        @Override
        public String fixValoreGrezzo(final String valoreGrezzo) {
            return elabora.fixMinuscola(valoreGrezzo);
        }

        @Override
        public String getValue(final Bio bio) {
            return bio != null ? bio.cognome : VUOTA;
        }
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
        //        @Override
        //        public void setValue(Bio bio, String value, LibBio libBio) {
        //            bio.sesso = value.equals("") ? null : libBio.fixSessoValido(value, true);
        //        }

        //        public String fixValoreGrezzo(String valoreGrezzo) {
        //            return libBio.fixSessoValido(valoreGrezzo, false);
        //        }

        //        public String fixParametro(String valoreGrezzo) {
        //            return libBio.fixSessoValido(valoreGrezzo, true);
        //        }

        //        @Override
        //        public String getValue(Bio bio) {
        //            return bio.sesso != null ? bio.sesso : "";
        //        }
    },// end of single enumeration

    luogoNascita("LuogoNascita", "luogoNato", true, true, true, true, false) {
                @Override
                public void setValue(Bio bio, String value) {
                    bio.luogoNato = value.equals(VUOTA) ? null : elabora.fixLuogoValido(value);
                }

//                public String estraeValoreInizialeGrezzo(String valorePropertyTmplBioServer) {
//                    return elabora.estraeValoreInizialeGrezzoPuntoAmmesso(valorePropertyTmplBioServer);
//                }
//
//                public String fixValoreGrezzo(String valoreGrezzo) {
//                    return elabora.fixMaiuscola(valoreGrezzo);
//                }

                @Override
                public String getValue(Bio bio) {
                    return bio != null ? bio.luogoNato : VUOTA;
                }
    },// end of single enumeration

    luogoNascitaLink("LuogoNascitaLink", "luogoNatoLink", true, false, true, true, false) {
        //        @Override
        //        public void setValue(Bio bio, String value, LibBio libBio) {
        //            bio.luogoNatoLink = value.equals("") ? null : libBio.fixLuogoValido(value);
        //        }

        //        @Override
        //        public String getValue(Bio bio) {
        //            return bio.luogoNatoLink != null ? bio.luogoNatoLink : "";
        //        }
    },// end of single enumeration

    luogoNascitaAlt("LuogoNascitaAlt", "luogoNatoAlt", false, false, true, false, false) {
    },// end of single enumeration

    giornoMeseNascita("GiornoMeseNascita", "giornoNato", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.giornoNato = value.equals(VUOTA) ? null : elabora.fixGiornoValido(value);
        }

        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return elabora.troncaParteFinaleGiornoAnno(testoOriginario);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixGiornoValido(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixGiornoValido(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.giornoNato : VUOTA;
        }
    },// end of single enumeration

    annoNascita("AnnoNascita", "annoNato", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.annoNato = value.equals(VUOTA) ? null : elabora.fixAnnoValido(value);
        }

        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return elabora.troncaParteFinaleGiornoAnno(testoOriginario);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixAnnoValido(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixAnnoValido(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.annoNato : VUOTA;
        }
    },// end of single enumeration

    noteNascita("NoteNascita", "NoteNascita", false, false, true, false, false) {
    },// end of single enumeration

    luogoMorte("LuogoMorte", "luogoMorto", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.luogoMorto = value.equals(VUOTA) ? null : elabora.fixLuogoValido(value);
        }

        //        public String estraeValoreInizialeGrezzo(String valorePropertyTmplBioServer) {
        //            return libBio.estraeValoreInizialeGrezzoPuntoAmmesso(valorePropertyTmplBioServer);
        //        }

        //        public String fixValoreGrezzo(String valoreGrezzo) {
        //            return libBio.fixMaiuscola(valoreGrezzo);
        //        }

        //        @Override
        //        public String fix(String value, LibBio libBio) {
        //            return libBio.fixLuogoValido(value);
        //        }

                @Override
                public String getValue(Bio bio) {
                    return bio != null ? bio.luogoMorto : VUOTA;
                }
    },// end of single enumeration

    luogoMorteLink("LuogoMorteLink", "luogoMortoLink", true, false, true, true, false) {
        //                @Override
        //                public void setValue(Bio bio, String value, LibBio libBio) {
        //                    bio.luogoNatoLink = value.equals("") ? null : libBio.fixLuogoValido(value);
        //                }
        //
        //                public String fixValore(String valoreGrezzo) {
        //                    return valoreGrezzo;
        //                }
        //
        //                @Override
        //                public String fix(String value, LibBio libBio) {
        //                    return libBio.fixLuogoValido(value);
        //                }
        //
        //                @Override
        //                public String getValue(Bio bio) {
        //                    return bio.luogoMortoLink != null ? bio.luogoMortoLink : "";
        //                }
    },// end of single enumeration

    luogoMorteAlt("LuogoMorteAlt", "luogoMortoAlt", false, false, true, false, false) {
    },// end of single enumeration

    giornoMeseMorte("GiornoMeseMorte", "giornoMorte", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.giornoMorto = value.equals(VUOTA) ? null : elabora.fixGiornoValido(value);
        }

        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return elabora.troncaParteFinaleGiornoAnno(testoOriginario);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixGiornoValido(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixGiornoValido(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.giornoMorto : VUOTA;
        }
    },// end of single enumeration

    annoMorte("AnnoMorte", "AnnoMorto", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.annoMorto = value.equals(VUOTA) ? null : elabora.fixAnnoValido(value);
        }

        public String estraeValoreInizialeGrezzo(String testoOriginario) {
            return elabora.troncaParteFinaleGiornoAnno(testoOriginario);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixAnnoValido(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixAnnoValido(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.annoMorto : VUOTA;
        }
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
        public void setValue(Bio bio, String value) {
            bio.attivita = value.equals(VUOTA) ? null : elabora.fixAttivitaValida(value);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixAttivitaValida(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixAttivitaValida(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.attivita : VUOTA;
        }
    },// end of single enumeration

    attivita2("Attività2", "attivita2", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.attivita2 = value.equals(VUOTA) ? null : elabora.fixAttivitaValida(value);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixAttivitaValida(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixAttivitaValida(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.attivita2 : VUOTA;
        }
    },// end of single enumeration

    attivita3("Attività3", "attivita3", true, false, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.attivita3 = value.equals(VUOTA) ? null : elabora.fixAttivitaValida(value);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixAttivitaValida(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixAttivitaValida(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.attivita3 : VUOTA;
        }
    },// end of single enumeration

    attivitaAltre("AttivitàAltre", "AttivitàAltre", false, false, false, false, false) {
    },// end of single enumeration

    nazionalita("Nazionalità", "nazionalita", true, true, true, true, false) {
        @Override
        public void setValue(Bio bio, String value) {
            bio.nazionalita = value.equals("") ? null : elabora.fixNazionalitaValida(value);
        }

        public String fixValoreGrezzo(String valoreGrezzo) {
            return elabora.fixNazionalitaValida(valoreGrezzo);
        }

        public String fixParametro(String valoreGrezzo) {
            return elabora.fixNazionalitaValida(valoreGrezzo);
        }

        @Override
        public String getValue(Bio bio) {
            return bio != null ? bio.nazionalita : VUOTA;
        }
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

    //     @Autowired nella classe statica interna  @Component ParBioServiceInjector
    //    protected LibBio libBio;

    // @Autowired nella classe statica interna  @Component ParInjector
    public AWikiBotService wikiBot;

    // @Autowired nella classe statica interna  @Component ParInjector
    protected TextService text;

    // @Autowired nella classe statica interna  @Component ParInjector
    protected ElaboraService elabora;

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
            }
        }

        return null;
    }// end of static method


    public static Attribute<?, ?>[] getCampiLista() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

        for (ParBio par : ParBio.values()) {
            if (par.isVisibileLista()) {
                lista.add(par.getAttributo());
            }
        }

        matrice = lista.toArray(new Attribute[lista.size()]);
        return matrice;
    }// end of method


    public static Attribute<?, ?>[] getCampiForm() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

        for (ParBio par : ParBio.values()) {
            lista.add(par.getAttributo());
        }

        matrice = lista.toArray(new Attribute[lista.size()]);
        return matrice;
    }// end of method


    public static Attribute<?, ?>[] getCampiValida() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoSignificativo()) {
                lista.add(par.getAttributo());
            }
        }

        matrice = lista.toArray(new Attribute[lista.size()]);
        return matrice;
    }// end of method


    public static ArrayList<ParBio> getCampiSignificativi() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoSignificativo()) {
                lista.add(par);
            }
        }

        return lista;
    }// end of method


    public static ArrayList<ParBio> getCampiNormali() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoNormale()) {
                lista.add(par);
            }
        }

        return lista;
    }// end of method


    public static ArrayList<ParBio> getCampiValidi() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoValido()) {
                lista.add(par);
            }
        }

        return lista;
    }// end of method


    public static ArrayList<ParBio> getCampiPunta() {
        ArrayList<ParBio> lista = new ArrayList<ParBio>();

        for (ParBio par : ParBio.values()) {
            if (par.isCampoPunta()) {
                lista.add(par);
            }
        }

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
        return wikiBot.estraeValoreInizialeGrezzoPuntoEscluso(valorePropertyTmplBioServer);
    }

    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValoreGrezzo(final String valoreGrezzo) {
        return wikiBot.fixValoreGrezzo(valoreGrezzo);
    }

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
    //    public String fixParametro(String valoreGrezzo) {
    //        return libBio.fixValoreGrezzo(valoreGrezzo);
    //    }

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
    }

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
    //    public String estraeValoreParametro(String valorePropertyTmplBioServer) {
    //        String valoreGrezzo = estraeValoreInizialeGrezzo(valorePropertyTmplBioServer);
    //        valoreGrezzo = fixValoreGrezzo(valoreGrezzo);
    //        return fixParametro(valoreGrezzo);
    //    }

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
    //    public String elaboraParteValida(String valorePropertyTmplBioServer) {
    //        String valoreFinale = VUOTA;
    //        String tag = "?";
    //        String testa = estraeValoreInizialeGrezzo(valorePropertyTmplBioServer).trim();
    //        String coda = valorePropertyTmplBioServer.trim().substring(testa.length());
    //        String valoreValido = fixValoreGrezzo(testa);
    //
    //        if (text.isValid(valoreValido)) {
    //            valoreFinale = valoreValido;
    //            if (valoreValido.length() > 0 && !coda.trim().equals(tag)) {
    //                valoreFinale = valoreFinale + coda;
    //            }
    //        }
    //        else {
    //            if (valorePropertyTmplBioServer.equals(tag)) {
    //                valoreFinale = VUOTA;
    //            }
    //            else {
    //                valoreFinale = valorePropertyTmplBioServer;
    //            }
    //        }
    //
    //        return valoreFinale.trim();
    //    }

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
    //    public String sostituisceParteValida(String valorePropertyTmplBioServer, String valoreMongoDB) {
    //        String valoreFinale = VUOTA;
    //        String tag = "?";
    //        String testa = estraeValoreInizialeGrezzo(valorePropertyTmplBioServer).trim();
    //        String coda = valorePropertyTmplBioServer.trim().substring(testa.length());
    //        String valoreValido = fixValoreGrezzo(testa);
    //        String parametroValido = fixParametro(testa);
    //
    //        if (text.isValid(valoreMongoDB)) {
    //            valoreValido = valoreMongoDB;
    //        }// end of if cycle
    //
    //        if (text.isValid(parametroValido) || text.isValid(valoreMongoDB)) {
    //            valoreFinale = valoreValido;
    //            if (valoreValido.length() > 0 && !coda.trim().equals(tag)) {
    //                valoreFinale = valoreFinale + coda;
    //            }// end of if cycle
    //        }
    //        else {
    //            if (valorePropertyTmplBioServer.equals(tag)) {
    //                valoreFinale = VUOTA;
    //            }
    //            else {
    //                valoreFinale = valorePropertyTmplBioServer;
    //            }// end of if/else cycle
    //        }// end of if/else cycle
    //
    //        return valoreFinale.trim();
    //    }// end of method

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
    //    public String fix(String value, LibBio libBio) {
    //        return value;
    //    }// end of method

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
     * Inserisce nell'istanza il valore passato come parametro <br>
     * La property dell'istanza ha lo stesso nome della enumeration <br>
     * DEVE essere sovrascritto (implementato) nella singola enumeration <br>
     *
     * @param bio   istanza da regolare
     * @param value valore da inserire
     */
    public void setValue(final Bio bio, final String value) {
    }

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
    public String getValue(final Bio bio) {
        return VUOTA;
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
        }

        return riga;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isVisibileLista() {
        return visibileLista;
    }

    public void setVisibileLista(boolean visibileLista) {
        this.visibileLista = visibileLista;
    }

    public boolean isCampoSignificativo() {
        return campoSignificativo;
    }

    public void setCampoSignificativo(boolean campoSignificativo) {
        this.campoSignificativo = campoSignificativo;
    }

    public SingularAttribute<Bio, String> getAttributo() {
        return attributo;
    }

    public void setAttributo(SingularAttribute<Bio, String> attributo) {
        this.attributo = attributo;
    }

    public boolean isCampoNormale() {
        return campoNormale;
    }

    public void setCampoNormale(boolean campoNormale) {
        this.campoNormale = campoNormale;
    }

    public boolean isCampoValido() {
        return campoValido;
    }

    public void setCampoValido(boolean campoValido) {
        this.campoValido = campoValido;
    }

    public boolean isCampoPunta() {
        return campoPunta;
    }

    public void setCampoPunta(boolean campoPunta) {
        this.campoPunta = campoPunta;
    }

    public void setWikiBot(AWikiBotService wikiBot) {
        this.wikiBot = wikiBot;
    }

    public void setText(TextService text) {
        this.text = text;
    }

    public void setElabora(ElaboraService elabora) {
        this.elabora = elabora;
    }

    @Component
    public static class ParInjector {

        @Autowired
        private TextService text;

        @Autowired
        private AWikiBotService wikiBot;

        @Autowired
        private ElaboraService elabora;

        @PostConstruct
        public void postConstruct() {
            for (ParBio parBio : ParBio.values()) {
                parBio.setText(text);
                parBio.setWikiBot(wikiBot);
                parBio.setElabora(elabora);
            }
        }

    }

}// end of enumeration
