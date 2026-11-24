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

public class QuickenHandAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("QuickenUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"激化"};

    private final AbstractPlayer p;
    private final boolean allHand;

    public QuickenHandAction(int amount) {
        this(amount, false);
    }

    public QuickenHandAction(boolean allHand) {
        this(0, allHand);
    }

    private QuickenHandAction(int amount, boolean allHand) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.allHand = allHand;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p == null || this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (this.allHand || this.p.hand.size() <= this.amount) {
                ArrayList<AbstractCard> snapshot = new ArrayList<>(this.p.hand.group);
                for (AbstractCard c : snapshot) {
                    InactiveHelper.quickenCard(c);
                }
                this.isDone = true;
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, false, false, false, false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                InactiveHelper.quickenCard(c);
                this.p.hand.addToTop(c);
            }
            this.p.hand.refreshHandLayout();
            this.p.hand.glowCheck();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        this.tickDuration();
    }
}
