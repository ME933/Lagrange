package ScheduleMip.Data.Entity.DataBaseEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "equip", schema = "schedule", catalog = "")
public class EquipEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "DEVICECODE")
    private String equipId;
    @Basic
    @Column(name = "CAPABILITY")
    private String capability;

}
