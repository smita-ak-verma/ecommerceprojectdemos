package com.adobe.aem.ecommerce.demos.core.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCard {

    private String title;

    private String description;

    private String src;

    private String ctaUrl;

    private String ctaLabel;

    private String videoSrc;

    private String industry;

    private String category;

    private String imgAlt;

    public ArticleCard(String industry, String category, String title, String description, String imageSrc, String imageAltText) {
        this.industry = industry;
        this.category = category;
        this.title = title;
        this.description = description;
        this.src = imageSrc;
        this.imgAlt = imageAltText;
    }

    public ArticleCard(String industry, String category, String title, String description, String imageSrc, String imageAltText, String ctaLabel, String ctaUrl, String videoSrc) {
        this.industry = industry;
        this.category = category;
        this.title = title;
        this.description = description;
        this.src = imageSrc;
        this.imgAlt = imageAltText;
        this.ctaLabel = ctaLabel;
        this.ctaUrl = ctaUrl;
        this.videoSrc = videoSrc;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSrc() {
        return src;
    }

    public String getCtaUrl() {
        return ctaUrl;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public String getVideoSrc() {
        return videoSrc;
    }

    public String getIndustry() {
        return industry;
    }

    public String getCategory() {
        return category;
    }

    public String getImgAlt() {
        return imgAlt;
    }
}
