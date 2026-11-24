package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.card.Special.BorderSandBarrier;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class SanctuaryOfExiles extends NahidaCard {
    public static final String ID = ModHelper.makePath(SanctuaryOfExiles.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public SanctuaryOfExiles() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.hasUPGRADE_DESCRIPTION = true;
        this.cardsToPreview = new BorderSandBarrier();
    }


    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.updateDescription();
            this.cardsToPreview.upgrade();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        NahidaCard card = new BorderSandBarrier();
        if (this.upgraded) {
            card.upgrade();
        }
        this.addToBot(new MakeTempCardInHandAction(card,2,false));
    }
}