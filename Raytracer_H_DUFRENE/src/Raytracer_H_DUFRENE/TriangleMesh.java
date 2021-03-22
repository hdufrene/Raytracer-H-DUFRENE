/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raytracer_H_DUFRENE;

import Raytracer_H_DUFRENE.obj.Mtl;
import Raytracer_H_DUFRENE.obj.MtlReader;
import Raytracer_H_DUFRENE.obj.Obj;
import Raytracer_H_DUFRENE.obj.ObjReader;
import Raytracer_H_DUFRENE.obj.ObjUtils;
import Raytracer_H_DUFRENE.obj.ObjData;
import Raytracer_H_DUFRENE.obj.ReadableObj;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 *
 * @author Hugo Dufrêne
 */
public class TriangleMesh extends Objet{
    
    private Vector maxTranslation; // MAXIMUM DE TRANSLATION
    private double scale =1.; // ECHELLE DU MAILLAGE
    private Vector offset = new Vector(0,0,0); // POSITIONNEMENT DU MAILLAGE
    private Vector albedo ;
    int[] ligneIndices ;
    ArrayList<TriangleIndices> trianglesIndices= new ArrayList<TriangleIndices>();
    
    float[] ligneVertices;
    ArrayList<Vector> vertices = new ArrayList<Vector>();
    
    float[] ligneNormals ;
    ArrayList<Vector> normals = new ArrayList<Vector>();
        
    float[] ligneTexCoords ;
    ArrayList vertexcolors = new ArrayList<Vector>();
    
    ArrayList<Vector> textures = new ArrayList<Vector>();
    
    String filePath = "";
    
    Noeud BVH = new Noeud();
    BoundingBox boundingBox;
    
    // CONSTRUCTEURS
    public TriangleMesh(String filePath,Vector maxTranslation,Vector albedo){
        this.filePath = filePath;
        this.maxTranslation = maxTranslation;
        this.albedo=albedo;
    }
    
    public TriangleMesh(String filePath,Vector maxTranslation,Vector albedo,double scale,Vector offset){
        this(filePath,maxTranslation,albedo);
        this.scale = scale;
        this.offset=offset;
    }

    // METHODE DE LECTURE DU FICHIER .OBJ
    public Obj readOBJ() throws FileNotFoundException, IOException {
        
        File initialFile = new File(this.filePath);
        InputStream inputStream = new FileInputStream(initialFile);
        
        Obj obj = ObjUtils.convertToRenderable(
            ObjReader.read(inputStream));

         
        int faceVertexIndices[] = ObjData.getFaceVertexIndicesArray(obj); // INDICES DES COORODONNEES DES FACES DES TRIANGLES
        int faceTexCoordIndices[] = ObjData.getFaceTexCoordIndicesArray(obj); // INDICES DES COORDONNEES DES TEXTURES
        int faceNormalIndices[] = ObjData.getFaceNormalIndicesArray(obj); // INDICES DES COORDONNEES DES NORMALES
        
        
        this.ligneVertices= ObjData.getVerticesArray(obj); //  FACES DES TRIANGLES EN LIGNE
        this.ligneNormals=ObjData.getNormalsArray(obj);// NORMALES DES TRIANGLES EN LIGNE
        this.ligneTexCoords=ObjData.getTexCoordsArray(obj, 2,false);// TEXTURES DES TRIANGLES EN LIGNE
        
        // ON CREE UNE LISTE DES INDICES
        for(int i=0;i<faceVertexIndices.length/3;i++){
           
            trianglesIndices.add(
                new TriangleIndices(
                     faceVertexIndices[i*3],
                    faceVertexIndices[i*3+1],
                    faceVertexIndices[i*3+2],
                    faceNormalIndices[i*3],
                    faceNormalIndices[i*3+1],
                    faceNormalIndices[i*3+2],
                    faceTexCoordIndices[i*3],
                    faceTexCoordIndices[i*3+1],
                    faceTexCoordIndices[i*3+2]
                )
            );
        }

        // ON CREE UNE LISTE DE VECTEURS POUR LES FACES, NORMALES ET LES TEXTURES
        for(int i=0;i<ligneVertices.length/3;i++){
            vertices.add(
                new Vector(
                    scale*ligneVertices[i*3]+offset.getX(),
                    scale*ligneVertices[i*3+1]+offset.getY(),
                    scale*ligneVertices[i*3+2]+offset.getZ()
                      
                ));
        }
        
        for(int i=0;i<ligneNormals.length/3;i++){
            normals.add(
                new Vector(
                    ligneNormals[i*3],
                    ligneNormals[i*3+1],
                    ligneNormals[i*3+2]
                ));
        }
        for(int i=0;i<ligneTexCoords.length/3;i++){
            textures.add(
                new Vector(
                    ligneTexCoords[i*3],
                    ligneTexCoords[i*3+1],
                    ligneTexCoords[i*3+2]
                ));
        }

        // ON CONSTRUIT LE BHV
        buildBVH(BVH,0, trianglesIndices.size());
        return obj;
    }
    
