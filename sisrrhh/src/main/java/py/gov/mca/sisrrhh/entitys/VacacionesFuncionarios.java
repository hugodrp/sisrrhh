/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vinsfran
 */
@Entity
@Table(name = "vacaciones_funcionarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VacacionesFuncionarios.findAll", query = "SELECT v FROM VacacionesFuncionarios v"),
    @NamedQuery(name = "VacacionesFuncionarios.findByAnioVacacion", query = "SELECT v FROM VacacionesFuncionarios v WHERE v.vacacionesFuncionariosPK.anioVacacion = :anioVacacion"),
    @NamedQuery(name = "VacacionesFuncionarios.findByNumeroDocumento", query = "SELECT v FROM VacacionesFuncionarios v WHERE v.vacacionesFuncionariosPK.numeroDocumento = :numeroDocumento"),
    @NamedQuery(name = "VacacionesFuncionarios.findByCantidadDiasCorrespondiente", query = "SELECT v FROM VacacionesFuncionarios v WHERE v.cantidadDiasCorrespondiente = :cantidadDiasCorrespondiente"),
    @NamedQuery(name = "VacacionesFuncionarios.findByCantidadDiasRestante", query = "SELECT v FROM VacacionesFuncionarios v WHERE v.cantidadDiasRestante = :cantidadDiasRestante")})
public class VacacionesFuncionarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VacacionesFuncionariosPK vacacionesFuncionariosPK;
    @Column(name = "cantidad_dias_correspondiente")
    private Integer cantidadDiasCorrespondiente;
    @Column(name = "cantidad_dias_restante")
    private Integer cantidadDiasRestante;
    @JoinColumn(name = "estado_vacacion", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estadoVacacion;
    @JoinColumn(name = "numero_documento", referencedColumnName = "numero_documento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Funcionarios funcionarios;

    public VacacionesFuncionarios() {
    }

    public VacacionesFuncionarios(VacacionesFuncionariosPK vacacionesFuncionariosPK) {
        this.vacacionesFuncionariosPK = vacacionesFuncionariosPK;
    }

    public VacacionesFuncionarios(int anioVacacion, String numeroDocumento) {
        this.vacacionesFuncionariosPK = new VacacionesFuncionariosPK(anioVacacion, numeroDocumento);
    }

    public VacacionesFuncionariosPK getVacacionesFuncionariosPK() {
        return vacacionesFuncionariosPK;
    }

    public void setVacacionesFuncionariosPK(VacacionesFuncionariosPK vacacionesFuncionariosPK) {
        this.vacacionesFuncionariosPK = vacacionesFuncionariosPK;
    }

    public Integer getCantidadDiasCorrespondiente() {
        return cantidadDiasCorrespondiente;
    }

    public void setCantidadDiasCorrespondiente(Integer cantidadDiasCorrespondiente) {
        this.cantidadDiasCorrespondiente = cantidadDiasCorrespondiente;
    }

    public Integer getCantidadDiasRestante() {
        return cantidadDiasRestante;
    }

    public void setCantidadDiasRestante(Integer cantidadDiasRestante) {
        this.cantidadDiasRestante = cantidadDiasRestante;
    }

    public Estados getEstadoVacacion() {
        return estadoVacacion;
    }

    public void setEstadoVacacion(Estados estadoVacacion) {
        this.estadoVacacion = estadoVacacion;
    }

    public Funcionarios getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Funcionarios funcionarios) {
        this.funcionarios = funcionarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vacacionesFuncionariosPK != null ? vacacionesFuncionariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VacacionesFuncionarios)) {
            return false;
        }
        VacacionesFuncionarios other = (VacacionesFuncionarios) object;
        if ((this.vacacionesFuncionariosPK == null && other.vacacionesFuncionariosPK != null) || (this.vacacionesFuncionariosPK != null && !this.vacacionesFuncionariosPK.equals(other.vacacionesFuncionariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.VacacionesFuncionarios[ vacacionesFuncionariosPK=" + vacacionesFuncionariosPK + " ]";
    }
    
}
