/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.web;

import de.stefanmarinkovic.kisten2.ejb.InhaltBean;
import de.stefanmarinkovic.kisten2.ejb.KisteBean;
import de.stefanmarinkovic.kisten2.entity.Inhalt;
import de.stefanmarinkovic.kisten2.entity.InhaltWrapper;
import de.stefanmarinkovic.kisten2.entity.Kiste;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIData;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author stefan
 */
@RequestScoped
@Named
public class InhaltBacking implements Serializable {

    private static final long serialVersionUID = 2142383151318489371L;

    private static final Logger logger = Logger.getLogger("kisten2.web.InhaltBacking");

    @EJB
    private InhaltBean bean;

    @EJB
    private KisteBean kBean;

    private String inhaltName = "";
    private Long kistenNummer;
    private String kategorieName = "";
    private String eigentuemerName = "";

    private List<InhaltWrapper> inhaltWrapperList;

    private List<String> kategorieNamen;

    private List<String> eigentuemerNamen;

    public InhaltBacking() {

    }

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "in InhaltBacking.init()");
        this.inhaltWrapperList = bean.getInhaltWrapper();
        this.kategorieNamen = bean.getKategorienNamen();
        this.eigentuemerNamen
                = bean.loadEigentuemer()
                        .stream()
                        .map(eigentuemer -> eigentuemer.getName())
                        .collect(Collectors.toList());
    }

    public void orderByInhalt() {
        logger.log(Level.INFO,"InhaltBacking.orderByInhalt()");
        this.inhaltWrapperList = bean.getInhaltWrapperOrderByInhalt();
    }

    public void orderByKistenNummer() {
        logger.log(Level.INFO,"InhaltBacking.orderByKistenNummer()");
        this.inhaltWrapperList = bean.getInhaltWrapperOrderByKistenNummer();
    }

    public void orderByEigentuemer() {
        logger.log(Level.INFO,"InhaltBacking.orderByEigentuemer()");
        this.inhaltWrapperList = bean.getInhaltWrapperOrderByEigentuemer();
    }

    public void orderByFuellgrad() {
        logger.log(Level.INFO,"InhaltBacking.orderByFuellgrad()");
        this.inhaltWrapperList = bean.getInhaltWrapperOrderByFuellgrad();
    }

    public void orderByKategorie() {
        logger.log(Level.INFO,"InhaltBacking.orderByKategorie()");
        this.inhaltWrapperList = bean.getInhaltWrapperOrderByKategorie();
    }

    public void orderByStandort() {
        logger.log(Level.INFO,"InhaltBacking.orderByStandort()");
        this.inhaltWrapperList = bean.getInhaltWrapperOrderByStandort();
    }

    public void addInhalt() {
        try {
            InhaltWrapper inhaltWrapper = bean.addInhalt(inhaltName, kistenNummer, kategorieName, eigentuemerName);
            this.inhaltWrapperList.add(inhaltWrapper);

            logger.log(Level.INFO, "Created new inhaltWrapper: " + inhaltWrapper.toString());
            this.inhaltName = "";
            this.kistenNummer = null;
            this.kategorieName = "";
            this.eigentuemerName = "";
        } catch (Exception e) {
            logger.warning("Problem creating inhalt in add().");
        }
    }

    public void delete(InhaltWrapper inhaltWrapper) {
        bean.deleteInhalt(inhaltWrapper);
        this.inhaltWrapperList.remove(inhaltWrapper);
    }

    public void update() {
        logger.log(Level.INFO, "InhaltBacking.update()");
        for (InhaltWrapper inhaltWrapper : inhaltWrapperList) {
            logger.log(Level.INFO, inhaltWrapper.toString());
        }

        bean.updateInhalte(inhaltWrapperList);

    }

    public void validateKistenNummer(FacesContext context,
            UIComponent toValidate,
            Object value) {
        logger.log(Level.INFO,"InhaltBacking.validateKistenNummer");
        Long kn = (Long) value;
        if (!kBean.kisteExists(kn)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Kiste gibt es noch nicht!");
            System.out.println(toValidate.getClientId(context));
            context.addMessage(toValidate.getClientId(context), message);
        }

    }

    /**
     * wenn sich der Füllgrad ändert, muss das in allen InhaltWrappern mit
     * gleicher Kistennummer sichtbar werden.
     *
     * @param event
     */
    public void fuellgradChangedAjax(AjaxBehaviorEvent event) {
        logger.log(Level.INFO,"InhaltBacking.fuellgradChangedAjax");
        UIInput fuellgradInput = (UIInput) event.getComponent();
        UIData uiData = (UIData) fuellgradInput.getParent().getParent();
        int rowIndex = uiData.getRowIndex();
        InhaltWrapper inhaltWrapper = inhaltWrapperList.get(rowIndex);
        Long knr = inhaltWrapper.getKistenNummer();
        float fuellgrad = (Float) fuellgradInput.getValue();

        inhaltWrapperList.forEach(iw -> {
            if (iw.getKistenNummer().equals(knr)) {
                iw.setFuellgrad(fuellgrad);
            }
        });
    }

    /**
     * wenn sich der Standort ändert, muss das in allen InhaltWrappern mit
     * gleicher Kistennummer sichtbar werden.
     *
     * @param event
     * @throws AbortProcessingException
     */
    public void standortChangedAjax(AjaxBehaviorEvent event)
            throws AbortProcessingException {
        logger.log(Level.INFO,"InhaltBacking.standorChangedAjax");
        UIInput standortInput = (UIInput) event.getComponent();
        UIData uiData = (UIData) standortInput.getParent().getParent();
        int rowIndex = uiData.getRowIndex();
        InhaltWrapper inhaltWrapper = inhaltWrapperList.get(rowIndex);
        Long knr = inhaltWrapper.getKistenNummer();
        String standort = (String) standortInput.getValue();
        inhaltWrapperList.forEach(iw -> {
            if (iw.getKistenNummer().equals(knr)) {
                iw.setStandort(standort);
            }
        });
    }

    /**
     * wenn sich die Kistennummer ändert, müssen sich auch Füllgrad und Standort
     * ändern.
     *
     * @param event
     * @throws AbortProcessingException
     */
    public void kisteChangedAjax(AjaxBehaviorEvent event)
            throws AbortProcessingException {

        UIInput in = (UIInput) event.getComponent();
        Long knr = (Long) in.getValue();

        UIData uiData = (UIData) event.getComponent().getParent().getParent();
        String rowData = ((UIData) uiData).getRowData().toString();
        int rowIndex = uiData.getRowIndex();

        InhaltWrapper inhaltWrapper = inhaltWrapperList.get(rowIndex);
        inhaltWrapper.setKistenNummer(knr);
        Kiste neueKiste = kBean.getManagedKiste(knr);
        inhaltWrapper.setFuellgrad(neueKiste.getFuellgrad());
        inhaltWrapper.setStandort(neueKiste.getStandort());

        logger.log(Level.INFO, "phaseId: {0}, eventSource: {1}, UIInput-Value: {2}, rowIndex: {3}, rowData: {4}",
                new Object[]{event.getPhaseId().getName(),
                    event.getSource().toString(),
                    knr,
                    rowIndex,
                    rowData});

    }

    /*
    public void kisteChanged(ValueChangeEvent event) {

        String name = event.getPhaseId().getName();
        String oldVal = event.getOldValue().toString();
        Object newValObject = event.getNewValue();
        String newVal = null == newValObject ? "" : newValObject.toString();
        UIData parentCompObj = (UIData) event.getComponent().getParent().getParent();
        String rowData = ((UIData) parentCompObj).getRowData().toString();
        int index = parentCompObj.getRowIndex();
        //bean.update(inhaltWrapper, index);
        //InhaltWrapper inhaltWrapper = (InhaltWrapper) (parentCompObj.getRowData());
        // Inhalt inhalt = inhaltWrapper.getInhalt();

        InhaltWrapper inhaltWrapper = inhaltWrapperList.get(index);
        inhaltWrapper.setKistenNummer(Long.valueOf(newVal));
        Kiste neueKiste = kBean.getManagedKiste(Long.valueOf(newVal));
        inhaltWrapper.setFuellgrad(neueKiste.getFuellgrad());
        inhaltWrapper.setStandort(neueKiste.getStandort());
        inhaltWrapperList.set(index, inhaltWrapper);

        String parentCom = parentCompObj.toString();
        String obj = event.getSource().toString();
        logger.log(Level.INFO, "name {0}, oldVal {1}, newVal {2}, parentComp {3}, object {4}, rowData {5}, rowIndex {6}",
                new Object[]{name, oldVal, newVal, parentCom, obj, rowData, index});

    }
     */
    public String getInhaltName() {
        return inhaltName;
    }

    public void setInhaltName(String inhaltName) {
        this.inhaltName = inhaltName;
    }

    public Long getKistenNummer() {
        return kistenNummer;
    }

    public void setKistenNummer(Long kistenNummer) {
        this.kistenNummer = kistenNummer;
    }

    public List<Long> getKistenNummern() {
        return kBean.getKistenNummern();
    }

    public String getKategorieName() {
        return kategorieName;
    }

    public void setKategorieName(String kategorieName) {
        this.kategorieName = kategorieName;
    }

    public String getEigentuemerName() {
        return eigentuemerName;
    }

    public void setEigentuemerName(String eigentuemerName) {
        this.eigentuemerName = eigentuemerName;
    }

    public List<String> getEigentuemerNamen() {
        return eigentuemerNamen;
    }

    public List<InhaltWrapper> getInhaltWrapperList() {
        logger.log(Level.INFO,"InhaltBacking.getInhaltWrapperList():");
        for (InhaltWrapper iw : inhaltWrapperList) {
            logger.log(Level.INFO,iw.toString());
        }
        return inhaltWrapperList;
    }

    public List<String> getKategorieNamen() {
        return kategorieNamen;
    }

}
