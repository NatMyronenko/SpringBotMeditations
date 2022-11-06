package io.project.SpringDemoBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "usersDataTable") //говорит о том что данный класс нужно привязать к таблице
public class User {
    @Id
    private Long chatId;

    private String firstName;
    private String lastName;
    private String username;
    private Timestamp registeredAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return chatId != null && Objects.equals(chatId, user.chatId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

