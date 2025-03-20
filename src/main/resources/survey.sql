CREATE TABLE survey
  (
     survey_id        NUMERIC(100) PRIMARY KEY NOT NULL,
     title            VARCHAR(100) NOT NULL,
     description      VARCHAR(100) NOT NULL,
     creationdate     DATE NOT NULL,
     lastmodifieddate DATE NOT NULL,
     status           CHAR(3) NOT NULL
  );

CREATE TABLE question
  (
     question_id      NUMERIC(100) PRIMARY KEY NOT NULL,
     survey_id        NUMERIC(100) NOT NULL,
     text             VARCHAR(100) NOT NULL,
     type             CHAR(3) NOT NULL,
     required         NUMERIC(1) NOT NULL,
     displayorder     NUMERIC(5) NOT NULL,
     conditionallogic VARCHAR(100) NOT NULL
  );

CREATE TABLE questionoption
  (
     questionoption_id NUMERIC(100) PRIMARY KEY NOT NULL,
     question_id       NUMERIC(100) NOT NULL,
     text              VARCHAR(100) NOT NULL,
     displayorder      NUMERIC(5) NOT NULL
  );

CREATE TABLE recipient
  (
     recipient_id NUMERIC(100) PRIMARY KEY NOT NULL,
     email        VARCHAR(100) NOT NULL,
     firstname    VARCHAR(100) NOT NULL,
     lastname     VARCHAR(100) NOT NULL,
     company      VARCHAR(100) NOT NULL,
     type         CHAR(3) NOT NULL
  );

ALTER TABLE question
  ADD FOREIGN KEY (survey_id) REFERENCES survey(survey_id);

ALTER TABLE questionoption
  ADD FOREIGN KEY (question_id) REFERENCES question(question_id);
