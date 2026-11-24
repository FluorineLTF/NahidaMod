package nahidamod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sundial;
import nahidamod.helpers.ModHelper;
import nahidamod.powers.WisdomPower;

public class Heart extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(Heart.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "NahidaResources/images/relics/Heart.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public Heart() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordProper("nahidamod:智慧")), (String) GameDictionary.keywords.get("nahidamod:智慧")));//加载关键字
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Heart();
    }

    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        counter++;
        if (counter == 4){
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,new WisdomPower(AbstractDungeon.player,1),1));
            counter = 0;
        }
    }
}