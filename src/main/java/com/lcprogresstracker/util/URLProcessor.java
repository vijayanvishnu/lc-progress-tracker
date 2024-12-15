package com.lcprogresstracker.util;

import com.lcprogresstracker.enums.RequestStatus;
import com.lcprogresstracker.request.CommonRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLProcessor {
    private static final String patternStr = "(?:https?://)?(?:www\\.)?leetcode\\.com/([^/]+)";
    public static CommonRequest processUsernameOrURL(String usernameOrURL){
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(usernameOrURL);
        CommonRequest commonRequest = new CommonRequest();
        if (matcher.find()) {
            commonRequest.setUsername(matcher.group(1));
            commonRequest.setRequestStatus(RequestStatus.VALID);
        } else {
            commonRequest.setUsername(usernameOrURL);
            commonRequest.setRequestStatus(RequestStatus.NOT_VALID);
        }
        return commonRequest;
    }
}
