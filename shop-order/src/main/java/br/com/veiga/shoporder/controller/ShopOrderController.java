package br.com.veiga.shoporder.controller;

import br.com.veiga.shoporder.dto.ShopOrderDTO;
import br.com.veiga.shoporder.service.ShopOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class ShopOrderController {

    private final ShopOrderService service;

    public ShopOrderController(ShopOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody ShopOrderDTO orderDTO) {
        service.create(orderDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ShopOrderDTO>> getAll(Pageable pageable) {
        Page<ShopOrderDTO> orderDTOs = service.getAll(pageable);
        return ResponseEntity.ok(orderDTOs);
    }
}
