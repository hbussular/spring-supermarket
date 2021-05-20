package market.controller;

import io.swagger.annotations.*;
import market.controller.request.PaymentCreateRequest;
import market.dto.model.payment.PaymentCreateDto;
import market.dto.model.payment.PaymentDto;
import market.dto.model.payment.PaymentReadDto;
import market.dto.response.Response;
import market.model.user.Role;
import market.model.user.User;
import market.service.PaymentService;
import market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/payments")
@Api(tags = "payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${PaymentController.readById}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readById(HttpServletRequest req, @PathVariable("id") Long id) {
        PaymentReadDto paymentDto;
        User user = userService.whoami(req);

        if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getRoles().contains(Role.ROLE_CASHIER)) {
            paymentDto = paymentService.findById(id);
        } else {
            paymentDto = paymentService.findUserPaymentById(user.getId(), id);
        }

        return Response.ok().setPayload(paymentDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${PaymentController.readAll}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readAll(HttpServletRequest req) {
        List<PaymentReadDto> allPaymentDto = paymentService.findAll();
        User user = userService.whoami(req);

        if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getRoles().contains(Role.ROLE_CASHIER)) {
            allPaymentDto = paymentService.findAll();
        } else {
            allPaymentDto = paymentService.findAllByUser(user.getId());
        }

        return Response.ok().setPayload(allPaymentDto);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${PaymentController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response create(@ApiParam("Create Payment") @RequestBody PaymentCreateRequest payment) {
        PaymentDto paymentDto = new PaymentDto()
                .setAmount(payment.getAmount())
                .setType(payment.getType())
                .setInstallments(payment.getInstallments());

        PaymentReadDto newPayment = paymentService.create(paymentDto, payment.getCustomerOrderId());

        return Response.ok().setPayload(newPayment);
    }
}


