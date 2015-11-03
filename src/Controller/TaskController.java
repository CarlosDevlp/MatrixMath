/*
 Clase que se encarga de controlar las tareas de cálculo se piden desde el view
 */
package Controller;

import java.util.*;
import Model.*;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author carlos
 */
public class TaskController {
     private String [][]matrixFunctionsList;     
     private JTextArea recommenderPointer;
     private JTextField iterationsFieldPointer;
     private JLabel iterationsLabelPointer;
     private ArrayList<  Matrix > matrixList;
     private Vector<String> matrixNamesList;
     //constructor
     public TaskController(){         
         this.matrixFunctionsList= new String[][]{
                                                  new String[]{"Multiplicar","Sumar","Determinante","Inversa","Sist.Ec.Jacobi","Sist.Ec.Gauss Seidel"},
                                                  new String[]{"Multiplicar: agrege 2 matrices, fíjese que el número de columnas del primero sea igual que al número de filas del segundo",
                                                               "Sumar: agrege 2 matrices y que tengan mismas dimensiones",
                                                               "Determinante: asegurese que la matriz sea cuadrada",
                                                               "Inversa: asegurese que la matriz sea cuadrada",
                                                               "Sist.Ec.Jacobi: agrege 2 matrices, la primera que sea la matriz de coeficientes(x,y,z...) y la matriz que se obtiene de los valores que iguala cada ecuación",
                                                               "Sist.Ec.Gauss Seidel: agrege 2 matrices, la primera que sea la matriz de coeficientes(x,y,z...) y la matriz que se obtiene de los valores que iguala cada ecuación"},
                                                };
         this.matrixList=new ArrayList();
         this.matrixNamesList=new Vector();
         //debug
         SystemEquations.setDebug(true);
     }
    //parser
    public ArrayList< ArrayList<Integer> > tableToArray(TableModel table) throws Exception{
        ArrayList< ArrayList<Integer> > temp=new ArrayList();
        ArrayList<Integer> aux;
        DefaultTableModel dtm = (DefaultTableModel)table;
        
        
        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount(),num;
        
        for (int i = 0 ; i < nRow ; i++){                        
            aux=new ArrayList();
            
            for (int j = 0 ; j < nCol ; j++)                
                try{
                    num=Integer.parseInt(dtm.getValueAt(i,j)+"");
                    aux.add(num);
                }catch(Exception err){break;}
            
            
                if(!aux.isEmpty()){
                    temp.add(aux);                     
                }
        }
        if(temp.isEmpty())
            throw new Exception("La matriz está vacía");
        return temp;
    }    
    public Object[][] arrayToTable(ArrayList< ArrayList<Integer> > array){
        Object[][] temp=new Object[array.size()][array.get(0).size()]; 
        for(int row=0;row<array.size();row++)
            for(int col=0;col<array.get(0).size();col++)
             temp[row][col]=array.get(row).get(col);        
        return temp;
    }
    public Object[][] arrayToTableFixed(ArrayList< ArrayList<Integer> > array,int ncols,int nrows){
        Object[][] temp=new Object[nrows][ncols];        
        for(int row=0;row<nrows;row++)
            for(int col=0;col<ncols;col++)                
                   temp[row][col]=((row<array.size() && col<array.get(0).size())?array.get(row).get(col):"");
        return temp;
    }
    
