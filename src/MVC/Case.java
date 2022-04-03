package MVC;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import Environnement.Ressource;
import Environnement.typeRessource;
import Unites.Ouvrier;
import Unites.Unite;

public class Case extends ZoneCliquable {
	private  Point posInGrid;
    private Ressource ressource = null;
    private boolean occupeeRessource = false;
    
    private Unite u = null;
    public boolean occupeUnite = false;

    // Constructeur
    public Case(Etat e, Point p) {
        // Initialisation d'une case cliquable, de dimensions 40*40 pixels.
        super(e,40, 40);
        posInGrid = p;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Border blackline = BorderFactory.createLineBorder(Color.gray);
        this.setBorder(blackline);

        //affichage graphique des ressources
        if(this.occupeeRessource) {
        	drawRessource(g);
        }
        if(this.occupeUnite) {
        	drawUnit(g);
        }
    }
    // Permet de tester si une case est occupée.
    public boolean estOccupeeRessource() { return this.occupeeRessource; }

    /**
     * Methode pour effectuer l'affichage graphique des ressources.
     */
    public void drawRessource(Graphics g) {
        try {
            Image imageMiel = ImageIO.read(new File("C:\\Users\\Thomas\\Desktop\\pcii\\ProjectPCII\\src\\Ressources\\miel.png"));
            Image imageBois = ImageIO.read(new File("C:\\Users\\Thomas\\Desktop\\pcii\\ProjectPCII\\src\\Ressources\\Ressource.png"));

            if (this.ressource.gettR() == typeRessource.bois)
                g.drawImage(imageBois, 0 , 0, 474/11, 288/8, this);
            else
                g.drawImage(imageMiel, 0 , 0, 839/22, 847/22, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRessource(Ressource r)
    {
        this.ressource = r;
        this.occupeeRessource = true;
    }
    
    
    public boolean estOccupeUnit() {
    	return this.occupeUnite;
    }
    
    public void setUnit(Unite unit) {
    	u = unit;
    	this.occupeUnite = true;
    }
    
    public void removeUnit() {
    	u = null;
    	this.occupeUnite = false;
    }
    
    public Unite getUnit() {
    	return u;
    }
    
    public Point getPosInGrid() {
    	return posInGrid;
    }
    
    
    public void drawUnit(Graphics g) {
    	try {
            Image imageUnit = ImageIO.read(new File("C:\\Users\\Thomas\\Desktop\\pcii\\ProjectPCII\\src\\Ressources\\ouvrier.jpeg"));
            if(u instanceof Ouvrier) {
            	g.drawImage(imageUnit, 0 , 0, 192/5, 165/4, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
        	clicDroit(e);
        }
        else {
            clicGauche(e);
        }
    }
    
    public void clicDroit(MouseEvent e) {
    	super.getEtat().posInitial = posInGrid;
    	System.out.println(posInGrid);
    	
    }
    
    public void clicGauche(MouseEvent e) {
    	super.getEtat().posfinal = posInGrid;
    	super.getEtat().unitADeplacer();
    }

}