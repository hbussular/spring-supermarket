package market.controller;

import io.swagger.annotations.*;
import market.controller.request.ProductCreateRequest;

import market.controller.request.ProductUpdateRequest;
import market.dto.model.product.ProductDto;
import market.dto.model.product.ProductReadDto;
import market.dto.model.product.ProductCreateDto;

import market.dto.response.Response;
import market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/products")
@Api(tags = "products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CASHIER')")
    @ApiOperation(value = "${ProductController.readById}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readById(@PathVariable("id") Long id) {
        ProductReadDto productDto = productService.findById(id);
        return Response.ok().setPayload(productDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${ProductController.readAll}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response readAll() {
        List<ProductReadDto> allProductDto = productService.findAll();
        return Response.ok().setPayload(allProductDto);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CASHIER')")
    @ApiOperation(value = "${ProductController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Product name is already in use")})
    public Response create(@ApiParam("Create Product") @RequestBody ProductCreateRequest product) {
        ProductCreateDto productDto = new ProductCreateDto()
                .setCategory(product.getCategory())
                .setQuantity(product.getQuantity())
                .setPrice(product.getPrice())
                .setName(product.getName());

        ProductReadDto newProduct = productService.create(productDto);

        return Response.ok().setPayload(newProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CASHIER')")
    @ApiOperation(value = "${ProductController.updateById}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response updateBy(@RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        ProductDto productDto = new ProductDto()
                .setId(productUpdateRequest.getId())
                .setName(productUpdateRequest.getName())
                .setQuantity(productUpdateRequest.getQuantity())
                .setCategory(productUpdateRequest.getCategory())
                .setPrice(productUpdateRequest.getPrice());

        ProductReadDto updatedProductDto = productService.update(productDto);

        return Response.ok().setPayload(updatedProductDto);
    }
}


