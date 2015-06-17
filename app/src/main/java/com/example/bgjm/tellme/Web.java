package com.example.bgjm.tellme;


/**
 * Created by Borja on 17/06/2015.
 */
public class Web {
    private String titulo;
    private String url;

    public Web(String titulo, String Url) {
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
