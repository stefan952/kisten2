/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.web;

import de.stefanmarinkovic.kisten2.ejb.InhaltBean;
import de.stefanmarinkovic.kisten2.ejb.KategorieBean;
import de.stefanmarinkovic.kisten2.entity.Eigentuemer;
import de.stefanmarinkovic.kisten2.entity.Kategorie;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UICommand;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIData;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefan
 */
@RequestScoped
@Named
public class KategorieBacking implements Serializable {

    private static final long serialVersionUID = 187398765192211713L;
    private static final Logger logger = Logger.getLogger("kisten2.web.KategorieBacking");

    @EJB
    private KategorieBean kategorieBean; // vor @PostConstruct init() initialisiert

    private Kategorie kategorie;
    private List<Kategorie> kategorien;

    public KategorieBacking() {
        this.kategorie = new Kategorie();
    }

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "in KategorieBacking.init()");
        this.kategorien = kategorieBean.loadKategorien();

    }

    public String add() {
        kategorie.setName(kategorie.getName().trim());
        if (kategorieBean.addKategorie(kategorie)) {
            //this.kategorien.add(kategorie);
            addKategorie();
            logger.log(Level.INFO, "Kategorie mit id {0}, name {1} hinzugefügt.",
                    new Object[]{kategorie.getId(), kategorie.getName()});
            this.kategorie = new Kategorie();
        } else {
            // set message
        }

        return null;

    }

    private void addKategorie() {
        Long eigentuemerBefore = kategorien.stream()
                .filter(kate -> kate.getName().compareToIgnoreCase(kategorie.getName()) < 0).count();
        this.kategorien.add(eigentuemerBefore.intValue(), kategorie); // an der richtigen Stelle einfügen!

    }

    public void deleteListener(ActionEvent event) {
        UICommand button = (UICommand) event.getComponent();
        UIData uidata = (UIData) (button.getParent().getParent());
        //UIOutput out = (UIOutput) button.findComponent("alleKistenNummer");
        Kategorie e = (Kategorie) uidata.getRowData();
        if (e.getInhalte().size() > 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Kategorie kann nicht gelöscht werden, hat noch " + e.getInhalte().size() + " Inhalte");
            context.addMessage(button.getClientId(context), message);
            logger.log(Level.INFO, "deleteListener: Kategorie: " + String.valueOf(e.getName()));
        }
    }

    public void delete(Kategorie kat) {
        if (!kategorieBean.deleteKategorie(kat)) {
            return;
        }
        kategorien.remove(kat);
        logger.log(Level.INFO, "deleted kategorie with id {0}, name {1}, ",
                new Object[]{kat.getId(), kat.getName()});
    }

    public void update() {
        kategorieBean.updateKategorien(kategorien);
        this.kategorien = kategorieBean.loadKategorien();

    }

    public void validateKategorie(FacesContext context,
            UIComponent toValidate,
            Object value) {
        String kategorieName = (String) value;
        if (kategorieBean.kategorieExists(kategorieName)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Kategorie existiert schon!");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    public void validateUpdateKategorie(FacesContext context,
            UIComponent toValidate,
            Object value) {
        logger.log(Level.INFO, "KategorieBacking.validateUpdateKategorie");
        UIData uidata = (UIData) (toValidate.getParent().getParent());
        Kategorie e = (Kategorie) uidata.getRowData();
        // for (Eigentuemer eig : ds.getEigentuemerList()) funktioniert nicht, da die Liste ja verändert worden ist.
        for (Kategorie eig : kategorieBean.loadKategorien()) { // Das Original wird gebraucht
            if (eig.getName().equalsIgnoreCase(((String) value).trim())
                    && eig.getId() != e.getId()) {
                ((UIInput) toValidate).setValid(false);
                FacesMessage message = new FacesMessage("Kategorie mit dem Namen gibt es schon!");
                context.addMessage(toValidate.getClientId(context), message);
                return;
            }
        }
    }

    public KategorieBean getInhaltBean() {
        return kategorieBean;
    }

    public void setInhaltBean(KategorieBean inhaltBean) {
        this.kategorieBean = inhaltBean;
    }


    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public List<Kategorie> getKategorien() {
        return kategorien;
    }

    public void setKategorien(List<Kategorie> kategorien) {
        this.kategorien = kategorien;
    }

}
