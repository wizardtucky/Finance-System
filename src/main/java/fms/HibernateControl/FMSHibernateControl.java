package fms.HibernateControl;

import fms.model.Categories.Category;
import fms.model.FinanceManagementSystem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class FMSHibernateControl {
    public FMSHibernateControl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FinanceManagementSystem fms) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(fms);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFMS(fms.getId()) != null) {
                throw new Exception("fms: " + fms + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void edit(FinanceManagementSystem fms) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            fms = em.merge(fms);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = fms.getId();
                if (findFMS(id) == null) {
                    throw new Exception("The fms with id " + fms + " no longer exists.");
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
            FinanceManagementSystem fms;
            try {
                fms = em.getReference(FinanceManagementSystem.class, id);
                fms.getId();
                em.merge(fms);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The fms with name " + id + " no longer exists.", enfe);
            }
            em.remove(fms);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FinanceManagementSystem> findFMSEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FinanceManagementSystem.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public FinanceManagementSystem findFirstFMSTable() {
        EntityManager em = getEntityManager();
        List<FinanceManagementSystem> list;
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FinanceManagementSystem.class));
            Query q = em.createQuery(cq);
            list = q.getResultList();
        } finally {
            em.close();
        }
        return list.get(0);
    }

    public FinanceManagementSystem findFMS(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FinanceManagementSystem.class, id);
        } finally {
            em.close();
        }
    }

    public int getFMSCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FinanceManagementSystem> rt = cq.from(FinanceManagementSystem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void removeTopCategory(FinanceManagementSystem fms, Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                //fms.removeTopCategory(category);
                em.merge(fms);
                em.merge(category);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing category from fms", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
