package io.project.SpringDemoBot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
@ToString
@Data
@Getter
@Setter
@Entity(name = "usersDataTable") //говорит о том что данный класс нужно привязать к таблице
public class User {
    @Id
    private Long chatId;

    private String firstName;
    private String lastName;
    private String username;
    private Timestamp registeredAt;

    }

