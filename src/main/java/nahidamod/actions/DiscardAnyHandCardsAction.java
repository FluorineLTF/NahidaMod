package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;
import nahidamod.powers.InspirationPower;

public class DiscardAnyHandCardsAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("DiscardAnyUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"弃置任意张手牌"};

    private final AbstractPlayer p;
    private final int inspirationPerCard;

    public DiscardAnyHandCardsAction(int inspirationPerCard) {
        this.p = AbstractDungeon.player;
        this.inspirationPerCard = inspirationPerCard;
        this.actionType = ActionType.DISCARD;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p == null || this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            int count = AbstractDungeon.handCardSelectScreen.selectedCards.size();
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.p.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
            }
            if (count > 0 && this.inspirationPerCard > 0) {
                int totalGain = count * this.inspirationPerCard;
                this.addToTop(new ApplyPowerAction(this.p, this.p, new InspirationPower(this.p, totalGain), totalGain));
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
