package com.sevak_avet.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by savetisyan on 05.07.15.
 */
@Entity
@Table(name = "anti_spam")
public class AntiSpam {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @ElementCollection
    @Column(name = "bad_words", nullable = false)
    private Set<String> badWords;

    @Column(name = "is_antispam_enabled", nullable = false)
    private Boolean isAntiSpamEnabled;

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

    public Set<String> getBadWords() {
        return badWords;
    }

    public void setBadWords(Set<String> badWords) {
        this.badWords = badWords;
    }

    public Boolean isAntiSpamEnabled() {
        return isAntiSpamEnabled;
    }

    public void setAntiSpamEnabled(Boolean isAntiSpamEnabled) {
        this.isAntiSpamEnabled = isAntiSpamEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AntiSpam antiSpam = (AntiSpam) o;

        if (!id.equals(antiSpam.id)) return false;
        if (!username.equals(antiSpam.username)) return false;
        if (!badWords.equals(antiSpam.badWords)) return false;
        return isAntiSpamEnabled.equals(antiSpam.isAntiSpamEnabled);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + badWords.hashCode();
        result = 31 * result + isAntiSpamEnabled.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AntiSpam{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", badWords=" + badWords +
                ", isAntiSpamEnabled=" + isAntiSpamEnabled +
                '}';
    }
}
