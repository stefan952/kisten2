/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.entity;

import jakarta.persistence.CascadeType;
import static jakarta.persistence.CascadeType.ALL;
import jakarta.persistence.Column;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author stefan
 */
@Entity
@Table
@NamedQuery(
        name = "findAllKisten",
        query = "SELECT ki FROM Kiste ki "
        + "ORDER BY ki.nummer"
)
public class Kiste implements Serializable {

    private static final long serialVersionUID = 12342422519825L;

    @Id
    private Long nummer;

    //@Column(nullable = false)
    //@NotEmpty
    private String standort;

    //@Column(nullable = false)
    private float fuellgrad;

    private List<Inhalt> inhalte = new ArrayList<>();

    @OneToMany(mappedBy = "kiste")
    public List<Inhalt> getInhalte() {
        return inhalte;
    }

    public void setInhalte(List<Inhalt> inhalte) {
        this.inhalte = inhalte;
    }
    

    public Kiste() {
    }

    public Kiste(Long nummer, String standort, float fuellgrad) {
        this.nummer = nummer;
        this.standort = standort;
        this.fuellgrad = fuellgrad;
    }

    public Long getNummer() {
        return nummer;
    }

    public void setNummer(Long nummer) {
        this.nummer = nummer;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public float getFuellgrad() {
        return fuellgrad;
    }

    public void setFuellgrad(float fuellgrad) {
        this.fuellgrad = fuellgrad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.nummer);
        return hash;
    }

    /**
     * zwei Kisten sind gleich, wenn sie die gleiche Kistennummer haben
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
        final Kiste other = (Kiste) obj;
        if (!Objects.equals(this.nummer, other.nummer)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Kiste{" + "nummer=" + nummer + ", standort=" + standort + ", fuellgrad=" + fuellgrad + ", inhalte=" + inhalte.size() + '}';
    }



}
