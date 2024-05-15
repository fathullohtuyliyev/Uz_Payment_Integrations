package integrations.integrations.Controllers;

import integrations.integrations.Models.Payme.Entities.CustomerOrder;
import integrations.integrations.Repositories.Payme.OrderRepository;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin(maxAge = 3600)
public class GlobalOrderController {

    private final OrderRepository orderRepository;

    @PostMapping("/create-order")
    @Transactional
    public ResponseEntity<?> createOrder(@RequestBody OrderBody body) {


        double orderTotalSum = 0.0;

        BillingUrl billingUrl = new BillingUrl();

        // Orders order = new Orders(...);
        // you have to save the order to fetch its id, now I will use the test one
        Integer orderId = 1;

        if (body.getPaymentType().equalsIgnoreCase("CLICK")) {

            // Write logic to create order in your own system and pass the saved order id to the billing url

            billingUrl.setBilling_url("https://my.click.uz/services/pay?service_id=28420&merchant_id=11369&return_url=https://xxx/profile/purchases-history&amount=" + orderTotalSum + "&transaction_param=" + orderId);
        }
        else if (body.getPaymentType().equalsIgnoreCase("PAYME")) {

            long amount = (long) (orderTotalSum * 100); // конвертируйте в тиины

            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setPaycomId(orderId.toString());
            customerOrder.setDelivered(false);
            customerOrder.setAmount((int) amount);
            orderRepository.save(customerOrder);

            String paymeUrl = "https://checkout.paycom.uz";
            String merchantId = "65e2f91cf4193eeca0afd4b0"; // define your own merchant id here
            String accountAcceptType = "account"; // define your own account type here that is defined in your payme business account
            String returnUrl = "https://xxx/profile/purchases-history";

            String data = "m=" + merchantId + ";ac." + accountAcceptType + "=" + orderId + ";a=" + amount + ";c=" + returnUrl;
            String encodedData = Base64.getEncoder().encodeToString(data.getBytes());

            String url = paymeUrl + "/" + encodedData;

            System.out.println(url);

            billingUrl.setBilling_url(url);
        }

        return ResponseEntity.ok(billingUrl);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingUrl {

        private String billing_url;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderBody {

        private Long user;
        private List<Product> products;
        private String paymentType;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Product {

            private Long product;
            private Double quantity;
            private Double orderPrice;
        }
    }
}