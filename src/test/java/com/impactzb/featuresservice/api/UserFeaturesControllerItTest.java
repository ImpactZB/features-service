package com.impactzb.featuresservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impactzb.featuresservice.FeaturesServiceApplication;
import com.impactzb.featuresservice.data.dto.FeatureFlag;
import com.impactzb.featuresservice.data.dto.UserFeatureDto;
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
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FeaturesServiceApplication.class})
@AutoConfigureMockMvc
public class UserFeaturesControllerItTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserFeaturesRepository userFeaturesRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithUserDetails(value = "admin")
    @DisplayName("API POST /user/feature - user with ADMIN role should successfully create user feature")
    void testFeatureSuccessfullyCreated() throws Exception {
        //given
        String userName = "user1";
        UUID featureId = UUID.randomUUID();

        //when
        MvcResult result = mvc.perform(post("/user/feature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"userName\": \"" + userName + "\", \"featureId\": \"" + featureId + "\", \"featureFlag\": \"" + FeatureFlag.ENABLED.name() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.userName", is(userName)))
                .andExpect(jsonPath("$.featureId", is(featureId.toString())))
                .andExpect(jsonPath("$.featureFlag", is(FeatureFlag.ENABLED.toString())))
                .andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        UserFeatureDto userFeature = objectMapper.readValue(contentAsString, UserFeatureDto.class);
        Optional<UserFeatureDto> storedUserFeatureOpt = userFeaturesRepository.getUserFeaturesEnabledForUser(userName).stream()
                .filter(userFeat -> userFeature.getFeatureId().equals(userFeat.getFeatureId()))
                        .findAny();
        assertFalse(storedUserFeatureOpt.isEmpty());
        UserFeatureDto storedUserFeature = storedUserFeatureOpt.get();
        assertEquals(featureId, storedUserFeature.getFeatureId());
        assertEquals(FeatureFlag.ENABLED, storedUserFeature.getFeatureFlag());
    }
}
