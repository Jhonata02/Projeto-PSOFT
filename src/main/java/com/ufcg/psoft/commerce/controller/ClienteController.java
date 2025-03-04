package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/clientes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarCliente(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.recuperar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> listarClientes(
            @RequestParam(required = false, defaultValue = "") String nome) {

        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(clienteService.listarPorNome(nome));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.listar());
    }

    @GetMapping("/{id}/exibir-catalogo")
    public ResponseEntity<?> exibirCatalogo(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "") String tipo,
            @RequestParam(required = false,defaultValue = "") String origem,
            @RequestParam(required = false, defaultValue = "") String perfilSensorial) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.exibirCatalogo(id,tipo,origem,perfilSensorial));
    }

    @PostMapping()
    public ResponseEntity<?> criarCliente(
            @RequestBody @Valid ClientePostPutRequestDTO clientePostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.criar(clientePostPutRequestDto));
    }

    @PutMapping("{id}/demonstrar-interesse-cafe")
    public ResponseEntity<?> demonstrarInteresseEmCafe(
            @PathVariable Long id,
            @RequestParam Long idCafe,
            @RequestParam String codigoAcesso) {
        clienteService.demonstrarInteresseEmCafe(id,idCafe,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Cafe adicionado na lista de interesses");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable Long id,
            @RequestParam String codigo,
            @RequestBody @Valid ClientePostPutRequestDTO clientePostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.alterar(id, codigo, clientePostPutRequestDto));
    }

    @PutMapping("/{id}/alterar-plano")
    public ResponseEntity<?> alterarPlano(
            @PathVariable Long id,
            @RequestParam String codigoAcesso) {
        clienteService.alterarPlano(id,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Plano alterado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(
            @PathVariable Long id,
            @RequestParam String codigo) {
        clienteService.remover(id, codigo);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}