package com.tasc.project.employee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tasc.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fullName;

    @Column(length = 1)
    private String gender;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dob;

    private String email;

    private String phone;

//    private String type;

    private BigDecimal salary;

    private BigDecimal bonus;

    private String description;

    @Lob
    private String detail;

    // relationship
    private long departmentId;

    private long roleId;

    private long userId;

    private long positionId;

}