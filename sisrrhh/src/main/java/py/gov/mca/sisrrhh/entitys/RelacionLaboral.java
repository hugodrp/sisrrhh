/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author vinsfran
 */
@Entity
@Table(name = "relacion_laboral")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelacionLaboral.findAll", query = "SELECT r FROM RelacionLaboral r"),
    @NamedQuery(name = "RelacionLaboral.findByRelacionLaboral", query = "SELECT r FROM RelacionLaboral r WHERE r.relacionLaboral = :relacionLaboral"),
    @NamedQuery(name = "RelacionLaboral.findByDescripcion", query = "SELECT r FROM RelacionLaboral r WHERE r.descripcion = :descripcion")})
public class RelacionLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "relacion_laboral")
    private String relacionLaboral;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "relacionLaboral")
    private List<Funcionarios> funcionariosList;

    public RelacionLaboral() {
    }

    public RelacionLaboral(String relacionLaboral) {
        this.relacionLaboral = relacionLaboral;
    }

    public RelacionLaboral(String relacionLaboral, String descripcion) {
        this.relacionLaboral = relacionLaboral;
        this.descripcion = descripcion;
    }

    public String getRelacionLaboral() {
        return relacionLaboral;
    }

    public void setRelacionLaboral(String relacionLaboral) {
        this.relacionLaboral = relacionLaboral;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Funcionarios> getFuncionariosList() {
        return funcionariosList;
    }

    public void setFuncionariosList(List<Funcionarios> funcionariosList) {
        this.funcionariosList = funcionariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relacionLaboral != null ? relacionLaboral.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelacionLaboral)) {
            return false;
        }
        RelacionLaboral other = (RelacionLaboral) object;
        if ((this.relacionLaboral == null && other.relacionLaboral != null) || (this.relacionLaboral != null && !this.relacionLaboral.equals(other.relacionLaboral))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.RelacionLaboral[ relacionLaboral=" + relacionLaboral + " ]";
    }
    
}
