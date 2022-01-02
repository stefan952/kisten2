/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.web;

import de.stefanmarinkovic.kisten2.ejb.KisteBean;
import de.stefanmarinkovic.kisten2.entity.Kiste;
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
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.TransactionRequiredException;
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
public class KistenBacking implements Serializable {

    private static final long serialVersionUID = 2142383151318489373L;

    @EJB
    private KisteBean kisteBean;

    private static final Logger logger = Logger.getLogger("kisten.web.KistenBacking");

    private List<Kiste> kisten;
    private Kiste kiste = new Kiste();

    public KistenBacking() {
    }

    @PostConstruct
    void init() {
        logger.log(Level.INFO, "KistenBacking.init()");
        this.kisten = kisteBean.loadAllKisten();
    }

    public List<Kiste> getKisten() {
        return kisten;
    }

    public void setKisten(List<Kiste> kisten) {
        this.kisten = kisten;
    }

    public Kiste getKiste() {
        return kiste;
    }

    public void setKiste(Kiste kiste) {
        this.kiste = kiste;
    }

    public void addKiste() {
        try {
            if (kisteBean.addKiste(kiste)) {

                Long kistenBefore = kisten.stream().filter(k -> k.getNummer() < kiste.getNummer()).count();
                this.kisten.add(kistenBefore.intValue(), kiste); // an der richtigen Stelle einfügen!

                logger.log(Level.INFO, "Created new kiste with nummer {0}, standort {1}, "
                        + "fuellgrad {2}.",
                        new Object[]{kiste.getNummer(), kiste.getStandort(), kiste.getFuellgrad()});
                kiste = new Kiste();
            } else {
                logger.log(Level.SEVERE, "TODO kiste existiert schon");
            }
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            e.printStackTrace();
            logger.warning("Problem creating kiste in submitKiste.");
        }
    }

    public void validateNummer(FacesContext context,
            UIComponent toValidate,
            Object value) {

        Long kn = (Long) value;

        if (kn <= 0) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Kistennummer muss größer 0 sein!");
            logger.log(Level.INFO, toValidate.getClientId(context));
            context.addMessage(toValidate.getClientId(context), message);
        } else if (kisteBean.kisteExists(kn)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Kiste gibt's schon!" /*+ toValidate.getClientId(context)*/);
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    public void validateFuellgrad(FacesContext context,
            UIComponent toValidate,
            Object value) {

        float fuellgrad = (Float) value; // Konvertierung hat schon vorher stattgefunden!
        if (fuellgrad < 0.0 || fuellgrad > 1.0) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Wert muss zwischen 0.0 und 1.0 liegen!");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    public void update() {
        kisteBean.updateKisten(kisten);
        this.kisten = kisteBean.loadAllKisten();
    }

    /**
     * finds the kiste in uiData-rowData and adds a message if the kiste contains Inhalte
     * @param event 
     */
    public void deleteListener(ActionEvent event) {
        UICommand button = (UICommand) event.getComponent();
        UIData uidata = (UIData) (button.getParent().getParent());
        //UIOutput out = (UIOutput) button.findComponent("alleKistenNummer");
        Kiste k = (Kiste) uidata.getRowData();
        if (k.getInhalte().size() > 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Kiste kann nicht gelöscht werden, hat noch " + k.getInhalte().size() + " Inhalte");
            context.addMessage(button.getClientId(context), message);
            logger.log(Level.INFO, "deleteListener: Kistennummer: " + String.valueOf(k.getNummer()));
        }
    }

    /**
     * Kiste is not deleted if it contains Inhalte 
     * @param kiste 
     */
    public void delete(Kiste kiste) {
        if (!kisteBean.deleteKiste(kiste)) {
            logger.log(Level.INFO, "delete(kiste): Kistennummer: " + String.valueOf(kiste.getNummer()));
            return;
        }
        kisten.remove(kiste);
    }
}
