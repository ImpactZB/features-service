package com.impactzb.featuresservice;

import com.impactzb.featuresservice.data.dto.FeatureDto;
import com.impactzb.featuresservice.data.dto.FeatureFlag;
import com.impactzb.featuresservice.data.dto.UserFeatureDto;

import java.util.UUID;

public class FeaturesTestsHelper {
    public static FeatureDto buildFeature(String name, FeatureFlag featureFlag){
        return FeatureDto.builder()
                .id(UUID.randomUUID())
                .name(name)
                .featureFlag(featureFlag)
                .build();
    }

    public static UserFeatureDto buildUserFeature(String userName, UUID featureId, FeatureFlag featureFlag){
        return UserFeatureDto.builder()
                .id(UUID.randomUUID())
                .userName(userName)
                .featureId(featureId)
                .featureFlag(featureFlag)
                .build();
    }
}
