package az.uni.unitechapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDetailResponse {

    private String iban;
    private String accountNumber;
    private BigDecimal balance;

}
