package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;

import java.util.Iterator;

public class ReturnCardToDrawPileAction extends AbstractGameAction {
    public static final String UI_ID = ModHelper.makePath(ReturnCardToDrawPileAction.class.getSimpleName());
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    private AbstractPlayer p;
    public static final String[] TEXT=ui_ID.TEXT;
    private CardGroup cardGroup ;
    private boolean isFree;
    private boolean isHand;
    private boolean canPickZero;
    private boolean isSmilesAndTears;
    private boolean isTop;
    private int amount;

    public ReturnCardToDrawPileAction(int amount, boolean isFree, boolean isHand , boolean canPickZero) {
        this.amount = amount;
        this.isFree = isFree;
        this.isHand = isHand;
        this.isSmilesAndTears = false;
        this.isTop = false;
        this.canPickZero = canPickZero;
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public ReturnCardToDrawPileAction (int amount){
        this(amount, false ,true ,true);
    }

    public ReturnCardToDrawPileAction (int amount ,boolean isHand){
        this(amount, false ,isHand ,true);
    }

    public ReturnCardToDrawPileAction (int amount , boolean isTop , boolean canPickZero){
        this(amount, false ,true ,canPickZero);
        this.isTop = isTop;
    }

    public ReturnCardToDrawPileAction (boolean isSmilesAndTears){
        this(999, false ,true ,true);
        this.isSmilesAndTears = isSmilesAndTears;
    }



    @Override
    public void update() {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (this.isHand){//判断需要返回的牌是手牌还是弃牌堆
                cardGroup = this.p.hand;
            }else {
                cardGroup = this.p.discardPile;
            }
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var5 = cardGroup.group.iterator();

            if (this.isFree){//判断是否需要返回0费
                while (var5.hasNext()) {
                    AbstractCard c = (AbstractCard) var5.next();
                    if (c.cost == 0){
                        tmp.addToTop(c);
                    }
                }
            }else if (this.isSmilesAndTears) {
                while (var5.hasNext()) {
                    AbstractCard c = (AbstractCard) var5.next();
                    if (!c.selfRetain){
                        tmp.addToTop(c);
                    }
                }
            }else {
                while (var5.hasNext()) {
                    AbstractCard c = (AbstractCard) var5.next();
                    tmp.addToTop(c);
                }
            }

            if (tmp.isEmpty()) {
                this.isDone = true;
            } else if ((tmp.size() <= this.amount && !this.isHand) || (this.amount == 999 && this.isHand)) {//返回弃牌堆的牌且需要返回的牌大于弃牌堆数目，则直接执行动作
                for (int i = tmp.size(); i > 0; --i) {
                    AbstractCard c = tmp.getTopCard();
                    moveCard(c);
                    tmp.removeCard(c);
                }
                this.isDone = true;
            } else {
                if (this.isHand){
                    AbstractDungeon.handCardSelectScreen.open(TEXT[2], this.amount, false, this.canPickZero, false, false, true);
                }else {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0] + this.amount + TEXT[1], false);
                }
                this.tickDuration();
            }
        }else {
            if (isHand && !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved){
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group){
                    moveCard(c);
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }else if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0){
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    card = (AbstractCard)var1.next();
                    card.unhover();
                    moveCard(card);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }


    private void moveCard (AbstractCard card) {
        if (this.isHand){
            this.p.hand.moveToDeck(card,!this.isTop);
        }else {
            this.p.discardPile.moveToDeck(card,!this.isTop);
        }
    }
}