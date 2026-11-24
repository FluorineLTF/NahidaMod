package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;

import java.util.Iterator;

public class LeavesReturnToRootAction extends AbstractGameAction {
    public static final String UI_ID = ModHelper.makePath("LeavesReturnToRootAction");
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    private AbstractPlayer p;
    public static final String[] TEXT=ui_ID.TEXT;

    public LeavesReturnToRootAction(int amount) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }


    @Override
    public void update() {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var5 = this.p.discardPile.getPurgeableCards().group.iterator();

            while (var5.hasNext()) {
                AbstractCard c = (AbstractCard) var5.next();
                tmp.addToRandomSpot(c);
            }

            if (tmp.isEmpty()) {
                this.isDone = true;
            } else if (tmp.size() == 1) {
                card = tmp.getTopCard();
                removeCard(card);
                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
                this.tickDuration();
            }
        }else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    card = (AbstractCard)var1.next();
                    card.unhover();
                    removeCard(card);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    private void removeCard(AbstractCard card) {
//        if (this.p.masterDeck.contains(card)) {
//            this.p.masterDeck.removeCard(card.cardID);
//        }
        this.p.masterDeck.removeCard(card.cardID);
        this.p.discardPile.moveToExhaustPile(card);
        card.current_y = -200.0F * Settings.scale;
        card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
        card.target_y = (float)Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        this.addToTop(new UnlimboAction(card));
        if (!Settings.FAST_MODE) {
            this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        } else {
            this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }
    }
}