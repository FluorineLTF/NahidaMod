package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;

import java.util.ArrayList;

public class QuickenDiscardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("QuickenDiscardUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"选择弃牌堆中的牌进行激化。"};

    private final AbstractPlayer p;

    public QuickenDiscardAction(int amount) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p == null || this.p.discardPile.isEmpty() || this.amount <= 0) {
                this.isDone = true;
                return;
            }
            if (this.p.discardPile.size() <= this.amount) {
                ArrayList<AbstractCard> snapshot = new ArrayList<>(this.p.discardPile.group);
                for (AbstractCard c : snapshot) {
                    InactiveHelper.quickenCard(c);
                }
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(this.p.discardPile, this.amount, TEXT[0], false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            ArrayList<AbstractCard> selected = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            for (AbstractCard c : selected) {
                c.unhover();
                InactiveHelper.quickenCard(c);
            }
            this.isDone = true;
        }
        this.tickDuration();
    }
}
