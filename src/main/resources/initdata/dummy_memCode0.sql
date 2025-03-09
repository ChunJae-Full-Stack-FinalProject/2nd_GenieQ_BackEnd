SELECT * FROM MEMBER;
SELECT * FROM NOTICE;
SELECT * FROM PASSAGE;
SELECT * FROM PAYMENT;
SELECT * FROM QUESTION;
SELECT * FROM SUBJECT;
SELECT * FROM TICKET;
SELECT * FROM USAGE;
-- 제일 밑에 연결된 더미 데이터 정보 조회 쿼리를 포함하고 있습니다.

-- 회원 데이터 추가
INSERT INTO MEMBER (MEM_CODE, MEM_NAME, MEM_EMAIL, MEM_PASSWORD, MEM_GENDER, MEM_TYPE, MEM_IS_DELETED)
VALUES (SEQ_MEM_NO.nextval, '김교사', 'teacher@gmail.com', 'password123', '남성', '중등교사', 0);

-- 주제 데이터 추가
INSERT INTO SUBJECT (SUB_CODE, SUB_TYPE, SUB_KEYWORD)
VALUES (SEQ_SUB_NO.nextval, '인문', '현대 문학의 특징과 발전 과정');

INSERT INTO SUBJECT (SUB_CODE, SUB_TYPE, SUB_KEYWORD)
VALUES (SEQ_SUB_NO.nextval, '예술', '인상주의 미술의 탄생과 주요 특징');

INSERT INTO SUBJECT (SUB_CODE, SUB_TYPE, SUB_KEYWORD)
VALUES (SEQ_SUB_NO.nextval, '사회', '경제학의 기본 개념과 원리');

INSERT INTO SUBJECT (SUB_CODE, SUB_TYPE, SUB_KEYWORD)
VALUES (SEQ_SUB_NO.nextval, '문화', '전통문화의 현대적 변용과 의의');

INSERT INTO SUBJECT (SUB_CODE, SUB_TYPE, SUB_KEYWORD)
VALUES (SEQ_SUB_NO.nextval, '과학', '양자역학의 탄생 배경과 주요 원리');

INSERT INTO SUBJECT (SUB_CODE, SUB_TYPE, SUB_KEYWORD)
VALUES (SEQ_SUB_NO.nextval, '기술', '인공지능 기술의 발전 과정과 사회적 영향');

-- 지문 데이터 추가
INSERT INTO PASSAGE (PAS_CODE, PAS_TITLE, PAS_CONTENT, PAS_GIST, PAS_DATE, PAS_IS_FAVORITE, PAS_IS_DELETED, MEM_CODE, SUB_CODE)
VALUES (SEQ_PAS_NO.nextval, '현대 문학의 흐름', '현대 문학은 19세기 말부터 현재까지 이어져온 다양한 문학적 움직임을 포괄합니다. 현대 문학은 전통적인 문학 형식과 구분되는 특징을 가지고 있으며, 산업화, 세계대전, 정보화 등의 사회적 변화를 반영합니다. 근대화 과정에서 발생한 개인의 소외, 실존적 문제, 정체성 탐구 등이 주요 주제로 등장하였습니다. 특히 20세기 초반의 모더니즘은 의식의 흐름 기법, 파편화된 서술, 비선형적 구조 등 혁신적인 기법을 도입했습니다. 이후 포스트모더니즘으로 넘어오면서 메타픽션, 상호텍스트성, 장르의 해체 등이 두드러지게 나타납니다.',
        '현대 문학의 특징과 발전 과정', TO_DATE('2023-10-10', 'YYYY-MM-DD'), 1, 0, 0, 0);

INSERT INTO PASSAGE (PAS_CODE, PAS_TITLE, PAS_CONTENT, PAS_GIST, PAS_DATE, PAS_IS_FAVORITE, PAS_IS_DELETED, MEM_CODE, SUB_CODE)
VALUES (SEQ_PAS_NO.nextval, '인상주의 미술의 특징', '인상주의는 19세기 후반 프랑스에서 시작된 미술 운동으로, 전통적인 미술 기법에서 벗어나 빛과 색채의 순간적인 인상을 포착하고자 했습니다. 인상주의 화가들은 실외에서 직접 그림을 그리며 자연광의 변화를 관찰했고, 빠른 붓터치와 밝은 색채를 사용했습니다. 모네, 르누아르, 드가와 같은 화가들이 대표적이며, 이들의 작품은 당시 미술계에서 큰 논란을 일으켰지만 현대 미술의 발전에 중요한 토대가 되었습니다.',
        '인상주의 미술의 탄생과 주요 특징', TO_DATE('2023-11-12', 'YYYY-MM-DD'), 0, 0, 0, 1);

