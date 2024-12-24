package com.adobe.aem.ecommerce.demos.core.models;

import com.adobe.aem.ecommerce.demos.core.beans.HeroBannerBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroBannerModel{

    @ValueMapValue
    private String technologyWeUse;

    @ValueMapValue
    private String src;

    @ChildResource
    private Resource technologies;

    private List<HeroBannerBean> technologyData;

    @PostConstruct
    protected void init() {
        technologyData = new ArrayList<>();
        Optional.ofNullable(technologies).ifPresent(items -> {
            Iterable<Resource> links = items.getChildren();
            links.forEach(link -> {
                ValueMap properties = link.getValueMap();
                HeroBannerBean techUsed = new HeroBannerBean(
                        properties.get("title", StringUtils.EMPTY),
                        properties.get("technology", StringUtils.EMPTY)
                );
                technologyData.add(techUsed);
            });
        });
    }


    public String getSrc() {
        return src;
    }

    public List<HeroBannerBean> getTechnologyData() {
        return technologyData;
    }

    public String getTechnologyWeUse() {
        return technologyWeUse;
    }
}
