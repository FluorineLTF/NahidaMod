package nahidamod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class VimanaAgamAction extends AbstractGameAction {
    private ArrayList<AbstractCard> cards = new ArrayList<>();
    private int amount;

    public VimanaAgamAction(int amount) {
        this.duration = Settings.ACTION_DUR_MED;
        this.actionType = ActionType.WAIT;
        this.amount = amount;

    }
    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        this.getAllCards(p.hand);
        this.getAllCards(p.drawPile);
        this.getAllCards(p.discardPile);
        if (cards.isEmpty()){//没有可升级卡牌时标记动作完成
            this.isDone = true;
        }
        if (cards.size() <= amount) {//可升级卡牌数目小于升级数，升级全部卡牌
            for (AbstractCard c : cards) {
                c.upgrade();
                c.applyPowers();
                randomCardPosition(c);
            }
        }else {
            for (int i = 1; i <= amount ; i++) {//随机升级
                AbstractCard c ;
                c= cards.get(AbstractDungeon.miscRng.random(0, cards.size() - 1));
                c.upgrade();
                c.applyPowers();
                randomCardPosition(c);
                cards.remove(c);
            }
        }
        this.isDone = true;

    }

    private void getAllCards (CardGroup cardGroup){
        for (AbstractCard c : cardGroup.group) {
            if (c.canUpgrade()){
                cards.add(c);
            }
        }
    }

    private void randomCardPosition (AbstractCard c){
        c.target_x = MathUtils.random((float)Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
        c.target_y = MathUtils.random((float)Settings.HEIGHT * 0.8F, (float)Settings.HEIGHT * 0.2F);//创建卡牌随机位置
        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(c.target_x,c.target_y));//卡牌升级动画
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),c.target_x, c.target_y));//展示卡牌
        this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
    }
}
