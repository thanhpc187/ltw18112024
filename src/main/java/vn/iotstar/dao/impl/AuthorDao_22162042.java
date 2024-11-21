package vn.iotstar.dao.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.com.config.JPAConfig;
import org.com.dao.IAuthorDao;
import org.com.entity.Author;

import java.util.List;

public class AuthorDao implements IAuthorDao {
    @Override
    public void insert(Author author) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            //gọi phuong thức để insert, update, delete
            enma.persist(author);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(Author author) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            //gọi phuong thức để insert, update, delete
            enma.merge(author);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(int cateid) throws Exception {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            //gọi phuong thức để insert, update, delete
            Author author = enma.find(Author.class, cateid);
            if (author != null) {
                enma.remove(author);
            } else {
                throw new Exception("Không tìm thấy");
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public Author findById(int cateid) {
        EntityManager enma = JPAConfig.getEntityManager();
        return enma.find(Author.class, cateid);
    }

    @Override
    public List<Author> findAll() {
        EntityManager enma = JPAConfig.getEntityManager();
        TypedQuery<Author> query = enma.createNamedQuery("Author.findAll", Author.class);
        return query.getResultList();
    }

    @Override
    public List<Author> findByCategoryname(String authorName) {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT c FROM Author c WHERE c.authorName like :authorName";
        TypedQuery<Author> query = enma.createQuery(jpql, Author.class);
        query.setParameter("authorName", "%" + authorName + "%");
        return query.getResultList();
    }

    @Override
    public List<Author> findAll(int page, int pagesize) {
        EntityManager enma = JPAConfig.getEntityManager();
        TypedQuery<Author> query = enma.createNamedQuery("Author.findAll", Author.class);
        query.setFirstResult(page * pagesize);
        query.setMaxResults(pagesize);
        return query.getResultList();
    }

    @Override
    public int count() {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT count(c) FROM Author c";
        Query query = enma.createQuery(jpql);
        return ((Long) query.getSingleResult()).intValue();
    }
}
