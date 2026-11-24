package nahidamod.helpers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import nahidamod.interfaces.CoreModifierListener;
import nahidamod.powers.BountifulCorePower;
import nahidamod.powers.DendroCorePower;

public class DendroCoreHelper {
    private static final Color SPAWN_COLOR = new Color(0.55F, 0.98F, 0.45F, 1.0F);

    public static void spawnDendroCore(int baseCount) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || baseCount <= 0) {
            return;
        }

        int finalCount = baseCount;
        boolean convertToBountiful = false;
        for (AbstractPower power : p.powers) {
            if (power instanceof CoreModifierListener) {
                CoreModifierListener listener = (CoreModifierListener) power;
                finalCount = listener.modifyDendroCoreSpawnCount(finalCount);
                if (listener.shouldConvertToBountifulCore()) {
                    convertToBountiful = true;
                }
            }
        }

        if (finalCount <= 0) {
            return;
        }

        AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_PLASMA_CHANNEL"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(
                new ShockWaveEffect(p.hb.cX, p.hb.cY, SPAWN_COLOR.cpy(), ShockWaveEffect.ShockWaveType.NORMAL),
                0.12F
        ));

        if (convertToBountiful) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new BountifulCorePower(p, finalCount), finalCount)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new DendroCorePower(p, finalCount), finalCount)
            );
        }
    }

    public static void spawnBountifulCore(int count) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || count <= 0) {
            return;
        }
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_PLASMA_CHANNEL"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(
                new ShockWaveEffect(p.hb.cX, p.hb.cY, SPAWN_COLOR.cpy(), ShockWaveEffect.ShockWaveType.NORMAL),
                0.12F
        ));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new BountifulCorePower(p, count), count)
        );
    }

    public static int getDendroCoreCount() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            return 0;
        }
        AbstractPower power = p.getPower(DendroCorePower.POWER_ID);
        if (power instanceof DendroCorePower) {
            return ((DendroCorePower) power).getCoreCount();
        }
        return 0;
    }

    public static int consumeAllDendroCores() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            return 0;
        }
        AbstractPower power = p.getPower(DendroCorePower.POWER_ID);
        if (power instanceof DendroCorePower) {
            return ((DendroCorePower) power).consumeAllCores();
        }
        return 0;
    }
}
