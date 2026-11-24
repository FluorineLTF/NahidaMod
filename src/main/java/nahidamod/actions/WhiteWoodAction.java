package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.FollowUp;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;

import java.util.ArrayList;

public class WhiteWoodAction extends AbstractGameAction {
    public static final String UI_ID = ModHelper.makePath(WhiteWoodAction.class.getSimpleName());
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT=ui_ID.TEXT;
    private int amount;
    private AbstractPlayer p;
    private ArrayList<AbstractCard> cards = new ArrayList<>();

    public WhiteWoodAction(int amount) {
        this.amount = amount;
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }


    @Override
    public void update() {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            cards.clear();
            if (AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()){
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, TEXT[0], 2.4F, 2.4F));//对话说明，看情况加
                this.isDone = true;
                return;
            }
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
                cards.add(c);
            }
            this.tickDuration();
        } else {
            for (int i = 0 , j = cards.size() - 1; i < amount; j--) {
                if (p.hand.size() >= 10){
                    this.p.createHandIsFullDialog();
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true, TEXT[1], 2.4F, 2.4F));//对话说明，看情况加，但是在游戏中没有显示，疑似出bug
                    this.isDone = true;
                    return;
                }
                card = cards.get(j);//获取集合中第j张卡牌
                if (p.drawPile.contains(card)){   //判断卡牌是否符合要求
                    this.addToBot(new DrawCardToHandAction(card));
                    i++;
                }else if (p.discardPile.contains(card)){
                    this.addToBot(new DiscardToHandAction(card));
                    i++;
                }

                if (j == 0){//cards里的所有卡牌都已经遍历完
                    this.isDone = true;
                    return;
                }
            }
            this.tickDuration();
        }
    }

}