package com.impactzb.featuresservice.service;

import com.impactzb.featuresservice.data.dto.FeatureDto;
import com.impactzb.featuresservice.data.dto.FeatureFlag;
import com.impactzb.featuresservice.data.dto.UserFeatureDto;
import com.impactzb.featuresservice.data.repository.FeaturesRepository;
import com.impactzb.featuresservice.data.repository.UserFeaturesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FeaturesServiceImpl implements FeaturesService {

    private final FeaturesRepository featuresRepository;

    private final UserFeaturesRepository userFeaturesRepository;

    @Override
    public FeatureDto createFeature(FeatureDto featureDto) {
        if(featureDto.getFeatureFlag() == null){
            featureDto.setFeatureFlag(FeatureFlag.DISABLED);
        }
        return featuresRepository.addFeature(featureDto);
    }

    @Override
    public Set<FeatureDto> getFeaturesEnabledForUser(String userName) {
        Set<FeatureDto> featuresEnabledGlobally = featuresRepository.getEnabledFeatures();
        Set<UUID> featuresIdsEnabledGlobally = featuresEnabledGlobally.stream().map(FeatureDto::getId).collect(Collectors.toSet());
        Set<FeatureDto> uniqueFeaturesEnabledForUserButNotEnabledGlobally = getUniqueUserFeaturesEnabledForUser(userName, featuresIdsEnabledGlobally);
        featuresEnabledGlobally.addAll(uniqueFeaturesEnabledForUserButNotEnabledGlobally);
        return featuresEnabledGlobally;

    }

    @Override
    public Set<FeatureDto> getFeatures() {
        return featuresRepository.getAllFeatures();
    }

    @Override
    public Set<UserFeatureDto> getAllUserFeatures() {
        return userFeaturesRepository.getAllUserFeatures();
    }

    @Override
    public UserFeatureDto createUserFeature(UserFeatureDto userFeatureDto) {
        return userFeaturesRepository.addUserFeature(userFeatureDto);
    }

    private Set<FeatureDto> getUniqueUserFeaturesEnabledForUser(String userName, Set<UUID> featuresIdsEnabledGlobally) {
        Set<UserFeatureDto> userFeaturesEnabledForUser = userFeaturesRepository.getUserFeaturesEnabledForUser(userName);
        Set<UUID> idsOfUniqueFeaturesEnabledForUserButNotEnabledGlobally = userFeaturesEnabledForUser.stream()
                .map(UserFeatureDto::getFeatureId)
                .filter(id -> !featuresIdsEnabledGlobally.contains(id)) //Filter out features already loaded from db, to limit data that need to be collected in next step
                .collect(Collectors.toSet());

        return featuresRepository.getFeaturesForGivenIds(idsOfUniqueFeaturesEnabledForUserButNotEnabledGlobally);
    }
}
