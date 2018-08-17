package fr.orsay.lri.varna.models.rna.pseudoknots;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import fr.orsay.lri.varna.applications.templateEditor.Couple;

public class Trombone {
	private static final double SPACE_BETWEEN_BASES = 50;
	
	private Point2D.Double first_point;
	private Point2D.Double last_point;
	private int nb_base;
	private int first_index;
	private boolean even_strand;
	private ArrayList<Couple<Integer,Point2D.Double>> points;
	private ArrayList<Couple<Integer,Point2D.Double>> centers;

	public Trombone() {
		this.first_point = new Point2D.Double();
		this.last_point = new Point2D.Double();
		this.nb_base = 0;
		this.first_index = 0;
		this.even_strand = false;
		this.points = new ArrayList<Couple<Integer,Point2D.Double>>();
		this.centers = new ArrayList<Couple<Integer,Point2D.Double>>();		
	}	

	public Trombone(Double first_point, Double last_point, int nb_base, int first_index, boolean even_strand) {
		this.first_point = first_point;
		this.last_point = last_point;
		this.nb_base = nb_base;
		this.first_index = first_index;
		this.even_strand = even_strand;
		this.points = new ArrayList<Couple<Integer,Point2D.Double>>();
		this.centers = new ArrayList<Couple<Integer,Point2D.Double>>();	
	}


	public Point2D.Double getFirst_point() {
		return first_point;
	}

	public void setFirst_point(Point2D.Double first_point) {
		this.first_point = first_point;
	}

	public Point2D.Double getLast_point() {
		return last_point;
	}

	public void setLast_point(Point2D.Double last_point) {
		this.last_point = last_point;
	}

	public int getNb_base() {
		return nb_base;
	}

	public void setNb_base(int nb_base) {
		this.nb_base = nb_base;
	}

	public int getFirst_index() {
		return first_index;
	}

	public void setFirst_index(int first_index) {
		this.first_index = first_index;
	}
	
