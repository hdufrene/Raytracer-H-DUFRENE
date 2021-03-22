/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Hugo Dufrêne
 */
public class Scene {

    private double tmax; // VALEUR MAXIMALE POUR LA LONGUEUR D'UN RAYON AVANT L'IMPACT
    
    private double I ; //INTENSITE LUMINEUSE DE LA SOURCE
    private Vector L ; // POSITION DE LA SOURCE LUMINEUSE

    public Scene(double tmax, double I, Vector L) {
        this.tmax = tmax;
        this.I = I;
        this.L = L;
    }
   
    private ArrayList<Objet> objets = new ArrayList<>(); // LISTE D'OBJETS COMPOSANT LA SCENE
    
    // AJOUT D'UN OBJET DANS LA SCENE 
    public boolean addObjet(Objet objet){
        if(objet!=null){
            objets.add(objet);
            return true;
        }
        return false;
    }
    
    // GETTERS
    public ArrayList<Objet> getObjects() {
        return this.objets;
    }
    
    // METHODE D'UNTERSECTION ENTRE UN RAYON ET UN OBJET DE LA SCENE
    // RENVOIT UN TABLEAU CONTENANT [intersection (Vrai/Faux), P (point intersection), N (normale au pt d'intersection), t, albedo, miroir, transparent, id de l'objet intersecté]
    public Object[] intersect(Ray r){
        Object[] data = new Object[8];
        
        data[0]=false; // HAS INTER
        data[1]=null; // P 
        data[2]=null; // N
        data[3]=1E10; // T
        data[4]=new Vector(0,0,0); // ALBEDO
        data[5] = false; // MIRROIR
        data[6] = false; // TRANSPARENT
        data[7]=0; // ID OBJET
        
        // ON PARCOURT LES OBJETS DANS LA LISTE D'OBJETS 
        for(int i=0; i<objets.size() ;i++){
            Ray transformedRay = new Ray(
                    Vector.soustraction(r.getC(),objets.get(i).getTranslation(r.getTime())),
                    r.getU(),
                    r.getTime()
                );
                     
            Object[] intersectioni = objets.get(i).intersect(transformedRay,i); // ON TESTE S'IL Y A UNE INTERSECTION ENTRE L'OBJET ET LE RAYON
            
            // S'IL Y A INTERSECTION ET SI L'OBJET EST SITUE DEVANT UN AUTRE OBJET DEJA INTERSECTE(OU S'IL N'Y A PAS D'OBJET INTERSECTE)
            // ON REMPLACE L'ANCIEN POINT D'INTERSECTION PAR LE NOUVEAU POINT INTERSECTE
            if((boolean) intersectioni[0] && (double) intersectioni[3]<(double) data[3]){
                
                data[0]=true; // INTERSECTION VRAI
                data[1]=Vector.addition((Vector) intersectioni[1],objets.get(i).getTranslation(r.getTime())); // NOUVEAU P AVEC RETABLISSEMENT DE LA TRANSLATION
                data[2]=(Vector) intersectioni[2]; // NOUVEAU N
                data[3]=(double) intersectioni[3]; // NOUVEAU t
                data[4]= (Vector) intersectioni[4]; // NOUVEAU ALBEDO
                data[5]= (boolean) intersectioni[5]; // NOUVEAU MIRROIR
                data[6]= (boolean) intersectioni[6]; // NOUVEAU TRANSPARENT
                data[7]=(int) intersectioni[7]; // NOUVEAU ID
            }
        }
        return data;
    }

    
    
