package org.thevoids.oncologic.dto.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Clase genérica para respuestas de API unificadas.
 * @param <T> Tipo de dato que se devolverá en caso de éxito
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String mensaje;
    private T datos;
    private boolean exito;
    
    /**
     * Constructor para respuesta exitosa con datos
     * @param mensaje Mensaje de éxito
     * @param datos Datos a devolver
     */
    public ApiResponse(String mensaje, T datos) {
        this.mensaje = mensaje;
        this.datos = datos;
        this.exito = true;
    }
    
    /**
     * Constructor para respuesta de error sin datos
     * @param mensaje Mensaje de error
     */
    public static <T> ApiResponse<T> error(String mensaje) {
        return new ApiResponse<>(mensaje, null, false);
    }
    
    /**
     * Método estático para crear una respuesta exitosa
     * @param mensaje Mensaje de éxito
     * @param datos Datos a devolver
     * @return Nueva instancia de ApiResponse con éxito
     */
    public static <T> ApiResponse<T> exito(String mensaje, T datos) {
        return new ApiResponse<>(mensaje, datos, true);
    }
} 