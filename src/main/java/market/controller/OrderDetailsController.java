package market.controller;

import io.swagger.annotations.*;
import market.controller.request.OrderDetailsCreateRequest;
import market.controller.request.OrderDetailsUpdateRequest;
import market.dto.model.orderDetails.OrderDetailsDto;
import market.dto.model.orderDetails.OrderDetailsReadDto;
import market.dto.response.Response;
import market.model.user.Role;
import market.model.user.User;
import market.service.OrderDetailsService;
import market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/orderDetails")
@Api(tags = "orderDetails")
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${OrderDetailsController.readById}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readById(HttpServletRequest req, @PathVariable("id") Long id) {

        OrderDetailsReadDto orderDetailsDto;
        User user = userService.whoami(req);

        if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getRoles().contains(Role.ROLE_CASHIER)) {
            orderDetailsDto = orderDetailsService.findById(id);
        } else {
            orderDetailsDto = orderDetailsService.findUserOrderDetailsById(user.getId(), id);
        }

        return Response.ok().setPayload(orderDetailsDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CASHIER')")
    @ApiOperation(value = "${OrderDetailsController.readAll}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readAll(HttpServletRequest req) {
        List<OrderDetailsReadDto> allOrderDetailsDto;
        User user = userService.whoami(req);

        if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getRoles().contains(Role.ROLE_CASHIER)) {
            allOrderDetailsDto = orderDetailsService.findAll();
        } else {
            allOrderDetailsDto = orderDetailsService.findAllByUser(user.getId());
        }

        return Response.ok().setPayload(allOrderDetailsDto);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${OrderDetailsController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response create(@ApiParam("Create OrderDetails") @RequestBody OrderDetailsCreateRequest orderDetailsCreateRequest) {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto()
                .setQuantity(orderDetailsCreateRequest.getQuantity())
                .setProductId(orderDetailsCreateRequest.getProductId())
                .setCustomerOrderId(orderDetailsCreateRequest.getCustomerOrderId());

        OrderDetailsReadDto newOrderDetails = orderDetailsService.create(orderDetailsDto);

        return Response.ok().setPayload(newOrderDetails);
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${OrderDetailsController.updateById}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response updateBy(HttpServletRequest req, @RequestBody @Valid OrderDetailsUpdateRequest orderDetailsUpdateRequest) {
        User user = userService.whoami(req);

        OrderDetailsDto orderDetailsDto = new OrderDetailsDto()
                .setId(orderDetailsUpdateRequest.getId())
                .setQuantity(orderDetailsUpdateRequest.getQuantity());

        boolean byPass = user.getRoles().contains(Role.ROLE_CASHIER) || user.getRoles().contains(Role.ROLE_ADMIN);
        OrderDetailsReadDto updatedOrderDetailsDto = orderDetailsService.update(orderDetailsDto, user.getId(), byPass);

        return Response.ok().setPayload(updatedOrderDetailsDto);
    }
}


