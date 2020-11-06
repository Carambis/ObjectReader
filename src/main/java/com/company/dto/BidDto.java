package com.company.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonTypeName("bid")
public class BidDto {
    @JsonProperty("id")
    public String id;
    @JsonProperty("ts")
    public String timestamp;
    @JsonProperty("ty")
    public String type;
    @JsonProperty("pl")
    public String payload;
}
