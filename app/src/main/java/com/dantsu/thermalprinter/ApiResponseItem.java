package com.dantsu.thermalprinter;

public class ApiResponseItem {
    private int id;
    private String nome;
    private int coperti;
    private int commanda__id;
    private int commanda__product_id;
    private int commanda__quantity;
    private String commanda__production_status;
    private String commanda__note;
    private String commanda__to_production;
    private String commanda__product__title;
    private String commanda__product__price;
    private int commanda__product__collection_id;
    private int commanda__product__tipo_prodotto_id;

    // Getters and Setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCoperti() {
        return coperti;
    }

    public void setCoperti(int coperti) {
        this.coperti = coperti;
    }

    public int getCommanda__id() {
        return commanda__id;
    }

    public void setCommanda__id(int commanda__id) {
        this.commanda__id = commanda__id;
    }

    public int getCommanda__product_id() {
        return commanda__product_id;
    }

    public void setCommanda__product_id(int commanda__product_id) {
        this.commanda__product_id = commanda__product_id;
    }

    public int getCommanda__quantity() {
        return commanda__quantity;
    }

    public void setCommanda__quantity(int commanda__quantity) {
        this.commanda__quantity = commanda__quantity;
    }

    public String getCommanda__production_status() {
        return commanda__production_status;
    }

    public void setCommanda__production_status(String commanda__production_status) {
        this.commanda__production_status = commanda__production_status;
    }

    public String getCommanda__note() {
        return commanda__note;
    }

    public void setCommanda__note(String commanda__note) {
        this.commanda__note = commanda__note;
    }

    public String getCommanda__to_production() {
        return commanda__to_production;
    }

    public void setCommanda__to_production(String commanda__to_production) {
        this.commanda__to_production = commanda__to_production;
    }

    public String getCommanda__product__title() {
        return commanda__product__title;
    }

    public void setCommanda__product__title(String commanda__product__title) {
        this.commanda__product__title = commanda__product__title;
    }

    public String getCommanda__product__price() {
        return commanda__product__price;
    }

    public void setCommanda__product__price(String commanda__product__price) {
        this.commanda__product__price = commanda__product__price;
    }

    public int getCommanda__product__collection_id() {
        return commanda__product__collection_id;
    }

    public void setCommanda__product__collection_id(int commanda__product__collection_id) {
        this.commanda__product__collection_id = commanda__product__collection_id;
    }

    public int getCommanda__product__tipo_prodotto_id() {
        return commanda__product__tipo_prodotto_id;
    }

    public void setCommanda__product__tipo_prodotto_id(int commanda__product__tipo_prodotto_id) {
        this.commanda__product__tipo_prodotto_id = commanda__product__tipo_prodotto_id;
    }
}