 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

import Raytracer_H_DUFRENE.obj.Obj;
import java.awt.Color;
import java.io.IOException;
/**
 *
 * @author Hugo Dufrêne
 */
public class MAIN {

   
    public static void main(String[] args) throws IOException {
        
        int H = 512; // DEFINITION DE LA HAUTEUR DE L'ECRAN
        int W = 512; // DEFINITION DE LA LARGEUR DE L'ECRAN
        double fov = 60*Math.PI/180; // DEFINITION DE L'ANGLE DU CHAMP VISUEL
        double gamma= 0.45; // FACTEUR POUR LA CORRECTION GAMMA DU RENDU
        double tmax=1E10; // VALEUR MAXIMALE POUR LA LONGUEUR D'UN RAYON AVANT L'IMPACT
        
        // CAMERA
        int nbRayons = 100;// NOMBRE DE RAYONS ENVOYES PAR PIXEL
        Vector position_camera = new Vector(0,0,55); // POSITIONNEMENT DE LA CAMERA SUR L'AXE X ET Y
        Camera camera=new Camera(position_camera,new Vector(0,0,-1),new Vector(0,1,0)); // DEFINITION DE LA CAMERA ET DE SON ORIENTATION
        double dist_plan_focal = 55; // POSITION DE L'ECRAN POUR GERER LA PROFONDEUR DE CHAMP (FLOU)
        
        // ANIMATION
        int nombreImages = 50; // NOMBRE D'IMAGES DE L'ANIMATION
        Vector translation = new Vector(0,20,-10); // TRANSLATION CAMERA
        double rotation = -15*Math.PI/180; // ANGLE DE ROTATION
        
        // SCENE
        double I = 5E9; //INTENSITE LUMINEUSE DE LA SOURCE
        Vector L =new Vector(-20,20,40); // POSITION DE LA SOURCE LUMINEUSE
        Scene scene = new Scene(tmax,I,L); // CREATION D'UNE SCENE A LAQUELLE ON AJOUTERA DES ELEMENTS
        
        // DEFINITION DE LA LUMIERE POUR L'ECLAIRAGE DE LA SCENE
        Sphere S1um = new Sphere(L,5,new Vector(1,1,1),false,false); //(POSITION, RAYON, ALBEDO,MIRROIR,TRANSPARENT, MAX TRANSLATION)

        // DEFINITION DES MURS DE LA SCENE
        Sphere Ssol=new Sphere(new Vector(0,-1000,0),990,new Vector(1,1,1),false,false); // (POSITION, RAYON, BLANC, MIRROIR,TRANSPARENT, MAX TRANSLATION)
        Sphere Smur1=new Sphere(new Vector(-1000,0,0),940,new Vector(1,0,0),false,false); // (POSITION, RAYON, ROUGE, MIRROIR,TRANSPARENT, MAX TRANSLATION) 
        Sphere Smur2=new Sphere(new Vector(1000,0,0),940,new Vector(0,0,1),false,false); // (POSITION, RAYON, BLEU, MIRROIR,TRANSPARENT, MAX TRANSLATION)
        Sphere Smur3=new Sphere(new Vector(0,0,-1000),940,new Vector(0,1,0),false,false); // (POSITION, RAYON, VERT, MIRROIR,TRANSPARENT, MAX TRANSLATION)
        Sphere Smur4=new Sphere(new Vector(0,0,1000),940,new Vector(1,1,1),false,false);  // (POSITION, RAYON, BLANC, MIRROIR,TRANSPARENT, MAX TRANSLATION)
        Sphere Splafond=new Sphere(new Vector(0,1000,0),960,new Vector(1,1,1),false,false); // (POSITION, RAYON, BLANC, MIRROIR,TRANSPARENT, MAX TRANSLATION)
        
        
        // DEFINITION DES ELEMENTS DE LA SCENE
        //Sphere S1 = new Sphere(new Vector(0,10,0),10,new Vector(1,1,1),false,false,new Vector(0,-10,0)); // DEFINITION DE LA SPHERE 1 (POSITION, RAYON, ALBEDO,MIRROIR,TRANSPARENT)
        Sphere S2 = new Sphere(new Vector(-20,0,0),10,new Vector(1,1,1),false,true); // DEFINITION DE LA SPHERE 2 (POSITION, RAYON, ALBEDO,MIRROIR,TRANSPARENT)
        Sphere S3 = new Sphere(new Vector(-20,0,0),10,new Vector(0,0,1),false,true); // DEFINITION DE LA SPHERE 3 (POSITION, RAYON, ALBEDO,MIRROIR,TRANSPARENT)
        
        
        // CHEMIN DU DOSSIER
        String folderPath= "C:/Users/Hugo Dufrêne/informatique-graphique"; // A MODIFIER 
        TriangleMesh mesh = new TriangleMesh(// DEFINITION ET IMPORTATION D'UN MESH (PATH, MAX TRANSLATION, ALBEDO,SCALE,OFFSET)
                folderPath+"/objet/3d-model.obj", // PATH
                new Vector(0,-10,0), // MAX TRANSLATION
                new Vector(103./255.,103./255.,103./255.), // ALBEDO
                2, // SCALE
                new Vector(10,0,0)
        ); // OFFSET
        Obj obj= mesh.readOBJ(); // LECTURE DU FICHIER ET DETERMINATION DES TRIANGLES, NORMALES, TEXTURES
        mesh.addMaterial(folderPath+"/objet/3d-model.mtl",obj); // LECTURE D'UN FICHIER MATERIAUX POUR L'AJOUT DE TEXTURES

        
        // AJOUT DES ELEMENTS DANS LA SCENE
        scene.addObjet(S1um); // SOURCE LUMINEUSE
        
        scene.addObjet(mesh); // MESH
        //scene.addObjet(S1); // SPHERE
        
        scene.addObjet(Ssol); // MURS
        scene.addObjet(Smur1);
        scene.addObjet(Smur2);
        scene.addObjet(Smur3);
        scene.addObjet(Smur4);
        scene.addObjet(Splafond);
        
       
        for(int numeroImage = 0;numeroImage<nombreImages;numeroImage++){
            
            Image image = new Image(W,H,"Image_"+numeroImage); // CREATION D'UNE IMAGE QUE L'ON ENREGISTERA SOUS UN FICHIER PAR LA SUITE
            System.out.println(numeroImage);
            
            for (int i = 0; i < H; i++) { // ON PARCOURT LES LIGNES DE PIXELS QUE L'IMAGE
                for (int j = 0; j < W; j++) { // ON PARCOURT CHAQUE PIXEL SUR CHAQUE LIGNE DE L'IMAGE

                    Vector color_init =  new Vector(0,0,0); // INITIALISATION DE LA COULEUR EN NOIR

                    for(int k =0;k<nbRayons;k++){ // ON LANCE PLUSIEURS RAYONS POUR UN MEME PIXEL, ANTIALIASING
                        // GAUSS MULLER
                        double r1 = Math.random();
                        double r2 = Math.random();
                        double R =Math.sqrt(-2*Math.log(r1));
                        double dx = R*Math.cos(2*Math.PI*r2);
                        double dy = R*Math.sin(2*Math.PI*r2);

                        // OUVERTURE DE L'OBJECTIF
                        double dx_ouv =  (Math.random() -0.001 )* 5;
                        double dy_ouv =  (Math.random() -0.001 )* 5;

                        // DEFINITION DU VECTEUR DIRECTION NORMALISE DU RAYON ENVOYE POUR LE PIXEL CONSIDERE
                        Vector u=(new Vector (j-W/2+0.5*dx,i-H/2+0.5*dy,W/(2*Math.tan(fov/2)))).normalized();

                        // TEMPS POUR LE FLOU DE MOUVEMENT
                        double time = numeroImage*1./nombreImages+Math.random()*1./Math.max(6.,nombreImages);
                        
                        // ORIENTATION DE LA CAMERA AU TEMPS SELECTIONNE
                        camera.translate(translation,time);
                        camera.rotate(rotation, time);
                        
                        // DEFINITION DE LA POSITION DE LA CAMERA
                        Vector camera_right= Vector.cross(camera.getDirection(), camera.getUp());
                        u = Vector.addition(
                                Vector.addition(
                                    Vector.multiplication(camera_right,u.getX()),
                                    Vector.multiplication(camera.getUp(),u.getY())
                                ),
                                Vector.multiplication(camera.getDirection(),u.getZ())
                        ).normalized();
                      
                        // DESTINATION DU RAYON SELON LA POSITION DE CAMERA 
                        Vector destination = Vector.addition( 
                            camera.getPosition(), 
                            Vector.multiplication(dist_plan_focal,u)
                        );

                        // ON RETROUVE LA NOUVELLE ORIGINE DU RAYON A PARTIR DE L'IMPACT SUR LE PLAN FOCAL
                        Vector nouvelle_origine = Vector.addition(
                            camera.getPosition(),
                            new Vector(dx_ouv,dy_ouv,0)
                        );

                        // ON TROUVE LA NOUVELLE DIRECTION NORMALISEE
                        Vector nouvelle_direction = Vector.soustraction(destination,nouvelle_origine).normalized();

                        // ON DEFINIT UN RAYON A PARTIR SON POINT D'EMISSION ET DE SA DIRECTION
                        Ray r = new Ray(nouvelle_origine,nouvelle_direction,time);

                        // ON INTERSECTE CE RAYON AVEC LES ELEMENTS DE LA SCENE POUR RECUPERER LA COULEUR CAPTEE SUR LE CAPTEUR
                        Vector color_k = scene.getColor(r, 0,new Vector(0,0,0),true);

                        // ON FAIT LA MOYENNE DE LA CONTRIBUTION DE COULEUR DE CHAQUE RAYON ENVOYE POUR UN MEME PIXEL
                        color_init.setX(color_init.getX()+color_k.getX()/nbRayons);
                        color_init.setY(color_init.getY()+color_k.getY()/nbRayons);
                        color_init.setZ(color_init.getZ()+color_k.getZ()/nbRayons);
                    }

                    // ON APPLIQUE LA CORRECTION GAMMA ET ON VERIFIE QUE CHAQUE COMPOSANTE DE COULEUR EST COMPRISE ENTRE 0 ET 255
                    Color color_final = new Color(
                        Math.min(255, Math.max((int) conversionGamma(color_init.getX(),gamma),0)),
                        Math.min(255, Math.max((int) conversionGamma(color_init.getY(),gamma),0)),
                        Math.min(255, Math.max((int) conversionGamma(color_init.getZ(),gamma),0))
                    );

                    // ON ATTRIBUE LA COULEUR AU PIXEL CORRESPONDANT SUR L'IMAGE 
                    image.setPixel(j,H-i-1, color_final);
                }
            }
            
            System.out.println(image.enregistrer());
        }
    }
    
    // FONCTION POUR CALCULER LA CONVERSION GAMMA 
    private  static int conversionGamma(double couleurRVB,Double gamma) { 
        return (int) Math.pow(couleurRVB, gamma); 
    }
}
