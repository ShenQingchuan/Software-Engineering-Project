package com.example.csgs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CommunityInfo")
public class CommunityInfoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "districtID",referencedColumnName = "id")
    DistrictEntity districtID;

    @Column(nullable = false)
    String community;               //所属小区

    @Column(nullable = false)       //小区住在数量
    Long numHouses;

    @Column(nullable = false)       //小区居民数量
    Long numResidents;

    @Column(nullable = false)       //小区停车位数量
    Long numParkingSpaces;
}