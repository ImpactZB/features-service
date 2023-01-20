package com.impactzb.featuresservice.data.repository;

import com.impactzb.featuresservice.data.dto.UserFeatureDto;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserFeaturesRepository {
    Map<UUID, UserFeatureDto> userFeatures = new HashMap<>();

    public UserFeatureDto addUserFeature(UserFeatureDto userFeature){
        UUID id = UUID.randomUUID();
        userFeature.setId(id);
        userFeatures.put(id, userFeature);
        return userFeature;
    }

    public Set<UserFeatureDto> getUserFeaturesEnabledForUser(String userName){
        return userFeatures.values().stream()
                .filter(userFeature -> userFeature.getUserName().equals(userName))
                .filter(UserFeatureDto ::isEnabled)
                .collect(Collectors.toSet());
    }

    public Set<UserFeatureDto> getAllUserFeatures() {
        return new HashSet<>(userFeatures.values());
    }
}
