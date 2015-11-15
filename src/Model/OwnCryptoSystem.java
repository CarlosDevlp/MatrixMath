/*
 * Clase Criptosistema "Own"
 
 */
package Model;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author carlos
 */
abstract public class OwnCryptoSystem {
    private static boolean debug=false;
    private static String debugStr="";
    private static Integer rounds=2;
    //Encriptar
    public static String encrypt(String plainText,String key) throws Exception{
        //evaluar la llave
        String cipherText="";        
        Matrix.debug=true;
        Matrix<Integer> m= Matrix.parseToIntegerMatrixFixed(plainText, "plain-text",16,4), k=Matrix.parseToIntegerMatrixFixed(key, "key",16,4);
            if(MatrixDeterminant.laplace(k.getValues())==0)
                throw new Exception("La llave privada carece de inversa");            
        Matrix<Integer> c=m;
        
        //comenzando a encriptar        
            //rondas
            for(int i=0;i<rounds;i++)
                c= Matrix.parseToIntegerMatrix(MatrixOperations.multiply(c.getValuesInDoubleType(), k.getValuesInDoubleType()), "cipher-text");
        ArrayList<ArrayList<String>> temp=c.getValuesInStrHex(); 
        
        String aux="";
        System.out.println(c.getValuesInStrHex());
        for(ArrayList<String> row: temp)
            for(String col: row){
                aux="";
                for(int i=0;i<(rounds*4)-col.length();i++)                    
                        aux+="0";
                cipherText+=aux+col;
            }
        return cipherText;
    }
    
    //Desencriptar
    public static String decrypt(String cipherText,String key) throws Exception{
        String plainText="";
        Matrix.debug=true;
        Matrix<Integer> c= Matrix.parseToIntegerMatrixFixedFromStrHex(cipherText, "plain-text",64*rounds,16*rounds,4*rounds), k=Matrix.parseToIntegerMatrixFixed(key, "key",16,4);
        Matrix<Double> invK=new Matrix("k-inverse",MatrixTypes.inverse(k.getValues()));                
        Matrix<Integer> m= c;
        System.out.println(c.getValuesInStrHex());
        for(int i=0;i<rounds;i++)
                m= Matrix.parseToIntegerMatrix(MatrixOperations.multiply( m.getValuesInDoubleType(), invK.getValues()), "cipher-text");        
        if(debug){        
            System.out.println(m.getValues());
            System.out.println(MatrixOperations.multiply( c.getValuesInDoubleType(), invK.getValues()));
        }
        
        for(ArrayList<Integer> row: m.getValues())
            for(Integer col: row)
                plainText+=((char)((int)col));
        return plainText;
    }
    
}

/*
//Encriptar
    public static String encrypt(String plainText,String key) throws Exception{
        //evaluar la llave
        String cipherText="";        
        Matrix<Integer> m= Matrix.parseToIntegerMatrixFixed(plainText, "plain-text",16,4), k=Matrix.parseToIntegerMatrixFixed(key, "key",16,4);
            if(MatrixDeterminant.laplace(k.getValues())==0)
                throw new Exception("La llave privada carece de inversa");            
              
        //comenzando a encriptar
        Matrix<Integer> c= Matrix.parseToIntegerMatrix(MatrixOperations.multiply(m.getValuesInDoubleType(), k.getValuesInDoubleType()), "cipher-text");                
        ArrayList<ArrayList<String>> temp=c.getValuesInStrHex();
        for(ArrayList<String> row: temp)
            for(String col: row)
                cipherText+=col;
        return cipherText;
    }
    
    //Desencriptar
    public static String decrypt(String cipherText,String key) throws Exception{
        String plainText="";
        Matrix<Integer> c= Matrix.parseToIntegerMatrixFixedFromStrHex(cipherText, "plain-text",64,16,4), k=Matrix.parseToIntegerMatrixFixed(key, "key",16,4);
        Matrix<Double> invK=new Matrix("",MatrixTypes.inverse(k.getValues()));                
        Matrix<Integer> m= Matrix.parseToIntegerMatrix(MatrixOperations.multiply( c.getValuesInDoubleType(), invK.getValues()), "cipher-text");
        System.out.println(m.getValues());        
        System.out.println(MatrixOperations.multiply( c.getValuesInDoubleType(), invK.getValues()));
        for(ArrayList<Integer> row: m.getValues())
            for(Integer col: row)
                plainText+=((char)((int)col));
        return plainText;
    }
*/