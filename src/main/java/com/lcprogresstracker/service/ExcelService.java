package com.lcprogresstracker.service;

import com.lcprogresstracker.request.CommonRequest;
import com.lcprogresstracker.util.URLProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
@Log4j2
public class ExcelService {
    public List<List<CommonRequest>> readExcelSheet(String filePath){
        List<List<CommonRequest>> usersList = new ArrayList<>();
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(new File(filePath));
            int sheets = workbook.getNumberOfSheets();
            for(int i = 0 ; i < sheets ; i++){
                List<String> sheetList = new ArrayList<>();
                XSSFSheet ithSheet =  workbook.getSheetAt(i);
                Iterator<Row> rowIterator = ithSheet.rowIterator();
                usersList.add(readSheetUsers(ithSheet,filePath));
            }
        }catch (Exception e){
            log.error(filePath + " " + e.getMessage());
        }
        return usersList;
    }
    private List<CommonRequest> readSheetUsers(XSSFSheet sheet , String filePath){
        List<CommonRequest> sheetList = new ArrayList<>();
        try{
            Iterator<Row> rowIterator = sheet.rowIterator();
            int cols = sheet.getRow(0).getLastCellNum();
            if(!sheet.getRow(0).getCell(0).toString().equals("username")){
                throw new Exception(filePath + " " + sheet.getSheetName() + " row : 0 , col : 0 is not username");
            }
            if(cols != 1){
                throw new Exception(filePath + " " + sheet.getSheetName() + " has more than one columns , it must contains only username column in the given file.");
            }
            rowIterator.next();
            while(rowIterator.hasNext()){
                Iterator<Cell> cellIterator = rowIterator.next().iterator();
                while(cellIterator.hasNext()){
                    CommonRequest processedRequest = URLProcessor.processUsernameOrURL(cellIterator.next().toString());
                    sheetList.add(processedRequest);
                }
            }
        }catch (Exception e){
            log.error(filePath + " " + e.getMessage());
        }
        return sheetList;
    }
}
