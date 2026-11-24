package nahidamod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.*;
import nahidamod.helpers.ModHelper;

public class BlossomingWhiteBranch extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(BlossomingWhiteBranch.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "NahidaResources/images/relics/TestRelic.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public BlossomingWhiteBranch() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);

    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BlossomingWhiteBranch();
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        this.flash();
        AbstractDungeon.player.increaseMaxHp(2, false);
    }
}