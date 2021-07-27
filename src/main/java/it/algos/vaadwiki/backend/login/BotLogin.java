package it.algos.vaadwiki.backend.login;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadwiki.backend.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 08-lug-2021
 * Time: 18:31
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BotLogin {

    private boolean bot = false;

    private AIResult result;
    /**
     * Property indispensabile ricevuta da QueryLogin <br>
     */
    private long lguserid;

    /**
     * Property indispensabile ricevuta da QueryLogin <br>
     */
    private String lgusername;

    private AETypeUser userType;

    public boolean isBot() {
        return bot;
    }

    public boolean nonCollegato() {
        return !isBot();
    }

    public AETypeUser getUserType() {
        return isBot() ? AETypeUser.bot : AETypeUser.anonymous;
    }

    public Map getCookies() {
        return result!=null?result.getMappa():null;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public AIResult getResult() {
        return result;
    }

    public long getLguserid() {
        return lguserid;
    }

    public void setLguserid(long lguserid) {
        this.lguserid = lguserid;
    }

    public String getLgusername() {
        return lgusername;
    }

    public void setLgusername(String lgusername) {
        this.lgusername = lgusername;
    }

    public void setResult(AIResult result) {
        this.result = result;
    }

}
