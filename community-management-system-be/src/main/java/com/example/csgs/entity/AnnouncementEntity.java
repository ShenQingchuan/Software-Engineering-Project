package com.example.csgs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementEntity {
    Long id;
    String titleName;
    String content;
    UserEntity creator;
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    Date createTime;
}
