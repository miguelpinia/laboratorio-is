/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author miguel
 */
@Entity
@Table(catalog = "imagenes", schema = "documentos")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Imagen.findAll", query = "SELECT i FROM Imagen i"), @NamedQuery(name = "Imagen.findById", query = "SELECT i FROM Imagen i WHERE i.id = :id"), @NamedQuery(name = "Imagen.findByNombre", query = "SELECT i FROM Imagen i WHERE i.nombre = :nombre"), @NamedQuery(name = "Imagen.findByRuta", query = "SELECT i FROM Imagen i WHERE i.ruta = :ruta")})
public class Imagen implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(nullable = false)
    private Integer id;
    @Basic(optional = false) @NotNull @Size(min = 1, max = 2147483647) @Column(nullable = false, length = 2147483647)
    private String nombre;
    @Basic(optional = false) @NotNull @Lob @Column(nullable = false)
    private byte[] contenido;
    @Basic(optional = false) @NotNull @Size(min = 1, max = 2147483647) @Column(nullable = false, length = 2147483647)
    private String ruta;
    @JoinColumn(name = "mime_type_id", referencedColumnName = "id", nullable = false) @ManyToOne(optional = false)
    private MimeType mimeTypeId;

    public Imagen() {
    }

    public Imagen(Integer id) {
        this.id = id;
    }

    public Imagen(Integer id, String nombre, byte[] contenido, String ruta) {
        this.id = id;
        this.nombre = nombre;
        this.contenido = contenido;
        this.ruta = ruta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public MimeType getMimeTypeId() {
        return mimeTypeId;
    }

    public void setMimeTypeId(MimeType mimeTypeId) {
        this.mimeTypeId = mimeTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Imagen)) {
            return false;
        }
        Imagen other = (Imagen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Imagen[ id=" + id + " ]";
    }

}
