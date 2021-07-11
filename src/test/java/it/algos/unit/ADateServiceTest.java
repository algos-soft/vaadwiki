package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.enumeration.AETypeData;
import it.algos.vaadflow14.backend.wrapper.APeriodo;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 06-giu-2020
 * Time: 19:18
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("ADateServiceTest")
@DisplayName("Unit test sulle date")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ADateServiceTest extends ATest {

    public static final String SEP = "  ->  ";

    //    protected final static Date DATE_TRE = new Date(1412485920000L); // 5 ottobre 2014, 7 e 12

    //    protected final static Date DATE_QUATTRO = new Date(1394259124000L); // 8 marzo 2014, 7 e 12 e 4

    protected final static LocalDate LOCAL_DATE_VUOTA = LocalDate.of(1970, 1, 1);

    protected final static LocalDate LOCAL_DATE_PRIMO_VALIDO = LocalDate.of(1970, 1, 2);

    protected final static LocalDate LOCAL_DATE_OLD = LocalDate.of(1946, 10, 28);

    protected final static LocalDateTime LOCAL_DATE_TIME_VUOTA = LocalDateTime.of(1970, 1, 1, 0, 0);

    protected final static LocalDateTime LOCAL_DATE_TIME_PRIMO_VALIDO = LocalDateTime.of(1970, 1, 1, 0, 1);

    protected final static LocalDateTime LOCAL_DATE_TIME_OLD = LocalDateTime.of(1946, 10, 28, 0, 0);

    protected final static LocalTime LOCAL_TIME_TRE = LocalTime.of(22, 0);

    protected final static LocalTime LOCAL_TIME_QUATTRO = LocalTime.of(6, 0);

    protected final static LocalTime LOCAL_TIME_VUOTO = LocalTime.of(0, 0);


    // alcune date di riferimento
    protected static Date DATE_UNO;

    protected static Date DATE_DUE;

    protected static Date DATE_UNO_MEZZANOTTE;

    protected static Date DATE_DUE_MEZZANOTTE;

    protected static LocalDate LOCAL_DATE_UNO;

    protected static LocalDate LOCAL_DATE_DUE;

    protected static LocalDate LOCAL_DATE_TRE;

    protected static LocalDate LOCAL_DATE_QUATTRO;

    protected static LocalDateTime LOCAL_DATE_TIME_UNO;

    protected static LocalDateTime LOCAL_DATE_TIME_DUE;

    protected static LocalDateTime LOCAL_DATE_TIME_UNO_MEZZANOTTE;

    protected static LocalDateTime LOCAL_DATE_TIME_DUE_MEZZANOTTE;

    protected static LocalTime LOCAL_TIME_UNO;

    protected static LocalTime LOCAL_TIME_DUE;

    // alcuni parametri utilizzati
    private Date dataPrevista = null;

    private Date dataOttenuta = null;

    private LocalDate localData = null;

    private LocalDate localDataPrevista = null;

    private LocalDate localDataOttenuta = null;

    private LocalDateTime localDateTimePrevista = null;

    private LocalDateTime localDateTimeOttenuta = null;

    private LocalTime localTimePrevisto = null;

    private LocalTime localTimeOttenuto = null;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);

        int delta = 1900;
        DATE_UNO = new Date(2014 - delta, 9, 21, 8, 52, 44);
        DATE_DUE = new Date(2017 - delta, 3, 18, 14, 7, 20);
        DATE_UNO_MEZZANOTTE = new Date(2014 - delta, 9, 21, 0, 0, 0);
        DATE_DUE_MEZZANOTTE = new Date(2017 - delta, 3, 18, 0, 0, 0);

        LOCAL_DATE_UNO = LocalDate.of(2014, 10, 21);
        LOCAL_DATE_DUE = LocalDate.of(2017, 4, 18);
        LOCAL_DATE_TRE = LocalDate.of(2015, 10, 21);
        LOCAL_DATE_QUATTRO = LocalDate.of(2015, 4, 18);

        LOCAL_DATE_TIME_UNO = LocalDateTime.of(2014, 10, 21, 8, 52, 44);
        LOCAL_DATE_TIME_DUE = LocalDateTime.of(2017, 4, 18, 14, 7, 20);
        LOCAL_DATE_TIME_UNO_MEZZANOTTE = LocalDateTime.of(2014, 10, 21, 0, 0, 0);
        LOCAL_DATE_TIME_DUE_MEZZANOTTE = LocalDateTime.of(2017, 4, 18, 0, 0, 0);

        LOCAL_TIME_UNO = LocalTime.of(8, 52, 44);
        LOCAL_TIME_DUE = LocalTime.of(14, 7, 20);
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }


    @Test
    @Order(1)
    @DisplayName("Deprecated - Trasforma Date in LocalDate")
    void dateToLocalDate() {
        String perdita = " (Con perdita di dati)";
        String senza = " (Senza perdita di dati)";
        System.out.println("Deprecated - Convert java.util.Date to java.time.LocalDate");
        System.out.println("");

        localDataPrevista = LOCAL_DATE_UNO;
        localDataOttenuta = date.dateToLocalDate(DATE_UNO);
        assertEquals(localDataPrevista, localDataOttenuta);
        System.out.println(DATE_UNO + SEP + localDataOttenuta + perdita);

        localDataPrevista = LOCAL_DATE_DUE;
        localDataOttenuta = date.dateToLocalDate(DATE_DUE);
        assertEquals(localDataPrevista, localDataOttenuta);
        System.out.println(DATE_DUE + SEP + localDataOttenuta + perdita);

        localDataPrevista = LOCAL_DATE_UNO;
        localDataOttenuta = date.dateToLocalDate(DATE_UNO_MEZZANOTTE);
        assertEquals(localDataPrevista, localDataOttenuta);
        System.out.println(DATE_UNO_MEZZANOTTE + SEP + localDataOttenuta + senza);

        localDataPrevista = LOCAL_DATE_DUE;
        localDataOttenuta = date.dateToLocalDate(DATE_DUE_MEZZANOTTE);
        assertEquals(localDataOttenuta, localDataPrevista);
        System.out.println(DATE_DUE_MEZZANOTTE + SEP + localDataOttenuta + senza);
    }


    @Test
    @Order(2)
    @DisplayName("Deprecated - Trasforma LocalDate in Date")
    void localDateToDate() {
        String senza = " (Senza perdita di dati)";
        System.out.println("Deprecated - Convert java.time.LocalDate to java.util.Date");
        System.out.println("");

        dataPrevista = DATE_UNO_MEZZANOTTE;
        dataOttenuta = date.localDateToDate(LOCAL_DATE_UNO);
        assertEquals(dataPrevista, dataOttenuta);
        System.out.println(LOCAL_DATE_UNO + SEP + dataOttenuta + senza);

        dataPrevista = DATE_DUE_MEZZANOTTE;
        dataOttenuta = date.localDateToDate(LOCAL_DATE_DUE);
        assertEquals(dataPrevista, dataOttenuta);
        System.out.println(LOCAL_DATE_DUE + SEP + dataOttenuta + senza);
    }


    @Test
    @Order(3)
    @DisplayName("Deprecated - Trasforma LocalDateTime in Date")
    void localDateTimeToDate() {
        String senza = " (Senza perdita di dati)";
        System.out.println("Deprecated - Convert java.time.LocalDateTime to java.util.Date");
        System.out.println("");

        dataPrevista = DATE_UNO;
        dataOttenuta = date.localDateTimeToDate(LOCAL_DATE_TIME_UNO);
        assertEquals(dataPrevista, dataOttenuta);
        System.out.println(LOCAL_DATE_TIME_UNO + SEP + dataOttenuta + senza);

        dataPrevista = DATE_DUE;
        dataOttenuta = date.localDateTimeToDate(LOCAL_DATE_TIME_DUE);
        assertEquals(dataPrevista, dataOttenuta);
        System.out.println(LOCAL_DATE_TIME_DUE + SEP + dataOttenuta + senza);

        dataPrevista = DATE_UNO_MEZZANOTTE;
        dataOttenuta = date.localDateTimeToDate(LOCAL_DATE_TIME_UNO_MEZZANOTTE);
        assertEquals(dataPrevista, dataOttenuta);
        System.out.println(LOCAL_DATE_TIME_UNO_MEZZANOTTE + SEP + dataOttenuta + senza);

        dataPrevista = DATE_DUE_MEZZANOTTE;
        dataOttenuta = date.localDateTimeToDate(LOCAL_DATE_TIME_DUE_MEZZANOTTE);
        assertEquals(dataPrevista, dataOttenuta);
        System.out.println(LOCAL_DATE_TIME_DUE_MEZZANOTTE + SEP + dataOttenuta + senza);
    }


    @Test
    @Order(4)
    @DisplayName("Deprecated - Trasforma LocalDate in LocalDateTime")
    void localDateToLocalDateTime() {
        String senza = " (Senza perdita di dati, con il tempo regolato a mezzanotte)";
        System.out.println("Deprecated - Convert java.time.LocalDate to java.time.LocalDateTime");
        System.out.println("");

        localDateTimePrevista = LOCAL_DATE_TIME_UNO_MEZZANOTTE;
        localDateTimeOttenuta = date.localDateToLocalDateTime(LOCAL_DATE_UNO);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
        System.out.println(LOCAL_DATE_UNO + SEP + localDateTimeOttenuta + senza);

        localDateTimePrevista = LOCAL_DATE_TIME_DUE_MEZZANOTTE;
        localDateTimeOttenuta = date.localDateToLocalDateTime(LOCAL_DATE_DUE);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
        System.out.println(LOCAL_DATE_DUE + SEP + localDateTimeOttenuta + senza);
    }


    @Test
    @Order(5)
    @DisplayName("5 - dateToLocalDateTime")
    void dateToLocalDateTime() {
        String senza = " (Senza perdita di dati)";
        System.out.println("Deprecated - Convert java.time.Date to java.time.LocalDateTime");
        System.out.println("");

        localDateTimePrevista = LOCAL_DATE_TIME_UNO;
        localDateTimeOttenuta = date.dateToLocalDateTime(DATE_UNO);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
        System.out.println(DATE_UNO + SEP + localDateTimeOttenuta + senza);

        localDateTimePrevista = LOCAL_DATE_TIME_DUE;
        localDateTimeOttenuta = date.dateToLocalDateTime(DATE_DUE);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
        System.out.println(DATE_DUE + SEP + localDateTimeOttenuta + senza);

        localDateTimePrevista = LOCAL_DATE_TIME_UNO_MEZZANOTTE;
        localDateTimeOttenuta = date.dateToLocalDateTime(DATE_UNO_MEZZANOTTE);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
        System.out.println(DATE_UNO_MEZZANOTTE + SEP + localDateTimeOttenuta + senza);

        localDateTimePrevista = LOCAL_DATE_TIME_DUE_MEZZANOTTE;
        localDateTimeOttenuta = date.dateToLocalDateTime(DATE_DUE_MEZZANOTTE);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
        System.out.println(DATE_DUE_MEZZANOTTE + SEP + localDateTimeOttenuta + senza);
    }


    @Test
    @Order(6)
    @DisplayName("6 - localDateTimeToLocalDate")
    void localDateTimeToLocalDate() {
        String perdita = " (Con perdita di dati)";
        String senza = " (Senza perdita di dati)";
        System.out.println("Convert java.util.LocalDateTime to java.time.LocalDate");
        System.out.println("");

        localDataPrevista = LOCAL_DATE_UNO;
        localDataOttenuta = date.localDateTimeToLocalDate(LOCAL_DATE_TIME_UNO);
        assertEquals(localDataPrevista, localDataOttenuta);
        System.out.println(LOCAL_DATE_TIME_UNO + SEP + localDataOttenuta + perdita);

        localDataPrevista = LOCAL_DATE_DUE;
        localDataOttenuta = date.localDateTimeToLocalDate(LOCAL_DATE_TIME_DUE);
        assertEquals(localDataPrevista, localDataOttenuta);
        System.out.println(LOCAL_DATE_TIME_DUE + SEP + localDataOttenuta + perdita);

        localDataPrevista = LOCAL_DATE_UNO;
        localDataOttenuta = date.localDateTimeToLocalDate(LOCAL_DATE_TIME_UNO_MEZZANOTTE);
        assertEquals(localDataPrevista, localDataOttenuta);
        System.out.println(LOCAL_DATE_TIME_UNO_MEZZANOTTE + SEP + localDataOttenuta + senza);

        localDataPrevista = LOCAL_DATE_DUE;
        localDataOttenuta = date.localDateTimeToLocalDate(LOCAL_DATE_TIME_DUE_MEZZANOTTE);
        assertEquals(localDataPrevista, localDataOttenuta);
        System.out.println(LOCAL_DATE_TIME_DUE_MEZZANOTTE + SEP + localDataOttenuta + senza);
    }


    @Test
    @Order(7)
    @DisplayName("7 - Testo from ISO LocalDate")
    void getISO() {
        System.out.println("Print LocalDate in formato ISO");
        System.out.println("");

        previsto = "2014-10-21T00:00:00";
        ottenuto = date.getISO(LOCAL_DATE_UNO);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_UNO + SEP + previsto);

        previsto = "2017-04-18T00:00:00";
        ottenuto = date.getISO(LOCAL_DATE_DUE);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_DUE + SEP + previsto);

        previsto = "2015-10-21T00:00:00";
        ottenuto = date.getISO(LOCAL_DATE_TRE);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_TRE + SEP + previsto);

        previsto = "2015-04-18T00:00:00";
        ottenuto = date.getISO(LOCAL_DATE_QUATTRO);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_QUATTRO + SEP + previsto);
    }


    @Test
    @Order(8)
    @DisplayName("8 - Testo from ISO LocalDateTime")
    void getISO2() {
        System.out.println("Print LocalDateTime in formato ISO");
        System.out.println("");

        previsto = "2014-10-21T08:52:44";
        ottenuto = date.getISO(LOCAL_DATE_TIME_UNO);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_TIME_UNO + SEP + previsto);

        previsto = "2017-04-18T14:07:20";
        ottenuto = date.getISO(LOCAL_DATE_TIME_DUE);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_TIME_DUE + SEP + previsto);

        previsto = "2014-10-21T00:00:00";
        ottenuto = date.getISO(LOCAL_DATE_TIME_UNO_MEZZANOTTE);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_TIME_UNO_MEZZANOTTE + SEP + previsto);

        previsto = "2017-04-18T00:00:00";
        ottenuto = date.getISO(LOCAL_DATE_TIME_DUE_MEZZANOTTE);
        assertEquals(previsto, ottenuto);
        System.out.println(LOCAL_DATE_TIME_DUE_MEZZANOTTE + SEP + previsto);
    }


    @Test
    @Order(9)
    @DisplayName("9 - localDateFromISO")
    void localDateFromISO() {
        localDataPrevista = LOCAL_DATE_UNO;
        sorgente = date.getISO(LOCAL_DATE_UNO);
        localDataOttenuta = date.localDateFromISO(sorgente);
        assertEquals(localDataPrevista, localDataOttenuta);
    }


    @Test
    @Order(10)
    @DisplayName("10 - localDateTimeFromISO")
    void localDateTimeFromISO() {
        localDateTimePrevista = LOCAL_DATE_TIME_UNO;
        sorgente = date.getISO(LOCAL_DATE_TIME_UNO);
        localDateTimeOttenuta = date.localDateTimeFromISO(sorgente);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
    }


    @Test
    @Order(11)
    @DisplayName("11 - Estrae l' orario dalla data")
    void localDateTimeToLocalTime() {
        System.out.println("Print orario estratto dalla data");
        System.out.println("");

        localTimePrevisto = LOCAL_TIME_UNO;
        localTimeOttenuto = date.localDateTimeToLocalTime(LOCAL_DATE_TIME_UNO);
        assertEquals(localTimePrevisto, localTimeOttenuto);
        System.out.println(LOCAL_DATE_TIME_UNO + SEP + localTimeOttenuto);
    }


    @Test
    @Order(12)
    @DisplayName("12 - Data corrente in formato standard")
    void get() {
        System.out.println("Print data corrente in formato standard");
        System.out.println("");

        ottenuto = date.get();
        Assertions.assertNotNull(ottenuto);
        System.out.println(ottenuto);
    }


    @Test
    @Order(13)
    @DisplayName("13 - Altre date in formato standard")
    void get2() {
        System.out.println("Print alcune date in formato standard");
        System.out.println("");

        ottenuto = date.get(LOCAL_DATE_UNO);
        Assertions.assertNotNull(ottenuto);
        System.out.println(ottenuto);

        ottenuto = date.get(LOCAL_DATE_DUE);
        Assertions.assertNotNull(ottenuto);
        System.out.println(ottenuto);

        ottenuto = date.get(LOCAL_DATE_TRE);
        Assertions.assertNotNull(ottenuto);
        System.out.println(ottenuto);

        ottenuto = date.get(LOCAL_DATE_QUATTRO);
        Assertions.assertNotNull(ottenuto);
        System.out.println(ottenuto);

        ottenuto = date.get(LOCAL_DATE_VUOTA);
        Assertions.assertNotNull(ottenuto);
        System.out.println(ottenuto);

        ottenuto = date.get(LOCAL_DATE_PRIMO_VALIDO);
        Assertions.assertNotNull(ottenuto);
        System.out.println(ottenuto);
    }


    @Test
    @Order(14)
    @DisplayName("14 - Date con pattern variabili")
    void get3() {
        System.out.println("Date con pattern variabili");
        System.out.println("");
        String tag = ": ";

        System.out.println("");
        for (AETypeData pattern : AETypeData.values()) {
            if (pattern.isSenzaTime()) {
                ottenuto = date.get(LOCAL_DATE_DUE, pattern);
            }
            else {
                ottenuto = date.get(LOCAL_DATE_TIME_DUE, pattern);
            }
            System.out.println(pattern.getTag() + tag + ottenuto);
        }

        System.out.println("");
        for (AETypeData pattern : AETypeData.values()) {
            if (pattern.isSenzaTime()) {
                ottenuto = date.get(LOCAL_DATE_UNO, pattern);
            }
            else {
                ottenuto = date.get(LOCAL_DATE_TIME_UNO, pattern);
            }
            System.out.println(pattern.getTag() + tag + ottenuto);
        }
    }


    @Test
    @Order(15)
    @DisplayName("15 - Formati principali con metodi diretti")
    void get4() {
        System.out.println("Data nei formati più usati");
        System.out.println("--------------------------");
        String tag = ": ";

        ottenuto = date.getCorta(LOCAL_DATE_DUE);
        System.out.println(AETypeData.dateShort.getTag() + tag + ottenuto);

        ottenuto = date.getNormale(LOCAL_DATE_DUE);
        System.out.println(AETypeData.dateNormal.getTag() + tag + ottenuto);

        ottenuto = date.getLunga(LOCAL_DATE_DUE);
        System.out.println(AETypeData.dateLong.getTag() + tag + ottenuto);

        ottenuto = date.getCompleta(LOCAL_DATE_DUE);
        System.out.println(AETypeData.dataCompleta.getTag() + tag + ottenuto);

        System.out.println("");
        System.out.println("");
        System.out.println("Data e orario nel formato normale e completo");
        System.out.println("--------------------------------------------");
        ottenuto = date.getDataOrario(LOCAL_DATE_TIME_UNO);
        System.out.println(AETypeData.normaleOrario.getTag() + tag + ottenuto);

        ottenuto = date.getDataOrarioCompleta(LOCAL_DATE_TIME_UNO);
        System.out.println(AETypeData.completaOrario.getTag() + tag + ottenuto);

        ottenuto = date.getDataOrario(LOCAL_DATE_TIME_DUE);
        System.out.println(AETypeData.normaleOrario.getTag() + tag + ottenuto);

        ottenuto = date.getDataOrarioCompleta(LOCAL_DATE_TIME_DUE);
        System.out.println(AETypeData.completaOrario.getTag() + tag + ottenuto);

        System.out.println("");
        System.out.println("");
        System.out.println("Orario nel formato standard");
        System.out.println("---------------------------");
        ottenuto = date.getOrario(LOCAL_DATE_TIME_UNO);
        System.out.println(LOCAL_DATE_TIME_UNO + SEP + ottenuto);

        ottenuto = date.getOrario(LOCAL_DATE_TIME_DUE);
        System.out.println(LOCAL_DATE_TIME_DUE + SEP + ottenuto);

        ottenuto = date.getOrario(LOCAL_DATE_TIME_UNO_MEZZANOTTE);
        System.out.println(LOCAL_DATE_TIME_UNO_MEZZANOTTE + SEP + ottenuto);

        ottenuto = date.getOrario(LOCAL_DATE_TIME_DUE_MEZZANOTTE);
        System.out.println(LOCAL_DATE_TIME_DUE_MEZZANOTTE + SEP + ottenuto);

        System.out.println("");
        System.out.println("");
        System.out.println("Orario nel formato completo");
        System.out.println("---------------------------");
        ottenuto = date.getOrarioCompleto(LOCAL_DATE_TIME_UNO);
        System.out.println(LOCAL_DATE_TIME_UNO + SEP + ottenuto);

        ottenuto = date.getOrarioCompleto(LOCAL_DATE_TIME_DUE);
        System.out.println(LOCAL_DATE_TIME_DUE + SEP + ottenuto);
    }

    @Test
    @Order(16)
    @DisplayName("16 - Classe APeriodo")
    void periodo() {
        APeriodo periodo = null;

        try {
            periodo = new APeriodo(LOCAL_DATE_DUE, LOCAL_DATE_UNO);
            System.out.println("Inizio: " + date.get(periodo.getInizio()));
            System.out.println("Fine: " + date.get(periodo.getFine()));
        } catch (Exception unErrore) {
            System.out.println(unErrore);
            System.out.println(VUOTA);
        }

        try {
            periodo = new APeriodo(LOCAL_DATE_UNO, LOCAL_DATE_DUE);
            System.out.println("Inizio: " + date.get(periodo.getInizio()));
            System.out.println("Fine: " + date.get(periodo.getFine()));
            System.out.println(VUOTA);
        } catch (Exception unErrore) {
            System.out.println(unErrore);
            System.out.println(VUOTA);
        }

        try {
            periodo = new APeriodo(LOCAL_DATE_UNO, LOCAL_DATE_UNO);
            System.out.println("Ammesso il periodo di durata nulla");
            System.out.println("Inizio: " + date.get(periodo.getInizio()));
            System.out.println("Fine: " + date.get(periodo.getFine()));
            System.out.println(VUOTA);
        } catch (Exception unErrore) {
            System.out.println(unErrore);
            System.out.println(VUOTA);
        }

    }


    @Test
    @Order(90)
    @DisplayName("Costruisce tutti i giorni dell' anno")
    void getAllGiorni() {
        System.out.println("Costruisce tutti i giorni dell'anno");
        String sep = " - ";

        List<HashMap> ottenutoGiorni = date.getAllGiorni();

        for (HashMap mappa : ottenutoGiorni) {
            System.out.print(mappa.get(KEY_MAPPA_GIORNI_NOME));
            System.out.print(sep);
            System.out.print(mappa.get(KEY_MAPPA_GIORNI_NORMALE));
            System.out.print(sep);
            System.out.print(mappa.get(KEY_MAPPA_GIORNI_BISESTILE));
            System.out.println(VUOTA);
        }
    }


    @Test
    @Order(91)
    @DisplayName("Anno bisestile")
    void bisestile() {
        ottenutoBooleano = date.bisestile(1);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(2);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(3);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(4);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(5);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(6);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(7);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(8);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(9);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(10);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1400);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1500);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1896);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1996);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1700);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1800);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1900);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(1600);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = date.bisestile(2000);
        assertTrue(ottenutoBooleano);
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa ad termine di ogni singolo test <br>
     */
    @AfterAll
    void tearDownAll() {
    }

}