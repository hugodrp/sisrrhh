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
import py.gov.mca.sisrrhh.entitys.RelacionLaboral;

/**
 *
 * @author vinsfran
 */
@Stateless
public class RelacionLaboralSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    public RelacionLaboral findByRelacionLaboral(String relacionLaboral) {
        Query q = em.createNamedQuery("RelacionLaboral.findByRelacionLaboral");
        q.setParameter("relacionLaboral", relacionLaboral);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (RelacionLaboral) q.getResultList().get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public List<RelacionLaboral> findAll() {
        Query q = em.createNamedQuery("RelacionLaboral.findAll");
        return q.getResultList();
    }

    public void create(RelacionLaboral relacionLaboral) {
        em.persist(relacionLaboral);
    }

    public void udtade(RelacionLaboral relacionLaboral) {
        //System.out.println("ENTRO " + horario.getHorario());
        em.merge(relacionLaboral);
    }

    public String udtadeMultiple(List<RelacionLaboral> relacionLaborales) {
        try {
            for (int i = 0; i < relacionLaborales.size(); i++) {
                RelacionLaboral relacionLaboral = relacionLaborales.get(i);
                //System.out.println("ENTRO ANTES DE CLEAR " + i);
                em.merge(relacionLaboral);
                if ((i % 1000) == 0) {
                    flushAndClear();
                    //System.out.println("ENTRO EN CLEAR " + i);
                }
                //System.out.println("Cnt : " + (i + 1) + " - RESTO: " + (i % 10000));
            }
            flushAndClear();
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            flushAndClear();
            return "FAIL";
        }
    }

    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
