import java.util.Scanner;
import java.util.ArrayList;

public class FracCalc {
   public static void main(String[] args) {
   
      System.out.println("Welcome to FracCalc!");
      Scanner console = new Scanner(System.in);
      System.out.print("Enter an expression: ");
      String input = console.nextLine();
      input = input.toLowerCase();
   
      //If input is "quit", program ends with a thank you message 
      while (!input.equals("quit")) {
         
         //If input is "test", runTests() method runs test cases
         if (input.equals("test")) {
            runTests();
         }
         else if (!input.matches("[0-9-/_ +]+")) {
            System.out.println("ERROR: Input is in an invalid format.");
           
         }
         else {
            //input produced by produceAnswer() method
            String output = produceAnswer(input);
            
            if (output.contains("ERROR")) {
               System.out.println(output);
            }
            else {
               //answer is then printed by the main method
               System.out.println("Answer: " + output);
            }  
         } 
      
         System.out.print("\nEnter an expression: ");
         input = console.nextLine();
         input = input.toLowerCase();
      } 
   
      System.out.println("Thank you for using FracCalc!");
   }
 
   public static String produceAnswer(String input) {
      
      //Uses String split() method to split input into parts at each space
      String[] parts = input.split(" ");
      String answer = parts[0];
   
      
      boolean error = false;
      
      //answer is error if there are less than 2 spaces in input
      if (parts.length < 3) {
         answer = "ERROR: Input is in an invalid format.";
      }
      else {   
         //uses for loop in order to handle multiple operations
         for (int i = 0; i < parts.length - 2; i += 2) {
            if (error == false) {   
               String operand1 = answer;
            
               String improper1 = parseOperand(operand1);
            
               String operator = parts[i + 1];
            
               String operand2 = parts[i + 2];
            
               String improper2 = parseOperand(operand2);
            
               //performs operation for specific operator
               answer = performOperation(improper1, improper2, operator); 
            
               //answer is error if operator is invalid
               if (answer.equals("invalid format")) {
                  answer = "ERROR: Input is in an invalid format.";
                  error = true;
               }
               else {
                  //answer is error if denominator of a fraction is zero
                  if (improper1.contains("/0") || improper2.contains("/0") ||
                   answer.contains("/0")) {
                     answer = "ERROR: Cannot divide by zero.";
                     error = true;
                  }
                  else {
                     //answer is simplified and converted to mixed number
                     answer = reduceFraction(answer);
                  }
               }
            }
         }
      }
                 
      return answer; 
   }
 
   public static String parseOperand(String operand) {
   //Parses operand into 3 different ints and returns operand as improper fraction
   
   /*if (!operand.matches("[0-9-/_]+"))  
     return "ERROR";*/
   
      String improper = null;
      int whole = 0;
      int num = 0;
      int denom = 1;
   
      //if the operand is a mixed number
      if (operand.contains("_") && operand.contains("/")) {
         whole = Integer.parseInt(operand.substring(0, operand.indexOf("_")));
         num = Integer.parseInt(operand.substring(operand.indexOf("_") + 1, operand.indexOf("/")));
         denom = Integer.parseInt(operand.substring(operand.indexOf("/") + 1));    
      }
      //if the operand is a fraction but not a mixed number
      else if (operand.contains("/")) {
         num = Integer.parseInt(operand.substring(0, operand.indexOf("/")));
         denom = Integer.parseInt(operand.substring(operand.indexOf("/") + 1));     
      }
      //if the operand is neither a mixed number nor a fraction
      else {
         whole = Integer.parseInt(operand);     
      }
   
      //operand is turned into an improper fraction
      if (whole < 0) {
         improper = "-" + (denom * Math.abs(whole) + num) + "/" + denom;
      }
      else {
         improper = denom * whole + num + "/" + denom;
      }
   
      return improper;
   }
 
   public static String performOperation(String operand1, String operand2, String operator) {
   
      //separates each operand into the numerator and denominator      
      int num1 = Integer.parseInt(operand1.substring(0, operand1.indexOf("/")));
      int denom1 = Integer.parseInt(operand1.substring(operand1.indexOf("/") + 1));
      int num2 = Integer.parseInt(operand2.substring(0, operand2.indexOf("/")));
      int denom2 = Integer.parseInt(operand2.substring(operand2.indexOf("/") + 1));
      String answer = null;
   
      //if operator is addition
      if (operator.equals("+")) {
         answer = ((num1 * denom2) + (num2 * denom1)) + "/" + (denom1 * denom2);
      }
      //if operator is subtraction
      else if (operator.equals("-")) {
         answer = ((num1 * denom2) - (num2 * denom1)) + "/" + (denom1 * denom2);
      }
      //if operator is multiplication
      else if (operator.equals("*")) {
         answer = (num1 * num2) + "/" + (denom1 * denom2);
      }
      //if operator is division
      else if (operator.equals("/")) {
         answer = (num1 * denom2) + "/" + (denom1 * num2);
      }
      else {
         answer = "invalid format";
      }
   
      return answer;
   }
   
