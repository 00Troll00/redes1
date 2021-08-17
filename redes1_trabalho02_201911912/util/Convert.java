/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 31/07/2021 15:18
 * Last update: 16/08/2021 14:31
 * Name: Convert.java
 * Function: provides some methoods to convert types
 *******************************************************************/
package util;

public class Convert {
  

  public static int[] charArrayToAscii(char[] charArray){
    int[] asciiArray = new int[charArray.length];
    for(int i=0; i<charArray.length; i++){
      asciiArray[i] = charArray[i];
    }
    return asciiArray;
  }

  public static int binaryToDecimal(int binary){
    return binaryToDecimal(binary, 0, 0);
  }
 
  private static int binaryToDecimal(int binary, int number, int cont){
    if(binary == 0)
      return number;
    if(binary % 10 == 1)
      number += (binary % 10) * pow(2, cont);
    return binaryToDecimal(binary/10, number, cont+1);
  }

  public static int binaryToDecimal(int[] binary){
    return binaryToDecimal(binary, 0, binary.length-1, 0);
  }

  private static int binaryToDecimal(int[] binary, int decimal, int position, int cont){
    if(position<0)
      return decimal;
    if(binary[position] == 1)
      decimal += pow(2,cont);
    return binaryToDecimal(binary, decimal, position-1, cont+1);
  }

  private static int pow(int base, int exponent){
    int temp = base;
    if(exponent == 0)
      return 1;
    while(exponent != 1){
      temp *= base;
      exponent--;
    }
    return temp;
  }

  public static int manchesterToDecimal(String manchester){
    char[] arrayChar = manchester.toCharArray();
    int number = 0;
    int cont = 0;
    
    for(int i=arrayChar.length-1; i>=0; i-=2){
      if(arrayChar[i] == arrayChar[i-1])
        break;
      if(arrayChar[i] == '0')
        number += pow(2, cont);
      cont++;
    }
    return number;
  }
}
