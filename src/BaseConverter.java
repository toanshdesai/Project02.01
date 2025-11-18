import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Converts numbers between bases (2–16), checks input validity, and handles integer overflow
 * @author toanshdesai
 * @version 11.17.2025
 * I worked with @solomonhornstein
 * Flint Session: https://app.flintk12.com/activities/test-data-file--f45294/sessions/4c151517-26b2-4e8f-bd21-a80d22ea6b64
 * Extras: Checks for integer overflow, allows user to select the file, checks if input contains digit greater than base
 */
public class BaseConverter {
    private final String alphaNum = "0123456789ABCDEF";

    /**
     * Convert a String num in fromBase to base-10 int.
     * @param num the input number
     * @param fromBase the base "num" is in
     * @return base 10 representation of "num"
     */
    public int strToInt(String num, String fromBase){
        // num = "CE" fromBase = "15"
        // CE in base 15 = 195 in base 10
        int base10=0;
        for(int exp =0; exp <num.length(); exp++){
            base10 += (int)(alphaNum.indexOf(num.charAt(num.length()-(1+exp)))*Math.pow(Integer.parseInt(fromBase),exp));
        }
        return base10;
    }

    /**
     * Convert a base-10 int to a String number of base toBase.
     * @param num base 10 representation of the input number
     * @param toBase the base "num" is to be converted to
     * @return "toBase" representation of "num"
     */
    public String intToStr(int num, int toBase){
        String result="";
        if(num==0){
            return "0";
        }
        while (num > 0) {
            int remainder = num % toBase;
            result = alphaNum.charAt(remainder) + result;
            num /= toBase;
        }
        return result;
    }

    /**
     * Convert a String num in fromBase to a BigInteger to handle values larger than the integer limit
     * @param num the input number
     * @param fromBase the base "num" is in
     * @return base 10 BigInteger representation of "num"
     */
    public BigInteger strToIntOverflow(String num, String fromBase){
        // num = "CE" fromBase = "15"
        // CE in base 15 = 195 in base 10
        BigInteger base10= BigInteger.valueOf(0);
        long longBase = Long.parseLong(fromBase);
        for(int exp =0; exp <num.length(); exp++){
            base10 = base10.add(BigInteger.valueOf(alphaNum.indexOf(num.charAt(num.length()-(1+exp)))).multiply(BigInteger.valueOf(longBase).pow(exp)));
        }
        return base10;
    }

    /**
     * Opens the file stream, inputs data one line at a time, converts, prints
     * the result to the console window and writes data to the output stream.
     */
    public void inputConvertPrintWrite(){
        Scanner in;
        PrintWriter pw;
        try{
            File file = new File("datafiles/values.dat");

            if (!file.exists()) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select test data file");
                chooser.setCurrentDirectory(new File("datafiles"));
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
            }
            in = new Scanner(file);

            pw = new PrintWriter(new FileWriter("datafiles/converted.dat"));
            String[] temp;
            while(in.hasNextLine()){
                boolean invalid = false;
                temp = in.nextLine().split("\t");
                temp[0] = temp[0].trim().toUpperCase();
                boolean goodChar = true;

                for(int i = 0; i < temp[0].length(); i++){
                    if (!alphaNum.contains(String.valueOf(temp[0].charAt(i)))) {
                        goodChar = false;
                        break;
                    }
                }
                if (!goodChar || temp[0].charAt(0) == '-')   System.out.println("Invalid input " + temp[0]);

                else if (Integer.parseInt(temp[1]) < 2 || Integer.parseInt(temp[1]) > 16){
                    System.out.println("Invalid input base " + temp[1]);
                }
                else if (Integer.parseInt(temp[2]) < 2 || Integer.parseInt(temp[2]) > 16){
                    System.out.println("Invalid output base " + temp[2]);
                }
                else {
                    for (int i =0; i<temp[0].length();i++){
                        if (alphaNum.indexOf(temp[0].charAt(i)) >= Integer.parseInt(temp[1])){
                            System.out.println("Digit "+temp[0].charAt(i)+" in "+temp[0]+" is greater than base "+temp[1]);
                            invalid = true;
                            break;
                        }
                    }
                    if (!invalid) {
                        BigInteger bigVal = strToIntOverflow(temp[0], temp[1]);
                        if (bigVal.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0) {
                            String newValue = intToStr(strToInt(temp[0],temp[1]), Integer.parseInt(temp[2]));
                            System.out.println(temp[0] + " base " + temp[1] + " = " + newValue + " base " + temp[2]);
                            pw.println(temp[0] + "\t" + temp[1] + "\t" + newValue + "\t" + temp[2]);
                        }
                        else {
                            System.out.println("Result of "+temp[0]+" exceeds integer limit");
                        }
                    }
                }
            }
            in.close();
            pw.close();
        }
        catch (Exception e){
            System.out.println("ERROR: "+ e);
        }
    }

    // Main method for class BaseConverter
    public static void main(String[] args) {
        BaseConverter app = new BaseConverter();
        app.inputConvertPrintWrite();
    }
}
