package com.impactzb.featuresservice.service;

import com.impactzb.featuresservice.data.dto.FeatureDto;
import com.impactzb.featuresservice.data.dto.UserFeatureDto;

import java.util.Set;

public interface FeaturesService {
    FeatureDto createFeature(FeatureDto featureDto);

    UserFeatureDto createUserFeature(UserFeatureDto userFeatureDto);

    Set<FeatureDto> getFeaturesEnabledForUser(String userName);

    Set<FeatureDto> getFeatures();

    Set<UserFeatureDto> getAllUserFeatures();
}
