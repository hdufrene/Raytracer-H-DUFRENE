/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hugod
 */
public class Camera {
    
    private Vector initialPosition; // POSITION INITIALE DE LA CAMERA
    private Vector initialDirection; // DIRECTION INITIALE DE LA CAMERA
    private Vector initialUp; // RAYON SUPERIEUR INITIAL DE LA CAMERA
    private Vector position = new Vector(0,0,0); // POSITION COURANTE DE LA CAMERA
    private Vector direction = new Vector(0,0,0) ; // DIRECTION COURANTE DE LA CAMERA
    private Vector up= new Vector(0,0,0); // RAYON SUPERIEUR COURANT DE LA CAMERA
    

    // CONSTRUCTEUR
    public Camera( Vector initialPosition, Vector initialDirection, Vector initialUp) {
       
        this.initialPosition = initialPosition;
        this.initialDirection = initialDirection;
        this.initialUp = initialUp;
        position.setX(initialPosition.getX());
        position.setY(initialPosition.getY());
        position.setZ(initialPosition.getZ());
        direction.setX(initialDirection.getX());
        direction.setY(initialDirection.getY());
        direction.setZ(initialDirection.getZ());
        up.setX(initialUp.getX());
        up.setY(initialUp.getY());
        up.setZ(initialUp.getZ());
        
    }

    // GETTERS ET SETTERS 
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Vector getUp() {
        return up;
    }

    public void setUp(Vector up) {
        this.up = up;
    }

    public Vector getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Vector initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Vector getInitialDirection() {
        return initialDirection;
    }

    public void setInitialDirection(Vector initialDirection) {
        this.initialDirection = initialDirection;
    }

    public Vector getInitialUp() {
        return initialUp;
    }

    public void setInitialUp(Vector initialUp) {
        this.initialUp = initialUp;
    }
    
    
    
    // TRANSLATION DE LA CAMERA
    void translate(Vector translation,double time){
        position=Vector.addition(
                initialPosition,
                Vector.multiplication(time,translation)
        );
    }
    
    // ROTATION DE LA CAMERA
     void rotate(double angle,double time){
        double cur_angle=time*angle; // ANGLE AU TEMPS SELECTIONNE
        
        // MODIFICATION DE LA DIRECTION AU TEMPS SELECTIONNE
        direction.setX(initialDirection.getX());
        direction.setY(Math.cos(cur_angle)*initialDirection.getIndice(1)-Math.sin(cur_angle)*initialDirection.getIndice(2));
        direction.setZ(Math.sin(cur_angle)*initialDirection.getIndice(1)+Math.cos(cur_angle)*initialDirection.getIndice(2));
 
        // MODIFICATION DU RAYON SUPERIEUR AU TEMPS SELECTIONNE
        up.setX(initialUp.getX());
        up.setY(Math.cos(cur_angle)*initialUp.getIndice(1)-Math.sin(cur_angle)*initialUp.getIndice(2));
        up.setZ(Math.sin(cur_angle)*initialUp.getIndice(1)+Math.cos(cur_angle)*initialUp.getIndice(2));
            
    }
        
}
