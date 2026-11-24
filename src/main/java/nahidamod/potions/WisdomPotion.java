package nahidamod.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nahidamod.helpers.ModHelper;
import nahidamod.powers.WisdomPower;

public class WisdomPotion extends CustomPotion {
    public static final String POTION_ID = ModHelper.makePath(WisdomPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public WisdomPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.BOTTLE, PotionColor.ANCIENT);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = new Color(0.5f, 1.0f, 0.5f, 1.0F);//药水边框颜色
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractCreature target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new WisdomPower(target, this.potency), this.potency));
        }
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));//不知道有什么用
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordProper("nahidamod:智慧")), (String) GameDictionary.keywords.get("nahidamod:智慧")));//加载关键字
    }

    @Override
    public int getPotency(int i) {
        return 3;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new WisdomPotion();
    }

}
