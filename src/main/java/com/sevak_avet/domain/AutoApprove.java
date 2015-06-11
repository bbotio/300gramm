package com.sevak_avet.domain;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 10:48
 */
@Entity
@Table(name = "auto_approve")
public class AutoApprove {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="username")
    private String username;

    @Column(name="period")
    private Integer period;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AutoApprove that = (AutoApprove) o;

        if (!id.equals(that.id)) return false;
        if (!username.equals(that.username)) return false;
        return period.equals(that.period);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + period.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AutoApprove{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", period=" + period +
                '}';
    }
}
