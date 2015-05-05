/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import jpa.exceptions.NonexistentEntityException;
import jpa.exceptions.RollbackFailureException;
import model.Imagen;
import model.MimeType;

/**
 *
 * @author miguel
 */
public class ImagenJpaController implements Serializable {

    public ImagenJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Imagen imagen) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MimeType mimeTypeId = imagen.getMimeTypeId();
            if (mimeTypeId != null) {
                mimeTypeId = em.getReference(mimeTypeId.getClass(), mimeTypeId.getId());
                imagen.setMimeTypeId(mimeTypeId);
            }
            em.persist(imagen);
            if (mimeTypeId != null) {
                mimeTypeId.getImagenList().add(imagen);
                mimeTypeId = em.merge(mimeTypeId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Imagen imagen) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Imagen persistentImagen = em.find(Imagen.class, imagen.getId());
            MimeType mimeTypeIdOld = persistentImagen.getMimeTypeId();
            MimeType mimeTypeIdNew = imagen.getMimeTypeId();
            if (mimeTypeIdNew != null) {
                mimeTypeIdNew = em.getReference(mimeTypeIdNew.getClass(), mimeTypeIdNew.getId());
                imagen.setMimeTypeId(mimeTypeIdNew);
            }
            imagen = em.merge(imagen);
            if (mimeTypeIdOld != null && !mimeTypeIdOld.equals(mimeTypeIdNew)) {
                mimeTypeIdOld.getImagenList().remove(imagen);
                mimeTypeIdOld = em.merge(mimeTypeIdOld);
            }
            if (mimeTypeIdNew != null && !mimeTypeIdNew.equals(mimeTypeIdOld)) {
                mimeTypeIdNew.getImagenList().add(imagen);
                mimeTypeIdNew = em.merge(mimeTypeIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = imagen.getId();
                if (findImagen(id) == null) {
                    throw new NonexistentEntityException("The imagen with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Imagen imagen;
            try {
                imagen = em.getReference(Imagen.class, id);
                imagen.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The imagen with id " + id + " no longer exists.", enfe);
            }
            MimeType mimeTypeId = imagen.getMimeTypeId();
            if (mimeTypeId != null) {
                mimeTypeId.getImagenList().remove(imagen);
                mimeTypeId = em.merge(mimeTypeId);
            }
            em.remove(imagen);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Imagen> findImagenEntities() {
        return findImagenEntities(true, -1, -1);
    }

    public List<Imagen> findImagenEntities(int maxResults, int firstResult) {
        return findImagenEntities(false, maxResults, firstResult);
    }

    private List<Imagen> findImagenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Imagen.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Imagen findImagen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Imagen.class, id);
        } finally {
            em.close();
        }
    }

    public Imagen findDocumento(String url) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Imagen.findByRuta");
        q.setParameter("ruta", url);
        return (Imagen) q.getResultList().get(0);
    }

    public Imagen findImagen(String name) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Imagen.findByNombre");
        q.setParameter("nombre", name);
        return (Imagen) q.getResultList().get(0);
    }

    public int getImagenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Imagen> rt = cq.from(Imagen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
