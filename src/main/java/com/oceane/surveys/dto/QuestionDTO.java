package com.oceane.surveys.dto;

import com.oceane.surveys.entities.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Le texte de la question est obligatoire")
    private String text;

    @NotNull(message = "Le type de question est obligatoire")
    private QuestionType type;

    private boolean required;
    private int displayOrder;
    private String conditionalLogic;

    private List<QuestionOptionDTO> options = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getConditionalLogic() {
        return conditionalLogic;
    }

    public void setConditionalLogic(String conditionalLogic) {
        this.conditionalLogic = conditionalLogic;
    }

    public List<QuestionOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOptionDTO> options) {
        this.options = options;
    }
}