    // METHODE POUR L'AJOUT DE TEXTURES (NE MARCHE PAS)
    public void addMaterial(String filename, Obj obj) throws FileNotFoundException, IOException{
        List<Mtl> allMtls = new ArrayList<Mtl>(); 
        InputStream mtlInputStream = new FileInputStream(filename); 
        List<Mtl> mtls = MtlReader.read(mtlInputStream); 
        allMtls.addAll(mtls);  
    }
    
    
    
    // METHODE DE CONSTRUCTION DE LA BOUNDING BOX
    public BoundingBox buildBB(int debut,int fin){
        BoundingBox bb= new BoundingBox(new Vector(1E9,1E9,1E9),new Vector(-1E9,-1E9,-1E9));
        
        for(int i=debut;i<fin;i++){
            for(int j=0;j<3;j++){
                bb.setMini(j,Math.min(bb.getMini(j),vertices.get(trianglesIndices.get(i).getVtxi()).getIndice(j)));
                bb.setMaxi(j,Math.max(bb.getMaxi(j),vertices.get(trianglesIndices.get(i).getVtxi()).getIndice(j)));
                bb.setMini(j,Math.min(bb.getMini(j),vertices.get(trianglesIndices.get(i).getVtxj()).getIndice(j)));
                bb.setMaxi(j,Math.max(bb.getMaxi(j),vertices.get(trianglesIndices.get(i).getVtxj()).getIndice(j)));
                bb.setMini(j,Math.min(bb.getMini(j),vertices.get(trianglesIndices.get(i).getVtxk()).getIndice(j)));
                bb.setMaxi(j,Math.max(bb.getMaxi(j),vertices.get(trianglesIndices.get(i).getVtxk()).getIndice(j)));
            }
        }
        return bb ;
    }
    
    // METHODE DE CONSTRUCTION DU BVH (RECURSIF)
    public void buildBVH(Noeud n, int debut, int fin){
        n.setDebut(debut);
        n.setFin(fin);
        n.setB(buildBB(debut,fin));
        n.setFg( null);
        n.setFd( null);
        
        Vector diag = Vector.soustraction(n.getB().getMaxi() ,n.getB().getMini());
        int dim = 0;
        
        if(diag.getIndice(0)>=diag.getIndice(1) && diag.getIndice(0)>=diag.getIndice(2)){
            dim=0;
        }else{
            if(diag.getIndice(1)>=diag.getIndice(0) && diag.getIndice(1)>=diag.getIndice(2)){
                dim=1;
            }else{
                dim=2;
            }
        }
        
        
        double milieu = (n.getB().getMini().getIndice(dim)+n.getB().getMaxi().getIndice(dim))/2;
        
        int indice_pivot = n.getDebut();
        
        for(int i=n.getDebut();i<n.getFin();i++){
            double milieu_triangle = (vertices.get(trianglesIndices.get(i).getVtxi()).getIndice(dim)+vertices.get(trianglesIndices.get(i).getVtxj()).getIndice(dim)+vertices.get(trianglesIndices.get(i).getVtxk()).getIndice(dim))/3;
            if(milieu_triangle<milieu){
                Collections.swap(trianglesIndices,i,indice_pivot);
                indice_pivot++;
            }
        }
        
        n.setFg( null);
        n.setFd( null);
        
        // CRITERE ARRET
        if(indice_pivot<=debut || indice_pivot >=fin ){
            return ;
        }
        n.setFg( new Noeud());
        n.setFd( new Noeud());
        buildBVH(n.getFg(),n.getDebut(),indice_pivot);
        buildBVH(n.getFd(),indice_pivot,n.getFin());
    }
    
   
    
