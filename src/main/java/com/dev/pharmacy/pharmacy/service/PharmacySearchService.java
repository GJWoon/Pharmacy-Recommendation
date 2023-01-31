package com.dev.pharmacy.pharmacy.service;


import com.dev.pharmacy.pharmacy.cache.PharmacyRedisTemplateService;
import com.dev.pharmacy.pharmacy.dto.PharmacyDto;
import com.dev.pharmacy.pharmacy.entity.Pharmacy;
import com.dev.pharmacy.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacySearchService {
    private final PharmacyRepository pharmacyRepository;

    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    public List<PharmacyDto> searchPharmacyDtoList() {

        //redis
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();

        if(!pharmacyDtoList.isEmpty()) return pharmacyDtoList;

        //db
        return pharmacyRepository.findAll()
                .stream().map(this::convertToPharmacyDto)
                .collect(Collectors.toList());
    }

    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .pharmacyName(pharmacy.getPharmacyName())
                .build();
    }
}
