package nahidamod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FossilizedHelix;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nahidamod.actions.ActedAction;
import nahidamod.helpers.ModHelper;

public class ApotheosisOfLuminescence extends CustomRelic implements ClickableRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(ApotheosisOfLuminescence.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "NahidaResources/images/relics/ApotheosisOfLuminescence.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public ApotheosisOfLuminescence() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordProper("nahidamod:激化")), (String) GameDictionary.keywords.get("nahidamod:激化")));//加载关键字
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new ApotheosisOfLuminescence();
    }

    @Override
    public void onRightClick() {
        if (!this.grayscale && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.flash();
            this.addToBot(new ActedAction(999));
            this.grayscale = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

}