    // ROUTINE D'INTERSECTION RAYON / TRIANGLE
    @Override
    public Object[] intersect(Ray r,int i){
        Object[] data = new Object[8];
        data[0]=false; //Has 
        
        data[1]=null; // localP
        data[2]=null; // localN
        data[3]=0.0; // LocalT
        data[4]=this.albedo;// ALBEDO
        
        data[5]=false; // MIRROR;
        data[6]=false; // TRANSPARENT
                
        data[7]=i; //ID
        
        
        double t=1E9;
        ArrayList<Noeud> l = new ArrayList<Noeud>();
        
        l.add(BVH);
        
        if(BVH.getB().intersect(r)){
            while(l.size()>0){
                Noeud current = l.remove(0);

                if(current.getFg()!=null){

                    if(current.getFg().getB().intersect(r)){
                        l.add(current.getFg());
                    }
                    if(current.getFd().getB().intersect(r)){
                        l.add(current.getFd());
                    }

                }else{ // ON A UNE FEUILLE

                    for (int j=current.getDebut();j<current.getFin();j++){
                        TriangleIndices triangle =(TriangleIndices) trianglesIndices.get(j);
                        Vector A = vertices.get(triangle.getVtxi());
                        Vector B = vertices.get(triangle.getVtxj());
                        Vector C =  vertices.get(triangle.getVtxk());

                        Vector e1 = Vector.soustraction(B,A);
                        Vector e2 = Vector.soustraction(C,A);
                        Vector N = Vector.cross(e1,e2);
                        Vector AO = Vector.soustraction(r.getC(),A);
                        Vector AOu = Vector.cross(AO,r.getU());
                        double invUN = 1/Vector.dot(r.getU(),N);
                        double beta = -Vector.dot(e2,AOu)*invUN;
                        double gamma = Vector.dot(e1,AOu)*invUN;
                        double alpha = 1 - beta-gamma; // COORDONNEES BARYCENTRIQUES POUR LISSER LES FACES
                        double localt= -Vector.dot(AO,N)*invUN;

                        if(beta>=0 && gamma>=0 && beta<=1 && gamma<=1 && alpha>=0 && localt>0 && localt<t){
                            data[0]=true;
                            data[1]=Vector.addition(
                                    r.getC(), 
                                    Vector.multiplication(localt,r.getU())
                            );// LOCAL P
                            
                            data[2] = Vector.addition(
                                    Vector.addition(
                                        Vector.multiplication(alpha,normals.get(triangle.getNi())),
                                        Vector.multiplication(beta,normals.get(triangle.getNj()))
                                    ),
                                    Vector.multiplication(gamma,normals.get(triangle.getNk()))
                                ).normalized();
                                    
                            
                            data[3]=localt; // LocalT
                            // AJOUT DE TEXTURES (NE MARCHE PAS)
                            //int textureId = faceGroup[i];
                            //int x =  (int)(Vector.multiplication(alpha,textures.get(triangle.getUvi())).getX()+Vector.multiplication(beta,textures.get(triangle.getUvj())).getX()+Vector.multiplication(gamma,textures.get(triangle.getUvk())).getX());
                            
                            //int y =  (int)(Vector.multiplication(alpha,textures.get(triangle.getUvi())).getY()+Vector.multiplication(beta,textures.get(triangle.getUvj())).getY()+Vector.multiplication(gamma,textures.get(triangle.getUvk())).getY());
                            
                            //data[4]= Vector.division(textures[textureId][y*w[textureId]+x],255; // LOCAL ALBEDO // COLOR

                            t = localt;
                        }
                    }
                }
            }
        }
        return data;
    }
    
    // RETOURNE LA TRANSLATION AU TEMPS SELECTIONNE
     public Vector getTranslation(double time) {
        return Vector.multiplication(time,maxTranslation);
    }
    
}
    


