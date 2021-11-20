package it.algos.vaadflow14.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 13-nov-2021
 * Time: 19:57
 */
public class MenuItemInfo {

    private String text;

    private String iconClass;

    private Class<? extends Component> view;

    public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
        this.text = text;
        this.iconClass = iconClass;
        this.view = view;
    }

    public String getText() {
        return text;
    }

    public String getIconClass() {
        return iconClass;
    }

    public Class<? extends Component> getView() {
        return view;
    }

}

