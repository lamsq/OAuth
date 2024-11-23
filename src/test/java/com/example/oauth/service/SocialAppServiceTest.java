package com.example.oauth.service;

import com.example.oauth.entity.User;
import com.example.oauth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SocialAppServiceTest {

    private UserRepository ur;
    private SocialAppService as;

    @BeforeEach
    void setUp() {
        ur = Mockito.mock(UserRepository.class);
        as = new SocialAppService(ur);
    }

    @Test
    void testLoadUser_NewUser() {
        OAuth2UserRequest ureq = Mockito.mock(OAuth2UserRequest.class);
        when(ureq.getClientRegistration().getRegistrationId()).thenReturn("google");

        Map<String, Object> attr = new HashMap<>();
        attr.put("email", "test@example.com");
        attr.put("name", "Test User");
        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(null, attr, "email");

        when(ur.findByProviderAndProviderId("google", "testId")).thenReturn(Optional.empty());
        OAuth2User res = as.loadUser(ureq);

        verify(ur, times(1)).save(any(User.class));
        assertEquals("test@example.com", res.getAttribute("email"));
    }

    @Test
    void testLoadUser_ExistingUser() {

        OAuth2UserRequest ureq = Mockito.mock(OAuth2UserRequest.class);
        when(ureq.getClientRegistration().getRegistrationId()).thenReturn("google");

        Map<String, Object> attr = new HashMap<>();
        attr.put("email", "test@example.com");
        attr.put("name", "Test User");
        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(null, attr, "email");

        User existingUser = new User(1L, "test@example.com", "Test User", "USER", "google", "testId");
        when(ur.findByProviderAndProviderId("google", "testId")).thenReturn(Optional.of(existingUser));
        OAuth2User res = as.loadUser(ureq);

        verify(ur, never()).save(any(User.class));
        assertEquals("test@example.com", res.getAttribute("email"));
    }
}
