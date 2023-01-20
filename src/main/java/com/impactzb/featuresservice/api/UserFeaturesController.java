package com.impactzb.featuresservice.api;

import com.impactzb.featuresservice.data.dto.UserFeatureDto;
import com.impactzb.featuresservice.service.FeaturesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@AllArgsConstructor
@RestController
@RequestMapping(path = UserFeaturesController.USER_FEATURES_ENDPOINT)
public class UserFeaturesController {
    public static final String USER_FEATURES_ENDPOINT = "/user/feature";

    private final FeaturesService featuresService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(description="Create new user feature")
    public ResponseEntity<?> createUserFeature(@RequestBody @Valid UserFeatureDto userFeatureDto){
        UserFeatureDto userFeature = featuresService.createUserFeature(userFeatureDto);
        return new ResponseEntity<>(userFeature, new HttpHeaders(), HttpStatus.CREATED);
    }

    //Functionality not requested in task description, implemented only for test convenience
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = "application/json")
    @Operation(description="Return list of all users features")
    public ResponseEntity<?> getAllUserFeatures(){
        Set<UserFeatureDto> userFeatures = featuresService.getAllUserFeatures();
        return new ResponseEntity<>(userFeatures, new HttpHeaders(), HttpStatus.OK);
    }
}