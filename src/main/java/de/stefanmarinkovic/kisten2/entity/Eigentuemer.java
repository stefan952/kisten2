/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@NamedQuery(
        name = "findAllEigentuemer",
        query = "SELECT e FROM Eigentuemer e "
        + "ORDER BY e.name"
)
public class Eigentuemer /*extends NamedEntity*/ implements Serializable, Comparable {

    private static final long serialVersionUID = 189356742387621L;

    private static final Logger logger = Logger.getLogger("kisten2.entity.Eigentuemer");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String name;
    private List<Inhalt> inhalte = new ArrayList<>();

    public Eigentuemer() {
    }

    public Eigentuemer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "eigentuemer")
    public List<Inhalt> getInhalte() {
        return inhalte;
    }

    public boolean addInhalt(Inhalt inhalt) {
        if (inhalte.contains(inhalt)) {
            return false;
        }
        return inhalte.add(inhalt);
    }

    public void setInhalte(List<Inhalt> inhalte) {
        this.inhalte = inhalte;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.name.toLowerCase());
        return hash;
    }

    /**
     * zwei Eigentümer sind gleich bei gleichem Namen (ignoreCase)
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
        final Eigentuemer other = (Eigentuemer) obj;
        if (!this.name.equalsIgnoreCase(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Eigentuemer{" + "id=" + id + ", name=" + name + ", inhalte=" + inhalte.size() + '}';
    }

    

    @Override
    public int compareTo(Object o) {
        logger.log(Level.INFO, "Eigentuemer.compareTo()");
        if (null == o) {
            logger.log(Level.INFO, "Eigentuemer.compareTo(): o == null");
        }
        if (null == ((Eigentuemer) o).getName()) {
            logger.log(Level.INFO, "Eigentuemer.compareTo(): eigentumer.getName() == null");
        }
        if (null == ((Eigentuemer) o).getId()) {
            logger.log(Level.INFO, "Eigentuemer.compareTo(): eigentumer.getId() == null");
        }
        return this.getId().compareTo(((Eigentuemer) o).getId());
    }

}
