package com.sistema.academico.sistema_academico.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Envoltorio simple para respuestas paginadas: evita exponer directamente
 * el objeto Page de Spring (que trae metadatos internos que no siempre queremos publicar)
 * y deja claro en Swagger qué forma tiene la respuesta.
 */
@Getter
@Builder
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> contenido;
    private int paginaActual;
    private int tamanoPagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean esUltimaPagina;

    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return PageResponseDTO.<T>builder()
                .contenido(page.getContent())
                .paginaActual(page.getNumber())
                .tamanoPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .esUltimaPagina(page.isLast())
                .build();
    }
}
