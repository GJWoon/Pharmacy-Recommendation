package com.dev.pharmacy.direction.service;


import com.dev.pharmacy.api.dto.DocumentDto;
import com.dev.pharmacy.direction.entity.Direction;
import com.dev.pharmacy.pharmacy.dto.PharmacyDto;
import com.dev.pharmacy.pharmacy.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT =3 ; // 약국 최대 검색 갯수
    private static final double RADIUS_KM = 10.0; // 검색 반경
    private final PharmacySearchService pharmacySearchService;
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

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

}
