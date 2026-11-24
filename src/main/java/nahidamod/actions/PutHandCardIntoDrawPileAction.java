package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;

public class PutHandCardIntoDrawPileAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("ReturnCardToDrawPileAction"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"选择", "张牌。", "将其返回抽牌堆。"};

    private final AbstractPlayer p;

    public PutHandCardIntoDrawPileAction(int amount) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
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
            if (this.p.hand.size() <= this.amount) {
                while (!this.p.hand.isEmpty()) {
                    AbstractCard c = this.p.hand.getTopCard();
                    this.p.hand.moveToDeck(c, true);
                }
                this.isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[2], this.amount, false, false, false, false, false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.p.hand.moveToDeck(c, true);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.p.hand.refreshHandLayout();
            this.isDone = true;
        }
        this.tickDuration();
    }
}
