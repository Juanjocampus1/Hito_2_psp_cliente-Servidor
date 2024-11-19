package com.empresa.Model;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Libro {
    private int id;
    private String titulo;
    private String autor;
}
