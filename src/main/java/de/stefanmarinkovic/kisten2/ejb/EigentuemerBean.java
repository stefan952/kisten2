/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.ejb;

import de.stefanmarinkovic.kisten2.entity.Eigentuemer;
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
public class EigentuemerBean /*extends NamedEntityBean */{

    private static final Logger logger = Logger.getLogger("kisten2.ejb.EigentuemerBean");

    @PersistenceContext
    private EntityManager em;

    public EigentuemerBean() {
    }

    @PostConstruct
    void init() {
        logger.log(Level.INFO, "in EigentuemerBean.init()");
    }

    /**
     *
     * @param eigentuemer
     * @return eigentuemer wird nur gelöscht, wenn er keine Inhalte mehr hat.
     */
    public boolean deleteEigentuemer(Eigentuemer eigentuemer) {
        Eigentuemer managedEigentuemer = em.find(Eigentuemer.class,
                eigentuemer.getId());
        if (managedEigentuemer.getInhalte().isEmpty()) {
            em.remove(managedEigentuemer);
            logger.log(Level.INFO, "eigentuemer {0} gelöscht",
                    managedEigentuemer.getName());
            return true;
        }
        logger.log(Level.INFO, "Eigentümer {0} hat noch Inhalte! ",
                managedEigentuemer.getName());
        return false;
    }

    public boolean eigentuemerExists(String eigentuemerName) {
        return loadEigentuemer()
                .stream()
                .filter(eig -> eig.getName().equalsIgnoreCase(eigentuemerName))
                .findAny()
                .isPresent();
    }

    public boolean eigentuemerExists(Eigentuemer eigentuemer) {
        if (loadEigentuemer()
                .stream()
                .map(eigen -> eigen.getName())
                .filter(name -> name.equalsIgnoreCase(eigentuemer.getName().trim()))
                .collect(Collectors.toList())
                .isEmpty()) {
            logger.log(Level.INFO, "eigentuemer {0} existiert schon",
                    eigentuemer.getName());
            return false;
        }
        return true;
    }

    public List<Eigentuemer> loadEigentuemer() {
        return (List<Eigentuemer>) em.createNamedQuery("findAllEigentuemer").getResultList();
    }

    public Eigentuemer getEigentuemer(String name) {
        Eigentuemer loaded
                = loadEigentuemer()
                        .stream()
                        .filter(eigen -> eigen.getName().equalsIgnoreCase(name.trim()))
                        .findAny()
                        .get();
        logger.log(Level.INFO,"getEigentuemer("+ name +") managed? " + em.contains(loaded));
        //return em.find(Eigentuemer.class,loaded.getId());
        return loaded;
    }

    public Eigentuemer getEigentuemer(Long id) {
        Eigentuemer found = em.find(Eigentuemer.class,id);
        logger.log(Level.INFO,"getEigentuemer("+id+") managed? " + em.contains(found));
        return found;
    }

    /**
     * nur hinzufügen, wenn Eigentümer mit dem Namen noch nicht existiert
     *
     * @param eigentuemer
     * @return
     */
    public boolean addEigentuemer(Eigentuemer eigentuemer) {
        eigentuemer.setName(eigentuemer.getName().trim());
        if (eigentuemerExists(eigentuemer)) {
            return false;
        }
        em.persist(eigentuemer);
        return true;
    }

    public void updateEigentuemer(Eigentuemer eigentuemer) {
        eigentuemer.setName(eigentuemer.getName().trim());
        em.merge(eigentuemer);
    }

    public void updateEigentuemer(List<Eigentuemer> eigentuemer) {
        // wenn eigentümer mit gleichem Namen schon existiert, darf es kein Update geben.
        List<Eigentuemer> savedEs = loadEigentuemer();

        eigentuemer.forEach(eigen -> {
            String name = eigen.getName();
            boolean sameNameAsOther = false;
            for (Eigentuemer savedE : savedEs) {
                if (savedE.getName().equalsIgnoreCase(name)
                        && !savedE.getId().equals(eigen.getId())) {
                    sameNameAsOther = true;

                    logger.log(Level.INFO, "Name des Eigentümers existiert bereits");
                }
            }
            if (!sameNameAsOther) {
                em.merge(eigen);

            }
        });
    }

}
