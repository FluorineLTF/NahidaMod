package nahidamod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;

public class WallPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath(WallPower.class.getSimpleName());
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public WallPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = Amount;

        // 添加一大一小两张能力图
        String path128 = "NahidaResources/images/powers/Example84.png";
        String path48 = "NahidaResources/images/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
//        if (damageAmount > this.amount) {
//            this.flash();
//            return damageAmount - this.amount;
//        } else {
//            damageAmount = 0 ;
//            return damageAmount;
//        }
        if ((this.owner.currentBlock >= damageAmount) && info.type != DamageInfo.DamageType.HP_LOSS){
            return damageAmount;
        }

        if (damageAmount >= this.amount){
            damageAmount -= this.amount;
        }else {
            damageAmount = 0;
        }
        this.flash();
        return damageAmount;
    }
}