   public static String reduceFraction(String improper) {
   
      int num = Integer.parseInt(improper.substring(0, improper.indexOf("/")));
      int denom = Integer.parseInt(improper.substring(improper.indexOf("/") + 1));
      boolean negative = true;
      
      //if num and denom are both negative or positive, result is positive
      if ((num < 0 && denom < 0) || (num >= 0 && denom >= 0)) {
         negative = false;
      }
      num = Math.abs(num);
      denom = Math.abs(denom);
      
      int gcf = findGCF(num, denom); //greatest common factor between num and denom
      String mixedNum = null;
      
      //reduces num and denom
      num = num / gcf;
      denom = denom / gcf;
      
      //converts improper fraction to mixed number
      int whole = num / denom;
      num = num % denom;
      
      if (num == 0) {
         mixedNum = "" + whole;
      }
      else if (whole == 0) {
         mixedNum = num + "/" + denom;
      }
      else {
         mixedNum = whole + "_" + num + "/" + denom;
      }
      
      //turns mixedNum into negative if necessary
      if (negative) {
         mixedNum = "-" + mixedNum;
      }
      
      return mixedNum;
   }
   
   public static int findGCF(int num, int denom) {
   //finds greatest common factor between numerator and denominator   
      
      ArrayList<Integer> numList = new ArrayList<Integer>(); //factors in numerator
      ArrayList<Integer> denomList = new ArrayList<Integer>(); //factors in denominator
      int gcf = 1; 
      
      //finds all factors of numerator
      for (int i = 1; i <= num; i++) {
         if (num % i == 0) {
            Integer k = new Integer(i);
            numList.add(k);
         }
      }
            
      //finds all factors of denominator
      for (int i = 1; i <= denom; i++) {
         if (denom % i == 0) {
            Integer k = new Integer(i);
            denomList.add(k);
         }
      }
      
   /* Finds gcf between numerator and denominator by comparing 
      each factor of numerator to see if it is in denominator. 
      Largest factor shared by both is the gcf. */      
      for (int i = 0; i < numList.size(); i++) {
         if (denomList.contains(numList.get(i))) {
            gcf = numList.get(i);
         }
      }
      
      return gcf;
   }
 
   public static void runTests() {
   /* Declares String array of input to test and their expected output
      For loop calls testAnswer() method for each test input + expected output
      testAnswer() method determines whether each test passed */
   
      String[] inputs = { "10/4 + 0", 
                          "12 - 6", 
                          "1_3/4 * 3/4", 
                          "3/4 * 3_6/7",
                          "98 / -5",
                          "17 - -1/2",
                          "-4_9/2 * -4_9/2",
                          "-2_3/4 + 0",
                          "1 / -3/4",
                          "1/2 + 1/3",
                          "-2_6/8 + 0",
                          "1 / -3",
                          "1 / -12/6",
                          "10_2/4 / -20/10",
                          "-2 / -2",
                          "1 + 2 + 3",
                          "3/4 * 4",
                          "-1/2 * 4 + 3/4",
                          "5_3/4 - -6_8/8 - 5_3/4",
                          "1 / 0",
                          "1/0 + 1",
                          "1 + 1/0",
                          "1 ++ 1",
                          "1 -- 2",
                          "5 *= 2",
                          "1+ 2",
                          "7/3" };
   
      String[] expected = { "2_1/2",
                            "6",
                            "1_5/16",
                            "2_25/28",
                            "-19_3/5",
                            "17_1/2",
                            "72_1/4",
                            "-2_3/4",
                            "-1_1/3",
                            "5/6",
                            "-2_3/4",
                            "-1/3",
                            "-1/2",
                            "-5_1/4",
                            "1",
                            "6",
                            "3",
                            "-1_1/4",
                            "7",
                            "ERROR: Cannot divide by zero.",
                            "ERROR: Cannot divide by zero.",
                            "ERROR: Cannot divide by zero.",
                            "ERROR: Input is in an invalid format.",
                            "ERROR: Input is in an invalid format.",
                            "ERROR: Input is in an invalid format.",
                            "ERROR: Input is in an invalid format.",
                            "ERROR: Input is in an invalid format." };
   
      for (int i = 0; i < inputs.length; i++) {
         testAnswer(inputs[i], expected[i]);
      }
   }
 
   public static void testAnswer(String input, String expected) {
   /* Uses test inputs and expected outputs from runTests() method
      Test passes if expected output matches output generated from produceAnswer() method
      If they don't match, the test fails
      Prints whether or not each test passes */
   
      String output = produceAnswer(input); 
      boolean pass = output.equals(expected);
   
      if (pass) {
         System.out.println("Test passed");
      }
      else {
         System.out.println("Test failed");
         System.out.println("input: " + input);
         System.out.println("expected: " + expected);
         System.out.println("output: " + output);
      }
   } 
}