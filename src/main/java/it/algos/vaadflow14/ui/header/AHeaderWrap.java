package it.algos.vaadflow14.ui.header;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.application.FlowVar;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: dom, 03-mag-2020
 * Time: 17:48
 * Visualizza una serie di messaggi di avviso come header della view. <br>
 * I messaggi sono divisi e visibili SOLO per i vari livelli di utenza (dev, admin, user) <br>
 * Se l' applicazione NON usa la 'security', gli avvisi sono visibili a tutti <br>
 * I messaggi sono colorati in maniera diversa per ogni gruppo <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AHeaderWrap extends AHeader {


    /**
     * Costruttore base senza parametri <br>
     * Non usa @Autowired perché l' istanza viene creata con appContext.getBean(AHeader.class) <br>
     */
    public AHeaderWrap() {
    }


    /**
     * Costruttore base con parametro <br>
     * Non usa @Autowired perché l' istanza viene creata con appContext.getBean(AHeader.class, alertWrap) <br>
     */
    public AHeaderWrap(AlertWrap alertWrap) {
        this.alertWrap = alertWrap;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        initView();
    }


    /**
     * Qui va tutta la logica iniziale della view <br>
     */
    protected void fixView() {
        if (array.isAllValid(alertBlack)) {
            for (String alert : alertBlack) {
                this.add(html.getSpan(alert));
            }
        }

        if (array.isAllValid(alertGreen)) {
            for (String alert : alertGreen) {
                this.add(html.getSpanVerde(alert));
            }
        }

        if (array.isAllValid(alertBlue)) {
            for (String alert : alertBlue) {
                this.add(html.getSpanBlu(alert));
            }
        }

        if (array.isAllValid(alertRed)) {
            for (String alert : alertRed) {
                this.add(html.getSpanRosso(alert));
            }
        }

        if (alertUser != null) {
            for (String alert : alertUser) {
                this.add(FlowVar.usaSecurity ? text.getLabelUser(alert) : text.getLabelAdmin(alert));
            }
        }
        if (alertAdmin != null) {
            for (String alert : alertAdmin) {
                this.add(text.getLabelAdmin(alert));
            }
        }
        if (alertDev != null) {
            for (String alert : alertDev) {
                this.add(FlowVar.usaSecurity ? text.getLabelDev(alert) : text.getLabelAdmin(alert));
            }
        }
        if (alertDevAll != null) {
            for (String alert : alertDevAll) {
                this.add(FlowVar.usaSecurity ? text.getLabelDev(alert) : text.getLabelAdmin(alert));
            }
        }
        if (alertParticolare != null) {
            for (String alert : alertParticolare) {
                this.add(FlowVar.usaSecurity ? text.getLabelDev(alert) : text.getLabelAdmin(alert));
            }
        }
    }


    public void setAlertWrap(AlertWrap alertWrap) {
        this.alertWrap = alertWrap;
        fixAlertWrap();
        initView();
    }


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaAlertLayout() {
        //        super.creaAlertLayout();
        //
        //        boolean isDeveloper = wamLogin.isDeveloper();
        //        boolean isAdmin = wamLogin.isAdmin();
        //        boolean isUser = !isDeveloper && !isAdmin;
        //
        //        if (alertUser != null && soloVisioneUser) {
        //            alertUser.add(USER_VISIONE);
        //        }// end of if cycle
        //        if (alertAdmin != null && soloVisioneAdmin) {
        //            alertAdmin.add(ADMIN_VISIONE);
        //            alertAdmin.add(ADMIN_DELETE);
        //        }// end of if cycle
        //        if (alertDev != null) {
        //            alertDev.add(DEVELOPER_DELETE);
        //            alertDev.add(DEVELOPER_IMPORT);
        //        }// end of if cycle
        //        if (alertDevAll != null) {
        //            alertDevAll.add(DEVELOPER_MOSTRA_ALL);
        //            alertDevAll.add(DEVELOPER_DELETE_ALL);
        //        }// end of if cycle
        //
        //        //--sempre (per tutti)
        //        if (alertUser != null && alertUser.size() > 0) {
        //            alertPlacehorder.add(text.getLabelUser(alertUser.get(0)));
        //        }// end of if cycle
        //
        //        //--solo utente
        //        if (isUser || isDeveloper) {
        //            if (alertUser != null) {
        //                for (int k = 1; k < alertUser.size(); k++) {
        //                    alertPlacehorder.add(text.getLabelUser(alertUser.get(k)));
        //                }// end of for cycle
        //            }// end of if cycle
        //        }// end of if cycle
        //
        //        //--solo admin
        //        if (isAdmin || isDeveloper) {
        //            if (alertAdmin != null) {
        //                for (String alert : alertAdmin) {
        //                    alertPlacehorder.add(text.getLabelAdmin(alert));
        //                }// end of for cycle
        //            }// end of if cycle
        //        }// end of if cycle
        //
        //        //--solo developer
        //        if (isDeveloper) {
        //            if (wamLogin != null && wamLogin.getCroce() != null) {
        //                if (alertDev != null) {
        //                    for (String alert : alertDev) {
        //                        alertPlacehorder.add(text.getLabelDev(alert));
        //                    }// end of for cycle
        //                    alertPlacehorder.add(getInfoImport(((WamService) service).lastImport, ((WamService) service).durataLastImport));
        //                }// end of if cycle
        //            } else {
        //                if (alertDevAll != null) {
        //                    for (String alert : alertDevAll) {
        //                        alertPlacehorder.add(text.getLabelDev(alert));
        //                    }// end of for cycle
        //                }// end of if cycle
        //            }// end of if/else cycle
        //
        //            if (alertParticolare != null) {
        //                for (String alert : alertParticolare) {
        //                    alertPlacehorder.add(text.getLabelDev(alert));
        //                }// end of for cycle
        //            }// end of if cycle
        //        }// end of if cycle
    }// end of method

}
