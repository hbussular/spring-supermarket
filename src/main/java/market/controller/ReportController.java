package market.controller;

import io.swagger.annotations.*;
import market.controller.request.ReportCreateRequest;
import market.dto.model.report.ReportDto;
import market.dto.model.report.ReportReadDto;
import market.dto.response.Response;
import market.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reports")
@Api(tags = "reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CASHIER')")
    @ApiOperation(value = "${ReportController.requestByType}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response requestByType(@ApiParam("Request Report") @RequestBody ReportCreateRequest reportCreateRequest) {
        ReportDto reportDto = new ReportDto()
                .setType(reportCreateRequest.getType());

        ReportReadDto reportReadDto = reportService.create(reportDto);
        return Response.ok()
                .setPayload(reportReadDto)
                .setMessage("Your report request has been successfully created. In a few moments your order will be processed and you will be able to consult it.");
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CASHIER')")
    @ApiOperation(value = "${ReportController.readByType}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readByType(@ApiParam("Request Report") @RequestBody ReportCreateRequest reportCreateRequest) {
        ReportReadDto reportReadDto = reportService.findLatestByType(reportCreateRequest.getType());
        return Response.ok().setPayload(reportReadDto);
    }
}


