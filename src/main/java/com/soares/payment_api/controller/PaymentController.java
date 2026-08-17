package com.soares.payment_api.controller;

import com.soares.payment_api.service.PaymentService;
import com.soares.payment_api.dto.PaymentRequest;
import com.soares.payment_api.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("payments")
@Tag(
        name = "Payments",
        description = "Endpoints for payment management"
)
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @Operation(
            summary = "Create a payment",
            description = "Creates a new payment with PENDING status and generates a QR Code"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment data"),
            @ApiResponse(responseCode = "500", description = "Error generating QR Code")
    })
    @PostMapping
    public PaymentResponse create(@Valid @RequestBody PaymentRequest request) {
        return service.save(request);
    }

    @Operation(
            summary = "List all payments",
            description = "Returns all registered payments"
    )
    @GetMapping
    public List<PaymentResponse> getAll(){
        return service.findAll();
    }
    @Operation(
            summary = "Find payment by ID",
            description = "Returns a specific payment by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment found successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
    })

    @GetMapping("/{id}")
    public  PaymentResponse getById(@PathVariable Long id){
        return  service.findById(id);
    }

    @Operation(
            summary = "Update a payment",
            description = "Updates amount and description of a pending payment"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Payment is not PENDING"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })

    @PutMapping("/{id}")
    public PaymentResponse update(@PathVariable Long id, @Valid @RequestBody PaymentRequest request){
        return service.update(id, request);
    }

    @Operation(
            summary = "Delete a payment",
            description = "Deletes a payment by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
    })

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @Operation(
            summary = "Pay a payment",
            description = "Changes a pending payment status to PAID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment paid successfully"),
            @ApiResponse(responseCode = "400", description = "Payment cannot be paid"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{id}/pay")
    public PaymentResponse pay(@PathVariable Long id){
        return service.pay(id);
    }

    @Operation(
            summary = "Cancel a payment",
            description = "Changes a pending payment status to CANCELED"
    )
        @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment cancelled successfully"),
        @ApiResponse(responseCode = "400", description = "Payment cannot be cancelled"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
})

    @PostMapping("/{id}/cancel")
    public PaymentResponse cancel(@PathVariable Long id){
        return service.cancel(id);
    }

}
