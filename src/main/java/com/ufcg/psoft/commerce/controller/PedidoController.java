package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/pedidos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @PostMapping()
    public ResponseEntity<?> criarPedido(
            @RequestParam Long idCliente,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO
            ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.criar(idCliente,codigoAcesso,pedidoPostPutRequestDTO));
    }
}
