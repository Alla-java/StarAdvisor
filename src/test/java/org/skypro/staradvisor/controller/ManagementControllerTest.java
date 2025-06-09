package org.skypro.staradvisor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.staradvisor.model.AppInfo;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagementControllerTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private BuildProperties buildProperties;

    @Mock
    private Cache cache;

    @InjectMocks
    private ManagementController managementController;

    @Test
    void clearCaches_ShouldClearAllCaches() {
        // Arrange
        when(cacheManager.getCacheNames()).thenReturn(Collections.singletonList("testCache"));
        when(cacheManager.getCache("testCache")).thenReturn(cache);

        // Act
        ResponseEntity<Void> response = managementController.clearCaches();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cacheManager).getCacheNames();
        verify(cacheManager).getCache("testCache");
        verify(cache).clear();
    }

    @Test
    void getAppInfo_ShouldReturnAppInfo() {
        // Arrange
        String expectedName = "StarAdvisor";
        String expectedVersion = "1.0.0";
        when(buildProperties.getVersion()).thenReturn(expectedVersion);

        // Act
        ResponseEntity<AppInfo> response = managementController.getAppInfo();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedName, response.getBody().getName());
        assertEquals(expectedVersion, response.getBody().getVersion());
    }

    @Test
    void getAppInfo_ShouldReturnCorrectAppInfoStructure() {
        // Arrange
        String expectedVersion = "1.0.0";
        when(buildProperties.getVersion()).thenReturn(expectedVersion);

        // Act
        AppInfo appInfo = managementController.getAppInfo().getBody();

        // Assert
        assertNotNull(appInfo);
        assertEquals("StarAdvisor", appInfo.getName()); // Ошибка в тесте - демонстрация
        assertEquals(expectedVersion, appInfo.getVersion());
    }
}
