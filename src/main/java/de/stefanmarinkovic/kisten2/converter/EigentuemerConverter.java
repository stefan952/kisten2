/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.converter;

import de.stefanmarinkovic.kisten2.ejb.InhaltBean;
import de.stefanmarinkovic.kisten2.entity.Eigentuemer;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefan
 */
//@FacesConverter(forClass=Eigentuemer.class, value="eigentuemer")
@FacesConverter("econv")

public class EigentuemerConverter implements Converter {

    @EJB
    private InhaltBean bean;
    
    private static final Logger logger = Logger.getLogger("kisten2.converter.EigentuemerConverter");
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (null == value || value.isEmpty()) return null;
        //Optional<Eigentuemer> loadedE = bean.findAnyEigentuemer(value);
        //if (loadedE.isPresent()) {
          //  return loadedE.get();
        //}
        
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (null == value) return "";
        return ((Eigentuemer) value).getName();
    }
    
}
