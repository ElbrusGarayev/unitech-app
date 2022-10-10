package az.uni.unitechapp.model.request;

import az.uni.unitechapp.constraint.Password;
import az.uni.unitechapp.constraint.Username;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    @Username
    private String pin;

    @Password
    private String password;

}
