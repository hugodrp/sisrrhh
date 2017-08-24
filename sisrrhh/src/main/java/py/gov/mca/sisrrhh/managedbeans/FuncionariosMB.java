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
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import py.gov.mca.sisrrhh.entitys.Auditoria;
import py.gov.mca.sisrrhh.entitys.Comentarios;
import py.gov.mca.sisrrhh.entitys.Estados;
import py.gov.mca.sisrrhh.entitys.Funcionarios;
import py.gov.mca.sisrrhh.entitys.Horarios;
import py.gov.mca.sisrrhh.entitys.RelacionLaboral;
import py.gov.mca.sisrrhh.entitys.Roles;
import py.gov.mca.sisrrhh.sessionbeans.AuditoriaSB;
import py.gov.mca.sisrrhh.sessionbeans.EstadosSB;
import py.gov.mca.sisrrhh.sessionbeans.FuncionariosSB;
import py.gov.mca.sisrrhh.sessionbeans.HorariosSB;
import py.gov.mca.sisrrhh.sessionbeans.RelacionLaboralSB;
import py.gov.mca.sisrrhh.utiles.MsgUtil;

/**
 *
 * @author vinsfran
 */
@Named(value = "funcionariosMB")
@SessionScoped
public class FuncionariosMB implements Serializable {

    @EJB
    private FuncionariosSB ejbFuncionariosSB;

    @EJB
    private HorariosSB ejHorariosSB;

    @EJB
    private EstadosSB ejbEstadosSB;

    @EJB
    private AuditoriaSB auditoriaSB;

    @EJB
    private RelacionLaboralSB ejbRelacionLaboralSB;

    private final SimpleDateFormat dateFormatFecha = new SimpleDateFormat("dd/MM/yyyy");

    private Part file;
    private String nombreArchivo;
    private int cantidadRegistros;
    private StringBuilder anio;
    private StringBuilder mes;
    private StringBuilder dia;
    private StringBuilder tipoFun;
    private String fechaArchivo;
    private boolean mostrarBotonSubirBd;
    private boolean activarBotonSubirBd;
    private Funcionarios funcionarioUsuario;
    private List<Funcionarios> listaFuncionarios;
    private List<Funcionarios> listaFuncionariosFiltro;
    private float progress;
    //private HtmlDataTable dataTableFuncionarios;
    private Funcionarios funcionario;
    private Funcionarios slcFuncionario;
    private List<Estados> estados;
    private List<Horarios> horarios;
    private Horarios horario;
    private List<RelacionLaboral> relacionLaborales;
    private RelacionLaboral relacionLaboral;
    private boolean boolNombreApellido;
    private boolean boolRelacionLaboral;
    private boolean boolHorario;
    private boolean boolJornal;
    private boolean boolFechaIngreso;
    private boolean boolEstado;

    public FuncionariosMB() {
    }

    @PostConstruct
    public void init() {
        funcionario = new Funcionarios();
        recuperarUsuarioSession();
    }

    public String prepararFormCargaDedeArchivoHorariosFuncionarios() {
        setCantidadRegistros(0);
        setNombreArchivo("Ningún archivo seleccionado");
        setMostrarBotonSubirBd(false);
        setActivarBotonSubirBd(true);
        return "/admin_form_carga_horarios_fun_desde_archivo";
    }

