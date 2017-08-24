/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.sessionbeans;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import py.gov.mca.sisrrhh.entitys.VacacionesFuncionarios;

/**
 *
 * @author vinsfran
 */
@Stateless
public class VacacionesFuncionariosSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;
    
   

    public List<VacacionesFuncionarios> findByNumeroDocumentoEstadoVacacion(String numeroDocumento, String estadoVacacion) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM VacacionesFuncionarios e ");
        jpql.append("WHERE e.vacacionesFuncionariosPK.numeroDocumento = :numeroDocumento ");
        jpql.append("AND e.estadoVacacion.codigo = :estadoVacacion ");
        jpql.append("ORDER BY e.vacacionesFuncionariosPK.anioVacacion DESC ");        
        Query q = em.createQuery(jpql.toString());
        q.setParameter("numeroDocumento", numeroDocumento);
        q.setParameter("estadoVacacion", estadoVacacion);        
        return q.getResultList();
    }
    
    public List<VacacionesFuncionarios> findByNumeroDocumentoEstadoVacacionConDiasRestantes(String numeroDocumento, String estadoVacacion) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM VacacionesFuncionarios e ");
        jpql.append("WHERE e.vacacionesFuncionariosPK.numeroDocumento = :numeroDocumento ");
        jpql.append("AND e.estadoVacacion.codigo = :estadoVacacion ");
        jpql.append("AND e.cantidadDiasRestante > 0 ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("numeroDocumento", numeroDocumento);
        q.setParameter("estadoVacacion", estadoVacacion);
        return q.getResultList();
    }

    public void create(VacacionesFuncionarios vacacionesFuncionarios) {
        em.persist(vacacionesFuncionarios);
    }

    public void udtade(VacacionesFuncionarios vacacionesFuncionarios) {
        em.merge(vacacionesFuncionarios);
    }

    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
