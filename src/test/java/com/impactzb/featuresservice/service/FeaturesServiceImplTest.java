package com.impactzb.featuresservice.service;

import com.impactzb.featuresservice.data.dto.FeatureDto;
import com.impactzb.featuresservice.data.dto.FeatureFlag;
import com.impactzb.featuresservice.data.dto.UserFeatureDto;
import com.impactzb.featuresservice.data.repository.FeaturesRepository;
import com.impactzb.featuresservice.data.repository.UserFeaturesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.impactzb.featuresservice.FeaturesTestsHelper.buildFeature;
import static com.impactzb.featuresservice.FeaturesTestsHelper.buildUserFeature;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FeaturesServiceImplTest {

    private static String USER_1_NAME = "user1";
    private static String FEATURE_1_NAME = "feature1";
    private static String FEATURE_2_NAME = "feature2";
    private static String FEATURE_3_NAME = "feature3";

    FeaturesRepository featuresRepository = Mockito.mock(FeaturesRepository.class);

    UserFeaturesRepository userFeaturesRepository = Mockito.mock(UserFeaturesRepository.class);

    FeaturesServiceImpl featuresService = new FeaturesServiceImpl(featuresRepository, userFeaturesRepository);

    @Test
    void getFeaturesEnabledForUser_shouldReturnAllFeaturesEnabledForUser(){
        //given
        FeatureDto feature1 = buildFeature(FEATURE_1_NAME, FeatureFlag.ENABLED);
        FeatureDto feature2 = buildFeature(FEATURE_2_NAME, FeatureFlag.ENABLED);
        FeatureDto feature3 = buildFeature(FEATURE_3_NAME, FeatureFlag.ENABLED);
        when(featuresRepository.getEnabledFeatures()).thenReturn(new HashSet<>(Arrays.asList(feature1, feature2)));
        UserFeatureDto userFeatureDto3 = buildUserFeature(USER_1_NAME, feature3.getId(), FeatureFlag.DISABLED);
        when(userFeaturesRepository.getUserFeaturesEnabledForUser(USER_1_NAME)).thenReturn(new HashSet<>(Arrays.asList(userFeatureDto3)));
        when(featuresRepository.getFeaturesForGivenIds(new HashSet<>(Arrays.asList(userFeatureDto3.getFeatureId())))).thenReturn(new HashSet<>(Arrays.asList(feature3)));

        //when
        Set<FeatureDto> features = featuresService.getFeaturesEnabledForUser(USER_1_NAME);

        //then
        Optional<FeatureDto> receivedFeature1Opt = features.stream().filter(feature -> feature1.getId().equals(feature.getId())).findAny();
        assertFalse(receivedFeature1Opt.isEmpty());
        FeatureDto receivedFeature1 = receivedFeature1Opt.get();
        assertEquals(feature1.getName(), receivedFeature1.getName());
        assertEquals(feature1.getFeatureFlag(), receivedFeature1.getFeatureFlag());

        Optional<FeatureDto> receivedFeature2Opt = features.stream().filter(feature -> feature2.getId().equals(feature.getId())).findAny();
        assertFalse(receivedFeature2Opt.isEmpty());
        FeatureDto receivedFeature2 = receivedFeature2Opt.get();
        assertEquals(feature2.getName(), receivedFeature2.getName());
        assertEquals(feature2.getFeatureFlag(), receivedFeature2.getFeatureFlag());

        Optional<FeatureDto> receivedFeature3Opt = features.stream().filter(feature -> feature3.getId().equals(feature.getId())).findAny();
        assertFalse(receivedFeature3Opt.isEmpty());
        FeatureDto receivedFeature3 = receivedFeature3Opt.get();
        assertEquals(feature3.getName(), receivedFeature3.getName());
        assertEquals(feature3.getFeatureFlag(), receivedFeature3.getFeatureFlag());
    }
}
