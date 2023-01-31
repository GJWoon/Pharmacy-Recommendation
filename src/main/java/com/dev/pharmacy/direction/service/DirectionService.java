package com.dev.pharmacy.direction.service;


import com.dev.pharmacy.api.dto.DocumentDto;
import com.dev.pharmacy.api.service.KakaoCategorySearchService;
import com.dev.pharmacy.direction.entity.Direction;
import com.dev.pharmacy.direction.repository.DirectionRepository;
import com.dev.pharmacy.pharmacy.dto.PharmacyDto;
import com.dev.pharmacy.pharmacy.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT =3 ; // 약국 최대 검색 갯수
    private static final double RADIUS_KM = 10.0; // 검색 반경
    private final PharmacySearchService pharmacySearchService;
    private final DirectionRepository directionRepository;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final Base62Service base62Service;

    @Transactional
    public List<Direction> saveAll(List<Direction> directionList){
        if(CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
        return directionRepository.saveAll(directionList);
    }

    public Direction findById(String encodedId){
      Long decodedId =  base62Service.decodeDirectionId(encodedId);
      return directionRepository.findById(decodedId).orElse(null);
    }

    public List<Direction> buildDirectionList(DocumentDto dto){

        // 약국 데이터 조회
        List<Direction> pharmacyDtoList = pharmacySearchService.searchPharmacyDtoList()
                .stream()
                .map(e-> Direction.builder()
                        .inputAddress(dto.getAddressName())
                        .inputLatitude(dto.getLatitude())
                        .inputLongitude(dto.getLongitude())
                        .targetPharmacyName(e.getPharmacyName())
                        .targetLatitude(e.getLatitude())
                        .targetLongitude(e.getLongitude())
                        .targetAddress(e.getPharmacyAddress())
                        .distance(
                                calculateDistance(dto.getLatitude(),dto.getLongitude(),e.getLatitude(),e.getLongitude())
                        )
                        .build()
                )
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());


        // 거리 계산 알고리즘을 이용하여 고객과 약국사이의 거리를 계산하고 가까운 순으로 sort

        return pharmacyDtoList;
    }
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestPharmacyCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetPharmacyName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

}
