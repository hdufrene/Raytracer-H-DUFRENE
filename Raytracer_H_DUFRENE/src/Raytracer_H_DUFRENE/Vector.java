/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

/**
 *
 * @author Hugo Dufrêne
 */
public class Vector {
    private double[] coords = new double[3]; // COORDONNEES x,y,z
    
    
    // CONSTRUCTEUR
    public Vector(double x, double y, double z) {
        this.coords[0] = x;
        this.coords[1] = y;
        this.coords[2] = z;
    }

    // GETTERS
    public double getX() {
        return  this.coords[0];
    }
    public double getY() {
        return  this.coords[1];
    }
    public double getZ() {
       return  this.coords[2];
    }
    public double getIndice(int indice){
        if(indice==0) return this.getX();
        if(indice==1) return this.getY();
        if(indice==2) return this.getZ();
        return 0.;
    }
    
     // SETTERS
    public void setX(double x) {
        this.coords[0] = x;
    }

    public void setY(double y) {
        this.coords[1] = y;
    }

    public void setZ(double z) {
         this.coords[2]= z;
    }
    
    
    
    //METHODE DE CALCUL DE LA NORME AU CARRE DU VECTEUR
    public double getNorm2(){
        return  this.coords[0]* this.coords[0] +  this.coords[1]* this.coords[1]+ this.coords[2]* this.coords[2];
    }
    
    //METHODE DE NORMALISATION DU VECTEUR
    public Vector normalized(){
        double norme = Math.sqrt(this.getNorm2());
        return  new Vector(this.coords[0]/norme,this.coords[1]/norme,this.coords[2]/norme);
    }
    
   // ADDITION DE DEUX VECTEURS
    public static Vector addition(Vector a,Vector b){ 
        return new Vector(a.getX()+b.getX(), a.getY()+b.getY(), a.getZ()+b.getZ());
    }
    

    // SOUSTRACTION DE DEUX VECTEURS
     public static Vector soustraction(Vector a,Vector b){
        return new Vector(a.getX()-b.getX(), a.getY()-b.getY(), a.getZ()-b.getZ());
    }
    
    // MULTIPLICATION D'UN SCALAIRE ET D'UN VECTEUR
    public static Vector multiplication(double a,Vector b){
        return new Vector(a*b.getX(), a*b.getY(), a*b.getZ());
    }
    // MULTIPLICATION D'UN VECTEUR ET D'UN SCALAIRE
    public static Vector multiplication(Vector a,double b){
        return new Vector(b*a.getX(), b*a.getY(), b*a.getZ());
    }
    
    // MULTIPLICATION DE DEUX VECTEURS TERME A TERME
    public static Vector multiplication(Vector a,Vector b){
        return new Vector(a.getX()*b.getX(), a.getY()*b.getY(), a.getZ()*b.getZ());
    }
     
    // DIVISION D'UN VECTEUR PAR UN SCALAIRE
    public static Vector division(Vector b,double a){
        return new Vector(b.getX()/a, b.getY()/a, b.getZ()/a);
    }
    
    // PRODUIT SCALAIRE ENTRE DEUX VECTEURS
    public static double dot(Vector a, Vector b){
        return a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ();
    }
    
    // PRODUIT VECTORIEL ENTRE DEUX VECTEURS
    public static Vector cross(Vector a,Vector b){
        return new Vector(
                a.getY()*b.getZ() - a.getZ()*b.getY(),
                a.getZ()*b.getX() - a.getX()*b.getZ(),
                a.getX()*b.getY() - a.getY()*b.getX());
    }
    
}  
