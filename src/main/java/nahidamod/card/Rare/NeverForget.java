package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.ArrayList;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class NeverForget extends NahidaCard {
    public static final String ID = ModHelper.makePath("NeverForget");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/NeverForget.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public NeverForget() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.RARE, CardTarget.SELF, 0);
        this.exhaust = true;
        this.hasUPGRADE_DESCRIPTION = true;
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
        final boolean upgradeAll = this.upgraded;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractCard> snapshot = new ArrayList<>(p.exhaustPile.group);
                for (AbstractCard c : snapshot) {
                    if (c == NeverForget.this
                            || c.type == CardType.STATUS
                            || c.type == CardType.CURSE
                            || c.cost == -2) {
                        continue;
                    }
                    AbstractCard copy = c.makeStatEquivalentCopy();
                    if (upgradeAll && copy.canUpgrade()) {
                        copy.upgrade();
                    }
                    copy.purgeOnUse = true;
                    copy.freeToPlayOnce = true;
                    AbstractMonster target = AbstractDungeon.getRandomMonster();
                    AbstractDungeon.actionManager.addCardQueueItem(
                            new CardQueueItem(copy, target, copy.energyOnUse, true, true),
                            true
                    );
                }
                this.isDone = true;
            }
        });
    }
}
