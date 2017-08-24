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
import py.gov.mca.sisrrhh.entitys.FechasEspeciales;

/**
 *
 * @author vinsfran
 */
@Stateless
public class FechasEspecialesSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public FechasEspeciales findByFechaEspecial(Date fechaEspecial) {
        Query q = em.createNamedQuery("FechasEspeciales.findByFechaEspecial");
        q.setParameter("fechaEspecial", fechaEspecial);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (FechasEspeciales) q.getResultList().get(0);
        }
    }

    public void create(FechasEspeciales fechasEspeciales) {
        em.persist(fechasEspeciales);
    }

    public void udtade(FechasEspeciales fechasEspeciales) {
        em.merge(fechasEspeciales);
    }

    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
