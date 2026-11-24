package nahidamod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.card.Special.SurasthanaFantasia;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;

public class SomeoneCallMyNamePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("SomeoneCallMyNamePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SomeoneCallMyNamePower(AbstractCreature owner, int inactiveAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, inactiveAmount);
        this.type = PowerType.BUFF;
        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = Math.min(this.amount, Math.max(0, stackAmount));
        this.updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        AbstractCard song = new SurasthanaFantasia();
        if (this.amount > 0) {
            InactiveHelper.setInactive(song, this.amount);
        }
        this.addToBot(new MakeTempCardInHandAction(song, 1));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
