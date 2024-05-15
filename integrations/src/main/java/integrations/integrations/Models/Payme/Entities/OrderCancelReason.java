package integrations.integrations.Models.Payme.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderCancelReason {

    RECEIVER_NOT_FOUND(1),
    DEBIT_OPERATION_ERROR(2),
    TRANSACTION_ERROR(3),
    TRANSACTION_TIMEOUT(4),
    MONEY_BACK(5),
    UNKNOWN_ERROR(10);

    private final int code;

    public static OrderCancelReason fromCode(int code) {

        for (OrderCancelReason reason : OrderCancelReason.values()) {

            if (reason.getCode() == code) {

                return reason;
            }
        }

        throw new IllegalArgumentException("Unknown code for OrderCancelReason: " + code);
    }
}