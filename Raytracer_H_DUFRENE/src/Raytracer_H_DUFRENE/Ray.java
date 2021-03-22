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
public class Ray {
    private Vector c; // ORIGINE DU RAYON
    private Vector u; // DIRECTION DU RAYON
    double time; // TEMPS AUQUEL LE RAYON EST ENVOYE

    // CONSTRUCTEUR
    public Ray(Vector c, Vector u,double time) {
        this.c = c;
        this.u = u;
        this.time=time;
    }
    
    //GETTERS
    public Vector getC() {
        return c;
    }

    public Vector getU() {
        return u;
    }

    public double getTime() {
        return time;
    }

    //SETTERS
    public void setC(Vector c) {
        this.c = c;
    }

    public void setU(Vector u) {
        this.u = u;
    }

    public void setTime(double time) {
        this.time = time;
    }

    
    
    
   
    
}
