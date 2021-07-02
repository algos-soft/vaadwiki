package it.algos.vaadflow14.wiki;

import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 01-lug-2021
 * Time: 10:25
 * <p>
 * Semplice wrapper per i dati essenziali di un elemento di una categoria di MediaWiki <br>
 */
public class WrapCat {

    private long pageid;

    private String title;

    public WrapCat(long pageid, String title) {
        this.pageid = pageid;
        this.title = title;
    }

    public long getPageid() {
        return pageid;
    }

    public String getTitle() {
        return title;
    }

}
