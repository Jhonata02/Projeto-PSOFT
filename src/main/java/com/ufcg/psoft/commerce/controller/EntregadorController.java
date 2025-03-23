package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.services.entregador.EntregadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/entregadores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EntregadorController {

    @Autowired
    EntregadorService entregadorService;

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarEntregadores(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorService.recuperar(id));

    }

    @GetMapping("")
    public ResponseEntity<?> listarEntregadores(
            @RequestParam(required = false, defaultValue = "") String nome) {
        if (nome != null && !nome.isEmpty()) {
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(entregadorService.listarPorNome(nome));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorService.listar());
    }

    @PostMapping()
    public ResponseEntity<?> criarEntregador(
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entregadorService.criar(entregadorPostPutRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEntregador(
            @PathVariable Long id,
            @RequestParam String codigo,
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorService.alterar(id, codigo, entregadorPostPutRequestDTO));
    }

    @PutMapping("/{id}/alterar-status")
    public ResponseEntity<?> alterarStatusEntregador(
            @PathVariable Long id,
            @RequestParam String codigoAcesso) {
        entregadorService.alterarStatus(id,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Status alterado!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirEntregador(
            @PathVariable Long id,
            @RequestParam String codigo) {
        entregadorService.remover(id, codigo);
        return  ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

}
