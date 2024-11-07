package az.ingress.ms.auth.client;

import az.ingress.ms.auth.client.decoder.CustomErrorDecoder;
import az.ingress.ms.auth.model.client.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-user",
        url = "${client.urls.ms-user}",
        configuration = CustomErrorDecoder.class)
public interface UserClient {

    @GetMapping("internal/v1/users/check-credential")
    UserResponseDto checkCredential(@RequestParam String username, @RequestParam String password);

}
