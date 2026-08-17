package com.soares.payment_api.controller;

import com.soares.payment_api.service.PaymentService;
import com.soares.payment_api.dto.PaymentRequest;
import com.soares.payment_api.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResponse create(@Valid @RequestBody PaymentRequest request) {
        return service.save(request);
    }

    @GetMapping
    public List<PaymentResponse> getAll(){
        return service.findAll();
    }
    @GetMapping("{id}")
    public  PaymentResponse getById(@PathVariable Long id){
        return  service.findById(id);
    }
    @PutMapping("{id}")
    public PaymentResponse update(@PathVariable Long id, @Valid @RequestBody PaymentRequest request){
        return service.update(id, request);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PostMapping("/{id}/pay")
    public PaymentResponse pay(@PathVariable Long id){
        return service.pay(id);
    }

    @PostMapping("/{id}/cancel")
    public PaymentResponse cancel(@PathVariable Long id){
        return service.cancel(id);
    }

}
