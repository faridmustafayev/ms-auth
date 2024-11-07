package az.ingress.ms.auth.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenClaimSet implements Serializable {
    @Serial
    private static final Long serialVersionUID = 1L;

    private Long userId;

    private String iss;

    @JsonProperty("exp")
    private Date expirationTime;

    @JsonProperty("iat")
    private Date createdTime;
}
