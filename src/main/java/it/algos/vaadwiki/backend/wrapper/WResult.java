package it.algos.vaadwiki.backend.wrapper;

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

    private WResult() {
        super();
    }

    private WResult(WrapBio wrap) {
        super();
        this.wrap = wrap;
    }

    public static AIResult crea(WrapBio wrap) {
        return new WResult(wrap);
    }

    public WrapBio getWrap() {
        return wrap;
    }

}
