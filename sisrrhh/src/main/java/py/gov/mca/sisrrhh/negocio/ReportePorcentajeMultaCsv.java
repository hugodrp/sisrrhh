/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.negocio;

import javax.ejb.EJB;
import py.gov.mca.sisrrhh.clasesaux.MultaCsv;

/**
 *
 * @author vinsfran
 */
public class ReportePorcentajeMultaCsv extends AbstractReportes<MultaCsv> {

    public ReportePorcentajeMultaCsv() {
        super(MultaCsv.class);
    }

}
