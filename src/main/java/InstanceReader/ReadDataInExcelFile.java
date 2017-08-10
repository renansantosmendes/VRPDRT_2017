/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Request;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class ReadDataInExcelFile {

    private String filePath;

    public ReadDataInExcelFile(String filePath) {
        this.filePath = filePath;
    }

    public void saveData(List<Request> requests) {
        requests.forEach(r -> System.out.println(r.getStringToFile()));
    }

    public void getRequests(String instanceName) throws IOException, BiffException {
        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath), conf);
        Sheet sheet = workbook.getSheet(instanceName);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        //List<Occurrence> occurrences = new ArrayList<>();
        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            Cell origin = sheet.getCell(1, i);
            Cell destination = sheet.getCell(2, i);
            Cell pickupTimeWindowLower = sheet.getCell(3, i);
            Cell pickupTimeWindowUpper = sheet.getCell(4, i);
            Cell deliveryTimeWindowLower = sheet.getCell(5, i);
            Cell deliveryTimeWindowUpper = sheet.getCell(6, i);

        }
    }
}