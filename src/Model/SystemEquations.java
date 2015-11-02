/*
 *Clase con funciones para resolver sistemas de ecuaciones
 */
package Model;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author carlos
 */
abstract public class SystemEquations {
       private static boolean debug=false;
       private static DecimalFormat df = new DecimalFormat("#.####");
       public static abstract class Iterations{//alternativa a enum
           public static final int DEPENDSOFERROR=-1,ONCE=1,TWICE=2;           
       };
       
       
       //Sistema de ecuaciones con jacobi  
       /*
          k+1   -1       -1          k
         X   = D *  B + D * (L + U)* X  
       */
       public static ArrayList< ArrayList<Double> > Jacobi(ArrayList< ArrayList<Integer> > coefficientMatrix, ArrayList< ArrayList<Integer> > equivalentMatrix,int iterations) throws Exception{
        
           if(coefficientMatrix.get(0).size()!=equivalentMatrix.size())
            throw new Exception("En Jacobi, El número de columnas de la matriz de coeficientes debe ser igual al número de filas de la matriz equivalente");
           else if(iterations<-1)
            throw new Exception("En Jacobi, El número iteraciones debe ser válido");       
        ArrayList< ArrayList<Integer> > A=coefficientMatrix,D= new ArrayList(),L= new ArrayList(),U= new ArrayList();
        ArrayList< ArrayList<Double> > B=new ArrayList(),UL=new ArrayList(),result=new ArrayList();
        
        int ndim=coefficientMatrix.size();//el tamaño de la matriz de coeficientes
         
        //inicializar la matriz de resultados o matriz X^k        
        for(int i=0;i<ndim;i++){
            result.add(new ArrayList<Double>());
            result.get(i).add(0.0);
        }        
        //convertir, la matriz de valores equivalentes, de valores Integer a Double
        for(ArrayList<Integer> row: equivalentMatrix){
                B.add(new ArrayList<Double>());
                    for(Integer col: row)
                        B.get(B.size()-1).add((double)col);
        }
        
        //suponemos que la matriz es cuadrada        
        //Diagonal
        int nrow=0,ncol;        
        for (ArrayList<Integer> row : A){
            ncol=0;
            D.add(new ArrayList<Integer>());
            for (Integer col :row){                 
                 D.get(nrow).add(((ncol==nrow)? col:0));
                 ncol++;
            }                                    
            nrow++;        
        }
        //Triangulo inferior sin diagonal y cambiado de signo
        nrow=0;
        for (ArrayList<Integer> row : A){
            ncol=0;
            L.add(new ArrayList<Integer>());
            for (Integer col :row){                 
                 L.get(nrow).add(((ncol<nrow)? -1*col:0));
                 ncol++;
            }                                    
            nrow++;        
        }    
        //Triangulo superior sin diagonal y cambiado de signo
        nrow=0;
        for (ArrayList<Integer> row : A){
            ncol=0;
            U.add(new ArrayList<Integer>());
            for (Integer col :row){                 
                 U.get(nrow).add(((ncol>nrow)? -1*col:0));
                 ncol++;
            }      
            nrow++;        
        }
        
        //antes de implementar jacobi        
            //inversa de la diagonal
            ArrayList< ArrayList<Double> > invD;            
            invD=MatrixTypes.inverse(D);
            //suma de la matriz U y L            
            for(int row=0;row<ndim;row++){
                UL.add(new ArrayList());
                for(int col=0;col<ndim;col++)
                    UL.get(row).add((double)(U.get(row).get(col)+L.get(row).get(col)));
            }
         
        ArrayList< ArrayList<Double> > BinvD,ULinvD,ULXinvD;
        BinvD=MatrixOperations.multiply(invD,B);
        ULinvD=MatrixOperations.multiply(invD,UL);
        //error aproximado porcentual
         double err,n=0,minErr=100;
       //seria los valores previos del resultado
         ArrayList< ArrayList<Double> > prevResult=new ArrayList();
        String debugStr;
        //calcular soluciones mediante iteraciones
        if(debug)//debug
            System.out.println("---Sistema de ecuaciones por Jacobi---\n");
                   
            //inicializar la matriz de resultados previos
            for(int i=0; i<ndim; i++){
                prevResult.add(new ArrayList());
                prevResult.get(i).add(0.0);
            }  
            int i=0;
            do{//repetir hasta que el error sea menor que 0.5
              if(iterations!=-1 && i>(iterations-1))
                     break;
              ULXinvD=MatrixOperations.multiply(ULinvD,result);
              debugStr="";
                for(int row=0;row<ndim;row++){
                    result.get(row).set(0,BinvD.get(row).get(0)+ULXinvD.get(row).get(0));
                    err=Math.abs( (result.get(row).get(0)-prevResult.get(row).get(0)) / result.get(row).get(0) )*100;
                    if(debug)//debug
                        debugStr+="\tA"+row+":\t"+df.format(result.get(row).get(0))+"\tA"+row+"ε:\t"+df.format(err);
                    if(minErr>err)
                        minErr=err;
                }
                    
              //copiar el contenido del resultado nuevo al resultado previo  
              for(int j=0;j<ndim;j++) 
                  prevResult.get(j).set(0,result.get(j).get(0));
              i++;
              if(debug)//debug
                    System.out.println("Iteración("+i+"): "+debugStr);
            }while(minErr>0.5);                   
       return result;
    }
       
