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
abstract public class MatrixTypes {
    public static boolean debug=false;
    //Función para hallar la inversa de una matriz
    public static ArrayList< ArrayList<Double> >  inverse(ArrayList< ArrayList<Integer> > matrix) throws Exception{
        ArrayList< ArrayList<Double> >  inv=new ArrayList();
        ArrayList< ArrayList<Integer> > adj=new ArrayList(),trans=new ArrayList(),subMatrix;
        int det, ndim=matrix.size();
        det=MatrixDeterminant.laplace(matrix);
                
        //transpuesta
        for(int row=0;row<ndim;row++){
            trans.add(new ArrayList<Integer>());
            for(int col=0;col<ndim;col++)
                trans.get(row).add(matrix.get(col).get(row));
        }
        
        //adjunta e inversa
        double adjElement,detInv=1/(double)det; //adjElement: elemento que se obtuvo de una determinante, para la adjunta. detInv: la inversa de la determinante
        if(ndim==2)
              for(int row=0;row<ndim;row++){
                //agregar fila a ajdunta
                adj.add(new ArrayList<Integer>());
                //agregar fila a inversa
                inv.add(new ArrayList<Double>());                
                for(int col=0;col<ndim;col++){
                    adjElement=((row==col)?1*matrix.get(1-row).get(1-col):-1*matrix.get(row).get(col));
                    adj.get(adj.size()-1).add((int)adjElement);
                    inv.get(adj.size()-1).add(detInv*adjElement);
                }
            }
        else
            for(int row=0;row<ndim;row++){
                //agregar fila a ajdunta
                adj.add(new ArrayList<Integer>());
                //agregar fila a inversa
                inv.add(new ArrayList<Double>());
                for(int col=0;col<ndim;col++){
                    //generar submatriz para hallar su determinante
                    subMatrix=new ArrayList();
                    for(int srow=0;srow<ndim;srow++){
                        if(srow!=row){                        
                        subMatrix.add(new ArrayList<Integer>());
                            for(int scol=0;scol<ndim;scol++)
                                if(scol!=col)
                                    subMatrix.get(subMatrix.size()-1).add(trans.get(srow).get(scol));
                        }
                    }
                     adjElement=(double)( Math.pow(-1,row+col+2)*MatrixDeterminant.laplace(subMatrix) );

                    //agregar columna a adjunta
                     adj.get(row).add((int) adjElement); //matriz adjunta
                    //agregar columna a inversa
                     inv.get(row).add(detInv*adjElement); //matriz inversa

                }
            }
        
        //debug
        if(debug){
         System.out.println("Transpuesta: "+trans);
         System.out.println("Adjunta: "+adj);
        }
        
       return inv;
    }
    //getters and setters
    public static boolean isDebug() {
        return debug;
    }
    public static void setDebug(boolean debug) {
        SystemEquations.debug = debug;
    }
}
