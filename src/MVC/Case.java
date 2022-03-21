package MVC;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import Environnement.Ressource;
import Environnement.typeRessource;
import Unites.Ouvrier;
import Unites.Unite;

public class Case extends ZoneCliquable {
    private Ressource ressource = null;
    private boolean occupeeRessource = false;
    
    private Unite u = null;
    private boolean occupeUnite = false;

    // Constructeur
    public Case(Etat e) {
        // Initialisation d'une case cliquable, de dimensions 40*40 pixels.
        super(e,40, 40);
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
    public boolean estOccupee() { return this.occupeeRessource; }

    /**
     * Methode pour effectuer l'affichage graphique des ressources.
     */
    public void drawRessource(Graphics g) {
        try {
            Image imageMiel = ImageIO.read(new File("/home/tp-home001/fd538609-ba12-4085-9f9e-417808d85a26/git/Foumizz/src/Ressources/miel.png"));
            Image imageBois = ImageIO.read(new File("/home/tp-home001/fd538609-ba12-4085-9f9e-417808d85a26/git/Foumizz/src/Ressources/Ressource.png"));

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
    
    
    public void drawUnit(Graphics g) {
    	try {
            Image imageUnit = ImageIO.read(new File("/home/tp-home001/fd538609-ba12-4085-9f9e-417808d85a26/git/Foumizz/src/Ressources/ouvrier.jpeg"));
            if(u instanceof Ouvrier) {
            	g.drawImage(imageUnit, 0 , 0, 192/5, 165/4, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}