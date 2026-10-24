package com.mytasks.model;

import java.time.LocalDate;

public class Tarefa {

    private int id;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private StatusTarefa status;
    private LocalDate dataEntrega;
    private int usuarioId;

    public Tarefa(String titulo, String descricao, Categoria categoria,
                  StatusTarefa status, LocalDate dataEntrega, int usuarioId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
        this.dataEntrega = dataEntrega;
        this.usuarioId = usuarioId;
    }

    public Tarefa(int id, String titulo, String descricao, Categoria categoria,
                  StatusTarefa status, LocalDate dataEntrega, int usuarioId) {
        this(titulo, descricao, categoria, status, dataEntrega, usuarioId);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public int getUsuarioId() {
        return usuarioId;
    }
}
