package nahidamod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;

public class ReducePower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath(ReducePower.class.getSimpleName());
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ReducePower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount = Amount;
        this.loadRegion("lessdraw");

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        if (this.amount - reduceAmount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            return;
        }
        this.amount -= reduceAmount;
        AbstractDungeon.player.gameHandSize += reduceAmount;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        AbstractDungeon.player.gameHandSize -= stackAmount;
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }
    public void onRemove() {
        if (this.amount > 0) {
            AbstractDungeon.player.gameHandSize += this.amount;
        }
    }
    public void onInitialApplication() {
//        --AbstractDungeon.player.gameHandSize;
        if (this.amount <= 0) {
            AbstractDungeon.player.gameHandSize -= this.amount;
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            return;
        }
        AbstractDungeon.player.gameHandSize -= this.amount;
    }

    public void atStartOfTurnPostDraw() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

}