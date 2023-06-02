package ScheduleMip.Data.Entity;

import ScheduleMip.Data.Entity.DataBaseEntity.StarEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class Star {
    private Integer starId;

    public Star(StarEntity starEntity){
        this.starId = starEntity.getStarId();
    }
}
