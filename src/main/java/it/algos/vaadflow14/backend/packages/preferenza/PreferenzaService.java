package it.algos.vaadflow14.backend.packages.preferenza;

import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 23-set-2020
 * Time: 10:44
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
@Qualifier("preferenzaService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class PreferenzaService extends AService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public PreferenzaService() {
        super(Preferenza.class);
    }

    //    /**
    //     * Retrieves an entity by its id.
    //     *
    //     * @param keyID must not be {@literal null}.
    //     *
    //     * @return the entity with the given id or {@literal null} if none found
    //     *
    //     * @throws IllegalArgumentException if {@code id} is {@literal null}
    //     */
    //    public Preferenza findByKey(String keyCode) {
    //        return (Preferenza) mongo.findOneUnique(Preferenza.class, "code", keyCode);
    //    }
    //
    //
    //    /**
    //     * Retrieves an entity by its id.
    //     *
    //     * @param keyID must not be {@literal null}.
    //     *
    //     * @return the entity with the given id or {@literal null} if none found
    //     *
    //     * @throws IllegalArgumentException if {@code id} is {@literal null}
    //     */
    //    public Preferenza findById(String keyID) {
    //        return (Preferenza) mongo.findById(Preferenza.class, keyID);
    //    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param aePref: enumeration per la creazione-reset di tutte le entities
     *
     * @return la nuova entity appena creata e salvata
     */
    public Preferenza creaIfNotExist(AIPreferenza aePref) {
        return creaIfNotExist(aePref.getKeyCode(), aePref.getDescrizione(), aePref.getType(), aePref.getDefaultValue(), aePref.isVaadFlow(), aePref.isUsaCompany(), aePref.isNeedRiavvio(), aePref.isVisibileAdmin(), aePref.getNote());
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param code          codice di riferimento (obbligatorio)
     * @param descrizione   (obbligatoria)
     * @param type          (obbligatorio) per convertire in byte[] i valori
     * @param defaultValue  (obbligatorio) memorizza tutto in byte[]
     * @param vaadFlow      (obbligatorio) preferenza di vaadflow, di default true
     * @param usaCompany    (obbligatorio) se FlowVar.usaCompany=false, sempre false
     * @param needRiavvio   (obbligatorio) occorre riavviare per renderla efficace, di default false
     * @param visibileAdmin (obbligatorio) visibile agli admin, di default false se FlowVar.usaCompany=true
     *
     * @return la nuova entity appena creata e salvata
     */
    public Preferenza creaIfNotExist(String code, String descrizione, AETypePref type, Object defaultValue, boolean vaadFlow, boolean usaCompany, boolean needRiavvio, boolean visibileAdmin, String note) {
        return (Preferenza) checkAndSave(newEntity(code, descrizione, type, defaultValue, vaadFlow, usaCompany, needRiavvio, visibileAdmin, note));
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * <p>
     *
     * @param code          codice di riferimento (obbligatorio)
     * @param descrizione   (obbligatoria)
     * @param type          (obbligatorio) per convertire in byte[] i valori
     * @param defaultValue  (obbligatorio) memorizza tutto in byte[]
     * @param vaadFlow      (obbligatorio) preferenza di vaadflow, di default true
     * @param usaCompany    (obbligatorio) se FlowVar.usaCompany=false, sempre false
     * @param needRiavvio   (obbligatorio) occorre riavviare per renderla efficace, di default false
     * @param visibileAdmin (obbligatorio) visibile agli admin, di default false se FlowVar.usaCompany=true
     * @param note          (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(String code, String descrizione, AETypePref type, Object defaultValue, boolean vaadFlow, boolean usaCompany, boolean needRiavvio, boolean visibileAdmin, String note) {
        Preferenza newEntityBean = Preferenza.builderPreferenza()
                .code(text.isValid(code) ? code : null)
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .type(type != null ? type : AETypePref.string)
                .value(type != null ? type.objectToBytes(defaultValue) : (byte[]) null)
                .vaadFlow(vaadFlow)
                .usaCompany(FlowVar.usaCompany ? usaCompany : false)
                .needRiavvio(needRiavvio)
                .visibileAdmin(FlowVar.usaCompany ? visibileAdmin : true)
                .build();

        if (text.isValid(note)) {
            newEntityBean.note = note;
        }

        return (Preferenza) fixKey(newEntityBean);
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
    public Preferenza findById(final String keyID) {
        //        return (Preferenza) mongo.findById(Preferenza.class, keyID);
        return (Preferenza) super.findById(keyID);
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
    public Preferenza findByKey(final String keyValue) {
        //        return (Preferenza) mongo.findOneUnique(Preferenza.class, "code", keyValue);
        return (Preferenza) super.findByKey(keyValue);
    }

    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato in fase di 'startup' e dal bottone Reset di alcune liste <br>
     * <p>
     * 1) deve esistere lo specifico metodo sovrascritto
     * 2) deve essere valida la entityClazz
     * 3) deve esistere la collezione su mongoDB
     * 4) la collezione non deve essere vuota
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
    public AIResult resetEmptyOnly() {
        AIResult result = super.resetEmptyOnly();
        int numRecGen = 0;
        int numRecSpec = 0;
        String message;

        //-- standard (obbligatorie) di Vaadflow14, prese dalla enumeration AEPreferenza
        for (AIPreferenza aePref : AEPreferenza.values()) {
            numRecGen = creaIfNotExist(aePref) != null ? numRecGen + 1 : numRecGen;
        }

        //-- specifiche (facoltative) dell'applicazione in uso prese da una enumeration apposita
        if (FlowVar.preferenzeSpecificheList != null && FlowVar.preferenzeSpecificheList.size() > 0) {
            for (AIPreferenza aePref : FlowVar.preferenzeSpecificheList) {
                numRecSpec = creaIfNotExist(aePref) != null ? numRecSpec + 1 : numRecSpec;
            }
        }

        if ((numRecGen + numRecSpec) > 0) {
            result = super.fixPostReset(AETypeReset.enumeration, numRecGen + numRecSpec);
            message = String.format("Sono state create %d preferenze generali e %d specifiche di questa applicazione", numRecGen, numRecSpec);
            result.setValidationMessage(message);
        }
        else {
            result = AResult.errato();
        }

        return result;
    }


    public Object getValue(String keyCode) {
        Object value = null;
        Preferenza pref = findByKey(keyCode);

        if (pref != null) {
            value = pref.getType().bytesToObject(pref.value);
        }

        return value;
    }


    public Boolean isBool(String keyCode) {
        boolean status = false;
        Object objValue = getValue(keyCode);

        if (objValue != null && objValue instanceof Boolean) {
            status = (boolean) objValue;
        }
        else {
            logger.error("Algos - Preferenze. La preferenza: " + keyCode + " è del tipo sbagliato");
        }

        return status;
    }

}