package it.algos.vaadwiki.liste;

import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.didascalia.EADidascalia;

/**
 * Project vaadwiki
 * <p>
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 08:43
 * <p>
 * Crea le liste dei nati o dei morti nel giorno
 */
public abstract class ListaGiorni extends Lista {


    //--property
    protected Giorno giorno;



    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaGiorni() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadGiornoNato.class, giorno) <br>
     * Usa: appContext.getBean(UploadGiornoMorto.class, giorno) <br>
     *
     * @param giorno di cui costruire la pagina sul server wiki
     */
    public ListaGiorni(Giorno giorno) {
        this.giorno = giorno;
    }// end of constructor


}// end of class
