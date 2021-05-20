package market.service;

import market.dto.model.payment.PaymentDto;
import market.dto.model.payment.PaymentReadDto;
import market.model.customerOrder.CustomerOrder;
import market.model.payment.PaymentStatus;
import market.model.payment.PaymentType;
import market.model.user.User;
import market.repository.CustomerOrderRepository;
import market.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PaymentServiceTests {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Before
    public void setUp() {
        User newCustomer = new User();

        newCustomer.setUsername("customer");
        newCustomer.setPassword("customer");
        newCustomer.setFullName("customer");
        newCustomer.setEmail("customer@market.com");
        newCustomer = userRepository.save(newCustomer);

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setAmount(10);
        customerOrder.setUser(newCustomer);

        customerOrderRepository.save(customerOrder);
    }

    @Test
    public void shouldCreateNewPayment() {
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setAmount(10);
        paymentDto.setType(PaymentType.CASH);
        paymentDto.setInstallments(1);

        List<CustomerOrder> customerOrder = customerOrderRepository.findAll();

        PaymentReadDto paymentReadDto = paymentService.create(paymentDto, customerOrder.get(0).getId());

        Assertions.assertNotNull(paymentReadDto);
        Assertions.assertNotNull(paymentDto.getId());
        Assertions.assertEquals(paymentReadDto.getAmount(), paymentDto.getAmount());
        Assertions.assertNotNull(paymentReadDto.getDate());
        Assertions.assertEquals(paymentReadDto.getStatus(), PaymentStatus.PROCESSING);
        Assertions.assertEquals(paymentReadDto.getInstallments(), paymentDto.getInstallments());
    }

    @Test
    public void throwsExceptionWhenTheCustomerOrderIsNotFound() {
        Long undefinedCustomerOrderId = 20L;
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setAmount(10);
        paymentDto.setType(PaymentType.CASH);
        paymentDto.setInstallments(1);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> paymentService.create(paymentDto, undefinedCustomerOrderId)
        );

        Assertions.assertTrue(thrown.getMessage().contains("The order does not exist."));
    }

    @Test
    public void throwsExceptionWhenAmountIsLessThanExpected() {
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setAmount(5);
        paymentDto.setType(PaymentType.CASH);
        paymentDto.setInstallments(1);

        List<CustomerOrder> customerOrder = customerOrderRepository.findAll();

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> paymentService.create(paymentDto, customerOrder.get(0).getId())
        );

        Assertions.assertTrue(thrown.getMessage().contains("It is not possible to make the payment because the amount sent ($5) is less than the amount of the service ($10)."));
    }

    @Test
    public void throwsExceptionWhenAmountIsMoreThanExpected() {
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setAmount(15);
        paymentDto.setType(PaymentType.CASH);
        paymentDto.setInstallments(1);

        List<CustomerOrder> customerOrder = customerOrderRepository.findAll();

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> paymentService.create(paymentDto, customerOrder.get(0).getId())
        );

        Assertions.assertTrue(thrown.getMessage().contains("It is not possible to make the payment because the amount sent ($15) is more than the amount of the service ($10)."));
    }

    @Test
    public void throwsExceptionWhenInstallmentsIsMoreThanOneForCash() {
        int invalidInstallmentsQty = 2;
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setAmount(15);
        paymentDto.setType(PaymentType.CASH);
        paymentDto.setInstallments(invalidInstallmentsQty);

        List<CustomerOrder> customerOrder = customerOrderRepository.findAll();

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> paymentService.create(paymentDto, customerOrder.get(0).getId())
        );

        Assertions.assertTrue(thrown.getMessage().contains("It is not possible to make more than one installment for the type of payment requested."));
    }

    @Test
    public void throwsExceptionWhenInstallmentsIsLessThanOne() {
        int invalidInstallmentsQty = 0;
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setAmount(15);
        paymentDto.setType(PaymentType.CASH);
        paymentDto.setInstallments(invalidInstallmentsQty);

        List<CustomerOrder> customerOrder = customerOrderRepository.findAll();

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> paymentService.create(paymentDto, customerOrder.get(0).getId())
        );

        Assertions.assertTrue(thrown.getMessage().contains("Invalid number of installments."));
    }
}
