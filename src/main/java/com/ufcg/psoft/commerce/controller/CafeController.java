package com.ufcg.psoft.commerce.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.ufcg.psoft.commerce.dto.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.CafeResponseDTO;
import com.ufcg.psoft.commerce.services.CafeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(
        value = "/cafes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class CafeController {

    @Autowired
    CafeService cafeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarCafe(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cafeService.recuperar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> listarCafes(
            @RequestParam(required = false, defaultValue = "") String nome) {
        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(cafeService.listarPorNome(nome));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cafeService.listar());
    }

    @PostMapping()
    public ResponseEntity<?> criarCafe(
            @RequestParam Long idFornecedor,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid CafePostPutRequestDTO cafePostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cafeService.criar(idFornecedor,codigoAcesso,cafePostPutRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCafe(
            @PathVariable Long id,
            @RequestParam Long idFornecedor,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid CafePostPutRequestDTO cafePostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cafeService.alterar(idFornecedor,id,codigoAcesso,cafePostPutRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCafe(
            @PathVariable Long id,
            @RequestParam Long idFornecedor,
            @RequestParam String codigoAcesso) {
        cafeService.remover(id,idFornecedor,codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}