INSERT INTO PASSAGE (PAS_CODE, PAS_TITLE, PAS_CONTENT, PAS_GIST, PAS_DATE, PAS_IS_FAVORITE, PAS_IS_DELETED, MEM_CODE, SUB_CODE)
VALUES (SEQ_PAS_NO.nextval, '현대 경제학의 기본 원리', '경제학은 희소한 자원을 어떻게 배분할 것인가에 대한 학문으로, 현대 경제학은 크게 미시경제학과 거시경제학으로 나뉩니다. 미시경제학은 개인과 기업의 의사결정과 시장 작동 원리를 연구하며, 거시경제학은 국가 경제의 총체적 움직임과 정책을 다룹니다. 경제학의 기본 원리에는 기회비용, 한계효용, 수요와 공급, 균형가격 등의 개념이 있으며, 이러한 개념들은 합리적 선택과 자원의 효율적 배분을 이해하는 데 필수적입니다.',
        '경제학의 기본 개념과 원리', TO_DATE('2023-12-30', 'YYYY-MM-DD'), 1, 1, 0, 2);

INSERT INTO PASSAGE (PAS_CODE, PAS_TITLE, PAS_CONTENT, PAS_GIST, PAS_DATE, PAS_IS_FAVORITE, PAS_IS_DELETED, MEM_CODE, SUB_CODE)
VALUES (SEQ_PAS_NO.nextval, '한국 전통문화의 현대적 계승', '한국의 전통문화는 역사적 깊이와 독특한 특성을 지니고 있으며, 현대사회에서도 다양한 형태로 계승되고 있습니다. 전통 음악, 무용, 공예, 건축 등은 현대적 해석과 창조적 변형을 통해 새로운 가치를 창출하고 있습니다. 특히 한류 열풍과 함께 전통문화의 요소가 대중문화에 접목되면서 국내외적으로 큰 관심을 받고 있습니다. 이러한 전통과 현대의 만남은 문화적 정체성을 유지하면서도 시대적 변화에 적응하는 중요한 과정입니다.',
        '전통문화의 현대적 변용과 의의', TO_DATE('2024-01-15', 'YYYY-MM-DD'), 1, 0, 0, 3);

INSERT INTO PASSAGE (PAS_CODE, PAS_TITLE, PAS_CONTENT, PAS_GIST, PAS_DATE, PAS_IS_FAVORITE, PAS_IS_DELETED, MEM_CODE, SUB_CODE)
VALUES (SEQ_PAS_NO.nextval, '양자역학의 기본 개념', '양자역학은 20세기 초에 발전한 물리학 이론으로, 원자 및 아원자 수준의 현상을 설명합니다. 고전역학으로는 설명할 수 없었던 블랙바디 복사, 광전효과, 원자 스펙트럼 등의 현상을 해석하기 위해 등장했습니다. 양자역학의 핵심 개념에는 파동-입자 이중성, 불확정성 원리, 양자 중첩 상태 등이 있으며, 이러한 개념들은 우리의 직관과 일상 경험에 위배되는 경우가 많습니다. 그럼에도 불구하고 양자역학은 현대 과학기술의 발전에 결정적인 역할을 하고 있습니다.',
        '양자역학의 탄생 배경과 주요 원리', TO_DATE('2024-04-17', 'YYYY-MM-DD'), 0, 1, 0, 4);

