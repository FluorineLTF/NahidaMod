package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.card.Special.BorderSandBarrier;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class SanctuaryOfExiles extends NahidaCard {
    public static final String ID = ModHelper.makePath("SanctuaryOfExiles");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/SanctuaryOfExiles.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public SanctuaryOfExiles() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.SELF, 0);
        this.magicNumber = this.baseMagicNumber = 2;
        this.hasUPGRADE_DESCRIPTION = true;
        this.cardsToPreview = new BorderSandBarrier();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            AbstractCard preview = new BorderSandBarrier();
            preview.upgrade();
            this.cardsToPreview = preview;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final boolean isUpgraded = this.upgraded;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower wp = p.getPower(WisdomPower.POWER_ID);
                if (wp != null && wp.amount >= 1) {
                    wp.reducePower(1);
                    wp.updateDescription();
                    if (wp.amount <= 0) {
                        p.powers.remove(wp);
                    }
                    AbstractCard barrier = new BorderSandBarrier();
                    if (isUpgraded) {
                        barrier.upgrade();
                    }
                    this.addToTop(new MakeTempCardInHandAction(barrier, SanctuaryOfExiles.this.magicNumber));
                }
                this.isDone = true;
            }
        });
    }
}
