package com.lcprogresstracker.response;

import com.lcprogresstracker.request.CommonRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private CommonRequest request;
    private String errorMessage;
    private Object data;
    private int code;
}
