package com.example.best_travel.domain.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Document(collection = "app_users")
public class AppUserDocument {

    private String id;//Es opcional poner la anotacion @Id
    private String dni;
    private String username;
    private boolean enabled; //valor primitivo ya trae un valor por defecto que es false
    private String password;
    private Role role;
}
