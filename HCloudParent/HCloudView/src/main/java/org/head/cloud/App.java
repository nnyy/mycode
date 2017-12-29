package org.head.cloud;

import org.head.cloud.util.ConnectionParams;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println(test(30));
    }
    
   static int test(int i) {
	   if(i<=2) {
		   return 2;
	   }else {
		   return test(i-1)+test(i-2);
	   }
   }
}
