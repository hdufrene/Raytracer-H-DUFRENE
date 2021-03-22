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
public class TriangleIndices {
    private int vtxi=-1, vtxj=-1, vtxk=-1; // indices des coordonnées des vecteurs
    private int uvi=-1, uvj=-1, uvk=-1;  // indices des coordonnées des textures
    private int ni=-1, nj=-1, nk=-1;  // indices des coordonnées des normales

    // CONSTRUCTEURS
    public TriangleIndices() {
    }
    public TriangleIndices(int vtxi, int vtxj, int vtxk){
        this.vtxi = vtxi;
        this.vtxj = vtxj;
        this.vtxk = vtxk;
    }
    public TriangleIndices(int vtxi, int vtxj, int vtxk,int ni,int nj,int nk){
        this(vtxi,vtxj,vtxk);
        this.ni=ni;
        this.nj=nj;
        this.nk=nk;
    }
    
    public TriangleIndices(int vtxi, int vtxj, int vtxk,int ni, int nj, int nk,int uvi, int uvj, int uvk) {
        this(vtxi,vtxj,vtxk,ni,nj,nk);
        this.uvi = uvi;
        this.uvj = uvj;
        this.uvk = uvk;
    }

    //GETTERS / SETTERS
    public int getVtxi() {
        return vtxi;
    }

    public void setVtxi(int vtxi) {
        this.vtxi = vtxi;
    }

    public int getVtxj() {
        return vtxj;
    }

    public void setVtxj(int vtxj) {
        this.vtxj = vtxj;
    }

    public int getVtxk() {
        return vtxk;
    }

    public void setVtxk(int vtxk) {
        this.vtxk = vtxk;
    }

    public int getUvi() {
        return uvi;
    }

    public void setUvi(int uvi) {
        this.uvi = uvi;
    }

    public int getUvj() {
        return uvj;
    }

    public void setUvj(int uvj) {
        this.uvj = uvj;
    }

    public int getUvk() {
        return uvk;
    }

    public void setUvk(int uvk) {
        this.uvk = uvk;
    }

    public int getNi() {
        return ni;
    }

    public void setNi(int ni) {
        this.ni = ni;
    }

    public int getNj() {
        return nj;
    }

    public void setNj(int nj) {
        this.nj = nj;
    }

    public int getNk() {
        return nk;
    }

    public void setNk(int nk) {
        this.nk = nk;
    }

    

}
