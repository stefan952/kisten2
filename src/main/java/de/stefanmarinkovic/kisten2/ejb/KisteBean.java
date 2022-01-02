/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.ejb;

import de.stefanmarinkovic.kisten2.entity.Kiste;
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
public class KisteBean {

    private static final Logger logger = Logger.getLogger("kisten2.ejb.KisteBean");

    @PersistenceContext
    private EntityManager em;

    private List<Kiste> kisten;

    public KisteBean() {
    }

    @PostConstruct
    void init() {
        logger.log(Level.INFO, "in KisteBean.init()");
        this.kisten = loadAllKisten();
    }

    public List<Kiste> loadAllKisten() {
        return (List<Kiste>) em.createNamedQuery("findAllKisten").getResultList();
    }

    public List<Long> getKistenNummern() {
        return loadAllKisten().stream().map(kiste -> kiste.getNummer()).collect(Collectors.toList());
    }

    public boolean addKiste(Kiste kiste) {
        if (kisteExists(kiste)) {
            return false;
        }
        em.persist(kiste);
        return true;
    }

    // TODO kistennummer kann nicht mehr geändert werden.
    public void updateKisten(List<Kiste> kisten) {
        List<Kiste> managedKisten = loadAllKisten();

        kisten.forEach(kiste -> {
            em.merge(kiste);
        });
    }

    public boolean deleteKiste(Kiste kiste) {
        Kiste managedKiste = em.find(Kiste.class, kiste.getNummer());
        if (managedKiste.getInhalte().isEmpty()) {
            em.remove(managedKiste);
            logger.log(Level.INFO, "Kiste {0} gelöscht", 
                    String.valueOf(managedKiste.getNummer()));
            return true;
        }
        logger.log(Level.INFO, "Kiste {0} hat noch Inhalte! ", 
                managedKiste.getNummer());
        return false;
    }

    public Kiste getManagedKiste(Long kistenNummer) {
        return em.find(Kiste.class,
                kistenNummer);
    }

    public boolean kisteExists(Long kistenNummer) {
        boolean present = loadAllKisten()
                .stream()
                .filter(ki -> ki.getNummer().equals(kistenNummer))
                .findAny()
                .isPresent();
        if (present) {
            return true;
        }
        return false;
    }

    public boolean kisteExists(Kiste kiste) {
        return kisteExists(kiste.getNummer());
    }
}
