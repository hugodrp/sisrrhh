/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.managedbeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import py.gov.mca.sisrrhh.clasesaux.MultaCsv;
import py.gov.mca.sisrrhh.entitys.AsignacionExcepcionesHorariosDiarios;
import py.gov.mca.sisrrhh.entitys.AsignacionExcepcionesHorariosDiariosPK;
import py.gov.mca.sisrrhh.entitys.Auditoria;
import py.gov.mca.sisrrhh.entitys.Estados;
import py.gov.mca.sisrrhh.entitys.FechasEspeciales;
import py.gov.mca.sisrrhh.entitys.FichaCalendario;
import py.gov.mca.sisrrhh.entitys.FichaCalendarioPK;
import py.gov.mca.sisrrhh.entitys.Funcionarios;
import py.gov.mca.sisrrhh.entitys.Horarios;
import py.gov.mca.sisrrhh.entitys.Marcacion;
import py.gov.mca.sisrrhh.entitys.MarcacionPK;
import py.gov.mca.sisrrhh.entitys.VacacionesFuncionarios;
import py.gov.mca.sisrrhh.entitys.VacacionesFuncionariosPK;
import py.gov.mca.sisrrhh.negocio.ReportePorcentajeMultaCsv;
import py.gov.mca.sisrrhh.negocio.ReportesFichaCalendario;
import py.gov.mca.sisrrhh.sessionbeans.AuditoriaSB;
import py.gov.mca.sisrrhh.sessionbeans.AsignacionExcepcionesHorariosDiariosSB;
import py.gov.mca.sisrrhh.sessionbeans.FechasEspecialesSB;
import py.gov.mca.sisrrhh.sessionbeans.FichaCalendarioSB;
import py.gov.mca.sisrrhh.sessionbeans.FuncionariosSB;
import py.gov.mca.sisrrhh.sessionbeans.MarcacionSB;
import py.gov.mca.sisrrhh.sessionbeans.VacacionesFuncionariosSB;
import py.gov.mca.sisrrhh.utiles.Converciones;
import py.gov.mca.sisrrhh.utiles.MsgUtil;

/**
 *
 * @author vinsfran
 */
@Named(value = "sisrrhhMB")
@SessionScoped
public class SisrrhhMB implements Serializable {

    @EJB
    private FuncionariosSB ejbFuncionariosSB;

    @EJB
    private MarcacionSB ejbMarcacionSB;

    @EJB
    private AuditoriaSB auditoriaSB;

    @EJB
    private AsignacionExcepcionesHorariosDiariosSB ejbAsignacionExcepcionesHorariosDiariosSB;

    @EJB
    private FichaCalendarioSB ejbFichaCalendarioSB;

    @EJB
    private VacacionesFuncionariosSB vacacionesFuncionariosSB;

    @EJB
    private FechasEspecialesSB ejbFechasEspecialesSB;

    private String cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private String direccion;
    private String telefono;
    private String cuentaCorriente;

    private String cedulaFuncionario;

    private String cedulaFuncionarioInicio;
    private String cedulaFuncionarioFin;

    private Date fechaDesde;
    private Date fechaHasta;

    private String loginUsuario;
    private String claveUsuario;
    private String contrasenaActual;
    private String contrasena1;
    private String contrasena2;
    private String linkExpediente;

    private List<Marcacion> listaMarcaciones;
    private List<Funcionarios> listaFuncionarios;
    private List<FichaCalendario> fichasCalendarios;
    private List<FichaCalendario> fichasCalendariosAux;
    private Funcionarios funcionarioUsuario;
    private Funcionarios funcionario;

    private Part file;
    private String nombreArchivo;
    private int cantidadRegistros;
    private int cantidadRegistrosError;
    private List<String> detallesErrores;
    private StringBuilder numeroReloj;
    private StringBuilder anio;
    private StringBuilder mes;
    private StringBuilder dia;
    private StringBuilder tipoFun;
    private String fechaArchivo;
    private String numeroRelojArchivo;
    private float progress;

    private final SimpleDateFormat dateFormatFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat dateFormatFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dateFormatHora = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat dateFormatDia = new SimpleDateFormat("EEEE", Locale.getDefault());
    private final SimpleDateFormat dateFormatMes = new SimpleDateFormat("MMMM", Locale.getDefault());
    private SimpleDateFormat dateFormatDiaNumero = new SimpleDateFormat("dd");
    private SimpleDateFormat dateFormatMesNumero = new SimpleDateFormat("MM");
    private SimpleDateFormat dateFormatAnioNumero = new SimpleDateFormat("yyyy");

    private LinkedHashMap<Integer, String> listaMeses;
    private boolean mostrarBotonSubirBd;
    private boolean activarBotonSubirBd;

    private boolean mostrarComponentesPantallaBuscarRangoFechaCedula;
    private Integer numeroMes;
    private Integer numeroAnio;
    private Integer porcentajeMulta;
    private Integer porcentajeMultaTotal;
    private Integer numeroFilaMarcacion;
    private Integer contadorMarcacion;

    //Variables para vacaciones
    private boolean mostrarComponentesVacaciones;
    private List<VacacionesFuncionarios> vacacionesFuncionarios;
    private Integer anioVacacion;
    private Integer diasCorrespondientesVacacion;
    private Integer diasRestantesVacacion;
    private Integer diasSolicitadoVacacion;
    private Integer diasRestantesTotalVacacion;
    private String cedulaFuncionarioVacaciones;
    private Funcionarios funcionarioVacaciones;
    private Date fechaDesdeFuncionarioTab1vacaciones;
    private Date fechaHastaFuncionarioTab1vacaciones;
    private List<Date> rangoDeFechaSeleccionado;
    private AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiariosVacaciones;
    private List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosVacacionesLista;
    private Integer totalDiasEntreFechasVacacion;
    private Integer totalDiasFeriadosVacacion;
    private Integer totalDiasDomingosVacacion;
    private Integer totalDiasYaAsignados;
    private HtmlDataTable dataTableVacaciones;

    /**
     * Creates a new instance of SisrrhhManagedBean
     */
    public SisrrhhMB() {
        this.listaMeses = new LinkedHashMap<>();
        listaMeses.put(0, "Enero");
        listaMeses.put(1, "Febrero");
        listaMeses.put(2, "Marzo");
        listaMeses.put(3, "Abril");
        listaMeses.put(4, "Mayo");
        listaMeses.put(5, "Junio");
        listaMeses.put(6, "Julio");
        listaMeses.put(7, "Agosto");
        listaMeses.put(8, "Septiembre");
        listaMeses.put(9, "Octubre");
        listaMeses.put(10, "Noviembre");
        listaMeses.put(11, "Diciembre");
    }

    @PostConstruct
    public void init() {
        recuperarUsuarioSession();
        this.loginUsuario = "";
        this.claveUsuario = "";

    }

