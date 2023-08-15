package com.example.best_travel.infrastructure.abstract_services;

import java.util.Set;
import java.util.Map;

public interface ModifyUserService {
    Map<String, Boolean> enabled(String username);
    Map<String, Set<String>> addRole(String username, String role);
    Map<String, Set<String>> removeRole(String username, String role);
}
