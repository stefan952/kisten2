/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.stefanmarinkovic.kisten2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author stefan
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "findAllInhalteByKistenNummer",
            query = "SELECT inh FROM Inhalt inh "
            + "ORDER BY inh.kiste, inh.inhalt"),
    @NamedQuery(
            name = "findAllInhalteByInhalt",
            query = "SELECT inh FROM Inhalt inh "
            + "ORDER BY inh.inhalt"),
    @NamedQuery(
            name = "findAllInhalteByEigentuemer",
            query = "SELECT inh FROM Inhalt inh "
            + "ORDER BY inh.eigentuemer, inh.kiste"),
    @NamedQuery(
            name = "findAllInhalteByFuellgrad",
            query = "SELECT inh FROM Inhalt inh "
            + "ORDER BY inh.kiste.fuellgrad ASC, inh.kiste"),
    @NamedQuery(
            name = "findAllInhalteByKategorie",
            query = "SELECT inh FROM Inhalt inh "
            + "ORDER BY inh.kategorie.name ASC, inh.kiste"),
    @NamedQuery(
            name = "findAllInhalteByStandort",
            query = "SELECT inh FROM Inhalt inh "
            + "ORDER BY inh.kiste.standort ASC, inh.kiste, inh.inhalt")
})
public class Inhalt implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private Eigentuemer eigentuemer;
    private Kategorie kategorie;
    private Kiste kiste;
    private String inhalt;

    public Inhalt() {
        //eigentuemer = new Eigentuemer();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Eigentuemer getEigentuemer() {
        return eigentuemer;
    }

    public void setEigentuemer(Eigentuemer eigentuemer) {
        this.eigentuemer = eigentuemer;
    }

    @ManyToOne
    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    @ManyToOne
    public Kiste getKiste() {
        return kiste;
    }

    public void setKiste(Kiste kiste) {
        this.kiste = kiste;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.eigentuemer);
        hash = 47 * hash + Objects.hashCode(this.kategorie);
        hash = 47 * hash + Objects.hashCode(this.inhalt.toLowerCase());
        return hash;
    }

    /**
     * Inhalte sind gleich bei gleichem Inhalt (ignoreCase), gleichem Eigentümer und gleicher Kategorie.
     * Die Kiste hat mit der Gleichheit nichts zu tun.
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
        final Inhalt other = (Inhalt) obj;
        if (!this.inhalt.equalsIgnoreCase(other.inhalt)) {
            return false;
        }
        if (!Objects.equals(this.eigentuemer, other.eigentuemer)) {
            return false;
        }
        if (!Objects.equals(this.kategorie, other.kategorie)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Inhalt{" + "id=" + id + ", eigentuemer=" + eigentuemer + ", kategorie=" + kategorie + ", kiste=" + kiste + ", inhalt=" + inhalt + '}';
    }

}
