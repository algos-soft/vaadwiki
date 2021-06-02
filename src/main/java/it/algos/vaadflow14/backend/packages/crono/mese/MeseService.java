package it.algos.vaadflow14.backend.packages.crono.mese;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: ven, 25-dic-2020
 * Last doc revision: mer, 19-mag-2021 alle 18:38 <br>
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
@Qualifier("crono/meseService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class MeseService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public MeseService() {
        super(Mese.class);
    }

    /**
     * Crea e registra una entityBean col flag reset=true <br>
     *
     * @param aeMese enumeration per la creazione-reset di tutte le entities
     *
     * @return true se la entity è stata creata e salvata
     */
    private boolean creaReset(final AEMese aeMese) {
        return super.creaReset(newEntity(aeMese.getNome(), aeMese.getGiorni(), aeMese.getGiorniBisestili(), aeMese.getSigla()));
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Mese newEntity() {
        return newEntity(VUOTA, 0, 0, VUOTA);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param mese            nome completo (obbligatorio, unico)
     * @param giorni          numero di giorni presenti (obbligatorio)
     * @param giorniBisestile numero di giorni presenti in un anno bisestile (obbligatorio)
     * @param sigla           nome abbreviato di tre cifre (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Mese newEntity(final String mese, final int giorni, final int giorniBisestile, final String sigla) {
        Mese newEntityBean = Mese.builderMese()
                .ordine(getNewOrdine())
                .mese(text.isValid(mese) ? mese : null)
                .giorni(giorni)
                .giorniBisestile(giorniBisestile)
                .sigla(text.isValid(sigla) ? sigla : null)
                .build();

        return (Mese) fixKey(newEntityBean);
    }


    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato dal bottone Reset di alcune liste <br>
     * <p>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return wrapper col risultato ed eventuale messaggio di errore
     */
    @Override
    public AIResult reset() {
        AIResult result = super.reset();
        int numRec = 0;

        if (result.isErrato()) {
            return result;
        }

        for (AEMese aeMese : AEMese.values()) {
            numRec = creaReset(aeMese) ? numRec + 1 : numRec;
        }

        return AResult.valido(AETypeReset.enumeration.get(), numRec);
    }

}// end of Singleton class