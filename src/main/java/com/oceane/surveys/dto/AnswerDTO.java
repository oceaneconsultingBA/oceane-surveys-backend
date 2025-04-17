package com.oceane.surveys.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private Long id;

    private Long recipientId;

    private String text;

    private Integer rating;

    private List<QuestionOptionDTO> options = new ArrayList<>();
}
