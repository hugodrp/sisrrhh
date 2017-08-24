/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author vinsfran
 */
@Embeddable
public class VacacionesFuncionariosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_vacacion")
    private int anioVacacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "numero_documento")
    private String numeroDocumento;

    public VacacionesFuncionariosPK() {
    }

    public VacacionesFuncionariosPK(int anioVacacion, String numeroDocumento) {
        this.anioVacacion = anioVacacion;
        this.numeroDocumento = numeroDocumento;
    }

    public int getAnioVacacion() {
        return anioVacacion;
    }

    public void setAnioVacacion(int anioVacacion) {
        this.anioVacacion = anioVacacion;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) anioVacacion;
        hash += (numeroDocumento != null ? numeroDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VacacionesFuncionariosPK)) {
            return false;
        }
        VacacionesFuncionariosPK other = (VacacionesFuncionariosPK) object;
        if (this.anioVacacion != other.anioVacacion) {
            return false;
        }
        if ((this.numeroDocumento == null && other.numeroDocumento != null) || (this.numeroDocumento != null && !this.numeroDocumento.equals(other.numeroDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.VacacionesFuncionariosPK[ anioVacacion=" + anioVacacion + ", numeroDocumento=" + numeroDocumento + " ]";
    }
    
}
