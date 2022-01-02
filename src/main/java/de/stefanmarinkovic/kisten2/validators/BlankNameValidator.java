/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefan
 */
@FacesValidator
public class BlankNameValidator implements Validator{

    private static final Logger logger = Logger.getLogger("kisten2.validators.BlankNameValidator");
    public BlankNameValidator() {}
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        logger.log(Level.INFO, "BlankNameValidator");
        if (((String) value).isBlank()) {
            ((UIInput) component).setValid(false);
            FacesMessage message = new FacesMessage("Feld darf nicht leer sein!");
            context.addMessage(component.getClientId(context), message);
            return;
        }
    }
    
}
