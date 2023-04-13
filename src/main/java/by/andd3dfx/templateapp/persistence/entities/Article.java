package by.andd3dfx.templateapp.persistence.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_id_seq")
    @SequenceGenerator(name = "article_id_seq", sequenceName = "article_id_seq", allocationSize = 1)
    @Column
    private Long id;

    @Column
    private String title;

    @Column
    private String summary;

    @Column
    private String text;

    @Column(name = "ts")
    private LocalDateTime timestamp;

    @Column
    private String author;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        dateUpdated = LocalDateTime.now();
        if (dateCreated == null) {
            dateCreated = dateUpdated;
        }
    }
}
