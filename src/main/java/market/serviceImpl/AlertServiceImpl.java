package market.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import market.dto.mapper.AlertMapper;
import market.dto.model.alert.AlertReadDto;
import market.dto.model.report.ReportTopProductDto;
import market.model.alert.Alert;
import market.model.alert.AlertType;
import market.model.product.Product;
import market.model.report.Report;
import market.model.report.ReportStatus;
import market.model.report.ReportType;
import market.repository.AlertRepository;
import market.repository.ProductRepository;
import market.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AlertServiceImpl implements AlertService {

  @Autowired
  private AlertRepository alertRepository;

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<AlertReadDto> findAll() {
    List<Alert> allAlerts = alertRepository.findAll();

    return allAlerts.stream()
            .map(AlertMapper::toAlertDto)
            .collect(Collectors.toList());
  }

  @Override
  public void processAlerts() {
    List<Product> allProducts = productRepository.findAllLowStockProducts(2);

    for(Product product : allProducts) {
      Alert alert = new Alert();
      alert.setDate(LocalDate.now());
      alert.setMessage(String.format("Product {%s} is close to running out.", product.getId()));
      alert.setType(AlertType.PRODUCT_OUT_OF_STOCK);
      alertRepository.save(alert);
    }
  }
}