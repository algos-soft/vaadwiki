package it.algos.vaadflow14.backend.enumeration;

import it.algos.vaadflow14.backend.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AEMese {

    gennaio("gennaio", 31, 31, "gen", "Jan"),

    febbraio("febbraio", 28, 29, "feb", "Feb"),

    marzo("marzo", 31, 31, "mar", "Mar"),

    aprile("aprile", 30, 30, "apr", "Apr"),

    maggio("maggio", 31, 31, "mag", "May"),

    giugno("giugno", 30, 30, "giu", "Jun"),

    luglio("luglio", 31, 31, "lug", "Jul"),

    agosto("agosto", 31, 31, "ago", "Aug"),

    settembre("settembre", 30, 30, "set", "Sep"),

    ottobre("ottobre", 31, 31, "ott", "Oct"),

    novembre("novembre", 30, 30, "nov", "Nov"),

    dicembre("dicembre", 31, 31, "dic", "Dec");


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;


    String nome;

    int giorni;

    int giorniBisestili;

    String sigla;

    String siglaEn;


    /**
     * Costruttore interno dell'Enumeration <br>
     */
    AEMese(String nome, int giorni, int giorniBisestili, String sigla, String siglaEn) {
        this.nome = nome;
        this.giorni = giorni;
        this.giorniBisestili = giorniBisestili;
        this.sigla = sigla;
        this.siglaEn = siglaEn;
    }


    /**
     * Numero di giorni di ogni mese <br>
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     * @param anno            l'anno di riferimento (per sapere se è bisestile)
     *
     * @return Numero di giorni del mese
     */
    public static int getGiorni(int numMeseDellAnno, int anno) {
        int giorniDelMese = 0;
        AEMese mese = getMese(numMeseDellAnno);

        if (mese != null) {
            if (!Year.of(anno).isLeap()) {
                giorniDelMese = mese.giorni;
            }
            else {
                giorniDelMese = mese.giorniBisestili;
            }
        }

        return giorniDelMese;
    }


    /**
     * Mese
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     *
     * @return Mese
     */
    public static AEMese getMese(int numMeseDellAnno) {
        AEMese mese = null;

        if (numMeseDellAnno > 0 && numMeseDellAnno < 13) {
            numMeseDellAnno = numMeseDellAnno - 1;
            for (AEMese meseTmp : AEMese.values()) {
                if (meseTmp.ordinal() == numMeseDellAnno) {
                    mese = meseTmp;
                }
            }
        }

        return mese;
    }


    /**
     * Mese
     *
     * @param nomeBreveLungo Nome breve o lungo del mese
     *
     * @return Mese
     */
    public static AEMese getMese(String sigla) {
        AEMese mese = null;

        if (sigla != null && !sigla.equals("")) {
            sigla = sigla.toLowerCase();
            for (AEMese meseTmp : AEMese.values()) {
                if (meseTmp.sigla.equals(sigla) || meseTmp.nome.equals(sigla)) {
                    mese = meseTmp;
                }
            }
        }

        return mese;
    }


    // l'anno parte da gennaio che è il numero 1
    private static String getMese(int ord, boolean flagBreve) {
        String nome = "";
        AEMese mese = null;

        mese = getMese(ord);
        if (mese != null) {
            if (flagBreve) {
                nome = mese.sigla;
            }
            else {
                nome = mese.nome;
            }
        }

        return nome;
    }


    /**
     * Numero del mese nell'anno
     *
     * @param nomeBreveLungo L'anno parte da gennaio che è il mese numero 1
     *
     * @return Numero del mese
     */
    public static int getOrder(String nomeBreveLungo) {
        int numMeseDellAnno = 0;
        AEMese mese = getMese(nomeBreveLungo);

        if (mese != null) {
            numMeseDellAnno = mese.ordinal();
            numMeseDellAnno = numMeseDellAnno + 1;
        }

        return numMeseDellAnno;
    }


    /**
     * Elenco di tutti i nomi in forma breve
     *
     * @return Stringa dei nomi brevi separati da virgola
     */
    public static String getAllShortString() {
        String stringa = "";
        String sep = ", ";

        for (AEMese mese : AEMese.values()) {
            stringa += mese.sigla;
            stringa += sep;
        }
        stringa = stringa.substring(0, stringa.length() - sep.length());

        return stringa;
    }


    /**
     * Elenco di tutti i nomi in forma completa
     *
     * @return Stringa dei nomi completi separati da virgola
     */
    public static String getAllLongString() {
        String stringa = "";
        String sep = ", ";

        for (AEMese mese : AEMese.values()) {
            stringa += mese.nome;
            stringa += sep;
        }
        stringa = stringa.substring(0, stringa.length() - sep.length());

        return stringa;
    }


    /**
     * Elenco di tutti i nomi in forma breve
     *
     * @return Array dei nomi brevi
     */
    public static List<String> getAllShortList() {
        List<String> lista = new ArrayList<String>();

        for (AEMese mese : AEMese.values()) {
            lista.add(mese.sigla);
        }

        return lista;
    }


    /**
     * Elenco di tutti i nomi in forma completa
     *
     * @return Array dei nomi completi
     */
    public static List<String> getAllLongList() {
        List<String> lista = new ArrayList<String>();

        for (AEMese mese : AEMese.values()) {
            lista.add(mese.nome);
        }

        return lista;
    }

    /**
     * Numero del mese dalla sigla inglese <br>
     *
     * @return numero del mese (gennaio=1)
     *
     * @since java 11
     */
    public static int getNumMese(String siglaEn) {
        int num;
        List<AEMese> meseList = Arrays.asList(AEMese.values());
        Stream<AEMese> meseStream = meseList.stream();

        num = meseStream
                .filter(mese -> mese.siglaEn.equals(siglaEn))
                .collect(Collectors.summingInt(AEMese::getOrd));

        return num;
    }


    /**
     * Nome completo del mese
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     *
     * @return Nome breve del mese
     */
    public static String getLong(int numMeseDellAnno) {
        return getMese(numMeseDellAnno, false);
    }


    @Override
    public String toString() {
        return nome;
    }


    public int getOrd() {
        return this.ordinal() + 1;
    }


    public String getNome() {
        return nome;
    }


    public String getSigla() {
        return sigla;
    }


    public int getGiorni() {
        return giorni;
    }


    public int getGiorniBisestili() {
        return giorniBisestili;
    }


    public void setText(TextService text) {
        this.text = text;
    }


    @Component
    public static class MeseServiceInjector {

        @Autowired
        private TextService text;


        @PostConstruct
        public void postConstruct() {
            for (AEMese mese : AEMese.values()) {
                mese.setText(text);
            }
        }

    }
}
