package Baloo.DuyNguyen;

import java.io.*;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }
}

class colorArray implements Serializable
{
    private color[] colors;
    private int size;

    public colorArray(){size = 0};
    public colorArray(int length)
    {
        size = 0;
        this.colors = new color[length];
    }

    //Last edited by C. Herbert 3/2/15
    public int countLines(String filename)
    {
        //set up file variables for buffered input
        BufferedReader bRead = null;
        Scanner infile = null;
        String thisLine = "";
        int count = 0;

        //count number of lines
        try
        {
            // create objects for buffered input
            bRead = new BufferedReader(new FileReader(filename));
            infile = new Scanner(bRead);
            while ((thisLine = bRead.readLine()) != null)
            {
                count++;
            }
            // close buffered input
            infile.close();
        } catch (FileNotFoundException exc)
        {
            System.out.println("countlines() failed, the file: "
                    + filename + " was not found.");
        } catch (Exception e)
        {
            System.out.println("Unreported Exception");
        }
        return count;
    }//end countlines(String)-------

    class color implements  Serializable
    {
        private String code;
        private int red;
        private int green;
        private int blue;

        color()
        {
            code = "";
            red = 0;
            green = 0;
            blue = 0;
        }

        color(String code, int red, int green, int blue)
        {
            this.code = code;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public String getCode()
        {
            return code;
        }
        public int getRed()
        {
            return red;
        }
        public int getGreen()
        {
            return green;
        }
        public int getBlue()
        {
            return blue;
        }
        public void setCode(String code)
        {
            this.code = code;
        }
        public void setRed(int red)
        {
            this.red = red;
        }
        public void setGreen(int green)
        {
            this.green = green;
        }
        public void setBlue(int blue)
        {
            this.blue = blue;
        }
        public String toString()
        {
            return(this.code + "" + this.red + "" + this.green + "" + this.blue);
        }
    }


}
