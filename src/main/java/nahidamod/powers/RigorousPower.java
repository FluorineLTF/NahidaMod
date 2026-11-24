package nahidamod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;

public class RigorousPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("RigorousPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean damaged = false;

    public RigorousPower(AbstractCreature owner, int blockAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = blockAmount;
        this.type = PowerType.BUFF;
        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.updateDescription();
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && !this.damaged) {
            this.damaged = true;
            this.flash();
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        if (!this.damaged && this.amount > 0) {
            this.flash();
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
