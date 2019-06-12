package it.algos.vaadwiki.liste;

import it.algos.vaadflow.modules.anno.Anno;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 10:33
 * <p>
 * Crea le liste dei nati o dei morti nel anno
 */
public abstract class ListaAnni extends Lista {


    //--property
    protected Anno anno;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaAnni() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaAnnoNato.class, anno) <br>
     * Usa: appContext.getBean(ListaAnnoMorto.class, anno) <br>
     *
     * @param anno di cui costruire la pagina sul server wiki
     */
    public ListaAnni(Anno anno) {
        this.anno = anno;
    }// end of constructor


}// end of class
