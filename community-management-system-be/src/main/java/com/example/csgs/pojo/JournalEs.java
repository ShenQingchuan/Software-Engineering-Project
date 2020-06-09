package com.example.csgs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalEs {
    private long id;
    private String titleName;
    private String typeName;
    private String creator;
    private String createTime;

}
