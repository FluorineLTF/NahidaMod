package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.LeavesAndPetalsPower;

public class RechargeAction extends AbstractGameAction {
    private NahidaCard card;
    private boolean hasPower;

    public RechargeAction(NahidaCard card) {
        this.card = card;
        this.hasPower = false;
    }

    @Override
    public void update() {
        //遍历玩家的能力
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof LeavesAndPetalsPower){
                hasPower = true;//有树与花的旋舞返回true
            }
        }

        //如果你想充能卡返回抽牌堆，注释以下代码

//        if (card.shuffleBackIntoDrawPile){
//            this.card.shuffleBackIntoDrawPile = false;
//        }

        if (hasPower && card.shuffleBackIntoDrawPile){
            //将卡牌从抽牌堆移回手牌
//            this.addToBot(new DrawCardToHandAction(this.card));
        }else {
            // 将卡牌从弃牌堆移回手牌
            this.addToBot(new DiscardToHandAction(this.card));
        }



        // 更新卡牌的 inactives、active 和描述
        this.card.inactives = 1;         // 设置卡牌的 inactives 为 1
        this.card.updateActive();        // 更新 active 状态
        this.card.updateDescription();   // 更新卡牌的描述
        this.card.triggerOnGlowCheck();

        this.isDone = true; // 标记动作已完成
    }
}

