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
abstract public class MatrixOperations {
    //crear una matriz
    public static ArrayList< ArrayList<Integer> > parseToMatrix(String plainText){
        ArrayList< ArrayList<Integer> > matrix=new ArrayList();
        String[] rows;
        
        if(plainText.charAt(0)=='[' && plainText.charAt(plainText.length()-1)==']'){
           plainText=plainText.replace('[','0');
           plainText=plainText.split("]")[0];
        }

        //crear una matriz con la sentencia introducida
        rows=plainText.split(";");
        
        for(String row:rows){
            matrix.add(new ArrayList<Integer>());
                for(String col: row.split(","))
                    matrix.get(matrix.size()-1).add(Integer.parseInt(col));
        }                        
       return matrix;
    }
    //Función para multiplicar matrices
    //M1(mxn)*M2(nxp)=R(mxp)
    public static ArrayList< ArrayList<Double> > multiply(ArrayList< ArrayList<Double> > matrixOne,ArrayList< ArrayList<Double> > matrixTwo) throws Exception{ 
        if(matrixOne.get(0).size()!=matrixTwo.size())
            throw new Exception("Para multiplicar matrices, el número de columnas de la primera matriz debe ser igual al de filas de la segunda matriz.");
        
        ArrayList< ArrayList<Double> > result=new ArrayList();
        int n=matrixTwo.size();//número de columnas de ambas matrices
        int m=matrixOne.size(),p=matrixTwo.get(0).size();
        
        //multiplicando las matrices        
        Double sum;
            for(int i=0;i<m;i++){//filas de la 1era matriz
                result.add(new ArrayList());//nueva fila para la matriz resultante
                for(int j=0;j<p;j++){//columnas de la 2da matriz
                    sum=0.0;                    
                    //multiplicación
                    for(int k=0;k<n;k++)
                        sum+=matrixOne.get(i).get(k)*matrixTwo.get(k).get(j);
                    
                  result.get(i).add(sum);//nueva columna para la matriz resultante
                }
            }                                             
        return result;
    }
    //Función para sumar matrices
    //M1(mxn)+M2(mxn)=R(mxn)
    public static ArrayList< ArrayList<Double> > plus(ArrayList< ArrayList<Double> > matrixOne,ArrayList< ArrayList<Double> > matrixTwo) throws Exception{
        if(matrixOne.size()==matrixTwo.size() && matrixOne.get(0).size()==matrixTwo.get(0).size())
           throw new Exception("En la suma de matrices, ambas matrices deben tener la misma dimensión");       
        ArrayList< ArrayList<Double> > result=matrixOne;
            for(int row=0;row<matrixTwo.size();row++)
                for(int col=0;col<matrixTwo.get(0).size();col++)
                    result.get(row).set(col, matrixOne.get(row).get(col) +  matrixTwo.get(row).get(col) );            
        return result;
    }
    
}
