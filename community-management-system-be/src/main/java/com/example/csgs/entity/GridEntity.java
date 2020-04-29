package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grid")
public class GridEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String districtName;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userEntity", referencedColumnName = "id")
    public UserEntity userEntity;

}