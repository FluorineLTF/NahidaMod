package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class WeavingDreams extends NahidaCard {
    public static final String ID = ModHelper.makePath("WeavingDreams");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/WeavingDreams.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public WeavingDreams() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.ALL_ENEMY, 0);
        this.magicNumber = this.baseMagicNumber = 2;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false), this.magicNumber));
            }
        }
    }
}
