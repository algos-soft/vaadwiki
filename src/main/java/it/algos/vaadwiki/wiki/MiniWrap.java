package it.algos.vaadwiki.wiki;

import java.time.*;
import java.time.format.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 02-lug-2021
 * Time: 13:48
 */
public class MiniWrap {

    private long pageid;

    private LocalDateTime lastModifica;

    public MiniWrap(long pageid, String lastModificaString) {
        this.pageid = pageid;
        this.lastModifica = (lastModificaString != null && lastModificaString.length() > 0) ? LocalDateTime.parse(lastModificaString, DateTimeFormatter.ISO_DATE_TIME) : null;
    }

    public long getPageid() {
        return pageid;
    }

    public LocalDateTime getLastModifica() {
        return lastModifica;
    }

}
