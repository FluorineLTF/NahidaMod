package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.actions.QuickenHandAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class RapidMetabolism extends NahidaCard {
    public static final String ID = ModHelper.makePath("RapidMetabolism");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/RapidMetabolism.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public RapidMetabolism() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.SELF, 0);
        this.magicNumber = this.baseMagicNumber = 3;
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
                    this.addToTop(new QuickenHandAction(true));
                    this.addToTop(new DrawCardAction(p, RapidMetabolism.this.magicNumber));
                }
                this.isDone = true;
            }
        });
    }
}
