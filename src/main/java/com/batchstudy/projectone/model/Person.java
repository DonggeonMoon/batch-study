package com.batchstudy.projectone.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Person {
    private String name;
    private String birthday;
    private String email;
    private BigDecimal revenue;
    private Boolean isCustomer;

    @JsonProperty("isCustomer")
    public Boolean isCustomer() {
        return isCustomer;
    }

    public static Person from(Person person) {
        return Person.builder()
                .birthday(person.getBirthday())
                .email(person.getEmail())
                .isCustomer(person.isCustomer)
                .name(person.name)
                .revenue(person.revenue)
                .build();
    }
}
