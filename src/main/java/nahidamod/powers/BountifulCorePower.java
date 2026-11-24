package nahidamod.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;

public class BountifulCorePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("BountifulCorePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int BASE_DAMAGE = 18;
    private static final Color BOUNTIFUL_COLOR = new Color(0.25F, 1.0F, 0.55F, 1.0F);

    public BountifulCorePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        this.type = PowerType.BUFF;
        this.canGoNegative = false;

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
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || this.amount <= 0) {
            return;
        }
        this.flash();
        for (int i = 0; i < this.amount; i++) {
            this.addToBot(new SFXAction("ATTACK_MAGIC_FAST_1"));
            this.addToBot(new VFXAction(
                    new ShockWaveEffect(this.owner.hb.cX, this.owner.hb.cY, BOUNTIFUL_COLOR.cpy(), ShockWaveEffect.ShockWaveType.CHAOTIC),
                    0.2F
            ));
            this.addToBot(new DamageAllEnemiesAction(
                    this.owner,
                    DamageInfo.createDamageMatrix(BASE_DAMAGE, true),
                    DamageInfo.DamageType.THORNS,
                    AbstractGameAction.AttackEffect.POISON
            ));
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
