package Unites;

import java.awt.Point;

public class Ouvrier extends Unite {
	private Point position;
	private Point posFinal = null;
	
	private int vie = 100;
    
	public Ouvrier() {
	}
	
	public Ouvrier(Point pos) {
		position = pos;
	}
	
	@Override
	public void seDeplacer(Point d) {
		position = d;
	}

	@Override
	public Point getPos() {
		return position;
	}
	
	
	@Override
	public void run() {
		while(/*posFinal != null */ true) {
	//		if(posFinal != null) {
				if(posFinal.x > position.x && posFinal.y > position.y) {
					position = new Point(position.x + 1, position.y +1);
				}
				else if(posFinal.x > position.x && posFinal.y < position.y) {
					position =  new Point(position.x + 1, position.y - 1);
				}
				else if(posFinal.x < position.x && posFinal.y > position.y) {
					position =  new Point(position.x - 1, position.y  + 1);
				}
				else if(posFinal.x < position.x && posFinal.y < position.y) {
					position =  new Point(position.x - 1, position.y - 1);
				}
				else if(posFinal.x < position.x && posFinal.y == position.y) {
					position =  new Point(position.x - 1, position.y);
				}
				else if(posFinal.x > position.x && posFinal.y == position.y) {
					position =  new Point(position.x + 1, position.y);
				}
				else if(posFinal.x == position.x && posFinal.y > position.y) {
					position =  new Point(position.x, position.y + 1);
				}
				else if(posFinal.x == position.x && posFinal.y < position.y) {
					position =  new Point(position.x, position.y - 1);
				}
				
			try {
				Thread.sleep(2000);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public void setPosFinal(Point p) {
		posFinal = p;
	}
	
	@Override
	public void setVie(int v) {
		this.vie = v;
	}
	
    @Override
	public int getVie() {
		return this.vie;
	}

}