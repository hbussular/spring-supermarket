package market;


import market.dto.model.customerOrder.CustomerOrderDto;
import market.dto.model.customerOrder.CustomerOrderReadDto;
import market.dto.model.orderDetails.OrderDetailsDto;
import market.dto.model.payment.PaymentDto;
import market.dto.model.payment.PaymentReadDto;
import market.dto.model.product.ProductCreateDto;
import market.dto.model.product.ProductReadDto;
import market.dto.model.report.ReportDto;
import market.dto.model.user.UserCreateDto;
import market.dto.model.user.UserReadDto;
import market.model.payment.PaymentStatus;
import market.model.payment.PaymentType;
import market.model.product.ProductCategory;
import market.model.report.ReportStatus;
import market.model.report.ReportType;
import market.model.user.Role;
import market.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class MarketServiceApp implements CommandLineRunner {

  public static final String EXCHANGE_NAME = "appExchange";
  public static final String QUEUE_PAYMENT_NAME = "appPaymentQueue";
  public static final String QUEUE_REPORT_NAME = "appReportQueue";
  public static final String QUEUE_ALERT_NAME = "appAlertQueue";
  public static final String PAYMENT_ROUTING_KEY = "route.payment.key";
  public static final String REPORT_ROUTING_KEY = "route.report.key";
  public static final String ALERT_ROUTING_KEY = "route.alert.key";

  @Autowired
  UserService userService;

  @Autowired
  ProductService productService;

  @Autowired
  CustomerOrderService customerOrderService;

  @Autowired
  OrderDetailsService orderDetailsService;

  @Autowired
  PaymentService paymentService;

  @Autowired
  ReportService reportService;

  public static void main(String[] args) {
    SpringApplication.run(MarketServiceApp.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  CommandLineRunner init() {
    return args -> {
      UserCreateDto newAdminDto = new UserCreateDto();
      newAdminDto.setUsername("admin");
      newAdminDto.setPassword("admin");
      newAdminDto.setFullName("Administrator");
      newAdminDto.setEmail("admin@market.com");

      UserReadDto adminDto = userService.signup(newAdminDto);
      userService.addRole(Role.ROLE_ADMIN, adminDto.getId());


      UserCreateDto newUserDto = new UserCreateDto();
      newUserDto.setUsername("user");
      newUserDto.setPassword("user");
      newUserDto.setFullName("Normal User");
      newUserDto.setEmail("user@gmail.com");

      UserReadDto userDto = userService.signup(newUserDto);

      UserCreateDto newCashierDto = new UserCreateDto();
      newCashierDto.setUsername("cashier");
      newCashierDto.setPassword("cashier");
      newCashierDto.setFullName("Cashier");
      newCashierDto.setEmail("cashier@market.com");

      UserReadDto cashierDto = userService.signup(newCashierDto);
      userService.addRole(Role.ROLE_CASHIER, cashierDto.getId());

      ProductCreateDto newProductDto = new ProductCreateDto();
      newProductDto.setName("milk");
      newProductDto.setPrice(5.00);
      newProductDto.setCategory(ProductCategory.DAIRY);
      newProductDto.setQuantity(10);
      productService.create(newProductDto);

      ProductCreateDto newProductDto2 = new ProductCreateDto();
      newProductDto2.setName("detergent");
      newProductDto2.setPrice(3.00);
      newProductDto2.setCategory(ProductCategory.CLEANING);
      newProductDto2.setQuantity(22);
      productService.create(newProductDto2);

      ProductCreateDto newProductDto3 = new ProductCreateDto();
      newProductDto3.setName("apple");
      newProductDto3.setPrice(1.50);
      newProductDto3.setCategory(ProductCategory.FRUITS);
      newProductDto3.setQuantity(11);
      ProductReadDto productReadDto3 = productService.create(newProductDto3);

      CustomerOrderDto newCustomerOrderDto = new CustomerOrderDto();
      newCustomerOrderDto.setUserId(userDto.getId());
      CustomerOrderReadDto customerReadDto = customerOrderService.create(newCustomerOrderDto);

      OrderDetailsDto newOrderDetailsDto = new OrderDetailsDto();
      newOrderDetailsDto.setProductId(productReadDto3.getId());
      newOrderDetailsDto.setQuantity(3);
      newOrderDetailsDto.setCustomerOrderId(customerReadDto.getId());
      orderDetailsService.create(newOrderDetailsDto);

      PaymentDto paymentDto = new PaymentDto();
      paymentDto.setAmount(4.5);
      paymentDto.setStatus(PaymentStatus.PROCESSING);
      paymentDto.setInstallments(1);
      paymentDto.setType(PaymentType.CASH);
      paymentService.create(paymentDto, customerReadDto.getId());

      ReportDto reportDto = new ReportDto();
      reportDto.setName(ReportType.TOP15PRODUCTS.name());
      reportDto.setType(ReportType.TOP15PRODUCTS);
      reportService.create(reportDto);
    };
  }

  // RabbitMQ

  @Bean
  public TopicExchange appExchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public Queue appQueuePayment() {
    return new Queue(QUEUE_PAYMENT_NAME);
  }

  @Bean
  public Queue appQueueReport() {
    return new Queue(QUEUE_REPORT_NAME);
  }

  @Bean
  public Queue appQueueAlert() {
    return new Queue(QUEUE_ALERT_NAME);
  }

  @Bean
  public Binding declareBindingPayment() {
    return BindingBuilder.bind(appQueuePayment()).to(appExchange()).with(PAYMENT_ROUTING_KEY);
  }

  @Bean
  public Binding declareBindingReport() {
    return BindingBuilder.bind(appQueueReport()).to(appExchange()).with(REPORT_ROUTING_KEY);
  }

  @Bean
  public Binding declareBindingAlert() {
    return BindingBuilder.bind(appQueueAlert()).to(appExchange()).with(ALERT_ROUTING_KEY);
  }

  @Override
  public void run(String... args) throws Exception {

  }
}