INSERT INTO PASSAGE (PAS_CODE, PAS_TITLE, PAS_CONTENT, PAS_GIST, PAS_DATE, PAS_IS_FAVORITE, PAS_IS_DELETED, MEM_CODE, SUB_CODE)
VALUES (SEQ_PAS_NO.nextval, '인공지능의 발전과 미래', '인공지능(AI)은 인간의 학습, 추론, 지각, 문제 해결 능력 등을 컴퓨터로 구현하는 기술입니다. 초기의 단순한 알고리즘에서 시작하여 머신러닝, 딥러닝을 거쳐 현재는 다양한 분야에서 혁신을 일으키고 있습니다. 특히 자연어 처리, 컴퓨터 비전, 패턴 인식 등의 영역에서 비약적인 발전을 이루었으며, 의료, 금융, 교육, 교통 등 산업 전반에 적용되고 있습니다. 인공지능의 발전은 효율성과 편의성을 높이는 동시에 일자리 변화, 개인정보 보호, 윤리적 문제 등 새로운 과제를 제시하고 있습니다.',
        '인공지능 기술의 발전 과정과 사회적 영향', TO_DATE('2024-11-18', 'YYYY-MM-DD'), 0, 0, 0, 5);

-- 문제 데이터 추가 (JSON 예시 데이터 활용)
INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (0, '윗글의 내용 전개 방식으로 적절한 것은?',
        '① 화제에 대한 연구들이 시작된 사회적 배경을 분석하고 있다.
                    ② 화제에 대한 연구들을 선행 연구와 연결하여 설명하고 있다.
                    ③ 화제에 대한 연구들을 분류하는 기준의 문제점을 검토하고 있다.
                    ④ 화제에 대한 연구들을 소개한 후 남겨진 연구 과제를 제시하고 있다.
                    ⑤ 화제에 대한 연구들이 봉착했던 난관과 극복 과정을 소개하고 있다.',
        '② 해설해설해설', 0);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (1, '다음 중 글의 주제를 적절하게 파악하지 못한 것은?',
        '① 필자는 인간의 기억력과 학습 능력의 한계를 지적하고 있다.
                    ② 필자는 새로운 기술이 가져올 윤리적 문제를 논의하고 있다.
                    ③ 필자는 과거의 사례를 통해 현재 상황을 예측하고 있다.
                    ④ 필자는 사회적 문제를 해결하기 위한 방법을 제시하고 있다.
                    ⑤ 필자는 전통적 가치를 강조하면서 현대적 해석을 부정하고 있다.',
        '⑤ 해설해설해설', 1);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (3, '다음 글이 논리적으로 타당하지 않은 이유는?',
        '① 주장과 근거 사이에 논리적 연결이 부족하다.
                    ② 상반된 입장을 충분히 고려하지 않았다.
                    ③ 통계 자료의 출처가 명확하지 않다.
                    ④ 결론이 지나치게 단순화되어 있다.
                    ⑤ 문제의 원인보다 해결 방안에 초점을 맞추고 있다.',
        '③ 해설해설해설', 2);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (4, '다음 중 문맥상 적절하지 않은 단어는?',
        '① 그의 태도는 언제나 일관되었다.
                    ② 그녀는 자신의 감정을 솔직하게 표현했다.
                    ③ 이번 연구 결과는 매우 혁신적이었다.
                    ④ 그는 자신의 실수를 당당하게 인정했다.
                    ⑤ 이 법안은 시민들의 불만을 더욱 가중시켰다.',
        '④ 해설해설해설', 2);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (5, '다음 중 글의 내용과 일치하지 않는 것은?',
        '① 필자는 고대 문명의 발전 과정을 설명하고 있다.
                    ② 필자는 전통적 가치의 중요성을 강조하고 있다.
                    ③ 필자는 과학 기술의 발전이 가져온 문제를 제시하고 있다.
                    ④ 필자는 특정 시대의 문화적 특징을 분석하고 있다.
                    ⑤ 필자는 특정 이론이 현대 사회에 미치는 영향을 논의하고 있다.',
        '① 해설해설해설', 2);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (6, '다음 글의 암시된 의미로 적절한 것은?',
        '① 인간의 본성은 쉽게 변하지 않는다.
                    ② 과거의 경험이 현재의 행동에 영향을 미친다.
                    ③ 사회적 변화는 개인의 인식에 따라 달라진다.
                    ④ 지식의 축적은 인간의 발전을 가능하게 한다.
                    ⑤ 혁신은 기존 질서를 유지하는 과정에서 발생한다.',
        '④ 해설해설해설', 4);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (7, '다음 중 글의 논지를 왜곡한 해석은?',
        '① 필자는 경제 성장의 한계를 지적하고 있다.
                    ② 필자는 인간의 감정이 의사 결정에 미치는 영향을 설명하고 있다.
                    ③ 필자는 사회적 문제를 해결하기 위한 정부의 역할을 강조하고 있다.
                    ④ 필자는 개인의 자유가 사회적 책임보다 중요하다고 주장하고 있다.
                    ⑤ 필자는 역사적 사건을 통해 현재의 문제를 분석하고 있다.',
        '④ 해설해설해설', 1);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (8, '다음 중 문법적으로 올바르지 않은 문장은?',
        '① 그는 어제 친구와 영화를 보러 갔다.
                    ② 나는 내일 여행을 갈 예정이다.
                    ③ 그가 말한 내용은 매우 중요했다.
                    ④ 그녀는 자신의 의견을 분명히 말했다.
                    ⑤ 우리는 이번 주말에 산책을 갔다.',
        '⑤ 해설해설해설', 1);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (9, '다음 중 글에서 직접적으로 언급된 사실은?',
        '① 필자는 과거의 사례를 제시하고 있다.
                    ② 필자는 특정 연구 결과를 인용하고 있다.
                    ③ 필자는 문제 해결 방안을 제시하고 있다.
                    ④ 필자는 다양한 관점을 비교 분석하고 있다.
                    ⑤ 필자는 자신의 의견을 명확하게 드러내고 있다.',
        '① 해설해설해설', 5);

