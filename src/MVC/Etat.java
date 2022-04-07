package MVC;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import javax.swing.JOptionPane;

import Batiments.Caserne;
import Batiments.Fourmiliere;
import Environnement.Carte;
import Environnement.Ressource;
import Environnement.typeRessource;
import Joueurs.AIPlayer;
import Joueurs.Joueur;
import Unites.Combattante;
import Unites.CombattanteAI;
import Unites.Ouvrier;
import Unites.Unite;

public class Etat {
	private Carte carte = new Carte();
	private ArrayList<Joueur> joueurs;
	private Affichage aff;
	private int nbJoueurs = 2;
	private AIPlayer ordi /*= new Joueurs.AIPlayer(this) */;
	private ArrayList<Ressource> listRessource = new ArrayList<>();
	private Timer timer = new Timer();
	private int tempspassee = 0;
	
	public Point posInitial = null;
	public Point posfinal = null;
	
	public Etat(Affichage a) {
		aff = a;
		joueurs = new ArrayList<Joueur>();
		Joueur j1 = new Joueur();
		joueurs.add(j1);
		
		ordi = new AIPlayer(this);
		
		initCarte();
		initRessources();
	}

	public ArrayList<Joueur> getJoueurs() {
		return joueurs;
	}

	public void initCarte() {
		for(Joueur j : joueurs) {
			carte.getListeUnite().add(j.getUnites());
		}
	}

	public Carte getCarte() {
		return carte;
	}
	
	public boolean verifBorne(Point p) {
	   return p.x <= carte.getLongueur()-1 && p.x > 0 &&
			   p.y <= carte.getLargeur()-1 && p.y > 0;

	}

	public void initRessources() {
		Random rand = new Random();
		int nbRessources = rand.nextInt(40) + 20;
		while(nbRessources != 0) {
			boolean tempB = true;
			Ressource temp = new Ressource();
			if(!this.listRessource.isEmpty()) {
				for(Ressource res : this.listRessource)
				{
					if(temp.getPosition().x == res.getPosition().x && temp.getPosition().y == res.getPosition().y)
					{
						tempB = false;
						break;
					}
				}
			}
			if(tempB)
			{
				this.listRessource.add(temp);

				nbRessources--;
			}

		}
	}
	
