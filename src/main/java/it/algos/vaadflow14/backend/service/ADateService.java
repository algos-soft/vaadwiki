package it.algos.vaadflow14.backend.service;

import com.google.gson.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mar, 05-mag-2020
 * Time: 10:15
 * <p>
 * Classe di servizio <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * L' istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private ADateService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ADateService extends AAbstractService {


    /**
     * Convert java.util.Date to java.time.LocalDate <br>
     * Date HA ore, minuti e secondi <br>
     * LocalDate NON ha ore, minuti e secondi <br>
     * Si perdono quindi le ore i minuti ed i secondi di Date <br>
     * Usare alternativamente dateToLocalDateTime <br>
     *
     * @param data da convertire
     *
     * @return data locale (deprecated)
     */
    @Deprecated
    public LocalDate dateToLocalDate(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }


    /**
     * Convert java.time.LocalDate to java.util.Date
     * LocalDate NON ha ore, minuti e secondi
     * Date HA ore, minuti e secondi
     * La Date ottenuta ha il tempo regolato a mezzanotte
     *
     * @param localDate da convertire
     *
     * @return data (deprecated)
     */
    @Deprecated
    public Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }


    /**
     * Convert java.time.LocalDateTime to java.util.Date
     * LocalDateTime HA ore, minuti e secondi
     * Date HA ore, minuti e secondi
     * Non si perde nulla
     *
     * @param localDateTime da convertire
     *
     * @return data (deprecated)
     */
    @Deprecated
    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }


    /**
     * Convert java.time.LocalDate to java.time.LocalDateTime
     * LocalDate NON ha ore, minuti e secondi
     * LocalDateTime HA ore, minuti e secondi
     * La LocalDateTime ottenuta ha il tempo regolato a mezzanotte
     *
     * @param localDate da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    @Deprecated
    public LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
        Date date = localDateToDate(localDate);
        Instant istante = date.toInstant();
        return istante.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    /**
     * Convert java.util.Date to java.time.LocalDateTime
     * Date HA ore, minuti e secondi
     * LocalDateTime HA ore, minuti e secondi
     * Non si perde nulla
     *
     * @param data da convertire
     *
     * @return data e ora locale
     */
    public LocalDateTime dateToLocalDateTime(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


    /**
     * Convert java.time.LocalDateTime to java.time.LocalDate
     * LocalDateTime HA ore, minuti e secondi
     * LocalDate NON ha ore, minuti e secondi
     * Si perdono quindi le ore i minuti ed i secondi di Date
     *
     * @param localDateTime da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    public LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.toLocalDate() : null;
    }


    /**
     * Trasforma la data nel formato standard ISO 8601.
     * <p>
     * 2017-02-16T21:00:00
     * Unsupported field: OffsetSeconds
     * Dovrebbe essere 2017-02-16T21:00:00.000+01:00 per essere completa
     *
     * @param localDate fornita
     *
     * @return testo standard ISO senza OffsetSeconds
     */
    public String getISO(LocalDate localDate) {
        return getISO(localDateToLocalDateTime(localDate));
    }


    /**
     * Trasforma la data e l'0rario nel formato standard ISO 8601.
     * <p>
     * 2017-02-16T21:00:00
     * Unsupported field: OffsetSeconds
     * Dovrebbe essere 2017-02-16T21:00:00.000+01:00 per essere completa
     *
     * @param localDateTime fornito
     *
     * @return testo standard ISO senza OffsetSeconds
     */
    public String getISO(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(AETypeData.iso8601.getPattern(), LOCALE));
    }


    /**
     * Costruisce una data da una stringa in formato ISO 8601
     *
     * @param isoStringa da leggere
     *
     * @return data costruita
     */
    private Date dateFromISO(String isoStringa) {
        Date data = null;
        DateFormat format = new SimpleDateFormat(AETypeData.iso8601.getPattern());

        try {
            data = format.parse(isoStringa);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "dateFromISO");

        }

        return data;
    }


    /**
     * Costruisce una localData da una stringa in formato ISO 8601
     * ATTENZIONE: si perdono ore, minuti e secondi (se ci sono)
     *
     * @param isoStringa da leggere
     *
     * @return localData costruita
     */
    public LocalDate localDateFromISO(String isoStringa) {
        return dateToLocalDate(dateFromISO(isoStringa));
    }


    /**
     * Costruisce una localDateTime da una stringa in formato ISO 8601
     *
     * @param isoStringa da leggere
     *
     * @return localDateTime costruita
     */
    public LocalDateTime localDateTimeFromISO(String isoStringa) {
        return dateToLocalDateTime(dateFromISO(isoStringa));
    }


    /**
     * Convert java.util.Date to java.time.LocalDateTime
     * Date HA ore, minuti e secondi
     * LocalDateTime HA ore, minuti e secondi
     * Non si perde nulla
     *
     * @param data da convertire
     *
     * @return data e ora locale
     */
    public LocalDateTime dateToLocalDateTimeUTC(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }


    /**
     * Convert java.util.LocalDateTime to java.time.LocalTime
     * Estrae la sola parte di Time
     * LocalDateTime HA anni, giorni, ore, minuti e secondi
     * LocalTime NON ha anni e giorni
     * Si perdono quindi gli anni ed i giorni di LocalDateTime
     *
     * @param localDateTime da convertire
     *
     * @return time senza il giorno
     */
    public LocalTime localDateTimeToLocalTime(LocalDateTime localDateTime) {
        return LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
    }


    /**
     * Restituisce la data corrente nella forma del pattern standard. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: d MMM yyyy <br>
     * Esempio: 20 gen 2019 <br>
     *
     * @return la data sotto forma di stringa
     */
    public String get() {
        return get(LocalDate.now());
    }


    /**
     * Restituisce la data nella forma del pattern standard. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: d MMM yyyy <br>
     * Esempio: 20 gen 2019 <br>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDate localDate) {
        return get(localDate, AETypeData.standard);
    }

    /**
     * Restituisce la data nella forma del pattern standard. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: d-M-yy H:mm <br>
     * Esempio: 18-4-17 13:45 <br>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDateTime localDateTime) {
        return get(localDateTime, AETypeData.normaleOrario);
    }

    /**
     * Restituisce la data nella forma del pattern standard. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: H:mm <br>
     * Esempio:  13:45 <br>
     *
     * @param localTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalTime localTime) {
        return get(localTime, AETypeData.orario);
    }


    /**
     * Restituisce la data nella forma del pattern ricevuto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param dateObj da rappresentare
     * @param pattern per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(Object dateObj, AETypeData pattern) {
        if (dateObj != null) {
            if (dateObj instanceof LocalDateTime) {
                return get((LocalDateTime) dateObj, pattern);
            }
            if (dateObj instanceof LocalDate) {
                return get((LocalDate) dateObj, pattern);
            }
            if (dateObj instanceof LocalTime) {
                return get((LocalTime) dateObj, pattern);
            }
            return VUOTA;
        }
        return VUOTA;
    }


    /**
     * Restituisce la data nella forma del pattern ricevuto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localDateTime da rappresentare
     * @param pattern       per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDateTime localDateTime, AETypeData pattern) {
        if (pattern.isSenzaTime()) {
            return VUOTA;
        }
        else {
            return get(localDateTime, pattern.getPattern());
        }
    }


    /**
     * Restituisce la data nella forma del pattern ricevuto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localDate da rappresentare
     * @param pattern   per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDate localDate, AETypeData pattern) {
        if (pattern.isSenzaTime()) {
            return get(localDate, pattern.getPattern());
        }
        else {
            return get(localDate);
        }
    }


    /**
     * Restituisce la data nella forma del pattern ricevuto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localTime da rappresentare
     * @param pattern   per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalTime localTime, AETypeData pattern) {
        return get(localTime, pattern.getPattern());
    }


    /**
     * Restituisce la data nella forma del pattern ricevuto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localDateTime da rappresentare
     * @param pattern       per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDateTime localDateTime, String pattern) {
        if (localDateTime != null) {
            return localDateTime.format(DateTimeFormatter.ofPattern(pattern, LOCALE));
        }
        else {
            return VUOTA;
        }
    }


    /**
     * Restituisce la data nella forma del pattern ricevuto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localDate da rappresentare
     * @param pattern   per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDate localDate, String pattern) {
        if (localDate != null) {
            return localDate.format(DateTimeFormatter.ofPattern(pattern, LOCALE));
        }
        else {
            return VUOTA;
        }
    }


    /**
     * Restituisce la data nella forma del pattern ricevuto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localTime da rappresentare
     * @param pattern   per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalTime localTime, String pattern) {
        if (localTime != null) {
            return localTime.format(DateTimeFormatter.ofPattern(pattern, LOCALE));
        }
        else {
            return VUOTA;
        }
    }


    /**
     * Restituisce la data nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <br>
     * Pattern: d-M-yy <br>
     * Esempio: 5-4-17 <br>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getCorta(LocalDate localDate) {
        return get(localDate, AETypeData.dateShort);
    }


    /**
     * Restituisce la data nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: d-MMM-yy <br>
     * Esempio: 5-ott-14 <br>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getNormale(LocalDate localDate) {
        return get(localDate, AETypeData.dateNormal);
    }


    /**
     * Restituisce la data nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: d-MMMM-yyy <br>
     * Esempio: 5-ottobre-2014 <br>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getLunga(LocalDate localDate) {
        return get(localDate, AETypeData.dateLong);
    }


    /**
     * Restituisce la data nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: EEEE, d-MMMM-yyy <br>
     * Esempio: domenica, 5-ottobre-2014 <br>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getCompleta(LocalDate localDate) {
        return get(localDate, AETypeData.dataCompleta);
    }

    /**
     * Restituisce la data nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: EEEE, d-MMMM-yyy <br>
     * Esempio: dom, 5-ott-2014 <br>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getCompletaShort(LocalDate localDate) {
        return get(localDate, AETypeData.dataCompletaShort);
    }


    /**
     * Restituisce la data e l' orario nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: d-M-yy H:mm <br>
     * Esempio: 18-4-17 13:45 <br>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getDataOrario(LocalDateTime localDateTime) {
        return get(localDateTime, AETypeData.normaleOrario);
    }


    /**
     * Restituisce la data e l' orario nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: EEEE, d-MMMM-yyy 'alle' H:mm <br>
     * Esempio: domenica, 5-ottobre-2014 alle 13:45 <br>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getDataOrarioCompleta(LocalDateTime localDateTime) {
        return get(localDateTime, AETypeData.completaOrario);
    }


    /**
     * Restituisce la data e l' orario nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     * Pattern: d-M-yy 'alle' H:mm <br>
     * Esempio: 5-ott-14 alle 13:45 <br>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getDataOrarioBreve(LocalDateTime localDateTime) {
        return get(localDateTime, AETypeData.breveOrario);
    }


    /**
     * Restituisce l' orario corrente nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @return la data sotto forma di stringa
     */
    public String getOrario() {
        return getOrario(LocalTime.now());
    }


    /**
     * Restituisce l' orario nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getOrario(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(AETypeData.orario.getPattern(), LOCALE));
    }


    /**
     * Restituisce l' orario nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the date <br>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getOrario(LocalDateTime localDateTime) {
        return getOrario(localDateTimeToLocalTime(localDateTime));
    }


    /**
     * Restituisce l' orario nella forma del pattern previsto. <br>
     * <p>
     * Returns a string representation of the time <br>
     * Pattern: H:mm:ss <br>
     * Esempio:  13:45:08 <br>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getOrarioCompleto(LocalDateTime localDateTime) {
        return localDateTimeToLocalTime(localDateTime).format(DateTimeFormatter.ofPattern(AETypeData.orarioLungo.getPattern(), LOCALE));
    }


    /**
     * Costruisce tutti i giorni del mese <br>
     * Considera anche l'anno bisestile <br>
     * <p>
     * Restituisce un array di Map <br>
     * Ogni mappa ha: <br>
     * numeroMese <br>
     * nomeMese <br>
     * #progressivoNormale <br>
     * #progressivoBisestile <br>
     * nome  (numero per il primo del mese) <br>
     * titolo (1° per il primo del mese) <br>
     *
     * @param numMese  numero del mese, partendo da 1 per gennaio
     * @param progAnno numero del giorno nell'anno, partendo da 1 per il 1° gennaio
     *
     * @return lista di mappe, una per ogni giorno del mese considerato
     */
    private List<HashMap> getGiorniMese(int numMese, int progAnno) {
        List<HashMap> listaMese = new ArrayList<HashMap>();
        HashMap mappa;
        int giorniDelMese;
        String nomeMese;
        AEMese mese = AEMese.getMese(numMese);
        nomeMese = AEMese.getLong(numMese);
        giorniDelMese = AEMese.getGiorni(numMese, 2016);
        final int taglioBisestile = 60;
        String tag;
        String tagUno;

        for (int k = 1; k <= giorniDelMese; k++) {
            progAnno++;
            tag = k + SPAZIO + nomeMese;
            mappa = new HashMap();

            mappa.put(KEY_MAPPA_GIORNI_MESE_NUMERO, numMese);
            mappa.put(KEY_MAPPA_GIORNI_MESE_TESTO, nomeMese);
            mappa.put(KEY_MAPPA_GIORNI_NOME, tag);
            mappa.put(KEY_MAPPA_GIORNI_BISESTILE, progAnno);
            mappa.put(KEY_MAPPA_GIORNI_NORMALE, progAnno);
            mappa.put(KEY_MAPPA_GIORNI_MESE_MESE, mese);

            if (k == 1) {
                mappa.put(KEY_MAPPA_GIORNI_TITOLO, PRIMO_GIORNO_MESE + SPAZIO + nomeMese);
            }
            else {
                mappa.put(KEY_MAPPA_GIORNI_TITOLO, tag);
            }
            //--gestione degli anni bisestili
            if (progAnno == taglioBisestile) {
                mappa.put(KEY_MAPPA_GIORNI_NORMALE, 0);
            }
            if (progAnno > taglioBisestile) {
                mappa.put(KEY_MAPPA_GIORNI_NORMALE, progAnno - 1);
            }
            listaMese.add(mappa);
        }
        return listaMese;
    }


    /**
     * Costruisce tutti i giorni dell'anno <br>
     * Considera anche l'anno bisestile <br>
     * <p>
     * Restituisce un array di Map <br>
     * Ogni mappa ha: <br>
     * numeroMese <br>
     * #progressivoNormale <br>
     * #progressivoBisestile <br>
     * nome  (numero per il primo del mese) <br>
     * titolo (1° per il primo del mese) <br>
     */
    public List<HashMap> getAllGiorni() {
        List<HashMap> listaAnno = new ArrayList<HashMap>();
        List<HashMap> listaMese;
        int progAnno = 0;

        for (int k = 1; k <= 12; k++) {
            listaMese = getGiorniMese(k, progAnno);
            for (HashMap mappa : listaMese) {
                listaAnno.add(mappa);
            }
            progAnno += listaMese.size();
        }

        return listaAnno;
    }


    /**
     * Anno bisestile
     *
     * @param anno da validare
     *
     * @return true se l'anno è bisestile
     */
    public boolean bisestile(int anno) {
        boolean bisestile = false;
        int deltaGiuliano = 4;
        int deltaSecolo = 100;
        int deltaGregoriano = 400;
        int inizioGregoriano = 1582;
        boolean bisestileSecolo = false;

        bisestile = math.divisibileEsatto(anno, deltaGiuliano);
        if (anno > inizioGregoriano && bisestile) {
            if (math.divisibileEsatto(anno, deltaSecolo)) {
                if (math.divisibileEsatto(anno, deltaGregoriano)) {
                    bisestile = true;
                }
                else {
                    bisestile = false;
                }
            }
        }

        return bisestile;
    }

    /**
     * Deserializza un elemento temporale
     *
     * @param json da cui estrarre l' elemento temporale
     *
     * @return elemento temporale
     */
    public LocalDateTime deserializeLocalDateTime(JsonElement json) {
        LocalDate data;
        LocalTime orario;

        String[] parti = json.getAsString().split(SPAZIO);
        int mese = AEMese.getNumMese(parti[0]);
        int giorno = Integer.parseInt(text.levaCoda(parti[1], VIRGOLA));
        int anno = Integer.parseInt(text.levaCoda(parti[2], VIRGOLA));

        data = LocalDate.of(anno, mese, giorno);
        parti = parti[3].split(DUE_PUNTI);
        orario = LocalTime.of(Integer.parseInt(parti[0]), Integer.parseInt(parti[1]), Integer.parseInt(parti[2]));

        return LocalDateTime.of(data, orario);
    }


    /**
     * Deserializza un elemento temporale
     *
     * @param json da cui estrarre l' elemento temporale
     *
     * @return elemento temporale
     */
    public LocalDate deserializeLocalDate(JsonElement json) {
        String[] parti = json.getAsString().split(SPAZIO);
        int mese = AEMese.getNumMese(parti[0]);
        int giorno = Integer.parseInt(text.levaCoda(parti[1], VIRGOLA));
        int anno = Integer.parseInt(text.levaCoda(parti[2], VIRGOLA));

        return LocalDate.of(anno, mese, giorno);
    }

    /**
     * Deserializza un elemento temporale
     *
     * @param json da cui estrarre l' elemento temporale
     *
     * @return elemento temporale
     */
    public LocalTime deserializeLocalTime(JsonElement json) {
        String[] parti = json.getAsString().split(SPAZIO);
        parti = parti[3].split(DUE_PUNTI);

        return LocalTime.of(Integer.parseInt(parti[0]), Integer.parseInt(parti[1]), Integer.parseInt(parti[2]));
    }

}