package nahidamod.modcore;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;

public abstract class NahidaCard extends CustomCard {
    public static final String UI_ID = ModHelper.makePath("NahidaCard");
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = ui_ID.TEXT;

    // 兼容旧卡字段，实际失活状态统一由 InactiveHelper / InactiveField 管理
    public int inactives;
    public boolean hasUPGRADE_DESCRIPTION;

    public NahidaCard(String id, String name, String imgPath, int cost, String description,
                      CardType type, CardColor color, CardRarity rarity, CardTarget target, int inactives) {
        super(id, name, loadCard_png(id), cost, description, type, color, rarity, target);
        this.inactives = Math.max(0, inactives);
        this.hasUPGRADE_DESCRIPTION = false;
        if (this.inactives > 0) {
            InactiveHelper.setInactive(this, this.inactives);
        }
    }

    private static String loadCard_png(String cardId) {
        String finalName = cardId.contains(":") ? cardId.split(":")[1] : cardId;
        String path = "NahidaResources/images/cards/" + finalName + ".png";
        if (Gdx.files != null && Gdx.files.internal(path).exists()) {
            return path;
        }
        return "NahidaResources/images/cards/TestPicture.png";
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }

    // 兼容旧卡调用的桥接方法
    public void updateActive() {
        this.inactives = InactiveHelper.getInactive(this);
    }

    public void changeInactive() {
        InactiveHelper.quickenCard(this);
        this.inactives = InactiveHelper.getInactive(this);
    }

    public void addInactive() {
        InactiveHelper.addInactive(this, 1);
        this.inactives = InactiveHelper.getInactive(this);
    }

    public void updateDescription() {
        CardStrings desc = CardCrawlGame.languagePack.getCardStrings(cardID);
        if (desc != null) {
            if (this.upgraded && this.hasUPGRADE_DESCRIPTION && desc.UPGRADE_DESCRIPTION != null) {
                this.rawDescription = desc.UPGRADE_DESCRIPTION;
            } else if (desc.DESCRIPTION != null) {
                this.rawDescription = desc.DESCRIPTION;
            }
        }
        this.initializeDescription();
    }
}
