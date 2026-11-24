package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.powers.OrmosPower;

public class OrmosAction extends AbstractGameAction {

    public OrmosAction() {
    }

    public void update() {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof OrmosPower){
                power.onSpecificTrigger();//触发喧繁之港的能力效果
            }
        }

        this.isDone = true; // 动作完成
    }
}