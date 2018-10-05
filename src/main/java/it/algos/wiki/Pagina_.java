package it.algos.wiki;
/**
 * Created by Gac on 05 ago 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */


import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Pagina.class)
public class Pagina_  {
    public static volatile SingularAttribute<Pagina, String> pippo;
}// end of entity class
