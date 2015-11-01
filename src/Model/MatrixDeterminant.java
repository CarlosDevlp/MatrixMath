/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author carlos
 */
abstract public class MatrixDeterminant {
    //Función para hallar la determinante de la matriz
    public static int laplace(ArrayList< ArrayList<Integer> > matrix) throws Exception{
        if(matrix.size()!=matrix.get(0).size())
           throw new Exception("La matriz debe ser cuadrada para hallar su inversa.");
        int det=0;
        ArrayList< ArrayList<Integer> > subMatrix;
        int ndim=matrix.size(); //ndim= número de columnas y filas que tiene la matriz actual
        //System.out.println(matrix);
        
        if(ndim==2){//Para una matriz cuadrada de orden 2
            int d=1,d2=1;//d=diagonal principal, d2=diagonal secundaria
            for(int row=0;row<2;row++)
                for(int col=0;col<2;col++)
                            if(row==col)
                             d*=matrix.get(row).get(col);
                            else
                             d2*=matrix.get(row).get(col);
            det=(d-d2);
        }
        else{//Para una matriz cuadrada de orden n>2
          int row=0;
            for(int col=0;col<ndim;col++){ 
                
                //generar una nueva matriz para buscar su determinante
                subMatrix=new ArrayList();
                for(int srow=0;srow<ndim;srow++){
                    if(srow!=row){
                        subMatrix.add(new ArrayList<Integer>());
                        for(int scol=0;scol<ndim;scol++)
                            if(scol!=col)
                                subMatrix.get(subMatrix.size()-1).add(matrix.get(srow).get(scol));
                    }
                }
                
                det+=matrix.get(row).get(col)*Math.pow(-1,row+col+2)*laplace(subMatrix);
            }
        }
                        
        return det;        
    }
    
}
