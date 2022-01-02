/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.web;

import de.stefanmarinkovic.kisten2.ejb.EigentuemerBean;

import de.stefanmarinkovic.kisten2.entity.Eigentuemer;
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
public class EigentuemerBacking implements Serializable {

    private static final long serialVersionUID = 1873987651922117613L;
    private static final Logger logger = Logger.getLogger("kisten2.ejb.EigentuemerBacking");

    @EJB
    private EigentuemerBean eigentuemerBean; // vor @PostConstruct init() initialisiert

    private Eigentuemer eigentuemer;
    private List<Eigentuemer> eigentuemerListe;

    public EigentuemerBacking() {
        eigentuemer = new Eigentuemer();
    }

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "in EigentumerBacking.init()");
        this.eigentuemerListe = eigentuemerBean.loadEigentuemer();

    }

    public void add() { // InhaltBean, dann EigentuemerBacking initialisiert: eigentuemerListe geladen, addEigentuemer aufgerufen, Seite als Response zurück.
        try {
            if (eigentuemerBean.addEigentuemer(eigentuemer)) {
                //this.eigentuemerListe.add(eigentuemer);
                addEigentuemer();
                logger.log(Level.INFO, "Created new eigentuemer with id {0}, name {1}, ",
                        new Object[]{eigentuemer.getId(), eigentuemer.getName()});
                this.eigentuemer = new Eigentuemer();
            } else {
                //set flag
                // show message eigentuemer mit diesem Namen existiert schon
            }
        } catch (Exception e) {
            logger.warning("Problem creating eigentuemer in add().");
        }
    }

    private void addEigentuemer() {
        Long eigentuemerBefore = eigentuemerListe.stream()
                .filter(e -> e.getName().compareToIgnoreCase(eigentuemer.getName()) < 0).count();
        this.eigentuemerListe.add(eigentuemerBefore.intValue(), eigentuemer); // an der richtigen Stelle einfügen!

    }

    public void update() {
        eigentuemerBean.updateEigentuemer(eigentuemerListe);
        this.eigentuemerListe = eigentuemerBean.loadEigentuemer();
    }

    public void deleteListener(ActionEvent event) {
        UICommand button = (UICommand) event.getComponent();
        UIData uidata = (UIData) (button.getParent().getParent());
        //UIOutput out = (UIOutput) button.findComponent("alleKistenNummer");
        Eigentuemer e = (Eigentuemer) uidata.getRowData();
        if (e.getInhalte().size() > 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Eigentümer kann nicht gelöscht werden, hat noch " + e.getInhalte().size() + " Inhalte");
            context.addMessage(button.getClientId(context), message);
            logger.log(Level.INFO, "deleteListener: Eigentümer: " + String.valueOf(e.getName()));
        }
    }

    public void delete(Eigentuemer eigentuemer) {
        if (!eigentuemerBean.deleteEigentuemer(eigentuemer)) {
            return;
        }
        eigentuemerListe.remove(eigentuemer);
        logger.log(Level.INFO, "deleted eigentuemer with id {0}, name {1}, ",
                new Object[]{eigentuemer.getId(), eigentuemer.getName()});
    }

    public void validateEigentuemer(FacesContext context,
            UIComponent toValidate,
            Object value) {
        String eigentuemerName = ((String) value).trim();
        if (eigentuemerBean.eigentuemerExists(eigentuemerName)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Eigentümer gibt's schon!");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    public void validateUpdateEigentuemer(FacesContext context,
            UIComponent toValidate,
            Object value) {
        logger.log(Level.INFO, "EigentuemerBacking.validateUpdateEigentuemer");
        UIData uidata = (UIData) (toValidate.getParent().getParent());
        Eigentuemer e = (Eigentuemer) uidata.getRowData();
        // for (Eigentuemer eig : ds.getEigentuemerList()) funktioniert nicht, da die Liste ja verändert worden ist.
        for (Eigentuemer eig : eigentuemerBean.loadEigentuemer()) { // Das Original wird gebraucht
            if (eig.getName().equalsIgnoreCase(((String) value).trim())
                    && eig.getId() != e.getId()) {
                ((UIInput) toValidate).setValid(false);
                FacesMessage message = new FacesMessage("Eigentümer mit dem Namen gibt es schon!");
                context.addMessage(toValidate.getClientId(context), message);
                return;
            }
        }
    }

    public Eigentuemer getEigentuemer() {
        return eigentuemer;
    }

    public void setEigentuemer(Eigentuemer eigentuemer) {
        this.eigentuemer = eigentuemer;
    }

    public List<Eigentuemer> getEigentuemerListe() {
        return eigentuemerListe;
    }

    public void setEigentuemerListe(List<Eigentuemer> eigentuemerListe) {
        this.eigentuemerListe = eigentuemerListe;
    }

}
