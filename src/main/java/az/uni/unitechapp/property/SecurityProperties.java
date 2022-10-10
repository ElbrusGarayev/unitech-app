package az.uni.unitechapp.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
@ConfigurationProperties(prefix = "security")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityProperties {

    CorsConfiguration cors;

}
