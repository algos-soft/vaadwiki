package it.algos.vaadwiki.backend.packages.professione;

import it.algos.vaadflow14.backend.annotation.AIScript;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: gio, 15-apr-2021 <br>
 * Last doc revision: mar, 18-mag-2021 alle 19:35 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, usa la classe EntityService <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo 'stato' della classe AEntity ma non mantiene lo stato di un'istanza entityBean <br>
 * L' istanza (SINGLETON) viene creata alla partenza del programma <br>
 * <p>
 * Annotated with @Service (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per iniettare questo singleton nel costruttore di xxxLogicList <br>
 * Annotated with @Scope (obbligatorio con SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
//Spring
@Service
//Spring
@Qualifier("professioneService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.servicePackage, doc = AEWizDoc.inizioRevisione)
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
        super.prefDownload = AEWikiPreferenza.lastDownloadProfessione;
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
     * Cancella la (eventuale) precedente lista di professione <br>
     * Elabora la lista di professione <br>
     * Crea le entities e le integra da altro modulo <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean downloadModulo(String wikiTitle) {
        boolean status = false;
        Map<String, String> mappa = wikiApi.leggeMappaModulo(wikiTitle);

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                this.creaOriginale(entry.getKey(), entry.getValue());
            }
            status = true;
        }
//        status = aggiunge();

        super.fixDataDownload();
        return status;
    }

}// end of singleton class