    public void leerArchivoHorariosFuncionarios() throws ParseException {
        setCantidadRegistros(0);
        setNombreArchivo("Ningún archivo seleccionado");
        try {
            String disposition = getFile().getHeader("content-disposition");
            setNombreArchivo(disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1"));
            String tipoArchivo = getFile().getContentType();
            System.out.println("Long " + getNombreArchivo().length());
            if (getNombreArchivo().length() == 21) {
                if (tipoArchivo.equals("text/csv")) {
                    StringBuilder nombreArchivoCorto = new StringBuilder();
                    nombreArchivoCorto.append(getNombreArchivo().charAt(0)); //f
                    nombreArchivoCorto.append(getNombreArchivo().charAt(1)); //u
                    nombreArchivoCorto.append(getNombreArchivo().charAt(2)); //n
                    nombreArchivoCorto.append(getNombreArchivo().charAt(3)); //c
                    nombreArchivoCorto.append(getNombreArchivo().charAt(4)); //i
                    nombreArchivoCorto.append(getNombreArchivo().charAt(5)); //o
                    nombreArchivoCorto.append(getNombreArchivo().charAt(6)); //n
                    nombreArchivoCorto.append(getNombreArchivo().charAt(7)); //a
                    nombreArchivoCorto.append(getNombreArchivo().charAt(8)); //r
                    nombreArchivoCorto.append(getNombreArchivo().charAt(9)); //i
                    nombreArchivoCorto.append(getNombreArchivo().charAt(10)); //0
                    nombreArchivoCorto.append(getNombreArchivo().charAt(11)); //s

                    nombreArchivoCorto.append(getNombreArchivo().charAt(12)); //_
                    nombreArchivoCorto.append(getNombreArchivo().charAt(13)); //h
                    nombreArchivoCorto.append(getNombreArchivo().charAt(14)); //o
                    nombreArchivoCorto.append(getNombreArchivo().charAt(15)); //r
                    nombreArchivoCorto.append(getNombreArchivo().charAt(16)); //a

                    if (nombreArchivoCorto.toString().equals("funcionarios_hora")) {
                        BufferedReader bf = null;
                        try {
                            bf = new BufferedReader(new InputStreamReader(getFile().getInputStream()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String line = null;
                        try {
                            setListaFuncionarios(new ArrayList<Funcionarios>());
                            while ((line = bf.readLine()) != null) {
                                StringTokenizer tokens = new StringTokenizer(line, ";");
                                while (tokens.hasMoreTokens()) {
                                    String numeroDocumento = tokens.nextToken();
                                    String horario = tokens.nextToken();
                                    Funcionarios funcionarioEncontrado = ejbFuncionariosSB.findByNumeroDocumento(numeroDocumento);
                                    if (funcionarioEncontrado != null) {
                                        Horarios horarioEncontrado = ejHorariosSB.findByHorario(horario);
                                        if (horarioEncontrado == null) {
                                            horarioEncontrado = new Horarios();
                                            horarioEncontrado.setHorario("00000000");
                                            System.out.println("Cedula: " + numeroDocumento + ", horario no definido: " + horario);
                                        }
                                        funcionarioEncontrado.setHorarioNormal(horarioEncontrado);
                                        getListaFuncionarios().add(funcionarioEncontrado);
                                    } else {
                                        System.out.println("Cedula no existe: " + numeroDocumento);
                                    }

                                }
                            }
                            setCantidadRegistros(getListaFuncionarios().size());
                            setMostrarBotonSubirBd(true);
                            setActivarBotonSubirBd(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        setMostrarBotonSubirBd(false);
                        setActivarBotonSubirBd(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre archivo no valido", ""));
                    }
                } else {
                    setMostrarBotonSubirBd(false);
                    setActivarBotonSubirBd(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo archivo no valido", ""));
                }

            } else {
                setMostrarBotonSubirBd(false);
                setActivarBotonSubirBd(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Longitud de archivo no valida", ""));
            }
        } catch (IOException e) {
            setMostrarBotonSubirBd(false);
            setActivarBotonSubirBd(true);
            e.printStackTrace();
        }
    }

    public void guardarDesdeArchivoHorariosFuncionarios() {
        if (getListaFuncionarios() != null) {
            setActivarBotonSubirBd(true);
            int i = 0;
            for (Funcionarios funcionarioAux : getListaFuncionarios()) {
                ejbFuncionariosSB.update(funcionarioAux);
                if (++i % 1000 == 0) {
                    ejbFuncionariosSB.flushAndClear();
                }
                setProgress((i * 100) / (getListaFuncionarios().size()));
            }
            ejbFuncionariosSB.flushAndClear();

            String descriAudit = "Nombre de archivo: " + getNombreArchivo() + ", Cantidad de registros: " + getListaFuncionarios().size();
            //guardarAuditoria(new Date(), funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(), "Insert o Update", "funcionarios", "", descriAudit);
            setListaFuncionarios(null);
            setActivarBotonSubirBd(false);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione otro archivo", ""));
        }
    }

    public String prepararFormCargaDedeArchivoFuncionarios() {
        setCantidadRegistros(0);
        setNombreArchivo("Ningún archivo seleccionado");
        setMostrarBotonSubirBd(false);
        setActivarBotonSubirBd(true);
        return "/administracion/admin_form_carga_desde_archivo_funcionarios";
    }

    public void leerArchivoFuncionarios() throws ParseException {
        setCantidadRegistros(0);
        setAnio(new StringBuilder());
        setMes(new StringBuilder());
        setDia(new StringBuilder());
        setTipoFun(new StringBuilder());
        setNombreArchivo("Ningún archivo seleccionado");
        setFechaArchivo("DD/MM/AAAA");

        setNombreArchivo("Ningún archivo seleccionado");
        try {
            String disposition = file.getHeader("content-disposition");
            setNombreArchivo(disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1"));
            String tipoArchivo = file.getContentType();
            if (getNombreArchivo().length() == 27) {
                if (tipoArchivo.equals("text/csv")) {
                    StringBuilder nombreArchivoCorto = new StringBuilder();
                    nombreArchivoCorto.append(getNombreArchivo().charAt(0)); //f
                    nombreArchivoCorto.append(getNombreArchivo().charAt(1)); //u
                    nombreArchivoCorto.append(getNombreArchivo().charAt(2)); //n
                    nombreArchivoCorto.append(getNombreArchivo().charAt(3)); //c
                    nombreArchivoCorto.append(getNombreArchivo().charAt(4)); //i
                    nombreArchivoCorto.append(getNombreArchivo().charAt(5)); //o
                    nombreArchivoCorto.append(getNombreArchivo().charAt(6)); //n
                    nombreArchivoCorto.append(getNombreArchivo().charAt(7)); //a
                    nombreArchivoCorto.append(getNombreArchivo().charAt(8)); //r
                    nombreArchivoCorto.append(getNombreArchivo().charAt(9)); //i
                    nombreArchivoCorto.append(getNombreArchivo().charAt(10)); //0
                    nombreArchivoCorto.append(getNombreArchivo().charAt(11)); //s
                    if (nombreArchivoCorto.toString().equals("funcionarios")) {
                        getAnio().append(getNombreArchivo().charAt(12));
                        getAnio().append(getNombreArchivo().charAt(13));
                        getAnio().append(getNombreArchivo().charAt(14));
                        getAnio().append(getNombreArchivo().charAt(15));
                        getMes().append(getNombreArchivo().charAt(16));
                        getMes().append(getNombreArchivo().charAt(17));
                        getDia().append(getNombreArchivo().charAt(18));
                        getDia().append(getNombreArchivo().charAt(19));
                        getTipoFun().append(getNombreArchivo().charAt(20));
                        getTipoFun().append(getNombreArchivo().charAt(21));
                        getTipoFun().append(getNombreArchivo().charAt(22));
                        setFechaArchivo(getDia() + "/" + getMes() + "/" + getAnio());
                        BufferedReader bf = null;
                        try {
                            bf = new BufferedReader(new InputStreamReader(file.getInputStream()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String line = null;
                        try {
                            listaFuncionarios = new ArrayList<>();
                            while ((line = bf.readLine()) != null) {
                                StringTokenizer tokens = new StringTokenizer(line, ";");
                                while (tokens.hasMoreTokens()) {
                                    String numeroDocumento = tokens.nextToken();
                                    String apellidos = tokens.nextToken();
                                    String nombres = tokens.nextToken();
                                    String relacionLaboral = tokens.nextToken();
                                    String sueldoDiario = tokens.nextToken();
                                    if(sueldoDiario.equals("NOMB")){
                                        System.out.println("CI " + numeroDocumento);
                                    }
                                    // String fechaIngreso = tokens.nextToken();

                                    Funcionarios funcionarioEncontrado = ejbFuncionariosSB.findByNumeroDocumento(numeroDocumento);
                                    if (funcionarioEncontrado == null) {
                                        funcionarioEncontrado = new Funcionarios();
                                        funcionarioEncontrado.setRelacionLaboral(new RelacionLaboral());
                                        funcionarioEncontrado.setHorarioNormal(new Horarios());
                                        funcionarioEncontrado.setEstadoFuncionario(new Estados());
                                        funcionarioEncontrado.setRolUsuario(new Roles());
                                        funcionarioEncontrado.setNumeroDocumento(numeroDocumento.trim());
                                        funcionarioEncontrado.setApellidos(apellidos.trim());
                                        funcionarioEncontrado.setNombres(nombres.trim());
                                        funcionarioEncontrado.getRelacionLaboral().setRelacionLaboral(relacionLaboral);
                                        funcionarioEncontrado.getHorarioNormal().setHorario("00000000");
                                        funcionarioEncontrado.getEstadoFuncionario().setCodigo("ACT");
                                        funcionarioEncontrado.getRolUsuario().setCodigo("FUN");
                                    }
                                    funcionarioEncontrado.setSueldoDiario(Double.parseDouble(sueldoDiario));
                                    //funcionarioEncontrado.setFechaIngreso(dateFormatFecha.parse(fechaIngreso));
                                    listaFuncionarios.add(funcionarioEncontrado);
                                }
                            }
                            cantidadRegistros = listaFuncionarios.size();
                            setMostrarBotonSubirBd(true);
                            setActivarBotonSubirBd(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        setMostrarBotonSubirBd(false);
                        setActivarBotonSubirBd(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre archivo no valido", ""));
                    }
                } else {
                    setMostrarBotonSubirBd(false);
                    setActivarBotonSubirBd(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo archivo no valido", ""));
                }

            } else {
                setMostrarBotonSubirBd(false);
                setActivarBotonSubirBd(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Longitud de archivo no valida", ""));
            }
        } catch (IOException e) {
            setMostrarBotonSubirBd(false);
            setActivarBotonSubirBd(true);
            e.printStackTrace();
        }
    }

    public void guardarDesdeArchivoFuncionarios() {
        if (listaFuncionarios != null) {
            activarBotonSubirBd = true;
            int i = 0;
            for (Funcionarios funcionarioAux : listaFuncionarios) {
                //System.out.println("Funcio: " + funcionarioAux.getNumeroDocumento() + " - " + funcionarioAux.getNombres());
                ejbFuncionariosSB.update(funcionarioAux);
                if (++i % 1000 == 0) {
                    ejbFuncionariosSB.flushAndClear();
                }
                progress = (i * 100) / (listaFuncionarios.size());
            }
            ejbFuncionariosSB.flushAndClear();

            String descriAudit = "Nombre de archivo: " + getNombreArchivo() + ", Cantidad de registros: " + listaFuncionarios.size();
            guardarAuditoria(new Date(), getFuncionarioUsuario().getUsuario(), getFuncionarioUsuario().getRolUsuario().getNombre(), "Insert o Update", "funcionarios", "", descriAudit);
            listaFuncionarios = null;
            activarBotonSubirBd = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione otro archivo", ""));
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

    public Funcionarios recuperarUsuarioSession() {
        //System.out.println("FundionariosMB.recuperarUsuarioSession()");
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        setFuncionarioUsuario((Funcionarios) session.getAttribute("funcionarioUsuario"));
        return getFuncionarioUsuario();
    }

    public void onComplete() {
        setProgress(0 / 1);
        //mostrarBotonSubirBd = false;       
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registros guardados " + getCantidadRegistros(), ""));
    }

    public String prepararMostrarListaFuncionarios() {
//        dataTableFuncionarios = new HtmlDataTable();
        listaFuncionarios = ejbFuncionariosSB.findAll2();
        return "admin_funcionario_lista";
    }

    public String modificarFuncionario(String tipo) {

        boolNombreApellido = true;
        boolRelacionLaboral = true;
        boolHorario = true;
        boolJornal = true;
        boolFechaIngreso = true;
        boolEstado = true;

        if (tipo.endsWith("nombreApellido")) {
            boolNombreApellido = false;
        }
        if (tipo.endsWith("relacionLaboral")) {
            boolRelacionLaboral = false;
        }
        if (tipo.endsWith("horario")) {
            boolHorario = false;
        }
        if (tipo.endsWith("jornal")) {
            boolJornal = false;
        }
        if (tipo.endsWith("fechaIngreso")) {
            boolFechaIngreso = false;
        }
        if (tipo.endsWith("estado")) {
            boolEstado = false;
        }

        funcionario = ejbFuncionariosSB.findByNumeroDocumento(slcFuncionario.getNumeroDocumento());
        if (funcionario.getComentariosId() == null) {
            funcionario.setComentariosId(new Comentarios());
        }
        System.out.println("boolHorario: " + boolHorario);
        System.out.println("funcionario " + funcionario.getNombres());
        return "admin_funcionario_from_modificacion";
    }

    public String nuevoFuncionario() {
        funcionario = new Funcionarios();
        funcionario.setRelacionLaboral(new RelacionLaboral());
        funcionario.setHorarioNormal(new Horarios());
        funcionario.setEstadoFuncionario(new Estados());
        funcionario.setRolUsuario(new Roles("FUN"));
        funcionario.setComentariosId(new Comentarios());

        return "admin_funcionario_from_alta";
    }

    public String cancelarFomularios() {
        return redireccion("CANCELAR", "");
    }

    public String guardarFomularios(String modo) {
        String campos = camposParaAuditoria(modo);
        return redireccion(modo, campos);
    }

    public String redireccion(String modo, String campos) {
//        dataTableFuncionarios = new HtmlDataTable();
        String retorno = "admin_funcionario_lista";
        if (!modo.equals("CANCELAR")) {
            System.out.println("MODO " + modo);
            System.out.println("CAMPOS " + campos);
            if (!campos.equals("")) {
                String resultado = ejbFuncionariosSB.guardarFuncionarioMantenimiento(funcionario, modo, campos, recuperarUsuarioSession().getUsuario(), recuperarUsuarioSession().getRolUsuario().getNombre());
                if (resultado.equals("OK")) {
                    listaFuncionarios = ejbFuncionariosSB.findAll2();
                } else {
                    MsgUtil.msg("No se pudo guardar", "Cedula ya existe", "ERROR");
                    retorno = "";
                }
            }
        }
        return retorno;
    }

    private String camposParaAuditoria(String modo) {
        String campos = "";
        switch (modo) {
            case "Update":
                if (!funcionario.getNombres().equals(slcFuncionario.getNombres())) {
                    campos = "Nombre anterior: ";
                    campos = campos + slcFuncionario.getNombres();
                    campos = campos + ", Nombre actual: ";
                    campos = campos + funcionario.getNombres();
                }
                if (!funcionario.getApellidos().equals(slcFuncionario.getApellidos())) {
                    if (!campos.equals("")) {
                        campos = campos + ", ";
                    }
                    campos = campos + "Apellido anterior: ";
                    campos = campos + slcFuncionario.getApellidos();
                    campos = campos + ", Apellido actual: ";
                    campos = campos + funcionario.getApellidos();
                }
                if (funcionario.getFechaIngreso() != null && !funcionario.getFechaIngreso().equals(slcFuncionario.getFechaIngreso())) {
                    if (!campos.equals("")) {
                        campos = campos + ", ";
                    }
                    campos = campos + "Fecha Ingreso anterior: ";
                    campos = campos + formatearFecha(slcFuncionario.getFechaIngreso());
                    campos = campos + ", Fecha Ingreso actual: ";
                    campos = campos + formatearFecha(funcionario.getFechaIngreso());
                }
                if (!funcionario.getSueldoDiario().equals(slcFuncionario.getSueldoDiario())) {
                    if (!campos.equals("")) {
                        campos = campos + ", ";
                    }
                    campos = campos + "Jornal anterior: ";
                    campos = campos + slcFuncionario.getSueldoDiario();
                    campos = campos + ", Jornal actual: ";
                    campos = campos + funcionario.getSueldoDiario();
                }
                if (!funcionario.getRelacionLaboral().equals(slcFuncionario.getRelacionLaboral())) {
                    if (!campos.equals("")) {
                        campos = campos + ", ";
                    }
                    campos = campos + "Relacion Laboral anterior: ";
                    campos = campos + slcFuncionario.getRelacionLaboral().getDescripcion();
                    campos = campos + ", Relacion Laboral actual: ";
                    campos = campos + funcionario.getRelacionLaboral().getDescripcion();
                }
                if (!funcionario.getHorarioNormal().equals(slcFuncionario.getHorarioNormal())) {
                    if (!campos.equals("")) {
                        campos = campos + ", ";
                    }
                    campos = campos + "Horario anterior: ";
                    campos = campos + slcFuncionario.getHorarioNormal().getDescripcion();
                    campos = campos + ", Horario actual: ";
                    campos = campos + funcionario.getHorarioNormal().getDescripcion();
                }
                if (!funcionario.getEstadoFuncionario().equals(slcFuncionario.getEstadoFuncionario())) {
                    if (!campos.equals("")) {
                        campos = campos + ", ";
                    }
                    campos = campos + "Estado anterior: ";
                    campos = campos + slcFuncionario.getEstadoFuncionario().getDescripcion();
                    campos = campos + ", Estado Laboral actual: ";
                    campos = campos + funcionario.getEstadoFuncionario().getDescripcion();
                }
                if (funcionario.getComentariosId() != null && !funcionario.getComentariosId().getDescripcion().equals(slcFuncionario.getComentariosId().getDescripcion())) {
                    if (!campos.equals("")) {
                        campos = campos + ", ";
                    }
                    campos = campos + "Comentario anterior: ";
                    campos = campos + slcFuncionario.getComentariosId().getDescripcion();
                    campos = campos + ", Comentario Laboral actual: ";
                    campos = campos + funcionario.getComentariosId().getDescripcion();
                }
                break;
            case "Insert":
                campos = "Nombre: ";
                campos = campos + funcionario.getNombres();
                if (!campos.equals("")) {
                    campos = campos + ", ";
                }
                campos = campos + "Apellido: ";
                campos = campos + funcionario.getApellidos();
                if (!campos.equals("")) {
                    campos = campos + ", ";
                }
                campos = campos + "Fecha Ingreso actual: ";
                campos = campos + formatearFecha(funcionario.getFechaIngreso());
                if (!campos.equals("")) {
                    campos = campos + ", ";
                }
                campos = campos + "Jornal: ";
                campos = campos + funcionario.getSueldoDiario();
                if (!campos.equals("")) {
                    campos = campos + ", ";
                }
                campos = campos + "Relacion Laboral: ";
                campos = campos + funcionario.getRelacionLaboral().getDescripcion();
                if (!campos.equals("")) {
                    campos = campos + ", ";
                }
                campos = campos + "Horario: ";
                campos = campos + funcionario.getHorarioNormal().getDescripcion();
                if (!campos.equals("")) {
                    campos = campos + ", ";
                }
                campos = campos + "Estado: ";
                campos = campos + funcionario.getEstadoFuncionario().getDescripcion();
                break;
            default:
                campos = "";
                break;
        }
        return campos;
    }

    public String formatearFecha(Date fecha) {
        if (fecha == null) {
            return "";
        } else {
            return getDateFormatFecha().format(fecha);
        }
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
     * @return the listaFuncionarios
     */
    public List<Funcionarios> getListaFuncionarios() {
        System.out.println("Tam: " + listaFuncionarios.size());
        return listaFuncionarios;
    }

    /**
     * @param listaFuncionarios the listaFuncionarios to set
     */
    public void setListaFuncionarios(List<Funcionarios> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
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
     * @return the dateFormatFecha
     */
    public SimpleDateFormat getDateFormatFecha() {
        return dateFormatFecha;
    }

    /**
     * @return the dataTableFuncionarios
     */
//    public HtmlDataTable getDataTableFuncionarios() {
//        return dataTableFuncionarios;
//    }
//
//    /**
//     * @param dataTableFuncionarios the dataTableFuncionarios to set
//     */
//    public void setDataTableFuncionarios(HtmlDataTable dataTableFuncionarios) {
//        this.dataTableFuncionarios = dataTableFuncionarios;
//    }
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
     * @return the estados
     */
    public List<Estados> getEstados() {
        estados = ejbEstadosSB.findAll();
        return estados;
    }

    /**
     * @param estados the estados to set
     */
    public void setEstados(List<Estados> estados) {
        this.estados = estados;
    }

    /**
     * @return the horarios
     */
    public List<Horarios> getHorarios() {
        horarios = ejHorariosSB.findAll();
        return horarios;
    }

    /**
     * @param horarios the horarios to set
     */
    public void setHorarios(List<Horarios> horarios) {
        this.horarios = horarios;
    }

    /**
     * @return the relacionLaborales
     */
    public List<RelacionLaboral> getRelacionLaborales() {
        relacionLaborales = ejbRelacionLaboralSB.findAll();
        return relacionLaborales;
    }

    /**
     * @param relacionLaborales the relacionLaborales to set
     */
    public void setRelacionLaborales(List<RelacionLaboral> relacionLaborales) {
        this.relacionLaborales = relacionLaborales;
    }

    /**
     * @return the relacionLaboral
     */
    public RelacionLaboral getRelacionLaboral() {
        return relacionLaboral;
    }

    /**
     * @param relacionLaboral the relacionLaboral to set
     */
    public void setRelacionLaboral(RelacionLaboral relacionLaboral) {
        this.relacionLaboral = relacionLaboral;
    }

    /**
     * @return the horario
     */
    public Horarios getHorario() {
        return horario;
    }

    /**
     * @param horario the horario to set
     */
    public void setHorario(Horarios horario) {
        this.horario = horario;
    }

    /**
     * @return the listaFuncionariosFiltro
     */
    public List<Funcionarios> getListaFuncionariosFiltro() {
        return listaFuncionariosFiltro;
    }

    /**
     * @param listaFuncionariosFiltro the listaFuncionariosFiltro to set
     */
    public void setListaFuncionariosFiltro(List<Funcionarios> listaFuncionariosFiltro) {
        this.listaFuncionariosFiltro = listaFuncionariosFiltro;
    }

    /**
     * @return the slcFuncionario
     */
    public Funcionarios getSlcFuncionario() {
        return slcFuncionario;
    }

    /**
     * @param slcFuncionario the slcFuncionario to set
     */
    public void setSlcFuncionario(Funcionarios slcFuncionario) {
        this.slcFuncionario = slcFuncionario;
    }

    /**
     * @return the boolNombreApellido
     */
    public boolean isBoolNombreApellido() {
        return boolNombreApellido;
    }

    /**
     * @param boolNombreApellido the boolNombreApellido to set
     */
    public void setBoolNombreApellido(boolean boolNombreApellido) {
        this.boolNombreApellido = boolNombreApellido;
    }

    /**
     * @return the boolRelacionLaboral
     */
    public boolean isBoolRelacionLaboral() {
        return boolRelacionLaboral;
    }

    /**
     * @param boolRelacionLaboral the boolRelacionLaboral to set
     */
    public void setBoolRelacionLaboral(boolean boolRelacionLaboral) {
        this.boolRelacionLaboral = boolRelacionLaboral;
    }

    /**
     * @return the boolHorario
     */
    public boolean isBoolHorario() {
        return boolHorario;
    }

    /**
     * @param boolHorario the boolHorario to set
     */
    public void setBoolHorario(boolean boolHorario) {
        this.boolHorario = boolHorario;
    }

    /**
     * @return the boolJornal
     */
    public boolean isBoolJornal() {
        return boolJornal;
    }

    /**
     * @param boolJornal the boolJornal to set
     */
    public void setBoolJornal(boolean boolJornal) {
        this.boolJornal = boolJornal;
    }

    /**
     * @return the boolFechaIngreso
     */
    public boolean isBoolFechaIngreso() {
        return boolFechaIngreso;
    }

    /**
     * @param boolFechaIngreso the boolFechaIngreso to set
     */
    public void setBoolFechaIngreso(boolean boolFechaIngreso) {
        this.boolFechaIngreso = boolFechaIngreso;
    }

    /**
     * @return the boolEstado
     */
    public boolean isBoolEstado() {
        return boolEstado;
    }

    /**
     * @param boolEstado the boolEstado to set
     */
    public void setBoolEstado(boolean boolEstado) {
        this.boolEstado = boolEstado;
    }

}
