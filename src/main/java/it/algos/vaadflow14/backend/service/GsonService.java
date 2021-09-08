package it.algos.vaadflow14.backend.service;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.*;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import org.bson.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;
import org.springframework.util.StringUtils;

import javax.annotation.*;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 23-ott-2020
 * Time: 17:58
 * <p>
 * Gson (also known as Google Gson) is an open-source Java library to serialize and deserialize Java objects to (and from) JSON. <br>
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
 *
 * @see https://www.baeldung.com/jackson-object-mapper-tutorial
 * @see https://www.baeldung.com/jackson-serialize-dates
 * @see https://www.baeldung.com/jackson-vs-gson
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GsonService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    public MongoDatabase dataBase = null;

    private MongoClient mongoClient = null;

    private MongoCollection<Document> collection;

    private Document document;

    private BasicDBObject query;

    private Document doc;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se viene implementata una sottoclasse, passa di qui per ogni sottoclasse oltre che per questa istanza <br>
     * Se esistono delle sottoclassi, passa di qui per ognuna di esse (oltre a questa classe madre) <br>
     */
    @PostConstruct
    private void postConstruct() {
        fixProperties();
    }

    /**
     * Creazione iniziale di eventuali properties indispensabili per l'istanza <br>
     * Metodo chiamato dall'esterno (tipicamente da un Test) <br>
     */
    public void fixProperties(final String databaseName) {
        this.databaseName = databaseName;
        this.fixProperties();
    }

    /**
     * Creazione iniziale di eventuali properties indispensabili per l'istanza <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Metodo private che NON può essere sovrascritto <br>
     */
    private void fixProperties() {
        MongoIterable<String> nomiCollezioni;
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + databaseName);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);

        if (text.isValid(databaseName)) {
            dataBase = mongoClient.getDatabase(databaseName);
        }
    }

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
     * @return contenuto della graffa COMPRESI gli estremi e il nome del paragrafo
     */
    public String estraeGraffa(final String jsonString) {
        String testoOut;
        int ini;
        int end;

        if (text.isEmpty(jsonString)) {
            return VUOTA;
        }

        testoOut = jsonString.trim();
        if (countGraffe(testoOut) < 0) {
            return VUOTA;
        }
        if (countGraffe(testoOut) == 0) {
            return VUOTA;
        }

        testoOut = text.setNoGraffe(testoOut);
        ini = testoOut.indexOf(GRAFFA_INI);
        end = testoOut.indexOf(GRAFFA_END, ini) + 1; //graffa
        ini = testoOut.lastIndexOf(VIRGOLA, ini) + 1;
        try {
            testoOut = testoOut.substring(ini, end);
        } catch (Exception unErrore) {
            int a = 87;
        }

        return testoOut.trim();
    }

    /**
     * Elimina il contenuto della PRIMA (eventuale) coppia di graffe <br>
     * Comprensivo del nome del parametro che PRECEDE le graffe <br>
     *
     * @param jsonString estratta dal 'documento' JSon della collezione mongo
     *
     * @return contenuto della graffa COMPRESI gli estremi e il nome del paragrafo
     */
    public String eliminaGraffa(final String jsonString) {
        String testoOut = jsonString;
        String testoGraffa = estraeGraffa(jsonString);

        if (text.isValid(testoGraffa)) {
            testoOut = jsonString.replace(VIRGOLA + testoGraffa, VUOTA);
        }

        return testoOut.trim();
    }


    /**
     * Estrae i contenuti di (eventuali) coppie di graffe interne <br>
     * Contenuto delle graffe COMPRESI gli estremi e il nome del paragrafo <br>
     * Se non ci sono graffe interne, restituisce un array nullo <br>
     * Se ci sono graffe interne, nel primo elemento dell'array il testoIn completo MENO i contenuti interni <br>
     *
     * @param jsonString estratta dal 'documento' JSon della collezione mongo
     *
     * @return lista di sub-contenuti, null se non ci sono graffe interne
     */
    public List<String> estraeGraffe(final String jsonString) {
        List<String> lista = new ArrayList<>();
        String testo;
        String contenuto;

        if (text.isEmpty(jsonString)) {
            return null;
        }

        if (countGraffe(jsonString) <= 0) {
            return array.creaArraySingolo(jsonString);
        }

        testo = jsonString.trim();
        testo = text.setNoGraffe(testo);
        while (countGraffe(testo) > 0) {
            contenuto = estraeGraffa(testo);
            testo = testo.replace(VIRGOLA + contenuto, VUOTA);
            contenuto = text.levaCoda(contenuto, VIRGOLA);

            if (lista.size() == 0) {
                lista.add(text.setGraffe(testo));
            }
            else {
                lista.set(0, text.setGraffe(testo));
            }
            lista.add(contenuto);
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
     * @param keyId       chiave identificativa della entityBean
     *
     * @return new entity
     */
    @Deprecated
    public AEntity creaOld(final Class entityClazz, final String keyId) {
        AEntity entityBean = null;
        String keyPropertyName;
        collection = dataBase.getCollection(entityClazz.getSimpleName().toLowerCase());

        query = new BasicDBObject();
        query.put("_id", keyId);

        doc = collection.find(query).first();

        if (doc != null) {
            entityBean = this.creaOld(doc, entityClazz);
            String pippoz = mongoToString(entityClazz, keyId);
        }
        else {
            keyPropertyName = annotation.getKeyPropertyName(entityClazz);
            if (text.isValid(keyPropertyName)) {
                query = new BasicDBObject();
                query.put(keyPropertyName, keyId);
                doc = collection.find(query).first();
                entityBean = this.creaOld(doc, entityClazz);
            }
        }

        return entityBean;
    }

    /**
     * Costruzione di un testo JSON partendo dal valore della keyID <br>
     *
     * @param entityClazz della entityBean
     * @param keyId       chiave identificativa della entityBean
     *
     * @return stringa json
     */
    public String mongoToString(final Class entityClazz, final String keyId) {
        String jsonString = VUOTA;
        collection = dataBase.getCollection(entityClazz.getSimpleName().toLowerCase());
        Gson gSon;
        String keyPropertyName;

        if (collection != null) {
            query = new BasicDBObject();
            query.put("_id", keyId);
            doc = collection.find(query).first();
        }

        if (doc != null) {
            gSon = new Gson();
            jsonString = gSon.toJson(doc);
        }
        else {
            keyPropertyName = annotation.getKeyPropertyName(entityClazz);
            if (text.isValid(keyPropertyName)) {
                query = new BasicDBObject();
                query.put(keyPropertyName, keyId);
                doc = collection.find(query).first();
                if (doc != null) {
                    gSon = new Gson();
                    jsonString = gSon.toJson(doc);
                }
            }
        }

        return jsonString;
    }

    /**
     * Costruzione della entity partendo da un documento JSON <br>
     * 1) Non ci sono campi linkati con @DBRef <br>
     * 2) Ci sono campi linkati con @DBRef <br>
     *
     * @param jsonString  'documento' JSon della collezione mongo
     * @param entityClazz della AEntity
     *
     * @return new entity
     */
    public AEntity stringToEntity(final Class entityClazz, final String jsonString) {
        AEntity entityBean = null;
        List<String> listaNoDbRefAndGraffe = estraeGraffe(jsonString);

        if (entityClazz != null && doc != null) {

            entityBean = creaNoDbRef(entityClazz, listaNoDbRefAndGraffe.get(0));

            if (isEsistonoDBRef(doc)) {
                entityBean = addAllDBRef(doc, entityBean);
            }
        }

        return entityBean;
    }


    /**
     * Costruzione della entity partendo dal valore della keyID <br>
     *
     * @param entityClazz della AEntity
     * @param keyId       chiave identificativa della entityBean
     *
     * @return new entity
     */
    public AEntity creaId(final Class entityClazz, final String keyId) {
        AEntity entityBean;

        String jsonString = this.mongoToString(entityClazz, keyId);
        jsonString = fixStringa(jsonString);
        entityBean = this.stringToEntity(entityClazz, jsonString);

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
    @Deprecated
    public AEntity creaOld(final Document doc, final Class entityClazz) {
        AEntity entityBean = null;
        String jsonString = this.fixDoc(doc);
        List<String> listaNoDbRefAndGraffe = estraeGraffe(jsonString);

        if (entityClazz != null && doc != null) {

            entityBean = creaNoDbRef(entityClazz, listaNoDbRefAndGraffe.get(0));

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
        jsonString = estraeGraffa(jsonString);
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
    public AEntity creaNoDbRef(final Class entityClazz, String jsonString) {
        AEntity entityBean = null;
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
        jsonString = jsonString.replace("_id", "id");
        try {
            entityBean = (AEntity) gSon.fromJson(jsonString, entityClazz);
        } catch (JsonSyntaxException unErrore) {
            logger.error(unErrore, this.getClass(), "creaNoDbRef");
        }

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
        collection = dataBase.getCollection(nomeCollezioneLinkata);

        query = new BasicDBObject();
        query.put("_id", valueID);
        document = collection.find(query).first();

        entityBeanLinkata = creaOld(document, entityLinkClazz);

        try {
            field.set(entityBeanConDBRef, entityBeanLinkata);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "creaEntity");
        }

        return entityBeanConDBRef;
    }

    /**
     * Costruzione del testo Json per il mongoDB <br>
     * Aggiungo: -class
     * Modifica: -id
     *
     * @param entityBean (nuova o esistente)
     *
     * @return testo Json
     */
    public String entityToString(final AEntity entityBean) {
        String jsonString = VUOTA;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            jsonString = mapper.writeValueAsString(entityBean);
        } catch (JsonProcessingException unErrore) {
            System.out.println(unErrore);
        }

        return jsonString;
    }

}