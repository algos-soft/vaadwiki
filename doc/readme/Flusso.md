Flusso
======================
Flusso degli eventi nelle classi controllate dal programma:

###Setup non-UI logic
- Spring context
- The entry point of the Spring Boot application: metodo `main`(String[] args) nella classe che implementa SpringBootServletInitializer
- Async stack trace (SpringBoot) in cui vengono create da `SpringBoot` tutte le istanze delle classi `Singleton` 


    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
nelle quali vengono iniettati i riferimenti ad altre classi ove previsti 
    
    @Autowired
- `FlowBoot`: un `ContextRefreshEvent` occurs when an ApplicationContext gets initialized or refreshed e viene intercettato da tutte le classi che implementano `ServletContextListener` nel metodo `onContextRefreshEvent`()
    
    
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshEvent() {

###Catena
- xxxApplication.main(String[] args);
- FlowBoot.onContextRefreshEvent();
- SimpleBoot.fixVariabili();
- SimpleBoot.fixData();
- SimpleBoot.fixMenuRoutes();
- SimpleBoot.fixVersioni();
- ....
-  UI.getCurrent().navigate()
- Logic.setParameter(final BeforeEvent beforeEvent, @OptionalParameter String bodyTextUTF8)
- Logic.beforeEnter(BeforeEnterEvent beforeEnterEvent)

###Browser call


#Analisi
####Spring context
####Application 
####@EventListener
####FlowBoot
- Inizializzazione di alcuni parametri del database mongoDB
- (?) Riferimento alla sottoclasse specifica per utilizzare il metodo sovrascritto resetPreferenze()
- Inizializzazione dei dati di alcune collections essenziali per la partenza
- Crea le preferenze standard, se non esistono
- Regola alcune variabili generali dell' applicazione al loro valore iniziale di default
- Regolazione delle preferenze standard effettuata nella sottoclasse specifica
- Inizializzazione dei dati di alcune collections sul DB mongo
- Aggiunge le @Route (view) standard






Nell'architettura di SpringBoot, occorre la classe di partenza col metodo `main`.

- La prima classe intercettabile nel flusso è

##Profiling
javamelody
    
    /monitoring
##Link
- running-code-on-spring-boot-startup [guru](https://springframework.guru/running-code-on-spring-boot-startup/)