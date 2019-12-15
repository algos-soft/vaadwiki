package it.algos.vaadwiki.upload;


import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.USA_PARAGRAFI_GIORNI;

/**
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (con frequenza giornaliera ?) <br>
 * Può essere invocato dal bottone 'Upload all' della classe WikiGiornoViewList <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
public abstract class UploadGiorni extends UploadCrono {


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected GiornoService giornoService;

    //--property
    protected Giorno giorno;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadGiorni() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadGiornoNato.class, giorno) <br>
     * Usa: appContext.getBean(UploadGiornoMorto.class, giorno) <br>
     *
     * @param giorno di cui costruire la pagina sul server wiki
     */
    public UploadGiorni(Giorno giorno) {
        this.giorno = giorno;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.usaHeadTocIndice = pref.isBool(USA_PARAGRAFI_GIORNI);
    }// end of method


    /**
     * Titolo della pagina 'madre'
     * <p>
     * Sovrascritto
     */
    @Override
    protected String getTitoloPaginaMadre() {
        String titolo = VUOTA;

        if (giorno != null) {
            titolo += giorno.getTitolo();
        }// fine del blocco if

        return titolo;
    }// fine del metodo



    /**
     * Incapsula il testo come parametro di un (eventuale) template
     * Se non viene incapsulato, restituisce il testo in ingresso
     * Sovrascritto
     *
     * @param testoBody
     */
    @Override
    protected String elaboraTemplate(String testoBody) {
        String testoOut = testoBody;
        String titoloTemplate = "Lista persone per giorno";
        String testoIni = VUOTA;
        String testoEnd = "}}";

        testoIni += "{{" + titoloTemplate;
        testoIni += A_CAPO;
        testoIni += "|titolo=" + titoloPagina;
        testoIni += A_CAPO;
        testoIni += "|voci=" + lista.size;
        testoIni += A_CAPO;
        testoIni += "|testo=";
        testoIni += A_CAPO;

        if (!testoBody.equals(VUOTA)) {
            testoOut = testoIni + testoBody + testoEnd;
        }// fine del blocco if

        return testoOut;
    }// fine del metodo


    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Giorni";
    }// end of method

}// fine della classe
