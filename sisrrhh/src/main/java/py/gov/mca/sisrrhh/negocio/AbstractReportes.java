/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.negocio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author vinsfran
 */
public abstract class AbstractReportes<T> {

    private Class<T> reporteClass;

    public AbstractReportes(Class<T> reporteClass) {
        this.reporteClass = reporteClass;
    }

    //    public JasperPrint getRawData(String empIds) {
//
//        JasperPrint jp = null;
//        String reportName = "Employee Report";
//
//        // use your own method to get empList
//        // eg: Lst<Employee> empList = empServiceClass.findByEmpIds(empIds);
//        JRDataSource jrDataSource = new JRBeanCollectionDataSource(empList);
//
//        // build your report 
//        DynamicReportBuilder dynamicReportBuilder = new DynamicReportBuilder();
//        dynamicReportBuilder.setAllowDetailSplit(false);
//        // configure your report with few more options here
//
//        // create columns
//        ColumnBuilder columnBuilderName = ColumnBuilder.getNew();
//        columnBuilderName.setTitle("Emp Name");
//        columnBuilderName.setWidth(300);
//        columnBuilderName.setFixedWidth(true);
//        columnBuilderName.setColumnProperty("name", String.class.getName());
//        dynamicReportBuilder.addColumn(columnBuilderName.build());
//
//        DynamicReport dynamicReport = dynamicReportBuilder.build();
//
//        jp = DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(), jrDataSource, new HashMap<String, Object>());
//        return jp;
//    }
    public JasperPrint exportarPDF(Map<String, Object> parametros, List<T> beanListDataSource, String nombreReporte) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(beanListDataSource);
        JasperReport jasper = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("py/gov/mca/sisrrhh/reportes/" + nombreReporte + ".jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parametros, beanCollectionDataSource);
        return jasperPrint;
    }

    public void exportarGenerico(String tipoReporte, String nombreArchivoSalida, HttpServletResponse response, Map<String, Object> parametros, List<T> beanListDataSource, String nombreReporte) throws IOException, JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(beanListDataSource);
        JasperReport jasper = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("py/gov/mca/sisrrhh/reportes/" + nombreReporte + ".jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parametros, beanCollectionDataSource);

        //OutputStream ouputStream = response.getOutputStream();
        ServletOutputStream ouputStream = response.getOutputStream();

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        if ("pdf".equalsIgnoreCase(tipoReporte)) {// Report  generated in - PDF /
            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "attachment; filename=" + nombreArchivoSalida + ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, ouputStream);
        } else if ("csv".equalsIgnoreCase(tipoReporte)) {// Report generated in - csv /
            //response.setContentType("application/csv");
            response.setContentType("text/csv");
            response.addHeader("Content-disposition", "attachment; filename=" + nombreArchivoSalida + ".csv");

            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ";");
            StringBuffer buffer = new StringBuffer();
            exporter.setParameter(JRCsvExporterParameter.OUTPUT_STRING_BUFFER, buffer);
            exporter.exportReport();
            response.getOutputStream().write(buffer.toString().getBytes());
            response.flushBuffer();
        }
        ouputStream.flush();
        ouputStream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

}
