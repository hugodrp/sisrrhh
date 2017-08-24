/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.converter;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import py.gov.mca.sisrrhh.entitys.RelacionLaboral;
import py.gov.mca.sisrrhh.sessionbeans.RelacionLaboralSB;

/**
 *
 * @author vinsfran
 */
@ManagedBean(name = "relacionLaboralConverter")
@RequestScoped
public class RelacionLaboralConverter implements Converter {

    @EJB
    private RelacionLaboralSB ejbRelacionLaboralSB;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (value != null && value.trim().length() > 0) {
                return ejbRelacionLaboralSB.findByRelacionLaboral(value);
            } else {
                return null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Error!", "Relacion Laboral no válido");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((RelacionLaboral) value).getRelacionLaboral();
        } else {
            return null;
        }

    }


}
