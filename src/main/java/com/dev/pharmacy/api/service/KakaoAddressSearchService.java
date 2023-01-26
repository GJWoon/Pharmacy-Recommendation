package com.dev.pharmacy.api.service;

import com.dev.pharmacy.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;
    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;
    public KakaoApiResponseDto requestAddressSearch(String address){

        //kakaoApi호출
        if(ObjectUtils.isEmpty(address)) return null;

        URI uri = kakaoUriBuilderService.builderUriByAddressSearch(address);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION,"KakaoAK "+kakaoRestApiKey);

        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

       return restTemplate.exchange(uri,HttpMethod.GET,httpEntity,KakaoApiResponseDto.class).getBody();

    }
}
