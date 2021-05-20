package market.repository;

import market.model.alert.Alert;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlertRepository extends JpaRepository<Alert, Long> { }