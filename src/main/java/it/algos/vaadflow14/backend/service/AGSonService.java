package it.algos.vaadflow14.backend.service;

import com.google.gson.*;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.algos.vaadflow14.backend.entity.AEntity;
import org.bson.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 23-ott-2020
 * Time: 17:58
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AGSonService.class); <br>
 * 3) @Autowired public AGSonService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AGSonService extends AAbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    private MongoClient mongoClient = null;

    private MongoDatabase database = null;

    private MongoCollection<Document> collection;

    private Document document;

    private BasicDBObject query;

    private Document doc;

    /**
     * Controlla se esistono coppie di graffe interne <br>
     * Serve per i parametri di type @DBRef <br>
     *
     * @param doc 'documento' JSon della collezione mongo
     *
     * @return true se esistono; false se non esistono
     */
    public boolean isEsistonoDBRef(final Document doc) {
        return countGraffe(fixDoc(doc)) > 0;
    }


    /**
     * Conta quante coppie di graffe interne esistono <br>
     * Serve per i parametri di type @DBRef <br>
     *
     * @param jsonString estratta dal 'documento' JSon della collezione mongo
     *
     * @return numero di coppie interne; 0 se mancano; -1 se coppie non pari
     */
    public int countGraffe(final String jsonString) {
        String testo = jsonString.trim();
        int error = -1;
        int numIni;
        int numEnd;

        if (text.isEmpty(jsonString)) {
            return error;
        }

        testo = text.setNoGraffe(testo);
        numIni = StringUtils.countOccurrencesOf(testo, GRAFFA_INI);
        numEnd = StringUtils.countOccurrencesOf(testo, GRAFFA_END);

        return numIni == numEnd ? numIni : error;
    }


    /**
     * Estrae il contenuto della PRIMA (eventuale) coppia di graffe <br>
     * Comprensivo del nome del parametro che PRECEDE le graffe <br>
     *
     * @param jsonString estratta dal 'documento' JSon della collezione mongo
     *
     * @return contenuto della graffa COMPRESI gli estremi ed il nome del paragrafo
     */
    public String estraeGraffa(final String jsonString) {
        String testoOut;
        int ini;
        int end;

        if (text.isEmpty(jsonString)) {
            return null;
        }

        testoOut = jsonString.trim();
        if (countGraffe(testoOut) <= 0) {
            return null;
        }

        testoOut = text.setNoGraffe(testoOut);
        ini = testoOut.indexOf(GRAFFA_INI);
        end = testoOut.indexOf(GRAFFA_END, ini) + 1;
        ini = testoOut.lastIndexOf(VIRGOLA, ini) + 1;
        testoOut = testoOut.substring(ini, end);
        testoOut = text.setNoDoppiApici(testoOut);

        return testoOut.trim();
    }


    /**
     * Estrae i contenuti di (eventuali) coppie di graffe interne <br>
     * Contenuto delle graffe COMPRESI gli estremi ed il nome del paragrafo <br>
     * Se non ci sono graffe interne, restituisce un array nullo <br>
     * Se ci sono graffe interne, nel primo elemento dell'array il testoIn completo MENO i contenuti interni <br>
     *
     * @param jsonString estratta dal 'documento' JSon della collezione mongo
     *
     * @return lista di sub-contenuti, null se non ci sono graffe interne
     */
    public List<String> estraeGraffe(final String jsonString) {
        List<String> lista;
        String testo;
        String contenuto;

        if (text.isEmpty(jsonString)) {
            return null;
        }

        testo = jsonString.trim();
        if (countGraffe(testo) <= 0) {
            return null;
        }

        lista = new ArrayList<>();
        testo = text.setNoGraffe(testo);
        testo = text.setNoDoppiApici(testo);
        while (countGraffe(testo) > 0) {
            contenuto = estraeGraffa(testo);
            testo = testo.replace("\"" + contenuto + VIRGOLA, VUOTA);

            if (lista.size() == 0) {
                lista.add(testo);
                lista.add(contenuto);
            }
            else {
                lista.remove(0);
                lista.add(0, testo);
                lista.add(contenuto);
            }
        }

        return lista;
    }


    /**
     * Elimina gli underscore del tag _id <br>
     * Elimina gli underscore del tag _class <br>
     *
     * @param jsonString estratta dal 'documento' JSon della collezione mongo
     *
     * @return stringa elaborata
     */
    public String fixStringa(final String jsonString) {
        String testoOut = VUOTA;

        if (text.isValid(jsonString)) {
            testoOut = jsonString;
            testoOut = testoOut.replace("_id", "id");
            testoOut = testoOut.replace("_class", "class");
        }

        return testoOut;
    }


    /**
     * Semplice serializzazione dell' oggetto <br>
     * Recupera dal documento la stringa <br>
     * Elimina gli underscore del tag _id <br>
     * Elimina gli underscore del tag _class <br>
     *
     * @param doc 'documento' JSon della collezione mongo
     *
     * @return stringa in formato JSon
     */
    public String fixDoc(final Object doc) {
        String stringaJSON = VUOTA;
        Gson gSon;

        if (doc != null) {
            gSon = new Gson();
            stringaJSON = gSon.toJson(doc);
            stringaJSON = fixStringa(stringaJSON);
        }

        return stringaJSON;
    }

    /**
     * Costruzione della entity partendo dal valore della keyID <br>
     *
     * @param entityClazz della AEntity
     * @param valueID     della entityBean
     *
     * @return new entity
     */
    public AEntity crea(final Class entityClazz, final String valueID) {
        AEntity entityBean = null;
        collection = database.getCollection(entityClazz.getSimpleName().toLowerCase());
        query = new BasicDBObject();
        query.put("_id", valueID);

        doc = (Document) collection.find(query).first();
        entityBean = this.crea(doc, entityClazz);

        return entityBean;
    }

    /**
     * Costruzione della entity partendo da un documento JSON <br>
     * 1) Non ci sono campi linkati con @DBRef <br>
     * 2) Ci sono campi linkati con @DBRef <br>
     *
     * @param doc         'documento' JSon della collezione mongo
     * @param entityClazz della AEntity
     *
     * @return new entity
     */
    public AEntity crea(final Document doc, final Class entityClazz) {
        AEntity entityBean = null;

        if (entityClazz != null && doc != null) {
            entityBean = creaNoDbRef(doc, entityClazz);

            if (isEsistonoDBRef(doc)) {
                entityBean = addAllDBRef(doc, entityBean);
            }
        }

        return entityBean;
    }


    /**
     * Costruzione della entity partendo da un documento Gson <br>
     * Non ci sono campi linkati con @DBRef <br>
     * Recupera dal documento la stringa, sostituendo gli underscore del keyID <br>
     *
     * @param doc         'documento' JSon della collezione mongo
     * @param entityClazz della AEntity
     *
     * @return new entity
     */
    public AEntity creaNoDbRef(final Document doc, final Class entityClazz) {
        String jsonString = this.fixDoc(doc);
        return creaNoDbRef(entityClazz, jsonString);
    }


    /**
     * Costruzione della entity partendo da una stringa Gson <br>
     * Non ci sono campi linkati con @DBRef <br>
     *
     * @param entityClazz della AEntity
     * @param jsonString  estratta dal 'documento' JSon della collezione mongo
     *
     * @return new entityBean
     */
    public AEntity creaNoDbRef(final Class entityClazz, final String jsonString) {
        AEntity entityBean;
        Gson gSon;
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

            @Override
            public LocalDateTime deserialize(JsonElement json, Type type2, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return date.deserializeLocalDateTime(json);
            }
        });
        builder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {

            @Override
            public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return date.deserializeLocalDate(json);
            }
        });
        builder.registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {

            @Override
            public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return date.deserializeLocalTime(json);
            }
        });
        gSon = builder.create();

        entityBean = (AEntity) gSon.fromJson(jsonString, entityClazz);

        return entityBean;
    }


    /**
     * Integra la entity aggiungendo i parametri DBRef <br>
     * Ci sono campi linkati con @DBRef <br>
     * Aggiunge il valore dei parametri linkati ad un'altra classe (@DBRef)
     * Recupera dal documento la stringa, sostituendo gli underscore del keyID <br>
     *
     * @param doc        'documento' JSon della collezione mongo
     * @param entityBean senza i parametri DBRef
     *
     * @return entityBean con aggiunti i parametri DBRef
     */
    public AEntity addAllDBRef(final Document doc, final AEntity entityBean) {
        String jsonString = this.fixDoc(doc);
        List<String> listaContenutiGraffe = estraeGraffe(jsonString);

        if (array.isAllValid(listaContenutiGraffe)) {
            for (String jsonTestoLink : listaContenutiGraffe.subList(1, listaContenutiGraffe.size())) {
                addSingoloDBRef(entityBean, jsonTestoLink);
            }
        }

        return entityBean;
    }


    /**
     * Integra la entity aggiungendo il singolo parametro DBRef <br>
     *
     * @param entityBean     senza il parametro DBRef
     * @param jsonStringLink estratta dal 'documento' JSon della collezione mongo
     *
     * @return entityBean con aggiunto il parametro DBRef
     */
    public AEntity addSingoloDBRef(final AEntity entityBean, final String jsonStringLink) {
        AEntity entityBeanConDBRef = null;
        String[] parti;
        String nomeCollezioneLinkata = VUOTA;
        String riferimentoLinkato = VUOTA;
        String valoreID = VUOTA;

        parti = jsonStringLink.split(DUE_PUNTI + GRAFFA_INI_REGEX);
        if (parti != null) {
            nomeCollezioneLinkata = parti[0];
            nomeCollezioneLinkata = text.setNoDoppiApici(nomeCollezioneLinkata).trim();
            riferimentoLinkato = parti[1];
        }
        if (text.isValid(riferimentoLinkato)) {
            parti = riferimentoLinkato.split(VIRGOLA);
            if (parti != null) {
                parti = parti[0].split(DUE_PUNTI);

                if (parti != null) {
                    valoreID = parti[1];
                    valoreID = text.setNoDoppiApici(valoreID).trim();
                }
            }
        }

        if (text.isValid(nomeCollezioneLinkata) && text.isValid(valoreID)) {
            entityBeanConDBRef = creaEntityDBRef(entityBean, nomeCollezioneLinkata, valoreID);
        }

        return entityBeanConDBRef;
    }


    /**
     * Crea la entity linkata dal DBRef <br>
     *
     * @param entityBean            senza il parametro DBRef
     * @param nomeCollezioneLinkata del parametro DBRef
     * @param valueID               della entityBeanLinkata
     *
     * @return entityBean con aggiunto il parametro DBRef
     */
    public AEntity creaEntityDBRef(final AEntity entityBean, final String nomeCollezioneLinkata, final String valueID) {
        AEntity entityBeanConDBRef = entityBean;
        AEntity entityBeanLinkata;
        Class entityLinkClazz;
        Field field;

        field = reflection.getField(entityBean.getClass(), nomeCollezioneLinkata);
        entityLinkClazz = annotation.getComboClass(field);
        collection = database.getCollection(nomeCollezioneLinkata);

        query = new BasicDBObject();
        query.put("_id", valueID);
        document = collection.find(query).first();

        entityBeanLinkata = crea(document, entityLinkClazz);

        try {
            field.set(entityBeanConDBRef, entityBeanLinkata);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "creaEntity");
        }

        return entityBeanConDBRef;
    }


}