package nahidamod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;

public class CandleOfLifePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("CandleOfLifePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CandleOfLifePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        this.updateDescription();
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount <= 0 || this.owner == null) {
            return damageAmount;
        }
        AbstractPower wp = this.owner.getPower(WisdomPower.POWER_ID);
        int wisdom = wp != null ? Math.max(0, wp.amount) : 0;
        if (wisdom > 0) {
            this.flash();
            return Math.max(0, damageAmount - wisdom);
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
