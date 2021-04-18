package it.algos.vaadwiki.backend.packages.genere;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.annotation.AIScript;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.AEOperation;
import it.algos.vaadflow14.backend.logic.AService;
import it.algos.vaadflow14.backend.enumeration.AETypeReset;
import it.algos.vaadflow14.backend.interfaces.AIResult;
import it.algos.vaadflow14.backend.wrapper.AResult;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.logic.*;
import static it.algos.vaadwiki.backend.logic.WikiLogicList.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: mer, 14-apr-2021 <br>
 * Fix time: 9:14 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, si usa la classe EntityService <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo 'stato' della classe AEntity ma non mantiene lo stato di un'istanza entityBean <br>
 * L' istanza (SINGLETON) viene creata alla partenza del programma <br>
 * <p>
 * Annotated with @Service (obbligatorio) <br>
 * Annotated with @Scope (obbligatorio con SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Qualifier("genereService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class GenereService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public GenereService() {
        super(Genere.class);
        super.prefDownload = AEWikiPreferenza.lastDownloadGenere;
    }


    /**
     * Crea e registra una entityBean <br>
     *
     * @param singolare        (obbligatorio NON unico)
     * @param pluraleMaschile  (facoltativo NON unico)
     * @param pluraleFemminile (facoltativo NON unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Genere crea(final String singolare, final String pluraleMaschile, final String pluraleFemminile) {
        return (Genere) mongo.insert(newEntity(singolare, pluraleMaschile, pluraleFemminile));
    }



    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param singolare        maschile e femminile (obbligatorio ed unico)
     * @param pluraleMaschile  (facoltativo NON unico)
     * @param pluraleFemminile (facoltativo NON unico)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Genere newEntity(final String singolare, final String pluraleMaschile, final String pluraleFemminile) {
        Genere newEntityBean = Genere.builderGenere()
                .singolare(text.isValid(singolare) ? singolare : null)
                .pluraleMaschile(text.isValid(pluraleMaschile) ? pluraleMaschile : null)
                .pluraleFemminile(text.isValid(pluraleFemminile) ? pluraleFemminile : null)
                .build();

        return (Genere) fixKey(newEntityBean);
    }


    /**
     * Regola la chiave se esiste il campo keyPropertyName. <br>
     * Se la company è nulla, la recupera dal login <br>
     * Se la company è ancora nulla, la entity viene creata comunque
     * ma verrà controllata ancora nel metodo beforeSave() <br>
     *
     * @param newEntityBean to be checked
     *
     * @return the checked entity
     */
    @Override
    public AEntity fixKey(AEntity newEntityBean) {
        String tagM = "M";
        String tagF = "F";
        String beginKey = ((Genere) newEntityBean).singolare.toLowerCase();

        if (text.isEmpty(newEntityBean.id)) {
            if (text.isValid(((Genere) newEntityBean).pluraleMaschile)&&text.isValid(((Genere) newEntityBean).pluraleFemminile)) {
                logger.error("Qualcosa non quadra");
            }

            if (text.isValid(((Genere) newEntityBean).pluraleMaschile)) {
                newEntityBean.id = beginKey+tagM;
            }
            if (text.isValid(((Genere) newEntityBean).pluraleFemminile)) {
                newEntityBean.id = beginKey+tagF;
            }
        }

        return newEntityBean;
    }


    /**
     * Fetches all entities of the type <br>
     * <p>
     * Ordinate secondo l'annotation @AIView(sortProperty) della entityClazz <br>
     * Ordinate secondo la property 'ordine', se esiste <br>
     * Ordinate secondo la property 'code', se esiste <br>
     * Ordinate secondo la property 'descrizione', se esiste <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Genere> fetch() {
        return (List<Genere>)super.fetch();
    }



    /**
     * Retrieves an entity by its id.
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Genere findById(final String keyID) {
        return (Genere) super.findById(keyID);
    }


    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param keyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Genere findByKey(final String keyValue) {
        return (Genere) super.findByKey(keyValue);
    }


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        return downloadModulo(PATH_MODULO_GENERE);
    }


    /**
     * Legge la mappa di valori dal modulo di wiki <br>
     * Cancella la (eventuale) precedente lista di genere <br>
     * Elabora la lista di genere <br>
     * Crea le entities e le integra da altro modulo <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean downloadModulo(String wikiTitle) {
        boolean status = false;
        Map<String, String> mappa = wiki.leggeMappaModulo(wikiTitle);
        String singolare = VUOTA;
        String pluraliGrezzi = VUOTA;
        String pluraleMaschile = VUOTA;
        String pluraleFemminile = VUOTA;

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                singolare = entry.getKey();
                pluraliGrezzi = entry.getValue();

                pluraleMaschile = this.estraeMaschile(pluraliGrezzi);
                pluraleFemminile = this.estraeFemminile(pluraliGrezzi);

                if (text.isValid(pluraleMaschile)) {
                    this.crea(singolare, pluraleMaschile, VUOTA);
                }
                if (text.isValid(pluraleFemminile)) {
                    this.crea(singolare, VUOTA, pluraleFemminile);
                }
            }
            status = true;
        }

        super.fixDataDownload();
        return status;
    }


    /**
     * Funziona solo per il format: {"avvocati","M", "avvocate","F"} oppure: {"avvocati","M"}
     */
    public String estraeMaschile(String testoPlurale) {
        String pluraleMaschile = VUOTA;
        String tagM = "M";
        String tagApi = "\"";

        if (testoPlurale.contains(tagM)) {
            pluraleMaschile = text.estrae(testoPlurale, tagApi, tagApi);
        }

        return pluraleMaschile;
    }


    /**
     * Funziona solo per il format: {"avvocati","M", "avvocate","F"} oppure: {"avvocate","F"}
     */
    public String estraeFemminile(String testoPlurale) {
        String pluraleFemminile = "";
        String plurale = "";
        String tagIni = "{";
        String tagEnd = "}";
        String tagVir = ",";
        String tagUgu = "=";
        String tagApi = "";
        String tagM = "M";
        String tagF = "F";
        String[] parti;
        String tag = "F";

        // Funziona solo per il format: { "avvocati","M", "avvocate","F"}
        plurale = text.setNoGraffe(testoPlurale);
        parti = plurale.split(tagVir);
        for (int k = 0; k < parti.length; k++) {
            parti[k] = text.setNoDoppiApici(parti[k]);
        }

        for (int k = 0; k < parti.length; k++) {
            if (parti[k].equals(tag)) {
                if (k > 0) {
                    pluraleFemminile = parti[k - 1];
                }
            }
        }

        return pluraleFemminile;
    }

}// end of singleton class