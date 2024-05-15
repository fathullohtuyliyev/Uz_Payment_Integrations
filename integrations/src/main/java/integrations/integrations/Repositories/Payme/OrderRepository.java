package integrations.integrations.Repositories.Payme;

import integrations.integrations.Models.Payme.Entities.CustomerOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<CustomerOrder, Long> {

    Optional<CustomerOrder> findByPaycomId(String id);
}