/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

import java.awt.Color;
/**
 *
 * @author Hugo Dufrêne
 */
public class Sphere extends Objet{
    
    private Vector o; // ORIGINE DE LA SPHERE
    private double r; // RAYON DE LA SPHERE
    private Vector albedo; // ALBEDO DE LA SPHERE
    private boolean mirror; // MIROIR OU NON
    private boolean transparent; // TRANSPARENTE OU NON
    private Vector maxTranslation=new Vector(0,0,0); // MAXIMUM TRANSLATION

    public Sphere(Vector o, double r, Vector albedo,boolean mirror,boolean transparent) {
        this.o = o;
       this.r = r;
       this.albedo = albedo;
       this.mirror = mirror;
       this.transparent = transparent;
    }
    public Sphere(Vector o, double r, Vector albedo,boolean mirror,boolean transparent,Vector maxTranslation) {
        this(o,r,albedo,mirror,transparent);
        this.maxTranslation=maxTranslation;
    }

    
  
    // GETTERS 
    public Vector getO() {
        return o;
    }

    public double getR() {
        return r;
    }

    public Vector getAlbedo() {
        return albedo;
    }

    public boolean isMirror() {
        return mirror;
    }

    public boolean isTransparent() {
        return transparent;
    }
     public Vector getTranslation(double time) {
        return Vector.multiplication(time,maxTranslation);
    }
   
   


    // SETTERS
    public void setO(Vector o) {
        this.o = o;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setAlbedo(Vector albedo) {
        this.albedo = albedo;
    }
    
    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }
     public void setMaxTranslation(Vector maxTranslation) {
        this.maxTranslation = maxTranslation;
    }
    
    // ROUTINE D'INTERSECTION POUR UNE SPHERE
    public  Object[]  intersect(Ray rayon,int i){
        double a = 1; //ARBITRAIRE
        double b = 2*Vector.dot(rayon.getU(),Vector.soustraction(rayon.getC() , this.o));
        double c = Vector.soustraction(rayon.getC(),this.o).getNorm2() - this.r*this.r;
        double delta = b*b-4*a*c;
        
        Object[] data = new Object[8];
        data[0]=false; //Has Inter
        data[1]=null; // localP
        data[2]=null; // localN
        data[3]=0.0; // LocalT
        data[4]=new Color(0,0,0);// ALBEDO
        data[5]=this.mirror;// MIRROR
        data[6]=this.transparent;// TRANSPARENT
        data[7]=i; //ID
        
        // PAS DE SOLUTION
        if(delta<0) return data;
        
        // CALCUL DES DEUX POINTS D'INTERSECTION
        double t1= (-b -Math.sqrt(delta))/(2*a);
        double t2= (-b +Math.sqrt(delta))/(2*a);
        
        // ON NE CONSIDERE QUE LES OBJETS DEVANT LA CAMERA
        if(t2<0) return data;
        
        // ON DETERMINE LE POINT D'INTERSECTION LE PLUS PROCHE        
        double t=0.0;
        if(t1>0){
            t=t1;
        }else{
            t=t2;
        }
        
        
        data[0]=true; // HAS INTER
        data[1]=Vector.addition(rayon.getC(),Vector.multiplication(t,rayon.getU())); // localP
        data[2]=(Vector.soustraction((Vector) data[1], this.o)).normalized(); // localN
        data[3]=t; //localT
        data[4]=this.albedo; //localAlbedo
        return data;
    }
    
    
}
