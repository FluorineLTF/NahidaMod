package nahidamod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.MarkOfPain;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import nahidamod.card.Uncommon.Kusava;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.ArrayList;

public class JinnAction extends AbstractGameAction {
    public static final String UI_ID = ModHelper.makePath(JinnAction.class.getSimpleName());
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT=ui_ID.TEXT;
    private AbstractPlayer p;
    private int amount;

    public JinnAction(int amount) {
        this.amount = amount;
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration;
    }
//    @Override
//    public void update() {
//        NahidaCard card;
//        if (this.duration == this.startDuration) {
//            ArrayList<NahidaCard> cards = new ArrayList<>();
//            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
//                if (c instanceof NahidaCard) {
//                    cards.add((NahidaCard) c);
//                }
//            }
//            if (cards.isEmpty()){
//                this.isDone = true;
//                return;
//            }
//            if (cards.size() == 1){
//                inactivatedCards(cards.get(0));
//                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));//对话说明，看情况加
//                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(cards.get(0).makeStatEquivalentCopy(),(float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));//展示卡牌
//                this.isDone = true;
//                return;
//            }
//            NahidaCard[] c = new NahidaCard[2];
//            for (int i = 0; i < amount ; i++) {//随机失活
//                c[i] = cards.get(AbstractDungeon.miscRng.random(0, cards.size() - 1));
//                inactivatedCards(c[i]);
//                cards.remove(c[i]);
//            }
//            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[1], true));//对话说明，看情况加
//            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c[0].makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
//            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c[1].makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
//            this.duration -= Gdx.graphics.getDeltaTime();
//
//        }
//        this.tickDuration();
//
//    }

//    private void inactivatedCards (NahidaCard card) {
//        if (card.inactives == -1){
//            card.inactives = 2;
//        }else {
//            card.inactives += 2;
//        }
//        card.updateActive();
//        card.updateDescription();
//    }

    //优化后代码
    private void inactivatedCards (NahidaCard card) {
        if (card.inactives == -1){
            card.inactives = 2;
        }else {
            card.inactives += 2;
        }
        card.updateActive();
        card.updateDescription();
        p.drawPile.removeCard(card);
        this.addToBot(new MakeTempCardInDrawPileAction(card,1,true,true));
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            ArrayList<NahidaCard> cards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c instanceof NahidaCard) {
                    cards.add((NahidaCard) c);
                }
            }
            if (cards.isEmpty()){
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[2], true));//对话说明，看情况加
                this.isDone = true;
                return;
            }
            if (cards.size() <= amount){
                for (NahidaCard c : cards){
                    inactivatedCards(c);
                }
                this.isDone = true;
                return;
            }
            for (int i = 0; i < amount ; i++) {//随机失活
                NahidaCard c = cards.get(AbstractDungeon.miscRng.random(0, cards.size() - 1));
                inactivatedCards(c);
                cards.remove(c);
            }

            this.duration -= Gdx.graphics.getDeltaTime();

        }
        this.tickDuration();

    }
}
