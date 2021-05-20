package market.service;

import market.dto.model.customerOrder.CustomerOrderReadDto;
import market.model.customerOrder.CustomerOrder;
import market.model.customerOrder.CustomerOrderStatus;
import market.model.orderDetails.OrderDetails;
import market.model.payment.Payment;
import market.model.payment.PaymentType;
import market.model.product.Product;
import market.model.product.ProductCategory;
import market.model.user.User;
import market.repository.*;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CustomerOrderServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Before
    public void setUp() {
        User newCustomer = new User();

        newCustomer.setUsername("customer");
        newCustomer.setPassword("customer");
        newCustomer.setFullName("customer");
        newCustomer.setEmail("customer@market.com");
        newCustomer = userRepository.save(newCustomer);

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setAmount(0);
        customerOrder.setUser(newCustomer);

        customerOrderRepository.save(customerOrder);

        Product product = new Product();
        product.setName("Apple");
        product.setCategory(ProductCategory.FRUITS);
        product.setQuantity(10);
        product.setPrice(1);

        productRepository.save(product);
    }

    @Test
    public void shouldReturnTheCorrectAmount() {
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        CustomerOrder customerOrder = customerOrderList.get(0);

        List<Product> productList = productRepository.findAll();
        Product product = productList.get(0);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomerOrder(customerOrder);
        orderDetails.setQuantity(2);
        orderDetails.setProduct(product);

        orderDetailsRepository.save(orderDetails);
        customerOrderService.recalculateAmountById(customerOrder.getId());

        Optional<CustomerOrder> customerOrderUpdated = customerOrderRepository.findById(customerOrder.getId());

        Assertions.assertEquals(customerOrderUpdated.get().getAmount(), product.getPrice() * orderDetails.getQuantity());
        Assertions.assertNotEquals(customerOrderUpdated.get().getAmount(), customerOrder.getAmount());
    }

    @Test
    public void shouldCheckoutCorrectly() {
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        CustomerOrder customerOrder = customerOrderList.get(0);

        List<Product> productList = productRepository.findAll();
        Product product = productList.get(0);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomerOrder(customerOrder);
        orderDetails.setQuantity(2);
        orderDetails.setProduct(product);

        orderDetailsRepository.save(orderDetails);
        customerOrderService.recalculateAmountById(customerOrder.getId());

        Payment payment = new Payment();
        payment.setAmount(orderDetails.getQuantity() * product.getPrice());
        payment.setType(PaymentType.CASH);
        payment.setDate(LocalDate.now());
        payment.setCustomerOrder(customerOrder);

        Payment paymentUpdated = paymentRepository.save(payment);

        CustomerOrderReadDto customerOrderReadDto = customerOrderService.checkout(orderDetails.getId(), paymentUpdated.getId());
        Assertions.assertEquals(customerOrderReadDto.getStatus(), CustomerOrderStatus.PAID);
    }

    @Test
    public void throwsExceptionWhenProductOutOfStock() {
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        CustomerOrder customerOrder = customerOrderList.get(0);

        List<Product> productList = productRepository.findAll();
        Product product = productList.get(0);

        // Change the product quantity to force the exception
        product.setQuantity(0);
        productRepository.save(product);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomerOrder(customerOrder);
        orderDetails.setQuantity(2);
        orderDetails.setProduct(product);

        orderDetailsRepository.save(orderDetails);
        customerOrderService.recalculateAmountById(customerOrder.getId());

        Payment payment = new Payment();
        payment.setAmount(orderDetails.getQuantity() * product.getPrice());
        payment.setType(PaymentType.CASH);
        payment.setDate(LocalDate.now());
        payment.setCustomerOrder(customerOrder);

        Payment paymentUpdated = paymentRepository.save(payment);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> customerOrderService.checkout(orderDetails.getId(), paymentUpdated.getId())
        );

        Assertions.assertTrue(thrown.getMessage().contains("Product out of stock. Please remove the product and make the payment again."));
    }
}
