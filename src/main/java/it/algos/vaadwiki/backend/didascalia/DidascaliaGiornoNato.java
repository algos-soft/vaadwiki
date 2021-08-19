package it.algos.vaadwiki.backend.didascalia;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 19:05
 * <p>
 * Didascalia specializzata per le liste di nati nel giorno. <br>
 * Può essere recuperata da DidascaliaService (Singleton). <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DidascaliaGiornoNato extends Didascalia {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public DidascaliaGiornoNato() {
    }// end of constructor


//    /**
//     * Costruttore con parametri <br>
//     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
//     * Usa: appContext.getBean(DidascaliaGiornoNato.class, bio) <br>
//     *
//     * @param bio di cui costruire la didascalia
//     */
//    public DidascaliaGiornoNato() {
//        super( );
//    }// end of constructor


    /**
     * Preferenze usate da questa 'didascalia' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

//        super.usaBottoneSearch = true;
//        super.maxNumeroBottoniPrimaRiga = 2;
    }

    /**
     * Costruisce la didascalia <br>
     *
     * @param bio di cui costruire la didascalia
     */
    public String esegue(final Bio bio) {
        return VUOTA;
    }

    /**
     * Costruisce il testo della didascalia <br>
     * Sovrascritto <br>
     */
    protected void regolaDidascalia() {
//        testoSenza = VUOTA;
//        testoCon = VUOTA;
//        String mortoTxt;
//
//        // giorno di nascita (deve esserci)
//        if (text.isEmpty(giornoNato)) {
//            return;
//        }// end of if cycle
//
//        // titolo e nome (obbligatori)
//        testoSenza += getNomeCognome();
//
//        // attivitaNazionalita (potrebbe non esserci)
//        testoSenza += getAttNaz();
//
//        // anno di morte (potrebbe non esserci)
//        if (text.isValid(annoMorto)) {
//            testoSenza += SPAZIO;
//            mortoTxt = TAG_MORTO;
//            mortoTxt += SPAZIO;
//            mortoTxt += LibWiki.setQuadre(annoMorto);
//            testoSenza += LibWiki.setTonde(mortoTxt);
//        }// end of if cycle
//
//        //--chiave per il paragrafo
//        //--anno di nascita (potrebbe non esserci)
//        //--inserito all'inizio del testo
//        if (text.isValid(annoNato)) {
//            testoCon += LibWiki.setQuadre(annoNato);
//            testoCon += TAG_SEP;
//            testoCon += testoSenza;
//        }
//        else {
//            testoCon = testoSenza;
//        }// end of if/else cycle

    }

}// end of class
