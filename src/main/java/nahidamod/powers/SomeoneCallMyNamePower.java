package nahidamod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.card.Special.SurasthanaFantasia;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

public class SomeoneCallMyNamePower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath(SomeoneCallMyNamePower.class.getSimpleName());
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private NahidaCard card;

    public SomeoneCallMyNamePower(AbstractCreature owner,int amount, NahidaCard card) {
        this.name = NAME;
        if (card.upgraded){
            this.ID = POWER_ID +"1";
        }else {
            this.ID = POWER_ID +"0";
        }
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.card = card ;

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
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        if (card.upgraded) {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[2];
        }else {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
        }
    }
    public void atStartOfTurn() {
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            return;
        }
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            this.addToBot(new MakeTempCardInHandAction(card, this.amount, false));
        }

    }
}