INSERT INTO QUESTION (QUE_CODE, QUE_QUERY, QUE_OPTION, QUE_ANSWER, PAS_CODE)
VALUES (10, '다음 중 글의 함의를 잘못 이해한 것은?',
        '① 필자는 경제적 불평등의 심화를 우려하고 있다.
                    ② 필자는 기술 발전이 노동 시장에 미치는 영향을 분석하고 있다.
                    ③ 필자는 역사적 사건이 현재의 정치적 환경을 결정한다고 주장하고 있다.
                    ④ 필자는 개인의 인식 변화가 사회적 변화를 촉진한다고 본다.
                    ⑤ 필자는 윤리적 문제를 고려하지 않는 기술 발전의 위험성을 경고하고 있다.',
        '③ 해설해설해설', 5);

-- 이용권 데이터 추가
INSERT INTO TICKET (TIC_CODE, TIC_NUMBER, TIC_PRICE)
VALUES (1, 10, 10000);

INSERT INTO TICKET (TIC_CODE, TIC_NUMBER, TIC_PRICE)
VALUES (2, 50, 40000);

INSERT INTO TICKET (TIC_CODE, TIC_NUMBER, TIC_PRICE)
VALUES (3, 100, 70000);

-- 결제 데이터 추가
INSERT INTO PAYMENT (PAY_CODE, PAY_PRICE, PAY_DATE, MEM_CODE, TIC_CODE)
VALUES (1, 40000, TO_DATE('2023-10-05', 'YYYY-MM-DD'), 0, 2);

