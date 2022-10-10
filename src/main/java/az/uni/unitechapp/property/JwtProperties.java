package az.uni.unitechapp.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtProperties {

    String secretKey;

}
