/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.ejb;

import de.stefanmarinkovic.kisten2.entity.Eigentuemer;
import de.stefanmarinkovic.kisten2.entity.Inhalt;
import de.stefanmarinkovic.kisten2.entity.InhaltWrapper;
import de.stefanmarinkovic.kisten2.entity.Kategorie;
import de.stefanmarinkovic.kisten2.entity.Kiste;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefan
 */
@Stateless
public class InhaltBean {

    @EJB
    private KategorieBean kategorieBean;

    @EJB
    private EigentuemerBean eigentuemerBean;

    @EJB
    private KisteBean kisteBean;

    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = Logger.getLogger("kisten2.ejb.InhaltBean");

    public InhaltBean() {
    }

    @PostConstruct
    void init() {
        logger.log(Level.INFO, "in InhaltBean.init()");
    }

    private List<Inhalt> loadInhalteOrderByKistenNummer() {
        return (List<Inhalt>) em.createNamedQuery("findAllInhalteByKistenNummer").getResultList();
    }

    public List<InhaltWrapper> getInhaltWrapperOrderByKistenNummer() {
        return getInhaltWrapper(loadInhalteOrderByKistenNummer());
    }

    private List<Inhalt> loadInhalteOrderByInhalt() {
        return (List<Inhalt>) em.createNamedQuery("findAllInhalteByInhalt").getResultList();
    }

    public List<InhaltWrapper> getInhaltWrapperOrderByInhalt() {
        return getInhaltWrapper(loadInhalteOrderByInhalt());
    }

    public List<Inhalt> loadInhalteOrderByEigentuemer() {
        return (List<Inhalt>) em.createNamedQuery("findAllInhalteByEigentuemer").getResultList();
    }

    public List<InhaltWrapper> getInhaltWrapperOrderByEigentuemer() {
        return getInhaltWrapper(loadInhalteOrderByEigentuemer());
    }

    private List<Inhalt> loadInhalteOrderByFuellgrad() {
        return (List<Inhalt>) em.createNamedQuery("findAllInhalteByFuellgrad").getResultList();
    }

    public List<InhaltWrapper> getInhaltWrapperOrderByFuellgrad() {
        return getInhaltWrapper(loadInhalteOrderByFuellgrad());
    }

    private List<Inhalt> loadInhalteOrderByKategorie() {
        return (List<Inhalt>) em.createNamedQuery("findAllInhalteByKategorie").getResultList();
    }

    public List<InhaltWrapper> getInhaltWrapperOrderByKategorie() {
        return getInhaltWrapper(loadInhalteOrderByKategorie());
    }

    private List<Inhalt> loadInhalteOrderByStandort() {
        return (List<Inhalt>) em.createNamedQuery("findAllInhalteByStandort").getResultList();
    }

    public List<InhaltWrapper> getInhaltWrapperOrderByStandort() {
        return getInhaltWrapper(loadInhalteOrderByStandort());
    }

    /**
     * wird in InhaltBacking gebraucht
     * @return
     */
    public List<Eigentuemer> loadEigentuemer() {
        return eigentuemerBean.loadEigentuemer();
    }

    public InhaltWrapper addInhalt(String inhaltName, Long kistenNummer, String kategorieName, String eigentuemerName) {
        Kiste kiste = kisteBean.getManagedKiste(kistenNummer);
        Kategorie kategorie = kategorieBean.getKategorie(kategorieName);
        Eigentuemer eigentuemer = eigentuemerBean.getEigentuemer(eigentuemerName);

        Inhalt newInhalt = new Inhalt();
        newInhalt.setInhalt(inhaltName);
        newInhalt.setKiste(kiste);
        newInhalt.setKategorie(kategorie);
        newInhalt.setEigentuemer(eigentuemer);

        kiste.getInhalte().add(newInhalt);
        kategorie.getInhalte().add(newInhalt);
        eigentuemer.getInhalte().add(newInhalt);

        em.persist(newInhalt);

        return new InhaltWrapper(newInhalt);
    }

    public void updateInhalte(List<InhaltWrapper> inhaltWrapperList) {
        for (InhaltWrapper iw : inhaltWrapperList) {
            long iwId = iw.getId();
            Inhalt inhalt = em.find(Inhalt.class, iwId);

            // Inhalt
            if (!iw.getInhaltName().equalsIgnoreCase(inhalt.getInhalt())) {
                inhalt.setInhalt(iw.getInhaltName());
            }
            // Kiste
            if (!iw.getKistenNummer().equals(inhalt.getKiste().getNummer())) {
                Kiste neueKiste = kisteBean.getManagedKiste(iw.getKistenNummer());
                Kiste alteKiste = kisteBean.getManagedKiste(inhalt.getKiste().getNummer());
                alteKiste.getInhalte().remove(inhalt);
                inhalt.setKiste(neueKiste);
                neueKiste.getInhalte().add(inhalt);
            }
            if (iw.getFuellgrad() != inhalt.getKiste().getFuellgrad()) {
                inhalt.getKiste().setFuellgrad(iw.getFuellgrad());
            }
            if (!iw.getStandort().equals(inhalt.getKiste().getStandort())) {
                inhalt.getKiste().setStandort(iw.getStandort());
            }
            // Eigentuemer
            if (!iw.getEigentuemerName().equals(inhalt.getEigentuemer().getName())) {
                Eigentuemer alterEigentuemer = eigentuemerBean.getEigentuemer(inhalt.getEigentuemer().getId());
                Eigentuemer neuerEigentuemer = eigentuemerBean.getEigentuemer(iw.getEigentuemerName());
                alterEigentuemer.getInhalte().remove(inhalt);
                inhalt.setEigentuemer(neuerEigentuemer);
                neuerEigentuemer.getInhalte().add(inhalt);
            }
            // Kategorie
            if (!iw.getKategorieName().equals(inhalt.getKategorie().getName())) {
                Kategorie neueKategorie = kategorieBean.getKategorie(iw.getKategorieName());
                Kategorie alteKategorie = kategorieBean.getKategorie(inhalt.getKategorie().getId());
                alteKategorie.getInhalte().remove(inhalt);
                inhalt.setKategorie(neueKategorie);
                neueKategorie.getInhalte().add(inhalt);
            }

        }
    }
  

    public boolean deleteInhalt(InhaltWrapper inhaltWrapper) {
        Long id = inhaltWrapper.getId();
        Inhalt inhalt = getManagedInhalt(id).orElseThrow();
        inhalt.getKiste().getInhalte().remove(inhalt);
        inhalt.getKategorie().getInhalte().remove(inhalt);
        inhalt.getEigentuemer().getInhalte().remove(inhalt);
        em.remove(inhalt);
        return true;
    }

    private Optional<Inhalt> getManagedInhalt(Long id) {
        return loadInhalteOrderByKistenNummer()
                .stream()
                .filter(inhalt -> inhalt.getId().equals(id))
                .findAny();

    }

    public List<InhaltWrapper> getInhaltWrapper(List<Inhalt> inhalte) {
        List<InhaltWrapper> res = new ArrayList<>();
        inhalte.forEach(inhalt -> {
            res.add(new InhaltWrapper(inhalt));
        });
        return res;
    }

    public List<InhaltWrapper> getInhaltWrapper() {
        return getInhaltWrapper(loadInhalteOrderByInhalt());
    }

    /**
     * wird in InhaltBacking gebraucht
     *
     * @return
     */
    public List<String> getKategorienNamen() {
        return kategorieBean.getKategorienNamen();
    }

}
