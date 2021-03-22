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
public class Noeud {
    private Noeud fg; // FILS GAUCHE
    private Noeud fd; // FILS DROI T
     
    private BoundingBox b; // BOUNDING BOX
    private int debut; // INDICE DE DEBUT
    private int fin; // INDICE DE FIN

    
    // CONSTRUCTEURS
    public Noeud() {
    }

    public Noeud(Noeud fg, Noeud fd, BoundingBox b, int debut, int fin) {
        this.fg = fg;
        this.fd = fd;
        this.b = b;
        this.debut = debut;
        this.fin = fin;
    }

    // GETTERS
    public Noeud getFg() {
        return fg;
    }

    public void setFg(Noeud fg) {
        this.fg = fg;
    }

    public Noeud getFd() {
        return fd;
    }

    public void setFd(Noeud fd) {
        this.fd = fd;
    }

    public BoundingBox getB() {
        return b;
    }

    // SETTERS
    public void setB(BoundingBox b) {
        this.b = b;
    }

    public int getDebut() {
        return debut;
    }

    public void setDebut(int debut) {
        this.debut = debut;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }
    
   
    
}