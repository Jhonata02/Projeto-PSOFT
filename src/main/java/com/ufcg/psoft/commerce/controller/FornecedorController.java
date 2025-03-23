package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.FornecedorPostPutRequestDTO;
import com.ufcg.psoft.commerce.services.cafe.CafeService;
import com.ufcg.psoft.commerce.services.fornecedor.FornecedorService;
import com.ufcg.psoft.commerce.services.pedido.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/fornecedores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class FornecedorController {
    @Autowired
    FornecedorService fornecedorService;
    @Autowired
    PedidoService pedidoService;
    @Autowired
    CafeService cafeService;

    @PostMapping()
    public ResponseEntity<?> criarFornecedor(
            @RequestBody @Valid FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fornecedorService.criar(fornecedorPostPutRequestDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarFornecedor(
            @PathVariable Long id,
            @RequestParam String codigo,
            @RequestBody @Valid FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fornecedorService.alterar(id, codigo, fornecedorPostPutRequestDTO));
    }

    @PutMapping("/{id}/aprovar-entregador")
    public ResponseEntity<?> aprovarEntregador(
            @PathVariable Long id,
            @RequestParam Long idEntregador,
            @RequestParam String codigoAcesso) {
        fornecedorService.aprovarEntregador(id,idEntregador,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Entregador Aprovado");
    }

    @PutMapping("/{id}/rejeitar-entregador")
    public ResponseEntity<?> rejeitarEntregador(
            @PathVariable Long id,
            @RequestParam Long idEntregador,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Entregador Rejeitado");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirFornecedor(
            @PathVariable Long id,
            @RequestParam String codigo) {
        fornecedorService.remover(id, codigo);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarFornecedor(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fornecedorService.recuperar(id));
    }

    @PutMapping("/{id}/Alterar-disponibilidade-cafe")
    public ResponseEntity<?> alterarDisponibilidadeCafe(
            @PathVariable Long id,
            @RequestParam Long idFornecedor,
            @RequestParam String codigoAcesso) {
        cafeService.alterarDisponibilidadeCafe(id,idFornecedor,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Disponibilidade alterada");
    }

    @PutMapping("{id}/alterar-status-pedido-para-pronto")
    public ResponseEntity<?> alterarStatusPedidoParaPronto(
            @PathVariable Long id,
            @RequestParam Long idFornecedor,
            @RequestParam String codigoAcesso) {
        pedidoService.alterarStatusPedidoParaPronto(id,idFornecedor,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Status do pedido alterado para PEDIDO_PRONTO");
    }

    @PutMapping("{id}/alterar-status-pedido-para-entrega")
    public ResponseEntity<?> alterarStatusPedidoParaEntrega(
            @PathVariable Long id,
            @RequestParam Long idForncedor,
            @RequestParam String codigoAcesso) {
        pedidoService.alterarStatusPedidoEmEntrega(id,idForncedor,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Status do pedido alterado para PEDIDO_EM_ENTREGA");
    }
}



