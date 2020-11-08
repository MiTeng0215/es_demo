package com.aegis.es_demo.domin;

import com.aegis.es_demo.jpaListener.GirlFriendListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "girl_friend")
@Getter
@Setter
@ToString
@EntityListeners(value = {GirlFriendListener.class})
public class GirlFriend {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "state")
    private Integer state;

    @Transient
    private Integer oldState;



}
