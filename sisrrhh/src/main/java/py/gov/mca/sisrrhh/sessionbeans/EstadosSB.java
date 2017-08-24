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
import py.gov.mca.sisrrhh.entitys.Estados;

/**
 *
 * @author vinsfran
 */
@Stateless
public class EstadosSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public Estados findByCodigo(String codigo) {
        Query q = em.createNamedQuery("Estados.findByCodigo");
        q.setParameter("codigo", codigo);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Estados) q.getResultList().get(0);
        }

    }

    @SuppressWarnings("unchecked")
    public List<Estados> findAll() {
        Query q = em.createNamedQuery("Estados.findAll");
        return q.getResultList();
    }

    public void create(Estados estado) {
        em.persist(estado);
    }

    public void udtade(Estados estado) {
        em.merge(estado);
    }

    public String udtadeMultiple(List<Estados> estados) {
        try {
            for (int i = 0; i < estados.size(); i++) {
                Estados estado = estados.get(i);
                //System.out.println("ENTRO ANTES DE CLEAR " + i);
                em.merge(estado);
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
