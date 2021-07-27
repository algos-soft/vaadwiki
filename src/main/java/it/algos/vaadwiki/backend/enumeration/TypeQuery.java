package it.algos.vaadwiki.backend.enumeration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 27-lug-2021
 * Time: 09:25
 */
public enum TypeQuery {
    get("GET"),
    getCookies("GET con loginCookies"),
    post("POST"),
    login("preliminary GET + POST"),
    ;

    private  String tag;


     TypeQuery(String tag) {
        this.tag = tag;
    }

    public String get() {
        return tag;
    }
}
