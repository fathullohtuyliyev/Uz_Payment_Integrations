package integrations.integrations.Controllers;

import com.fasterxml.jackson.databind.JsonNode;

import integrations.integrations.Exceptions.Payme.*;
import integrations.integrations.Models.Payme.Entities.*;
import integrations.integrations.Services.Payme.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping("/api/payme")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class PaymeOrdersController {

    private final MerchantService merchantService;

    @PostMapping("/transactions")
    public ResponseEntity<?> handleTransaction(@RequestHeader(required = false) HttpHeaders headers,
                                               @RequestBody JsonNode jsonRequest)
            throws OrderNotExistsException, WrongAmountException, UnableCompleteException, TransactionNotFoundException, UnableCancelTransactionException {

        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization != null && authorization.startsWith("Basic ")) {

            String base64Credentials = authorization.substring("Basic ".length()).trim();
            String credentials;

            try {

                credentials = new String(Base64.getDecoder().decode(base64Credentials));
            }
            catch (IllegalArgumentException e) {

                throw new UnableCompleteException("Corrupted headers", -32504, "authorization");
            }

            String[] values = credentials.split(":");

            if (values.length == 2) {

                String username = values[0];
                String password = values[1];

                // write logic for checking the authorization for the payme account
            }
            else {

                throw new UnableCompleteException("Corrupted headers", -32504, "authorization");
            }
        }
        else {

            throw new UnableCompleteException("Corrupted headers", -32504, "authorization");
        }

        String method = jsonRequest.get("method").asText();
        JsonNode params = jsonRequest.get("params");
        JsonNode accountJson = params.get("account");
        Account account;
        int amount;

        String transactionId;

        switch (method) {

            case "CheckPerformTransaction":

                amount = params.get("amount").intValue();

                if (!accountJson.isEmpty()) {

                    // in accountJson.get() it should be value that you set in your payme business account as default accept value
                    account = new Account(accountJson.get("KaleUz").asText());
                }
                else {

                    // sometimes there could be cases, when payme does not send account and the exception is thrown
                    // , so we need to make sure our backend will continue to work
                    account = new Account("1");
                }

                return ResponseEntity.ok(merchantService.checkPerformTransaction(amount, account));

            case "CreateTransaction":

                transactionId = params.get("id").asText();
                long time = params.get("time").longValue();
                amount = params.get("amount").intValue();
                Date transactionDate = new Date(time);

                if (!accountJson.isEmpty()) {

                    account = new Account(accountJson.get("KaleUz").asText());
                }
                else {

                    account = new Account("1");
                }

                return ResponseEntity.ok(merchantService.createTransaction(transactionId, transactionDate, amount, account));

            case "CheckTransaction":

                transactionId = params.get("id").asText();

                return ResponseEntity.ok(merchantService.checkTransaction(transactionId));

            case "PerformTransaction":

                transactionId = params.get("id").asText();

                return ResponseEntity.ok(merchantService.performTransaction(transactionId));

            case "CancelTransaction":

                transactionId = params.get("id").asText();
                int reasonCode = params.get("reason").intValue();

                OrderCancelReason reason = OrderCancelReason.fromCode(reasonCode);

                return ResponseEntity.ok(merchantService.cancelTransaction(transactionId, reason));

            case "GetStatement":

                long from = params.get("from").longValue();
                long to = params.get("to").longValue();

                Date fromDate = new Date(from);
                Date toDate = new Date(to);

                return ResponseEntity.ok(merchantService.getStatement(fromDate, toDate));

            default:
                return ResponseEntity.badRequest().body("Unsupported method");
        }
    }
}