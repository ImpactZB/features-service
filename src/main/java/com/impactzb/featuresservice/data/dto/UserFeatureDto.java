package com.impactzb.featuresservice.data.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Builder
@Data
public class UserFeatureDto {
    private UUID id;
    private String userName;
    private UUID featureId;
    private FeatureFlag featureFlag;

    public boolean isEnabled(){
        return FeatureFlag.ENABLED.equals(this.featureFlag);
    }

}
