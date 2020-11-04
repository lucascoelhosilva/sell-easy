package com.coelho.models;

import com.coelho.config.MonetaryAmountConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.money.MonetaryAmount;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_EXPENSE")
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ACCOUNT", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CATEGORY", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CUSTOMER", nullable = false)
    @ToString.Exclude
    private Customer customer;

    @CreationTimestamp
    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "PAID", nullable = false)
    private Boolean paid;

    @Column(name = "REMINDER", nullable = false)
    private Boolean reminder;

    @Column(name = "RECURRENT", nullable = false)
    private Boolean recurrent;

    @Column(name = "VALUE")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount value;

}
