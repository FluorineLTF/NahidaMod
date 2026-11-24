package nahidamod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;
import nahidamod.powers.WisdomPower;

public class Heart extends CustomRelic {
    public static final String ID = ModHelper.makePath("Heart");
    private static final String IMG_PATH = "NahidaResources/images/relics/Heart.png";

    public Heart() {
        super(ID, TextureLoader.getTexture(IMG_PATH), RelicTier.STARTER, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new WisdomPower(AbstractDungeon.player, 1),
                1
        ));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Heart();
    }
}
