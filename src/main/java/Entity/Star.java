package Entity;

import Entity.DataBaseEntity.StarEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Star {
    private Integer starId;

    public Star(StarEntity starEntity){
        this.starId = starEntity.getStarId();
    }
}
