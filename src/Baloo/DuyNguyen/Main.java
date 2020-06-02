package Baloo.DuyNguyen;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.awt.Color;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

    public static void main(String[] args) throws IOException {

	    colorArray colors = new colorArray("Book1.xlsx");
	    colors.printElements();
	    colors.convertHSB();
    }
}

class colorArray implements Serializable
{
    private color[] colors;
    private float[] hue;
    private float[] sat;
    private float[] brightness;
    private int size;

    public colorArray() {size = 0;}
    public colorArray(int length)
    {
        size = 0;
        this.colors = new color[length];
    }

    colorArray(String filename) throws IOException {
        colors = new color[countRows(filename)];
        readFile(filename);
    }

    colorArray(color[] colors)
    {
        size = colors.length;
        this.colors = new color[size + 2];

        System.arraycopy(colors, 0, this.colors, 0, size);
    }

    //Last edited by C. Herbert 3/2/15
    public int countRows(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

        return mySheet.getPhysicalNumberOfRows();
    }//end countlines(String)-------

    public void setElement(int index, color myColor)
    {
        try
        {
            if (index <= size)
            {
                if (index == size)
                {
                    //size++ if adding a new element, otherwise, just add element
                    size++;

                    //only print if readCsv did NOT call this method
                    System.out.println("'" + myColor.code + "' appended.");

                } else
                {
                    //only print if readCsv did NOT call this method
                    System.out.println("Element " + index
                                + " set to " + "'" + myColor.code + "'.");
                }
                //set the element
                colors[index] = myColor;
            } else
            {           //print error if the index is larger than the size of array
                System.out.println("Cannot insert an element because the "
                        + "index\n was too large.You may only insert an "
                        + "element at \n an existing element, or at the end"
                        + " of the array");
            }
        } catch (ArrayIndexOutOfBoundsException exc)
        {
            System.out.println("setElement failed, the given index, "
                    + index + " is not in the array.");
        }
    }
    public void readFile(String filename)
    {
        try{
            int control = 0;
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            String[] data = new String[4];
            while(itr.hasNext())
            {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int i = 0;
                while(cellIterator.hasNext())
                {

                    Cell cell = cellIterator.next();
                    if(cell.getCellTypeEnum() == CellType.STRING)
                    {
                        data[i] = cell.getStringCellValue();
                        i++;
                    }
                    else
                    {
                        data[i] = String.valueOf(Math.round(cell.getNumericCellValue()));
//                        System.out.println(data[i]);
                        i++;
                    }
                }
                color myColor = new color(data);
                this.setElement(control, myColor);
                control++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void printElements()
    {
        for(int i = 0; i < size; i++)
        {
            System.out.println(colors[i]);
        }
    }

    public void convertHSB()
    {
        hue = new float[size];
        sat = new float[size];
        brightness = new float[size];

        for(int i = 0; i < size; i++)
        {
            float[] hsb = Color.RGBtoHSB(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), null);
            hue[i] = hsb[0];
            sat[i] = hsb[1];
            brightness[i] = hsb[2];
//            System.out.println(hue[i]);
//            System.out.println(sat[i]);
//            System.out.println(brightness[i]);
        }
    }

    static int partition(int[] array, 0, int size) {
        int pivot = 0;

        int counter = 0;
        for (int i = 0; i < size; i++) {
            if (array[i] < array[pivot]) {
                int temp = array[counter];
                array[counter] = array[i];
                array[i] = temp;
                counter++;
            }
        }
        int temp = array[pivot];
        array[pivot] = array[counter];
        array[counter] = temp;

        return counter;
    }

    public static void quickSort(int[] array, int begin, int end) {
        if (end <= begin) return;
        int pivot = partition(array, begin, end);
        quickSort(array, begin, pivot-1);
        quickSort(array, pivot+1, end);
    }

    public void sortHue()
    {

    }
    static class color implements Serializable
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
        color(String[] data)
        {
            code = data[0];
            red = Integer.parseInt(data[1]);
            green = (Integer.parseInt(data[2]));
            blue = (Integer.parseInt(data[3]));
        }//end Quote(String[])--------------------------------------------------

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
            return(this.code + " " + this.red + " " + this.green + " " + this.blue);
        }
    }


}
