package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.MetodoPagamento;
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

    @GetMapping("{id}")
    public ResponseEntity<?> recuperarPedido(
            @PathVariable Long id,
            @RequestParam Long idCliente,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.recuperar(id,idCliente,codigoAcesso));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> excluirPedido(
            @PathVariable Long id,
            @RequestParam Long idCliente,
            @RequestParam String codigoAcesso) {
        pedidoService.remover(id,idCliente,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editarPedido(
            @PathVariable Long id,
            @RequestParam Long idCliente,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.alterar(id,idCliente,codigoAcesso,pedidoPostPutRequestDTO));
    }

    @PutMapping("{id}/confirmar-pagamento")
    public ResponseEntity<?> aprovarPagamento(
            @PathVariable Long id,
            @RequestParam Long idCLiente,
            @RequestParam String codigoAcesso,
            @RequestParam MetodoPagamento metodoPagamento) {
        pedidoService.confirmarPagamento(id,idCLiente,codigoAcesso,metodoPagamento);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }

}
