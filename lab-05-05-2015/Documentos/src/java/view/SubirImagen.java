/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import jpa.ImagenJpaController;
import jpa.MimeTypeJpaController;
import model.MimeType;

/**
 *
 * @author miguel
 */
public class SubirImagen extends HttpServlet {

    private String dirUploadFiles;

    @Resource
    private UserTransaction utx;

    @PersistenceUnit(unitName = "DocumentosPU")
    private EntityManagerFactory emf;

    /**
     * Processes requests for both HTTP <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ImagenJpaController ijc = new ImagenJpaController(utx, emf);
            MimeTypeJpaController mjc = new MimeTypeJpaController(utx, emf);
            model.Imagen imagen = new model.Imagen();
            dirUploadFiles = "imagenes/";
            if (ServletFileUpload.isMultipartContent(request)) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setSizeMax(Long.MAX_VALUE);

                List listUploadFiles = null;
                FileItem item = null;

                try {

                    listUploadFiles = upload.parseRequest(request);

                    Iterator it = listUploadFiles.iterator();
                    while (it.hasNext()) {
                        item = (FileItem) it.next();

                        if (!item.isFormField()) {

                            if (item.getSize() > 0) {

                                String nombre = item.getName();
                                imagen.setNombre(nombre);
                                String tipo = item.getContentType();
                                MimeType mimeType = mjc.findMimeType(tipo);
                                imagen.setMimeTypeId(mimeType);
                                imagen.setRuta(dirUploadFiles + nombre);
                                long tamanio = item.getSize();
                                String extension = nombre.substring(nombre.lastIndexOf("."));
                                imagen.setContenido(item.get());
                                ijc.create(imagen);
                                out.println("Nombre: " + nombre + "<br>");
                                out.println("Tipo: " + tipo + "<br>");
                                out.println("Extension: " + extension + "<br>");
                                out.println("GUARDADO " + imagen.getRuta() + "</p>");
                                out.println("<a href='/Documentos/ListaImagenes'>Imagenes Subidas </a>");
                            }
                        }
                    }

                } catch (FileUploadException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    // poner respuesta = false; si existe alguna problema
                    e.printStackTrace();
                }
            }
            out.println("Fin de la operacion! ;)");
            out.close();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
