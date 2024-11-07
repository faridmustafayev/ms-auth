package az.ingress.ms.auth.client.decoder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JsonNodeFieldName {
    CODE("code"),
    MESSAGE("message");
    private final String value;
}
