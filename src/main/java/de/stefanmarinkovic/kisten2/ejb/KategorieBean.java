/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.ejb;

import de.stefanmarinkovic.kisten2.entity.Kategorie;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author stefan
 */
@Stateless
public class KategorieBean {

    private static final Logger logger = Logger.getLogger("kisten2.ejb.KategorieBean");

    @PersistenceContext
    private EntityManager em;

    public KategorieBean() {
    }

    @PostConstruct
    void init() {
        logger.log(Level.INFO, "in KategorieBean.init()");
    }

    public boolean addKategorie(Kategorie kategorie) {
        if (kategorieExists(kategorie)) {
            return false;
        }
        em.persist(kategorie);
        return true;
    }

    public boolean deleteKategorie(Kategorie kategorie) {
        Kategorie managedKat = em.find(Kategorie.class, kategorie.getId());
        if (managedKat.getInhalte().isEmpty()) {
            em.remove(managedKat);
            logger.log(Level.INFO, "Kategorie {0} gelöscht", 
                    managedKat.getName());
            return true;
        }
        logger.log(Level.INFO, "Kategorie {0} hat noch Inhalte! ", 
                managedKat.getName());
        return false;
    }

    public void updateKategorien(List<Kategorie> kategorien) {
        List<Kategorie> savedKs = loadKategorien();
        kategorien.forEach(kat -> {
            String name = kat.getName();
            boolean sameNameAsOther = false;
            for (Kategorie savedKat : savedKs) {
                if (savedKat.getName().equalsIgnoreCase(name) && !savedKat.getId().equals(kat.getId())) {
                    sameNameAsOther = true;
                    logger.log(Level.INFO, "Kategorie existiert bereits");
                }
            }
            if (!sameNameAsOther) {
                em.merge(kat);
            }
        });
    }

    public List<Kategorie> loadKategorien() {
        return (List<Kategorie>) em.createNamedQuery("findAllCategories").getResultList();
    }

    /**
     * Precondition: für kategorieName existiert eine Kategorie
     *
     * @param kategorieName
     * @return
     */
    public Kategorie getKategorie(String kategorieName) {
        Kategorie result = loadKategorien().stream().filter(kat -> kat.getName().equals(kategorieName)).findAny().get();
        logger.log(Level.INFO, "Kategorie " + result.getName() + " geholt");
        return result;
    }

    public Kategorie getKategorie(Long id) {
        return em.find(Kategorie.class,
                id);
    }

     public List<String> getKategorienNamen() {
        return loadKategorien().stream().map(kat -> kat.getName()).collect(Collectors.toList());
    }
    public boolean kategorieExists(String kategorieName) {
        return loadKategorien().stream().filter(kat -> kat.getName().equalsIgnoreCase(kategorieName)).findAny().isPresent();
    }

    public boolean kategorieExists(Kategorie kategorie) {
        boolean present = loadKategorien().stream().filter(kat -> kat.getName().equals(kategorie.getName())).findAny().isPresent();
        // if (loadKategorien().stream().filter(kat -> kat.getName().equals(kategorie.getName())).collect(Collectors.toList()).isEmpty()) {
        //     return false;
        //}
        if (present) {
            logger.log(Level.INFO, "Kategorie {0} existiert bereits", kategorie.getName());
            return true;
        }
        return false;
    }
}
