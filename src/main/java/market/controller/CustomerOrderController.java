package market.controller;

import io.swagger.annotations.*;
import market.controller.request.CustomerOrderCreateRequest;
import market.dto.model.customerOrder.CustomerOrderCreateDto;
import market.dto.model.customerOrder.CustomerOrderDto;
import market.dto.model.customerOrder.CustomerOrderReadDto;
import market.dto.response.Response;
import market.model.user.Role;
import market.model.user.User;
import market.service.CustomerOrderService;
import market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/customerOrders")
@Api(tags = "customerOrders")
public class CustomerOrderController {
    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${CustomerOrderController.readById}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readById(HttpServletRequest req, @PathVariable("id") Long id) {
        CustomerOrderReadDto customerOrderDto;
        User user = userService.whoami(req);

        if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getRoles().contains(Role.ROLE_CASHIER)) {
            customerOrderDto = customerOrderService.findById(id);
        } else {
            customerOrderDto = customerOrderService.findUserOrderById(user.getId(), id);
        }

        return Response.ok().setPayload(customerOrderDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${CustomerOrderController.readAll}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readAll(HttpServletRequest req) {
        List<CustomerOrderReadDto> allCustomerOrderDto;
        User user = userService.whoami(req);

        if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getRoles().contains(Role.ROLE_CASHIER)) {
            allCustomerOrderDto = customerOrderService.findAll();
        } else {
            allCustomerOrderDto = customerOrderService.findAllByUser(user.getId());
        }

        return Response.ok().setPayload(allCustomerOrderDto);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${CustomerOrderController.create}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response create(@ApiParam("Create CustomerOrder") HttpServletRequest req) {
        User user = userService.whoami(req);



        CustomerOrderDto customerOrderDto = new CustomerOrderDto()
                .setUserId(user.getId());

        CustomerOrderReadDto newCustomerOrder = customerOrderService.create(customerOrderDto);

        return Response.ok().setPayload(newCustomerOrder);
    }
}