	public ArrayList<Couple<Integer, Point2D.Double>> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Couple<Integer, Point2D.Double>> points) {
		this.points = points;
	}

	public ArrayList<Couple<Integer, Point2D.Double>> getCenters() {
		return centers;
	}

	public void setCenters(ArrayList<Couple<Integer, Point2D.Double>> centers) {
		this.centers = centers;
	}

	public void assignPointsAndCentersCoords(){
		double trombone_radius = (this.last_point.getX()-this.first_point.getX())/2.0;
		double dy_first_last = Math.abs(this.last_point.getY() - this.first_point.getY());
		double trombone_length = (this.nb_base+1)*Trombone.SPACE_BETWEEN_BASES;
		double delta = 0;
		double pos = 0;
		int index = 0;
		boolean eval_length = false;
		Couple<Point2D.Double,Point2D.Double> c1 = new Couple<Point2D.Double,Point2D.Double>();
		Couple<Point2D.Double,Point2D.Double> c2 = new Couple<Point2D.Double,Point2D.Double>();
		Couple<Integer,Point2D.Double> point;
		Couple<Integer,Point2D.Double> center;
		while(!eval_length) {
			delta = (trombone_length - Math.PI*trombone_radius - dy_first_last)/2.0;
			pos = (1.0/(this.nb_base+1));
			c1 = this.findPointandCenter(trombone_length, delta, dy_first_last, trombone_radius, pos);
			pos = (1.0/(this.nb_base+1))*this.nb_base;
			c2 = this.findPointandCenter(trombone_length, delta, dy_first_last, trombone_radius, pos);
			boolean eval1 = c1.first.getX() == this.first_point.getX() && (Math.abs(c1.first.getY() - this.first_point.getY()) >= Trombone.SPACE_BETWEEN_BASES);
			boolean eval2 = c2.first.getX() == this.last_point.getX() && (Math.abs(c2.first.getY() - this.last_point.getY()) >= Trombone.SPACE_BETWEEN_BASES);
			if(eval1 && eval2){
				index = this.first_index + 1;
				point = new Couple<Integer,Point2D.Double>(index,c1.first);
				center = new Couple<Integer,Point2D.Double>(index,c1.second);
				this.points.add(point);
				this.centers.add(center);
				index = this.first_index + this.nb_base;
				point = new Couple<Integer,Point2D.Double>(index,c2.first);
				center = new Couple<Integer,Point2D.Double>(index,c2.second);
				this.points.add(point);
				this.centers.add(center);
				eval_length = true;
			}
			else {
				trombone_length += 5;
			}
		}
		for(int i = this.nb_base - 1; i > 1; i--){			
			pos = (1.0/(this.nb_base+1))*i;
			Couple<Point2D.Double,Point2D.Double> c = this.findPointandCenter(trombone_length, delta, dy_first_last, trombone_radius, pos);
			index = this.first_index + i;
			point = new Couple<Integer,Point2D.Double>(index,c.first);
			center = new Couple<Integer,Point2D.Double>(index,c.second);
			this.points.add(1,point);
			this.centers.add(1,center);
		}
	}
	/*public void AssignPointsandCentersCoords(){
		double trombone_length = (this.nb_base+1)*Trombone.SPACE_BETWEEN_BASES;
		double trombone_radius = (this.last_point.getX()-this.first_point.getX())/2.0;
		double dy_first_last = 0;
		if(this.first_point.getY() < this.last_point.getY()){
			dy_first_last = this.last_point.getY() - this.first_point.getY();
		}
		else {
			dy_first_last = this.first_point.getY() - this.last_point.getY();
		}
		double delta = (trombone_length - Math.PI*trombone_radius - dy_first_last)/2.0;
		for(int i = 1; i <= this.nb_base; i++){
			double pos = (1.0/(this.nb_base+1))*i;
			Couple<Point2D.Double,Point2D.Double> c = this.findPointandCenter(trombone_length, delta, dy_first_last, trombone_radius, pos);
			int index = this.first_index + i;
			Couple<Integer,Point2D.Double> point = new Couple<Integer,Point2D.Double>(index,c.first);
			Couple<Integer,Point2D.Double> center = new Couple<Integer,Point2D.Double>(index,c.second);
			this.points.add(point);
			this.centers.add(center);
		}
		for(Couple<Integer,Point2D.Double> c : points){
			//System.out.println("POINTS "+c.first+" "+c.second.getY());
		}
		//System.out.println("TESTA "+points.size());
	}*/

	private Couple<Point2D.Double,Point2D.Double> findPointandCenter(double trombone_length, double delta, double dy_first_last, double trombone_radius, double pos) {
		Point2D.Double point = new Point2D.Double();
		Point2D.Double center = new Point2D.Double();
		double p = trombone_length * pos;
		double x1,y1,x2,y2;
		if(this.even_strand){
			if(this.first_point.getY() < this.last_point.getY()){
				if(p < delta){
					x1 = this.first_point.getX();
					y1 = this.first_point.getY() - p;
					x2 = this.first_point.getX() + 5;
					y2 = this.first_point.getY() - p;
				}
				else if((p >= delta) && (p <= delta+Math.PI*trombone_radius)){
					double theta = (p - delta) / trombone_radius;
					x1 = this.first_point.getX() + trombone_radius - trombone_radius*Math.cos(theta);
					y1 = this.first_point.getY() - delta - trombone_radius*Math.sin(theta);
					x2 = this.first_point.getX() + trombone_radius;
					y2 = this.first_point.getY() - delta;
				}
				else {
					x1 = this.last_point.getX();
					y1 = this.last_point.getY() - (trombone_length - p);
					x2 = this.last_point.getX() - 5;
					y2 = this.last_point.getY() - (trombone_length - p);
				}
			}
			else {
				if(p < delta + dy_first_last){
					x1 = this.first_point.getX();
					y1 = this.first_point.getY() - p;
					x2 = this.first_point.getX() + 5;
					y2 = this.first_point.getY() - p;
				}
				else if((p >= delta + dy_first_last) && (p <= delta+dy_first_last+Math.PI*trombone_radius)){
					double theta = (p - (delta + dy_first_last)) / trombone_radius;
					x1 = this.first_point.getX() + trombone_radius - trombone_radius*Math.cos(theta);
					y1 = this.first_point.getY() - (delta + dy_first_last) - trombone_radius*Math.sin(theta);
					x2 = this.first_point.getX() + trombone_radius;
					y2 = this.first_point.getY() - (delta + dy_first_last);
				}
				else {
					x1 = this.last_point.getX();
					y1 = this.last_point.getY() - (trombone_length - p);
					x2 = this.last_point.getX() - 5;
					y2 = this.last_point.getY() - (trombone_length - p);
				}
			}
		}
		else {
			if(this.first_point.getY() < this.last_point.getY()){
				if(p < delta + dy_first_last){
					x1 = this.first_point.getX();
					y1 = this.first_point.getY() + p;
					x2 = this.first_point.getX() + 5;
					y2 = this.first_point.getY() + p;
				}
				else if((p >= delta + dy_first_last) && (p <= delta+dy_first_last+Math.PI*trombone_radius)){
					double theta = (p - (delta + dy_first_last)) / trombone_radius;
					x1 = this.first_point.getX() + trombone_radius - trombone_radius*Math.cos(theta);
					y1 = this.first_point.getY() + (delta + dy_first_last) + trombone_radius*Math.sin(theta);
					x2 = this.first_point.getX() + trombone_radius;
					y2 = this.first_point.getY() + (delta + dy_first_last);
				}
				else {
					x1 = this.last_point.getX();
					y1 = this.last_point.getY() + (trombone_length - p);
					x2 = this.last_point.getX() - 5;
					y2 = this.last_point.getY() + (trombone_length - p);
				}
			}
			else {
				if(p < delta){
					x1 = this.first_point.getX();
					y1 = this.first_point.getY() + p;
					x2 = this.first_point.getX() + 5;
					y2 = this.first_point.getY() + p;
				}
				else if((p >= delta) && (p <= delta+Math.PI*trombone_radius)){
					double theta = (p - delta) / trombone_radius;
					x1 = this.first_point.getX() + trombone_radius - trombone_radius*Math.cos(theta);
					y1 = this.first_point.getY() + delta + trombone_radius*Math.sin(theta);
					x2 = this.first_point.getX() + trombone_radius;
					y2 = this.first_point.getY() + delta + trombone_radius;
				}
				else {
					x1 = this.last_point.getX();
					y1 = this.last_point.getY() + (trombone_length - p);
					x2 = this.last_point.getX() - 5;
					y2 = this.last_point.getY() + (trombone_length - p);
				}
			}
		}
		point.setLocation(x1, y1);
		center.setLocation(x2, y2);
		Couple<Point2D.Double,Point2D.Double> c = new Couple<Point2D.Double,Point2D.Double>(point,center);
		return c;
	}
}