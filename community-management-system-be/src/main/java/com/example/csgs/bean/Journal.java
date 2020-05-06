package com.example.csgs.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Journal {
    private long id;
    private String titleName;
    private String type;
    private String creator;
    private String creatTime;

}
