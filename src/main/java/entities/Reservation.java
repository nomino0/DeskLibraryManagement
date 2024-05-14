package entities;

import java.util.Date;

public class Reservation {
    private int id ;
    private Livre livre;
    private Date dateDebut;
    private Date dateFin;
    private String nomClient;
    private String cinClient;
    private boolean archived;



    public Reservation(int id ,Livre livre, Date dateDebut, Date dateFin, String nomClient, String cinClient) {
        this.id = id ;
        this.livre = livre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nomClient = nomClient;
        this.cinClient = cinClient;
        this.archived = archived;
    }

    public Reservation(Livre livre, Date dateDebut, Date dateFin, String nomClient, String cinClient) {
        this.livre = livre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nomClient = nomClient;
        this.cinClient = cinClient;
        this.archived = false;
    }



    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }




    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }



    public String getCinClient() {
        return cinClient;
    }

    public void setCinClient(String cinClient) {
        this.cinClient = cinClient;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", livre=" + livre +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", nomClient='" + nomClient + '\'' +
                ", cinClient='" + cinClient + '\'' +
                '}';
    }
}
