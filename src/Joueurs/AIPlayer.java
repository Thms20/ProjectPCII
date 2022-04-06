package Joueurs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import MVC.Etat;
import Unites.CombattanteAI;
import Unites.Unite;

public class AIPlayer extends Thread{
	ArrayList<CombattanteAI> list = new ArrayList<CombattanteAI>();
	private Etat etat;
	
	public AIPlayer(Etat e) {
		etat = e;
	}
	
	/*
	@Override
	public void  run() {
		while(true) {
			for(Unite fourmi : list) {
				int random;
				
				Point p = new Point(0,0);
				
				while(!control.getEtat().verifBorne(p)) { // depasse d'une case par rapport � a borne 10, � faire
                    random = (new Random()).nextInt(100);
					if(random < 50) {
						if(random < 25) p = new Point(fourmi.getPos().x - 1, fourmi.getPos().y - 1);
						else  p = new Point(fourmi.getPos().x - 1, fourmi.getPos().y + 1);
					}
					else {
						if(random < 75)  p = new Point(fourmi.getPos().x + 1, fourmi.getPos().y - 1);
						else p = new Point(fourmi.getPos().x + 1, fourmi.getPos().y + 1);
					}
				}
				fourmi.seDeplacer(p); 
				//System.out.println(p.x + " " + p.y);
			}
            control.getAff().setAIList(list);
			control.getEtat().move();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	@Override
	public void  run() {
		while(true) {
			ArrayList<Unite> unitesJoueur = etat.getJoueurs().get(0).getUnites();
			Random rand = new Random();
			int numUnitAChasser;
			
			for(CombattanteAI u : list) {	
				if(u.getEnemy() == -1) {
					numUnitAChasser  = rand.nextInt(unitesJoueur.size());
					u.setEnemy(numUnitAChasser,  etat.getJoueurs().get(0).getUnites().get(numUnitAChasser).getPos());
				}
				else {
					if(u.getPosFinal().x == u.getPos().x && u.getPosFinal().y == u.getPos().y) {
						u.setPosFinal(etat.getJoueurs().get(0).getUnites().get(u.getEnemy()).getPos());
					}
				}
				
			}
			
			// Je rajoute des combattanteAI si le nombre d'unite du joueur est trop grande
			if(unitesJoueur.size()/2 > list.size()) {
				CombattanteAI c = new CombattanteAI(new Point(0, 14));
				list.add(c);
				c.start();
				
			}
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public  ArrayList<CombattanteAI> getUnit() {
		return list;
	}

}
