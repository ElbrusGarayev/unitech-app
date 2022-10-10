package az.uni.unitechapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstraintsViolationError {

    private String property;
    private Object rejectedValue;
    private String message;

    public ConstraintsViolationError(String property, String message) {
        this.property = property;
        this.message = message;
    }

    public ConstraintsViolationError(String property, Object rejectedValue) {
        this.property = property;
        this.rejectedValue = rejectedValue;
    }

}
