package com.adobe.aem.ecommerce.demos.core.models;

import com.adobe.aem.ecommerce.demos.core.beans.ArticleCard;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
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
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ArticleCardModel.class);

    /**
     * RESOURCE_TYPE.
     */
    protected static final String RESOURCE_TYPE = "ecommercepractise/components/article-card";

    /**
     * articleRootPath
     */
    @Inject
    private String articleRootPath;

    /**
     * articleType
     */
    @Inject
    private String articleType;  // Default Value=banner

    /**
     * resourceResolver
     */
    @SlingObject
    private ResourceResolver resourceResolver;

    /**
     * String pageTitle
     */
    private String pageTitle;

    /**
     * String pageDescription
     */
    private String pageDescription;

    /**
     * String industryTag
     */
    private String industryTag;

    /**
     * String categoryTag
     */
    private String categoryTag;

    /**
     * String imagePathSrc
     */
    private String imagePathSrc;

    /**
     * String imageAltText
     */
    private String imageAltText;

    /**
     * String ctaUrl
     */
    private String ctaUrl;

    /**
     * String ctaLabel
     */
    private String ctaLabel;

    /**
     * String videoSrc
     */
    private String videoSrc;

    /**
     * articleCards
     */
    private List<ArticleCard> articleCards;

    /**
     * propertyMap
     */
    private Map<String, Object> propertyMap = new HashMap<>();

    /**
     * jsonStringData
     */
    private String jsonStringData;

    @PostConstruct
    protected void init() {
        articleCards = new ArrayList<>();
        Optional.ofNullable(resourceResolver.getResource(articleRootPath)).ifPresent(pageResource -> {
            Boolean isBanner = articleType.equals("banner");    // Setting whether its is Banner or Article-Card (using it later on for calling different Constructor)
            Page resourcePage = pageResource.adaptTo(Page.class);
            Iterator<Page> childPages = resourcePage.listChildren();
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                if ("articleCard".equals(articleType) || childPage.getProperties().get("showPageAsBanner", false)) {
                    processPage(childPage, articleCards);
                    if (isBanner) {
                        ArticleCard articleCard = new ArticleCard(industryTag, categoryTag, pageTitle, pageDescription, imagePathSrc, imageAltText);
                        articleCards.add(articleCard);
                    } else {
                        ArticleCard articleCard = new ArticleCard(industryTag, categoryTag, pageTitle, pageDescription, imagePathSrc, imageAltText, ctaLabel, ctaUrl, videoSrc);
                        articleCards.add(articleCard);
                    }
                }
            }
        });
        propertyMap.put("article-cards", articleCards);
        jsonStringData = generateJsonString(propertyMap);
        LOG.info("*** Final Data=>> {}", jsonStringData);
    }

    private void processPage(Page childPage, List<ArticleCard> articleCards) {
        pageTitle = childPage.getProperties().get("jcr:title", StringUtils.EMPTY);
        pageDescription = childPage.getProperties().get("jcr:description", StringUtils.EMPTY);
        ctaLabel = childPage.getProperties().get("ctaLabel", StringUtils.EMPTY);
        ctaUrl = childPage.getProperties().get("ctaUrl", StringUtils.EMPTY);
        videoSrc = childPage.getProperties().get("videoUrl", StringUtils.EMPTY);
        industryTag = getTagTitle(childPage.getProperties().get("industryTag", StringUtils.EMPTY));
        categoryTag = getTagTitle(childPage.getProperties().get("categoryTag", StringUtils.EMPTY));
        imagePathSrc = getImageReference(childPage).getLeft();
        imageAltText = getImageReference(childPage).getRight();
    }

    private String getTagTitle(String tagPath) {
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        return Optional.ofNullable(tagManager)
                .map(manager -> manager.resolve(tagPath))   // Resolve the tag
                .map(Tag::getTitle)
                .orElse(StringUtils.EMPTY);     // Return EMPTY if tagManager is null
    }

    private Pair<String, String> getImageReference(Page childPage) {
        return Optional.ofNullable(childPage)
                .map(page -> page.getContentResource("cq:featuredimage"))
                .map(resource -> resource.adaptTo(ValueMap.class))
                .map(properties -> Pair.of(
                        properties.get("fileReference", StringUtils.EMPTY), // First value
                        properties.get("alt", StringUtils.EMPTY) // Second value
                ))
                .orElse(Pair.of(StringUtils.EMPTY, StringUtils.EMPTY));
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

