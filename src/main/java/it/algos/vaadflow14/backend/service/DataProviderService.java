package it.algos.vaadflow14.backend.service;

import com.vaadin.flow.data.provider.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 04-ott-2020
 * Time: 16:33
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ADataProviderService.class); <br>
 * 3) @Autowired public ADataProviderService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataProviderService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    @Autowired
    private AIMongoService mongo;


    public DataProvider<AEntity, Void> creaDataProvider(Class entityClazz) {
        DataProvider dataProvider = DataProvider.fromCallbacks(

                // First callback fetches items based on a query
                fetchCallback -> {
                    // Esistono DUE tipi di Sort: quello di Spring e quello di Vaadin
                    Sort sortSpring;

                    // The index of the first item to load
                    int offset = fetchCallback.getOffset();

                    // The number of items to load
                    int limit = fetchCallback.getLimit();

                    // Ordine delle colonne
                    // Vaadin mi manda sempre UNA sola colonna. Perché?
                    List<QuerySortOrder> sortVaadinList = fetchCallback.getSortOrders();

                    // Alla partenza (se l'ordinamento manca) usa l'ordine base della AEntity
                    // le volte successive usa l'ordine selezionato da un header della Grid
                    // Converto il tipo di sort
                    sortSpring = utility.sortVaadinToSpring(sortVaadinList, entityClazz);

                    try {
                        return mongo.fetch(entityClazz, (WrapFiltri) null, offset, limit).stream();
                    } catch (AlgosException unErrore) {
                        logger.error(unErrore, this.getClass(), "fromCallbacks");
                        return null;
                    }
                },

                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> {
                    try {
                        return mongo.count(entityClazz);
                    } catch (AlgosException unErrore) {
                        logger.error(unErrore, this.getClass(), "creaDataProvider");
                        return 0;
                    }
                }
        );

        return dataProvider;
    }


    public DataProvider<AEntity, Void> creaDataProvider(Class entityClazz, WrapFiltri wrapFiltri) {
        DataProvider dataProvider = DataProvider.fromCallbacks(

                // First callback fetches items based on a query
                fetchCallback -> {
                    // Esistono DUE tipi di Sort: quello di Spring e quello di Vaadin
                    Sort sortSpring;

                    // The index of the first item to load
                    int offset = fetchCallback.getOffset();

                    // The number of items to load
                    int limit = fetchCallback.getLimit();

                    // Ordine delle colonne
                    // Vaadin/Grid mi manda sempre UNA sola colonna. Perché?
                    List<QuerySortOrder> sortVaadinList = fetchCallback.getSortOrders();

                    // Alla partenza (se l'ordinamento manca) usa l'ordine base della AEntity
                    // le volte successive usa l'ordine selezionato da un header della Grid
                    // Converto il tipo di sort
                    sortSpring = utility.sortVaadinToSpring(sortVaadinList, entityClazz);

                    try {
                        return mongo.fetch(entityClazz, wrapFiltri, offset, limit).stream();
                    } catch (AlgosException unErrore) {
                        logger.error(unErrore, this.getClass(), "fromCallbacks");
                        return null;
                    }
                },

                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> {
                    try {
                        return mongo.count(entityClazz, wrapFiltri);
                    } catch (AlgosException unErrore) {
                        logger.error(unErrore, this.getClass(), "creaDataProvider");
                        return 0;
                    }
                }
        );

        return dataProvider;
    }


        public DataProvider<AEntity, Void> creaDataProvider(Class entityClazz, Map<String, AFiltro> mappaFiltri) {
        DataProvider dataProvider = DataProvider.fromCallbacks(

                // First callback fetches items based on a query
                fetchCallback -> {
                    // Esistono DUE tipi di Sort: quello di Spring e quello di Vaadin
                    Sort sortSpring;

                    // The index of the first item to load
                    int offset = fetchCallback.getOffset();

                    // The number of items to load
                    int limit = fetchCallback.getLimit();

                    // Ordine delle colonne
                    // Vaadin/Grid mi manda sempre UNA sola colonna. Perché?
                    List<QuerySortOrder> sortVaadinList = fetchCallback.getSortOrders();

                    // Alla partenza (se l'ordinamento manca) usa l'ordine base della AEntity
                    // le volte successive usa l'ordine selezionato da un header della Grid
                    // Converto il tipo di sort
                    sortSpring = utility.sortVaadinToSpring(sortVaadinList, entityClazz);

                    try {
                        return ((MongoService) mongo).fetch(entityClazz, mappaFiltri, sortSpring, offset, limit).stream();
                    } catch (AlgosException unErrore) {
                        logger.error(unErrore, this.getClass(), "fromCallbacks");
                        return null;
                    }
                },

                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> {
                    try {
                        return mongo.count(entityClazz, mappaFiltri);
                    } catch (AlgosException unErrore) {
                        logger.error(unErrore, this.getClass(), "creaDataProvider");
                        return 0;
                    }
                }
        );

        return dataProvider;
    }

}