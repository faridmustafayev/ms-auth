package az.ingress.ms.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenClaimSet {
    private Long userId;
    private Date exp;
    private Integer count;
    private String iss;
}
