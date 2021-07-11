package it.algos.vaadflow14.wiki;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

import java.time.*;
import java.time.format.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 20-giu-2021
 * Time: 07:19
 * <p>
 * Semplice wrapper per i dati essenziali di una pagina recuperata da MediaWiki <br>
 */
public class WrapPage {

    private AETypePage type;

    private long pageid;

    private String title;

    private String text;

    private String domain;

    private LocalDateTime time;

    private String tmpl;

    private boolean valida;


    public WrapPage(final String domain, final String title, final AETypePage type) {
        this(domain, 0, title, VUOTA, VUOTA, type);
    }


    public WrapPage(final String domain, final long pageid, final String title, final String text, final String stringTimestamp, final AETypePage type) {
        this.domain = domain;
        this.pageid = pageid;
        this.title = title;
        this.text = type == AETypePage.testoSenzaTmpl ? text : VUOTA;
        this.tmpl = type == AETypePage.testoConTmpl ? text : VUOTA;
        this.time = (stringTimestamp != null && stringTimestamp.length() > 0) ? LocalDateTime.parse(stringTimestamp, DateTimeFormatter.ISO_DATE_TIME) : null;
        this.type = type;
        this.valida = (type == AETypePage.testoSenzaTmpl || type == AETypePage.testoConTmpl);
    }


    public String getDomain() {
        return domain;
    }

    public long getPageid() {
        return pageid;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public AETypePage getType() {
        return type;
    }

    public String getTmpl() {
        return tmpl;
    }

    public boolean isValida() {
        return valida;
    }

}
