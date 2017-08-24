/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.clasesaux;

import java.util.Date;
import java.util.List;
import py.gov.mca.sisrrhh.entitys.FichaCalendario;

/**
 *
 * @author vinsfran
 */
public class FuncionarioMarcacion {

    private String nroDoc;
    private String nombres;
    private String apellidos;
    private String nombresApellidos;
    private String apellidosNombres;
    private String tipoFuncionario;
    private Date fechaDesde;
    private Date fechaHasta;
    private List<FichaCalendario> listaMarcacion;

    public FuncionarioMarcacion() {
    }

    public FuncionarioMarcacion(String nroDoc, String nombres, String apellidos, String tipoFuncionario, Date fechaDesde, Date fechaHasta, List<FichaCalendario> listaMarcacion) {
        this.nroDoc = nroDoc;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipoFuncionario = tipoFuncionario;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.listaMarcacion = listaMarcacion;
        this.nombresApellidos = nombres + " " + apellidos;
        this.apellidosNombres = apellidos + ", " + nombres;
    }

    /**
     * @return the nroDoc
     */
    public String getNroDoc() {
        return nroDoc;
    }

    /**
     * @param nroDoc the nroDoc to set
     */
    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    /**
     * @return the nombres
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * @param nombres the nombres to set
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * @return the apellidos
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * @param apellidos the apellidos to set
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * @return the tipoFuncionario
     */
    public String getTipoFuncionario() {
        return tipoFuncionario;
    }

    /**
     * @param tipoFuncionario the tipoFuncionario to set
     */
    public void setTipoFuncionario(String tipoFuncionario) {
        this.tipoFuncionario = tipoFuncionario;
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
     * @return the listaMarcacion
     */
    public List<FichaCalendario> getListaMarcacion() {
        return listaMarcacion;
    }

    /**
     * @param listaMarcacion the listaMarcacion to set
     */
    public void setListaMarcacion(List<FichaCalendario> listaMarcacion) {
        this.listaMarcacion = listaMarcacion;
    }

    /**
     * @return the nombresApellidos
     */
    public String getNombresApellidos() {
        return nombresApellidos;
    }

    /**
     * @param nombresApellidos the nombresApellidos to set
     */
    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    /**
     * @return the apellidosNombres
     */
    public String getApellidosNombres() {
        return apellidosNombres;
    }

    /**
     * @param apellidosNombres the apellidosNombres to set
     */
    public void setApellidosNombres(String apellidosNombres) {
        this.apellidosNombres = apellidosNombres;
    }

}
