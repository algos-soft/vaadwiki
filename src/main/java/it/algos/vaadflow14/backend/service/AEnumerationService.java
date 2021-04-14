package it.algos.vaadflow14.backend.service;

import com.vaadin.flow.component.Component;
import it.algos.vaadflow14.backend.enumeration.AETypePref;
import it.algos.vaadflow14.backend.packages.preferenza.Preferenza;
import it.algos.vaadflow14.ui.fields.AComboField;
import it.algos.vaadflow14.ui.fields.ARadioField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadflow14.backend.application.FlowCost.PUNTO_VIRGOLA;
import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Jul-2019
 * Time: 21:35
 * <p>
 * Gli AETypeField.enumeration, vengono registrati nel mongoDB col valore originale della property (qualsiasi) <br>
 * Nelle liste (Grid), vengono presentati come singolo valore di stringa <br>
 * Nelle schede (Form), vengono presentati come ComboBox di selezione del valore da selezionare tra quelli previsti
 * - nella annotation  @AIField che possono essere hardcoded oppure letti da una collection specificata oppure
 * - elaborati da un metodo specifico indicato nella annotation stessa
 * <p>
 * Nelle preferenze, gli AETypePref.enumeration sono costituiti da una stringa di valori (solo String) <br>
 * La stringa è sempre nella forma (x,y,z;y);
 * - una serie di valori (String) separati da virgola
 * - un punto e virgola di separazione
 * - un valore (String) selezionato che deve essere compreso tra quelli elencati prima del punto e virgola
 * La stringa viene registrata nel mongoDB come byte[] col metodo objectToBytes(value) <br>
 * Nelle liste (Grid), vengono presentati come singolo valore <br>
 * Nelle schede (Form), vengono presentati come ComboBox di selezione del valore da selezionare tra quelli fissi
 * - costituiti alla creazione della preferenza (e non più modificabili) <br>
 * <p>
 * La stringa della preferenza per il mongoDB può venire costruita:
 * - da una lista (List<String>) di valori, seguita dal valore (String) selezionato
 * - da una stringa di valori (separati da virgola), seguita dal valore (String) selezionato
 * <p>
 * Dalla stringa del mongoDB si può estrarre:
 * - la lista di valori
 * - il valore selezionato
 * <p>
 * Si può modificare la stringa del mongoDB, passandogli un nuovo valore selezionato che sostituisce quello precedente
 * lasciano immutata la lista dei valori di selezione
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AEnumerationService extends AAbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private constructor to avoid client applications to use constructor <br>
     * In alcune circostanze SpringBoot non riesce a costruire l'istanza <br>
     * Rimesso 'public' al posto del precedente 'private' <br>
     */
    public AEnumerationService() {
    }// end of constructor


    /**
     * La stringa della preferenza per il mongoDB può venire costruita:
     * - da una lista (List<String>) di valori, seguita dal valore (String) selezionato
     * - da una stringa di valori (separati da virgola), seguita dal valore (String) selezionato
     *
     * @param listaValoriPrevisti           non più modificabili dopo la creazione della preferenza
     * @param valoreInizialmenteSelezionato che deve essere compreso tra quelli previsti
     */
    public String fixPreferenzaMongoDB(List<String> listaValoriPrevisti, String valoreInizialmenteSelezionato) {
        String valoreStringaMongoDB = VUOTA;
        List<String> listaValori;

        if (array.isEmpty(listaValoriPrevisti) || text.isEmpty(valoreInizialmenteSelezionato)) {
            return VUOTA;
        }

        if (!listaValoriPrevisti.contains(valoreInizialmenteSelezionato)) {
            return VUOTA;
        }

        valoreStringaMongoDB = array.toStringa(listaValoriPrevisti);
        valoreStringaMongoDB += PUNTO_VIRGOLA;
        valoreStringaMongoDB += valoreInizialmenteSelezionato.trim();

        return valoreStringaMongoDB;
    }


    /**
     * La stringa della preferenza per il mongoDB può venire costruita:
     * - da una lista (List<String>) di valori, seguita dal valore (String) selezionato
     * - da una stringa di valori (separati da virgola), seguita dal valore (String) selezionato
     *
     * @param serieValoriPrevisti           non più modificabili dopo la creazione della preferenza
     * @param valoreInizialmenteSelezionato che deve essere compreso tra quelli previsti
     */
    public String fixPreferenzaMongoDB(String serieValoriPrevisti, String valoreInizialmenteSelezionato) {
        String valoreStringaMongoDB = VUOTA;
        List<String> listaValori;

        if (text.isEmpty(serieValoriPrevisti) || text.isEmpty(valoreInizialmenteSelezionato)) {
            return VUOTA;
        }

        listaValori = array.fromStringa(serieValoriPrevisti);
        if (array.isAllValid(listaValori)) {
            return fixPreferenzaMongoDB(listaValori, valoreInizialmenteSelezionato);
        }

        return VUOTA;
    }


    /**
     * La stringa della preferenza per il mongoDB può venire costruita:
     * - da una stringa di valori (separati da virgola), recuperata dal valore esistente nella entityBean
     * - dal nuovo valore (String) selezionato
     *
     * @param entityBean preferenza di riferimento
     * @param valueField da cui estrarre il nuovo valore
     */
    public byte[] generateModelValue(Preferenza entityBean, Component valueField) {
        String valueMongoDB = VUOTA;
        String selectedNewValue = VUOTA;
        List<String> listaValoriAmmissibili;

        if (entityBean == null || valueField == null) {
            return null;
        }

        if (valueField instanceof AComboField) {
            selectedNewValue = (String) ((AComboField<?>) valueField).getValue();
        }

        if (valueField instanceof ARadioField) {
            selectedNewValue = (String) ((ARadioField) valueField).getValue();
        }

        listaValoriAmmissibili = getList(entityBean);
        valueMongoDB = fixPreferenzaMongoDB(listaValoriAmmissibili, selectedNewValue);

        return AETypePref.enumeration.objectToBytes(valueMongoDB);
    }


    /**
     * Restituisce le due parti del valore grezzo <br>
     * Se manca il punto e virgola (valore selezionato = nullo), la matrice ha un solo valore <br>
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return matrice delle due parti
     */
    public String[] getParti(String rawValue) {
        String[] parti;
        String prima;
        String seconda;

        if (rawValue.contains(PUNTO_VIRGOLA)) {
            prima = rawValue.substring(0, rawValue.indexOf(PUNTO_VIRGOLA));
            seconda = rawValue.substring(rawValue.indexOf(PUNTO_VIRGOLA) + 1);
            parti = new String[]{prima, seconda};
        } else {
            parti = new String[]{rawValue};
        }

        return parti;
    }


    //    public byte[] generateModelValue(Preferenza entityBean, String rawValue) {
    //        return null;
    //    }


    /**
     * Trasforma il contenuto del mongoDB nel valore selezionato, eliminando la lista dei valori possibili <br>
     * Estrae la parte dopo il punto e virgola (se esiste) <br>
     *
     * @param bytes contenuto grezzo del mongoDB
     *
     * @return valore selezionato
     */
    public String setPresentationValue(byte[] bytes) {
        String value = VUOTA;
        String rawValue = (String) AETypePref.enumeration.bytesToObject(bytes);

        return text.isValid(rawValue) ? convertToPresentation(rawValue) : VUOTA;
    }


    /**
     * Trasforma il contenuto del mongoDB nel valore selezionato, eliminando la lista dei valori possibili <br>
     * Estrae la parte dopo il punto e virgola (se esiste) <br>
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return valore selezionato
     */
    public String convertToPresentation(String rawValue) {
        String value = VUOTA;
        String[] parti = getParti(rawValue);

        if (parti != null && parti.length == 2) {
            value = parti[1];
        }

        return value;
    }


    /**
     * Restituisce una stringa dei valori ammissibili <br>
     * Estrae la parte prima del punto e virgola (se esiste), altrimenti tutto <br>
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return stringa dei soli valori ammissibili
     */
    public String getStringaValori(String rawValue) {
        String stringaValori = rawValue;
        String[] parti = getParti(rawValue);

        if (parti != null && parti.length == 2) {
            stringaValori = parti[0];
        }

        return stringaValori;
    }


    /**
     * Restituisce la lista dei valori ammissibili <br>
     *
     * @param rawValue dei valori ammessi seguita dal valore selezionato
     *
     * @return lista dei valori ammissibili
     */
    public List<String> getList(String rawValue) {
        List<String> lista = null;
        String stringaValori = getStringaValori(rawValue);

        if (text.isValid(stringaValori)) {
            lista = array.fromStringa(stringaValori);
        }

        return lista;
    }


    /**
     * Restituisce la lista dei valori ammissibili <br>
     *
     * @param entityBean preferenza di riferimento
     *
     * @return lista dei valori ammissibili
     */
    public List<String> getList(Preferenza entityBean) {
        String rawValue = (String) entityBean.getValore();
        return text.isValid(rawValue) ? getList(rawValue) : null;
    }


    /**
     * Restituisce il valore selezionato, se esiste <br>
     *
     * @param entityBean preferenza di riferimento
     *
     * @return valore selezionato
     */
    public String getValue(Preferenza entityBean) {
        String rawValue = (String) entityBean.getValore();
        return text.isValid(rawValue) ? convertToPresentation(rawValue) : null;
    }


    /**
     * Modifica il valore della stringa per il mongoDB <br>
     * Costruisce la stringa da memorizzare <br>
     *
     * @param rawValue         dei valori registrati nel mongoDB (valori ammissibili seguiti dal valore selezionato)
     * @param newSelectedValue da sostituire
     *
     * @return valore selezionato
     */
    public String convertToModel(String rawValue, String newSelectedValue) {
        String newRawValue;
        String stringaValori = getStringaValori(rawValue);

        if (stringaValori.contains(newSelectedValue)) {
            if (text.isValid(stringaValori) && text.isValid(newSelectedValue)) {
                newRawValue = stringaValori + PUNTO_VIRGOLA + newSelectedValue;
            } else {
                newRawValue = stringaValori;
            }
        } else {
            newRawValue = rawValue;
            if (newRawValue.endsWith(PUNTO_VIRGOLA)) {
                newRawValue = text.levaCoda(newRawValue, PUNTO_VIRGOLA);
            }
        }

        return newRawValue;
    }

}