    public Vector getColor(Ray r, int rebond,Vector color, boolean lastDiffuse) {
        // ON LANCE LA ROUTINE D'INTERSECTION POUR DETERMINER LE PLUS PROCHE OBJET INTERSECTE
        Object[] intersection = this.intersect(r); 
       
        // ON LIMITE LE NOMBRE DE REBONDS A 5 ARBITRAIREMENT
        if(rebond>5){
            return color;
        }

        // S'IL Y A UNE INTERSECTION, ON RECUPERE LES INFORMATIONS DE L'OBJET INTERSECTE
        if((boolean) intersection[0]){
            Vector P = (Vector) intersection[1]; // POINT D'INTERSECTION
            Vector N = (Vector) intersection[2]; // NORMALE AU POINT D'INTERSECTION
            double t = (double) intersection[3]; // t AU POINT D'INTERSECTION 
            Vector albedo = (Vector) intersection[4]; // ALBEDO AU POINT D'INTERSECTION
            boolean mirror = (boolean) intersection[5]; // MIRROIR DE L'OBJET INTERSECTE
            boolean transparent = (boolean) intersection[6]; // TRANSPARENT DE L'OBJET INTERSECTE
            int objectId = (int) intersection[7]; // ID DE L'OBJET INTERSECTE

            if(objectId==0){ // SOURCE LUMINEUSE INTERSECTEE 
                if(rebond==0 || !lastDiffuse){
                    double facteur=  I /(4 * Math.PI* Math.PI*objets.get(0).getR()*objets.get(0).getR());
                    return new Vector(facteur,facteur,facteur);
                }else{
                     return new Vector(0,0,0);
                     
                }
            }
            
            if(mirror){ // SI L'OBJET EST UN MIROIR
                
                // ON APPLIQUE LES LOIS DE SNELL DESCARTES POUR UN RAYON REFLECHI SUR UN MIROIR
                
                // ON OBTIENT LA DIRECTION DU RAYON REFLECHI
                Vector reflectedDir = Vector.soustraction( 
                    r.getU(), 
                    Vector.multiplication(2* Vector.dot(r.getU(),N),N)
                ); 
                
                // ON CREE LE RAYON REFLECHI A PARTIR DE LA DIRECTION ET DU POINT D INTERSECTION
                Ray reflectedRay = new Ray(Vector.addition(P , Vector.multiplication(0.001, N)),reflectedDir,r.getTime());
                
                // ON RECALCULE LA COULEUR A PARTIR DE CE NOUVEAU RAYON REFLECHI, EN AJOUTANT UN RAYON. UN MIROIR NE PEUT PAS ETRE DIFFUS.
                return getColor(reflectedRay,rebond+1,color, false);
                

            }else{ // SI CE N'EST PAS UN MIROIR
                
                if(transparent){ // SI L'OBJET EST TRANSPARENT
                   
                    // DEFINITION DES INDICES DES MILIEUX (AIR =1, VERRE =1.4)
                    double n1 = 1.; 
                    double n2 = 1.4; // PAR DEFAUT ON ARRIVE DE LA SPHERE
                    
                    Vector N2 = N;
                    
                    // DANS CE CAS ON SORT DE LA SPHERE ET ON DOIT INTERCHANGER LES INDICES ET INVERSER LE SENS DE LA NORMALE
                    if(Vector.dot(r.getU(),N)>0){ 
                        double aux = n1;
                        n1 = n2;
                        n2=aux;
                        N2 = Vector.multiplication(-1,N);
                    }
                    
                    // ON APPLIQUE LA LOI DE SNELL DESCARTES POUR LA REFRACTION
                    Vector Tt = Vector.multiplication(
                        n1/n2,
                        Vector.soustraction(
                            r.getU(),
                            Vector.multiplication(Vector.dot(r.getU(),N2),N2)
                        )
                    );
                    
                    // ON CALCULE L'ANGLE DE REFRACTION ET ON VERIFIE QU'IL EST POSITIF (SINON REFLEXION COMPLETE)
                    double rad = 1 - Math.pow((n1/n2),2)*(1 - Math.pow((Vector.dot(r.getU(),N2)),2));
                    
                    if(rad < 0){ // IDEM QUE LA REFLEXION
                        Vector reflectedDir = Vector.soustraction(
                            r.getU(), 
                            Vector.multiplication(2* Vector.dot(r.getU(),N),N)
                        );
                        Ray reflectedRay = new Ray(Vector.addition(P , Vector.multiplication(0.001, N)),reflectedDir,r.getTime());
                        
                        return getColor(reflectedRay,rebond + 1, color, false);
                    }
                    
                    // ON CALCULE UN RAYON REFRACTE A PARTIR DE LA DIRECTION ET DU POINT
                    Vector Tn = Vector.multiplication(-Math.sqrt(rad),N2);
                    Vector refractedDir = Vector.addition(Tt , Tn);
                    Ray refractedRay = new Ray(Vector.addition(P , Vector.multiplication(-0.001, N2)),refractedDir,r.getTime());
                    
                    return getColor(refractedRay,rebond+1,color,false);
                    
                    
                    
                }else{ // SI L'OBJET N'EST NI TRANSPARENT NI UN MIROIR -> BRDF
                    
                    // ON CALCULE LA DIRECTION DE LA LUMIERE A PARTIR DU POINT INTERSECTE (PROVENANCE DE LA LUMIERE)
                    Vector PL = Vector.soustraction(this.L, P);
                    PL = PL.normalized();
                                   
                    // ON CALCULE UNE DIRECTION ALEATOIRE (DIFFUSION DE LA LUMIERE)
                    Vector w = random_cos(Vector.multiplication(-1,PL));
                    
                    Vector xprime = Vector.addition(
                        Vector.multiplication(w,objets.get(0).getR()),
                        objets.get(0).getO()
                    ); // POINT SUR LE BON COTE DE L'OBJET
                    
                    
                   
                    Vector Pxprime = Vector.soustraction( xprime, P);
                    double d = Math.sqrt(Pxprime.getNorm2());
                    Pxprime =Vector.division(Pxprime,d);
                    
                    // RAYON OMBRES (MEME METHODE)
                    Ray shadowRay = new Ray(Vector.addition(P , Vector.multiplication(0.00001, N)),Pxprime,r.getTime());
                    Object[] shadowIntersection = this.intersect(shadowRay); 

                    Vector shadowP = (Vector) shadowIntersection[1];
                    Vector shadowN = (Vector) shadowIntersection[2];
                    double shadowt = (double) shadowIntersection[3];
                    Vector shadowAlbedo = (Vector) shadowIntersection[4];
                    boolean shadowMirror = (boolean) shadowIntersection[5];

                    double facteur ;
    
                    if ((boolean) shadowIntersection[0] && shadowt<d-0.0001){  // ON A UNE OMBRE
                         facteur=0. ; 
                          
                    }else{ // PAS D'OMBRE
                        double proba = Math.max(0,Vector.dot(Vector.multiplication(-1, PL),w)/(Math.PI*objets.get(0).getR()*objets.get(0).getR())); // PROBABILITE
                        double J = Vector.dot(
                                    w,
                                    Vector.multiplication(-1,Pxprime)
                                )/(d*d); // JACOBIEN
                        facteur = I /(4 * Math.PI* Math.PI*objets.get(0).getR()*objets.get(0).getR())/Math.PI*Vector.dot(N,Pxprime)*J/proba ;
                
                    }

                    
                    Vector couleur_brute = new Vector(
                        (int) (facteur),
                        (int) (facteur),
                        (int) (facteur)
                    ); // COULEUR BRUTE OBTENUE PAR INTERSECTION
                   
                    
                    // ON CALCULE L'ECLAIRAGE INDIRECT EN GENERANT UN AUTRE RAYON
                    Vector wi = random_cos(N);  
                    Ray wiRay= new Ray(Vector.addition(P , Vector.multiplication(0.001, N)),wi,r.getTime());               
                            
                    // ON AJOUTE LA CONTRIBUTION DE L'ECLAIRAGE INDIRECT A LA LUMIERE INTIALEMENT OBTENUE
                    couleur_brute = addColor(couleur_brute, getColor(wiRay,rebond+1,couleur_brute,true));
                    couleur_brute = addAlbedo(couleur_brute,albedo);
                    

                    return couleur_brute; // ON RETOURNE LA COULEUR OBTENUE
                   
                    }
                }
            }   
        return color;
    }
    
    
    
