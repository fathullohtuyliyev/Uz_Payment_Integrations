package integrations.integrations.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/api/click")
public class ClickOrdersController {

    @PostMapping("/prepare-order")
    public ResponseEntity<?> prepareOrder(@RequestParam Map<String, String> body) {

        String clickTransId = body.get("click_trans_id");
        String merchantTransId = body.get("merchant_trans_id");

        Map<String, String> response = new HashMap<>();

        response.put("click_trans_id", clickTransId);
        response.put("merchant_trans_id", merchantTransId);
        response.put("merchant_prepare_id", merchantTransId);
        response.put("error", "0");
        response.put("error_note", "Success");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete-order")
    public ResponseEntity<?> completeOrder(@RequestParam Map<String, String> body) {

        String clickTransId = body.get("click_trans_id");
        String merchantTransId = body.get("merchant_trans_id");
        String error = body.get("error");

        Map<String, Object> response = new HashMap<>();

        response.put("click_trans_id", clickTransId);
        response.put("merchant_trans_id", merchantTransId);
        Integer merchantConfirmId = error.equals("0") ? 1 : null;
        response.put("merchant_confirm_id", merchantConfirmId);

        Long orderId = Long.valueOf(body.get("merchant_trans_id"));

        // write logic to fetch the order by orderId and check if the items are in stock or not
        boolean itemEnded = false;

        if (itemEnded) {

            response.put("error", "-1905");
            response.put("error_note", "Товар Закончился!");
        }
        else {

            response.put("error", "0");
            response.put("error_note", "Success");
        }

        return ResponseEntity.ok(response);
    }
}