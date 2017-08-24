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
import py.gov.mca.sisrrhh.entitys.TiposExcepciones;

/**
 *
 * @author vinsfran
 */
@Stateless
public class TiposExcepcionesSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public TiposExcepciones findByCodigoExcepcion(String codigo) {
        Query q = em.createNamedQuery("TiposExcepciones.findByCodigoExcepcion");
        q.setParameter("codigoExcepcion", codigo);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (TiposExcepciones) q.getResultList().get(0);
        }

    }

    @SuppressWarnings("unchecked")
    public List<TiposExcepciones> findAll() {
        Query q = em.createNamedQuery("TiposExcepciones.findAll");
        return q.getResultList();
    }

    public void create(TiposExcepciones horario) {
        em.persist(horario);
    }

    public void udtade(TiposExcepciones tipoExcepcion) {
        em.merge(tipoExcepcion);
    }

    public String udtadeMultiple(List<TiposExcepciones> tiposExcepciones) {
        try {
            for (int i = 0; i < tiposExcepciones.size(); i++) {
                TiposExcepciones tipoExcepcion = tiposExcepciones.get(i);
                System.out.println("ENTRO ANTES DE CLEAR " + i);
                em.merge(tipoExcepcion);
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
