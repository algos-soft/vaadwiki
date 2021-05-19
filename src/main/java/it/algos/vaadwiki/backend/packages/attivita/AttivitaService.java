package it.algos.vaadwiki.backend.packages.attivita;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.genere.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: mer, 14-apr-2021 <br>
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
@Qualifier("attivitaService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.servicePackage, doc = AEWizDoc.inizioRevisione)
public class AttivitaService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    public static String EX = "ex ";

    public static String EX2 = "ex-";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GenereService genereService;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public AttivitaService() {
        super(Attivita.class);
        super.prefDownload = AEWikiPreferenza.lastDownloadAttivita;
    }

    /**
     * Crea e registra una entityBean <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
     * @param plurale   (facoltativo, non unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Attivita creaOriginale(final String singolare, final String plurale) {
        return (Attivita) mongo.insert(newEntity(singolare, plurale, false));
    }

    /**
     * Crea e registra una entityBean <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
     * @param plurale   (facoltativo, non unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Attivita creaAggiunta(final String singolare, final String plurale) {
        return (Attivita) mongo.insert(newEntity(singolare, plurale, true));
    }

    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
     * @param plurale   (facoltativo, non unico)
     * @param aggiunta  flag (facoltativo, di default false)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Attivita newEntity(final String singolare, final String plurale, final boolean aggiunta) {
        Attivita newEntityBean = Attivita.builderAttivita()
                .singolare(text.isValid(singolare) ? singolare : null)
                .plurale(text.isValid(plurale) ? plurale : null)
                .aggiunta(aggiunta)
                .build();

        return (Attivita) fixKey(newEntityBean);
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
    public Attivita findById(final String keyID) {
        return (Attivita) super.findById(keyID);
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
    public Attivita findByKey(final String keyValue) {
        return (Attivita) super.findByKey(keyValue);
    }


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        return downloadModulo(PATH_MODULO_ATTIVITA);
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
        Map<String, String> mappa = wikiApi.leggeMappaModulo(wikiTitle);

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                this.creaOriginale(entry.getKey(), entry.getValue());
            }
            status = true;
        }
        status = aggiunge();

        super.fixDataDownload();
        return status;
    }

    /**
     * Aggiunge le ex-attività NON presenti nel modulo 'Modulo:Bio/Plurale attività' <br>
     * Le recupera dal modulo 'Modulo:Bio/Plurale attività genere' <br>
     * Le aggiunge se trova la corrispondenza tra il nome con e senza EX <br>
     */
    private boolean aggiunge() {
        boolean status = false;
        List<Genere> listaGenere = genereService.fetch();
        String attivitaSingolare;
        String genereSingolare;
        Attivita entity;

        if (array.isAllValid(listaGenere)) {
            for (Genere genere : listaGenere) {
                entity = null;
                attivitaSingolare = VUOTA;
                genereSingolare = genere.singolare;

                if (genereSingolare.startsWith(EX)) {
                    attivitaSingolare = genereSingolare.substring(EX.length());
                }
                if (genereSingolare.startsWith(EX2)) {
                    attivitaSingolare = genereSingolare.substring(EX2.length());
                }

                if (text.isValid(attivitaSingolare)) {
                    entity = findByKey(attivitaSingolare);
                }

                if (entity != null) {
                    creaAggiunta(genereSingolare, entity.plurale);
                }
            }
        }

        return status;
    }

}// end of singleton class