	public void threadRessource()
	{
		new Thread(() ->
		{
			while(true)
			{
				boolean tempB = true;
				Ressource r = new Ressource();
				if(this.listRessource.size() < 60)
				{
					for (Ressource res : this.listRessource)
					{
						if (r.getPosition().x == res.getPosition().x && r.getPosition().y == res.getPosition().y)
						{
							tempB = false;
							break;
						}
					}
					if(tempB)
					{
						this.listRessource.add(r); // ajoute de la nouvelle ressource a la liste de ressources.
						this.aff.getPlateau()[r.getPosition().x][r.getPosition().y].setRessource(r); // ajout de la nouvelles ressource au plateau de jeu.
						this.aff.refreshReesources(); // appel pour actualiser l'affichage graphique.
					}
				}
				try
				{
					Thread.sleep(2500);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void threadUnit() {
		new Thread(() -> {
			while(true) {
				for(Case[] tabCase : this.aff.getPlateau()) {
					for(Case c : tabCase) {
						if (c.estOccupeUnit()) {
							c.removeUnit();
						}
						if (c.estOccupeeCombattante()) {
							c.removeCombattante();
						}
						if (c.estOccupeeCombattanteAI()) {
							c.removeCombattanteAI();
						}
					}
				}
				for(Joueur j : joueurs) {
					for(Unite u : j.getUnites()) {
						Case c = this.aff.getPlateau()[u.getPos().x][u.getPos().y];
						if (u instanceof Combattante) {
							c.setCombattante((Combattante) u);
						}
						else {
							c.setUnit(u);
						
						    if(c.estOccupeeRessource()) { // Je regarde si la case contient une ressource si c'est le cas alors je l'enleve et augmente le score du joueur
						    	Ressource r = c.removeRessource();
						    	if (r.gettR() == typeRessource.bois) {
						    		j.setNbBois(1);
						    		System.out.println("nombre de bois : " + j.getNbBois());
						    		}
						    	else {
						    		j.setNbNourritures(1);
						    		System.out.println("nombre de nourriture : " + j.getNbNourritures());
						    		}
						    	} 
					//	this.aff.refreshUnit();
						    }
						}
				}
				
				
				for(CombattanteAI u : ordi.getUnit()) {
					Case c = this.aff.getPlateau()[u.getPos().x][u.getPos().y];
					c.setCombattanteAI(u);
				}
				
				
				//Verification de fin partie
				if(joueurs.get(0).getNbNourritures() >= 110) {
					win();
				}
				else if(joueurs.get(0).getNbNourritures() < 10 && (joueurs.get(0).getNbBois() < 20 && joueurs.get(0).getUnites().size() == 0)) {
					lose();
				}

				try {
					Thread.sleep(1000);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	
	public void threadAttaqueJoueur(){
		new Thread(() -> {
			while(true){
				ArrayList<Unite> listUniteJ = this.joueurs.get(0).getUnites();
				ArrayList<CombattanteAI> listUniteE = this.ordi.getUnit();
				ArrayList<Point> temp = new ArrayList<Point>();
				for(Unite uJ : listUniteJ){
					for(Unite uAI : listUniteE){
						if(uJ instanceof Combattante){
							Point p1 = uJ.getPos();
							Point p2 = uAI.getPos();
							int xValide = Math.abs(p1.x - p2.x);
							int yValide = Math.abs(p1.y - p2.y);
							if(xValide <= 1 && yValide <= 1){
								uAI.setVie(uAI.getVie()-((Combattante) uJ).getAttack());
								System.out.println(uAI.getVie());
								if(uAI.getVie() <= 0){
									temp.add(uAI.getPos());
								}
							}
						}
					}
				}
				for(Point p : temp){
					this.getAI().getUnit().remove(this.aff.getPlateau()[p.x][p.y].getCombattanteAI());
					this.aff.getPlateau()[p.x][p.y].removeCombattanteAI();
				}
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	
	
	
	public void threadAttaqueAI(){
		new Thread(() -> {
			while(true){
				ArrayList<Unite> listUniteJ = this.joueurs.get(0).getUnites();
				ArrayList<CombattanteAI> listUniteE = this.ordi.getUnit();
				ArrayList<Point> temp = new ArrayList<Point>();

				for(Unite uAI : listUniteE){
					for(Unite uJ : listUniteJ){
						if(uJ instanceof Combattante){
							Point p1 = uJ.getPos();
							Point p2 = uAI.getPos();
							int xValide = Math.abs(p1.x - p2.x);
							int yValide = Math.abs(p1.y - p2.y);
							if(xValide <= 1 && yValide <= 1){
								uJ.setVie(uJ.getVie() - ((CombattanteAI) uAI).getAttack());
								//System.out.println(uJ.getVie());
								if(uJ.getVie() <= 0) {
									temp.add(uJ.getPos());
								}
							}
						}
						else if(uJ instanceof Ouvrier){
							Point p1 = uJ.getPos();
							Point p2 = uAI.getPos();
							int xValide = Math.abs(p1.x - p2.x);
							int yValide = Math.abs(p1.y - p2.y);
							if(xValide <= 1 && yValide <= 1){
								uJ.setVie(uJ.getVie() - ((CombattanteAI) uAI).getAttack());
						//		System.out.println(uJ.getVie());
								if(uJ.getVie() <= 0) {
									temp.add(uJ.getPos());
								}
							}
						}
					}
				}
				for(Point p : temp){
					Case c = this.aff.getPlateau()[p.x][p.y];
					if(c.estOccupeUnit()){
						this.getJoueurs().get(0).getUnites().remove(this.aff.getPlateau()[p.x][p.y].getUnit());
						this.aff.getPlateau()[p.x][p.y].removeUnit();
					}
					else {
						this.getJoueurs().get(0).getUnites().remove(this.aff.getPlateau()[p.x][p.y].getCombattante());
						this.aff.getPlateau()[p.x][p.y].removeCombattante();
					}
				}
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public ArrayList<Ressource> getListRessource()
	{
		return this.listRessource;
	}

	public AIPlayer getAI() {
		return ordi;
	}
	
	public Affichage getAff() {
		return aff;
	}
	
	public void unitADeplacer() {
		Case c = this.getAff().getPlateau()[posInitial.x][posInitial.y];
		Unite u = null;
		if(c.estOccupeUnit()) {
			u = c.getUnit();
		}
		else if(c.estOccupeeCombattante())
			u = c.getCombattante();
		u.setPosFinal(posfinal);
		if(!u.isAlive()) {
			u.start();
		}
		posInitial = posfinal;
	}
	
	
	public void setCombattantePlateau(Combattante c){
		this.joueurs.get(0).addUnite(c);
		this.aff.getPlateau()[c.getPos().x][c.getPos().y].setCombattante(c);
	}
	
	public void setCombattanteAIPlateau(CombattanteAI c) {
		this.aff.getPlateau()[c.getPos().x][c.getPos().y].setCombattanteAI(c);
	}
	
	public void setFourmilierePlateau(Fourmiliere f){
		this.joueurs.get(0).addBat(f);
		this.aff.getPlateau()[f.getPosition().x][f.getPosition().y].setFourmiliere(f);
	}
	
	public void setCasernePlateau(Caserne c){
		this.joueurs.get(0).addBat(c);
		this.aff.getPlateau()[c.getPosition().x][c.getPosition().y].setCaserne(c);
	}
	
	
	public void win() {
		JOptionPane fin = new JOptionPane();
		String s = "Vous avez assez de rations pour l'hiver pour votre fourmiliere, BRAVO !";
		fin.showConfirmDialog(aff, s, "Victoire!", JOptionPane.DEFAULT_OPTION);
	}
	
	public void lose() {
		JOptionPane fin = new JOptionPane();
		String s = "Vous avez perdu !";
		fin.showConfirmDialog(aff, s, "Defaite !", JOptionPane.DEFAULT_OPTION);
	}
	
	

}