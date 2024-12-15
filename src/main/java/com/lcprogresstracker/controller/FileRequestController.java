package com.lcprogresstracker.controller;

import com.lcprogresstracker.enums.RequestStatus;
import com.lcprogresstracker.model.ProblemStats;
import com.lcprogresstracker.request.CommonRequest;
import com.lcprogresstracker.response.CommonResponse;
import com.lcprogresstracker.service.ExcelService;
import com.lcprogresstracker.service.ProblemStatsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
public class FileRequestController {
    private List<Map<String,Object>> responseDataAsMap;
    private final ExcelService excelService;
    private final ProblemStatsService problemStatsService;
    @Autowired
    public FileRequestController(ExcelService excelService,ProblemStatsService problemStatsService){
        this.excelService = excelService;
        this.problemStatsService = problemStatsService;
    }
    @GetMapping("/fetchDatas")
    public ResponseEntity<List<Map<String,Object>>> getDatas(){
        return ResponseEntity.ok(responseDataAsMap);
    }

    // @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/files")
    public ResponseEntity<List<Map<String,Object>>> getFileFromForm(@RequestParam("file") MultipartFile file) {
        File newFile = new File("inputFile.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(newFile);
             InputStream fileInputStream = file.getInputStream()) {
            int val;
            while ((val = fileInputStream.read()) != -1) {
                outputStream.write(val);
            }

            List<List<CommonRequest>> usersList = excelService.readExcelSheet(newFile.getName());
            List<List<CommonResponse>> responseDataAsProblemStats = problemStatsService.getResponseAsList(usersList);
            responseDataAsMap = getListOfMap(responseDataAsProblemStats);

            return ResponseEntity.ok(responseDataAsMap);
        } catch (IOException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    private List<Map<String,Object>> getListOfMap(List<List<CommonResponse>> usersData){
        List<Map<String,Object>> responseList = new ArrayList<>();
        for (List<CommonResponse> list : usersData) {
            for(CommonResponse commonResponse :  list){
                Map<String , Object> dataOnMap = new HashMap<>();
                CommonResponse objectResponse = commonResponse;
                ProblemStats response = (ProblemStats) objectResponse.getData();
                CommonRequest request = commonResponse.getRequest();
                dataOnMap.put("total" , response.getTotal());
                dataOnMap.put("username" , request.getUsername());
                dataOnMap.put("easy" , response.getEasy());
                dataOnMap.put("hard" , response.getHard());
                dataOnMap.put("medium" , response.getMedium());
                dataOnMap.put("status" ,request.getRequestStatus());
                responseList.add(dataOnMap);
            }
        }
        return responseList;
    }
    private List<JSONObject> getListOfJSON(List<List<JSONObject>> llJson){
        List<JSONObject> jsonList = new ArrayList<>();
        for(List<JSONObject> jsonObjectList : llJson){
            jsonList.addAll(jsonObjectList);
        }
        return jsonList;
    }
}
