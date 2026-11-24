package nahidamod.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
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

import java.util.ArrayList;
import java.util.Iterator;

public class DendroCorePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("DendroCorePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int BASE_DAMAGE = 12;
    public static final int DEFAULT_TURNS = 2;
    private static final Color DENDRO_COLOR = new Color(0.42F, 0.92F, 0.28F, 1.0F);

    private final ArrayList<Integer> coreTimers = new ArrayList<>();

    public DendroCorePower(AbstractCreature owner, int count) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;

        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");

        addCores(count);
    }

    public void addCores(int count) {
        for (int i = 0; i < count; i++) {
            coreTimers.add(DEFAULT_TURNS);
        }
        this.amount = coreTimers.size();
        this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (stackAmount > 0) {
            addCores(stackAmount);
        }
    }

    public int getCoreCount() {
        return coreTimers.size();
    }

    public int consumeAllCores() {
        int count = coreTimers.size();
        coreTimers.clear();
        this.amount = 0;
        this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        return count;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        int explodingCount = 0;
        for (int i = 0; i < coreTimers.size(); i++) {
            int next = coreTimers.get(i) - 1;
            coreTimers.set(i, next);
            if (next <= 0) {
                explodingCount++;
            }
        }

        Iterator<Integer> it = coreTimers.iterator();
        while (it.hasNext()) {
            if (it.next() <= 0) {
                it.remove();
            }
        }

        if (explodingCount > 0) {
            this.flash();
            for (int i = 0; i < explodingCount; i++) {
                this.addToBot(new SFXAction("ATTACK_MAGIC_FAST_2"));
                this.addToBot(new VFXAction(
                        new ShockWaveEffect(this.owner.hb.cX, this.owner.hb.cY, DENDRO_COLOR.cpy(), ShockWaveEffect.ShockWaveType.ADDITIVE),
                        0.15F
                ));
                this.addToBot(new DamageRandomEnemyAction(
                        new DamageInfo(this.owner, BASE_DAMAGE, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.POISON
                ));
            }
        }

        this.amount = coreTimers.size();
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            this.updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        int nextTurn = 0;
        int twoTurns = 0;
        for (Integer t : coreTimers) {
            if (t <= 1) {
                nextTurn++;
            } else {
                twoTurns++;
            }
        }
        this.description = DESCRIPTIONS[0] + coreTimers.size()
                + DESCRIPTIONS[1] + nextTurn
                + DESCRIPTIONS[2] + twoTurns
                + DESCRIPTIONS[3];
    }
}
