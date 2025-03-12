package com.cj.genieq.passage.service;

import com.cj.genieq.passage.dto.request.PassageWithQuestionsRequestDto;
import com.cj.genieq.question.dto.request.QuestionInsertRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {


    // ✅ 폰트 경로 설정
    private static final String FONT_PATH = "src/main/resources/fonts/NanumGothic.ttf";


    public byte[] createPdfFromDto(PassageWithQuestionsRequestDto dto) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // ✅ 폰트 설정
            PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(FONT_PATH), "Identity-H");
            document.setFont(font);

            // ✅ 제목 출력 (Bold)
            Text title = new Text("제목: " + dto.getTitle());
            document.add(new Paragraph(title));

            // ✅ 유형 및 키워드 출력
            document.add(new Paragraph("유형: " + dto.getType()));
            document.add(new Paragraph("키워드: " + dto.getKeyword()));

            // ✅ 본문 출력 (Bold)
            document.add(new Paragraph(new Text("\n[내용]")));
            document.add(new Paragraph(dto.getContent()));

            // ✅ 요지 출력 (Bold)
            document.add(new Paragraph(new Text("\n[요지]")));
            document.add(new Paragraph(dto.getGist()));

            // ✅ 문제 출력
            if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
                document.add(new Paragraph(new Text("\n[문제]")));

                for (QuestionInsertRequestDto question : dto.getQuestions()) {
                    // ✅ 문제 출력 (Bold)
                    Text questionText = new Text("\nQ: " + question.getQueQuery());
                    document.add(new Paragraph(questionText));

                    // ✅ 선택지 출력
                    List list = new List();
                    for (String option : question.getQueOption()) {
                        list.add(new ListItem(option));
                    }
                    document.add(list);

                    // ✅ 정답 출력 (Italic)
                    Text answerText = new Text("정답: " + question.getQueAnswer());
                    document.add(new Paragraph(answerText));
                }
            }

            document.close();

            return baos.toByteArray(); // PDF 데이터를 반환
        } catch (Exception e) {
            throw new RuntimeException("PDF 생성 실패", e);
        }
    }
}