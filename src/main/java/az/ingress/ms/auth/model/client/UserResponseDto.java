package az.ingress.ms.auth.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private Integer age;
    private String email;
    private String password;
    private UserType userType;
    private LocalDateTime birthDate;
    private String profilePhoto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
