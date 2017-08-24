/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author vinsfran
 */
@Embeddable
public class FichaCalendarioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_calendario")
    @Temporal(TemporalType.DATE)
    private Date fechaCalendario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "numero_documento_funcionario")
    private String numeroDocumentoFuncionario;

    public FichaCalendarioPK() {
    }

    public FichaCalendarioPK(Date fechaCalendario, String numeroDocumentoFuncionario) {
        this.fechaCalendario = fechaCalendario;
        this.numeroDocumentoFuncionario = numeroDocumentoFuncionario;
    }

    public Date getFechaCalendario() {
        return fechaCalendario;
    }

    public void setFechaCalendario(Date fechaCalendario) {
        this.fechaCalendario = fechaCalendario;
    }

    public String getNumeroDocumentoFuncionario() {
        return numeroDocumentoFuncionario;
    }

    public void setNumeroDocumentoFuncionario(String numeroDocumentoFuncionario) {
        this.numeroDocumentoFuncionario = numeroDocumentoFuncionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechaCalendario != null ? fechaCalendario.hashCode() : 0);
        hash += (numeroDocumentoFuncionario != null ? numeroDocumentoFuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FichaCalendarioPK)) {
            return false;
        }
        FichaCalendarioPK other = (FichaCalendarioPK) object;
        if ((this.fechaCalendario == null && other.fechaCalendario != null) || (this.fechaCalendario != null && !this.fechaCalendario.equals(other.fechaCalendario))) {
            return false;
        }
        if ((this.numeroDocumentoFuncionario == null && other.numeroDocumentoFuncionario != null) || (this.numeroDocumentoFuncionario != null && !this.numeroDocumentoFuncionario.equals(other.numeroDocumentoFuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.FichaCalendarioPK[ fechaCalendario=" + fechaCalendario + ", numeroDocumentoFuncionario=" + numeroDocumentoFuncionario + " ]";
    }
    
}
