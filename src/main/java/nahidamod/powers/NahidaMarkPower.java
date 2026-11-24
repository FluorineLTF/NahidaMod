package nahidamod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;

public class NahidaMarkPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("NahidaMarkPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public NahidaMarkPower(AbstractCreature owner, int amt) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amt;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.loadRegion("pressure_points");
        this.updateDescription();

        if (this.owner == AbstractDungeon.player || this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        this.updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        this.updateDescription();
    }

    @Override
    public void atEndOfRound() {
        if (this.amount <= 1) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    public void triggerFixedLoss(int hpLoss) {
        if (hpLoss > 0 && this.owner != null && !this.owner.isDeadOrEscaped()) {
            this.flash();
            this.addToBot(new LoseHPAction(this.owner, AbstractDungeon.player, hpLoss, AbstractGameAction.AttackEffect.POISON));
        }
    }

    // 兼容旧代码调用
    public void triggerNahidaMarks(AbstractCard card) {
        triggerFixedLoss(card != null && card.upgraded ? 7 : 5);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
