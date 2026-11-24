package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.card.Wish.Memory;
import nahidamod.card.Wish.Surprise;
import nahidamod.card.Wish.Sweet;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.ArrayList;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Wish extends NahidaCard {
    public static final String ID = ModHelper.makePath("Wish");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Wish.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Wish() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.RARE, CardTarget.SELF, 0);
        this.exhaust = true;
        this.hasUPGRADE_DESCRIPTION = true;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new Surprise());
        choices.add(new Sweet());
        choices.add(new Memory());
        if (this.upgraded) {
            for (AbstractCard c : choices) {
                c.upgrade();
            }
        }
        this.addToBot(new ChooseOneAction(choices));
    }
}
