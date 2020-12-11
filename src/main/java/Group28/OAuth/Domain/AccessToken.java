package Group28.OAuth.Domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="AccessTokens")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "accessToken_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "clientApp_id", nullable = false)
    private ClientApp clientApp;

    @OneToMany(mappedBy = "accessToken", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RefreshToken> refreshTokens;

    private String scopes;

    private Date createdAt;

    private Date updatedAt;

    private Date expiresAt;

    private boolean revoked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    public Set<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(Set<RefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }
}
