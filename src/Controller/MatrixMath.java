/*
  ---MatrixMath---
  Software matemático para resolución de problemas con matrices
 */
package Controller;
import java.util.*;
import Model.*;
/**
 *
 * @author carlos chavez laguna
 */
public class MatrixMath {

    /**
     * @param args the command line arguments
     * @return 
     */
    
     /*
        ejem1
        17,-2,-3;-5,21,-2;-5,-5,22
        500;200;30
        
        ejem2
        10,1,2;4,6,-1;-2,3,8
        3;9;51
    
        ejem3
        3,1;-1,5
        1;7
    
        ejem4
        [-1,2,-1;0,-1,2;2,-1,0]
        [4;14;124]
        */    
    public static void main(String[] args) {
        ArrayList< ArrayList<Integer> > matrix,equivalences;
        
        Scanner scanf=new Scanner(System.in);
        String text;
        try{ 
            //ingresando una matriz
            System.out.println("Ingrese su matriz de coeficientes:");
            text=scanf.nextLine();
            matrix=MatrixOperations.parseToMatrix(text);
            System.out.println(matrix);
            System.out.println("Ingrese su matriz con los valores equivalentes:");
            text=scanf.nextLine();
            equivalences=MatrixOperations.parseToMatrix(text);
            System.out.println(equivalences);
            
                    
            SystemEquations.setDebug(true);
            MatrixTypes.setDebug(true);
            //resultado de jacobi            
            System.out.println("Resultados con jacobi: "+SystemEquations.Jacobi(matrix,equivalences,8));
            //System.out.println("Resultados con gauss seidel: "+SystemEquations.GaussSeidel(matrix,equivalences,SystemEquations.Iterations.DEPENDSOFERROR));
            
        }catch(Exception err){
            System.out.println(err);
        }
        
    }
    
}