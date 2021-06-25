Wiring
======================
- Dependency Injection is a fundamental aspect of the Spring framework, through which the Spring container "injects" objects into other objects or "dependencies".

- Simply put, this allows for loose coupling of components and moves the responsibility of managing components onto the container.

Annotations related to dependency injection, namely the annotations:
- @Resource
- @Inject
- @Autowired

These annotations provide classes with a declarative way to resolve dependencies
- If the mandate is for all dependencies to be handled by the Spring Framework, the only choice is the @Autowired annotation.

##@Autowired on setter
- Nel caso di una classe con sottoclassi che potrebbero avere costruttori con parametri differenti.
- Nel caso di una classe con molti costruttori.

        /**
         * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
         * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
         * al termine del ciclo init() del costruttore di questa classe <br>
         */
        public Environment environment;
        
        /**
         * Constructor with @Autowired on setter. Usato quando ci sono sottoclassi. <br>
         * Per evitare di avere nel costruttore tutte le property che devono essere iniettate e per poterle aumentare <br>
         * senza dover modificare i costruttori delle sottoclassi, l'iniezione tramite @Autowired <br>
         * viene delegata ad alcuni metodi setter() che vengono qui invocati con valore (ancora) nullo. <br>
         * Al termine del ciclo init() del costruttore il framework SpringBoot/Vaadin, inietterà la relativa istanza <br>
         */
        public FlowBoot() {
            ...
        }// end of constructor with @Autowired on setter
         
        /**
         * Set con @Autowired di una property chiamata dal costruttore <br>
         * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
         * Chiamata dal costruttore di questa classe con valore nullo <br>
         * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
         */
        @Autowired
        public void setEnvironment(Environment environment) {

###Link
- Wiring in Spring [Baeldung](https://www.baeldung.com/spring-annotations-resource-inject-autowire)
