package com.impactzb.featuresservice.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class FeatureDto {
    private UUID id;
    private String name;
    private FeatureFlag featureFlag;

    public boolean isEnabled(){
        return FeatureFlag.ENABLED.equals(this.featureFlag);
    }

}
