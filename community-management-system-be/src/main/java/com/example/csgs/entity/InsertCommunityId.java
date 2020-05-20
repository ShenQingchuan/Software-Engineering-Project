package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertCommunityId {
    private Long pid;
    private Long CommunityId;

    public InsertCommunityId(Long communityId) {
        CommunityId = communityId;
    }
}
