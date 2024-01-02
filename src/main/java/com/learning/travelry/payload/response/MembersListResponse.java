package com.learning.travelry.payload.response;


import com.learning.travelry.entities.PublicUser;

import java.util.List;

public class MembersListResponse {

    private List<PublicUser> members;

    public MembersListResponse(List<PublicUser> members) {
        this.members = members;
    }

    public List<PublicUser> getMembers() {
        return members;
    }

    public void setMembers(List<PublicUser> members) {
        this.members = members;
    }

}
