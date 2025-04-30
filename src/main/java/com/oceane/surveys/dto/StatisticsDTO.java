package com.oceane.surveys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticsDTO {
    private Long activeSurveys;
    private Long answers;
    private Long questions;
}
