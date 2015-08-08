package com.example.bgjm.tellme;


import java.io.Serializable;

/**
 * @author hefesto
 */
public class Web implements Serializable {
    private static final long serialVersionUID = 4349879151820234260L;
    private String titulo;
    private String url;
    public static final String WEB="web";

    public Web(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
