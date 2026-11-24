package nahidamod.actions;

import basemod.devcommands.power.Power;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nahidamod.powers.WisdomPower;

public class SeeThroughAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private int wiseIncrease;
    private AbstractMonster targetMonster;

    public SeeThroughAction(int wiseIncrease, AbstractMonster m) {
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.wiseIncrease = wiseIncrease;
        this.targetMonster = m;
    }

    public void update() {
        if (this.targetMonster != null && this.targetMonster.getIntentBaseDmg() >= 0) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new WisdomPower(AbstractDungeon.player, this.wiseIncrease), this.wiseIncrease));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }

        this.isDone = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("OpeningAction");
        TEXT = uiStrings.TEXT;
    }
}