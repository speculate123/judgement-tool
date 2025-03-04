/*
 * Copyright (c) 2025 LY Corporation. All rights reserved.
 * LY Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.law.service;

import java.util.List;

import com.law.configuration.DataPathConfig;
import com.law.entity.Judgement;
import com.law.model.JudgementView;
import com.law.repository.JudgementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class QueryService {

    private final DataPathConfig dataPathConfig;

    private final JudgementRepository judgementRepository;

    public List<JudgementView> query(String query, Integer page, Integer size) {
        var found = judgementRepository.findBySearchOnAllFields(query, Pageable.ofSize(size).withPage(page));

        return toView(found.getContent());
    }

    private List<JudgementView> toView(List<Judgement> judgements) {
        return judgements.stream().map(j -> {
            var view = new JudgementView();
            if (j.getTitle().contains("pdf")) {
                view.setFilePath(String.format("file://%s/%s", dataPathConfig.getArchivedPath(), j.getTitle()));
            }
            view.setTitle(j.getTitle());
            view.setContent(j.getContent());
            return view;
        }).toList();
    }
}
