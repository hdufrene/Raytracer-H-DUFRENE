/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

/**
 *
 * @author hugod
 */
public class BoundingBox {
    
    private Vector mini;
    private Vector maxi;

    // CONSTRUCTEUR
    public BoundingBox(Vector mini, Vector maxi) {
        this.mini = mini;
        this.maxi = maxi;
    }

    // GETTERS
    public Vector getMini() {
        return mini;
    }
    public Vector getMaxi() {
        return maxi;
    }
    public double getMini(int indice){
      return this.mini.getIndice(indice);
    }
    public double getMaxi(int indice){
      return this.maxi.getIndice(indice);
    }

    // SETTERS
    public void setMini(int indice,double value) {
        if(indice==0){
            this.mini.setX(value);
        }else if(indice==1){
            this.mini.setY(value);
        }else if(indice==2){
            this.mini.setZ(value);
        }
    }
    public void setMaxi(int indice,double value) {
        if(indice==0){
            this.maxi.setX(value);
        }else if(indice==1){
            this.maxi.setY(value);
        }else if(indice==2){
            this.maxi.setZ(value);
        }
    }
    
   
    // METHODE D'INTERSECTION ENTRE UN RAYON ET UNE BOUNDING BOX
    public boolean intersect(Ray r){
        
        double t1x = (mini.getX() - r.getC().getX()) / r.getU().getX();
        double t2x = (maxi.getX() - r.getC().getX()) / r.getU().getX();

        double tminx = Math.min(t1x, t2x);
        double tmaxx = Math.max(t1x, t2x);

        double t1y = (mini.getY() - r.getC().getY()) / r.getU().getY();
        double t2y = (maxi.getY() - r.getC().getY()) / r.getU().getY();

        double tminy = Math.min(t1y, t2y);
        double tmaxy = Math.max(t1y, t2y);
        
        double t1z = (mini.getZ() - r.getC().getZ()) / r.getU().getZ();
        double t2z = (maxi.getZ() - r.getC().getZ()) / r.getU().getZ();

        double tminz = Math.min(t1z, t2z);
        double tmaxz = Math.max(t1z, t2z);
        
        
        if(Math.min(tmaxx,Math.min(tmaxy,tmaxz)) - Math.max(tminx,Math.max(tminy,tminz))>0) return true;
        return false;
    }
        
    
    
}