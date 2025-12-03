package kr.co.manuals.common.api.application.export;

import kr.co.manuals.common.api.application.ApiService;
import kr.co.manuals.common.api.application.dto.ApiDiscoveryResult;
import kr.co.manuals.common.api.application.support.SwaggerSuggester;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiDocExportService {
    private final ApiService apiService;
    private final SwaggerSuggester swaggerSuggester;

    private static record Row(String registerType, String methodType, String apiNm, String apiUrl, String apiDsctn, boolean registered) {}

    public byte[] exportDocx(Boolean changed, Boolean registered, String type) {
        try (XWPFDocument doc = new XWPFDocument(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            addTitle(doc, "API 명세서");
            addParagraph(doc, "생성일: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            addParagraph(doc, "");

            List<ApiDiscoveryResult> discovered = new ArrayList<>(
                Optional.ofNullable(apiService.discoverAllWithUnregistered(changed, registered))
                    .orElse(Collections.emptyList()));
            if (changed != null) {
                discovered = discovered.stream().filter(d -> d.changed() == changed).collect(Collectors.toList());
            }
            if (registered != null) {
                discovered = discovered.stream().filter(d -> d.registered() == registered).collect(Collectors.toList());
            }

            Set<String> suggesterKeys = swaggerSuggester.endpoints().stream()
                    .map(ep -> buildKey(ep.method(), ep.path()))
                    .collect(Collectors.toSet());
            boolean canJudgeDeleted = !suggesterKeys.isEmpty();

            List<Row> rows = discovered.stream()
                    .map(d -> {
                        String t;
                        if (d.registered()) {
                            String key = buildKey(d.methodType(), d.apiUrl());
                            if (canJudgeDeleted && !suggesterKeys.contains(key)) t = "삭제";
                            else t = (d.changed() ? "변경" : "등록");
                        } else {
                            t = "미등록";
                        }
                        return new Row(t, d.methodType(), orEmpty(d.apiNm()), d.apiUrl(), orEmpty(d.apiDsctn()), d.registered());
                    })
                    .sorted(Comparator.comparing(Row::registerType).thenComparing(Row::methodType).thenComparing(Row::apiUrl))
                    .toList();

            String typeLabel = normalizeTypeLabel(type);
            if (typeLabel != null) {
                rows = rows.stream().filter(r -> typeLabel.equals(r.registerType())).collect(Collectors.toList());
            }

            long cntAll = rows.size();
            long cntReg = rows.stream().filter(r -> "등록".equals(r.registerType())).count();
            long cntUnreg = rows.stream().filter(r -> "미등록".equals(r.registerType())).count();
            long cntChanged = rows.stream().filter(r -> "변경".equals(r.registerType())).count();
            long cntDeleted = rows.stream().filter(r -> "삭제".equals(r.registerType())).count();
            addParagraph(doc, "요약: 전체 %d, 등록 %d, 미등록 %d, 변경 %d, 삭제 %d".formatted(cntAll, cntReg, cntUnreg, cntChanged, cntDeleted));
            addParagraph(doc, "");

            addListTable(doc, rows);

            addHeading(doc, "상세");
            for (Row r : rows) {
                addSubHeading(doc, r.methodType() + " " + r.apiUrl());
                addParagraph(doc, "명칭: " + safe(r.apiNm()));
                addParagraph(doc, "설명: " + safe(r.apiDsctn()));
                addParagraph(doc, "상태: " + r.registerType());

                SwaggerSuggester.Examples ex = swaggerSuggester.suggest(r.methodType(), r.apiUrl());
                addCodeBlock(doc, "Request Headers", ex.getHeader());
                addCodeBlock(doc, "Request Body", ex.getBody());
                addCodeBlock(doc, "Response", ex.getResponse());
            }

            doc.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            try (XWPFDocument fallback = new XWPFDocument(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                addTitle(fallback, "API 명세서");
                addParagraph(fallback, "문서 생성 중 오류가 발생했습니다: " + e.getMessage());
                fallback.write(bos);
                return bos.toByteArray();
            } catch (Exception ex) {
                return ("Export failed: " + e.getMessage()).getBytes(StandardCharsets.UTF_8);
            }
        }
    }

    private String orEmpty(String s){ return s == null ? "" : s; }
    private String safe(String s){ return (s == null || s.isBlank()) ? "-" : s; }

    private String buildKey(String m, String p){ return ((m==null?"":m).toUpperCase(Locale.ROOT)+":"+normalize(p)).toUpperCase(Locale.ROOT); }
    private String normalize(String p){ if(p==null) return ""; String r=p.trim().replaceAll("//+","/"); if(r.endsWith("/")&&r.length()>1) r=r.substring(0,r.length()-1); return r; }

    private void addTitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setStyle("Title");
        XWPFRun run = p.createRun();
        run.setBold(true);
        run.setFontSize(18);
        run.setText(text);
    }

    private void addHeading(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setBold(true);
        run.setFontSize(14);
        run.setText(text);
    }

    private void addSubHeading(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setBold(true);
        run.setFontSize(12);
        run.setText(text);
    }

    private void addParagraph(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setFontSize(10);
        run.setText(text == null ? "" : text);
    }

    private void addListTable(XWPFDocument doc, List<Row> rows) {
        int cols = 5;
        XWPFTable table = doc.createTable(Math.max(1, rows.size() + 1), cols);
        setCellText(table.getRow(0).getCell(0), "등록구분", true);
        setCellText(table.getRow(0).getCell(1), "Method", true);
        setCellText(table.getRow(0).getCell(2), "API명", true);
        setCellText(table.getRow(0).getCell(3), "URL", true);
        setCellText(table.getRow(0).getCell(4), "설명", true);

        for (int i = 0; i < rows.size(); i++) {
            Row r = rows.get(i);
            int rowIdx = i + 1;
            setCellText(table.getRow(rowIdx).getCell(0), r.registerType(), false);
            setCellText(table.getRow(rowIdx).getCell(1), r.methodType(), false);
            setCellText(table.getRow(rowIdx).getCell(2), safe(r.apiNm()), false);
            setCellText(table.getRow(rowIdx).getCell(3), r.apiUrl(), false);
            setCellText(table.getRow(rowIdx).getCell(4), safe(r.apiDsctn()), false);
        }
        addParagraph(doc, "");
    }

    private void setCellText(XWPFTableCell cell, String text, boolean header) {
        cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        XWPFRun run = p.createRun();
        if (header) run.setBold(true);
        run.setFontSize(10);
        run.setText(text == null ? "" : text);
    }

    private void addCodeBlock(XWPFDocument doc, String title, String json) {
        if (json == null) json = "";
        if (json.isBlank()) return;
        addParagraph(doc, title + ":");
        String[] lines = json.split("\n");
        for (String line : lines) {
            XWPFParagraph p = doc.createParagraph();
            XWPFRun run = p.createRun();
            run.setFontFamily("Consolas");
            run.setFontSize(9);
            run.setText(line);
        }
        addParagraph(doc, "");
    }

    private String normalizeTypeLabel(String type) {
        if (type == null || type.isBlank()) return null;
        String t = type.trim().toLowerCase(Locale.ROOT);
        return switch (t) {
            case "deleted" -> "삭제";
            case "changed" -> "변경";
            case "unregistered" -> "미등록";
            case "registered" -> "등록";
            default -> {
                if (Set.of("삭제","변경","미등록","등록").contains(type)) yield type;
                yield null;
            }
        };
    }
}
