package com.insider.sanjuanisland.models;

/**
 *
 */
public class Parent_Child_Relationships_Model {
    Integer Parent_Child_Relationship_ID, Parent_Location_ID, Child_Location_ID;

    public Integer getChild_Location_ID() {
        return Child_Location_ID;
    }

    public void setChild_Location_ID(Integer child_Location_ID) {
        Child_Location_ID = child_Location_ID;
    }

    public Integer getParent_Child_Relationship_ID() {
        return Parent_Child_Relationship_ID;
    }

    public void setParent_Child_Relationship_ID(Integer parent_Child_Relationship_ID) {
        Parent_Child_Relationship_ID = parent_Child_Relationship_ID;
    }

    public Integer getParent_Location_ID() {
        return Parent_Location_ID;
    }

    public void setParent_Location_ID(Integer parent_Location_ID) {
        Parent_Location_ID = parent_Location_ID;
    }
}
