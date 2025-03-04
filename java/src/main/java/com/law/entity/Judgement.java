/*
 * Copyright (c) 2025 LY Corporation. All rights reserved.
 * LY Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.law.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "judgement")
@Data
@NoArgsConstructor
public class Judgement {
    @Id
    private String id;

    private String title;

    private String content;

    @Field(type = FieldType.Date)
    private long createdTime;

}