    public String btnIngresar() {
        if (getLoginUsuario() != null) {
            if (getLoginUsuario().equals("") || getClaveUsuario().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los campos con (*) no pueden estar vacio.", ""));
                return "/index";
            } else {
                setFuncionarioUsuario(null);
                setFuncionarioUsuario(ejbFuncionariosSB.findByUsuario(getLoginUsuario().trim()));
                if (getFuncionarioUsuario() != null) {
                    Converciones c = new Converciones();
                    String contrasenaMD5 = c.getMD5(getClaveUsuario());
                    /// System.out.println("Clave" +contrasenaMD5);
                    if (contrasenaMD5 == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo ingresar, intentelo de nuevo.", ""));
                        return "/index";
                    } else if (getFuncionarioUsuario().getContrasena().equals(getClaveUsuario()) && getFuncionarioUsuario().getEstadoFuncionario().getCodigo().equals("ACT")) {
                        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                        httpSession.setAttribute("loginUsuario", this.getLoginUsuario());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido!", ""));
                        return "/admin_panel_principal";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña no validos, intentelo de nuevo.", ""));
                        return "/index";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no exite, intentelo de nuevo.", ""));
                    return "/index";
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los campos con null.", ""));
            return "/index";
        }

    }

    public String btnSalir() {
        this.funcionarioUsuario = null;
        this.funcionario = null;
        this.loginUsuario = null;
        this.claveUsuario = null;

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.invalidate();

//        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
//        String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
//        HttpSession session = (HttpSession) ctx.getSession(false);
//        if (session != null) {
//            this.setUsuario(null);
//            session.invalidate();
//        }
        return "/index";
    }

    public String prepararFormCargaDedeArchivoAsistencias() {
        cantidadRegistros = 0;
        cantidadRegistrosError = 0;
        setNombreArchivo("Ningún archivo seleccionado");
        setNumeroReloj(new StringBuilder());
        setAnio(new StringBuilder());
        setMes(new StringBuilder());
        setDia(new StringBuilder());
        setFechaArchivo("DD/MM/AAAA");
        setNumeroRelojArchivo("0");
        mostrarBotonSubirBd = false;
        activarBotonSubirBd = true;
        return "admin_form_carga_desde_archivo_asistencias";
    }

    public void leerArchivoAsistencias() {
        cantidadRegistros = 0;
        cantidadRegistrosError = 0;
        setNumeroReloj(new StringBuilder());
        setAnio(new StringBuilder());
        setMes(new StringBuilder());
        setDia(new StringBuilder());
        setNombreArchivo("Ningún archivo seleccionado");
        setFechaArchivo("DD/MM/AAAA");
        setNumeroRelojArchivo("0");
        String errorLec;
        try {
            String disposition = file.getHeader("content-disposition");
            setNombreArchivo(disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1"));
            String tipoArchivo = file.getContentType();
            if (getNombreArchivo().length() == 23) {
                if (tipoArchivo.equals("text/csv")) {
                    StringBuilder nombreArchivoCorto = new StringBuilder();
                    nombreArchivoCorto.append(getNombreArchivo().charAt(0)); //a
                    nombreArchivoCorto.append(getNombreArchivo().charAt(1)); //s
                    nombreArchivoCorto.append(getNombreArchivo().charAt(2)); //i
                    nombreArchivoCorto.append(getNombreArchivo().charAt(3)); //s
                    nombreArchivoCorto.append(getNombreArchivo().charAt(4)); //t
                    nombreArchivoCorto.append(getNombreArchivo().charAt(5)); //e
                    nombreArchivoCorto.append(getNombreArchivo().charAt(6)); //n
                    nombreArchivoCorto.append(getNombreArchivo().charAt(7)); //c
                    nombreArchivoCorto.append(getNombreArchivo().charAt(8)); //i
                    nombreArchivoCorto.append(getNombreArchivo().charAt(9)); //a
                    if (nombreArchivoCorto.toString().equals("asistencia")) {
                        getNumeroReloj().append(getNombreArchivo().charAt(10));
                        getAnio().append(getNombreArchivo().charAt(11));
                        getAnio().append(getNombreArchivo().charAt(12));
                        getAnio().append(getNombreArchivo().charAt(13));
                        getAnio().append(getNombreArchivo().charAt(14));
                        getMes().append(getNombreArchivo().charAt(15));
                        getMes().append(getNombreArchivo().charAt(16));
                        getDia().append(getNombreArchivo().charAt(17));
                        getDia().append(getNombreArchivo().charAt(18));
                        setFechaArchivo(getDia() + "/" + getMes() + "/" + getAnio());
                        setNumeroRelojArchivo(getNumeroReloj().toString());
                        try {
                            BufferedReader bf = new BufferedReader(new InputStreamReader(file.getInputStream()));
                            String line;
                            try {
                                listaMarcaciones = new ArrayList<>();
                                detallesErrores = new ArrayList<>();
                                int contadorLinea = 0;
                                int contadorErrorLectura = 0;
                                while ((line = bf.readLine()) != null) {
                                    if (contadorLinea > 0) {
                                        StringTokenizer tokens = new StringTokenizer(line, ";");
                                        while (tokens.hasMoreTokens()) {
                                            //String dpto = tokens.nextToken().replaceAll("\"", "");
                                            //String nombreAux = tokens.nextToken().replaceAll("\"", "");
                                            //String no = tokens.nextToken().replaceAll("\"", "");
                                            //String fechaHora = tokens.nextToken();
                                            //String locacionId = tokens.nextToken().replaceAll("\"", "");
                                            //String idNumero = tokens.nextToken().replaceAll("\"", "");
                                            //String vericicaCod = tokens.nextToken().replaceAll("\"", "");
                                            //String tarjetaNo = tokens.nextToken().replaceAll("\"", "");
                                            //String dpto = tokens.nextToken();
                                            //System.out.println("dpto: " + dpto);
                                            //String nombreAux = tokens.nextToken();
                                            //System.out.println("nombreAux: " + nombreAux);
                                            String no = tokens.nextToken();
                                            // System.out.println("no - cedula: " + no);
                                            String fechaHora = tokens.nextToken();
                                            //  System.out.println("fechaHora: " + fechaHora);
                                            //String locacionId = tokens.nextToken();
                                            //System.out.println("locacionId: " + locacionId);
                                            //String idNumero = tokens.nextToken();
                                            //System.out.println("idNumero: " + idNumero);
                                            String vericicaCod = tokens.nextToken();
                                            // System.out.println("vericicaCod: " + vericicaCod);
                                            //String tarjetaNo = tokens.nextToken();
                                            //System.out.println("---------||---------");
                                            Marcacion marcacion = new Marcacion();
                                            try {
                                                marcacion.setMarcacionPK(new MarcacionPK());
                                                marcacion.getMarcacionPK().setCedula(no);
                                                marcacion.getMarcacionPK().setFechaHoraMarcacion(dateFormatFechaHora.parse(fechaHora));
                                                marcacion.getMarcacionPK().setTipoMacacion(vericicaCod.toUpperCase().trim());
                                                marcacion.setFechaMarcacionChar(marcacion.getMarcacionPK().getFechaHoraMarcacion());
                                                marcacion.setHoraMarcacionChar(marcacion.getMarcacionPK().getFechaHoraMarcacion());
                                                marcacion.setDiaMarcacion(dateFormatDia.format(marcacion.getMarcacionPK().getFechaHoraMarcacion()).toUpperCase());
                                                marcacion.setFormaMarcacion("");
                                                marcacion.setNombreArchivo(getNombreArchivo());
                                                listaMarcaciones.add(marcacion);

                                            } catch (ParseException ex) {
                                                contadorErrorLectura = contadorErrorLectura + 1;
                                                Logger.getLogger(SisrrhhMB.class.getName()).log(Level.SEVERE, null, ex);
                                                errorLec = "Cedula: " + marcacion.getMarcacionPK().getCedula() + ", Fecha: " + fechaHora;
                                                detallesErrores.add(errorLec);
                                                MsgUtil.msg("No se puede convertir a fecha", errorLec, "ERROR");
                                            }

                                        }
                                    }
                                    contadorLinea = contadorLinea + 1;
                                }
                                cantidadRegistros = listaMarcaciones.size() - contadorErrorLectura;
                                cantidadRegistrosError = contadorErrorLectura;
                                if (contadorErrorLectura == 0) {
                                    mostrarBotonSubirBd = true;
                                    activarBotonSubirBd = false;
                                }
                            } catch (IOException e) {
                                errorLec = "IOException1: " + e.getMessage();
                                detallesErrores.add(errorLec);
                                MsgUtil.msg("Error en archivo", errorLec, "ERROR");
                            }
                        } catch (FileNotFoundException e) {
                            errorLec = "FileNotFoundException: " + e.getMessage();
                            detallesErrores.add(errorLec);
                            MsgUtil.msg("Error en archivo", errorLec, "ERROR");
                        }
                    } else {
                        mostrarBotonSubirBd = false;
                        activarBotonSubirBd = true;
                        MsgUtil.msg("Nombre archivo no valido", "", "ERROR");
                    }
                } else {
                    mostrarBotonSubirBd = false;
                    activarBotonSubirBd = true;
                    MsgUtil.msg("Tipo archivo no valido", "Extención debe ser .csv", "ERROR");
                }

            } else {
                mostrarBotonSubirBd = false;
                activarBotonSubirBd = true;
                MsgUtil.msg("Longitud del nombre de archivo no valido", "Extención debe ser 23 caracteres", "ERROR");
            }
        } catch (IOException e) {
            mostrarBotonSubirBd = false;
            activarBotonSubirBd = true;
            errorLec = "IOException2: " + e.getMessage();
            detallesErrores.add(errorLec);
            MsgUtil.msg("Error en archivo", errorLec, "ERROR");
        }
    }

    public void guardarDesdeArchivoAsistencias() {
        if (listaMarcaciones != null) {
            activarBotonSubirBd = true;
            progress = ((listaMarcaciones.size() / 2) * 100) / (listaMarcaciones.size());
            int i = 0;
            contadorMarcacion = 0;
            for (Marcacion mar : listaMarcaciones) {
                contadorMarcacion = contadorMarcacion + 1;
                ejbMarcacionSB.insert(mar);
                if (++i % 1000 == 0) {
                    ejbMarcacionSB.flushAndClear();
                }
                progress = (i * 100) / (listaMarcaciones.size());
            }
            ejbMarcacionSB.flushAndClear();
//            if (ejbMarcacionSB.udtadeMultiple(listaMarcaciones).equals("OK")) {
//                progress = (listaMarcaciones.size() * 100) / (listaMarcaciones.size());
//                String descriAudit = "Nombre de archivo: " + getNombreArchivo() + ", Cantidad de registros: " + listaMarcaciones.size();
//                guardarAuditoria(new Date(), funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(), "Insert o Update", "marcacion", "", descriAudit);
//                activarBotonSubirBd = false;
//            }else{
//               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OCURRIO UN ERROR EN LA BD", "")); 
//            }
        } else {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione otro archivo", ""));
        }
    }

    public String formatearFechaToHora(Date fecha) {
        if (fecha == null) {
            return "";
        } else {
            return dateFormatHora.format(fecha);
        }
    }

    public String formatearFecha(Date fecha) {
        if (fecha == null) {
            return "";
        } else {
            return dateFormatFecha.format(fecha);
        }
    }

    public void guardarAuditoria(Date fechaAudit, String usuarioAudit, String rolAudit, String tipoMovimientoAudit, String nombreTablaAudit, String campoClaveAudit, String descriAudit) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(fechaAudit);
        auditoria.setUsuario(usuarioAudit);
        auditoria.setRolUsuario(rolAudit);
        auditoria.setTipoMovimiento(tipoMovimientoAudit);
        auditoria.setNombreTabla(nombreTablaAudit);
        auditoria.setClaveTabla(campoClaveAudit);
        auditoria.setDescripcion(descriAudit);
        auditoriaSB.create(auditoria);
    }

    public String prepararFormBuscarPorCedulaFechaDesdeFechaHasta() {
        mostrarComponentesPantallaBuscarRangoFechaCedula = false;
        return "admin_form_reporte_rango_fecha_cedula";
    }

    public String prepararFormBuscarPorRangoCedulaFechaDesdeFechaHasta() {
        mostrarComponentesPantallaBuscarRangoFechaCedula = false;
        return "admin_form_reporte_rango_fecha_rango_cedula";
    }

    public void exportarPDFporRangoCedulaFechaDesdeFechaHasta() throws JRException, IOException {
        List<Funcionarios> listaFuncionariosAux = ejbFuncionariosSB.findByRangoCedula(cedulaFuncionarioInicio, cedulaFuncionarioFin);

        for (int i = 0; i < listaFuncionariosAux.size(); i++) {
            listaFuncionariosAux.get(i).setFichaCalendarioList(getMarcacionesPorCedulaFechaDedeFechaHasta(listaFuncionariosAux.get(i).getNumeroDocumento(), fechaDesde, fechaHasta));
        }

        JasperReport jasper;
        Map<String, Object> parametros = new HashMap<>();
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String urlImagen = ((ServletContext) ctx.getContext()).getRealPath("/resources/images/escudo.gif");
        String urlImagen2 = ((ServletContext) ctx.getContext()).getRealPath("/resources/images/asu128_trans.png");

        parametros.put("urlImagen", urlImagen);
        parametros.put("urlImagen2", urlImagen2);
        parametros.put("nombreDependencia", "Dirección de Recursos Humanos");
        parametros.put("fechaDesde", getFechaDesde());
        parametros.put("fechaHasta", getFechaHasta());
        parametros.put("fechaGeneracion", new Date());

        parametros.put("cedulaFuncionario", getFuncionario().getNumeroDocumento());
        parametros.put("apellidoNombreFuncionario", getFuncionario().getApellidos() + ", " + getFuncionario().getNombres());
        parametros.put("nombreApellidoFuncionario", getFuncionario().getNombres() + " " + getFuncionario().getApellidos());

        parametros.put("totalRegistros", listaFuncionariosAux.size());
        parametros.put("usuarioGeneracion", funcionarioUsuario.getNombres() + " " + funcionarioUsuario.getApellidos());
        parametros.put("SUBREPORT_DIR", "py/gov/mca/sisrrhh/reportes/");

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaFuncionariosAux);
        jasper = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("py/gov/mca/sisrrhh/reportes/ReporteMarcacionCedulaFechaDesdeFechaHasta.jasper"));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parametros, beanCollectionDataSource);

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment; filename=marcacion_" + getFuncionario().getNumeroDocumento() + "_" + dateFormatMes.format(getFechaDesde()) + ".pdf");
        //response.
        //Response.Write("<script>window.print();</script>"); 

        ServletOutputStream stream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void buscarPorCedulaFechaDesdeFechaHasta() {
        funcionario = getFuncionario(getCedulaFuncionario());
        fichasCalendarios = getMarcacionesPorCedulaFechaDedeFechaHasta(getCedulaFuncionario(), getFechaDesde(), getFechaHasta());
        mostrarComponentesPantallaBuscarRangoFechaCedula = true;
    }

    public void exportarPDFporCedulaFechaDesdeFechaHasta() throws JRException, IOException {
        System.out.println("exportarPDFporCedulaFechaDesdeFechaHasta()");
        ReportesFichaCalendario reporte = new ReportesFichaCalendario();
        Map<String, Object> parametros = new HashMap<>();
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String urlImagen = ((ServletContext) ctx.getContext()).getRealPath("/resources/images/escudo.gif");
        String urlImagen2 = ((ServletContext) ctx.getContext()).getRealPath("/resources/images/asu128_trans.png");
        parametros.put("urlImagen", urlImagen);
        parametros.put("urlImagen2", urlImagen2);
        parametros.put("nombreDependencia", "Dirección de Recursos Humanos");
        parametros.put("fechaDesde", getFechaDesde());
        parametros.put("fechaHasta", getFechaHasta());
        parametros.put("fechaGeneracion", new Date());
        parametros.put("cedulaFuncionario", getFuncionario().getNumeroDocumento());
        parametros.put("apellidoNombreFuncionario", getFuncionario().getApellidos() + ", " + getFuncionario().getNombres());
        parametros.put("nombreApellidoFuncionario", getFuncionario().getNombres() + " " + getFuncionario().getApellidos());
        parametros.put("totalRegistros", fichasCalendarios.size());
        parametros.put("usuarioGeneracion", funcionarioUsuario.getNombres() + " " + funcionarioUsuario.getApellidos());
        parametros.put("SUBREPORT_DIR", "py/gov/mca/sisrrhh/reportes/");
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        reporte.exportarGenerico("pdf", "marcacion_" + getFuncionario().getNumeroDocumento() + "_" + dateFormatMes.format(getFechaDesde()), response, parametros, getFichasCalendarios(), "ReporteMarcacionCedulaFechaDesdeFechaHasta");
    }

    public void exportarPDFporCedulaMesAnio() throws JRException, IOException {
        System.out.println("exportarPDFporCedulaFechaDesdeFechaHasta()");
        ReportesFichaCalendario reporte = new ReportesFichaCalendario();
        Map<String, Object> parametros = new HashMap<>();
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String urlImagen = ((ServletContext) ctx.getContext()).getRealPath("/resources/images/escudo.gif");
        String urlImagen2 = ((ServletContext) ctx.getContext()).getRealPath("/resources/images/asu128_trans.png");
        parametros.put("urlImagen", urlImagen);
        parametros.put("urlImagen2", urlImagen2);
        parametros.put("nombreDependencia", "Dirección de Recursos Humanos");
        parametros.put("mes", numeroMes);
        parametros.put("anio", numeroAnio);
        parametros.put("fechaGeneracion", new Date());
        parametros.put("cedulaFuncionario", getFuncionario().getNumeroDocumento());
        parametros.put("apellidoNombreFuncionario", getFuncionario().getApellidos() + ", " + getFuncionario().getNombres());
        parametros.put("nombreApellidoFuncionario", getFuncionario().getNombres() + " " + getFuncionario().getApellidos());
        parametros.put("totalRegistros", fichasCalendarios.size());
        parametros.put("usuarioGeneracion", funcionarioUsuario.getNombres() + " " + funcionarioUsuario.getApellidos());
        parametros.put("SUBREPORT_DIR", "py/gov/mca/sisrrhh/reportes/");
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        String mesLetra = listaMeses.get(numeroMes);

        reporte.exportarGenerico("pdf", ("marcacion_" + getFuncionario().getNumeroDocumento() + "_" + mesLetra + "_" + numeroAnio).toLowerCase(), response, parametros, fichasCalendarios, "ReporteMarcacionCedulaMesAnio");
    }

    public void generarCsvPorcentajeMultasTotalesPorRangoDeFecha() throws IOException, JRException {
        ReportePorcentajeMultaCsv reporte = new ReportePorcentajeMultaCsv();
        List<MultaCsv> multas = new ArrayList<>();
        List<Funcionarios> listaFuncionariosActivos = ejbFuncionariosSB.findByEstado("ACT");
        for (int i = 0; i < listaFuncionariosActivos.size(); i++) {
            Integer sueldoDiario = 0;
            Integer totalPorcentajeMulta = 0;
            Integer totalMontoFijo = 0;
            Integer totalDiasTrabajados = 0;
            Integer totalDiasComplementarios = 0;
            Integer totalMulta = 0;
            List<FichaCalendario> fichasCalendariosAux2 = ejbFichaCalendarioSB.findByCedulaFechaDesdeFechaHasta(listaFuncionariosActivos.get(i).getNumeroDocumento(), fechaDesde, fechaHasta);
            for (int j = 0; j < fichasCalendariosAux2.size(); j++) {
                if (fichasCalendariosAux2.get(j).getPorcentajeMulta() != null) {
                    totalPorcentajeMulta = totalPorcentajeMulta + fichasCalendariosAux2.get(j).getPorcentajeMulta();
                }

                if (fichasCalendarios.get(j).getMontoFijo() != null) {
                    totalMontoFijo = totalMontoFijo + fichasCalendarios.get(j).getMontoFijo();
                }

                if (fichasCalendarios.get(j).getDiaTrabajado() != null) {
                    if (fichasCalendarios.get(j).getDiaTrabajado()) {
                        totalDiasTrabajados = totalDiasTrabajados + 1;
                    }
                }

                if (fichasCalendarios.get(j).getDiaComplementario() != null) {
                    if (fichasCalendarios.get(j).getDiaComplementario()) {
                        totalDiasComplementarios = totalDiasComplementarios + 1;
                    }
                }

                fichasCalendarios.get(j).setMarcaBloqueo("SI");
            }
            if (listaFuncionariosActivos.get(i).getSueldoDiario() != null) {
                sueldoDiario = listaFuncionariosActivos.get(i).getSueldoDiario().intValue();
                Double montoMultaDiaria = ((listaFuncionariosActivos.get(i).getSueldoDiario() * totalPorcentajeMulta) / 100);
                montoMultaDiaria = Math.ceil(montoMultaDiaria);
                totalMulta = totalMulta + montoMultaDiaria.intValue() + totalMontoFijo;
            } else {
                System.out.println("Cedula: " + listaFuncionariosActivos.get(i).getNumeroDocumento() + ", Sin salario diario ");
            }

            System.out.println("Cedula: " + listaFuncionariosActivos.get(i).getNumeroDocumento() + ", "
                    + "Jornal: " + sueldoDiario + ", "
                    + "Total Pocentaje: " + totalPorcentajeMulta + "%, "
                    + "Total Monto Fijo: " + totalMontoFijo + ", "
                    + "Total Dias Trabajados: " + totalDiasTrabajados + ", "
                    + "Total Dias Complementarios: " + totalDiasComplementarios + ", "
                    + "Total Multa:  " + totalMulta);

            MultaCsv multa = new MultaCsv(listaFuncionariosActivos.get(i).getNumeroDocumento(), sueldoDiario, totalPorcentajeMulta, totalMontoFijo, totalDiasTrabajados, totalDiasComplementarios, totalMulta);
            multas.add(multa);
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        reporte.exportarGenerico("csv", "porcentaje_total_", response, null, multas, "ReportePorcentajeMultasEnCvs");
    }

    public void onComplete() {
        progress = (0 / 1);
        //mostrarBotonSubirBd = false;       
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registros guardados " + cantidadRegistros, ""));
    }

    public String guardarFichaPorMesAnioCedula() {
        FichaCalendario fichaCalendario;

        //fichasCalendariosAux = getFichaCalendarioNom(getCedulaFuncionario(), numeroMes + 1, numeroAnio);
        for (int i = 0; i < getFichasCalendarios().size(); i++) {
            /*fichaCalendario = new FichaCalendario();
            fichaCalendario.setFichaCalendarioPK(new FichaCalendarioPK());
            fichaCalendario.getFichaCalendarioPK().setFechaCalendario(marcacionesLinea.get(i).getFechaMarcacion());
            fichaCalendario.getFichaCalendarioPK().setNumeroDocumentoFuncionario(cedulaFuncionario);
            fichaCalendario.setDiaCalendarioLetra(marcacionesLinea.get(i).getDiaMarcacion());
            fichaCalendario.setDiaCalendarioNumero(marcacionesLinea.get(i).getDiaMarcacionNumero());
            fichaCalendario.setMesCalendarioNumero((numeroMes + 1));
            fichaCalendario.setAnioCalendarioNumero(numeroAnio);
            fichaCalendario.setHoraEntrada(marcacionesLinea.get(i).getHoraEntrada());
            fichaCalendario.setHoraSalida(marcacionesLinea.get(i).getHoraSalida());
            fichaCalendario.setHoraNoDefinida(marcacionesLinea.get(i).getHoraNoDefinida());
            fichaCalendario.setHorarioAsignado(marcacionesLinea.get(i).getHorarioAsignado());
            fichaCalendario.setPorcentajeMulta(marcacionesLinea.get(i).getPorcentajeMulta());
            fichaCalendario.setTipoExepcion(marcacionesLinea.get(i).getTipoExepcion());
            fichaCalendario.setUsuariosUsuario(usuario);*/
            getFichasCalendarios().get(i).setMarcaCalculoPorcentaje(1);
            // if (!fichasCalendarios.get(i).getPorcentajeMulta().equals(fichasCalendariosAux.get(i).getPorcentajeMulta())) {
            getFichasCalendarios().get(i).setMesCalendarioNumero((numeroMes + 1));
            getFichasCalendarios().get(i).setAnioCalendarioNumero(numeroAnio);

            String descriAudit = "Fecha: " + getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario() + ", "
                    + "Monto Porcentaje: " + getFichasCalendarios().get(i).getPorcentajeMulta();
            actualizarFichaCalendario(getFichasCalendarios().get(i));

            guardarAuditoria(new Date(), funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(),
                    "Insert o Update", "ficha_calendario", getFichasCalendarios().get(i).getFichaCalendarioPK().getNumeroDocumentoFuncionario(), descriAudit);
            // }

        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos guardados", ""));
        return "admin_funcionario_ficha_general";
    }

    public void actualizarFichaCalendario(FichaCalendario fichaCalendario) {
        ejbFichaCalendarioSB.udtade(fichaCalendario);

    }

    public String prepararFormPorcentaje(Integer porcentaje, Integer numeroFila) {
        porcentajeMulta = porcentaje;
        numeroFilaMarcacion = numeroFila;
        return "admin_form_porcentaje";
    }

    public String guardarPorcentajeEnFila() {
        for (int i = 0; i < getFichasCalendarios().size(); i++) {
            if (numeroFilaMarcacion.equals(getFichasCalendarios().get(i).getDiaCalendarioNumero())) {
                getFichasCalendarios().get(i).setPorcentajeMulta(porcentajeMulta);
            }
        }
        return "admin_funcionario_ficha_general";
    }

    public String prepararFormulariosVacaciones(String pagina) {
        funcionarioVacaciones = null;
        cedulaFuncionarioVacaciones = "";
        anioVacacion = 0;
        diasCorrespondientesVacacion = 0;
        diasRestantesVacacion = 0;
        diasSolicitadoVacacion = 0;
        diasRestantesTotalVacacion = 0;
        totalDiasEntreFechasVacacion = 0;
        totalDiasDomingosVacacion = 0;
        totalDiasFeriadosVacacion = 0;
        totalDiasYaAsignados = 0;
        mostrarComponentesVacaciones = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        numeroMes = cal.get(Calendar.MONTH);
        numeroAnio = cal.get(Calendar.YEAR);
        dataTableVacaciones = new HtmlDataTable();
        String pag = "";
        switch (pagina) {
            case "form_pedido":
                pag = "admin_funcionario_vacaciones_form_pedido";
                break;
            case "form_modificacion":
                pag = "admin_funcionario_vacaciones_form_modificacion";
                break;
            case "form_anual":
                pag = "admin_funcionario_vacaciones_form_anual";
                break;
        }
        return pag;
    }

    public void buscarFuncionarioVacacionesPedido() {
        funcionarioVacaciones = getFuncionario(getCedulaFuncionarioVacaciones());
        diasRestantesTotalVacacion = 0;
        if (funcionarioVacaciones != null) {
            mostrarComponentesVacaciones = true;
            vacacionesFuncionarios = vacacionesFuncionariosSB.findByNumeroDocumentoEstadoVacacionConDiasRestantes(funcionarioVacaciones.getNumeroDocumento(), "ACT");
            calcularDiasRestantesTotalVacacion();
        } else {
            vacacionesFuncionarios = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe funcionario", ""));
        }
    }

    public void guardarVacacionSolicitada() {
        if (diasSolicitadoVacacion > diasRestantesTotalVacacion) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Excede a la cantidad de dias disponibles", ""));
        } else {
            for (int i = 0; i < vacacionesFuncionarios.size(); i++) {
                if (diasSolicitadoVacacion > vacacionesFuncionarios.get(i).getCantidadDiasRestante()) {
                    diasSolicitadoVacacion = diasSolicitadoVacacion - vacacionesFuncionarios.get(i).getCantidadDiasRestante();
                    vacacionesFuncionarios.get(i).setCantidadDiasRestante(0);
                } else if (diasSolicitadoVacacion < vacacionesFuncionarios.get(i).getCantidadDiasRestante()) {
                    vacacionesFuncionarios.get(i).setCantidadDiasRestante((vacacionesFuncionarios.get(i).getCantidadDiasRestante() - diasSolicitadoVacacion));
                    diasSolicitadoVacacion = 0;
                } else {
                    diasSolicitadoVacacion = 0;
                    vacacionesFuncionarios.get(i).setCantidadDiasRestante(0);
                }
            }
            actualizarVacacionesFuncionarios();
            insertarAsignacionExcepcionesHorariosDiarios();
            diasRestantesTotalVacacion = 0;
            calcularDiasRestantesTotalVacacion();
        }

    }

    public void actualizarVacacionesFuncionarios() {
        for (int i = 0; i < vacacionesFuncionarios.size(); i++) {
            vacacionesFuncionariosSB.udtade(vacacionesFuncionarios.get(i));
            String descriAudit = "Periodo: " + vacacionesFuncionarios.get(i).getVacacionesFuncionariosPK().getAnioVacacion() + ", "
                    + "Cedula: " + vacacionesFuncionarios.get(i).getVacacionesFuncionariosPK().getNumeroDocumento() + ", "
                    + "Dias correspondiente: " + vacacionesFuncionarios.get(i).getCantidadDiasCorrespondiente() + ", "
                    + "Dias restante: " + vacacionesFuncionarios.get(i).getCantidadDiasRestante();
            guardarAuditoria(new Date(), funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(), "Update", "vacaciones_funcionarios", vacacionesFuncionarios.get(i).getVacacionesFuncionariosPK().getNumeroDocumento(), descriAudit);
        }
    }

    public void insertarAsignacionExcepcionesHorariosDiarios() {
        for (int i = 0; i < rangoDeFechaSeleccionado.size(); i++) {
            AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiariosAux = new AsignacionExcepcionesHorariosDiarios();
            asignacionExcepcionesHorariosDiariosAux.setAsignacionExcepcionesHorariosDiariosPK(new AsignacionExcepcionesHorariosDiariosPK());

            asignacionExcepcionesHorariosDiariosAux.setHorarioAsignado(new Horarios());
            asignacionExcepcionesHorariosDiariosAux.setEstadoHorarioAsignado(new Estados());

            asignacionExcepcionesHorariosDiariosAux.getAsignacionExcepcionesHorariosDiariosPK().setCodigoExcepcion("VACA");
            asignacionExcepcionesHorariosDiariosAux.getAsignacionExcepcionesHorariosDiariosPK().setFechaAsignacion(rangoDeFechaSeleccionado.get(i));
            asignacionExcepcionesHorariosDiariosAux.getAsignacionExcepcionesHorariosDiariosPK().setNumeroDocumentoFuncionario(funcionarioVacaciones.getNumeroDocumento());
            asignacionExcepcionesHorariosDiariosAux.setHorarioAsignado(funcionarioVacaciones.getHorarioNormal());
            asignacionExcepcionesHorariosDiariosAux.setMesAsignacion(Integer.parseInt(dateFormatMesNumero.format(rangoDeFechaSeleccionado.get(i))));
            asignacionExcepcionesHorariosDiariosAux.setAnioAsignacion(Integer.parseInt(dateFormatAnioNumero.format(rangoDeFechaSeleccionado.get(i))));
            asignacionExcepcionesHorariosDiariosAux.setDescripcionAsignacion("NRO FORMULARIO XX");
            asignacionExcepcionesHorariosDiariosAux.getEstadoHorarioAsignado().setCodigo("ACT");
            String descripcion = "FechaAsignacion: " + asignacionExcepcionesHorariosDiariosAux.getAsignacionExcepcionesHorariosDiariosPK().getFechaAsignacion() + ", "
                    + "Cedula: " + funcionarioVacaciones.getNumeroDocumento() + ", "
                    + "HorarioAsignado: " + funcionarioVacaciones.getHorarioNormal() + ", "
                    + "MesAsignacion: " + asignacionExcepcionesHorariosDiariosAux.getMesAsignacion() + ", "
                    + "AnioAsignacion: " + asignacionExcepcionesHorariosDiariosAux.getAnioAsignacion() + ", "
                    + "DescripcionAsignacion: " + asignacionExcepcionesHorariosDiariosAux.getDescripcionAsignacion();
            actualizarAsignacionExcepcionesHorariosDiarios(asignacionExcepcionesHorariosDiariosAux,
                    new Date(),
                    funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update", "asignacion_excepciones_horarios_diarios", funcionarioVacaciones.getNumeroDocumento(), descripcion);

        }
    }

    public void actualizarAsignacionExcepcionesHorariosDiarios(AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiariosAux,
            Date fecha, String usuario, String usuarioRol, String tipoMovimiento, String nombreTabla, String claveTabla, String descripcion) {
        ejbAsignacionExcepcionesHorariosDiariosSB.udtade(asignacionExcepcionesHorariosDiariosAux);
        guardarAuditoria(fecha, usuario, usuarioRol, tipoMovimiento, nombreTabla, claveTabla, descripcion);

    }

    public void eliminarAsignacionExcepcionesHorariosDiarios(AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiariosAux,
            Date fecha, String usuario, String usuarioRol, String tipoMovimiento, String nombreTabla, String claveTabla, String descripcion) {
        ejbAsignacionExcepcionesHorariosDiariosSB.delete(asignacionExcepcionesHorariosDiariosAux);
        guardarAuditoria(fecha, usuario, usuarioRol, tipoMovimiento, nombreTabla, claveTabla, descripcion);
    }

    public void calcularDiasHabiles() {
        totalDiasEntreFechasVacacion = 0;
        totalDiasDomingosVacacion = 0;
        totalDiasFeriadosVacacion = 0;
        setTotalDiasYaAsignados((Integer) 0);
        rangoDeFechaSeleccionado = new ArrayList<>();
        long diferenciaEn_ms = fechaHastaFuncionarioTab1vacaciones.getTime() - fechaDesdeFuncionarioTab1vacaciones.getTime();
        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
        for (int i = 0; i < (dias + 1); i++) {
            totalDiasEntreFechasVacacion = totalDiasEntreFechasVacacion + 1;
            Date fechaAux = sumaDias(fechaDesdeFuncionarioTab1vacaciones, (i));
            FechasEspeciales fechaEspecial = ejbFechasEspecialesSB.findByFechaEspecial(fechaAux);
            if (fechaEspecial == null) {
                String domingo = dateFormatDia.format(fechaAux).toUpperCase();
                if (!domingo.equals("DOMINGO")) {
                    asignacionExcepcionesHorariosDiariosVacaciones = ejbAsignacionExcepcionesHorariosDiariosSB.findByFechaAsignacionNumeroDocumentoFuncionario(fechaAux, funcionarioVacaciones.getNumeroDocumento(), "VACA", "ACT");
                    if (asignacionExcepcionesHorariosDiariosVacaciones == null) {
                        rangoDeFechaSeleccionado.add(fechaAux);
                    } else {
                        totalDiasYaAsignados = totalDiasYaAsignados + 1;
                    }
                } else {
                    totalDiasDomingosVacacion = totalDiasDomingosVacacion + 1;
                }
            } else {
                totalDiasFeriadosVacacion = totalDiasFeriadosVacacion + 1;
            }
        }
        diasSolicitadoVacacion = rangoDeFechaSeleccionado.size();
    }

    public static Date sumaDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DAY_OF_YEAR, dias);
        return cal.getTime();
    }

    public void calcularDiasRestantesTotalVacacion() {
        for (int i = 0; i < vacacionesFuncionarios.size(); i++) {
            diasRestantesTotalVacacion = diasRestantesTotalVacacion + vacacionesFuncionarios.get(i).getCantidadDiasRestante();
        }
    }

    public void guardarVacacionAnio() {
        VacacionesFuncionarios vacaAnio = new VacacionesFuncionarios();
        vacaAnio.setEstadoVacacion(new Estados());
        vacaAnio.getEstadoVacacion().setCodigo("ACT");
        vacaAnio.setCantidadDiasCorrespondiente(diasCorrespondientesVacacion);
        vacaAnio.setCantidadDiasRestante(diasRestantesVacacion);
        vacaAnio.setVacacionesFuncionariosPK(new VacacionesFuncionariosPK());
        vacaAnio.getVacacionesFuncionariosPK().setAnioVacacion(anioVacacion);
        vacaAnio.getVacacionesFuncionariosPK().setNumeroDocumento(funcionarioVacaciones.getNumeroDocumento());
        actualizarCantidadDiasAnual(vacaAnio);
    }

    public void actualizarCantidadDiasAnual(VacacionesFuncionarios vacaAnio) {
        vacacionesFuncionariosSB.udtade(vacaAnio);
        String descriAudit = "Año: " + anioVacacion + ", Cedula: " + funcionarioVacaciones.getNumeroDocumento() + ", Dias Total: " + vacaAnio.getCantidadDiasCorrespondiente() + ", Dias Resto: " + vacaAnio.getCantidadDiasRestante() + ", ESTADO: " + vacaAnio.getEstadoVacacion().getCodigo();
        guardarAuditoria(new Date(), funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(), "Insert o Update", "vacaciones_funcionarios", funcionarioVacaciones.getNumeroDocumento(), descriAudit);
        vacacionesFuncionarios = vacacionesFuncionariosSB.findByNumeroDocumentoEstadoVacacion(funcionarioVacaciones.getNumeroDocumento(), "ACT");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos guardados", ""));
    }

    public void buscarFuncionarioVacacionesAnual() {
        funcionarioVacaciones = getFuncionario(getCedulaFuncionarioVacaciones());
        if (funcionarioVacaciones != null) {
            mostrarComponentesVacaciones = true;
            vacacionesFuncionarios = vacacionesFuncionariosSB.findByNumeroDocumentoEstadoVacacion(funcionarioVacaciones.getNumeroDocumento(), "ACT");
        } else {
            vacacionesFuncionarios = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe funcionario", ""));
        }
    }

    public void buscarFuncionarioVacacionesModificacion() {
        funcionarioVacaciones = getFuncionario(getCedulaFuncionarioVacaciones());
        if (funcionarioVacaciones != null) {
            dataTableVacaciones = new HtmlDataTable();
            mostrarComponentesVacaciones = true;
            asignacionExcepcionesHorariosDiariosVacacionesLista = ejbAsignacionExcepcionesHorariosDiariosSB.findByCedulaMesAnioTipo(funcionarioVacaciones.getNumeroDocumento(), (numeroMes + 1), numeroAnio, "VACA");
        } else {
            vacacionesFuncionarios = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe funcionario", ""));
        }
    }

    public String modificarFechaVacacion() {
        asignacionExcepcionesHorariosDiariosVacaciones = new AsignacionExcepcionesHorariosDiarios();
        asignacionExcepcionesHorariosDiariosVacaciones = (AsignacionExcepcionesHorariosDiarios) getDataTableVacaciones().getRowData();
        vacacionesFuncionarios = vacacionesFuncionariosSB.findByNumeroDocumentoEstadoVacacion(funcionarioVacaciones.getNumeroDocumento(), "ACT");

        return "admin_funcionario_vacaciones_form_anular_fecha";
    }

    public String borrarFechaVacacion() {
        Date fechaAsignacion = asignacionExcepcionesHorariosDiariosVacaciones.getAsignacionExcepcionesHorariosDiariosPK().getFechaAsignacion();
        String numeroDocumento = funcionarioVacaciones.getNumeroDocumento();
        String codigoExcepcion = "VACA";
        String estadoHorarioAsignado = "ACT";

        AsignacionExcepcionesHorariosDiarios diaAsig = ejbAsignacionExcepcionesHorariosDiariosSB.findByFechaAsignacionNumeroDocumentoFuncionario(fechaAsignacion, numeroDocumento, codigoExcepcion, estadoHorarioAsignado);
        AsignacionExcepcionesHorariosDiarios diaAsigBorrar = ejbAsignacionExcepcionesHorariosDiariosSB.findByFechaAsignacionNumeroDocumentoFuncionario(fechaAsignacion, numeroDocumento, codigoExcepcion, estadoHorarioAsignado);

        diaAsig.getAsignacionExcepcionesHorariosDiariosPK().setCodigoExcepcion("NULL");
        diaAsig.setHorarioAsignado(funcionarioVacaciones.getHorarioNormal());
        diaAsig.setEstadoHorarioAsignado(new Estados("DES"));

        String descripcion = "Eliminacion de tipo de excepcion y horario asignado para fecha: " + diaAsig.getAsignacionExcepcionesHorariosDiariosPK().getFechaAsignacion();

        actualizarAsignacionExcepcionesHorariosDiarios(diaAsig,
                new Date(),
                funcionarioUsuario.getUsuario(),
                funcionarioUsuario.getRolUsuario().getNombre(),
                "Update", "asignacion_excepciones_horarios_diarios",
                diaAsig.getAsignacionExcepcionesHorariosDiariosPK().getNumeroDocumentoFuncionario(),
                descripcion);
        ejbAsignacionExcepcionesHorariosDiariosSB.delete(diaAsigBorrar);
        System.out.println("Fecha seleccionada: " + asignacionExcepcionesHorariosDiariosVacaciones.getAsignacionExcepcionesHorariosDiariosPK().getFechaAsignacion());
        System.out.println("Fecha asig: " + diaAsig.getAsignacionExcepcionesHorariosDiariosPK().getFechaAsignacion());

        for (int i = 0; i < vacacionesFuncionarios.size(); i++) {
            if (vacacionesFuncionarios.get(i).getCantidadDiasCorrespondiente() > vacacionesFuncionarios.get(i).getCantidadDiasRestante()) {
                diasSolicitadoVacacion = 1 + vacacionesFuncionarios.get(i).getCantidadDiasRestante();
                vacacionesFuncionarios.get(i).setCantidadDiasRestante(diasSolicitadoVacacion);
                actualizarCantidadDiasAnual(vacacionesFuncionarios.get(i));
                break;
            }
        }

        dataTableVacaciones = new HtmlDataTable();
        asignacionExcepcionesHorariosDiariosVacacionesLista = ejbAsignacionExcepcionesHorariosDiariosSB.findByCedulaMesAnioTipo(funcionarioVacaciones.getNumeroDocumento(), (numeroMes + 1), numeroAnio, "VACA");

        return "admin_funcionario_vacaciones_form_modificacion";
    }

    public Integer cantidadDiasDelMes(Integer anio, Integer mes) {
        Calendar cal = new GregorianCalendar(anio, mes, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    public Funcionarios getFuncionario(String cedula) {
        Funcionarios funcionarioRetorno = ejbFuncionariosSB.findByNumeroDocumento(cedula);
        return funcionarioRetorno;
    }

    public List<FichaCalendario> getMarcacionesPorCedulaFechaDedeFechaHasta(String cedula, Date fechaDesde, Date fechaHasta) {

        List<Marcacion> marcaciones = ejbMarcacionSB.findByCedulaFechaDesdeFechaHasta(cedula, fechaDesde, fechaHasta);
        List<Marcacion> marcacionesAux;

        List<FichaCalendario> fichaCalendarioRetorno = new ArrayList<>();

        String control = "";
        FichaCalendario fichaCalendario;
        for (int i = 0; i < marcaciones.size(); i++) {
            System.out.println("ENTROOOOO" + marcaciones.get(i).getFechaMarcacionChar());
            if (!control.equals(marcaciones.get(i).getFechaMarcacionChar().toString())) {
                Date fechaInicial;
                try {
                    fechaInicial = dateFormatFecha.parse(dateFormatFecha.format(marcaciones.get(i).getMarcacionPK().getFechaHoraMarcacion()));
                    marcacionesAux = new ArrayList<>();
                    for (int j = 0; j < marcaciones.size(); j++) {
                        if (fechaInicial.equals(dateFormatFecha.parse(dateFormatFecha.format(marcaciones.get(j).getMarcacionPK().getFechaHoraMarcacion())))) {
                            marcacionesAux.add(marcaciones.get(j));
                        }
                    }
                    Date fechaMayor;
                    Date fechaMenor;
                    Marcacion marcacionMenor = new Marcacion();
                    Marcacion marcacionMayor = new Marcacion();
                    Marcacion marcacionNoDefinida = new Marcacion();
                    for (int k = 0; k < marcacionesAux.size(); k++) {
                        fechaMayor = marcacionesAux.get(k).getMarcacionPK().getFechaHoraMarcacion();
                        fechaMenor = marcacionesAux.get(k).getMarcacionPK().getFechaHoraMarcacion();
                        for (int l = 0; l < marcacionesAux.size(); l++) {
                            if (marcacionesAux.get(l).getMarcacionPK().getFechaHoraMarcacion().compareTo(fechaMayor) > 0) {
                                marcacionMayor = marcacionesAux.get(l);
                                fechaMayor = marcacionesAux.get(l).getMarcacionPK().getFechaHoraMarcacion();
                            } else if (marcacionesAux.get(l).getMarcacionPK().getFechaHoraMarcacion().compareTo(fechaMenor) < 0) {
                                marcacionMenor = marcacionesAux.get(l);
                                fechaMenor = marcacionesAux.get(l).getMarcacionPK().getFechaHoraMarcacion();
                            } else {
                                marcacionNoDefinida = marcacionesAux.get(l);
                            }
                        }
                    }
                    fichaCalendario = new FichaCalendario();

                    fichaCalendario.setDiaCalendarioLetra(marcacionesAux.get(0).getDiaMarcacion());

                    fichaCalendario.setFichaCalendarioPK(new FichaCalendarioPK());
                    fichaCalendario.getFichaCalendarioPK().setFechaCalendario(marcacionesAux.get(0).getMarcacionPK().getFechaHoraMarcacion());
                    fichaCalendario.getFichaCalendarioPK().setNumeroDocumentoFuncionario(marcacionesAux.get(0).getMarcacionPK().getCedula());

                    //FALTA FUNCIONARIO
                    if (marcacionMenor.getHoraMarcacionChar() != null) {

                        fichaCalendario.setHoraEntrada(marcacionMenor.getHoraMarcacionChar());
                    } else {
                        fichaCalendario.setHoraEntrada(null);
                    }

                    if (marcacionMayor.getHoraMarcacionChar() != null) {
                        fichaCalendario.setHoraSalida(marcacionMayor.getHoraMarcacionChar());
                    } else {
                        fichaCalendario.setHoraSalida(null);
                    }
                    if (marcacionNoDefinida.getHoraMarcacionChar() != null) {
                        fichaCalendario.setHoraNoDefinida(marcacionNoDefinida.getHoraMarcacionChar());
                        if (marcacionMenor.getHoraMarcacionChar() != null) {
                            fichaCalendario.setHoraNoDefinida(null);
                        }
                        if (marcacionMayor.getHoraMarcacionChar() != null) {
                            fichaCalendario.setHoraNoDefinida(null);
                        }
                    } else {
                        fichaCalendario.setHoraNoDefinida(null);
                    }
                    fichaCalendarioRetorno.add(fichaCalendario);
                } catch (ParseException ex) {
                    Logger.getLogger(SisrrhhMB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            control = marcaciones.get(i).getFechaMarcacionChar().toString();
        }
        return fichaCalendarioRetorno;
    }

    public Funcionarios recuperarUsuarioSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        funcionarioUsuario = (Funcionarios) session.getAttribute("funcionarioUsuario");
        return funcionarioUsuario;
    }

    public List<AsignacionExcepcionesHorariosDiarios> getAsignacionesHorariosPorFuncionario(String cedula, Integer mes, Integer anio) {
        List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosRetorno = ejbAsignacionExcepcionesHorariosDiariosSB.findByCedulaMesAnio(cedula, mes, anio);
        return asignacionExcepcionesHorariosDiariosRetorno;
    }

    public List<FichaCalendario> getFichaCalendarioNom(String cedula, Integer mes, Integer anio) {
        List<FichaCalendario> fichaCalendarioRetorno = ejbFichaCalendarioSB.findByCedulaMesAnio(cedula, mes, anio);
        return fichaCalendarioRetorno;
    }

    public String test() {
        String r = "test";
        r = ejbFichaCalendarioSB.findTest();
        System.out.println(r);
        return r;
    }

    /**
     * @return the cedula
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * @param cedula the cedula to set
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the cuentaCorriente
     */
    public String getCuentaCorriente() {
        return cuentaCorriente;
    }

    /**
     * @param cuentaCorriente the cuentaCorriente to set
     */
    public void setCuentaCorriente(String cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    /**
     * @return the loginUsuario
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }

    /**
     * @param loginUsuario the loginUsuario to set
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    /**
     * @return the claveUsuario
     */
    public String getClaveUsuario() {
        return claveUsuario;
    }

    /**
     * @param claveUsuario the claveUsuario to set
     */
    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    /**
     * @return the contrasenaActual
     */
    public String getContrasenaActual() {
        return contrasenaActual;
    }

    /**
     * @param contrasenaActual the contrasenaActual to set
     */
    public void setContrasenaActual(String contrasenaActual) {
        this.contrasenaActual = contrasenaActual;
    }

    /**
     * @return the contrasena1
     */
    public String getContrasena1() {
        return contrasena1;
    }

    /**
     * @param contrasena1 the contrasena1 to set
     */
    public void setContrasena1(String contrasena1) {
        this.contrasena1 = contrasena1;
    }

    /**
     * @return the contrasena2
     */
    public String getContrasena2() {
        return contrasena2;
    }

    /**
     * @param contrasena2 the contrasena2 to set
     */
    public void setContrasena2(String contrasena2) {
        this.contrasena2 = contrasena2;
    }

    /**
     * @return the linkExpediente
     */
    public String getLinkExpediente() {
        return linkExpediente;
    }

    /**
     * @param linkExpediente the linkExpediente to set
     */
    public void setLinkExpediente(String linkExpediente) {
        this.linkExpediente = linkExpediente;
    }

    /**
     * @return the funcionarioUsuario
     */
    public Funcionarios getFuncionarioUsuario() {
        return funcionarioUsuario;
    }

    /**
     * @param funcionarioUsuario the usuario to set
     */
    public void setFuncionarioUsuario(Funcionarios funcionarioUsuario) {
        this.funcionarioUsuario = funcionarioUsuario;
    }

    /**
     * @return the file
     */
    public Part getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(Part file) {
        this.file = file;
    }

    /**
     * @return the mostrarBotonSubirBd
     */
    public boolean isMostrarBotonSubirBd() {
        return mostrarBotonSubirBd;
    }

    /**
     * @param mostrarBotonSubirBd the mostrarBotonSubirBd to set
     */
    public void setMostrarBotonSubirBd(boolean mostrarBotonSubirBd) {
        this.mostrarBotonSubirBd = mostrarBotonSubirBd;
    }

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo the nombreArchivo to set
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * @return the cantidadRegistros
     */
    public int getCantidadRegistros() {
        return cantidadRegistros;
    }

    /**
     * @param cantidadRegistros the cantidadRegistros to set
     */
    public void setCantidadRegistros(int cantidadRegistros) {
        this.cantidadRegistros = cantidadRegistros;
    }

    /**
     * @return the anio
     */
    public StringBuilder getAnio() {
        return anio;
    }

    /**
     * @param anio the anio to set
     */
    public void setAnio(StringBuilder anio) {
        this.anio = anio;
    }

    /**
     * @return the numeroReloj
     */
    public StringBuilder getNumeroReloj() {
        return numeroReloj;
    }

    /**
     * @param numeroReloj the numeroReloj to set
     */
    public void setNumeroReloj(StringBuilder numeroReloj) {
        this.numeroReloj = numeroReloj;
    }

    /**
     * @return the mes
     */
    public StringBuilder getMes() {
        return mes;
    }

    /**
     * @param mes the mes to set
     */
    public void setMes(StringBuilder mes) {
        this.mes = mes;
    }

    /**
     * @return the dia
     */
    public StringBuilder getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(StringBuilder dia) {
        this.dia = dia;
    }

    /**
     * @return the fechaArchivo
     */
    public String getFechaArchivo() {
        return fechaArchivo;
    }

    /**
     * @param fechaArchivo the fechaArchivo to set
     */
    public void setFechaArchivo(String fechaArchivo) {
        this.fechaArchivo = fechaArchivo;
    }

    /**
     * @return the numeroRelojArchivo
     */
    public String getNumeroRelojArchivo() {
        return numeroRelojArchivo;
    }

    /**
     * @param numeroRelojArchivo the numeroRelojArchivo to set
     */
    public void setNumeroRelojArchivo(String numeroRelojArchivo) {
        this.numeroRelojArchivo = numeroRelojArchivo;
    }

    /**
     * @return the progress
     */
    public float getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(float progress) {
        this.progress = progress;
    }

    /**
     * @return the activarBotonSubirBd
     */
    public boolean isActivarBotonSubirBd() {
        return activarBotonSubirBd;
    }

    /**
     * @param activarBotonSubirBd the activarBotonSubirBd to set
     */
    public void setActivarBotonSubirBd(boolean activarBotonSubirBd) {
        this.activarBotonSubirBd = activarBotonSubirBd;
    }

    /**
     * @return the cedulaFuncionario
     */
    public String getCedulaFuncionario() {
        return cedulaFuncionario;
    }

    /**
     * @param cedulaFuncionario the cedulaFuncionario to set
     */
    public void setCedulaFuncionario(String cedulaFuncionario) {
        this.cedulaFuncionario = cedulaFuncionario;
    }

    /**
     * @return the fechaDesde
     */
    public Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     * @param fechaDesde the fechaDesde to set
     */
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * @return the fechaHasta
     */
    public Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     * @param fechaHasta the fechaHasta to set
     */
    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * @return the listaFuncionarios
     */
    public List<Funcionarios> getListaFuncionarios() {
        return listaFuncionarios;
    }

    /**
     * @param listaFuncionarios the listaFuncionarios to set
     */
    public void setListaFuncionarios(List<Funcionarios> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    /**
     * @return the funcionario
     */
    public Funcionarios getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(Funcionarios funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the mostrarComponentesPantallaBuscarRangoFechaCedula
     */
    public boolean isMostrarComponentesPantallaBuscarRangoFechaCedula() {
        return mostrarComponentesPantallaBuscarRangoFechaCedula;
    }

    /**
     * @param mostrarComponentesPantallaBuscarRangoFechaCedula the
     * mostrarComponentesPantallaBuscarRangoFechaCedula to set
     */
    public void setMostrarComponentesPantallaBuscarRangoFechaCedula(boolean mostrarComponentesPantallaBuscarRangoFechaCedula) {
        this.mostrarComponentesPantallaBuscarRangoFechaCedula = mostrarComponentesPantallaBuscarRangoFechaCedula;
    }

    /**
     * @return the listaMeses
     */
    public LinkedHashMap<Integer, String> getListaMeses() {
        return listaMeses;
    }

    /**
     * @param listaMeses the listaMeses to set
     */
    public void setListaMeses(LinkedHashMap<Integer, String> listaMeses) {
        this.listaMeses = listaMeses;
    }

    /**
     * @return the numeroMes
     */
    public Integer getNumeroMes() {
        return numeroMes;
    }

    /**
     * @param numeroMes the numeroMes to set
     */
    public void setNumeroMes(Integer numeroMes) {
        this.numeroMes = numeroMes;
    }

    /**
     * @return the numeroAnio
     */
    public Integer getNumeroAnio() {
        return numeroAnio;
    }

    /**
     * @param numeroAnio the numeroAnio to set
     */
    public void setNumeroAnio(Integer numeroAnio) {
        this.numeroAnio = numeroAnio;
    }

    /**
     * @return the porcentajeMulta
     */
    public Integer getPorcentajeMulta() {
        return porcentajeMulta;
    }

    /**
     * @param porcentajeMulta the porcentajeMulta to set
     */
    public void setPorcentajeMulta(Integer porcentajeMulta) {
        this.porcentajeMulta = porcentajeMulta;
    }

    /**
     * @return the numeroFilaMarcacion
     */
    public Integer getNumeroFilaMarcacion() {
        return numeroFilaMarcacion;
    }

    /**
     * @param numeroFilaMarcacion the numeroFilaMarcacion to set
     */
    public void setNumeroFilaMarcacion(Integer numeroFilaMarcacion) {
        this.numeroFilaMarcacion = numeroFilaMarcacion;
    }

    /**
     * @return the porcentajeMultaTotal
     */
    public Integer getPorcentajeMultaTotal() {
        return porcentajeMultaTotal;
    }

    /**
     * @param porcentajeMultaTotal the porcentajeMultaTotal to set
     */
    public void setPorcentajeMultaTotal(Integer porcentajeMultaTotal) {
        this.porcentajeMultaTotal = porcentajeMultaTotal;
    }

    /**
     * @return the fichasCalendarios
     */
    public List<FichaCalendario> getFichasCalendarios() {
        return fichasCalendarios;
    }

    /**
     * @param fichasCalendarios the fichasCalendarios to set
     */
    public void setFichasCalendarios(List<FichaCalendario> fichasCalendarios) {
        this.fichasCalendarios = fichasCalendarios;
    }

    /**
     * @return the cedulaFuncionarioInicio
     */
    public String getCedulaFuncionarioInicio() {
        return cedulaFuncionarioInicio;
    }

    /**
     * @param cedulaFuncionarioInicio the cedulaFuncionarioInicio to set
     */
    public void setCedulaFuncionarioInicio(String cedulaFuncionarioInicio) {
        this.cedulaFuncionarioInicio = cedulaFuncionarioInicio;
    }

    /**
     * @return the cedulaFuncionarioFin
     */
    public String getCedulaFuncionarioFin() {
        return cedulaFuncionarioFin;
    }

    /**
     * @param cedulaFuncionarioFin the cedulaFuncionarioFin to set
     */
    public void setCedulaFuncionarioFin(String cedulaFuncionarioFin) {
        this.cedulaFuncionarioFin = cedulaFuncionarioFin;
    }

    /**
     * @return the tipoFun
     */
    public StringBuilder getTipoFun() {
        return tipoFun;
    }

    /**
     * @param tipoFun the tipoFun to set
     */
    public void setTipoFun(StringBuilder tipoFun) {
        this.tipoFun = tipoFun;
    }

    /**
     * @return the mostrarComponentesVacaciones
     */
    public boolean isMostrarComponentesVacaciones() {
        return mostrarComponentesVacaciones;
    }

    /**
     * @param mostrarComponentesVacaciones the mostrarComponentesVacaciones to
     * set
     */
    public void setMostrarComponentesVacaciones(boolean mostrarComponentesVacaciones) {
        this.mostrarComponentesVacaciones = mostrarComponentesVacaciones;
    }

    /**
     * @return the vacacionesFuncionarios
     */
    public List<VacacionesFuncionarios> getVacacionesFuncionarios() {
        return vacacionesFuncionarios;
    }

    /**
     * @param vacacionesFuncionarios the vacacionesFuncionarios to set
     */
    public void setVacacionesFuncionarios(List<VacacionesFuncionarios> vacacionesFuncionarios) {
        this.vacacionesFuncionarios = vacacionesFuncionarios;
    }

    /**
     * @return the anioVacacion
     */
    public Integer getAnioVacacion() {
        return anioVacacion;
    }

    /**
     * @param anioVacacion the anioVacacion to set
     */
    public void setAnioVacacion(Integer anioVacacion) {
        this.anioVacacion = anioVacacion;
    }

    /**
     * @return the diasCorrespondientesVacacion
     */
    public Integer getDiasCorrespondientesVacacion() {
        return diasCorrespondientesVacacion;
    }

    /**
     * @param diasCorrespondientesVacacion the diasCorrespondientesVacacion to
     * set
     */
    public void setDiasCorrespondientesVacacion(Integer diasCorrespondientesVacacion) {
        this.diasCorrespondientesVacacion = diasCorrespondientesVacacion;
    }

    /**
     * @return the diasRestantesVacacion
     */
    public Integer getDiasRestantesVacacion() {
        return diasRestantesVacacion;
    }

    /**
     * @param diasRestantesVacacion the diasRestantesVacacion to set
     */
    public void setDiasRestantesVacacion(Integer diasRestantesVacacion) {
        this.diasRestantesVacacion = diasRestantesVacacion;
    }

    /**
     * @return the diasSolicitadoVacacion
     */
    public Integer getDiasSolicitadoVacacion() {
        return diasSolicitadoVacacion;
    }

    /**
     * @param diasSolicitadoVacacion the diasSolicitadoVacacion to set
     */
    public void setDiasSolicitadoVacacion(Integer diasSolicitadoVacacion) {
        this.diasSolicitadoVacacion = diasSolicitadoVacacion;
    }

    /**
     * @return the diasRestantesTotalVacacion
     */
    public Integer getDiasRestantesTotalVacacion() {
        return diasRestantesTotalVacacion;
    }

    /**
     * @param diasRestantesTotalVacacion the diasRestantesTotalVacacion to set
     */
    public void setDiasRestantesTotalVacacion(Integer diasRestantesTotalVacacion) {
        this.diasRestantesTotalVacacion = diasRestantesTotalVacacion;
    }

    /**
     * @return the fechaDesdeFuncionarioTab1vacaciones
     */
    public Date getFechaDesdeFuncionarioTab1vacaciones() {
        return fechaDesdeFuncionarioTab1vacaciones;
    }

    /**
     * @param fechaDesdeFuncionarioTab1vacaciones the
     * fechaDesdeFuncionarioTab1vacaciones to set
     */
    public void setFechaDesdeFuncionarioTab1vacaciones(Date fechaDesdeFuncionarioTab1vacaciones) {
        this.fechaDesdeFuncionarioTab1vacaciones = fechaDesdeFuncionarioTab1vacaciones;
    }

    /**
     * @return the fechaHastaFuncionarioTab1vacaciones
     */
    public Date getFechaHastaFuncionarioTab1vacaciones() {
        return fechaHastaFuncionarioTab1vacaciones;
    }

    /**
     * @param fechaHastaFuncionarioTab1vacaciones the
     * fechaHastaFuncionarioTab1vacaciones to set
     */
    public void setFechaHastaFuncionarioTab1vacaciones(Date fechaHastaFuncionarioTab1vacaciones) {
        this.fechaHastaFuncionarioTab1vacaciones = fechaHastaFuncionarioTab1vacaciones;
    }

    /**
     * @return the rangoDeFechaSeleccionado
     */
    public List<Date> getRangoDeFechaSeleccionado() {
        return rangoDeFechaSeleccionado;
    }

    /**
     * @param rangoDeFechaSeleccionado the rangoDeFechaSeleccionado to set
     */
    public void setRangoDeFechaSeleccionado(List<Date> rangoDeFechaSeleccionado) {
        this.rangoDeFechaSeleccionado = rangoDeFechaSeleccionado;
    }

    /**
     * @return the totalDiasEntreFechasVacacion
     */
    public Integer getTotalDiasEntreFechasVacacion() {
        return totalDiasEntreFechasVacacion;
    }

    /**
     * @param totalDiasEntreFechasVacacion the totalDiasEntreFechasVacacion to
     * set
     */
    public void setTotalDiasEntreFechasVacacion(Integer totalDiasEntreFechasVacacion) {
        this.totalDiasEntreFechasVacacion = totalDiasEntreFechasVacacion;
    }

    /**
     * @return the totalDiasFeriadosVacacion
     */
    public Integer getTotalDiasFeriadosVacacion() {
        return totalDiasFeriadosVacacion;
    }

    /**
     * @param totalDiasFeriadosVacacion the totalDiasFeriadosVacacion to set
     */
    public void setTotalDiasFeriadosVacacion(Integer totalDiasFeriadosVacacion) {
        this.totalDiasFeriadosVacacion = totalDiasFeriadosVacacion;
    }

    /**
     * @return the totalDiasDomingosVacacion
     */
    public Integer getTotalDiasDomingosVacacion() {
        return totalDiasDomingosVacacion;
    }

    /**
     * @param totalDiasDomingosVacacion the totalDiasDomingosVacacion to set
     */
    public void setTotalDiasDomingosVacacion(Integer totalDiasDomingosVacacion) {
        this.totalDiasDomingosVacacion = totalDiasDomingosVacacion;
    }

    /**
     * @return the dateFormatDiaNumero
     */
    public SimpleDateFormat getDateFormatDiaNumero() {
        return dateFormatDiaNumero;
    }

    /**
     * @param dateFormatDiaNumero the dateFormatDiaNumero to set
     */
    public void setDateFormatDiaNumero(SimpleDateFormat dateFormatDiaNumero) {
        this.dateFormatDiaNumero = dateFormatDiaNumero;
    }

    /**
     * @return the dateFormatMesNumero
     */
    public SimpleDateFormat getDateFormatMesNumero() {
        return dateFormatMesNumero;
    }

    /**
     * @param dateFormatMesNumero the dateFormatMesNumero to set
     */
    public void setDateFormatMesNumero(SimpleDateFormat dateFormatMesNumero) {
        this.dateFormatMesNumero = dateFormatMesNumero;
    }

    /**
     * @return the dateFormatAnioNumero
     */
    public SimpleDateFormat getDateFormatAnioNumero() {
        return dateFormatAnioNumero;
    }

    /**
     * @param dateFormatAnioNumero the dateFormatAnioNumero to set
     */
    public void setDateFormatAnioNumero(SimpleDateFormat dateFormatAnioNumero) {
        this.dateFormatAnioNumero = dateFormatAnioNumero;
    }

    /**
     * @return the asignacionExcepcionesHorariosDiariosVacaciones
     */
    public AsignacionExcepcionesHorariosDiarios getAsignacionExcepcionesHorariosDiariosVacaciones() {
        return asignacionExcepcionesHorariosDiariosVacaciones;
    }

    /**
     * @param asignacionExcepcionesHorariosDiariosVacaciones the
     * asignacionExcepcionesHorariosDiariosVacaciones to set
     */
    public void setAsignacionExcepcionesHorariosDiariosTab1Vacaciones(AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiariosVacaciones) {
        this.asignacionExcepcionesHorariosDiariosVacaciones = asignacionExcepcionesHorariosDiariosVacaciones;
    }

    /**
     * @return the totalDiasYaAsignados
     */
    public Integer getTotalDiasYaAsignados() {
        return totalDiasYaAsignados;
    }

    /**
     * @param totalDiasYaAsignados the totalDiasYaAsignados to set
     */
    public void setTotalDiasYaAsignados(Integer totalDiasYaAsignados) {
        this.totalDiasYaAsignados = totalDiasYaAsignados;
    }

    /**
     * @return the cedulaFuncionarioVacaciones
     */
    public String getCedulaFuncionarioVacaciones() {
        return cedulaFuncionarioVacaciones;
    }

    /**
     * @param cedulaFuncionarioVacaciones the cedulaFuncionarioVacaciones to set
     */
    public void setCedulaFuncionarioVacaciones(String cedulaFuncionarioVacaciones) {
        this.cedulaFuncionarioVacaciones = cedulaFuncionarioVacaciones;
    }

    /**
     * @return the funcionarioVacaciones
     */
    public Funcionarios getFuncionarioVacaciones() {
        return funcionarioVacaciones;
    }

    /**
     * @param funcionarioVacaciones the funcionarioVacaciones to set
     */
    public void setFuncionarioVacaciones(Funcionarios funcionarioVacaciones) {
        this.funcionarioVacaciones = funcionarioVacaciones;
    }

    /**
     * @return the asignacionExcepcionesHorariosDiariosVacacionesLista
     */
    public List<AsignacionExcepcionesHorariosDiarios> getAsignacionExcepcionesHorariosDiariosVacacionesLista() {
        return asignacionExcepcionesHorariosDiariosVacacionesLista;
    }

    /**
     * @param asignacionExcepcionesHorariosDiariosVacacionesLista the
     * asignacionExcepcionesHorariosDiariosVacacionesLista to set
     */
    public void setAsignacionExcepcionesHorariosDiariosVacacionesLista(List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosVacacionesLista) {
        this.asignacionExcepcionesHorariosDiariosVacacionesLista = asignacionExcepcionesHorariosDiariosVacacionesLista;
    }

    /**
     * @return the dataTableVacaciones
     */
    public HtmlDataTable getDataTableVacaciones() {
        return dataTableVacaciones;
    }

    /**
     * @param dataTableVacaciones the dataTableVacaciones to set
     */
    public void setDataTableVacaciones(HtmlDataTable dataTableVacaciones) {
        this.dataTableVacaciones = dataTableVacaciones;
    }

    /**
     * @return the contadorMarcacion
     */
    public Integer getContadorMarcacion() {
        return contadorMarcacion;
    }

    /**
     * @param contadorMarcacion the contadorMarcacion to set
     */
    public void setContadorMarcacion(Integer contadorMarcacion) {
        this.contadorMarcacion = contadorMarcacion;
    }

    /**
     * @return the cantidadRegistrosError
     */
    public int getCantidadRegistrosError() {
        return cantidadRegistrosError;
    }

    /**
     * @param cantidadRegistrosError the cantidadRegistrosError to set
     */
    public void setCantidadRegistrosError(int cantidadRegistrosError) {
        this.cantidadRegistrosError = cantidadRegistrosError;
    }

    /**
     * @return the detallesErrores
     */
    public List<String> getDetallesErrores() {
        return detallesErrores;
    }

    /**
     * @param detallesErrores the detallesErrores to set
     */
    public void setDetallesErrores(List<String> detallesErrores) {
        this.detallesErrores = detallesErrores;
    }

}