    //sistema de ecuaciones con Gauss seidel
       /*
          k+1   -1       -1     k
         X   = N *  B + N * P* X  
       */
    public static ArrayList< ArrayList<Double> > GaussSeidel(ArrayList< ArrayList<Integer> > coefficientMatrix, ArrayList< ArrayList<Integer> > equivalentMatrix,int iterations) throws Exception{
         if(coefficientMatrix.get(0).size()!=equivalentMatrix.size())
            throw new Exception("En GaussSeidel,El número de columnas de la matriz de coeficientes debe ser igual al número de filas de la matriz equivalente");         
         else if(iterations<-1)
            throw new Exception("En GaussSeidel, El número iteraciones debe ser válido");       
         ArrayList< ArrayList<Integer> > A=coefficientMatrix,N=new ArrayList();
         ArrayList< ArrayList<Double> > result=new ArrayList(),B=new ArrayList(),P=new ArrayList();
        int ndim=coefficientMatrix.size();//el tamaño de la matriz de coeficientes
        
        //inicializar la matriz de resultados o matriz X^k        
        for(int i=0;i<ndim;i++){
            result.add(new ArrayList<Double>());
            result.get(i).add(0.0);
        }
        //convertir, la matriz de valores equivalentes, de valores Integer a Double
        for(ArrayList<Integer> row: equivalentMatrix){
                B.add(new ArrayList<Double>());
                    for(Integer col: row)
                        B.get(B.size()-1).add((double)col);
        }
        //triangulo superior
        int nrow=0,ncol;
        for (ArrayList<Integer> row : A){
            ncol=0;
            N.add(new ArrayList<Integer>());
            for (Integer col :row){                 
                 N.get(nrow).add(((ncol>=nrow)? col:0));
                 ncol++;
            }      
            nrow++;        
        }
        //triangulo inferior sin diagonal
        nrow=0;
        for (ArrayList<Integer> row : A){
            ncol=0;
            P.add(new ArrayList<Double>());
            for (Integer col :row){                 
                 P.get(nrow).add((double)((ncol<nrow)? -1*col:0));
                 ncol++;
            }      
            nrow++;    
        }
        //antes de comenzar a iterar
            //N^⁻1
             ArrayList< ArrayList<Double> > invN=MatrixTypes.inverse(N);
            //N^⁻1 * B
             ArrayList< ArrayList<Double> > BinvN=MatrixOperations.multiply(invN, B);
            //N^⁻1 * P
             ArrayList< ArrayList<Double> > PinvN=MatrixOperations.multiply(invN, P);
            //N^⁻1 * P*X^k
             ArrayList< ArrayList<Double> > PXinvN;
            //error aproximado porcentual
            double err,minErr=100;
            //Matriz con valores previos de la matriz resultado
            ArrayList< ArrayList<Double> > prevResult=new ArrayList();
            //inicializar la matriz de resultados previos
                for(int i=0;i<ndim;i++){
                    prevResult.add(new ArrayList());
                    prevResult.get(i).add(0.0);
                }
        //comenzando a calcular las soluciones mediante iteración
            if(debug)//debug
                System.out.println("---Sistema de ecuaciones por Gauss Seidel---\n");
                          
                int i=0;
                String debugStr;
                do{//repetir hasta que el error sea menor que 0.5
                  
                    if(iterations!=-1 && i>(iterations-1))
                      break;
                  debugStr="";
                  PXinvN=MatrixOperations.multiply(PinvN,result);
                    for(int row=0;row<ndim;row++){
                        result.get(row).set(0,BinvN.get(row).get(0)+PXinvN.get(row).get(0));
                        err=Math.abs( (result.get(row).get(0)-prevResult.get(row).get(0)) / result.get(row).get(0) )*100;
                         if(debug)//debug
                            debugStr+="\tA"+row+":\t"+df.format(result.get(row).get(0))+"\tA"+row+"ε:\t"+df.format(err);
                        if(minErr>err)
                            minErr=err;
                    }                                      
                  //copiar el contenido del resultado nuevo al resultado previo  
                  for(int j=0;j<ndim;j++)   
                      prevResult.get(j).set(0,result.get(j).get(0));
                  
                  i++;
                  if(debug)//debug
                    System.out.println("Iteración("+i+"): "+debugStr);
                }while(minErr>0.5); 
        
        return result;
    }
    //getters and setters
    public static boolean isDebug() {
        return debug;
    }
    public static void setDebug(boolean debugx) {
        debug = debugx;
    }
       
}
