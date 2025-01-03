package com.adobe.aem.ecommerce.demos.core.models.impl;

import com.adobe.aem.ecommerce.demos.core.beans.TeamMember;
import com.adobe.aem.ecommerce.demos.core.models.OurTeamMembersModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = OurTeamMembersModel.class,
        resourceType = OurTeamMembersModelImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(  
            name = "jackson", 
            extensions ="json",
            options = {
                @ExporterOption(name = "SerializationFeature.WRAP_ROOT_VALUE", value="true")
            }
)
@JsonRootName("team-member")
public class OurTeamMembersModelImpl implements OurTeamMembersModel {

    /**
     * Logger LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(OurTeamMembersModelImpl.class);

    /**
     * RESOURCE_TYPE.
     */
    protected static final String RESOURCE_TYPE = "ecommercepractise/components/our-team-members";

    /**
     * SUB_HEADER.
     */
    @ValueMapValue
    private String subheader;

    /**
     * TITLE.
     */
    @ValueMapValue
    private String title;

    /**
     * TEAM_OVERVIEW.
     */
    @ValueMapValue
    private String teamOverview;

    /**
     * CHILD_RESOURCE.
     */
    @ChildResource
    private Resource teamMembers;

    private List<TeamMember> teamMembersDetails;

     /**
     * POST_CONSTRUCT.
     */
    @PostConstruct
    protected void init() {
        try{
            teamMembersDetails = new ArrayList<>();
            Optional.ofNullable(teamMembers).ifPresent(items -> {
                Iterable<Resource> links = items.getChildren();
                links.forEach(link -> {
                    ValueMap properties = link.getValueMap();
                    TeamMember teamMember = new TeamMember(
                            properties.get("image", StringUtils.EMPTY),
                            properties.get("alt", StringUtils.EMPTY),
                            properties.get("name", StringUtils.EMPTY),
                            properties.get("role", StringUtils.EMPTY)
                    );
                    teamMembersDetails.add(teamMember);
                    LOG.info("Team Member Details : " +teamMember);
                });
            });
        }catch (Exception e){
            LOG.info("\n ERROR while getting Member Details {} ",e.getMessage());
        }
    }

    @Override
    public String getSubheader(){
        return subheader;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getTeamOverview() {
        return teamOverview;
    }

    @Override
    @JsonProperty(value = "teamMembers")
    public List<TeamMember> getTeamMembersDetails() {
        return teamMembersDetails;
    }
    
}
