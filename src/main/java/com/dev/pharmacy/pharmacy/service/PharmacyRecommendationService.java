package com.dev.pharmacy.pharmacy.service;


import com.dev.pharmacy.api.dto.DocumentDto;
import com.dev.pharmacy.api.dto.KakaoApiResponseDto;
import com.dev.pharmacy.api.service.KakaoAddressSearchService;
import com.dev.pharmacy.direction.entity.Direction;
import com.dev.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyRecommendationService {


    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    public void recommendPharmacyList(String address){

     KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

     if(Objects.isNull(kakaoApiResponseDto)){
         log.error("[PharmacyRecommendationService error!!] address:{}",address);
         return;
     }
     DocumentDto document = kakaoApiResponseDto.getDocumentList().get(0);

     List<Direction> directionList = directionService.buildDirectionList(document);

     directionService.saveAll(directionList);

    }

}
