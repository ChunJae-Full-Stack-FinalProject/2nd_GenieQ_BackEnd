package com.cj.genieq.passage.service;

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
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {


    // ✅ 폰트 경로 설정
    private static final String FONT_PATH = "src/main/resources/fonts/NanumGothic.ttf";

    public byte[] createPdfFromJson(String jsonData) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // ✅ 한글 폰트 적용
            PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(FONT_PATH), "Identity-H");
            document.setFont(font);

            // ✅ JSON 데이터 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);

            // ✅ 지문 정보 출력
            document.add(new Paragraph("제목: " + rootNode.get("title").asText()));
            document.add(new Paragraph("유형: " + rootNode.get("type").asText()));
            document.add(new Paragraph("키워드: " + rootNode.get("keyword").asText()));
            document.add(new Paragraph("\n[내용]"));
            document.add(new Paragraph(rootNode.get("content").asText()));

            document.add(new Paragraph("\n[요지]"));
            document.add(new Paragraph(rootNode.get("gist").asText()));

            // ✅ 문제 출력
            document.add(new Paragraph("\n[문제]"));
            JsonNode questions = rootNode.get("questions");
            for (JsonNode question : questions) {
                document.add(new Paragraph("\nQ: " + question.get("queQuery").asText()));

                // ✅ 선택지 출력
                List list = new List();
                for (JsonNode option : question.get("queOption")) {
                    list.add(new ListItem(option.asText()));
                }
                document.add(list);

                // ✅ 정답 출력
                document.add(new Paragraph("정답: " + question.get("queAnswer").asText()));
            }

            document.close();

            return baos.toByteArray(); // PDF 데이터를 반환
        } catch (Exception e) {
            throw new RuntimeException("PDF 생성 실패", e);
        }
    }
}
