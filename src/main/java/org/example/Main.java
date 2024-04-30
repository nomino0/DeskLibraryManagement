package org.example;

import crud.LivreCrud;
import entities.Livre;

import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        LivreCrud C = new LivreCrud();
      Livre livre = new Livre("Title","Si ali","123",new Date(),12,"AAA","OUI");
      C.ajouterLivre(livre);

        }

}