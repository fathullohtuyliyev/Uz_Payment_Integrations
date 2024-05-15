package integrations.integrations.Models.Payme.Result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckPerformTransactionResult {

    private boolean allow;

    private String code;
    private String message;
}
