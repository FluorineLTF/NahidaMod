package nahidamod.helpers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.interfaces.OnAnyCardQuickenedListener;
import nahidamod.interfaces.OnQuickenCard;
import nahidamod.patches.InactiveField;
import nahidamod.patches.QuickenAttackBonusPatch;

import java.util.ArrayList;

public class InactiveHelper {

    public static int getInactive(AbstractCard card) {
        if (card == null) {
            return 0;
        }
        Integer val = InactiveField.inactives.get(card);
        return val == null ? 0 : val;
    }

    public static void setInactive(AbstractCard card, int amount) {
        if (card == null) {
            return;
        }
        InactiveField.inactives.set(card, Math.max(0, amount));
        card.initializeDescription();
        resetGlowIfActive(card);
        refreshHandGlow();
    }

    public static void addInactive(AbstractCard card, int amount) {
        if (card == null || amount <= 0) {
            return;
        }
        int current = getInactive(card);
        InactiveField.inactives.set(card, current + amount);
        card.superFlash(Color.RED.cpy());
        card.initializeDescription();
        refreshHandGlow();
    }

    public static void reduceInactiveByDraw(AbstractCard card) {
        int current = getInactive(card);
        if (current > 0) {
            InactiveField.inactives.set(card, current - 1);
            card.superFlash();
            card.initializeDescription();
            resetGlowIfActive(card);
            refreshHandGlow();
        }
    }

    public static boolean quickenCard(AbstractCard card) {
        if (card == null) {
            return false;
        }
        boolean triggered = false;
        int current = getInactive(card);
        if (current > 0) {
            InactiveField.inactives.set(card, current - 1);
            triggered = true;
        }
        if (card instanceof OnQuickenCard) {
            ((OnQuickenCard) card).onQuicken();
            triggered = true;
        }
        if (card.type == AbstractCard.CardType.ATTACK) {
            QuickenAttackBonusPatch.addBonus(card, 1);
            card.applyPowers();
            card.superFlash();
            triggered = true;
        }

        if (triggered) {
            card.superFlash(Color.LIME.cpy());
            card.initializeDescription();
            resetGlowIfActive(card);
            notifyQuickenListeners(card);
            refreshHandGlow();
        }
        return triggered;
    }

    private static void notifyQuickenListeners(AbstractCard quickenedCard) {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof OnAnyCardQuickenedListener) {
                ((OnAnyCardQuickenedListener) power).onAnyCardQuickened(quickenedCard);
            }
        }
        ArrayList<AbstractCard> discardSnapshot = new ArrayList<>(AbstractDungeon.player.discardPile.group);
        for (AbstractCard c : discardSnapshot) {
            if (c instanceof OnAnyCardQuickenedListener) {
                ((OnAnyCardQuickenedListener) c).onAnyCardQuickened(quickenedCard);
            }
        }
    }

    private static void resetGlowIfActive(AbstractCard card) {
        if (card != null && getInactive(card) <= 0
                && card.glowColor != null
                && card.glowColor.equals(nahidamod.patches.InactivePatch.INACTIVE_GLOW_COLOR)) {
            card.glowColor = nahidamod.patches.InactivePatch.DEFAULT_GLOW_COLOR.cpy();
        }
    }

    private static void refreshHandGlow() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hand != null) {
            AbstractDungeon.player.hand.glowCheck();
        }
    }
}
