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
public class Matrix <T>{
    private String name;
    private ArrayList< ArrayList<T> > values;
    
    //constructor
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
    
    
}
