package az.ingress.ms.auth.client.decoder;

import az.ingress.ms.auth.exception.CustomFeignException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static az.ingress.ms.auth.client.decoder.JsonNodeFieldName.CODE;
import static az.ingress.ms.auth.client.decoder.JsonNodeFieldName.MESSAGE;
import static az.ingress.ms.auth.constants.ExceptionConstant.CLIENT_ERROR_CODE;
import static az.ingress.ms.auth.constants.ExceptionConstant.CLIENT_ERROR_MESSAGE;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        var errorCode = CLIENT_ERROR_CODE;
        var errorMessage = CLIENT_ERROR_MESSAGE;

        JsonNode jsonNode;
        try(var body = response.body().asInputStream()) {
            jsonNode = new ObjectMapper().readValue(body, JsonNode.class);
        }catch (Exception ex) {
            throw new CustomFeignException(errorCode, errorMessage, response.status());
        }

        if (jsonNode.has(MESSAGE.getValue())) {
            errorCode = jsonNode.get(CODE.getValue()).asText();
            errorMessage = jsonNode.get(MESSAGE.getValue()).asText();
        }

        log.error("ActionLog.decode.error Code: {}, Message: {}, Method: {}", errorCode, errorMessage, methodKey);
        return new CustomFeignException(errorCode, errorMessage, response.status());
    }
}
