package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.powers.NahidaMarkPower;

import java.util.Iterator;
import java.util.Objects;

public class TriggerNahidaMarksAction extends AbstractGameAction {
    AbstractCard card;

    public TriggerNahidaMarksAction(AbstractCard callingCard) {
        this.card = callingCard;
    }

    public void update() {
        // 遍历当前房间中的怪物
        Iterator<AbstractMonster> monsterIterator = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while (monsterIterator.hasNext()) {
            AbstractMonster monster = monsterIterator.next();
            // 遍历怪物的所有力量
            Iterator<AbstractPower> powerIterator = monster.powers.iterator();

            while (powerIterator.hasNext()) {
                AbstractPower power = powerIterator.next();
                // 只有当力量是 NahidaMarkPower时，才调用 triggerMarks 方法
                if (power instanceof NahidaMarkPower) {
                    ((NahidaMarkPower) power).triggerNahidaMarks(this.card);
                }
            }
        }

        this.isDone = true; // 动作完成
    }
}


