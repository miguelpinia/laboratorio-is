/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author miguel
 */
@Entity
@Table(catalog = "test", schema = "public")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Archivo.findAll", query = "SELECT a FROM Archivo a"), @NamedQuery(name = "Archivo.findById", query = "SELECT a FROM Archivo a WHERE a.id = :id"), @NamedQuery(name = "Archivo.findByNombre", query = "SELECT a FROM Archivo a WHERE a.nombre = :nombre"), @NamedQuery(name = "Archivo.findByPath", query = "SELECT a FROM Archivo a WHERE a.path = :path")})
public class Archivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(nullable = false)
    private Integer id;
    @Basic(optional = false) @Column(nullable = false, length = 2147483647)
    private String nombre;
    @Basic(optional = false) @Lob @Column(nullable = false)
    private byte[] contenido;
    @Basic(optional = false) @Column(nullable = false, length = 2147483647)
    private String path;
    @JoinColumn(name = "mime_type_id", referencedColumnName = "id", nullable = false) @ManyToOne(optional = false)
    private MimeType mimeTypeId;

    public Archivo() {
    }

    public Archivo(Integer id) {
        this.id = id;
    }

    public Archivo(Integer id, String nombre, byte[] contenido, String path) {
        this.id = id;
        this.nombre = nombre;
        this.contenido = contenido;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        if (!(object instanceof Archivo)) {
            return false;
        }
        Archivo other = (Archivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Archivo[ id=" + id + " ]";
    }

}
