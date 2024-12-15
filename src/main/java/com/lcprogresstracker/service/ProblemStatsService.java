package com.lcprogresstracker.service;

import com.lcprogresstracker.enums.RequestStatus;
import com.lcprogresstracker.model.ProblemStats;
import com.lcprogresstracker.request.CommonRequest;
import com.lcprogresstracker.response.CommonResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ProblemStatsService {
    private final LCDataService LCDataService;
    @Autowired
    public ProblemStatsService(LCDataService LCDataService){
        this.LCDataService = LCDataService;
    }
    public String getQueryForProblemStats(CommonRequest leetcode){
        return "{\"query\": \"query userSessionProgress($username: String!) { matchedUser(username: $username) { submitStats { acSubmissionNum { count } } } }\", \"variables\": { \"username\": \""
                + leetcode.getUsername() + "\" }}";
    }
    public CommonResponse getProblemStats(CommonRequest request){
        ProblemStats problemStats = new ProblemStats();
        CommonResponse response = new CommonResponse();
        response.setRequest(request);
        if(request.getRequestStatus() == RequestStatus.VALID){
            response = LCDataService.getQueryResponse(getQueryForProblemStats(request));
            response.setRequest(request);
            if(response.getCode() == 200){
                try {
                    JSONObject jsonData = (JSONObject) response.getData();
                    JSONArray solvedCounts = jsonData.getJSONObject("data").getJSONObject("matchedUser").getJSONObject("submitStats").getJSONArray("acSubmissionNum");
                    problemStats.setTotal(solvedCounts.getJSONObject(0).getInt("count"));
                    problemStats.setEasy(solvedCounts.getJSONObject(1).getInt("count"));
                    problemStats.setMedium(solvedCounts.getJSONObject(2).getInt("count"));
                    problemStats.setHard(solvedCounts.getJSONObject(3).getInt("count"));
                }catch(Exception e){
                    problemStats.setEasy(-1);
                    problemStats.setMedium(-1);
                    problemStats.setHard(-1);
                    problemStats.setTotal(-1);
                    response.getRequest().setRequestStatus(RequestStatus.WRONG_USERNAME);
                    log.warn(request.getUsername() + " "  + response.getErrorMessage() + " " + e.getMessage());
                }
            }else {
                problemStats.setEasy(-1);
                problemStats.setMedium(-1);
                problemStats.setHard(-1);
                problemStats.setTotal(-1);
                response.getRequest().setRequestStatus(RequestStatus.WRONG_USERNAME);
                log.warn(request.getUsername() + " " + response.getErrorMessage());
            }
        }else{
            problemStats.setEasy(-1);
            problemStats.setMedium(-1);
            problemStats.setHard(-1);
            problemStats.setTotal(-1);
            response.getRequest().setRequestStatus(request.getRequestStatus());
            log.warn(request.getUsername() + " is not a Valid URL or Username.");
        }
        response.setCode(200);
        response.setData(problemStats);
        response.setRequest(request);
        return response;
    }
    public List<CommonResponse> getResponse(List<CommonRequest> usersList)  {
        List<CommonResponse> problemStatsList = new ArrayList<>();
        for(CommonRequest username : usersList){
            problemStatsList.add(getProblemStats(username));
        }
        return problemStatsList;
    }
    public List<List<CommonResponse>> getResponseAsList(List<List<CommonRequest>> usersList){
        List<List<CommonResponse>> responseList = new ArrayList<>();
        for(List<CommonRequest> listUsers : usersList){
            responseList.add(getResponse(listUsers));
        }
        return responseList;
    }
}
