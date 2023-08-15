package com.example.best_travel.config;

import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private static final String[] PUBLIC_RESOURCES = {"/fly/**","/hotel/**","/swagger-ui/**", "/.well-known/**, ", "/v3/api-docs/**"};
    private static final String[] USER_RESOURCES = {"/tour/**","/ticket/**","/reservation/**"};
    private static final String[] ADMIN_RESOURCES = {"/user/**", "/report/**"};
    private static final String LOGIN_RESOURCE = "/login";
    private static final String APPLICATION_OWNER = "Mike Padilla";
    @Value(value = "${app.client.id}")
    private String clientId;
    @Value(value = "${app.client.secret}")
    private String clientSecret;
    @Value(value = "${app.client-scope-read}")
    private String scopeRead;
    @Value(value = "${app.client-scope-write}")
    private String scopeWrite;
    @Value(value = "${app.client-redirect-debugger}")
    private String redirectUri;
    @Value(value = "${app.client-redirect-spring-doc}")
    private String redirectUri2;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults());
        http.exceptionHandling(e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));
        return http.build();
    }
    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(USER_RESOURCES)
                .authenticated()
                .requestMatchers(ADMIN_RESOURCES)
                .hasAuthority("write")
                .and()
                .oauth2ResourceServer()
                .jwt();

// ******* NEW WAY TO IMPLEMENT METHODS OF HTTPSECURITY *******
//        http
//                .formLogin(withDefaults())
//                .authorizeHttpRequests(oauth -> {
//                        oauth.requestMatchers(PUBLIC_RESOURCES).permitAll()
//                             .requestMatchers(USER_RESOURCES).authenticated()
//                             .requestMatchers(ADMIN_RESOURCES).hasAuthority("write");
//                })
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder encoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
    @Bean
    public RegisteredClientRepository registeredClientRepository(BCryptPasswordEncoder encoder){
        var registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(encoder.encode(clientSecret))
                .scope(scopeRead)
                .scope(scopeWrite)
                .redirectUri(redirectUri)
                .redirectUri(redirectUri2)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }
    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter(){
        var converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix(new String());
        return converter;
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter){
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .refreshTokenTimeToLive(Duration.ofHours(8))
                .build();
    }
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext>  oAuth2TokenCustomizer(){
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)){
                context.getClaims().claims(claim -> {
                    claim.putAll(Map.of("owner", APPLICATION_OWNER,
                            "date_request", LocalDateTime.now().toString()
                    ));
                }); //settear algo en singular seria claim
            }
        };
    }
    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        var rsaKey = generateKeys();
        var jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }
    private static KeyPair generateRSA(){
        KeyPair keyPair = null;
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;
        }catch (NoSuchAlgorithmException ex){
            throw new IllegalStateException(ex);
        }
    }
    private static RSAKey generateKeys(){
        var keyPair = generateRSA();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
    }
    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder().build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
