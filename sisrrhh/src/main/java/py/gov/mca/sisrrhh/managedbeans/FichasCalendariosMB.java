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
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import py.gov.mca.sisrrhh.clasesaux.FechaHora;
import py.gov.mca.sisrrhh.clasesaux.FuncionarioMarcacion;
import py.gov.mca.sisrrhh.clasesaux.MultaCsv;
import py.gov.mca.sisrrhh.entitys.AsignacionExcepcionesHorariosDiarios;
import py.gov.mca.sisrrhh.entitys.Auditoria;
import py.gov.mca.sisrrhh.entitys.EscalaMultas;
import py.gov.mca.sisrrhh.entitys.FichaCalendario;
import py.gov.mca.sisrrhh.entitys.FichaCalendarioPK;
import py.gov.mca.sisrrhh.entitys.Funcionarios;
import py.gov.mca.sisrrhh.entitys.Horarios;
import py.gov.mca.sisrrhh.entitys.Marcacion;
import py.gov.mca.sisrrhh.entitys.TiposExcepciones;
import py.gov.mca.sisrrhh.negocio.ReportePorcentajeMultaCsv;
import py.gov.mca.sisrrhh.negocio.ReportesFichaCalendario;
import py.gov.mca.sisrrhh.negocio.ReportesFuncionarioMarcacion;
import py.gov.mca.sisrrhh.sessionbeans.AsignacionExcepcionesHorariosDiariosSB;
import py.gov.mca.sisrrhh.sessionbeans.AuditoriaSB;
import py.gov.mca.sisrrhh.sessionbeans.EscalaMultasSB;
import py.gov.mca.sisrrhh.sessionbeans.FichaCalendarioSB;
import py.gov.mca.sisrrhh.sessionbeans.FuncionariosSB;
import py.gov.mca.sisrrhh.sessionbeans.HorariosSB;
import py.gov.mca.sisrrhh.sessionbeans.MarcacionSB;
import py.gov.mca.sisrrhh.sessionbeans.TiposExcepcionesSB;

/**
 *
 * @author vinsfran
 */
@Named(value = "fichasCalendariosMB")
@SessionScoped
public class FichasCalendariosMB implements Serializable {

    @EJB
    private FichaCalendarioSB ejbFichaCalendarioSB;

    @EJB
    private FuncionariosSB ejbFuncionariosSB;

    @EJB
    private MarcacionSB ejbMarcacionSB;

    @EJB
    private AuditoriaSB auditoriaSB;

    @EJB
    private AsignacionExcepcionesHorariosDiariosSB ejbAsignacionExcepcionesHorariosDiariosSB;

    @EJB
    private EscalaMultasSB escalaMultasSB;

    @EJB
    private HorariosSB horariosSB;

    @EJB
    private TiposExcepcionesSB tiposExcepcionesSB;

    private final SimpleDateFormat dateFormatFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat dateFormatFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dateFormatFechaGuionBajo = new SimpleDateFormat("dd_MM_yyyy");
    private final SimpleDateFormat dateFormatHora = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat dateFormatDia = new SimpleDateFormat("EEEE", Locale.getDefault());
    private final SimpleDateFormat dateFormatMes = new SimpleDateFormat("MMMM", Locale.getDefault());
    private SimpleDateFormat dateFormatDiaNumero = new SimpleDateFormat("dd");
    private SimpleDateFormat dateFormatMesNumero = new SimpleDateFormat("MM");
    private SimpleDateFormat dateFormatAnioNumero = new SimpleDateFormat("yyyy");
    private Integer numeroMes;
    private Integer numeroCiclo;
    private Integer numeroAnio;
    private Integer numeroTipoMarcacion;
    private Integer diasBoolean;
    private String tipoGeneracion;
    private List<FichaCalendario> fichasCalendarios;
    private Funcionarios funcionarioUsuario;
    private LinkedHashMap<Integer, String> listaMeses;
    private LinkedHashMap<Integer, String> listaCiclos;
    private LinkedHashMap<Integer, String> listaTipoMarcacion;
    private LinkedHashMap<Integer, String> listaSiNo;

    private boolean mostrarComponentesPantallaBuscarRangoFechaCedula;
    private boolean activarComponentes;
    private String fichaVerificada;
    private Integer porcentajeMulta;
    private Integer porcentajeMultaTotal;
    private Integer montoFijo;
    private Integer montoFijoTotal;
    private Integer diasTrabajados;
    private Integer diasComplementarios;
    private Funcionarios funcionario;
    private String cedulaFuncionario;
    private Date fechaDesde;
    private Date fechaHasta;

    private Part file;
    private String nombreArchivo;
    private String tipoReporte;
    private String tipoReporte2;
    private int cantidadRegistros;
    private List<String> cedulasDesdeArchivo;

    private HtmlDataTable dataTableFichasCalendarios;
    private FichaCalendario fichaCalendario;
    private List<Horarios> horarios;
    private List<TiposExcepciones> tiposExcepciones;

    public FichasCalendariosMB() {
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

        this.listaCiclos = new LinkedHashMap<>();
        listaCiclos.put(0, "Enero - Febrero");
        listaCiclos.put(1, "Febrero - Marzo");
        listaCiclos.put(2, "Marzo - Abril");
        listaCiclos.put(3, "Abril - Mayo");
        listaCiclos.put(4, "Mayo - Junio");
        listaCiclos.put(5, "Junio - Julio");
        listaCiclos.put(6, "Julio - Agosto");
        listaCiclos.put(7, "Agosto - Septiembre");
        listaCiclos.put(8, "Septiembre - Octubre");
        listaCiclos.put(9, "Octubre - Noviembre");
        listaCiclos.put(10, "Noviembre - Diciembre");
        listaCiclos.put(11, "Diciembre - Enero");

        this.listaTipoMarcacion = new LinkedHashMap<>();
        listaTipoMarcacion.put(1, "Entrada");
        listaTipoMarcacion.put(2, "Salida");

        this.listaSiNo = new LinkedHashMap<>();
        listaSiNo.put(1, "SI");
        listaSiNo.put(2, "NO");

    }

    @PostConstruct
    public void init() {
        recuperarUsuarioSession();
        prepararFormFichaGeneralMensualNom();
        prepararFormFichaGeneralMensualJor();
    }

    public String prepararUpdateBloqueo() {

        return "/administracion/admin_form_update_bloqueo";
    }

    public void updateBloqueo() {
        fichasCalendarios = ejbFichaCalendarioSB.findAll();
        for (int i = 0; i < fichasCalendarios.size(); i++) {
            fichasCalendarios.get(i).setMarcaBloqueo("NO");
            //System.out.println("NRO: " + (i + 1));
        }
        guardarFichasCalendarios();
    }

