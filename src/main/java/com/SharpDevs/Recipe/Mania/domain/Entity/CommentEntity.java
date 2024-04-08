package com.SharpDevs.Recipe.Mania.domain.Entity;

import com.SharpDevs.Recipe.Mania.Service.RecipeService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    @NotNull(message = "Created Date Cannot be Null")
    private LocalDate created;

    @Column(name = "content")
    @NotBlank(message  = "Content Cannot be Blank")
    private String content;

    @JoinColumn(name = "user_id")
    private UserEntity user;


//    @Column(name = "replies")
//    @OneToMany
//    private Set<CommentReply> replies;
}