-- 활동 내역 데이터 추가
INSERT INTO USAGE (USA_CODE, USA_TYPE, USA_DATE, USA_COUNT, USA_BALANCE, MEM_CODE)
VALUES (1, '회원가입', TO_DATE('2023-10-01 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 5, 0);

INSERT INTO USAGE (USA_CODE, USA_TYPE, USA_DATE, USA_COUNT, USA_BALANCE, MEM_CODE)
VALUES (2, '이용권 충전', TO_DATE('2023-10-05 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 50, 55, 0);

INSERT INTO USAGE (USA_CODE, USA_TYPE, USA_DATE, USA_COUNT, USA_BALANCE, MEM_CODE)
VALUES (3, '지문 생성', TO_DATE('2023-10-10 10:15:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 54, 0);

INSERT INTO USAGE (USA_CODE, USA_TYPE, USA_DATE, USA_COUNT, USA_BALANCE, MEM_CODE)
VALUES (4, '문항 생성', TO_DATE('2023-10-12 11:20:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 53, 0);

INSERT INTO USAGE (USA_CODE, USA_TYPE, USA_DATE, USA_COUNT, USA_BALANCE, MEM_CODE)
VALUES (5, '오픈 이벤트', TO_DATE('2023-11-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),10, 63,  0);

-- 공지사항 데이터 추가
INSERT INTO NOTICE (NOT_CODE, NOT_TYPE, NOT_TITLE, NOT_CONTENT, NOT_DATE)
VALUES (1, '서비스', '지니Q 서비스 오픈 안내', '안녕하세요. 지니Q 서비스가 오픈되었습니다. 지니Q는 AI를 활용한 국어 문제 생성 서비스로, 다양한 유형의 문제를 빠르고 효과적으로 제작할 수 있습니다. 많은 이용 부탁드립니다.', TO_DATE('2023-10-01', 'YYYY-MM-DD'));

INSERT INTO NOTICE (NOT_CODE, NOT_TYPE, NOT_TITLE, NOT_CONTENT, NOT_DATE)
VALUES (2, '작업', '서버 점검 안내', '안녕하세요. 보다 안정적인 서비스 제공을 위해 2023년 12월 15일 새벽 2시부터 5시까지 서버 점검이 예정되어 있습니다. 해당 시간에는 서비스 이용이 제한될 수 있으니 양해 부탁드립니다.', TO_DATE('2023-12-10', 'YYYY-MM-DD'));




-- 연결된 더미 데이터 정보 조회 쿼리

-- 회원 코드가 1인 회원이 저장한 삭제되지 않은 모든 지문 조회
SELECT m.MEM_CODE, p.PAS_CODE, p.PAS_TITLE, p.PAS_DATE
FROM MEMBER m
         JOIN PASSAGE p ON m.MEM_CODE = p.MEM_CODE
WHERE m.MEM_CODE = 0
  AND p.PAS_IS_DELETED = 0
ORDER BY p.PAS_DATE DESC;

-- 회원 코드가 1인 회원이 저장한 '인문' 주제의 삭제되지 않은 지문 조회
SELECT m.MEM_CODE, p.PAS_CODE, s.SUB_TYPE, p.PAS_TITLE
FROM MEMBER m
         JOIN PASSAGE p ON m.MEM_CODE = p.MEM_CODE
         JOIN SUBJECT s ON p.SUB_CODE = s.SUB_CODE
WHERE m.MEM_CODE = 1
  AND p.PAS_IS_DELETED = 0
  AND s.SUB_TYPE = '인문';

-- 회원 코드가 1인 회원이 저장한 삭제되지 않은 지문에 속한 삭제되지 않은 모든 문제 조회
SELECT m.MEM_CODE, q.QUE_CODE, p.PAS_CODE, p.PAS_TITLE
FROM MEMBER m
         JOIN PASSAGE p ON m.MEM_CODE = p.MEM_CODE
         JOIN QUESTION q ON p.PAS_CODE = q.PAS_CODE
WHERE m.MEM_CODE = 1
  AND p.PAS_IS_DELETED = 0
ORDER BY p.PAS_CODE;

-- 회원 코드가 1인 회원이 즐겨찾기한 삭제되지 않은 지문과 관련 정보 조회
SELECT m.MEM_CODE, p.PAS_CODE, p.PAS_TITLE, s.SUB_TYPE, p.PAS_DATE
FROM MEMBER m
         JOIN PASSAGE p ON m.MEM_CODE = p.MEM_CODE
         JOIN SUBJECT s ON p.SUB_CODE = s.SUB_CODE
WHERE m.MEM_CODE = 1
  AND p.PAS_IS_DELETED = 0
  AND p.PAS_IS_FAVORITE = 1
ORDER BY p.PAS_DATE DESC;

-- 회원 코드가 1인 회원의 2023년 10월 활동 내역 조회
SELECT m.MEM_CODE, m.MEM_NAME, u.USA_TYPE, u.USA_COUNT, u.USA_BALANCE, u.USA_DATE
FROM MEMBER m
         JOIN USAGE u ON m.MEM_CODE = u.MEM_CODE
WHERE m.MEM_CODE = 1
  AND u.USA_DATE BETWEEN TO_DATE('2023-10-01', 'YYYY-MM-DD') AND TO_DATE('2023-10-31', 'YYYY-MM-DD')
ORDER BY u.USA_DATE;

-- 회원 코드가 1인 회원의 모든 결제 내역과 구매한 이용권 정보 조회
SELECT m.MEM_CODE, p.PAY_CODE, p.PAY_PRICE, p.PAY_DATE, t.TIC_NUMBER, t.TIC_PRICE
FROM MEMBER m
         JOIN PAYMENT p ON m.MEM_CODE = p.MEM_CODE
         JOIN TICKET t ON p.TIC_CODE = t.TIC_CODE
WHERE m.MEM_CODE = 1
ORDER BY p.PAY_DATE DESC;