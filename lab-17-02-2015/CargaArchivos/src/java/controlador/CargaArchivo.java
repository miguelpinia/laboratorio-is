/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import modelo.Archivo;
import modelo.ArchivoJpaController;

/**
 *
 * @author miguel
 */
public class CargaArchivo {

    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("CargaArchivosPU");

    private final ArchivoJpaController controller;

    public CargaArchivo() {
        controller = new ArchivoJpaController(factory);
    }

    public void cargaArchivo(Archivo archivo){
        controller.create(archivo);
    }

}
