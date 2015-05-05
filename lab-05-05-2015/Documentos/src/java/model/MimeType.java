/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author miguel
 */
@Entity
@Table(name = "mime_type", catalog = "imagenes", schema = "documentos", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre"})})
@XmlRootElement
@NamedQueries({@NamedQuery(name = "MimeType.findAll", query = "SELECT m FROM MimeType m"), @NamedQuery(name = "MimeType.findById", query = "SELECT m FROM MimeType m WHERE m.id = :id"), @NamedQuery(name = "MimeType.findByNombre", query = "SELECT m FROM MimeType m WHERE m.nombre = :nombre"), @NamedQuery(name = "MimeType.findByExtensiones", query = "SELECT m FROM MimeType m WHERE m.extensiones = :extensiones")})
public class MimeType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(nullable = false)
    private Integer id;
    @Basic(optional = false) @NotNull @Size(min = 1, max = 2147483647) @Column(nullable = false, length = 2147483647)
    private String nombre;
    @Size(max = 2147483647) @Column(length = 2147483647)
    private String extensiones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mimeTypeId")
    private List<Imagen> imagenList;

    public MimeType() {
    }

    public MimeType(Integer id) {
        this.id = id;
    }

    public MimeType(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public String getExtensiones() {
        return extensiones;
    }

    public void setExtensiones(String extensiones) {
        this.extensiones = extensiones;
    }

    @XmlTransient
    public List<Imagen> getImagenList() {
        return imagenList;
    }

    public void setImagenList(List<Imagen> imagenList) {
        this.imagenList = imagenList;
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
        if (!(object instanceof MimeType)) {
            return false;
        }
        MimeType other = (MimeType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MimeType[ id=" + id + " ]";
    }

}
