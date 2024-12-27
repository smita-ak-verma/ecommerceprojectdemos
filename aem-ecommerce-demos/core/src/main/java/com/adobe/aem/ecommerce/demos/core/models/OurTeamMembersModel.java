package com.adobe.aem.ecommerce.demos.core.models;

import com.adobe.aem.ecommerce.demos.core.beans.TeamMember;
import java.util.List;

public interface OurTeamMembersModel {

    public String getSubheader();

    public String getTitle();

    public String getTeamOverview();

    public List<TeamMember> getTeamMembersDetails();

}