    // METHODE QUI PERMET DE GENERER UN VECTEUR NORMAL POUR UNE BRDF DIFFUSE
    private Vector random_cos(Vector N){
        double u1 = Math.random();
        double u2 = Math.random();
        double x1 =  Math.cos(2*Math.PI*u1) *Math.sqrt(1-u2);
        double x2 = Math.sin(2*Math.PI*u1) *Math.sqrt(1-u2);
        double z = Math.sqrt(u2);
        
        Vector T1 ;
        if(N.getX()<N.getY() && N.getX()<N.getZ()){
            T1 = new Vector(0,N.getZ(),-N.getY());
        }
        else{
            if (N.getY()<N.getZ() && N.getY()<N.getX()){
                T1 = new Vector(N.getZ(), 0 , -N.getX());
            }
            else{
                T1 = new Vector(N.getY(), -N.getX(), 0);
            }
        }
        
        T1 = T1.normalized();
        
        Vector T2 = Vector.cross(T1,N);
        
        return Vector.addition(
                
                Vector.addition(
                    Vector.multiplication(z,N),
                    Vector.multiplication(x1,T1)
                ),
           
                Vector.multiplication(x2, T2)
            );
        }
    
    
    
    // METHODE QUI PERMET L'AJOUT D'UNE CONTRIBUTION DE COULEUR A LA COULEUR EXISTANTE
    private Vector addColor(Vector couleur_brute, Vector color_supp){ 
        return new Vector(
            couleur_brute.getX()+color_supp.getX(),
            couleur_brute.getY()+color_supp.getY(),
            couleur_brute.getZ()+color_supp.getZ()
        );
    }
    
    // METHODE QUI PERMET D'AJOUTER LA CONTRIBUTION DE L'ALBEDO A UNE COULEUR
    private Vector addAlbedo(Vector couleur_brute,Vector albedo){
        return new Vector(
            couleur_brute.getX()*albedo.getX(),
            couleur_brute.getY()*albedo.getY(),
            couleur_brute.getZ()*albedo.getZ()
        ); 
    }
    
}
