package entities;

import java.util.Date;

public class Livre {
    private  int id ;
    private String titre;
    private String auteur;
    private Date datePub;
    private String isbn;
    private float prix;
    private String genre;
    private String disponible;

    public Livre() {
    }

    @Override
    public String toString() {
        return "Livre " +
                "\nAuteur : '" + getAuteur() + "'," +
                "\nTitle: '" + getTitre() + "'," +
                "\nDate de Publication : '" + getDatePub() + "'," +
                "\nISBN : '" + getIsbn() + "'," +
                "\nPrix : '" + getPrix() + "'," +
                "\nGenre : '" + getGenre() + "'" +
                "\nDisponiblite : '" + getDisponible() + "'" ;
    }

    public int getId() {
        return id;
    }

    public Livre(int id, String titre, String auteur, Date datePub, String isbn, float prix, String genre, String disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.datePub = datePub;
        this.isbn = isbn;
        this.prix = prix;
        this.genre = genre;
        this.disponible = disponible;
    }

    public Livre(String titre, String auteur, String isbn, Date datePub, float prix, String genre, String disponible) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.datePub = datePub;
        this.prix = prix;
        this.genre = genre;
        this.disponible = disponible;
    }



    public void setId(int id) {
        this.id = id;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDatePub() {
        return datePub;
    }

    public void setDatePub(Date datePub) {
        this.datePub = datePub;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }
}
