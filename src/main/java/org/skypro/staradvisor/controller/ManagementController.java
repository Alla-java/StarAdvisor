package org.skypro.staradvisor.controller;

import org.skypro.staradvisor.model.AppInfo;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final CacheManager cacheManager;
    //private final BuildProperties buildProperties;

    public ManagementController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        //this.buildProperties = buildProperties;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<AppInfo> getAppInfo() {
        return ResponseEntity.ok(new AppInfo(
                "StarAdvisor", "23.3"
                //buildProperties.getVersion()
        ));
    }


}
