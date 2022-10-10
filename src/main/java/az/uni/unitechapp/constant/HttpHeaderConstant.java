package az.uni.unitechapp.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpHeaderConstant {

    public static final String X_KONG_JWT_CLAIM_USER_ID = "jwt-claim-user-id";
    public static final String AUTHORIZATION = "Authorization";
    public static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

}
