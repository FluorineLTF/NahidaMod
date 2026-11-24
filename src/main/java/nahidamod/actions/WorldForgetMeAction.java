package nahidamod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.purple.InnerPeace;
import com.megacrit.cardcrawl.cards.purple.MentalFortress;
import com.megacrit.cardcrawl.cards.purple.Rushdown;
import com.megacrit.cardcrawl.cards.purple.Tantrum;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;

import java.util.ArrayList;

public class WorldForgetMeAction extends AbstractGameAction {
    private AbstractPlayer p;
    private CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private ArrayList<AbstractCard> cards = new ArrayList<>();

    public WorldForgetMeAction() {
        this.p = AbstractDungeon.player;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }
    @Override
    public void update() {

        if (!p.discardPile.isEmpty()) {
            group = p.discardPile;
            Ex();
        }

        if (!p.drawPile.isEmpty()) {
            group = p.drawPile;
            Ex();
        }

        if (!p.hand.isEmpty()) {
            ArrayList<AbstractCard> tmp = new ArrayList<>();
            for(int i = 0; i < p.hand.size(); ++i) {
                AbstractCard c = p.hand.group.get(i);
                if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) {
                    tmp.add(c);
                }
            }
            if (!tmp.isEmpty()){
                for (AbstractCard c : tmp) {
                    this.p.hand.moveToExhaustPile(c);
                }
            }
        }

        this.tickDuration();
        this.isDone = true;

    }

    private void Exhaust (AbstractCard c) {
            c.target_x = MathUtils.random((float) Settings.WIDTH * 0.1F, (float) Settings.WIDTH * 0.9F);
            c.target_y = MathUtils.random((float) Settings.HEIGHT * 0.8F, (float) Settings.HEIGHT * 0.2F);//创建卡牌随机位置
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), c.target_x, c.target_y));//展示卡牌
            group.moveToExhaustPile(c);
    }

    private void Ex () {
        for (AbstractCard c : group.group) {
            if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) {
                cards.add(c);
            }
        }
        for (int i = cards.size() - 1; i >= 0; i--) {
            Exhaust(cards.get(i));
            this.tickDuration();
        }
        cards.clear();
    }
}
