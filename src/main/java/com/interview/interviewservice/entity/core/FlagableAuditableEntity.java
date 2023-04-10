package com.interview.interviewservice.entity.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.interview.interviewservice.model.Flag;
import com.interview.interviewservice.model.Flagable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class FlagableAuditableEntity  extends AuditableEntity implements Flagable, Serializable {

    @Convert(converter = JpaConverter.FlagConverter.class)
    @Basic(optional = true)
    @Column(name = "flag")
    @Enumerated(EnumType.ORDINAL)
    @Audited
    private Flag flag;
}
