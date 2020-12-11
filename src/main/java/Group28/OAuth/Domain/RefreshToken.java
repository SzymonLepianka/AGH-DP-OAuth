package Group28.OAuth.Domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="RefreshTokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "refreshToken_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accessToken_id")
    private AccessToken accessToken;

    private boolean revoked;

    private Date expiresAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
