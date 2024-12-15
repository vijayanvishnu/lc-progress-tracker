package com.lcprogresstracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ProblemStats {
    private int easy;
    private int medium;
    private int hard;
    private int total;
}
