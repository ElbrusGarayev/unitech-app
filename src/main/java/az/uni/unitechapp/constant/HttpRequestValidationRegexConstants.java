package az.uni.unitechapp.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpRequestValidationRegexConstants {

    public static final String PIN = "^[A-Z0-9]{7}$";
    public static final String PASSWORD = "^[a-zA-Z0-9]{8,100}$";

}
