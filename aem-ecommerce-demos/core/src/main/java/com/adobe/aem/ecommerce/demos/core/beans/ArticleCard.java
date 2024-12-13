package com.adobe.aem.ecommerce.demos.core.beans;

public class ArticleCard {


    private String pageTitle;

    private String pageDescription;

    private String imagePathRef;

    public ArticleCard(String title, String description, String imagePath) {
        this.pageTitle = title;
        this.pageDescription = description;
        this.imagePathRef = imagePath;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getImagePathRef() {
        return imagePathRef;
    }

    public String getPageDescription() {
        return pageDescription;
    }
}
