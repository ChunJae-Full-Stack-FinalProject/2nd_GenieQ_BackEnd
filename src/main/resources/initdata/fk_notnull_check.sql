-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
-- -- 배포 전에는 이 파일을 실행하지 말아주세요.
--
--
-- -- FK
-- ALTER TABLE PASSAGE		ADD CONSTRAINT FK_MEMBER_TO_PASSAGE		FOREIGN KEY (MEM_CODE) REFERENCES MEMBER (MEM_CODE);
-- ALTER TABLE PASSAGE		ADD CONSTRAINT FK_SUBJECT_TO_PASSAGE	FOREIGN KEY (SUB_CODE) REFERENCES SUBJECT (SUB_CODE);
-- ALTER TABLE PAYMENT		ADD CONSTRAINT FK_MEMBER_TO_PAYMENT		FOREIGN KEY (MEM_CODE) REFERENCES MEMBER (MEM_CODE);
-- ALTER TABLE PAYMENT		ADD CONSTRAINT FK_TICKET_TO_PAYMENT		FOREIGN KEY (TIC_CODE) REFERENCES TICKET (TIC_CODE);
-- ALTER TABLE QUESTION	ADD CONSTRAINT FK_PASSAGE_TO_QUESTION	FOREIGN KEY (PAS_CODE) REFERENCES PASSAGE (PAS_CODE);
-- ALTER TABLE USAGE		ADD CONSTRAINT FK_MEMBER_TO_USAGE		FOREIGN KEY (MEM_CODE) REFERENCES MEMBER (MEM_CODE);
--
-- ---------------------------------------------------
--
-- -- NOT NULL
-- ALTER TABLE MEMBER
--     MODIFY (MEM_CODE NOT NULL, MEM_EMAIL NOT NULL, MEM_IS_DELETED NOT NULL,
--             MEM_NAME NOT NULL, MEM_PASSWORD NOT NULL);
--
-- ALTER TABLE NOTICE
--     MODIFY (NOT_CODE NOT NULL, NOT_CONTENT NOT NULL, NOT_DATE NOT NULL,
--             NOT_TITLE NOT NULL, NOT_TYPE NOT NULL);
--
-- ALTER TABLE PASSAGE
--     MODIFY (MEM_CODE NOT NULL, PAS_CODE NOT NULL, PAS_CONTENT NOT NULL,
--             PAS_DATE NOT NULL, PAS_GIST NOT NULL, PAS_IS_DELETED NOT NULL,
--             PAS_IS_FAVORITE NOT NULL, PAS_TITLE NOT NULL, SUB_CODE NOT NULL);
--
-- ALTER TABLE PAYMENT
--     MODIFY (MEM_CODE NOT NULL, PAY_CODE NOT NULL, PAY_DATE NOT NULL,
--             PAY_PRICE NOT NULL, TIC_CODE NOT NULL);
--
-- ALTER TABLE QUESTION
--     MODIFY (PAS_CODE NOT NULL, QUE_ANSWER NOT NULL, QUE_CODE NOT NULL,
--             QUE_OPTION NOT NULL, QUE_QUERY NOT NULL);
--
-- ALTER TABLE SUBJECT
--     MODIFY (SUB_CODE NOT NULL, SUB_KEYWORD NOT NULL, SUB_TYPE NOT NULL);
--
-- ALTER TABLE TICKET
--     MODIFY (TIC_CODE NOT NULL, TIC_NUMBER NOT NULL, TIC_PRICE NOT NULL);
--
-- ALTER TABLE USAGE
--     MODIFY (MEM_CODE NOT NULL, USA_BALANCE NOT NULL, USA_CODE NOT NULL,
--             USA_COUNT NOT NULL, USA_DATE NOT NULL, USA_TYPE NOT NULL);
--
-- ---------------------------------------------------
--
-- -- CHECK
-- ALTER TABLE MEMBER
--     ADD CONSTRAINT CHK_MEM_GENDER CHECK (MEM_GENDER IN ('남성', '여성'));
--
-- ALTER TABLE MEMBER
--     ADD CONSTRAINT CHK_MEM_IS_DELETED CHECK (MEM_IS_DELETED IN (0, 1));
--
-- ALTER TABLE MEMBER
--     ADD CONSTRAINT CHK_MEM_TYPE CHECK (MEM_TYPE IN ('고등교사', '기타', '기업', '학생', '초등교사', '중등교사', '학부모', '학원'));
--
-- ALTER TABLE NOTICE
--     ADD CONSTRAINT CHK_NOT_TYPE CHECK (NOT_TYPE IN ('서비스', '작업'));
--
-- ALTER TABLE PASSAGE
--     ADD CONSTRAINT CHK_PAS_IS_DELETED CHECK (PAS_IS_DELETED IN (0, 1));
--
-- ALTER TABLE PASSAGE
--     ADD CONSTRAINT CHK_PAS_IS_FAVORITE CHECK (PAS_IS_FAVORITE IN (0, 1));
--
-- ALTER TABLE SUBJECT
--     ADD CONSTRAINT CHK_SUB_TYPE CHECK (SUB_TYPE IN ('기술', '문화', '사회', '예술', '인문', '과학'));
--
-- ALTER TABLE TICKET
--     ADD CONSTRAINT CHK_TIC_NUMBER CHECK (TIC_NUMBER IN (10, 50, 100));
--
-- ALTER TABLE TICKET
--     ADD CONSTRAINT CHK_TIC_PRICE CHECK (TIC_PRICE IN (10000, 40000, 70000));
--
-- ALTER TABLE USAGE
--     ADD CONSTRAINT CHK_USA_TYPE CHECK (USA_TYPE IN ('문항 생성', '이용권 결제', '오픈 이벤트', '지문 생성'));