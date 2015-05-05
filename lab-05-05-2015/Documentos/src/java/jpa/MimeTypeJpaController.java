/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;
import jpa.exceptions.RollbackFailureException;
import model.Imagen;
import model.MimeType;

/**
 *
 * @author miguel
 */
public class MimeTypeJpaController implements Serializable {

    public MimeTypeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MimeType mimeType) throws RollbackFailureException, Exception {
        if (mimeType.getImagenList() == null) {
            mimeType.setImagenList(new ArrayList<Imagen>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Imagen> attachedImagenList = new ArrayList<Imagen>();
            for (Imagen imagenListImagenToAttach : mimeType.getImagenList()) {
                imagenListImagenToAttach = em.getReference(imagenListImagenToAttach.getClass(), imagenListImagenToAttach.getId());
                attachedImagenList.add(imagenListImagenToAttach);
            }
            mimeType.setImagenList(attachedImagenList);
            em.persist(mimeType);
            for (Imagen imagenListImagen : mimeType.getImagenList()) {
                MimeType oldMimeTypeIdOfImagenListImagen = imagenListImagen.getMimeTypeId();
                imagenListImagen.setMimeTypeId(mimeType);
                imagenListImagen = em.merge(imagenListImagen);
                if (oldMimeTypeIdOfImagenListImagen != null) {
                    oldMimeTypeIdOfImagenListImagen.getImagenList().remove(imagenListImagen);
                    oldMimeTypeIdOfImagenListImagen = em.merge(oldMimeTypeIdOfImagenListImagen);
                }
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

    public void edit(MimeType mimeType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MimeType persistentMimeType = em.find(MimeType.class, mimeType.getId());
            List<Imagen> imagenListOld = persistentMimeType.getImagenList();
            List<Imagen> imagenListNew = mimeType.getImagenList();
            List<String> illegalOrphanMessages = null;
            for (Imagen imagenListOldImagen : imagenListOld) {
                if (!imagenListNew.contains(imagenListOldImagen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Imagen " + imagenListOldImagen + " since its mimeTypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Imagen> attachedImagenListNew = new ArrayList<Imagen>();
            for (Imagen imagenListNewImagenToAttach : imagenListNew) {
                imagenListNewImagenToAttach = em.getReference(imagenListNewImagenToAttach.getClass(), imagenListNewImagenToAttach.getId());
                attachedImagenListNew.add(imagenListNewImagenToAttach);
            }
            imagenListNew = attachedImagenListNew;
            mimeType.setImagenList(imagenListNew);
            mimeType = em.merge(mimeType);
            for (Imagen imagenListNewImagen : imagenListNew) {
                if (!imagenListOld.contains(imagenListNewImagen)) {
                    MimeType oldMimeTypeIdOfImagenListNewImagen = imagenListNewImagen.getMimeTypeId();
                    imagenListNewImagen.setMimeTypeId(mimeType);
                    imagenListNewImagen = em.merge(imagenListNewImagen);
                    if (oldMimeTypeIdOfImagenListNewImagen != null && !oldMimeTypeIdOfImagenListNewImagen.equals(mimeType)) {
                        oldMimeTypeIdOfImagenListNewImagen.getImagenList().remove(imagenListNewImagen);
                        oldMimeTypeIdOfImagenListNewImagen = em.merge(oldMimeTypeIdOfImagenListNewImagen);
                    }
                }
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
                Integer id = mimeType.getId();
                if (findMimeType(id) == null) {
                    throw new NonexistentEntityException("The mimeType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MimeType mimeType;
            try {
                mimeType = em.getReference(MimeType.class, id);
                mimeType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mimeType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Imagen> imagenListOrphanCheck = mimeType.getImagenList();
            for (Imagen imagenListOrphanCheckImagen : imagenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MimeType (" + mimeType + ") cannot be destroyed since the Imagen " + imagenListOrphanCheckImagen + " in its imagenList field has a non-nullable mimeTypeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mimeType);
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

    public List<MimeType> findMimeTypeEntities() {
        return findMimeTypeEntities(true, -1, -1);
    }

    public List<MimeType> findMimeTypeEntities(int maxResults, int firstResult) {
        return findMimeTypeEntities(false, maxResults, firstResult);
    }

    private List<MimeType> findMimeTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MimeType.class));
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

    public MimeType findMimeType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MimeType.class, id);
        } finally {
            em.close();
        }
    }

    public MimeType findMimeType(String nombre) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("MimeType.findByNombre");
        q.setParameter("nombre", nombre);
        return (MimeType) q.getResultList().get(0);
    }

    public int getMimeTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MimeType> rt = cq.from(MimeType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
