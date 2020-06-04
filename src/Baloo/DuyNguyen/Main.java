package Baloo.DuyNguyen;

import java.io.*;
import java.util.*;
import java.awt.Color;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.FileOutputStream;
import java.io.IOException;

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
	    colors.convertHSB();
	    colors.sortArray();
	    colors.convertHSB();
	    colors.sortShade();
	    colors.convertHSB();
	    colors.writeFile();
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
        for(int i = 0; i < colors.length; i++)
        {
            System.out.println(i + ": " + colors[i]);

        }
        System.out.println("Size: " + colors.length);
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
//            System.out.println("hue is: " + hue[i] + " " + "sat is: " + sat[i] + " " + "brightness is: " + brightness[i]);
//            System.out.println(sat[i]);
//            System.out.println(brightness[i]);
        }
    }

    public void sortArray()
    {
        boolean sorted = false;
        float tempH;
        color temp;
        while(!sorted)
        {
            sorted = true;
            for(int i = 0; i < colors.length - 1; i++)
            {
                if(hue[i] > hue[i + 1])
                {
                    temp = colors[i];
                    colors[i] = colors[i + 1];
                    colors[i + 1] = temp;
                    tempH = hue[i];
                    hue[i] = hue[i + 1];
                    hue[i + 1] = tempH;
                    sorted = false;
                }
            }
        }
    }

    public void sortShade()
    {
        boolean sorted = false;
        float tempH, tempS;
        color temp;
        while(!sorted)
        {
            sorted = true;
            for(int i = 0; i < colors.length - 1; i++)
            {
                if(hue[i] == hue[i + 1] && sat[i] > sat[i + 1])
                {
                    temp = colors[i];
                    colors[i] = colors[i + 1];
                    colors[i + 1] = temp;
                    tempH = hue[i];
                    hue[i] = hue[i + 1];
                    hue[i + 1] = tempH;
                    tempS = sat[i];
                    sat[i] = sat[i + 1];
                    sat[i + 1] = tempS;
                    sorted = false;
                }
            }
        }
    }

    public void writeFile() {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet( "Sort Colors");

        Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
        data.put(1, new Object[] {"Code", "Red", "Green", "Blue"});
        for(int i = 0; i < colors.length; i++)
        {
            data.put(i + 2, new Object[]{colors[i].getCode(), colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue()});
        }
        Set<Integer> keyset = data.keySet();

        int rownum = 0;
        for(Integer key: keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for(Object obj: objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("UpdatedBook.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("File written successfully on disk.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
