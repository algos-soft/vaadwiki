package it.algos.vaadwiki.service;


import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;

/**
 * Created by gac on 28 set 2015.
 * .
 */
public enum ParBio {


    titolo("Titolo", true, false, false, false, false) {
    },// end of single enumeration
    nome("Nome", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setNome(value.equals("") ? null : libBio.fixNomeValido(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getNome() != null ? bio.getNome() : "";
        }// end of method
    },// end of single enumeration
    cognome("Cognome", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setCognome(value.equals("") ? null : libBio.fixCognomeValido(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getCognome() != null ? bio.getCognome() : "";
        }// end of method
    },// end of single enumeration
    pseudonimo("Pseudonimo", true, false, true, false, false) {
    },// end of single enumeration
    postPseudonimo("PostPseudonimo", true, false, true, false, false) {
    },// end of single enumeration
    cognomePrima("CognomePrima", true, false, true, false, false) {
    },// end of single enumeration
    postCognome("PostCognome", true, false, true, false, false) {
    },// end of single enumeration
    postCognomeVirgola("PostCognomeVirgola", false, false, true, false, false) {
    },// end of single enumeration
    forzaOrdinamento("ForzaOrdinamento", false, false, false, false, false) {
    },// end of single enumeration
    preData("PreData", false, false, false, false, false) {
    },// end of single enumeration
    sesso("Sesso", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setSesso(value.equals("") ? null : value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getSesso() != null ? bio.getSesso() : "";
        }// end of method
    },// end of single enumeration
    luogoNascita("LuogoNascita", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoNato(value.equals("") ? null : value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoNato() != null ? bio.getLuogoNato() : "";
        }// end of method
    },// end of single enumeration
    luogoNascitaLink("LuogoNascitaLink", true, false, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoNatoLink(value.equals("") ? null : value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoNatoLink() != null ? bio.getLuogoNatoLink() : "";
        }// end of method
    },// end of single enumeration
    luogoNascitaAlt("LuogoNascitaAlt", false, false, true, false, false) {
    },// end of single enumeration
    giornoMeseNascita("GiornoMeseNascita", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setGiornoNascita(value.equals("") ? null : libBio.fixGiornoLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Giorno giorno = bio.getGiornoNascita();
            return giorno != null ? giorno.titolo : "";
        }// end of method
    },// end of single enumeration
    annoNascita("AnnoNascita", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAnnoNascita(value.equals("") ? null : libBio.fixAnnoLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Anno anno = bio.getAnnoNascita();
            return anno != null ? anno.titolo : "";
        }// end of method
    },// end of single enumeration
    noteNascita("NoteNascita", true, false, true, false, false) {
    },// end of single enumeration
    luogoMorte("LuogoMorte", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoMorto(value.equals("") ? null : value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoMorto() != null ? bio.getLuogoMorto() : "";
        }// end of method
    },// end of single enumeration
    luogoMorteLink("LuogoMorteLink", true, false, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setLuogoMortoLink(value.equals("") ? null : value);
        }// end of method


        @Override
        public String getValue(Bio bio) {
            return bio.getLuogoMortoLink() != null ? bio.getLuogoMortoLink() : "";
        }// end of method
    },// end of single enumeration
    luogoMorteAlt("LuogoMorteAlt", false, false, true, false, false) {
    },// end of single enumeration
    giornoMeseMorte("GiornoMeseMorte", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setGiornoMorte(value.equals("") ? null : libBio.fixGiornoLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Giorno giorno = bio.getGiornoMorte();
            return giorno != null ? giorno.titolo : "";
        }// end of method
    },// end of single enumeration
    annoMorte("AnnoMorte", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAnnoMorte(value.equals("") ? null : libBio.fixAnnoLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Anno anno = bio.getAnnoMorte();
            return anno != null ? anno.titolo : "";
        }// end of method
    },// end of single enumeration
    noteMorte("NoteMorte", true, false, true, false, false) {
    },// end of single enumeration
    preAttivita("PreAttività", true, false, true, false, false) {
    },// end of single enumeration
    attivita("Attività", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAttivita(value.equals("") ? null : libBio.fixAttivitaLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Attivita attivita = bio.getAttivita();
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivita2("Attività2", false, false, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAttivita2(value.equals("") ? null : libBio.fixAttivitaLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Attivita attivita = bio.getAttivita2();
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivita3("Attività3", false, false, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setAttivita3(value.equals("") ? null : libBio.fixAttivitaLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Attivita attivita = bio.getAttivita3();
            return attivita != null ? attivita.singolare : "";
        }// end of method
    },// end of single enumeration
    attivitaAltre("AttivitàAltre", false, false, false, false, false) {
    },// end of single enumeration
    epoca("Epoca", true, false, true, false, false) {
    },// end of single enumeration
    epoca2("Epoca2", true, false, true, false, false) {
    },// end of single enumeration
    nazionalita("Nazionalità", true, true, true, false, false) {
        @Override
        public void setValue(Bio bio, String value, LibBio libBio) {
            bio.setNazionalita(value.equals("") ? null : libBio.fixNazionalitaLink(value));
        }// end of method


        @Override
        public String getValue(Bio bio) {
            Nazionalita nazionalita = bio.getNazionalita();
            return nazionalita != null ? nazionalita.singolare : "";
        }// end of method
    },// end of single enumeration
    nazionalitaNaturalizzato("NazionalitàNaturalizzato", true, false, true, false, false) {
    },// end of single enumeration
    cittadinanza("Cittadinanza", true, false, true, false, false) {
    },// end of single enumeration
    categorie("Categorie", true, false, true, false, false) {
    },// end of single enumeration
    postNazionalita("PostNazionalità", true, false, true, false, false) {
    },// end of single enumeration
    punto("Punto", true, false, true, false, false) {
    },// end of single enumeration
    fineIncipit("FineIncipit", true, false, true, false, false) {
    },// end of single enumeration
    immagine("Immagine", true, false, true, false, false) {
    },// end of single enumeration
    dimImmagine("DimImmagine", true, false, true, false, false) {
    },// end of single enumeration
    didascalia("Didascalia", true, false, true, false, false) {
    },// end of single enumeration
    didascalia2("Didascalia2", true, false, true, false, false) {
    };// end of single enumeration

    private static String VUOTA = "";
//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    private LibBio libBio;

    private String tag = "";

    private boolean visibileLista = false;

    private boolean campoNormale = false;

    private boolean campoSignificativo = false;

    private SingularAttribute<Bio, String> attributo;

    private boolean campoValido = false;

    private boolean campoPunta = false;


    ParBio(String tag, boolean visibileLista, boolean campoNormale, boolean campoSignificativo, boolean campoValido, boolean campoPunta) {
        this.setTag(tag);
        this.setVisibileLista(visibileLista);
        this.setCampoNormale(campoNormale);
        this.setCampoSignificativo(campoSignificativo);
        this.setCampoValido(campoValido);
        this.setCampoPunta(campoPunta);
    }// end of general constructor


    public static Attribute<?, ?>[] getCampiLista() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

//        lista.add(Bio_.pageid);
//        lista.add(Bio_.title);
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

//        lista.add(Bio_.pageid);
//        lista.add(Bio_.title);
        for (ParBio par : ParBio.values()) {
            lista.add(par.getAttributo());
        } // fine del ciclo for-each

        matrice = lista.toArray(new Attribute[lista.size()]);
        return matrice;
    }// end of method


    public static Attribute<?, ?>[] getCampiValida() {
        Attribute<?, ?>[] matrice;
        ArrayList<Attribute> lista = new ArrayList<Attribute>();

//        lista.add(Bio_.pageid);
//        lista.add(Bio_.title);
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


//    /**
//     * Inserisce nell'istanza il valore passato come parametro
//     * La property dell'istanza ha lo stesso nome della enumeration
//     * DEVE essere sovrascritto (implementato)
//     *
//     * @param bio   istanza da regolare
//     * @param value valore da inserire
//     *
//     * @return istanza regolata
//     */
//    public void setValue(Bio bio, String value) {
//    }// end of method


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
    }


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


    public String getTag() {
        return tag;
    }// end of getter method


    public void setTag(String tag) {
        this.tag = tag;
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
}
