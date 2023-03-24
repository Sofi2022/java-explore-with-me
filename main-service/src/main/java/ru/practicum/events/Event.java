package ru.practicum.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.Location;
import ru.practicum.categories.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 200)
    private String annotation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

   // private Integer confirmedRequests;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User initiator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(columnDefinition = "boolean default false")
    private Boolean paid;

    @Column(columnDefinition = "integer default 0")
    private Integer participantLimit; // ограничение мест

    @Column(columnDefinition = "boolean default true")
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие.true -ожидать подтверждения

    @Size(min = 3, max = 120)
    private String title;
}
