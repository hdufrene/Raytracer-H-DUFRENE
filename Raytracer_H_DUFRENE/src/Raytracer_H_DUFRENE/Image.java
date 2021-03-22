/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hugo Dufrêne
 */
public class Image {
    private int width;
    private int height;
    private String nom;
    private BufferedImage image;

    public Image(int width, int height,String nom) {
        this.width=width;
        this.height = height;
        this.nom=nom;
        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    }
    
    
    // METHODE QUI PERMET D'ASSIGNER UNE COULEUR A UN PIXEL DE L'IMAGE
    public boolean setPixel(int i, int j, Color color){
        this.image.setRGB(i, j, color.getRGB());
        return true;
    }
    
    // METHODE QUI PERMET D'ENREGISTRER UNE IMAGE DANS LE REPERTOIRE COURANT
    public boolean enregistrer() throws IOException{
        try{
            ImageIO.write(this.image, "png", new File(nom+".png"));
            return true;
        }catch(IOException IOException){
            return false;
        }
       
    }
    

}
