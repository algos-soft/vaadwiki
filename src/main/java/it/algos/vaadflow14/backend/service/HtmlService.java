package it.algos.vaadflow14.backend.service;

import com.vaadin.flow.component.html.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 16-feb-2021
 * Time: 17:38
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AHtmlService.class); <br>
 * 3) @Autowired public AHtmlService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HtmlService extends AbstractService {


    /**
     * Costruisce uno span semplice <br>
     *
     * @param message da visualizzare
     *
     * @return elemento Span per html
     */
    public Span getSpan(final String message) {
        return getSpan(message, (AIType[]) null);
    }

    /**
     * Costruisce uno span con eventuali property html specifiche <br>
     *
     * @param message  da visualizzare
     * @param typeSpan property html da regolare: Color, Height, Size, Weight...
     *
     * @return elemento Span per html
     */
    public Span getSpan(final String message, final AIType... typeSpan) {
        //        Span span = new Span();
        Span span = getSpanHtml(message);
        String height = AEPreferenza.lineHeight.getStr();

        if (text.isValid(message)) {
            span.setText(message);

            if (typeSpan != null && typeSpan.length > 0) {
                for (AIType type : typeSpan) {
                    if (type != null) {
                        span.getElement().getStyle().set(type.getTag(), type.get());
                    }
                }
            }
            span.getElement().getStyle().set("line-height", height);
        }

        return span;
    }

    /**
     * Costruisce uno span
     *
     * @param message da visualizzare
     *
     * @return elemento per html
     */
    private Span getSpanBase(final AETypeColor typeColor, final String message, final AIType... typeSpan) {
        List<AIType> typeList = new ArrayList(Arrays.asList(typeColor));

        if (typeSpan != null && typeSpan.length > 0) {
            for (AIType type : typeSpan) {
                typeList.add(type);
            }
        }

        return getSpan(message, typeList.toArray(new AIType[typeList.size()]));
    }

    /**
     * Costruisce uno span colorato verde <br>
     *
     * @param message  da visualizzare
     * @param typeSpan property html da regolare: Color, Height, Size, Weight...
     *
     * @return elemento Span per html
     */
    public Span getSpanVerde(final String message, final AIType... typeSpan) {
        return getSpanBase(AETypeColor.verde, message, typeSpan);
    }

    /**
     * Costruisce uno span colorato verde <br>
     *
     * @param message da visualizzare
     *
     * @return elemento Span per html
     */
    public Span getSpanVerde(final String message) {
        return getSpanVerde(message, (AIType[]) null);
    }

    /**
     * Costruisce uno span colorato
     *
     * @param message da visualizzare
     *
     * @return elemento per html
     */
    public Span getSpanBlu(final String message, final AIType... typeSpan) {
        return getSpanBase(AETypeColor.blu, message, typeSpan);
    }

    /**
     * Costruisce uno span colorato
     *
     * @param message da visualizzare
     *
     * @return elemento per html
     */
    public Span getSpanBlu(final String message) {
        return getSpanBlu(message, null);
    }

    /**
     * Costruisce uno span colorato
     *
     * @param message da visualizzare
     *
     * @return elemento per html
     */
    public Span getSpanRosso(final String message, final AIType... typeSpan) {
        return getSpanBase(AETypeColor.rosso, message, typeSpan);
    }

    /**
     * Costruisce uno span colorato
     *
     * @param message da visualizzare
     *
     * @return elemento per html
     */
    public Span getSpanRosso(final String message) {
        return getSpanRosso(message, null);
    }

    /**
     * Costruisce uno span html
     *
     * @param message da visualizzare
     *
     * @return elemento per html
     */
    public Span getSpanHtml(final String message) {
        Span span = new Span();
        String height = AEPreferenza.lineHeight.getStr();

        if (text.isValid(message)) {
            span.setText(message);
            span.getElement().setProperty("innerHTML", message);
            span.getElement().getStyle().set("line-height", height);
        }

        return span;
    }

    /**
     * Contorna il testo con uno span
     *
     * @param testoIn da regolare
     *
     * @return testo contornato da tag iniziale e finale
     */
    private String colore(final String colore, final String testoIn) {
        String testoOut = VUOTA;
        String tagIni = String.format("<span style=\"color:%s\">", colore);
        String tagEnd = "</span>";

        if (text.isValid(testoIn)) {
            testoOut += tagIni;
            testoOut += testoIn;
            testoOut += tagEnd;
        }

        return testoOut;
    }

    /**
     * Contorna il testo con uno span
     *
     * @param testoIn da regolare
     *
     * @return testo contornato da tag iniziale e finale
     */
    public String verde(final String testoIn) {
        return colore("green", testoIn);
    }

    /**
     * Contorna il testo con uno span
     *
     * @param testoIn da regolare
     *
     * @return testo contornato da tag iniziale e finale
     */
    public String blu(final String testoIn) {
        return colore("blue", testoIn);
    }

    /**
     * Contorna il testo con uno span
     *
     * @param testoIn da regolare
     *
     * @return testo contornato da tag iniziale e finale
     */
    public String rosso(final String testoIn) {
        return colore("red", testoIn);
    }

    /**
     * Elimina un tag HTML in testa e coda della stringa. <br>
     * Funziona solo se i tags sono esattamente in TESTA ed in CODA alla stringa <br>
     * Se arriva una stringa vuota, restituisce una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param stringaIn in ingresso
     * @param tag       html iniziale
     *
     * @return stringa con tags eliminati
     */
    public String setNoHtmlTag(String stringaIn, String tag) {
        String stringaOut = stringaIn;
        String tagIni = "<" + tag + ">";
        String tagEnd = "</" + tag + ">";

        if (text.isValid(stringaIn)) {
            stringaIn = stringaIn.trim();

            if (stringaIn.startsWith(tagIni) && stringaIn.endsWith(tagEnd)) {
                stringaOut = stringaIn;
                stringaOut = text.levaCoda(stringaOut, tagEnd);
                stringaOut = text.levaTesta(stringaOut, tagIni);
            }
        }

        return stringaOut.trim();
    }

    /**
     * Contorna il testo con un uno span bold. <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa regolata secondo la property html
     */
    public String bold(String stringaIn) {
        String stringaOut = VUOTA;
        String tagIni = String.format("<span style=\"font-weight:%s\">", AETypeWeight.bold.get());
        String tagEnd = "</span>";

        if (text.isValid(stringaIn)) {
            stringaOut = tagIni;
            stringaOut += stringaIn;
            stringaOut += tagEnd;
        }

        return stringaOut.trim();
    }

    /**
     * Controlla che le occorrenze del tag iniziale e di quello finale si pareggino all'interno del testo. <br>
     * Ordine ed annidamento NON considerato <br>
     *
     * @param testo  da spazzolare
     * @param tagIni tag iniziale
     * @param tagEnd tag finale
     *
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    public boolean isPariTag(final String testo, final String tagIni, final String tagEnd) {
        boolean pari = false;
        int numIni;
        int numEnd;

        // controllo di congruità
        if (testo != null && tagIni != null && tagEnd != null) {
            numIni = getNumTag(testo, tagIni);
            numEnd = getNumTag(testo, tagEnd);
            pari = (numIni == numEnd);
        }

        return pari;
    }

    /**
     * Restituisce il numero di occorrenze di un tag nel testo. <br>
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo <br>
     *
     * @param testo da spazzolare
     * @param tag   da cercare
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    public int getNumTag(final String testo, final String tag) {
        int numTag = 0;
        int pos;

        // controllo di congruità
        if (text.isValid(testo)&&text.isValid(tag)) {
            if (testo.contains(tag)) {
                pos = testo.indexOf(tag);
                while (pos != -1) {
                    pos = testo.indexOf(tag, pos + tag.length());
                    numTag++;
                }
            }
            else {
                numTag = 0;
            }
        }

        return numTag;
    }

}