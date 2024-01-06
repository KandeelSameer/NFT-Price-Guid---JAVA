package application;

import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model.ModelForTable;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
  
public class ExcelApi {
	 private static ExcelApi instance;
	private ExcelApi() {
		
	}
	//singleton
	  public  static ExcelApi getInstance() {
	    	 if(instance==null) {
	    		 instance=new ExcelApi();
	    		 
	    	 }
	    	 return instance;
	     }
    // any exceptions need to be caught
    public  void exportToExcel(String filename,ArrayList<ModelForTable> list)
    {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
  
        // spreadsheet object
        XSSFSheet spreadsheet
            = workbook.createSheet(" Student Data ");
  
        // creating a row object
        XSSFRow row;
  
        // This data needs to be written (Object[])
        Map<String, Object[]> studentData
            = new TreeMap<String, Object[]>();
  System.out.println("getting here to send excel file");
        studentData.put(
            "1",
            new Object[] { "Collection Name", "openSea Price [SOL]", "magic eden Price[SOL]","differance" });
     int    i=1;
        for(ModelForTable rowModel:list) {
        	i++;
            studentData.put(String.valueOf(i), new Object[] { rowModel.getCollectionName(), rowModel.getOpenSeaPrice(),
            		rowModel.getMagicEdenPrice() ,rowModel.getDiff().toString()});
        }
 
  
        Set<String> keyid = studentData.keySet();
  
        int rowid = 0;
  
        // writing the data into the sheets...
  
        for (String key : keyid) {
  
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = studentData.get(key);
            int cellid = 0;
  
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }
  
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        try {
        FileOutputStream out = new FileOutputStream(
            new File(filename+".xlsx"));
  
        workbook.write(out);
        out.close();
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
}
