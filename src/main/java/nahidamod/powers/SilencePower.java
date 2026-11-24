package nahidamod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;

public class SilencePower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath(SilencePower.class.getSimpleName());
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SilencePower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount = Amount;
        this.canGoNegative = false;

        // 添加一大一小两张能力图
        String path128 = "NahidaResources/images/powers/Example84.png";
        String path48 = "NahidaResources/images/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "NahidaMod:SilencePower"));
        }
    }
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "NahidaMod:SilencePower"));
        }
    }
    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public boolean canPlayCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.POWER) {
            card.cantUseMessage = "我现在不能打出 #b能力牌 ！";
            return false;
        }
        return super.canPlayCard(card);
    }
}
