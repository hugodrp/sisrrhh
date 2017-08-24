/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.sessionbeans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import py.gov.mca.sisrrhh.entitys.Auditoria;

/**
 *
 * @author vinsfran
 */
@Stateless
public class AuditoriaSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Auditoria> findByCodigo(String codigo) {
        Query q = em.createNamedQuery("Auditoria.findByCodigo");
        q.setParameter("codigo", codigo);
        return q.getResultList();
    }

    public void create(Auditoria auditoria) {
        em.persist(auditoria);
    }

    public void udtade(Auditoria auditoria) {
        em.merge(auditoria);
    }
    
    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
