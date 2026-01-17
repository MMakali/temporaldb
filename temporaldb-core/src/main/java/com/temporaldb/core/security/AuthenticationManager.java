package com.temporaldb.core.security;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages user authentication and JWT token generation.
 */
public class AuthenticationManager {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final SecretKey secretKey;
    private final long tokenExpirationMs;

    public AuthenticationManager() {
        this.secretKey = Keys.hmacShaKeyFor(
                "TemporalDB-Secret-Key-For-JWT-Tokens-Must-Be-256-Bits-Long-Here".getBytes()
        );
        this.tokenExpirationMs = 24 * 60 * 60 * 1000; // 24 hours
    }

    /**
     * Create a new user.
     * @param username Username
     * @param plainPassword Plain text password
     * @param email Email address
     * @return Created user
     */
    public User createUser(String username, String plainPassword, String email) {
        String passwordHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, username, passwordHash, email);
        users.put(username, user);
        logger.info("User created: {}", username);
        return user;
    }

    /**
     * Authenticate user and generate JWT token.
     * @param username Username
     * @param password Plain password
     * @return JWT token
     * @throws AuthenticationException if credentials invalid
     */
    public String authenticate(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            logger.warn("Login failed: user not found: {}", username);
            throw new AuthenticationException("User not found");
        }

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            logger.warn("Login failed: invalid password for user: {}", username);
            throw new AuthenticationException("Invalid password");
        }

        user.recordLogin();
        return generateToken(user);
    }

    /**
     * Generate JWT token for user.
     * @param user User
     * @return JWT token
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpirationMs);

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("roles", new ArrayList<>(user.getRoles()))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        logger.info("Token generated for user: {}", user.getUsername());
        return token;
    }

    /**
     * Validate JWT token.
     * @param token JWT token
     * @return Username from token
     * @throws JwtException if token invalid
     */
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (JwtException e) {
            logger.warn("Token validation failed", e);
            throw new AuthenticationException("Invalid token", e);
        }
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}