package com.impactzb.featuresservice.api;

import com.impactzb.featuresservice.config.security.UserRole;
import com.impactzb.featuresservice.data.dto.FeatureDto;
import com.impactzb.featuresservice.service.FeaturesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(path = FeaturesController.FEATURES_ENDPOINT)
public class FeaturesController {

    public static final String FEATURES_ENDPOINT = "/feature";

    private final FeaturesService featuresService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(description="Create new feature")
    public ResponseEntity<?> createFeature(@RequestBody @Valid FeatureDto featureDto){
        FeatureDto feature = featuresService.createFeature(featureDto);
        return new ResponseEntity<>(feature, new HttpHeaders(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @GetMapping(produces = "application/json")
    @Operation(description="Return list of all features accessible for user")
    public ResponseEntity<?> getFeatures(Authentication authentication){
        User user = ((User)authentication.getPrincipal());
        if(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(UserRole.ADMIN.getSecurityRole()::equals)){//Functionality not requested in task description, implemented only for test convenience
            Set<FeatureDto> features = featuresService.getFeatures();
            return new ResponseEntity<>(features, new HttpHeaders(), HttpStatus.OK);
        }
        String userName = ((User)authentication.getPrincipal()).getUsername();
        Set<FeatureDto> features = featuresService.getFeaturesEnabledForUser(userName);
        return new ResponseEntity<>(features, new HttpHeaders(), HttpStatus.OK);
    }
}
