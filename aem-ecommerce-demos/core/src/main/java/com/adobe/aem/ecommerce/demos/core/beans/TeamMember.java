package com.adobe.aem.ecommerce.demos.core.beans;

public class TeamMember {

    private String image;
    private String name;
    private String role;

    public TeamMember(String image, String name, String role) {
        this.image = image;
        this.name = name;
        this.role = role;  
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}

