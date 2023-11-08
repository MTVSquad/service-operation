package com.vsquad.iroas.config.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class SpringEnvironmentHelper {

    private final Environment environment;

    private final String PROD = "prod";

    private final String DEV = "dev";

    private final List<String> PROD_AND_DEV = List.of("prod", "dev");

    public Boolean isProdProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> currentProfile = Arrays.stream(activeProfiles).collect(Collectors.toList());
        return currentProfile.contains(PROD);
    }

    public Boolean isDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> currentProfile = Arrays.stream(activeProfiles).collect(Collectors.toList());
        return currentProfile.contains(DEV);
    }

    public Boolean isProdAndDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> currentProfiles = Arrays.stream(activeProfiles).collect(Collectors.toList());
        return CollectionUtils.containsAny(PROD_AND_DEV, currentProfiles);
    }
}
