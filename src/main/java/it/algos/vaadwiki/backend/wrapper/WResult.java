package it.algos.vaadwiki.backend.wrapper;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadwiki.wiki.*;
import org.springframework.stereotype.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-ago-2021
 * Time: 09:05
 * Semplice wrapper per veicolare una risposta con diverse property <br>
 */
@Component
public class WResult extends AResult {

    private WrapBio wrap;
    private String wikiBio = VUOTA;
    private String summary = VUOTA;
    private String newtimestamp = VUOTA;
    private String newtext = VUOTA;
    private long newrevid = 0;
    private boolean modificata = false;

    private WResult() {
        super();
    }

    private WResult(WrapBio wrap) {
        super();
        this.wrap = wrap;
    }

    public static WResult crea(WrapBio wrap) {
        return new WResult(wrap);
    }

    public static WResult errato() {
        WResult wResult = new WResult();
        wResult.setValido(false);
        return wResult;
    }

    public WrapBio getWrap() {
        return wrap;
    }

    public void setWrap(WrapBio wrap) {
        this.wrap = wrap;
    }
    public static WResult valido() {
        return new WResult();
    }

    public String getWikiBio() {
        return wikiBio;
    }

    public void setWikiBio(String wikiBio) {
        this.wikiBio = wikiBio;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNewtimestamp() {
        return newtimestamp;
    }

    public void setNewtimestamp(String newtimestamp) {
        this.newtimestamp = newtimestamp;
    }

    public long getNewrevid() {
        return newrevid;
    }

    public void setNewrevid(long newrevid) {
        this.newrevid = newrevid;
    }

    public boolean isModificata() {
        return modificata;
    }

    public void setModificata(boolean modificata) {
        this.modificata = modificata;
    }

    public String getNewtext() {
        return newtext;
    }

    public void setNewtext(String newtext) {
        this.newtext = newtext;
    }

}
