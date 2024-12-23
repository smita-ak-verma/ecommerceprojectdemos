package com.adobe.aem.ecommerce.demos.core.models;

import com.adobe.aem.ecommerce.demos.core.beans.TeamMember;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import javax.inject.Inject;
import java.util.*;

@Model(adaptables = Resource.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.REQUIRED)
public class OurTeamMembersModel{

    private final static Logger LOGGER =  Logger.getLogger(OurTeamMembersModel.class);

    @ChildResource(name = "teamMembers")
    private List<Resource> teamMemberResources;

    private List<TeamMember> teamMembers;

    public List<TeamMember> getTeamMembers() {
        if (teamMembers == null) {
            teamMembers = new ArrayList<>();
            if (teamMemberResources != null) {
                for (Resource resource : teamMemberResources) {
                    TeamMember member = new TeamMember();
                    member.setName(resource.getValueMap().get("name", String.class));
                    member.setRole(resource.getValueMap().get("role", String.class));
                    member.setImage(resource.getValueMap().get("image", String.class));
                    teamMembers.add(member);
                }
            }
        }
        return teamMembers;
    }

}
