package com.example.csgs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntity {
    private Long id;
    private String titleName;
    private String content;
    private UserEntity creatorId;
    private JournalTypeEntity typeId;
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private Date createTime;
}
