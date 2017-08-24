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
import py.gov.mca.sisrrhh.entitys.Horarios;
import py.gov.mca.sisrrhh.sessionbeans.HorariosSB;

/**
 *
 * @author vinsfran
 */
@ManagedBean(name = "horariosConverter")
@RequestScoped
public class HorariosConverter implements Converter {

    @EJB
    private HorariosSB ejbHorariosSB;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (value != null && value.trim().length() > 0) {
                return ejbHorariosSB.findByHorario(value);
            } else {
                return null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Error!", "Horario no válido");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((Horarios) value).getHorario();
        } else {
            return null;
        }

    }


}
