package com.dev.pharmacy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetaDto {

    @JsonProperty("total_count")
    //실제 response의 값은 total_count지만 해당 값을 dto의 totalCount로 매핑시켜주는 역할

    private Integer totalCount;

}
