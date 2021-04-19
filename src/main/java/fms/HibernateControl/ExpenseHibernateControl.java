package fms.HibernateControl;

import fms.model.Finances.Expenses;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class ExpenseHibernateControl {
    public ExpenseHibernateControl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Expenses expense) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(expense);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExpense(expense.getId()) != null) {
                throw new Exception("expense: " + expense + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Expenses expense) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            expense = em.merge(expense);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = expense.getId();
                if (findExpense(id) == null) {
                    throw new Exception("expense with id " + expense + " no longer exists.");
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
            Expenses expense;
            try {
                expense = em.getReference(Expenses.class, id);
                expense.getId();
                em.merge(expense);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("expense with id " + id + " no longer exists.", enfe);
            }
            em.remove(expense);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Expenses> findExpenseEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Expenses.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Expenses findExpense(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Expenses.class, id);
        } finally {
            em.close();
        }
    }
}
