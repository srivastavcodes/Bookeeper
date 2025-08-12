package com.project.bookbackend.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.bookbackend.common.BaseEntity;
import com.project.bookbackend.feedback.Feedback;
import com.project.bookbackend.history.BookTransactionHistory;
import com.project.bookbackend.user.User;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;

    private String bookCover;

    private boolean isArchived;
    private boolean isShareable;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRating() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rating = this.feedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);

        // Returns 4.0 if rounded rate is less than 4.5 or else returns 4.5
        return Math.round(rating * 10.0) / 10.0;
    }
}
