package com.oceane.surveys.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerStateDTO {
    private Long surveyId;

    private Long recipientId;

    private LocalDateTime answerDate;
}
