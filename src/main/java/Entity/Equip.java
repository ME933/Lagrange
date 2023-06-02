package Entity;

import Entity.DataBaseEntity.EquipEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Equip {
    private Integer equipId;
    private Integer capability;

    public Equip(EquipEntity equip){
        this.equipId = Integer.valueOf(equip.getEquipId());
        this.capability = Integer.valueOf(equip.getCapability());
    }
}
