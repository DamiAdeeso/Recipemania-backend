package com.SharpDevs.Recipe.Mania.domain.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "Blog")
public class BlogsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "created")
    @NotBlank(message = "created cannot be blank")
    private Date created;

    @Column(name = "updated")
    @NotBlank(message = "updated cannot be blank")

    private Date updated;

    @Column(name = "title")
    @NotBlank(message = "Title cannot be blank")
    private String title ="test";
    ArrayList list = new ArrayList();
    @Column(name = "permLink")
    @NotBlank(message = "permLink cannot be blank")
    private String permLink;

    @Column(name = "shortDescription")
    @NotBlank(message = "shortDescription cannot be blank")
    private String shortDescription;

    @Column(name = "description")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Column(name = "image")
    @NotBlank(message = "Image cannot be blank")
    private String image;

    @Column(name = "metaDescription")
    @NotBlank(message = "metaDescription cannot be blank")
    private String metaDescription;


}

