/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.utiles;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author vinsfran
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable throwable = context.getException();

            try {
                String error = throwable.getMessage() + "(" + throwable.getClass().getName() + ")";
                //System.out.println("ENTRO EN " + error);
                if (throwable.getClass() == javax.faces.application.ViewExpiredException.class) {
                    error = "Su sesión a caducado, por favor vuelva a iniciar su sesión.";
                    //System.out.println("ENTRO EN CustomExceptionHandler " + error);
                }

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Disculpe ", error);
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            } finally {
                iterator.remove();
            }
        }
        getWrapped().handle();
    }
}