    public String prepararFormCrearFichasCalendariosNom() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        setNumeroMes((Integer) cal.get(Calendar.MONTH) - 1);
        setNumeroAnio((Integer) cal.get(Calendar.YEAR));
        setTipoGeneracion("NOM");
        return "/administracion/admin_funcionario_ficha_general_form_creacion_nom";
    }

    public void crearFichasCalendariosNom() throws ParseException {
        int diasDelMes = cantidadDiasDelMes(getNumeroAnio(), getNumeroMes());
        List<Funcionarios> listaFuncionariosActivos = ejbFuncionariosSB.findByEstadoRelacionLaboralFiltro("ACT", getTipoGeneracion());

        Integer mesInt = (getNumeroMes() + 1);
        Integer diaInt;

        String mesDesdeString = mesInt + "";
        if (mesInt < 10) {
            mesDesdeString = "0" + mesInt;
        }

        Date fechaDesdeAux = getDateFormatFecha().parse(01 + "/" + mesDesdeString + "/" + getNumeroAnio());
        Date fechaHastaAux = getDateFormatFecha().parse(diasDelMes + "/" + mesDesdeString + "/" + getNumeroAnio());
        setFichasCalendarios(new ArrayList<FichaCalendario>());

        for (int i = 0; i < listaFuncionariosActivos.size(); i++) {
            List<FichaCalendario> fichasCalendariosAux = getMarcacionesPorCedulaFechaDedeFechaHasta(listaFuncionariosActivos.get(i).getNumeroDocumento(), fechaDesdeAux, fechaHastaAux);
            FichaCalendario fichaCalendario;
            String fechaSeleccionada;
            for (int j = 0; j < diasDelMes; j++) {
                diaInt = (j + 1);
                String diaString = diaInt + "";
                if (diaInt < 10) {
                    diaString = "0" + diaInt;
                }
                String mesString = mesInt + "";
                if (mesInt < 10) {
                    mesString = "0" + mesInt;
                }
                fechaSeleccionada = diaString + "/" + mesString + "/" + getNumeroAnio();
                fichaCalendario = new FichaCalendario();
                fichaCalendario.setFichaCalendarioPK(new FichaCalendarioPK());
                fichaCalendario.getFichaCalendarioPK().setFechaCalendario(getDateFormatFecha().parse(fechaSeleccionada));
                fichaCalendario.getFichaCalendarioPK().setNumeroDocumentoFuncionario(listaFuncionariosActivos.get(i).getNumeroDocumento());
                fichaCalendario.setMesCalendarioNumero((getNumeroMes() + 1));
                fichaCalendario.setAnioCalendarioNumero(getNumeroAnio());
                fichaCalendario.setDiaCalendarioNumero(diaInt);
                fichaCalendario.setDiaCalendarioLetra(dateFormatDia.format(getDateFormatFecha().parse(fechaSeleccionada)).toUpperCase());
                fichaCalendario.setHorarioAsignado(listaFuncionariosActivos.get(i).getHorarioNormal());
                if (fichasCalendariosAux.isEmpty()) {
                    fichaCalendario.setHoraEntrada(null);
                    fichaCalendario.setHoraSalida(null);
                    fichaCalendario.setHoraNoDefinida(null);
                } else {
                    for (int k = 0; k < fichasCalendariosAux.size(); k++) {
                        String fecha1 = getDateFormatFecha().format(fichaCalendario.getFichaCalendarioPK().getFechaCalendario());
                        String fecha2 = getDateFormatFecha().format(fichasCalendariosAux.get(k).getFichaCalendarioPK().getFechaCalendario());
                        if (fecha1.equals(fecha2)) {
                            fichaCalendario.setHoraEntrada(fichasCalendariosAux.get(k).getHoraEntrada());
                            fichaCalendario.setHoraSalida(fichasCalendariosAux.get(k).getHoraSalida());
                            fichaCalendario.setHoraNoDefinida(fichasCalendariosAux.get(k).getHoraNoDefinida());
                            break;
                        }

                    }
                }
                fichaCalendario.setPorcentajeMulta(0);
                fichaCalendario.setMontoFijo(0);
                fichaCalendario.setMarcaBloqueo("NO");
                getFichasCalendarios().add(fichaCalendario);
            }
        }
        guardarFichasCalendarios();
    }

    public String prepararFormCrearFichasCalendariosJor() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        setNumeroCiclo((Integer) cal.get(Calendar.MONTH) - 1);
        setNumeroAnio((Integer) cal.get(Calendar.YEAR));
        setTipoGeneracion("JOR");
        return "/administracion/admin_funcionario_ficha_general_form_creacion_jor";
    }

    public void crearFichasCalendariosJor() throws ParseException {
        List<Funcionarios> listaFuncionariosActivos = ejbFuncionariosSB.findByEstadoRelacionLaboralFiltro("ACT", getTipoGeneracion());

        Integer mesIni = (getNumeroCiclo() + 1);
        Integer diaInt;

        String mesDesdeString = mesIni + "";
        if (mesIni < 10) {
            mesDesdeString = "0" + mesIni;
        }

        Integer mesHas = (mesIni + 1);
        String mesHastaString = (mesHas + 1) + "";
        if (mesHas < 10) {
            mesHastaString = "0" + mesHas;
        }

        Integer anioIni = getNumeroAnio();

        Integer anioFin = getNumeroAnio();

        if (mesIni.equals(12)) {
            mesHastaString = "01";
            anioFin = (anioFin + 1);
        }

        Date fechaDesdeAux = getDateFormatFecha().parse(11 + "/" + mesDesdeString + "/" + anioIni);
        Date fechaHastaAux = getDateFormatFecha().parse(10 + "/" + mesHastaString + "/" + anioFin);

        setFichasCalendarios(new ArrayList<FichaCalendario>());

        List<Date> calendarioJor = new ArrayList<>();

        Calendar cal = new GregorianCalendar();
        cal.setLenient(false);

        Date fechaIni = fechaDesdeAux;
        Date fechaFin = fechaHastaAux;

        int ban = 0;
        while (ban == 0) {
            if (fechaIni.after(fechaFin)) {
                ban = 1;
            } else {
                calendarioJor.add(fechaIni);
                cal.setTime(fechaIni);
                cal.add(Calendar.DATE, 1);
                fechaIni = calendarToDate(cal);
            }
        }

        for (int i = 0; i < listaFuncionariosActivos.size(); i++) {
            List<FichaCalendario> fichasCalendariosAux = getMarcacionesPorCedulaFechaDedeFechaHasta(listaFuncionariosActivos.get(i).getNumeroDocumento(), fechaDesdeAux, fechaHastaAux);
            FichaCalendario fichaCalendario;

            for (int j = 0; j < calendarioJor.size(); j++) {
                fichaCalendario = new FichaCalendario();
                fichaCalendario.setFichaCalendarioPK(new FichaCalendarioPK());
                fichaCalendario.getFichaCalendarioPK().setFechaCalendario(calendarioJor.get(j));
                fichaCalendario.getFichaCalendarioPK().setNumeroDocumentoFuncionario(listaFuncionariosActivos.get(i).getNumeroDocumento());
                fichaCalendario.setMesCalendarioNumero(Integer.parseInt(dateFormatMesNumero.format(calendarioJor.get(j))));
                fichaCalendario.setAnioCalendarioNumero(Integer.parseInt(dateFormatAnioNumero.format(calendarioJor.get(j))));
                fichaCalendario.setDiaCalendarioNumero(Integer.parseInt(dateFormatDiaNumero.format(calendarioJor.get(j))));
                fichaCalendario.setDiaCalendarioLetra(dateFormatDia.format(calendarioJor.get(j)).toUpperCase());
                fichaCalendario.setHorarioAsignado(listaFuncionariosActivos.get(i).getHorarioNormal());
                if (fichasCalendariosAux.isEmpty()) {
                    fichaCalendario.setHoraEntrada(null);
                    fichaCalendario.setHoraSalida(null);
                    fichaCalendario.setHoraNoDefinida(null);
                } else {
                    for (int k = 0; k < fichasCalendariosAux.size(); k++) {
                        String fecha1 = getDateFormatFecha().format(fichaCalendario.getFichaCalendarioPK().getFechaCalendario());
                        String fecha2 = getDateFormatFecha().format(fichasCalendariosAux.get(k).getFichaCalendarioPK().getFechaCalendario());
                        if (fecha1.equals(fecha2)) {
                            fichaCalendario.setHoraEntrada(fichasCalendariosAux.get(k).getHoraEntrada());
                            fichaCalendario.setHoraSalida(fichasCalendariosAux.get(k).getHoraSalida());
                            fichaCalendario.setHoraNoDefinida(fichasCalendariosAux.get(k).getHoraNoDefinida());
                            if (fichasCalendariosAux.get(k).getHoraEntrada() != null || fichasCalendariosAux.get(k).getHoraSalida() != null || fichasCalendariosAux.get(k).getHoraNoDefinida() != null) {
                                fichaCalendario.setDiaTrabajado(true);
                            }
                            fichaCalendario.setDiaComplementario(false);
                            break;
                        }

                    }
                }
                fichaCalendario.setPorcentajeMulta(0);
                fichaCalendario.setMontoFijo(0);

                fichaCalendario.setMarcaBloqueo("NO");
                getFichasCalendarios().add(fichaCalendario);
            }
        }
        guardarFichasCalendarios();
    }

    public void guardarFichasCalendarios() {
        if (getFichasCalendarios() != null) {
            // List<FichaCalendario> listaAux = ejbFichaCalendarioSB.findByFechaCalendarioNumeroDocumentoFuncionarioLista(getFichasCalendarios());

            // if (listaAux.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se generaron fichas nueva", ""));
            //  } else {

            if (ejbFichaCalendarioSB.udtadeMultiple(getFichasCalendarios()).equals("OK")) {
                String descriAudit = "Fecha: " + new Date() + ", "
                        + "Total Registros insertados: " + getFichasCalendarios().size();
                guardarAuditoria(new Date(), getFuncionarioUsuario().getUsuario(), getFuncionarioUsuario().getRolUsuario().getNombre(), "Insert", "ficha_calendario", "", descriAudit);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se generaron fichas nueva", "Total: " + getFichasCalendarios().size()));
            }
            //}
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al generar las fichas", ""));
        }
        fichasCalendarios = null;
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

    public List<FichaCalendario> getMarcacionesPorCedulaFechaDedeFechaHasta(String cedula, Date fechaDesde, Date fechaHasta) {

        List<Marcacion> marcaciones = ejbMarcacionSB.findByCedulaFechaDesdeFechaHasta(cedula, fechaDesde, fechaHasta);
        List<Marcacion> marcacionesAux;

        List<FichaCalendario> fichaCalendarioRetorno = new ArrayList<>();

        String control = "";
        FichaCalendario fichaCalendario;
        for (int i = 0; i < marcaciones.size(); i++) {
            if (!control.equals(marcaciones.get(i).getFechaMarcacionChar().toString())) {
                Date fechaInicial;
                try {
                    fechaInicial = getDateFormatFecha().parse(getDateFormatFecha().format(marcaciones.get(i).getMarcacionPK().getFechaHoraMarcacion()));
                    marcacionesAux = new ArrayList<>();
                    for (int j = 0; j < marcaciones.size(); j++) {
                        if (fechaInicial.equals(getDateFormatFecha().parse(getDateFormatFecha().format(marcaciones.get(j).getMarcacionPK().getFechaHoraMarcacion())))) {
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

    public String prepararFormFichaGeneralMensualNom() {
        dataTableFichasCalendarios = new HtmlDataTable();
        mostrarComponentesPantallaBuscarRangoFechaCedula = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        numeroMes = cal.get(Calendar.MONTH) - 1;
        numeroAnio = cal.get(Calendar.YEAR);
        porcentajeMultaTotal = 0;
        return "admin_funcionario_ficha_general_nom";
    }

    public String moverHorarioMarcacionFichaGeneral() {
        fichaCalendario = new FichaCalendario();
        fichaCalendario = (FichaCalendario) getDataTableFichasCalendarios().getRowData();
        return "admin_funcionario_mover_tipo_marcacion_ficha_general";
    }

    public String guardarMoverHorarioMarcacionFichaGeneral() {
        String retorno = "";
        if (numeroTipoMarcacion != null) {
            String horarioMovido;
            if (numeroTipoMarcacion == 1) {
                fichaCalendario.setHoraEntrada(fichaCalendario.getHoraNoDefinida());
                horarioMovido = "HoraEntrada: " + fichaCalendario.getHoraEntrada();
            } else {
                fichaCalendario.setHoraSalida(fichaCalendario.getHoraNoDefinida());
                horarioMovido = "HoraSalida: " + fichaCalendario.getHoraSalida();
            }
            fichaCalendario.setHoraNoDefinida(null);
            ejbFichaCalendarioSB.udtade(fichaCalendario);
            String descriAudit = "Fecha: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario() + ", "
                    + horarioMovido;
            guardarAuditoria(new Date(),
                    funcionarioUsuario.getUsuario(),
                    funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update",
                    "ficha_calendario",
                    fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario() + "-" + fichaCalendario.getFichaCalendarioPK().getFechaCalendario(),
                    descriAudit);
            dataTableFichasCalendarios = new HtmlDataTable();
            numeroTipoMarcacion = 0;
            retorno = redireccion();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un tipo de marcación", ""));
        }
        return retorno;
    }

    public String cambiarHorarioAsignadoFichaGeneral() {
        fichaCalendario = new FichaCalendario();
        fichaCalendario = (FichaCalendario) getDataTableFichasCalendarios().getRowData();
        return "admin_funcionario_cambiar_horario_asignado_ficha_general";
    }

    public String guardarCambiarHorarioAsignadoFichaGeneral() {
        String retorno = "";
        if (!StringUtils.isBlank(fichaCalendario.getMotivoCambioHora())) {
            ejbFichaCalendarioSB.udtade(fichaCalendario);
            String descriAudit = "Fecha: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario() + ", "
                    + fichaCalendario.getMotivoCambioHora();
            guardarAuditoria(new Date(),
                    funcionarioUsuario.getUsuario(),
                    funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update",
                    "ficha_calendario",
                    fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario() + "-" + fichaCalendario.getFichaCalendarioPK().getFechaCalendario(),
                    descriAudit);
            retorno = redireccion();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El campo Comentario no puede estar vacio. ", ""));
        }
        return retorno;
    }

    public String agregarMotivoFichaGeneral() {
        fichaCalendario = new FichaCalendario();
        fichaCalendario = (FichaCalendario) getDataTableFichasCalendarios().getRowData();
        if (fichaCalendario.getCodigoExcepcion() == null) {
            fichaCalendario.setCodigoExcepcion(new TiposExcepciones());
        }
        return "admin_funcionario_agregar_motivo_ficha_general";
    }

    public String guardarAgregarMotivoFichaGeneral() {
        String retorno = "";
        if (!StringUtils.isBlank(fichaCalendario.getMotivo())) {
            String descriAudit = "Fecha: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario() + ", "
                    + fichaCalendario.getMotivo();
            guardarAuditoria(new Date(),
                    funcionarioUsuario.getUsuario(),
                    funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update",
                    "ficha_calendario",
                    fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario() + "-" + fichaCalendario.getFichaCalendarioPK().getFechaCalendario(),
                    descriAudit);
            ejbFichaCalendarioSB.udtade(fichaCalendario);
            retorno = redireccion();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El campo Comentario no puede estar vacio. ", ""));
        }
        return retorno;
    }

    public String agregarProcentajeMultaFichaGeneral() {
        fichaCalendario = new FichaCalendario();
        porcentajeMulta = null;
        fichaCalendario = (FichaCalendario) getDataTableFichasCalendarios().getRowData();
        if (fichaCalendario.getCodigoExcepcion() == null) {
            fichaCalendario.setCodigoExcepcion(new TiposExcepciones());
        }
        return "admin_funcionario_agregar_porcentaje_multa_ficha_general";
    }

    public String guardarAgregarProcentajeMultaFichaGeneral() {
        String retorno = "";
        if (porcentajeMulta != null && porcentajeMulta > -1 && porcentajeMulta < 101) {
            fichaCalendario.setPorcentajeMulta(porcentajeMulta);
            String descriAudit = "Fecha: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario() + ", PorcentajeMulta: "
                    + fichaCalendario.getPorcentajeMulta();
            guardarAuditoria(new Date(),
                    funcionarioUsuario.getUsuario(),
                    funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update",
                    "ficha_calendario",
                    fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario() + "-" + fichaCalendario.getFichaCalendarioPK().getFechaCalendario(),
                    descriAudit);
            ejbFichaCalendarioSB.udtade(fichaCalendario);
            retorno = redireccion();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Valor no valido para Cambiar Pocentaje Actual. ", "Debe estar entre 0 y 100"));
        }
        calcutarTotalPorcentaje();
        return retorno;
    }

    public String agregarMontoFijoMultaFichaGeneral() {
        fichaCalendario = new FichaCalendario();
        montoFijo = null;
        fichaCalendario = (FichaCalendario) getDataTableFichasCalendarios().getRowData();
        if (fichaCalendario.getCodigoExcepcion() == null) {
            fichaCalendario.setCodigoExcepcion(new TiposExcepciones());
        }
        return "admin_funcionario_agregar_monto_fijo_multa_ficha_general";
    }

    public String guardarAgregarMontoFijoMultaFichaGeneral() {
        String retorno = "";
        if (montoFijo != null && montoFijo > -1) {
            fichaCalendario.setMontoFijo(montoFijo);
            String descriAudit = "Fecha: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario() + ", MontoFijoMulta: "
                    + fichaCalendario.getMontoFijo();
            guardarAuditoria(new Date(),
                    funcionarioUsuario.getUsuario(),
                    funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update",
                    "ficha_calendario",
                    fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario() + "-" + fichaCalendario.getFichaCalendarioPK().getFechaCalendario(),
                    descriAudit);
            ejbFichaCalendarioSB.udtade(fichaCalendario);
            retorno = redireccion();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Valor no valido para Cambiar Monto Fijo Actual. ", "No puede estar vacio"));
        }
        calcutarTotalPorcentaje();
        return retorno;
    }

    public String diaTrabajadoFichaGeneral() {
        diasBoolean = null;
        fichaCalendario = new FichaCalendario();
        montoFijo = null;
        fichaCalendario = (FichaCalendario) getDataTableFichasCalendarios().getRowData();
        if (fichaCalendario.getCodigoExcepcion() == null) {
            fichaCalendario.setCodigoExcepcion(new TiposExcepciones());
        }
        return "admin_funcionario_dia_trabajado_marcacion_ficha_general";
    }

    public String guardarDiaTrabajadoMarcacionFichaGeneral() {
        String retorno = "";
        if (diasBoolean != null) {
            String diaTrabajado = "Dia Trabajado: ";
            if (diasBoolean == 1) {
                fichaCalendario.setDiaTrabajado(true);
                diaTrabajado = diaTrabajado + "SI";
            } else {
                fichaCalendario.setDiaTrabajado(false);
                diaTrabajado = diaTrabajado + "NO";
            }
            ejbFichaCalendarioSB.udtade(fichaCalendario);
            String descriAudit = "Fecha: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario() + ", "
                    + diaTrabajado;
            guardarAuditoria(new Date(),
                    funcionarioUsuario.getUsuario(),
                    funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update",
                    "ficha_calendario",
                    fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario() + "-" + fichaCalendario.getFichaCalendarioPK().getFechaCalendario(),
                    descriAudit);
            diasBoolean = 0;
            retorno = redireccion();
            calcutarDiasTrabajados();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar valor para Cambio de estado", ""));
        }
        return retorno;
    }

    public String diaComplementarioFichaGeneral() {
        diasBoolean = null;
        fichaCalendario = new FichaCalendario();
        montoFijo = null;
        fichaCalendario = (FichaCalendario) getDataTableFichasCalendarios().getRowData();
        if (fichaCalendario.getCodigoExcepcion() == null) {
            fichaCalendario.setCodigoExcepcion(new TiposExcepciones());
        }
        return "admin_funcionario_dia_complementario_marcacion_ficha_general";
    }

    public String guardarDiaComplementarioMarcacionFichaGeneral() {
        String retorno = "";
        if (diasBoolean != null) {
            String diaComplementario = "Dia Complementario: ";
            if (diasBoolean == 1) {
                fichaCalendario.setDiaComplementario(true);
                diaComplementario = diaComplementario + "SI";
            } else {
                fichaCalendario.setDiaComplementario(false);
                diaComplementario = diaComplementario + "NO";
            }
            ejbFichaCalendarioSB.udtade(fichaCalendario);
            String descriAudit = "Fecha: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario() + ", "
                    + diaComplementario;
            guardarAuditoria(new Date(),
                    funcionarioUsuario.getUsuario(),
                    funcionarioUsuario.getRolUsuario().getNombre(),
                    "Update",
                    "ficha_calendario",
                    fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario() + "-" + fichaCalendario.getFichaCalendarioPK().getFechaCalendario(),
                    descriAudit);
            diasBoolean = 0;
            retorno = redireccion();
            calcutarDiasComplementarios();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar valor para Cambio de estado", ""));
        }
        return retorno;
    }

    public String cancelarModificarFichaGeneral() {
        fichaCalendario = null;
        return redireccion();
    }

    public String redireccion() {
        dataTableFichasCalendarios = new HtmlDataTable();
        List<FichaCalendario> listAux;
        String retorno;
        if (funcionario.getRelacionLaboral().getRelacionLaboral().equals("JOR")) {
            listAux = getFichaCalendarioJor(getCedulaFuncionario(), fechaDesde, fechaHasta);

            //fichasCalendarios = getFichaCalendarioJor(getCedulaFuncionario(), fechaDesde, fechaHasta);
            retorno = "admin_funcionario_ficha_general_jor";
        } else {
            listAux = getFichaCalendarioNom(getCedulaFuncionario(), (numeroMes + 1), numeroAnio);

            //fichasCalendarios = getFichaCalendarioNom(getCedulaFuncionario(), (numeroMes + 1), numeroAnio);
            retorno = "admin_funcionario_ficha_general_nom";
        }
        for (int i = 0; i < listAux.size(); i++) {
            if (listAux.get(i).getPorcentajeMulta() != null && listAux.get(i).getPorcentajeMulta() > 0) {
                //fichasCalendarios.get(i).setPorcentajeMulta(listAux.get(i).getPorcentajeMulta());
                listAux.get(i).setPorcentajeMulta(fichasCalendarios.get(i).getPorcentajeMulta());
            }
            if (listAux.get(i).getMontoFijo() != null && listAux.get(i).getMontoFijo() > 0) {
                //fichasCalendarios.get(i).setMontoFijo(listAux.get(i).getMontoFijo());
                listAux.get(i).setMontoFijo(fichasCalendarios.get(i).getMontoFijo());
            }
        }
        fichasCalendarios.clear();
        fichasCalendarios = listAux;
        return retorno;
    }

    public void buscarFichaPorMesAnioCedulaNom() throws ParseException {
        int dias = cantidadDiasDelMes(numeroAnio, numeroMes);
        setFuncionario(getFuncionario(getCedulaFuncionario()));
        FichaCalendario fichaCalendarioAux;
        String fechaSeleccionada;
        if (getFuncionario() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe el funcionario", ""));
            prepararFormFichaGeneralMensualNom();
        } else if (getFuncionario().getRelacionLaboral().getRelacionLaboral().equals("JOR")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El funcionario es Jornalero", ""));
            prepararFormFichaGeneralMensualNom();
        } else {

            mostrarComponentesPantallaBuscarRangoFechaCedula = true;
            Integer mesInt = (numeroMes + 1);
            Integer diaInt;

            fichasCalendarios = getFichaCalendarioNom(getCedulaFuncionario(), mesInt, numeroAnio);
            activarComponentes = false;
            for (int k = 0; k < fichasCalendarios.size(); k++) {
                if (fichasCalendarios.get(k).getMarcaBloqueo().equals("SI")) {
                    activarComponentes = true;
                }
            }

            fichaVerificada = "NO";
            if (fichasCalendarios.get(0).getMarcaFichaVerificada() != null && fichasCalendarios.get(0).getMarcaFichaVerificada()) {
                fichaVerificada = "SI";
            }
            /*if (fichasCalendarios.isEmpty()) {
            //System.out.println("fichasCalendarios" + fichasCalendarios.get(0).getPorcentajeMulta());
            fichasCalendarios = new ArrayList<>();
            for (int i = 0; i < dias; i++) {
                diaInt = (i + 1);
                String diaString = diaInt + "";
                if (diaInt < 10) {
                    diaString = "0" + diaInt;
                }

                String mesString = mesInt + "";
                if (mesInt < 10) {
                    mesString = "0" + mesInt;
                }
                fechaSeleccionada = diaString + "/" + mesString + "/" + numeroAnio;

                fichaCalendario = new FichaCalendario();
                fichaCalendario.setFichaCalendarioPK(new FichaCalendarioPK());
                fichaCalendario.getFichaCalendarioPK().setFechaCalendario(fechaSeleccionada);
                fichaCalendario.getFichaCalendarioPK().setNumeroDocumentoFuncionario(funcionario.getNumeroDocumento());

                fichaCalendario.setDiaCalendarioNumero(diaInt);
                fichaCalendario.setDiaCalendarioLetra(dateFormatDia.format(dateFormatFecha.parse(fechaSeleccionada)).toUpperCase());

                fichaCalendario.setHorarioAsignado(funcionario.getHorarioNormal());
                fichaCalendario.setHoraEntrada("");
                fichaCalendario.setHoraSalida("");
                fichaCalendario.setHoraNoDefinida("");
                fichaCalendario.setPorcentajeMulta(0);

                getFichasCalendarios().add(fichaCalendario);
            }

        }*/

 /* Date fechaDesdeAux = dateFormatFecha.parse(getFichasCalendarios().get(0).getFichaCalendarioPK().getFechaCalendario());
        Date fechaHastaAux = dateFormatFecha.parse(getFichasCalendarios().get((getFichasCalendarios().size() - 1)).getFichaCalendarioPK().getFechaCalendario());

        List<FichaCalendario> fichasCalendariosAux = getMarcacionesPorCedulaFechaDedeFechaHasta(getCedulaFuncionario(), fechaDesdeAux, fechaHastaAux);
        for (int i = 0; i < getFichasCalendarios().size(); i++) {
            for (int j = 0; j < fichasCalendariosAux.size(); j++) {
                if (getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario().equals(fichasCalendariosAux.get(j).getFichaCalendarioPK().getFechaCalendario())) {
                    getFichasCalendarios().get(i).setHoraEntrada(fichasCalendariosAux.get(j).getHoraEntrada());
                    getFichasCalendarios().get(i).setHoraSalida(fichasCalendariosAux.get(j).getHoraSalida());
                    getFichasCalendarios().get(i).setHoraNoDefinida(fichasCalendariosAux.get(j).getHoraNoDefinida());
                }
            }
        }*/
            List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiarios = getAsignacionesHorariosPorFuncionarioNom(getCedulaFuncionario(), mesInt, numeroAnio);
            if (!asignacionExcepcionesHorariosDiarios.isEmpty()) {
                for (int i = 0; i < getFichasCalendarios().size(); i++) {
                    for (int j = 0; j < asignacionExcepcionesHorariosDiarios.size(); j++) {
                        if (asignacionExcepcionesHorariosDiarios.get(j).getEstadoHorarioAsignado().getCodigo().equals("ACT")) {
                            Date fechaCal = getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario();
                            //Verificar si fechaCal es igual a fecha de asignacion
                            if (fechaCal.equals(asignacionExcepcionesHorariosDiarios.get(j).getAsignacionExcepcionesHorariosDiariosPK().getFechaAsignacion())) {
                                getFichasCalendarios().get(i).setHorarioAsignado(asignacionExcepcionesHorariosDiarios.get(j).getHorarioAsignado());
                                getFichasCalendarios().get(i).setMotivo(asignacionExcepcionesHorariosDiarios.get(j).getTiposExcepciones().getDescripcionExcepcion());
                            }
                        } else {
                            getFichasCalendarios().get(i).setHorarioAsignado(getFuncionario().getHorarioNormal());
                            getFichasCalendarios().get(i).setMotivo("");

                        }
                    }
                }
            }

            //calcularMultas();
            calcutarTotalPorcentaje();
            calcutarMotoFijoTotal();
            calcutarDiasTrabajados();
            calcutarDiasComplementarios();

        }
    }

    public String prepararFormFichaGeneralMensualJor() {
        dataTableFichasCalendarios = new HtmlDataTable();
        setMostrarComponentesPantallaBuscarRangoFechaCedula(false);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        numeroCiclo = cal.get(Calendar.MONTH) - 1;
        numeroAnio = cal.get(Calendar.YEAR);
        setPorcentajeMultaTotal((Integer) 0);
        return "admin_funcionario_ficha_general_jor";
    }

    public void buscarFichaPorPeriodoCedulaJor() throws ParseException {
        setFuncionario(getFuncionario(getCedulaFuncionario()));
        if (getFuncionario() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe el funcionario", ""));
            prepararFormFichaGeneralMensualJor();
        } else if (!getFuncionario().getRelacionLaboral().getRelacionLaboral().equals("JOR")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El funcionario no es Jornalero", ""));
            prepararFormFichaGeneralMensualJor();
        } else {

            mostrarComponentesPantallaBuscarRangoFechaCedula = true;

            calcularPeriodo("JOR");

            fichasCalendarios = getFichaCalendarioJor(getCedulaFuncionario(), fechaDesde, fechaHasta);
            activarComponentes = false;
            for (int k = 0; k < fichasCalendarios.size(); k++) {
                if (fichasCalendarios.get(k).getMarcaBloqueo().equals("SI")) {
                    activarComponentes = true;
                }
            }

            fichaVerificada = "NO";
            if (fichasCalendarios.get(0).getMarcaFichaVerificada() != null && fichasCalendarios.get(0).getMarcaFichaVerificada()) {
                fichaVerificada = "SI";
            }

            List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiarios = getAsignacionesHorariosPorFuncionarioJor(getCedulaFuncionario(), fechaDesde, fechaHasta);
            if (!asignacionExcepcionesHorariosDiarios.isEmpty()) {
                for (int i = 0; i < getFichasCalendarios().size(); i++) {
                    for (int j = 0; j < asignacionExcepcionesHorariosDiarios.size(); j++) {
                        if (asignacionExcepcionesHorariosDiarios.get(j).getEstadoHorarioAsignado().getCodigo().equals("ACT")) {
                            Date fechaCal = getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario();
                            if (fechaCal.equals(asignacionExcepcionesHorariosDiarios.get(j).getAsignacionExcepcionesHorariosDiariosPK().getFechaAsignacion())) {
                                getFichasCalendarios().get(i).setHorarioAsignado(asignacionExcepcionesHorariosDiarios.get(j).getHorarioAsignado());
                                getFichasCalendarios().get(i).setMotivo(asignacionExcepcionesHorariosDiarios.get(j).getTiposExcepciones().getDescripcionExcepcion());
                            }
                        } else {
                            getFichasCalendarios().get(i).setHorarioAsignado(getFuncionario().getHorarioNormal());
                            getFichasCalendarios().get(i).setMotivo("");
                        }
                    }
                }
            }
            //calcularMultas();
            calcutarTotalPorcentaje();
            calcutarMotoFijoTotal();
            calcutarDiasTrabajados();
            calcutarDiasComplementarios();
        }
    }

    public void calcularMultas() {
        List<EscalaMultas> listaEscalaMultas = escalaMultasSB.findByTipoEscala("PORCENTAJE");

        for (int i = 0; i < getFichasCalendarios().size(); i++) {
            String anioAux;
            String mesAux;
            String diaAux;
            String horaInicio;
            String horaFin;
            FechaHora f;
            if (!StringUtils.isBlank(String.valueOf(getFichasCalendarios().get(i).getAnioCalendarioNumero())) && !String.valueOf(getFichasCalendarios().get(i).getAnioCalendarioNumero()).isEmpty()) {
                anioAux = String.valueOf(getFichasCalendarios().get(i).getAnioCalendarioNumero());
                if (!StringUtils.isBlank(String.valueOf(getFichasCalendarios().get(i).getMesCalendarioNumero())) && !String.valueOf(getFichasCalendarios().get(i).getMesCalendarioNumero()).isEmpty()) {
                    mesAux = String.valueOf(getFichasCalendarios().get(i).getMesCalendarioNumero());
                    if (!StringUtils.isBlank(String.valueOf(getFichasCalendarios().get(i).getDiaCalendarioNumero())) && !String.valueOf(getFichasCalendarios().get(i).getDiaCalendarioNumero()).isEmpty()) {
                        diaAux = String.valueOf(getFichasCalendarios().get(i).getDiaCalendarioNumero());

                        if (!StringUtils.isBlank(getFichasCalendarios().get(i).getHorarioAsignado().getHoraEntrada()) && !getFichasCalendarios().get(i).getHorarioAsignado().getHoraEntrada().isEmpty()) {
                            if (!getFichasCalendarios().get(i).getHorarioAsignado().getHorario().equals("00000000")) {
                                horaInicio = getFichasCalendarios().get(i).getHorarioAsignado().getHoraEntrada();
                                if (getFichasCalendarios().get(i).getHoraEntrada() != null) {
                                    horaFin = dateFormatHora.format(getFichasCalendarios().get(i).getHoraEntrada());
                                    f = new FechaHora();
                                    f.calcularHorasMinutosMismoDia(anioAux, mesAux, diaAux, horaInicio, horaFin);
                                    long hor = f.getMinutos() / 60;
                                    long rest = f.getMinutos() % 60;
                                    if (f.getMinutos() < 0.0) {
//                                        System.out.println("LLEGADA ANTES");
//                                        System.out.println("Horas: " + (hor * -1) + " Minutos: " + (rest * -1));
//                                        System.out.println("------");
                                    } else {
                                        String h = "";
                                        String m = "";
                                        Integer por = 0;

                                        for (int j = 0; j < listaEscalaMultas.size(); j++) {
                                            if (f.getMinutos() >= listaEscalaMultas.get(j).getCantidadMinutosDesde()) {
                                                h = hor + "";
                                                m = rest + "";
                                                por = listaEscalaMultas.get(j).getPorcentajeMulta().intValue();
                                            }

                                            if (f.getMinutos() >= listaEscalaMultas.get(j).getCantidadMinutosDesde() && f.getMinutos() <= listaEscalaMultas.get(j).getCantidadMinutosHasta()) {
                                                h = hor + "";
                                                m = rest + "";
                                                por = listaEscalaMultas.get(j).getPorcentajeMulta().intValue();
                                            }

                                        }

                                        if (por > 0) {
                                            System.out.println("LLEGADA TARDIA");
                                            System.out.println("Fecha: " + getDateFormatFecha().format(getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario()));
                                            System.out.println("Horas: " + h + " Minutos: " + m);

                                            System.out.println("PORCENTAJE: " + por);
                                            System.out.println("------");
                                            getFichasCalendarios().get(i).setPorcentajeMulta(por);
                                            //ejbFichaCalendarioSB.udtade(getFichasCalendarios().get(i));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        calcutarTotalPorcentaje();
        calcutarMotoFijoTotal();
    }

    public Integer cantidadDiasDelMes(Integer anio, Integer mes) {
        Calendar cal = new GregorianCalendar(anio, mes, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    public long cantidadDiasDelPeriodo(Date fechaIni, Date fechaFin) {
        long milSecsPorDia = 24 * 60 * 60 * 1000; //Milisegundos al día 
        long diferencia = (fechaFin.getTime() - fechaIni.getTime()) / milSecsPorDia;
        return diferencia;
    }

    //Convert Date to Calendar
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    //Convert Calendar to Date
    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    private void calcularPeriodo(String tipoFuncionario) throws ParseException {
        String diaIni;
        String diaHas;
        Integer mesIni;
        Integer mesHas;
        String mesDesdeString;
        String mesHastaString;
        Integer anioIni = getNumeroAnio();
        Integer anioFin = getNumeroAnio();

        if (tipoFuncionario.equals("JOR")) {
            diaIni = "11";
            diaHas = "10";
            mesIni = (getNumeroCiclo() + 1);
            mesHas = (mesIni + 1);

            mesDesdeString = mesIni + "";
            if (mesIni < 10) {
                mesDesdeString = "0" + mesIni;
            }

            mesHastaString = mesHas + "";
            if (mesHas < 10) {
                mesHastaString = "0" + mesHas;
            }

            if (mesIni.equals(12)) {
                mesHastaString = "01";
                anioFin = (anioFin + 1);
            }
        } else {
            diaIni = "01";
            diaHas = cantidadDiasDelMes(numeroAnio, numeroMes) + "";
            mesIni = (getNumeroMes() + 1);
            mesHas = mesIni;

            mesDesdeString = mesIni + "";
            if (mesIni < 10) {
                mesDesdeString = "0" + mesIni;
            }

            mesHastaString = mesHas + "";
            if (mesHas < 10) {
                mesHastaString = "0" + mesHas;
            }
        }

        fechaDesde = getDateFormatFecha().parse(diaIni + "/" + mesDesdeString + "/" + anioIni);
        fechaHasta = getDateFormatFecha().parse(diaHas + "/" + mesHastaString + "/" + anioFin);
    }

    public Funcionarios recuperarUsuarioSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        setFuncionarioUsuario((Funcionarios) session.getAttribute("funcionarioUsuario"));
        return getFuncionarioUsuario();
    }

    public Funcionarios getFuncionario(String cedula) {
        Funcionarios funcionarioRetorno = ejbFuncionariosSB.findByNumeroDocumento(cedula);
        return funcionarioRetorno;
    }

    public List<FichaCalendario> getFichaCalendarioNom(String cedula, Integer mes, Integer anio) {
        List<FichaCalendario> fichaCalendarioRetorno = ejbFichaCalendarioSB.findByCedulaMesAnio(cedula, mes, anio);
        return fichaCalendarioRetorno;
    }

    public List<FichaCalendario> getFichaCalendarioJor(String cedula, Date fechaIni, Date fechaFin) {
        List<FichaCalendario> fichaCalendarioRetorno = ejbFichaCalendarioSB.findByCedulaFechaDesdeFechaHasta(cedula, fechaIni, fechaFin);
        return fichaCalendarioRetorno;
    }

    public List<AsignacionExcepcionesHorariosDiarios> getAsignacionesHorariosPorFuncionarioNom(String cedula, Integer mes, Integer anio) {
        List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosRetorno = ejbAsignacionExcepcionesHorariosDiariosSB.findByCedulaMesAnio(cedula, mes, anio);
        return asignacionExcepcionesHorariosDiariosRetorno;
    }

    public List<AsignacionExcepcionesHorariosDiarios> getAsignacionesHorariosPorFuncionarioJor(String cedula, Date fechaIni, Date fechaFin) {
        List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosRetorno = ejbAsignacionExcepcionesHorariosDiariosSB.findByCedulaFechaDesdeFechaHasta(cedula, fechaIni, fechaFin);
        return asignacionExcepcionesHorariosDiariosRetorno;
    }

    public void calcutarTotalPorcentaje() {
        porcentajeMultaTotal = 0;
        if (fichasCalendarios != null) {
            for (int i = 0; i < fichasCalendarios.size(); i++) {
                if (fichasCalendarios.get(i).getPorcentajeMulta() != null) {
                    porcentajeMultaTotal = porcentajeMultaTotal + fichasCalendarios.get(i).getPorcentajeMulta();
                }
            }
        }
    }

    public void calcutarMotoFijoTotal() {
        montoFijoTotal = 0;
        if (fichasCalendarios != null) {
            for (int i = 0; i < fichasCalendarios.size(); i++) {
                if (fichasCalendarios.get(i).getMontoFijo() != null) {
                    montoFijoTotal = montoFijoTotal + fichasCalendarios.get(i).getMontoFijo();
                }
            }
        }
    }

    public void calcutarDiasTrabajados() {
        diasTrabajados = 0;
        if (fichasCalendarios != null) {
            for (int i = 0; i < fichasCalendarios.size(); i++) {
                if (fichasCalendarios.get(i).getDiaTrabajado() != null) {
                    if (fichasCalendarios.get(i).getDiaTrabajado()) {
                        diasTrabajados = diasTrabajados + 1;
                    }
                }
            }
        }
    }

    public void calcutarDiasComplementarios() {
        diasComplementarios = 0;
        if (fichasCalendarios != null) {
            for (int i = 0; i < fichasCalendarios.size(); i++) {
                if (fichasCalendarios.get(i).getDiaComplementario() != null) {
                    if (fichasCalendarios.get(i).getDiaComplementario()) {
                        diasComplementarios = diasComplementarios + 1;
                    }
                }
            }
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
            return getDateFormatFecha().format(fecha);
        }
    }

    public Integer validarFicha() {
        Integer banderaError = 0;
        for (int i = 0; i < getFichasCalendarios().size(); i++) {
            if (getFichasCalendarios().get(i).getHoraNoDefinida() != null) {
                banderaError = 1;
            }
        }
        return banderaError;
    }

    public void exportarPDFporCedulaMesAnio() throws JRException, IOException {

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
        String mesLetra;
        if (getFuncionario().getRelacionLaboral().getRelacionLaboral().equals("JOR")) {
            mesLetra = listaCiclos.get(numeroCiclo);
            mesLetra = mesLetra.replace(" ", "");
            mesLetra = mesLetra.replace("-", "_");
        } else {
            mesLetra = listaMeses.get(numeroMes);
        }
        reporte.exportarGenerico("pdf", ("marcacion_" + getFuncionario().getNumeroDocumento() + "_" + mesLetra + "_" + numeroAnio).toLowerCase(), response, parametros, fichasCalendarios, "ReporteMarcacionCedulaMesAnio");

    }

    public void exportarCSVporCedula() throws IOException, JRException {
        ReportesFichaCalendario reporte = new ReportesFichaCalendario();
        Map<String, Object> parametros = new HashMap<>();

        parametros.put("fechaGeneracion", new Date());
        parametros.put("cedulaFuncionario", getFuncionario().getNumeroDocumento());
        parametros.put("apellidoNombreFuncionario", getFuncionario().getApellidos() + ", " + getFuncionario().getNombres());
        parametros.put("nombreApellidoFuncionario", getFuncionario().getNombres() + " " + getFuncionario().getApellidos());
        parametros.put("usuarioGeneracion", funcionarioUsuario.getNombres() + " " + funcionarioUsuario.getApellidos());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        String mesLetra;
        if (getFuncionario().getRelacionLaboral().getRelacionLaboral().equals("JOR")) {
            mesLetra = listaCiclos.get(numeroCiclo);
            mesLetra = mesLetra.replace(" ", "");
            mesLetra = mesLetra.replace("-", "_");
        } else {
            mesLetra = listaMeses.get(numeroMes);
        }
        reporte.exportarGenerico("csv", ("marcacion_" + getFuncionario().getNumeroDocumento() + "_" + mesLetra + "_" + numeroAnio).toLowerCase(), response, parametros, fichasCalendarios, "ReporteMarcacionCedula");
    }

    public String prepararFormReporteRangoFecha() {
        setCantidadRegistros(0);
        setNombreArchivo("Ningún archivo seleccionado");
        return "admin_funcionario_reporte_rango_fecha";
    }

    public void leerArchivoCedulas() throws ParseException {
        setCantidadRegistros(0);
        setNombreArchivo("Ningún archivo seleccionado");
        try {
            String disposition = getFile().getHeader("content-disposition");
            setNombreArchivo(disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1"));
            String tipoArchivo = getFile().getContentType();
            if (getNombreArchivo().length() == 15) {
                if (tipoArchivo.equals("text/csv")) {
                    StringBuilder nombreArchivoCorto = new StringBuilder();
                    nombreArchivoCorto.append(getNombreArchivo().charAt(0)); //c
                    nombreArchivoCorto.append(getNombreArchivo().charAt(1)); //e
                    nombreArchivoCorto.append(getNombreArchivo().charAt(2)); //d
                    nombreArchivoCorto.append(getNombreArchivo().charAt(3)); //u
                    nombreArchivoCorto.append(getNombreArchivo().charAt(4)); //l
                    nombreArchivoCorto.append(getNombreArchivo().charAt(5)); //a
                    nombreArchivoCorto.append(getNombreArchivo().charAt(6)); //s
                    nombreArchivoCorto.append(getNombreArchivo().charAt(7)); //_
                    nombreArchivoCorto.append(getNombreArchivo().charAt(8)); //x
                    nombreArchivoCorto.append(getNombreArchivo().charAt(9)); //x
                    nombreArchivoCorto.append(getNombreArchivo().charAt(10)); //x
                    if (nombreArchivoCorto.toString().equals("cedulas_nom") || nombreArchivoCorto.toString().equals("cedulas_jor")) {
                        if (nombreArchivoCorto.toString().equals("cedulas_nom")) {
                            tipoReporte = "NOMBRADOS, CONTRATRADOS, ETC.";
                            tipoReporte2 = "nom";
                        } else {
                            tipoReporte = "JORNALEROS";
                            tipoReporte2 = "jor";
                        }

                        BufferedReader bf = null;
                        try {
                            bf = new BufferedReader(new InputStreamReader(getFile().getInputStream()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String line = null;
                        try {
                            cedulasDesdeArchivo = new ArrayList<>();
                            int contadorLinea = 0;
                            while ((line = bf.readLine()) != null) {
                                StringTokenizer tokens = new StringTokenizer(line, ";");
                                while (tokens.hasMoreTokens()) {
                                    cedulasDesdeArchivo.add(tokens.nextToken());
                                }
                            }
                            setCantidadRegistros(cedulasDesdeArchivo.size());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre archivo no valido", ""));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo archivo no valido", ""));
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Longitud de archivo no valida", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportarPDFporCedulaRangoFechas() throws JRException, IOException {
        ReportesFuncionarioMarcacion reporte = new ReportesFuncionarioMarcacion();

        List<FuncionarioMarcacion> listaParaReporte = new ArrayList();

        List<Funcionarios> funcionariosSegunArchivo = new ArrayList();
        for (int i = 0; i < cedulasDesdeArchivo.size(); i++) {
            Funcionarios func = ejbFuncionariosSB.findByNumeroDocumento(cedulasDesdeArchivo.get(i).trim());
            if (func != null) {
                funcionariosSegunArchivo.add(func);
            }
        }

        for (int i = 0; i < funcionariosSegunArchivo.size(); i++) {
            List<FichaCalendario> fichas = ejbFichaCalendarioSB.findByCedulaFechaDesdeFechaHastaConNull(funcionariosSegunArchivo.get(i).getNumeroDocumento(), fechaDesde, fechaHasta);
            if (fichas != null) {
                FuncionarioMarcacion funMarAux = new FuncionarioMarcacion(
                        funcionariosSegunArchivo.get(i).getNumeroDocumento(),
                        funcionariosSegunArchivo.get(i).getNombres(),
                        funcionariosSegunArchivo.get(i).getApellidos(),
                        funcionariosSegunArchivo.get(i).getRelacionLaboral().getDescripcion(),
                        fechaDesde,
                        fechaHasta,
                        fichas);
                listaParaReporte.add(funMarAux);
            }
        }

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
        parametros.put("totalRegistros", listaParaReporte.size());
        parametros.put("usuarioGeneracion", funcionarioUsuario.getNombres() + " " + funcionarioUsuario.getApellidos());
        parametros.put("SUBREPORT_DIR", "py/gov/mca/sisrrhh/reportes/");
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        reporte.exportarGenerico("pdf", ("marcacion_rango_fecha_" + dateFormatFechaGuionBajo.format(fechaDesde) + "_" + dateFormatFechaGuionBajo.format(fechaHasta)).toLowerCase() + "_" + tipoReporte2, response, parametros, listaParaReporte, "ReporteMarcacionCedulaConSubReport");
    }

    public String guardarFichaPorCedula() {
        String retorno;
        Integer banderaErrror = 0;
        for (int i = 0; i < getFichasCalendarios().size(); i++) {
            if (getFichasCalendarios().get(i).getHoraNoDefinida() != null) {
                banderaErrror = 1;
            }
        }

        if (banderaErrror == 0) {
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
                getFichasCalendarios().get(i).setMarcaFichaVerificada(true);
                // if (!fichasCalendarios.get(i).getPorcentajeMulta().equals(fichasCalendariosAux.get(i).getPorcentajeMulta())) {
                getFichasCalendarios().get(i).setMesCalendarioNumero(Integer.parseInt(dateFormatMesNumero.format(getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario())));
                getFichasCalendarios().get(i).setAnioCalendarioNumero(Integer.parseInt(dateFormatAnioNumero.format(getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario())));
//            getFichasCalendarios().get(i).setMesCalendarioNumero((numeroMes + 1));
//            getFichasCalendarios().get(i).setAnioCalendarioNumero(numeroAnio);
                String descriAudit = "Fecha: " + getFichasCalendarios().get(i).getFichaCalendarioPK().getFechaCalendario() + ", "
                        + "Monto Porcentaje: " + getFichasCalendarios().get(i).getPorcentajeMulta();
                actualizarFichaCalendario(getFichasCalendarios().get(i));
                guardarAuditoria(new Date(), funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(),
                        "Insert o Update", "ficha_calendario", getFichasCalendarios().get(i).getFichaCalendarioPK().getNumeroDocumentoFuncionario(), descriAudit);
                // }

            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos guardados", ""));
            if (funcionario.getRelacionLaboral().getRelacionLaboral().equals("JOR")) {
                retorno = prepararFormFichaGeneralMensualJor();
            } else {
                retorno = prepararFormFichaGeneralMensualNom();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Existen horarios no definidos", "La ficha no se puede guardar"));
            retorno = "";
        }
        return retorno;
    }

    public String prepararFormGenerarMultasNom() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        numeroMes = cal.get(Calendar.MONTH) - 1;
        numeroAnio = cal.get(Calendar.YEAR);
        return "admin_form_generar_multas_nom_csv";
    }

    public String prepararFormGenerarMultasJor() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        numeroCiclo = cal.get(Calendar.MONTH) - 1;
        numeroAnio = cal.get(Calendar.YEAR);
        return "admin_form_generar_multas_jor_csv";
    }

    public void generarMultasTotalesPorRangoDeFechaEnCsv(String tipoFuncionario) throws IOException, JRException, ParseException {
        calcularPeriodo(tipoFuncionario);
        ReportePorcentajeMultaCsv reporte = new ReportePorcentajeMultaCsv();
        List<MultaCsv> multas = new ArrayList<>();
        List<Funcionarios> listaFuncionariosActivos = ejbFuncionariosSB.findByEstadoRelacionLaboralFiltro("ACT", tipoFuncionario);
        for (int i = 0; i < listaFuncionariosActivos.size(); i++) {
            Integer sueldoDiario = 0;
            Integer totalPorcentajeMulta = 0;
            Integer totalMontoFijo = 0;
            Integer totalDiasTrabajados = 0;
            Integer totalDiasComplementarios = 0;
            Integer totalMulta = 0;
            fichasCalendarios = ejbFichaCalendarioSB.findByCedulaFechaDesdeFechaHasta(listaFuncionariosActivos.get(i).getNumeroDocumento(), fechaDesde, fechaHasta);
            for (int j = 0; j < fichasCalendarios.size(); j++) {
                if (fichasCalendarios.get(j).getPorcentajeMulta() != null) {
                    totalPorcentajeMulta = totalPorcentajeMulta + fichasCalendarios.get(j).getPorcentajeMulta();
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
//                System.out.println("Cedula: " + listaFuncionariosActivos.get(i).getNumeroDocumento() + ", "
//                        + "Jornal: " + sueldoDiario + ", "
//                        + "Total Pocentaje: " + totalPorcentajeMulta + "%, "
//                        + "Total Monto Fijo: " + totalMontoFijo + ", "
//                        + "Total Dias Trabajados: " + totalDiasTrabajados + ", "
//                        + "Total Dias Complementarios: " + totalDiasComplementarios + ", "
//                        + "Total Multa:  " + totalMulta);

                MultaCsv multa = new MultaCsv(listaFuncionariosActivos.get(i).getNumeroDocumento(), sueldoDiario, totalPorcentajeMulta, totalMontoFijo, totalDiasTrabajados, totalDiasComplementarios, totalMulta);
                multas.add(multa);
                guardarFichasCalendarios();
            } else {
                //System.out.println("Cedula: " + listaFuncionariosActivos.get(i).getNumeroDocumento() + ", Sin salario diario ");
            }

        }
        String nombreReporte = "ReporteMultasEnCvs";
        if (tipoFuncionario.equals("JOR")) {
            nombreReporte = "ReporteMultasEnCvsJor";
        }

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        reporte.exportarGenerico("csv", ("total_multas_" + dateFormatFechaGuionBajo.format(fechaDesde) + "_" + dateFormatFechaGuionBajo.format(fechaHasta) + "_" + tipoFuncionario).toLowerCase(), response, null, multas, nombreReporte);
    }

    public void actualizarFichaCalendario(FichaCalendario fichaCalendario) {
        ejbFichaCalendarioSB.udtade(fichaCalendario);

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
     * @return the funcionarioUsuario
     */
    public Funcionarios getFuncionarioUsuario() {
        return funcionarioUsuario;
    }

    /**
     * @param funcionarioUsuario the funcionarioUsuario to set
     */
    public void setFuncionarioUsuario(Funcionarios funcionarioUsuario) {
        this.funcionarioUsuario = funcionarioUsuario;
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
     * @return the listaCiclos
     */
    public LinkedHashMap<Integer, String> getListaCiclos() {
        return listaCiclos;
    }

    /**
     * @param listaCiclos the listaCiclos to set
     */
    public void setListaCiclos(LinkedHashMap<Integer, String> listaCiclos) {
        this.listaCiclos = listaCiclos;
    }

    /**
     * @return the numeroCiclo
     */
    public Integer getNumeroCiclo() {
        return numeroCiclo;
    }

    /**
     * @param numeroCiclo the numeroCiclo to set
     */
    public void setNumeroCiclo(Integer numeroCiclo) {
        this.numeroCiclo = numeroCiclo;
    }

    /**
     * @return the tipoGeneracion
     */
    public String getTipoGeneracion() {
        return tipoGeneracion;
    }

    /**
     * @param tipoGeneracion the tipoGeneracion to set
     */
    public void setTipoGeneracion(String tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
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
     * @return the cedulasDesdeArchivo
     */
    public List<String> getCedulasDesdeArchivo() {
        return cedulasDesdeArchivo;
    }

    /**
     * @param cedulasDesdeArchivo the cedulasDesdeArchivo to set
     */
    public void setCedulasDesdeArchivo(List<String> cedulasDesdeArchivo) {
        this.cedulasDesdeArchivo = cedulasDesdeArchivo;
    }

    /**
     * @return the montoFijo
     */
    public Integer getMontoFijo() {
        return montoFijo;
    }

    /**
     * @param montoFijo the montoFijo to set
     */
    public void setMontoFijo(Integer montoFijo) {
        this.montoFijo = montoFijo;
    }

    /**
     * @return the montoFijoTotal
     */
    public Integer getMontoFijoTotal() {
        return montoFijoTotal;
    }

    /**
     * @param montoFijoTotal the montoFijoTotal to set
     */
    public void setMontoFijoTotal(Integer montoFijoTotal) {
        this.montoFijoTotal = montoFijoTotal;
    }

    /**
     * @return the diasTrabajados
     */
    public Integer getDiasTrabajados() {
        return diasTrabajados;
    }

    /**
     * @param diasTrabajados the diasTrabajados to set
     */
    public void setDiasTrabajados(Integer diasTrabajados) {
        this.diasTrabajados = diasTrabajados;
    }

    /**
     * @return the diasComplementarios
     */
    public Integer getDiasComplementarios() {
        return diasComplementarios;
    }

    /**
     * @param diasComplementarios the diasComplementarios to set
     */
    public void setDiasComplementarios(Integer diasComplementarios) {
        this.diasComplementarios = diasComplementarios;
    }

    /**
     * @return the activarComponentes
     */
    public boolean isActivarComponentes() {
        return activarComponentes;
    }

    /**
     * @param activarComponentes the activarComponentes to set
     */
    public void setActivarComponentes(boolean activarComponentes) {
        this.activarComponentes = activarComponentes;
    }

    /**
     * @return the dataTableFichasCalendarios
     */
    public HtmlDataTable getDataTableFichasCalendarios() {
        return dataTableFichasCalendarios;
    }

    /**
     * @param dataTableFichasCalendarios the dataTableFichasCalendarios to set
     */
    public void setDataTableFichasCalendarios(HtmlDataTable dataTableFichasCalendarios) {
        this.dataTableFichasCalendarios = dataTableFichasCalendarios;
    }

    /**
     * @return the fichaCalendario
     */
    public FichaCalendario getFichaCalendario() {
        return fichaCalendario;
    }

    /**
     * @param fichaCalendario the fichaCalendario to set
     */
    public void setFichaCalendario(FichaCalendario fichaCalendario) {
        this.fichaCalendario = fichaCalendario;
    }

    /**
     * @return the dateFormatFecha
     */
    public SimpleDateFormat getDateFormatFecha() {
        return dateFormatFecha;
    }

    /**
     * @return the horarios
     */
    public List<Horarios> getHorarios() {
        return horarios = horariosSB.findAll();
    }

    /**
     * @param horarios the horarios to set
     */
    public void setHorarios(List<Horarios> horarios) {
        this.horarios = horarios;
    }

    /**
     * @return the tiposExcepciones
     */
    public List<TiposExcepciones> getTiposExcepciones() {
        return tiposExcepciones = tiposExcepcionesSB.findAll();
    }

    /**
     * @param tiposExcepciones the tiposExcepciones to set
     */
    public void setTiposExcepciones(List<TiposExcepciones> tiposExcepciones) {
        this.tiposExcepciones = tiposExcepciones;
    }

    /**
     * @return the listaTipoMarcacion
     */
    public LinkedHashMap<Integer, String> getListaTipoMarcacion() {
        return listaTipoMarcacion;
    }

    /**
     * @param listaTipoMarcacion the listaTipoMarcacion to set
     */
    public void setListaTipoMarcacion(LinkedHashMap<Integer, String> listaTipoMarcacion) {
        this.listaTipoMarcacion = listaTipoMarcacion;
    }

    /**
     * @return the numeroTipoMarcacion
     */
    public Integer getNumeroTipoMarcacion() {
        return numeroTipoMarcacion;
    }

    /**
     * @param numeroTipoMarcacion the numeroTipoMarcacion to set
     */
    public void setNumeroTipoMarcacion(Integer numeroTipoMarcacion) {
        this.numeroTipoMarcacion = numeroTipoMarcacion;
    }

    /**
     * @return the fichaVerificada
     */
    public String getFichaVerificada() {
        return fichaVerificada;
    }

    /**
     * @param fichaVerificada the fichaVerificada to set
     */
    public void setFichaVerificada(String fichaVerificada) {
        this.fichaVerificada = fichaVerificada;
    }

    /**
     * @return the diasBoolean
     */
    public Integer getDiasBoolean() {
        return diasBoolean;
    }

    /**
     * @param diasBoolean the diasBoolean to set
     */
    public void setDiasBoolean(Integer diasBoolean) {
        this.diasBoolean = diasBoolean;
    }

    /**
     * @return the listaSiNo
     */
    public LinkedHashMap<Integer, String> getListaSiNo() {
        return listaSiNo;
    }

    /**
     * @param listaSiNo the listaSiNo to set
     */
    public void setListaSiNo(LinkedHashMap<Integer, String> listaSiNo) {
        this.listaSiNo = listaSiNo;
    }

    /**
     * @return the tipoReporte
     */
    public String getTipoReporte() {
        return tipoReporte;
    }

    /**
     * @param tipoReporte the tipoReporte to set
     */
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    /**
     * @return the tipoReporte2
     */
    public String getTipoReporte2() {
        return tipoReporte2;
    }

    /**
     * @param tipoReporte2 the tipoReporte2 to set
     */
    public void setTipoReporte2(String tipoReporte2) {
        this.tipoReporte2 = tipoReporte2;
    }

}
