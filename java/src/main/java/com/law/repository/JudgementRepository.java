/*
 * Copyright (c) 2025 LY Corporation. All rights reserved.
 * LY Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.law.repository;

import java.util.List;
import java.util.Optional;

import com.law.entity.Judgement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgementRepository extends ElasticsearchRepository<Judgement, String> {
    Optional<Judgement> findTop1ByOrderByCreatedTimeDesc();

    @Query("""
           {
             "query_string": {
               "query": "?0"
             }
           }
           """)
    Page<Judgement> findBySearchOnAllFields(String search, Pageable pageable);
}

//interface JudgementRepositoryCustom {
//    long abc();
//}

//@Component
//@RequiredArgsConstructor
//class StaticListV2RepositoryCustomImpl implements JudgementRepositoryCustom {
//
//    private final ElasticsearchOperations elasticsearchOperations;
//
//    @Override
//    public long abc() {
//        SearchHits<Judgement> judgements = elasticsearchOperations.search(
//            NativeQuery.builder()
//                       .withQuery(q -> q.queryString(QueryStringQuery.builder().query("abc").build()))
//                       .withSort(Sort.by(Sort.Direction.DESC, "createdTime"))
//                       .withPageable(PageRequest.of(0, 1))
//                       .build(),
//            Judgement.class
//        );
//        Judgement lastRecord = judgements.stream().findFirst()
//                                         .map(SearchHit::getContent)
//                                         .orElse(null);
//        return lastRecord == null ? null : lastRecord.getCreatedTime();
//    }
//}
