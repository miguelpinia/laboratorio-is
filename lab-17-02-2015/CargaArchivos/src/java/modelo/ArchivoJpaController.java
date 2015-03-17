/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import modelo.exceptions.NonexistentEntityException;

/**
 *
 * @author miguel
 */
public class ArchivoJpaController implements Serializable {

    public ArchivoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Archivo archivo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MimeType mimeTypeId = archivo.getMimeTypeId();
            if (mimeTypeId != null) {
                mimeTypeId = em.getReference(mimeTypeId.getClass(), mimeTypeId.getId());
                archivo.setMimeTypeId(mimeTypeId);
            }
            em.persist(archivo);
            if (mimeTypeId != null) {
                mimeTypeId.getArchivoList().add(archivo);
                mimeTypeId = em.merge(mimeTypeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Archivo archivo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivo persistentArchivo = em.find(Archivo.class, archivo.getId());
            MimeType mimeTypeIdOld = persistentArchivo.getMimeTypeId();
            MimeType mimeTypeIdNew = archivo.getMimeTypeId();
            if (mimeTypeIdNew != null) {
                mimeTypeIdNew = em.getReference(mimeTypeIdNew.getClass(), mimeTypeIdNew.getId());
                archivo.setMimeTypeId(mimeTypeIdNew);
            }
            archivo = em.merge(archivo);
            if (mimeTypeIdOld != null && !mimeTypeIdOld.equals(mimeTypeIdNew)) {
                mimeTypeIdOld.getArchivoList().remove(archivo);
                mimeTypeIdOld = em.merge(mimeTypeIdOld);
            }
            if (mimeTypeIdNew != null && !mimeTypeIdNew.equals(mimeTypeIdOld)) {
                mimeTypeIdNew.getArchivoList().add(archivo);
                mimeTypeIdNew = em.merge(mimeTypeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = archivo.getId();
                if (findArchivo(id) == null) {
                    throw new NonexistentEntityException("The archivo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Archivo archivo;
            try {
                archivo = em.getReference(Archivo.class, id);
                archivo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The archivo with id " + id + " no longer exists.", enfe);
            }
            MimeType mimeTypeId = archivo.getMimeTypeId();
            if (mimeTypeId != null) {
                mimeTypeId.getArchivoList().remove(archivo);
                mimeTypeId = em.merge(mimeTypeId);
            }
            em.remove(archivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Archivo> findArchivoEntities() {
        return findArchivoEntities(true, -1, -1);
    }

    public List<Archivo> findArchivoEntities(int maxResults, int firstResult) {
        return findArchivoEntities(false, maxResults, firstResult);
    }

    private List<Archivo> findArchivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Archivo.class));
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

    public Archivo findArchivo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Archivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArchivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Archivo> rt = cq.from(Archivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
