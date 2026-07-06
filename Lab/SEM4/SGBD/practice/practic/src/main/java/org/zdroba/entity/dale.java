package org.zdroba.entity;

import jakarta.persistence.*;

@Entity
@Table(name="daleBeton")
public class dale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tip;

    private String firma;
    private Integer grosime;
    private Integer pret;

    public dale() {
    }

    public dale(Long id, String tip, String firma, Integer grosime, Integer pret) {
        this.id = id;
        this.tip = tip;
        this.firma = firma;
        this.grosime = grosime;
        this.pret = pret;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public Integer getGrosime() {
        return grosime;
    }

    public void setGrosime(Integer grosime) {
        this.grosime = grosime;
    }

    public Integer getPret() {
        return pret;
    }

    public void setPret(Integer pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "dale{" +
                "id=" + id +
                ", tip='" + tip + '\'' +
                ", firma='" + firma + '\'' +
                ", grosime=" + grosime +
                ", pret=" + pret +
                '}';
    }
}
