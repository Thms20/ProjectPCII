package MVC;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import Unites.Unite;

public class ZoneCliquable extends Controle {

    private boolean occupee = false;

    public ZoneCliquable(Etat e, int x, int y) {
        super(e, x, y);
    }

    // Permet de tester si une case est occupée.
    public boolean estOccupee() { return this.occupee; }

   /* public void poseElement(){
        this.occupee = true;
    }

    public void enleveElement(){
        this.occupee = false;
    } */

   
    /**
     * Interfaçage entre la bibliothèque standard et les méthodes [clicGauche]
     * et [clicDroit].
     */
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            this.clicDroit();
        } /* else { 
            this.clicGauche();
        } */
    }

    
    public void clicDroit() {
    	Unite unit = super.getEtat().getJoueurs().get(0).getUnites().get(0);
    	super.getEtat().getAff().getPlateau()[unit.getPos().x][unit.getPos().y].repaint();
    	super.getEtat().getJoueurs().get(0).getUnites().get(0).seDeplacer(new Point(1, 14));
    }
}
