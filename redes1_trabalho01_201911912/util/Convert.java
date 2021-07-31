/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 31/07/2021 15:18
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package util;

public class Convert {
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
}
