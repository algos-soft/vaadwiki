package it.algos.vaadwiki.backend.packages.professione;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.annotation.AIScript;
import it.algos.vaadflow14.backend.enumeration.AEOperation;
import it.algos.vaadflow14.backend.logic.AService;
import it.algos.vaadflow14.backend.enumeration.AETypeReset;
import it.algos.vaadflow14.backend.interfaces.AIResult;
import it.algos.vaadflow14.backend.wrapper.AResult;
import it.algos.vaadwiki.backend.logic.*;
import static it.algos.vaadwiki.backend.logic.WikiLogicList.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: gio, 15-apr-2021 <br>
 * Fix time: 6:53 <br>
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
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class ProfessioneService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public ProfessioneService() {
        super(Professione.class);
    }

    /**
     * Crea e registra una entityBean <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
     * @param plurale   (facoltativo, non unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Professione creaOriginale(final String singolare, final String plurale) {
        return (Professione) mongo.insert(newEntity(singolare, plurale,false));
    }

    /**
     * Crea e registra una entityBean <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
     * @param plurale   (facoltativo, non unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Professione creaAggiunta(final String singolare, final String plurale) {
        return (Professione) mongo.insert(newEntity(singolare, plurale,true));
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
	 * @param pagina (facoltativo, non unico)
	 * @param aggiunta flag (facoltativo, di default false)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Professione newEntity(final String singolare, final String pagina, final boolean aggiunta) {
        Professione newEntityBean = Professione.builderProfessione()
                .singolare(text.isValid(singolare) ? singolare : null)
				.pagina(text.isValid(pagina) ? pagina : null)
				.aggiunta(aggiunta)
                .build();

        return (Professione) fixKey(newEntityBean);
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
    public Professione findById(final String keyID) {
        return (Professione) super.findById(keyID);
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
    public Professione findByKey(final String keyValue) {
        return (Professione) super.findByKey(keyValue);
    }


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        return downloadModulo(PATH_MODULO_PROFESSIONE);
    }

    /**
     * Legge la mappa di valori dal modulo di wiki <br>
     * Cancella la (eventuale) precedente lista di attività <br>
     * Elabora la lista di attività <br>
     * Crea le entities e le integra da altro modulo <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean downloadModulo(String wikiTitle) {
        boolean status = false;
        Map<String, String> mappa = wiki.leggeMappaModulo(wikiTitle);

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                this.creaOriginale(entry.getKey(), entry.getValue());
            }
            status = true;
        }
//        status = aggiunge();

        return status;
    }

}// end of singleton class