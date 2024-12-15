package com.lcprogresstracker.service;

import com.lcprogresstracker.response.CommonResponse;
import lombok.Data;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@Data
public class LCDataService {
    private final CloseableHttpClient httpClient;
    private final HttpPost httpPost;
    @Autowired
    public LCDataService(CloseableHttpClient httpClient , HttpPost httpPost){
        this.httpPost = httpPost;
        this.httpClient = httpClient;
    }
    public CommonResponse getQueryResponse(String query) {
        StringEntity stringEntity = new StringEntity(query);
        CommonResponse response = new CommonResponse();
        httpPost.setEntity(stringEntity);
        try{
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
            JSONObject jsonData = new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity()));
            if(jsonData.has("errors")){
                response.setCode(404);
                response.setErrorMessage(": username not founded");
            }else{
                response.setData(jsonData);
                response.setCode(200);
            }
        }catch(Exception e){
            response.setCode(404);
            response.setErrorMessage("can't reach : " + e.getMessage());
        }
        return response;
    }
}
