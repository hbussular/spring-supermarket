package market.controller;

import io.swagger.annotations.*;
import market.controller.request.ReportCreateRequest;
import market.dto.model.alert.AlertReadDto;
import market.dto.model.report.ReportDto;
import market.dto.model.report.ReportReadDto;
import market.dto.response.Response;
import market.service.AlertService;
import market.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/alerts")
@Api(tags = "alerts")
public class AlertController {
    @Autowired
    private AlertService alertService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CASHIER')")
    @ApiOperation(value = "${AlertController.readAll}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readAll() {
        List<AlertReadDto> alertReadDtoList = alertService.findAll();

        return Response.ok().setPayload(alertReadDtoList);
    }
}


