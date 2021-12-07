package it.algos.vaadwiki.backend.packages.nomeDoppio;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: dom, 18-apr-2021 <br>
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
@Qualifier("prenomeService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.servicePackage, doc = AEWizDoc.inizioRevisione)
public class NomeDoppioService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public NomeDoppioService() {
        super(NomeDoppio.class);
        super.prefDownload = AEWikiPreferenza.lastDownloadPrenome;
    }

    /**
     * Crea e registra una entityBean <br>
     *
     * @param nome di riferimento (obbligatorio, unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public NomeDoppio crea(final String nome) {
        return (NomeDoppio) ((MongoService) mongo).insert(newEntity(nome));
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param nome di riferimento (obbligatorio, unico)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public NomeDoppio newEntity(final String nome) {
        NomeDoppio newEntityBean = NomeDoppio.builderPrenome()
                .nome(text.isValid(nome) ? nome : null)
                .build();

        return (NomeDoppio) fixKey(newEntityBean);
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
    public NomeDoppio findById(final String keyID) throws AlgosException {
        return (NomeDoppio) super.findById(keyID);
    }


    /**
     * Retrieves an entity by a keyProperty.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido SOLO se ne esiste una sola <br>
     *
     * @param propertyName  per costruire la query
     * @param propertyValue must not be {@literal null}
     *
     * @return the founded entity unique or {@literal null} if none found
     */
    @Override
    public NomeDoppio findByProperty(String propertyName, Serializable propertyValue) throws AlgosException {
        return (NomeDoppio) super.findByProperty(propertyName, propertyValue);
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
    public NomeDoppio findByKey(final Serializable keyValue) throws AlgosException {
        return (NomeDoppio) super.findByKey(keyValue);
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
    public List<NomeDoppio> fetch() {
        return (List<NomeDoppio>) super.fetch();
    }

    /**
     * Fetches all code of Prenome <br>
     *
     * @return all selected property
     */
    public List<String> fetchCode() {
        List<String> lista = new ArrayList<>();
        List<NomeDoppio> listaEntities = fetch();

        for (NomeDoppio nomeDoppio : listaEntities) {
            lista.add(nomeDoppio.nome);
        }

        return lista;
    }

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        return downloadModulo(PATH_MODULO_PRENOME);
    }

    /**
     * Legge la mappa di valori dalla pagina wiki <br>
     * Cancella la (eventuale) precedente lista di prenome <br>
     * Elabora la lista di prenome <br>
     * Crea le entities e le integra da altro modulo <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean downloadModulo(String wikiTitle) {
        boolean status = false;
        String tag = A_CAPO + "\\*";
        String[] righe = null;
        String nome;
        String testoPagina = wikiBot.leggeQueryTxt(wikiTitle);

        if (text.isValid(testoPagina)) {
            righe = testoPagina.split(tag);
        }

        if (array.isAllValid(righe)) {
            this.deleteAll();

            //--il primo va eliminato (non pertinente)
            for (int k = 1; k < righe.length; k++) {
                nome = righe[k];

                //--l'ultimo va troncato
                if (k == righe.length - 1) {
                    nome = nome.substring(0, nome.indexOf("\n\n"));
                }

                this.crea(nome);
            }
        }
        else {
            logger.error("downloadModulo - Qualcosa non ha funzionato");
        }

        super.fixDataDownload();
        return status;
    }

}// end of singleton class