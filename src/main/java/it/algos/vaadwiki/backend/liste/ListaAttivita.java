package it.algos.vaadwiki.backend.liste;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 18:44
 * <p>
 * Lista delle persone per attività <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Creata con appContext.getBean(ListaAttivita.class, attivita) in (@PostConstruct)UploadAttivita.inizia() <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaService attivitaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioService bioService;

    //--property
    protected Attivita attivita;

    /**
     * The Service.
     */
    @Autowired
    public MongoService mongoService;

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param attivita di cui costruire la pagina sul server wiki
     */
    public ListaAttivita(Attivita attivita) {
        this.attivita = attivita;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
    }


    /**
     * Costruisce una lista di didascalie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaMappaDidascalie() {
        super.creaMappaDidascalie();
    }


    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata <br>
     */
    public List<Bio> getListaBio() {
        List<Bio> listaBio = null;

        switch (FlowVar.typeSerializing) {
            case spring:
                listaBio = bioService.fetch("attivita", attivita.singolare);
                break;
            case gson:
                try {
                    listaBio = mongoService.fetchGson(Bio.class, (WrapFiltri)null, 0, 0);
                    int a=87;
                } catch (AlgosException unErrore) {
                    //                    throw AlgosException.stack(unErrore, this.getClass(), "nomeDelMetodo");
                }
                int ab=87;

                break;
            case jackson:
                break;
            default:
                break;
        }

        return listaBio;
    }

}
