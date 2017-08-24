/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.clasesaux;

/**
 *
 * @author vinsfran
 */
public class MultaCsv {

    private String nroDoc;
    private Integer sueldoDiario;
    private Integer porcentaMultaTotal;
    private Integer totalMontoFijo;
    private Integer totalDiasTrabajados;
    private Integer totalDiasComplementarios;
    private Integer totalMulta;

    public MultaCsv() {
    }

    public MultaCsv(String nroDoc, Integer sueldoDiario, Integer porcentaMultaTotal, Integer totalMontoFijo, Integer totalDiasTrabajados, Integer totalDiasComplementarios, Integer totalMulta) {
        this.nroDoc = nroDoc;
        this.sueldoDiario = sueldoDiario;
        this.porcentaMultaTotal = porcentaMultaTotal;
        this.totalMontoFijo = totalMontoFijo;
        this.totalDiasTrabajados = totalDiasTrabajados;
        this.totalDiasComplementarios = totalDiasComplementarios;
        this.totalMulta = totalMulta;
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
     * @return the porcentaMultaTotal
     */
    public Integer getPorcentaMultaTotal() {
        return porcentaMultaTotal;
    }

    /**
     * @param porcentaMultaTotal the porcentaMultaTotal to set
     */
    public void setPorcentaMultaTotal(Integer porcentaMultaTotal) {
        this.porcentaMultaTotal = porcentaMultaTotal;
    }

    /**
     * @return the totalMulta
     */
    public Integer getTotalMulta() {
        return totalMulta;
    }

    /**
     * @param totalMulta the totalMulta to set
     */
    public void setTotalMulta(Integer totalMulta) {
        this.totalMulta = totalMulta;
    }

    /**
     * @return the sueldoDiario
     */
    public Integer getSueldoDiario() {
        return sueldoDiario;
    }

    /**
     * @param sueldoDiario the sueldoDiario to set
     */
    public void setSueldoDiario(Integer sueldoDiario) {
        this.sueldoDiario = sueldoDiario;
    }

    /**
     * @return the totalMontoFijo
     */
    public Integer getTotalMontoFijo() {
        return totalMontoFijo;
    }

    /**
     * @param totalMontoFijo the totalMontoFijo to set
     */
    public void setTotalMontoFijo(Integer totalMontoFijo) {
        this.totalMontoFijo = totalMontoFijo;
    }

    /**
     * @return the totalDiasTrabajados
     */
    public Integer getTotalDiasTrabajados() {
        return totalDiasTrabajados;
    }

    /**
     * @param totalDiasTrabajados the totalDiasTrabajados to set
     */
    public void setTotalDiasTrabajados(Integer totalDiasTrabajados) {
        this.totalDiasTrabajados = totalDiasTrabajados;
    }

    /**
     * @return the totalDiasComplementarios
     */
    public Integer getTotalDiasComplementarios() {
        return totalDiasComplementarios;
    }

    /**
     * @param totalDiasComplementarios the totalDiasComplementarios to set
     */
    public void setTotalDiasComplementarios(Integer totalDiasComplementarios) {
        this.totalDiasComplementarios = totalDiasComplementarios;
    }

}
