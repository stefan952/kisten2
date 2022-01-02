/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.entity;

import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;

/**
 *
 * @author stefan
 */
@Entity
@NamedQuery(
        name = "findAllCategories",
        query = "SELECT c FROM Kategorie c "
        + "ORDER BY c.name"
)
public class Kategorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private List<Inhalt> inhalte = new ArrayList<>();

    public Kategorie() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "kategorie")
    public List<Inhalt> getInhalte() {
        return inhalte;
    }

    public void setInhalte(List<Inhalt> inhalte) {
        this.inhalte = inhalte;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.name.toLowerCase());
        return hash;
    }

    /**
     * zwei Kategorien sind gleich bei gleichem Namen (ignoreCase)
     * @param obj
     * @return 
     */ 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kategorie other = (Kategorie) obj;
        if (!this.name.equalsIgnoreCase(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Kategorie{" + "id=" + id + ", name=" + name + ", inhalte=" + inhalte.size() + '}';
    }



}
