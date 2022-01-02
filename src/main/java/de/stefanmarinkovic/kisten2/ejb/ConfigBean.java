/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.ejb;

import de.stefanmarinkovic.kisten2.entity.Eigentuemer;
import de.stefanmarinkovic.kisten2.entity.Inhalt;
import de.stefanmarinkovic.kisten2.entity.Kategorie;
import de.stefanmarinkovic.kisten2.entity.Kiste;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.logging.Logger;

/**
 *
 * @author stefan
 */
@Singleton
@Startup
public class ConfigBean {

    @PersistenceContext
    private EntityManager em;
    private static final Logger logger = Logger.getLogger("com.ma.kisten2.ejb.ConfigBean");

    @PostConstruct
    public void init() {
        Eigentuemer e1 = new Eigentuemer();
        e1.setName("e1");
        //e1.setId(3L);

        Eigentuemer e2 = new Eigentuemer();
        e2.setName("e2");
        //e2.setId(4L);
        //em.persist(e2);

        Eigentuemer e3 = new Eigentuemer();
        e3.setName("e3");
        //  e3.setId(5L);
        //em.persist(e3);

        Kategorie kat1 = new Kategorie();
        kat1.setName("kat1");
        //kat1.setId(1L);
        //em.persist(kat1);

        Kategorie kat2 = new Kategorie();
        kat2.setName("kat2");
        //kat2.setId(2L);
        //em.persist(kat2);

        Kategorie kat3 = new Kategorie();
        kat3.setName("kat3");
        //kat3.setId(3L);

        
        Kiste k2 = new Kiste(2L, "standort2", 0.2F);
        Kiste k3 = new Kiste(3L, "standort3", 0.3F);
        Kiste k4 = new Kiste(4L, "standort4", 0.4F);
        // setter führen merkwürdigerweise zu SQLIntegrity Violations
        //       k2.setNummer(3L);
        //     k2.setFuellgrad(0.3F);
        //   k2.setStandort("standort3");

      

        Inhalt i2 = new Inhalt();
        //i2.setId(2L);
        i2.setInhalt("inhalt2");
        i2.setEigentuemer(e2);
        i2.setKategorie(kat2);
        i2.setKiste(k4);
        e2.getInhalte().add(i2);
        kat2.getInhalte().add(i2);
        k4.getInhalte().add(i2);

        Inhalt i3 = new Inhalt();
        i3.setInhalt("inhalt3");
        //i3.setId(3L);
        i3.setEigentuemer(e3);
        i3.setKategorie(kat3);
        i3.setKiste(k3);
        e3.getInhalte().add(i3);
        kat3.getInhalte().add(i3);
        k3.getInhalte().add(i3);
/*
        Inhalt i4 = new Inhalt();
        i4.setInhalt("inhalt4");
        //i1.setId(1L);
        i4.setEigentuemer(e1);
        i4.setKategorie(kat1);
        i4.setKiste(k3);
        e1.getInhalte().add(i4);
        kat1.getInhalte().add(i4);
        k3.getInhalte().add(i4);
        
         Inhalt i5 = new Inhalt();
        i5.setInhalt("inhalt5");
        //i1.setId(1L);
        i5.setEigentuemer(e2);
        i5.setKategorie(kat1);
        i5.setKiste(k2);
        e2.getInhalte().add(i5);
        kat1.getInhalte().add(i5);
        k2.getInhalte().add(i5);
        
         Inhalt i6 = new Inhalt();
        i6.setInhalt("inhalt6");
        //i1.setId(1L);
        i6.setEigentuemer(e3);
        i6.setKategorie(kat2);
        i6.setKiste(k2);
        e3.getInhalte().add(i6);
        kat2.getInhalte().add(i6);
        k2.getInhalte().add(i6);
        */
        
        em.persist(e1);
        em.persist(e2);
        em.persist(e3);

        em.persist(kat1);
        em.persist(kat2);
        em.persist(kat3);

        em.persist(k4);
        em.persist(k2);
        em.persist(k3);

        
        em.persist(i2);
        em.persist(i3);
       // em.persist(i4);
        //em.persist(i5);
        //em.persist(i6);

    }
}
