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
public abstract class Objet {

    // CONSTRUCTEUR
    public Objet() {
        
    }
    
    // ROUTINE D'INTERSECTION DEFINIE POUR LA CLASSE OBJET (IMPLEMENTEE DANS LES CLASSES SPHERE ET TRIANGLEMESH)
    public Object[] intersect(Ray r, int i){
        return null;
    }
    
    // GETTERS 
    public double getR(){
        return 0.;
    } 
    public Vector getO(){
        return new Vector(0,0,0);
    } 
    public Vector getTranslation(double time) {
      return null;
  }

   
    
}
