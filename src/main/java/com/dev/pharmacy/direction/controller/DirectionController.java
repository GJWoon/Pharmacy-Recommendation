package com.dev.pharmacy.direction.controller;

import com.dev.pharmacy.direction.entity.Direction;
import com.dev.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;
    private static final String DIRECTION_BASE_URL ="https://map.kakao.com/link/map/";

    @GetMapping("/dir/{encodedId}")
    public String searchDirection(@PathVariable String encodedId){

        Direction direction =directionService.findById(encodedId);
        String param = String.join(",",direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()),String.valueOf(direction.getTargetLongitude()));

        String uriResult = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL+param).toUriString();

        return "redirect:"+uriResult;
    }
}
