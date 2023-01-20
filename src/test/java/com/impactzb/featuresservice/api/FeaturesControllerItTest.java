package com.impactzb.featuresservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impactzb.featuresservice.FeaturesServiceApplication;
import com.impactzb.featuresservice.data.dto.FeatureDto;
import com.impactzb.featuresservice.data.dto.FeatureFlag;
import com.impactzb.featuresservice.data.dto.UserFeatureDto;
import com.impactzb.featuresservice.data.repository.FeaturesRepository;
import com.impactzb.featuresservice.data.repository.UserFeaturesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.Set;

import static com.impactzb.featuresservice.FeaturesTestsHelper.buildFeature;
import static com.impactzb.featuresservice.FeaturesTestsHelper.buildUserFeature;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FeaturesServiceApplication.class})
@AutoConfigureMockMvc
public class FeaturesControllerItTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    FeaturesRepository featuresRepository;

    @Autowired
    UserFeaturesRepository userFeaturesRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("API POST /feature - user with ADMIN role should successfully create feature")
    void testFeatureSuccessfullyCreated() throws Exception {
        //given
        String featureName = "feature1";

        //when
        MvcResult result = mvc.perform(post("/feature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"" + featureName + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(featureName)))
                .andExpect(jsonPath("$.featureFlag", is(FeatureFlag.DISABLED.toString())))
                .andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        FeatureDto feature = objectMapper.readValue(contentAsString, FeatureDto.class);
        Optional<FeatureDto> storedFeatureOpt = featuresRepository.getFeaturesForGivenIds(Set.of(feature.getId()))
                .stream().findAny();
        assertFalse(storedFeatureOpt.isEmpty());
        FeatureDto storedFeature = storedFeatureOpt.get();
        assertEquals(featureName, storedFeature.getName());
        assertEquals(FeatureFlag.DISABLED, storedFeature.getFeatureFlag());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("API POST /feature - user with USER role should not be able to create feature")
    void testFeatureNotCreatedByUnauthorizedUser() throws Exception {
        //given
        String featureName = "feature1";

        //when
        mvc.perform(post("/feature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"" + featureName + "\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("API GET /feature - user with USER role should successfully load all features that are accessible for him/her")
    void testFeatureSuccessfullyLoadedForRegularUser() throws Exception {
        //given
        FeatureDto feature1 = buildFeature("feature1", FeatureFlag.DISABLED);
        FeatureDto feature2 = buildFeature("feature2", FeatureFlag.ENABLED); //globally enabled for all users
        FeatureDto feature3 = buildFeature("feature3", FeatureFlag.DISABLED);
        FeatureDto feature4 = buildFeature("feature4", FeatureFlag.DISABLED);
        featuresRepository.addFeature(feature1);
        featuresRepository.addFeature(feature2);
        featuresRepository.addFeature(feature3);
        featuresRepository.addFeature(feature4);
        UserFeatureDto userFeatureDto1 = buildUserFeature("user1", feature3.getId(), FeatureFlag.DISABLED);
        UserFeatureDto userFeatureDto2 = buildUserFeature("user1", feature4.getId(), FeatureFlag.ENABLED); //enabled for user1
        UserFeatureDto userFeatureDto3 = buildUserFeature("user2", feature1.getId(), FeatureFlag.DISABLED);
        UserFeatureDto userFeatureDto4 = buildUserFeature("user2", feature1.getId(), FeatureFlag.ENABLED);
        userFeaturesRepository.addUserFeature(userFeatureDto1);
        userFeaturesRepository.addUserFeature(userFeatureDto2);
        userFeaturesRepository.addUserFeature(userFeatureDto3);
        userFeaturesRepository.addUserFeature(userFeatureDto4);

        //when
        mvc.perform(get("/feature")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[?(@.id == \"" + feature2.getId().toString() + "\" && @.name == \"" + feature2.getName()
                        + "\" && @.featureFlag == \"" + feature2.getFeatureFlag().name() + "\")]").exists())
                .andExpect(jsonPath("$.[?(@.id == \"" + feature4.getId().toString() + "\" && @.name == \"" + feature4.getName()
                        + "\" && @.featureFlag == \"" + feature4.getFeatureFlag().name() + "\")]").exists());
    }
}
