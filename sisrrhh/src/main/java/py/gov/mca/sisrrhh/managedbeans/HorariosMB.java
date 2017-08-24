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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Part;
import py.gov.mca.sisrrhh.entitys.Horarios;
import py.gov.mca.sisrrhh.sessionbeans.HorariosSB;

/**
 *
 * @author vinsfran
 */
@Named(value = "horariosMB")
@SessionScoped
public class HorariosMB implements Serializable {

    @EJB
    private HorariosSB ejHorariosSB;

    private Part file;
    private String nombreArchivo;
    private int cantidadRegistros;
    private boolean mostrarBotonSubirBd;
    private boolean activarBotonSubirBd;
    private List<Horarios> listaHorarios;
    private float progress;

    public HorariosMB() {
    }

    @PostConstruct
    public void init() {
    }

    public String prepararFormCargaDedeArchivoHorarios() {
        cantidadRegistros = 0;
        setNombreArchivo("Ningún archivo seleccionado");
        setMostrarBotonSubirBd(false);
        setActivarBotonSubirBd(true);
        return "/administracion/admin_form_carga_horarios_desde_archivo";
    }

    public void leerArchivoHorarios() throws ParseException {
        setCantidadRegistros(0);
        setNombreArchivo("Ningún archivo seleccionado");

        try {
            String disposition = getFile().getHeader("content-disposition");
            setNombreArchivo(disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1"));
            String tipoArchivo = getFile().getContentType();
            if (getNombreArchivo().length() == 12) {
                if (tipoArchivo.equals("text/csv")) {
                    StringBuilder nombreArchivoCorto = new StringBuilder();
                    nombreArchivoCorto.append(getNombreArchivo().charAt(0)); //h
                    nombreArchivoCorto.append(getNombreArchivo().charAt(1)); //o
                    nombreArchivoCorto.append(getNombreArchivo().charAt(2)); //r
                    nombreArchivoCorto.append(getNombreArchivo().charAt(3)); //a
                    nombreArchivoCorto.append(getNombreArchivo().charAt(4)); //r
                    nombreArchivoCorto.append(getNombreArchivo().charAt(5)); //i
                    nombreArchivoCorto.append(getNombreArchivo().charAt(6)); //o
                    nombreArchivoCorto.append(getNombreArchivo().charAt(7)); //s
                    if (nombreArchivoCorto.toString().equals("horarios")) {
                        BufferedReader bf = null;
                        try {
                            bf = new BufferedReader(new InputStreamReader(getFile().getInputStream()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String line = null;

                        try {
                            setListaHorarios(new ArrayList<Horarios>());
                            while ((line = bf.readLine()) != null) {
                                StringTokenizer tokens = new StringTokenizer(line, ";");
                                while (tokens.hasMoreTokens()) {

                                    String sHorario = tokens.nextToken();

                                    String horaSalida = sHorario.substring(4, 6);
                                    String minSalida = sHorario.substring(6, 8);
                                    String horaEntrada = sHorario.substring(0, 2);
                                    String minEntrada = sHorario.substring(2, 4);

                                    Horarios horario = new Horarios();
                                    horario.setHorario(sHorario);
                                    horario.setHoraEntrada(horaEntrada + ":" + minEntrada);
                                    horario.setHoraSalida(horaSalida + ":" + minSalida);
                                    horario.setDescripcion(horario.getHoraEntrada() + " a " + horario.getHoraSalida());
                                    horario.setCantidadHoras(0);
                                    horario.setCantidadMinutos(0);
                                    System.out.println("Horario: " + horario.getHorario() + " - " + horario.getHoraEntrada() + " - " + horario.getHoraSalida() + " - " + horario.getDescripcion());
                                    getListaHorarios().add(horario);
                                }
                            }
                            cantidadRegistros = getListaHorarios().size();
                            System.out.println("Can " + cantidadRegistros);
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "IOException", e.getMessage()));
        }
    }

    public void guardarDesdeArchivoHorarios() {
        if (getListaHorarios() != null) {
            activarBotonSubirBd = true;

            setProgress(((getListaHorarios().size() / 2) * 100) / (getListaHorarios().size()));

            int i = 0;
            for (Horarios horario : getListaHorarios()) {
                ejHorariosSB.udtade(horario);
                if (++i % 1000 == 0) {
                    ejHorariosSB.flushAndClear();
                }
                setProgress((i * 100) / (getListaHorarios().size()));
            }
            ejHorariosSB.flushAndClear();
//            if (ejHorariosSB.udtadeMultiple(getListaHorarios()).equals("OK")) {
//                progress = (getListaHorarios().size() * 100) / (getListaHorarios().size());
//                String descriAudit = "Nombre de archivo: " + getNombreArchivo() + ", Cantidad de registros: " + getListaHorarios().size();
//                //guardarAuditoria(new Date(), funcionarioUsuario.getUsuario(), funcionarioUsuario.getRolUsuario().getNombre(), "Insert o Update", "marcacion", "", descriAudit);
//                activarBotonSubirBd = false;
//            } else {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OCURRIO UN ERROR EN LA BD", ""));
//            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione otro archivo", ""));
        }
    }

    public void onComplete() {
        setProgress(0 / 1);
        //mostrarBotonSubirBd = false;       
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registros guardados " + getCantidadRegistros(), ""));
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
     * @return the listaHorarios
     */
    public List<Horarios> getListaHorarios() {
        return listaHorarios;
    }

    /**
     * @param listaHorarios the listaHorarios to set
     */
    public void setListaHorarios(List<Horarios> listaHorarios) {
        this.listaHorarios = listaHorarios;
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

}
