package nahidamod.card.Wish;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

public class Sweet extends NahidaCard {
    public static final String ID = ModHelper.makePath("Sweet");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Sweet.png";
    private static final int COST = -2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Sweet() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE, 0);
        this.magicNumber = this.baseMagicNumber = 15;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(5);
        }
    }

    @Override
    public void onChoseThisOption() {
        if (AbstractDungeon.player != null) {
            int healAmt = Math.max(1, Math.round(AbstractDungeon.player.maxHealth * (this.magicNumber / 100.0F)));
            this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, healAmt));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }
}
