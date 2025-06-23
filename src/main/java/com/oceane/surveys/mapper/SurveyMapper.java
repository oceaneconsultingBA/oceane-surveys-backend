package com.oceane.surveys.mapper;

import com.oceane.surveys.dto.*;
import com.oceane.surveys.entities.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SurveyMapper {

    public SurveyDTO toDto(Survey survey) {
        if (survey == null) {
            return null;
        }

        SurveyDTO dto = new SurveyDTO();
        dto.setId(survey.getId());
        dto.setTitle(survey.getTitle());
        dto.setDescription(survey.getDescription());
        dto.setCreationDate(survey.getCreationDate());
        dto.setLastModifiedDate(survey.getLastModifiedDate());
        dto.setStatus(survey.getStatus());

        // Map questions if not empty
        if (survey.getQuestions() != null && !survey.getQuestions().isEmpty()) {
            List<QuestionDTO> questionDTOs = survey.getQuestions().stream()
                    .map(this::questionToDto)
                    .toList();
            dto.setQuestions(questionDTOs);
        }

        // Map recipients if not empty
        if (survey.getRecipients() != null && !survey.getRecipients().isEmpty()) {
            List<Long> recipientIds = survey.getRecipients().stream()
                    .map(Recipient::getId)
                    .toList();
            dto.setRecipientIds(recipientIds);
        }

        return dto;
    }

    public Survey toEntity(SurveyDTO dto) {
        if (dto == null) {
            return null;
        }

        Survey survey = new Survey();
        survey.setId(dto.getId());
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setCreationDate(dto.getCreationDate());
        survey.setLastModifiedDate(dto.getLastModifiedDate());
        survey.setStatus(dto.getStatus());

        return survey;
    }

    public TokenDTO toDto(SurveyToken token) {
        if (token == null) {
            return null;
        }

        var dto = new TokenDTO();
        dto.setId(token.getId());
        dto.setToken(token.getToken());
        dto.setSurvey(toDto(token.getSurvey()));
        dto.setRecipient(recipientToDto(token.getRecipient()));

        return dto;
    }

    public SurveyToken toEntity(TokenDTO dto) {
        if (dto == null) {
            return null;
        }

        var survey = new SurveyToken();
        survey.setId(dto.getId());
        survey.setToken(dto.getToken());
        survey.setSurvey(toEntity(dto.getSurvey()));
        survey.setRecipient(recipientDtoToEntity(dto.getRecipient()));

        return survey;
    }

    public QuestionDTO questionToDto(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setType(question.getType());
        dto.setRequired(question.isRequired());
        dto.setDisplayOrder(question.getDisplayOrder());
        dto.setConditionalLogic(question.getConditionalLogic());

        // Map options if not empty
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            List<QuestionOptionDTO> optionDTOs = question.getOptions().stream()
                    .map(this::optionToDto)
                    .collect(Collectors.toList());
            dto.setOptions(optionDTOs);
        }

        return dto;
    }

    public Question questionDtoToEntity(QuestionDTO dto) {
        Question question = new Question();
        // Ne pas définir l'ID si c'est 0 ou null
        if (dto.getId() != null && dto.getId() != 0) {
            question.setId(dto.getId());
        }
        question.setText(dto.getText());
        question.setType(dto.getType());
        question.setRequired(dto.isRequired());
        question.setDisplayOrder(dto.getDisplayOrder());
        question.setConditionalLogic(dto.getConditionalLogic());

        // Map options if not empty
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            dto.getOptions().forEach(optionDTO -> {
                QuestionOption option = optionDtoToEntity(optionDTO);
                option.setQuestion(question);
                question.getOptions().add(option);
            });
        }

        return question;
    }

    public QuestionOptionDTO optionToDto(QuestionOption option) {
        QuestionOptionDTO dto = new QuestionOptionDTO();
        dto.setId(option.getId());
        dto.setText(option.getText());
        dto.setDisplayOrder(option.getDisplayOrder());
        return dto;
    }

    public QuestionOption optionDtoToEntity(QuestionOptionDTO dto) {
        QuestionOption option = new QuestionOption();
        // Ne pas définir l'ID si c'est 0 ou null
        if (dto.getId() != null && dto.getId() != 0) {
            option.setId(dto.getId());
        }
        option.setText(dto.getText());
        option.setDisplayOrder(dto.getDisplayOrder());
        return option;
    }

    public RecipientDTO recipientToDto(Recipient recipient) {
        if (recipient == null) {
            return null;
        }

        RecipientDTO dto = new RecipientDTO();
        dto.setId(recipient.getId());
        dto.setEmail(recipient.getEmail());
        dto.setFirstName(recipient.getFirstName());
        dto.setLastName(recipient.getLastName());
        dto.setCompany(recipient.getCompany());
        dto.setType(recipient.getType());
        return dto;
    }

    public Recipient recipientDtoToEntity(RecipientDTO dto) {
        if (dto == null) {
            return null;
        }

        Recipient recipient = new Recipient();
        recipient.setId(dto.getId());
        recipient.setEmail(dto.getEmail());
        recipient.setFirstName(dto.getFirstName());
        recipient.setLastName(dto.getLastName());
        recipient.setCompany(dto.getCompany());
        recipient.setType(dto.getType());
        return recipient;
    }

    public AnswerDTO answerToDto(Answer answer) {
        AnswerDTO dto = new AnswerDTO();
        dto.setId(answer.getId());
        dto.setCreationDate(answer.getCreationDate());
        dto.setText(answer.getText());
        dto.setRating(answer.getRating());
        dto.setRecipientId(answer.getRecipient().getId());

        // Map options if not empty
        if (answer.getOptions() != null && !answer.getOptions().isEmpty()) {
            List<QuestionOptionDTO> optionDTOs = answer.getOptions().stream()
                    .map(this::optionToDto)
                    .toList();
            dto.setOptions(optionDTOs);
        }

        return dto;
    }

    public Answer answerDtoToEntity(AnswerDTO dto) {
        Answer answer = new Answer();
        // Ne pas définir l'ID si c'est 0 ou null
        if (dto.getId() != null && dto.getId() != 0) {
            answer.setId(dto.getId());
        }
        answer.setCreationDate(dto.getCreationDate());
        answer.setText(dto.getText());
        answer.setRating(dto.getRating());

        // Map options if not empty
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            dto.getOptions().forEach(optionDTO -> {
                QuestionOption option = optionDtoToEntity(optionDTO);
                answer.getOptions().add(option);
            });
        }

        return answer;
    }
}
