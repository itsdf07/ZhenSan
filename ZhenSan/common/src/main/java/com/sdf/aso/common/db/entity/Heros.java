package com.sdf.aso.common.db.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "HEROS".
 */
public class Heros {

    private Long id;
    /** Not-null value. */
    private String name;
    private String camp;

    public Heros() {
    }

    public Heros(Long id) {
        this.id = id;
    }

    public Heros(Long id, String name, String camp) {
        this.id = id;
        this.name = name;
        this.camp = camp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public String getCamp() {
        return camp;
    }

    public void setCamp(String camp) {
        this.camp = camp;
    }

}