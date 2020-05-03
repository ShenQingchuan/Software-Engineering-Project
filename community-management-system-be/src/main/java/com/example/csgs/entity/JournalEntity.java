package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "journal")
public class JournalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String titleName;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    String content;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    JournalTypeEntity type;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    UserEntity creator;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
}
