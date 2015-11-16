/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author carlos
 */
public class Matrix <T>{
    private String name;
    public static boolean debug=false;
    private ArrayList< ArrayList<T> > values;
    
    //constructor
    public Matrix() {
        this.name = "null";
        this.values = null;
    } 
    public Matrix(String name, ArrayList<ArrayList<T>> values) {
        this.name = name;
        this.values = values;
    }
    //setters y getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ArrayList<T>> getValues() {
        return values;
    }

    public ArrayList<ArrayList<Double>> getValuesInDoubleType() {
        ArrayList<ArrayList<Double>> temp=new ArrayList();
        for(ArrayList<T> row: values){
            temp.add(new ArrayList());
            for(T col: row)
                temp.get(temp.size()-1).add(Double.parseDouble(col.toString()));
        }
        return temp;
    }
    public ArrayList<ArrayList<String>> getValuesInStrHex(){
        ArrayList<ArrayList<String>> temp=new ArrayList();
        for(ArrayList<T> row: values){
            temp.add(new ArrayList());
            for(T col: row)
                temp.get(temp.size()-1).add(Integer.toHexString(
                                                    Integer.parseInt(col.toString())
                                            ));
        }
        return temp;
    }
    public void setValues(ArrayList<ArrayList<T>> values) {
        this.values = values;
    }
    //Clonación
    @Override
    public Matrix clone(){
        ArrayList< ArrayList<T> > clonedValues=new ArrayList();
        for(ArrayList<T> row : this.values){
            clonedValues.add(new ArrayList<T>());
            for(T col : row)
                clonedValues.get(clonedValues.size()-1).add(col);
        }
        return new Matrix(this.name,clonedValues);
    }
    //Parseadores
    public static Matrix<Integer> parseToIntegerMatrix(String str,String name){
        char []chrs=str.toCharArray();
        ArrayList<ArrayList<Integer>> temp=new ArrayList();
        
        for(int i=1;i<=chrs.length;i++){
            if(i!=2 && 4%i==0)
                temp.add(new ArrayList<Integer>());            
            temp.get(temp.size()-1).add((int) chrs[i-1]);
        }        
        return new Matrix(name,temp);
    }
    public static Matrix<Integer> parseToIntegerMatrix(ArrayList<ArrayList<Double>> list,String name){        
        ArrayList<ArrayList<Integer>> temp=new ArrayList();        
        for(ArrayList<Double> row:list){
            temp.add(new ArrayList<Integer>());
            for(Double col:row) 
                temp.get(temp.size()-1).add( round(col) );
            
        }
        return new Matrix(name,temp);
    }    
    
    public static Matrix<Integer> parseToIntegerMatrixFixed(String str,String name,int nrows,int ncols){
        
        char []chrs=str.toCharArray();
        ArrayList<ArrayList<Integer>> temp=new ArrayList();
        
        temp.add(new ArrayList<Integer>());
        for(int i=0;i<nrows;i++){
            if(i>=ncols && i%ncols==0)
                temp.add(new ArrayList<Integer>());            
            temp.get(temp.size()-1).add( ((i<chrs.length)?(int) chrs[i] : 0 ) );
        }
        if(debug)
         System.out.println(temp.toString());
        return new Matrix(name,temp);        
    }
    public static Matrix<Integer> parseToIntegerMatrixFixedFromStrHex(String list,String name,int nrows,int ncols,int jump){   
        char []chrs=list.toCharArray();
        String aux="";
        ArrayList<ArrayList<Integer>> temp=new ArrayList();
                temp.add(new ArrayList<Integer>());
        for(int i=0;i<nrows;i+=jump){
            if(i>=ncols && i%ncols==0){
                temp.add(new ArrayList<Integer>());
                System.out.print(aux);
            }
            if(i<chrs.length){ 
                aux="";
                for(int ii=0;ii<jump;ii++)
                   aux+=chrs[i+ii];
                temp.get(temp.size()-1).add( Integer.parseInt(aux,16) );
                System.out.print(aux);
            }else
                temp.get(temp.size()-1).add( 0 );
            //temp.get(temp.size()-1).add( ((i<chrs.length)?Integer.parseInt(chrs[i]+""+chrs[i+1]+""+chrs[i+2]+""+chrs[i+3], 16)  : 0 ) );
            //System.out.print(((i<chrs.length)?Integer.parseInt(chrs[i]+""+chrs[i+1]+""+chrs[i+2]+""+chrs[i+3], 16)+" "  : "0 " ));   
        }
        if(debug)
            System.out.println("\n"+temp.toString());        
        return new Matrix(name,temp);        
    }
    //Números grandes      
        //parseadores
        public static Matrix<BigInteger> parseToBigIntegerMatrixFixed(String str,String name,int nrows,int ncols){

            char []chrs=str.toCharArray();
            ArrayList<ArrayList<BigInteger>> temp=new ArrayList();

            temp.add(new ArrayList<BigInteger>());
            for(int i=0;i<nrows;i++){
                if(i>=ncols && i%ncols==0)
                    temp.add(new ArrayList<BigInteger>());            
                temp.get(temp.size()-1).add( ((i<chrs.length)?new BigInteger( ((int)chrs[i])+"") : BigInteger.ZERO ) );
            }        
            if(debug)
             System.out.println(temp.toString());
            return new Matrix(name,temp);        
        }
        public static Matrix<BigInteger> parseToBigIntegerMatrix(ArrayList<ArrayList<BigDecimal>> list,String name){
            ArrayList<ArrayList<BigInteger>> temp=new ArrayList();        
            for(ArrayList<BigDecimal> row:list){
                temp.add(new ArrayList<BigInteger>());
                for(BigDecimal col:row) 
                    temp.get(temp.size()-1).add( round(col) );

            }
            return new Matrix(name,temp);
        }
        public static Matrix<BigInteger> parseToBigIntegerMatrixFixedFromStrHex(String list,String name,int nrows,int ncols,int jump){
           char []chrs=list.toCharArray();
           String aux="";
           ArrayList<ArrayList<BigInteger>> temp=new ArrayList();
                   temp.add(new ArrayList<BigInteger>());
           for(int i=0;i<nrows;i+=jump){
               if(i>=ncols && i%ncols==0){
                   temp.add(new ArrayList<BigInteger>());
                   System.out.print(aux);
               }
               if(i<chrs.length){ 
                   aux="";
                   for(int ii=0;ii<jump;ii++)
                      aux+=chrs[i+ii];
                   temp.get(temp.size()-1).add( new BigInteger(aux,16) );
                   //System.out.print(aux);
               }else
                   temp.get(temp.size()-1).add( BigInteger.ZERO );
            }
           if(debug)
               System.out.println("\n"+temp.toString());        
           return new Matrix(name,temp);        
       }
       public ArrayList<ArrayList<String>> getValuesInStrBigHex(){
            ArrayList<ArrayList<String>> temp=new ArrayList();
            for(ArrayList<T> row: values){
                temp.add(new ArrayList());
                for(T col: row)
                    temp.get(temp.size()-1).add( (new BigInteger(col.toString())).toString(16) );
            }
            return temp;
        }
        public ArrayList<ArrayList<BigDecimal>> getValuesInBigDecimalType() {
           ArrayList<ArrayList<BigDecimal>> temp=new ArrayList();
           for(ArrayList<T> row: values){
               temp.add(new ArrayList());
               for(T col: row)
                   temp.get(temp.size()-1).add(new BigDecimal(col.toString()));
           }
           return temp;
        }
        
        public Matrix<BigDecimal> getValuesAsBigDecimalMatrix() {           
           return this.getValuesAsBigDecimalMatrix(this.name);
        }
        
        public Matrix<BigDecimal> getValuesAsBigDecimalMatrix(String name) {
           ArrayList<ArrayList<BigDecimal>> temp=new ArrayList();
           for(ArrayList<T> row: values){
               temp.add(new ArrayList());
               for(T col: row)
                   temp.get(temp.size()-1).add(new BigDecimal(col.toString()));
           }
           return new Matrix(name,temp);
        }
        
       
    //función de redondeo
    private static int round(double d){
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result<0.5){
            return d<0 ? -i : i;            
        }else{
            return d<0 ? -(i+1) : i+1;          
        }
    }
    
    private static BigInteger round(BigDecimal d){        
        BigInteger i = d.toBigInteger();
        BigDecimal result = d.subtract(new BigDecimal(i) );
        if(result.compareTo(new BigDecimal("0.5"))<0){
            return i;
        }else{
            return i.add(BigInteger.ONE);  
        }
    }
    //as
     //crear un solo elemento con todos los elementos
    public String asOneElement(){
        String str="";
        for(ArrayList<T> row: this.values)
            for(T col: row)
                str+=col;        
        return str;
    }
    
}
