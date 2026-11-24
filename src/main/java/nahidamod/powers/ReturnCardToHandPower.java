package nahidamod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ReboundPower;
import com.megacrit.cardcrawl.relics.BlueCandle;
import nahidamod.actions.DrawCardToHandAction;
import nahidamod.actions.ReturnCardToDrawPileAction;
import nahidamod.helpers.ModHelper;

public class ReturnCardToHandPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath(ReturnCardToHandPower.class.getSimpleName());
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean justEvoked = true;

    public ReturnCardToHandPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = amount;

        // 添加一大一小两张能力图
        String path128 = "NahidaResources/images/powers/Example84.png";
        String path48 = "NahidaResources/images/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        // 首次添加能力更新描述
        this.updateDescription();

        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (this.justEvoked) {
            this.justEvoked = false;
        }else if (!card.exhaust || card.type != AbstractCard.CardType.POWER){
//            if (AbstractDungeon.player.drawPile.contains(card)){
//                this.addToBot(new DrawCardToHandAction(card));
//                Power(action);
//            }else if (AbstractDungeon.player.discardPile.contains(card)){
//                this.addToBot(new DiscardToHandAction(card));
//                Power(action);
//            }
            if (card.shuffleBackIntoDrawPile){
                //将卡牌从抽牌堆移回手牌
                this.addToBot(new DrawCardToHandAction(card));
            }else {
                // 将卡牌从弃牌堆移回手牌
                this.addToBot(new DiscardToHandAction(card));
            }
            Power(action);
        }
    }

    private void Power (UseCardAction action) {
        this.flash();
        action.returnToHand = true;
        this.addToBot(new ReducePowerAction(this.owner, this.owner, "NahidaMod:ReturnCardToHandPower", 1));
    }
}
