package com.cj.genieq.passage.service;

import com.cj.genieq.passage.dto.response.PassageWithQuestionsResponseDto;
import com.cj.genieq.question.dto.response.QuestionSelectResponseDto;
import org.apache.poi.xwpf.usermodel.*;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class WordService {

    private String stripHtmlTags(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        // HTML 태그 제거 정규식
        return html.replaceAll("<[^>]*>", "");
    }

    public byte[] createWordFromDto(PassageWithQuestionsResponseDto dto) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // ✅ Word 문서 생성
            XWPFDocument document = new XWPFDocument();

            // ✅ 제목 작성 (Bold)
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText("제목: " + dto.getTitle());
            titleRun.setBold(true);
            titleRun.setFontSize(14);
            titleParagraph.setSpacingAfter(200); // 줄 간격 설정

            // ✅ 본문 작성 (Bold 처리)
            XWPFParagraph contentTitleParagraph = document.createParagraph();
            XWPFRun contentTitleRun = contentTitleParagraph.createRun();
            contentTitleRun.setText("[내용]");
            contentTitleRun.setBold(true);
            contentTitleRun.setFontSize(12);

            XWPFParagraph contentParagraph = document.createParagraph();
            XWPFRun contentRun = contentParagraph.createRun();
            contentRun.setText(stripHtmlTags(dto.getContent()));
            contentRun.setFontSize(12);

            // ✅ 문제 작성
            if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
                XWPFParagraph questionTitleParagraph = document.createParagraph();
                XWPFRun questionTitleRun = questionTitleParagraph.createRun();
                questionTitleRun.setText("[문제]");
                questionTitleRun.setBold(true);
                questionTitleRun.setFontSize(12);

                for (QuestionSelectResponseDto question : dto.getQuestions()) {
                    // ✅ 문제 출력 (Bold)
                    XWPFParagraph questionParagraph = document.createParagraph();
                    XWPFRun questionRun = questionParagraph.createRun();
                    questionRun.setText("Q: " + question.getQueQuery());
                    questionRun.setBold(true);
                    questionRun.setFontSize(12);

                    // ✅ 선택지 출력
                    for (String option : question.getQueOption()) {
                        XWPFParagraph optionParagraph = document.createParagraph();
                        XWPFRun optionRun = optionParagraph.createRun();
                        optionRun.setText(" - " + option);
                        optionRun.setFontSize(12);
                    }

                    // ✅ 정답 출력 (Italic 처리)
                    XWPFParagraph answerParagraph = document.createParagraph();
                    XWPFRun answerRun = answerParagraph.createRun();
                    answerRun.setText("정답: " + question.getQueAnswer());
                    answerRun.setItalic(true);
                    answerRun.setFontSize(12);
                }
            }

            // ✅ 문서 작성 후 닫기
            document.write(baos);
            document.close();

            return baos.toByteArray(); // Word 데이터를 바이트 배열로 반환
        } catch (Exception e) {
            throw new RuntimeException("Word 파일 생성 실패", e);
        }
    }
}

