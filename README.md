# oceane-surveys-backend

Specifications: https://oceaneconsultinggroup-my.sharepoint.com/:f:/r/personal/takrache_oceaneconsulting_com/Documents/Questionnaires%20Oceane%20Consulting%20BA?e=5%3a719c5d61346b4ccba4ed7984c7e5f572&sharingv2=true&fromShare=true&at=9

## Database schema
| USER |
| --------- |
| USER_ID number not null  |
| FIRSTNAME varchar not null  |
| LASTNAME varchar not null  |
| MAIL varchar nullable  |
| PROFILE char(3) nullable  |
| CREATION_DATE DATE nullable  |

| CAMPAIGN |
| --------- |
| CAMPAIGN_ID number not null  |
| CREATION_DATE DATE nullable  |
| CREATOR_ID (USER.USER_ID) number nullable  |

| QUESTION |
| --------- |
| QUESTION_ID number not null  |
| CAMPAIGN_ID number not null  |
| POSITION number nullable  |
| SENTENCES varchar(1000) not null  |
| TYPE number not null  |

| OPTION |
| --------- |
| OPTION_ID number not null  |
| QUESTION_ID number not null  |
| POSITION number nullable  |
| SENTENCES varchar(1000) not null  |

| SUBMISSION |
| --------- |
| SUBMISSION_ID number not null  |
| CAMPAIGN_ID number not null  |
| CREATION_DATE DATE nullable  |
| SUBMITOR_ID (USER.USER_ID) number nullable  |
| STATUS char(3) not null  |

| ANSWER |
| --------- |
| ANSWER_ID number not null  |
| SUBMISSION_ID_IF_ACTUAL number not null  |
| QUESTION_ID number not null  |
| OPTION_ID number nullable  |
| ANSWER varchar(1000) nullable  |
| CHECKBOX char(1) nullable  |
| STARS number(10) nullable  |
| DATE_ANSWER DATE nullable  |

| DISABLING_CONDITION |
| --------- |
| DISABLING_CONDITION_ID number not null  |
| CONDITIONNED_QUESTION_ID number not null  |
| ANSWER_ID number not null  |
