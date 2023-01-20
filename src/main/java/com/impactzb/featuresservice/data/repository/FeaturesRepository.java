package com.impactzb.featuresservice.data.repository;

import com.impactzb.featuresservice.data.dto.FeatureDto;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FeaturesRepository {

    Map<UUID, FeatureDto> features = new HashMap<>();

    public FeatureDto addFeature(FeatureDto feature){
        UUID id = UUID.randomUUID();
        feature.setId(id);
        features.put(id, feature);
        return feature;
    }

    public Set<FeatureDto> getEnabledFeatures(){
        return features.values().stream()
                .filter(FeatureDto::isEnabled)
                .collect(Collectors.toSet());
    }

    public Set<FeatureDto> getFeaturesForGivenIds(Set<UUID> idsOfUniqueFeaturesEnabledForUserButNotEnabledGlobally) {
        return idsOfUniqueFeaturesEnabledForUserButNotEnabledGlobally.stream()
                .map(features::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<FeatureDto> getAllFeatures() {
        return new HashSet<>(features.values());
    }
}
