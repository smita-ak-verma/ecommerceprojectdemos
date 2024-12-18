package com.adobe.aem.ecommerce.demos.core.models;

import com.adobe.aem.ecommerce.demos.core.beans.ArticleCard;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
        resourceType = ArticleCardModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleCardModel {

    /**
     * Logger LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(ArticleCardModel.class);

    /**
     * RESOURCE_TYPE.
     */
    protected static final String RESOURCE_TYPE = "ecommercepractise/components/article-card";

    @Inject
    private String articleRootPath;

    @Inject
    private String articleType;

    /**
     * resourceResolver
     */
    @SlingObject
    private ResourceResolver resourceResolver;

    private List<ArticleCard> articleCards;

    private Map<String, Object> propertyMap= new HashMap<>();

    private String jsonStringData;

    @PostConstruct
    protected void init() {
    articleCards = new ArrayList<>();
        Optional.ofNullable(resourceResolver.getResource(articleRootPath)).ifPresent(pageResource -> {
        Page resourcePage = pageResource.adaptTo(Page.class);
        Iterator<Page> childPages = resourcePage.listChildren();
        while (childPages.hasNext()) {
            Page childPage = childPages.next();

            if ("articleCard".equals(articleType) || childPage.getProperties().get("showPageAsBanner", false)) {
                processPage(childPage, articleCards);
            }
        }
    });
        propertyMap.put("article-cards", articleCards);
        jsonStringData = generateJsonString(propertyMap);
        LOG.info("Final Json Data=>> {}", jsonStringData);
    }


    private void processPage(Page childPage, List<ArticleCard> articleCards) {
        String pageTitle = childPage.getProperties().get("jcr:title", StringUtils.EMPTY);
        String pageDescription = childPage.getProperties().get("jcr:description", StringUtils.EMPTY);
        String imagePathRef = getImageReference(childPage);
        ArticleCard articleCard = new ArticleCard(pageTitle, pageDescription, imagePathRef);

        articleCards.add(articleCard);
    }

    private String getImageReference(Page childPage) {
        return Optional.ofNullable(childPage) // Check if childPage is not null
                .map(page -> page.getContentResource("cq:featuredimage")) // Get the featured image resource
                .map(resource -> resource.adaptTo(ValueMap.class)) // Adapt the resource to a ValueMap
                .map(properties -> properties.get("fileReference", String.class)) // Get the fileReference property
                .orElse(StringUtils.EMPTY); // Return EMPTY if any step is null or missing
    }

    private String generateJsonString(final Map<String, Object> commonPropMap) {
        String jsonStringResp = null;
        try {
            jsonStringResp = new ObjectMapper().writeValueAsString(commonPropMap);
        } catch (JsonProcessingException e) {
            LOG.error("Error while converting to json object", e);
        }
        return jsonStringResp;
    }

    public List<ArticleCard> getArticleCards() {
        return articleCards;
    }

    public String getArticleRootPath() {
        return articleRootPath;
    }

    public String getJsonStringData() {
        return jsonStringData;
    }

}

