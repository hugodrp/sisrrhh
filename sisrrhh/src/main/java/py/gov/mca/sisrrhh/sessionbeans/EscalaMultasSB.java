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
import py.gov.mca.sisrrhh.entitys.EscalaMultas;

/**
 *
 * @author vinsfran
 */
@Stateless
public class EscalaMultasSB {
    
    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    public EscalaMultas findByHorario(Integer id) {
        Query q = em.createNamedQuery("EscalaMultas.findById");
        q.setParameter("id", id);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (EscalaMultas) q.getResultList().get(0);
        }

    }

    @SuppressWarnings("unchecked")
    public List<EscalaMultas> findAll() {
        Query q = em.createNamedQuery("EscalaMultas.findAll");
        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<EscalaMultas> findByTipoEscala(String tipoEscala) {
        Query q = em.createNamedQuery("EscalaMultas.findByTipoEscala");
        q.setParameter("tipoEscala", tipoEscala);
        return q.getResultList();
    }
    

    public void create(EscalaMultas escalaMultas) {
        em.persist(escalaMultas);
    }

    public void udtade(EscalaMultas escalaMultas) {
        em.merge(escalaMultas);
    }

    public String udtadeMultiple(List<EscalaMultas> escalaMultas) {
        try {
            for (int i = 0; i < escalaMultas.size(); i++) {
                EscalaMultas escalaMulta = escalaMultas.get(i);
                System.out.println("ENTRO ANTES DE CLEAR " + i);
                em.merge(escalaMulta);
                if ((i % 1000) == 0) {
                    flushAndClear();
                    System.out.println("ENTRO EN CLEAR " + i);
                }
                System.out.println("Cnt : " + (i + 1) + " - RESTO: " + (i % 10000));
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
