/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.utiles;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author vinsfran
 */
public class MsgUtil {

    private static FacesMessage msg;

    public static void msg(String resumen, String detalle, String tipo) {
        switch (tipo) {
            case "INFO":
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, resumen, detalle);
                break;
            case "WARNING":
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, resumen, detalle);
                break;
            case "ERROR":
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, resumen, detalle);
                break;
            default:
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, resumen, detalle);
                break;
        }
        msgFacesMessage();
    }

    private static void msgFacesMessage() {
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}