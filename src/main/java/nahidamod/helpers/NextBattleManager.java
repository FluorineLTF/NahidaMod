package nahidamod.helpers;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import nahidamod.powers.WisdomPower;

import java.util.ArrayList;
import java.util.UUID;

public class NextBattleManager implements CustomSavable<NextBattleManager.SaveData> {
    public static final NextBattleManager INSTANCE = new NextBattleManager();

    public static class SaveData {
        public int energy = 0;
        public int draw = 0;
        public int wisdom = 0;
        public ArrayList<String> innateUuids = new ArrayList<>();
    }

    private SaveData data = new SaveData();

    public void addNextBattleEnergy(int amount) {
        if (amount > 0) {
            data.energy += amount;
        }
    }

    public void addNextBattleDraw(int amount) {
        if (amount > 0) {
            data.draw += amount;
        }
    }

    public void addNextBattleWisdom(int amount) {
        if (amount > 0) {
            data.wisdom += amount;
        }
    }

    public void addNextBattleInnate(UUID cardUuid) {
        if (cardUuid != null) {
            String key = cardUuid.toString();
            if (!data.innateUuids.contains(key)) {
                data.innateUuids.add(key);
            }
        }
    }

    public void applyAndClear() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            return;
        }

        if (data.energy > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(data.energy));
        }
        if (data.draw > 0) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, data.draw));
        }
        if (data.wisdom > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new WisdomPower(p, data.wisdom), data.wisdom)
            );
        }
        if (data.innateUuids != null && !data.innateUuids.isEmpty() && p.drawPile != null) {
            ArrayList<AbstractCard> toTop = new ArrayList<>();
            for (AbstractCard c : p.drawPile.group) {
                if (data.innateUuids.contains(c.uuid.toString())) {
                    c.isInnate = true;
                    toTop.add(c);
                }
            }
            for (AbstractCard c : toTop) {
                p.drawPile.removeCard(c);
                p.drawPile.addToTop(c);
            }
        }

        reset();
    }

    public void reset() {
        data = new SaveData();
    }

    @Override
    public SaveData onSave() {
        return data;
    }

    @Override
    public void onLoad(SaveData loaded) {
        if (loaded == null) {
            reset();
        } else {
            this.data = loaded;
            if (this.data.innateUuids == null) {
                this.data.innateUuids = new ArrayList<>();
            }
        }
    }
}
