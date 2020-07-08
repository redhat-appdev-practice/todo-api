package com.redhat.openapi.todo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.json.bind.Jsonb;

import com.redhat.openapi.todo.models.User;

@RestController
@RequestMapping("/")
public class UserApiController implements UserApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserApiController.class);

    private final NativeWebRequest request;

    private final ObjectMapper mapper;

    @Autowired
    public UserApiController(NativeWebRequest request, ObjectMapper mapper) {
        this.request = request;
        this.mapper = mapper;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<User> getUserProfile() {
        String bearer = request.getHeader("Authorization");
        if (bearer != null) {
            String jsonWebToken = new String(Base64.getDecoder().decode(bearer.split(" ")[1].split("\\.")[1]));
            try {
                Map<String, String> userData = mapper.readValue(jsonWebToken, Map.class);
                User profile = new User();
                profile.setFamilyName(userData.get("family_name"));
                profile.setGivenName(userData.get("given_name"));
                profile.setPreferredUsername(userData.get("preferred_username"));
                profile.setName(userData.get("name"));
                return ResponseEntity.ok(profile);
            } catch (JsonProcessingException e) {
                LOG.error("Unable to parse Bearer token", e);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
