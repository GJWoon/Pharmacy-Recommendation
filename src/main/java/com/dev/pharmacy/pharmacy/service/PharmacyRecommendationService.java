package com.dev.pharmacy.pharmacy.service;


import com.dev.pharmacy.api.dto.DocumentDto;
import com.dev.pharmacy.api.dto.KakaoApiResponseDto;
import com.dev.pharmacy.api.service.KakaoAddressSearchService;
import com.dev.pharmacy.direction.dto.OutputDto;
import com.dev.pharmacy.direction.entity.Direction;
import com.dev.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyRecommendationService {


    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    private static final String ROAD_VIEW_BASE_URL ="https://map.kakao.com/link/roadview/";
    private static final String DIRECTION_BASE_URL ="https://map.kakao.com/link/map/";

    public List<OutputDto> recommendPharmacyList(String address){

     KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

     if(Objects.isNull(kakaoApiResponseDto)){
         log.error("[PharmacyRecommendationService error!!] address:{}",address);
     }
     DocumentDto document = kakaoApiResponseDto.getDocumentList().get(0);

     List<Direction> directionList = directionService.buildDirectionList(document);

     // 카카오 카테고리 API를 사용
     //List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        directionService.saveAll(directionList);
        return directionList.stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction) {

        String param = String.join(",",direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()),String.valueOf(direction.getTargetLongitude()));

        String uriResult = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL+param).toUriString();

        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(uriResult)
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() +","+direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
