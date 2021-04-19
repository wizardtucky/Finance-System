package fms.HibernateControl;

import fms.model.Finances.Income;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class IncomeHibernateControl {
    public IncomeHibernateControl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Income income) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(income);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIncome(income.getId()) != null) {
                throw new Exception("income: " + income + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Income income) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            income = em.merge(income);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = income.getId();
                if (findIncome(id) == null) {
                    throw new Exception("income with id " + income + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Income income;
            try {
                income = em.getReference(Income.class, id);
                income.getId();
                em.merge(income);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("income with id " + id + " no longer exists.", enfe);
            }
            em.remove(income);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Income> findIncomeEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Income.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Income findIncome(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Income.class, id);
        } finally {
            em.close();
        }
    }
}

