package MVC;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import Batiments.Caserne;
import Batiments.Fourmiliere;
import Environnement.Ressource;
import Unites.Combattante;
import Unites.Unite;

/**
 * @author Thomas, Arsene, Charlies
 * 
 * La classe Affichage correspond à la Vue dans le modele MVC. Dans cette classe on cree graphiquement notre plateau de jeu avec
 * les elements dessus.  
 *
 */
public class Affichage extends Grille {
	private Etat etat = new Etat(this);

	// Attributs : taille et tableau de cases
	private Case[][] plateau;

	public Affichage(int taille) {
		super(taille, taille);
		this.plateau = new Case[taille][taille];

		for (int x = 0; x < plateau.length; x++) {
			for (int y = 0; y < plateau[x].length; y++) {
				this.plateau[x][y] = new Case(etat, new Point(x, y)); // A chaque case on lui donne acces a l'Etat et on lui envoie sa position
				ajouteElement(this.plateau[x][y]);
			}
		}
		this.setAllRessources();
	    setAllUnit();
		this.setBackground(Color.orange);
		
		//Ici on demarre tous les Threads definis dans l'Etat et on place les batiments et une combattante sur le plateau de depart
		this.etat.threadUnit();
		this.etat.threadRessource();
		this.etat.setCombattantePlateau(new Combattante(new Point(12, 2)));
		this.etat.setFourmilierePlateau(new Fourmiliere(new Point(14, 1)));
		this.etat.setCasernePlateau(new Caserne(new Point(13, 0)));
		
		this.etat.getAI().start();

		this.etat.threadAttaqueJoueur();
		this.etat.threadAttaqueAI();
	}

	/**
	 * Methode pour initialise toute les ressources dans chaque case en fonction des coordonnees de chaque ressources.
	 */
	public void setAllRessources() {
		for (Ressource r : this.etat.getListRessource()) {
			this.plateau[r.getPosition().x][r.getPosition().y].setRessource(r);
		}
	}
	
	

	/**
	 * Methode pour actualiser l'affichage graphique.
	 */
	public void refreshReesources()
	{
		for(Case[] tabCase : this.plateau)
		{
			for(Case c : tabCase)
			{
				// repaint seulement les cases ou il y'a une ressource.
				if(c.estOccupeeRessource())
					this.revalidate();
					c.repaint();
			}
		}
	}
	
	/**
	 * Permet d'afficher sur le plateau toutes les unites du joueur 
	 */
	public void setAllUnit() {
		for(Unite u : etat.getJoueur().getUnites()) {
			this.plateau[u.getPos().x][u.getPos().y].setUnit(u);
			
		}
	}
	
	/**
	 * Permet de repaint une case qui contient une unite 
	 */
	public void refreshUnit() {
		for(Case[] tabCase : this.plateau) {
			for(Case c : tabCase) {
				if(c.estOccupeUnit()) {
					c.repaint();
				}
			}
		}
	}
	
    /**
     * @return le plateau de jeu
     */
	public Case[][] getPlateau() {
		return plateau;
	}

}