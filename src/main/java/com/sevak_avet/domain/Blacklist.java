package com.sevak_avet.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Avetisyan Sevak
 * Date: 18.07.2015
 * Time: 19:44
 */

@Entity
@Table(name = "blacklist")
public class Blacklist {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @ElementCollection
    @Column(name = "blacklist", nullable = false)
    private Set<String> blacklist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Set<String> blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blacklist blacklist1 = (Blacklist) o;

        if (!id.equals(blacklist1.id)) return false;
        if (!username.equals(blacklist1.username)) return false;
        return blacklist.equals(blacklist1.blacklist);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + blacklist.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Blacklist{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", blacklist=" + blacklist +
                '}';
    }
}
