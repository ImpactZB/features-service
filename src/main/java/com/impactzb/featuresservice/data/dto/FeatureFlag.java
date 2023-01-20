package com.impactzb.featuresservice.data.dto;

public enum FeatureFlag {
    ENABLED,
    DISABLED;

    public boolean isEnabled(){
        return this.equals(ENABLED);
    }
}
