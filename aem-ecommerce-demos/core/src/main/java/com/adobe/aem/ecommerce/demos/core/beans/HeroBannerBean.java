package com.adobe.aem.ecommerce.demos.core.beans;

public class HeroBannerBean {

    private String title;

    private String technology;

    public HeroBannerBean(String technology, String title) {
        this.technology = technology;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getTechnology() {
        return technology;
    }
}