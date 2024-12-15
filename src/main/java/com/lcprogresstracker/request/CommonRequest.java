package com.lcprogresstracker.request;

import com.lcprogresstracker.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonRequest {
    private String username;
    private RequestStatus requestStatus = RequestStatus.NOT_DETERMINED;
}
