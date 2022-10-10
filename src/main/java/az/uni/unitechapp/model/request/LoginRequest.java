package az.uni.unitechapp.model.request;

import az.uni.unitechapp.constraint.Password;
import az.uni.unitechapp.constraint.Username;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @Username
    private String username;

    @Password
    private String password;

}