    //función para hacer tareas (en este caso operaciones matemáticas con matrices)
    public Object[][] doTask(int option) throws Exception{
        Object[][] result;
        ArrayList<ArrayList<Double>> aux1=new ArrayList();
        ArrayList<ArrayList<Integer>> aux2=new ArrayList();
        
        switch(option){
            case 0://Multiplicar
               try{
               if(matrixList.size()<2)
                      throw new Exception("Deben haber 2 matrices como mínimo en la lista de matrices para proceder a calcular.");
                   aux1=MatrixOperations.multiply(matrixList.get(0).getValuesInDoubleType(), matrixList.get(1).getValuesInDoubleType());
                   
               }catch(Exception err){
                   throw new Exception(err);
               }
                break;
            case 1://Sumar                
                if(matrixList.size()<2)
                      throw new Exception("Deben haber 2 matrices como mínimo en la lista de matrices para proceder a calcular.");                                     
                     ArrayList<ArrayList<Integer>> temp=matrixList.get(0).clone().getValues();
                     
                     //inicializar la variable temporal
                     for(int i=0;i<temp.size();i++)
                         for(int j=0;j<temp.get(0).size();j++)
                               temp.get(i).set(j, 0);
                     
                     for(Matrix m:matrixList)//Sumatoria de todas las matrices existentes en la lista
                      temp=MatrixOperations.plus(temp,m.getValues());
                     aux2=temp;
                break;
            case 2://Determinante
                if(matrixList.size()<1)
                      throw new Exception("Deben haber 1 matriz para poder hallar la determinante");
                
                    aux2.add(new ArrayList<Integer>());
                    aux2.get(0).add(MatrixDeterminant.laplace(matrixList.get(0).getValues()));                
                break;
            case 3://Inversa
                if(matrixList.size()<1)
                     throw new Exception("Deben haber 1 matriz para poder hallar la inversa");
                aux1=MatrixTypes.inverse(matrixList.get(0).getValues());
                recommenderPointer.append(MatrixTypes.getDebugStr());
                break;
            case 4://Sist.Ec.Jacobi
                if(matrixList.size()<2)
                     throw new Exception("Deben haber 2 matrices para poder resolver un sistema de ecuaciones");               
                    aux1=SystemEquations.Jacobi(matrixList.get(0).getValues(), matrixList.get(1).getValues(),((this.iterationsFieldPointer.getText().compareTo("")!=0) ? Integer.parseInt(this.iterationsFieldPointer.getText()) : SystemEquations.Iterations.DEPENDSOFERROR));
                recommenderPointer.append(SystemEquations.getDebugStr());
                break;
            case 5://Sist.Ec.Gauss Seidel
                if(matrixList.size()<2)
                     throw new Exception("Deben haber 2 matrices para poder resolver un sistema de ecuaciones");
                aux1=SystemEquations.GaussSeidel(matrixList.get(0).getValues(), matrixList.get(1).getValues(), ((this.iterationsFieldPointer.getText().compareTo("")!=0) ? Integer.parseInt(this.iterationsFieldPointer.getText()) : SystemEquations.Iterations.DEPENDSOFERROR));
                recommenderPointer.append(SystemEquations.getDebugStr());
                break;
        }
       
        
       if(!aux1.isEmpty()){
           //System.out.println(aux1);
        result=new Object[aux1.size()][aux1.get(0).size()];       
        for(int row=0;row<aux1.size();row++)
            for(int col=0;col<aux1.get(0).size();col++)
            result[row][col]=aux1.get(row).get(col);
       }else{
          //System.out.println(aux2);
        result=new Object[aux2.size()][aux2.get(0).size()]; 
        for(int row=0;row<aux2.size();row++)
            for(int col=0;col<aux2.get(0).size();col++)
            result[row][col]=aux2.get(row).get(col);
       
       }
       return result;
    }
    //setters and getters
    public String[] getMatrixFunctionsList() {
        return matrixFunctionsList[0];
    }
    //recibir recomendaciones de cada opción antes de realizar el cálculo
    public String getMatrixFunctionSuggestion(int index) {
        return matrixFunctionsList[1][index];
    }
    public void setMatrixFunctionsList(String[] matrixFunctionsList) {
        this.matrixFunctionsList[0] = matrixFunctionsList;
    }
    public ArrayList<Matrix> getMatrixList() {
        return matrixList;
    }
    public Matrix getMatrixAt(int index) {
        return matrixList.get(index);
    }
    public void removeMatrixAt(int index) {
        this.matrixList.remove(index);
    }
    public void removeAllMatrixList() {
        this.matrixList.clear();
    }
    public void setMatrixList(ArrayList<Matrix> matrixList) {
        this.matrixList = matrixList;
    }
    public Vector<String> getMatrixNamesList() {
        return matrixNamesList;
    }
    public void setMatrixNamesList(Vector<String> matrixNamesList) {
        this.matrixNamesList = matrixNamesList;
    }

    public  JTextArea getRecommenderPointer() {
        return recommenderPointer;
    }

    public void setRecommenderPointer(JTextArea recommenderPointer) {
        this.recommenderPointer = recommenderPointer;
    }

    public JTextField getIterationsFieldPointer() {
        return iterationsFieldPointer;
    }

    public void setIterationsFieldPointer(JTextField iterationsFieldPointer) {
        this.iterationsFieldPointer = iterationsFieldPointer;
    }

    public JLabel getIterationsLabelPointer() {
        return iterationsLabelPointer;
    }

    public void setIterationsLabelPointer(JLabel iterationsLabelPointer) {
        this.iterationsLabelPointer = iterationsLabelPointer;
    }
    
    //add
    public void addMatrix(String name,TableModel table) throws Exception {
        if(name.trim().compareTo("")==0)
               throw new Exception("Se debe agregar un nombre válido");
        try{            
            this.matrixList.add(new Matrix<Integer>(name,this.tableToArray(table)));            
            this.matrixNamesList.addElement(name);
        }catch(Exception err){
            throw new Exception("La matriz agregada a sido inválida");
        }
    }
     
     
}

