package de.stefanmarinkovic.kisten2.entity;

/**
 *
 * @author stefan
 */
public class InhaltWrapper {
    
    private Long id;
    private String inhaltName;
    private Long kistenNummer;
    private float fuellgrad;
    private String standort;
    private String kategorieName;
    private String eigentuemerName;
    
    public InhaltWrapper(Inhalt inhalt) {
        this.id = inhalt.getId();
        this.inhaltName = inhalt.getInhalt();
        this.kistenNummer = inhalt.getKiste().getNummer();
        this.fuellgrad = inhalt.getKiste().getFuellgrad();
        this.standort = inhalt.getKiste().getStandort();  
        this.kategorieName = inhalt.getKategorie().getName();
        this.eigentuemerName = inhalt.getEigentuemer().getName();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public String getInhaltName() {
        return inhaltName;
    }

    public void setInhaltName(String inhaltName) {
        this.inhaltName = inhaltName;
    }

    public Long getKistenNummer() {
        return kistenNummer;
    }

    public void setKistenNummer(Long kistenNummer) {
        this.kistenNummer = kistenNummer;
    }

    public float getFuellgrad() {
        return fuellgrad;
    }

    public void setFuellgrad(float fuellgrad) {
        this.fuellgrad = fuellgrad;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public String getKategorieName() {
        return kategorieName;
    }

    public void setKategorieName(String kategorieName) {
        this.kategorieName = kategorieName;
    }

    public String getEigentuemerName() {
        return eigentuemerName;
    }

    public void setEigentuemerName(String eigentuemerName) {
        this.eigentuemerName = eigentuemerName;
    }

    @Override
    public String toString() {
        return "InhaltWrapper{" + "id=" + id + 
                ", inhaltName=" + inhaltName + 
                ", kistenNummer=" + kistenNummer + 
                ", fuellgrad=" + fuellgrad + 
                ", standort=" + standort + 
                ", kategorieName=" + kategorieName + 
                ", eigentuemerName=" + eigentuemerName +  '}';
    }

 
    
    
}
