/*
 * Copyright (c) 2025 LY Corporation. All rights reserved.
 * LY Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.law.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.law.configuration.DataPathConfig;
import com.law.entity.Judgement;
import com.law.repository.JudgementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class InitJudgementService {

    private final DataPathConfig dataPathConfig;

    private final JudgementRepository judgementRepository;

    @PostConstruct
    public void init() {
        System.out.println("init");
        System.out.println(judgementRepository.findTop1ByOrderByCreatedTimeDesc().map(Judgement::getCreatedTime).orElse(0L));

        File archivedFolder = new File(dataPathConfig.getArchivedPath());
        if (!archivedFolder.exists()) {
            archivedFolder.mkdirs();
        }

        try (Stream<Path> paths = Files.list(Paths.get(dataPathConfig.getDataPath()))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                String fileName = path.getFileName().toString();
                System.out.println(fileName);
//                try {
//                    BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
//                    System.out.println("fileName: " + fileName);
//                    System.out.println("lastRecordCreatedTime: " + lastRecordCreatedTime);
//                    System.out.println("creationTime: " + attr.creationTime().toMillis());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }

                String content = "";
                if (path.toString().endsWith(".docx")) {
                    try {
                        content = readDocxFile(path.toFile());
                        System.out.println(Files.move(path, Paths.get(archivedFolder.getAbsolutePath(), fileName)));
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }

                if (path.toString().endsWith(".pdf")) {
                    try {
                        content = readPdfFile(path.toFile());
                        Files.move(path, Paths.get(archivedFolder.getAbsolutePath(), fileName));
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }

                if (StringUtils.isNotBlank(content)) {
                    Judgement judgement = new Judgement();
                    judgement.setTitle(fileName);
                    judgement.setContent(content);
                    judgement.setCreatedTime(System.currentTimeMillis());
                    judgementRepository.save(judgement);
                } else {
                    log.error("failed to read file: {}", fileName);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String readDocxFile(File file) throws IOException {
        try(FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
            XWPFDocument document = new XWPFDocument(fis);

            List<XWPFParagraph> paragraphs = document.getParagraphs();

            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph para : paragraphs) {
                sb.append(para.getText());
            }
            return sb.toString();
        }
    }

    private String readPdfFile(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
