package fms.HibernateControl;

import fms.model.Categories.Category;
import fms.model.Categories.topCategory;
import fms.model.Finances.Expenses;
import fms.model.Finances.Income;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public class CategoriesHibernateControl {
    public CategoriesHibernateControl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void createTopCategory(Category cat) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cat);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTopCategory(cat.getId()) != null) {
                throw new Exception("Top Category: " + cat + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editTopCategory(Category cat) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            cat = em.merge(cat);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = cat.getId();
                if (findTopCategory(id) == null) {
                    throw new Exception("Top category with id " + cat + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<topCategory> findTopCategoryEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(topCategory.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Category findTopCategory(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public void removeSubCategory(Category category, Category subCat) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category.removeSubCategory(subCat);
                em.merge(category);
                em.merge(subCat);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing subCategory from category list", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeExpenseFromCat(Category category, Expenses expense) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category.removeExpenseFromList(expense);
                em.merge(category);
                em.merge(expense);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing income from category list", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeIncomeFromCat(Category category, Income income) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category.removeIncomeFromList(income);
                em.merge(category);
                em.merge(income);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing income from category list", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
