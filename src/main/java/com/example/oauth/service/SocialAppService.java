package com.example.oauth.service;

import com.example.oauth.entity.User;
import com.example.oauth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(SocialAppService.class);

    public SocialAppService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        logger.info("Processing OAuth2 login for provider: {}, providerId: {}, email: {}", provider, providerId, email);

        if (email == null || name == null) {
            throw new IllegalArgumentException("Email or name is missing from provider");
        }

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(provider, providerId);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        else {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setRole("USER");
            userRepository.save(user);
        }
        return oAuth2User;
    }
}
