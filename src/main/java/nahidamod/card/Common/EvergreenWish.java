package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ForgetHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.NextBattleManager;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class EvergreenWish extends NahidaCard {
    public static final String ID = ModHelper.makePath("EvergreenWish");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/EvergreenWish.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public EvergreenWish() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.SELF, 0);
        this.exhaust = true;
        this.hasUPGRADE_DESCRIPTION = true;
        this.cardsToPreview = new Miracle();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            AbstractCard preview = new Miracle();
            preview.upgrade();
            this.cardsToPreview = preview;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard miracle = new Miracle();
        if (this.upgraded) {
            miracle.upgrade();
        }
        this.addToBot(new MakeTempCardInDrawPileAction(miracle, 1, false, true));
        NextBattleManager.INSTANCE.addNextBattleEnergy(1);
        ForgetHelper.markCardForForget(this);
    }
}
