package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;

public class MakeHandCardEtherealAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("EtherealUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"令其获得虚无", " NL 虚无 。"};

    private final AbstractPlayer p;

    public MakeHandCardEtherealAction(int amount) {
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
                for (AbstractCard c : this.p.hand.group) {
                    applyEthereal(c);
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
                applyEthereal(c);
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

    private void applyEthereal(AbstractCard c) {
        if (!c.isEthereal) {
            c.isEthereal = true;
            c.rawDescription = c.rawDescription + TEXT[1];
            c.initializeDescription();
        }
        c.superFlash();
    }
}
