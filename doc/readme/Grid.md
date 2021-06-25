Grid
======================


##Documentazione
- [Is your Grid too slow?](https://vaadin.com/blog/using-the-right-r)

##Tipi possibili di colonne
In ordine di velocità e difficoltà crescenti:
1) ComponentRenderers (Easy to Build, Slow to Render)
2) TemplateRenderers (Flexible and Fast, if You Can Get Your Hands Dirty with HTML)
3) ValueProvider (The Fastest, but Usable Only for Simple Values)


    // ComponentRenderer
    grid.addComponentColumn(item -> new Span(item));

    // TemplateRenderer
    grid.addColumn(TemplateRenderer.<String> of("[[item.string]]").withProperty("string", item -> item));

    // ValueProvider
    grid.addColumn(ValueProvider.identity()); // same as item -> item
    
##Soluzioni

 1) ComponentRenderer
 
 - Massima libertà e flessibilità di composizione
 - Decisamente più lento
 - Più complesso da scrivere
 - Non supporta le colonne sortable
 
 
    PaginatedGrid grid = new PaginatedGrid<>();
    grid.addComponentColumn(item -> new Span(((Regione)item).ordine+"")).setHeader("#");

 
 2) TemplateRenderer
 
 - Richiede utilizzo di HTML
 - Inutilmente complesso e farraginoso
 - Non particolarmente veloce
 
 3) ValueProvider

- Valori elementari (Text ed Integer)
- Semplicità di realizzazione
- Velocità di esecuzione


    PaginatedGrid grid = new PaginatedGrid<>();
    grid.addColumn(item -> ((Regione)item).ordine).setHeader("#").setSortable(true);
