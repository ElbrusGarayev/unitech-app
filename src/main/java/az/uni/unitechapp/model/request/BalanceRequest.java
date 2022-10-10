package az.uni.unitechapp.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class BalanceRequest {

    @NotNull
    @Min(0)
    private BigDecimal balance;

}
