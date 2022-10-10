package az.uni.unitechapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

}
