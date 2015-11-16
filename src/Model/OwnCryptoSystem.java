/*
 * Clase Criptosistema "Own"
 
 */
package Model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author carlos
 */
abstract public class OwnCryptoSystem {
    public static boolean debug=false;
    public static String debugStr="";
    private static Integer rounds=7;
    
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
        
        String aux;
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
    
    //números grandes
      //Encriptar
        public static String bigEncrypt(String plainText,String key) throws Exception{
            //evaluar la llave
            String cipherText=""; debugStr="";      
            Matrix.debug=true;

            Matrix<BigInteger> m= Matrix.parseToBigIntegerMatrixFixed(plainText, "plain-text",16,4), 
                               k= Matrix.parseToBigIntegerMatrixFixed(key, "key",16,4);
            //verificar la valides de la llave
                if(MatrixDeterminant.bigLaplace(k.getValues()).equals(BigInteger.ZERO))
                    throw new Exception("La llave privada carece de inversa");
                
            
            //comenzando a encriptar
            Matrix<BigInteger> c=m;
                //rondas
                for(int i=1;i<=rounds;i++){                    
                    if(debug) debugStr+=(new Matrix("c-hex",c.getValuesInStrBigHex())).asOneElement()+"\t"+i+"\n";
                    c= Matrix.parseToBigIntegerMatrix(
                            MatrixOperations.BigMultiply(c.getValuesInBigDecimalType(), k.getValuesInBigDecimalType()),
                            "cipher-text");
                }
            //función xor final
            ArrayList<ArrayList<String>> temp=bigXOR(c.getValues(),k.getValues()).getValuesInStrBigHex(); 
            
            //creación del texto cifrado final a mostrar
            String aux;            
            System.out.println(c.getValuesInStrBigHex());
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
         public static String bigDecrypt(String cipherText,String key) throws Exception{
            String plainText=""; debugStr="";
            Matrix.debug=true;
            Matrix<BigInteger> c= Matrix.parseToBigIntegerMatrixFixedFromStrHex(cipherText, "plain-text",64*rounds,16*rounds,4*rounds);
            Matrix<Integer>    k= Matrix.parseToIntegerMatrixFixed(key, "key",16,4);
            
            Matrix<BigDecimal> invK=(new Matrix<Double>("k-inverse",MatrixTypes.inverse(k.getValues()))).getValuesAsBigDecimalMatrix();
            Matrix<BigInteger> m= c;
            
            for(int i=1;i<=rounds;i++){
                    if(debug) debugStr+=(new Matrix("c-hex",c.getValuesInStrBigHex())).asOneElement()+"\t"+i+"\n";
                    m= Matrix.parseToBigIntegerMatrix(MatrixOperations.BigMultiply( m.getValuesInBigDecimalType(), invK.getValues()), "cipher-text");
            }
            
            if(debug)
                System.out.println(m.getValues());
            
            //creación del texto plano original
            for(ArrayList<BigInteger> row: m.getValues())
                for(BigInteger col: row)
                    plainText+=((char)(col.intValue()));
            
            return plainText;
        }
        public static Matrix<BigInteger> bigXOR(ArrayList<ArrayList<BigInteger>> a,ArrayList<ArrayList<BigInteger>> b){
            int nrows=a.size(),ncols=a.get(0).size();
            ArrayList<ArrayList<BigInteger>> c=new ArrayList();
            for(int row=0;row<nrows;row++){
                c.add(new ArrayList<BigInteger>());
                for(int col=0;col<ncols;col++)                    
                    c.get(row).add(a.get(row).get(col).xor(b.get(row).get(col)));
                   //b.get(row).get(col) BigInteger.valueOf(255L)
            }
            return new Matrix("",c);
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