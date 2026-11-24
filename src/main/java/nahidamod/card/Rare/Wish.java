package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.optionCards.BecomeAlmighty;
import com.megacrit.cardcrawl.cards.optionCards.FameAndFortune;
import com.megacrit.cardcrawl.cards.optionCards.LiveForever;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.card.Wish.Memory;
import nahidamod.card.Wish.Surprise;
import nahidamod.card.Wish.Sweet;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import java.util.ArrayList;
import java.util.Iterator;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Wish extends NahidaCard {
    public static final String ID = ModHelper.makePath(Wish.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Wish() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.magicNumber = this.baseMagicNumber = 15;
        this.baseBlock = 3;
        this.baseDamage = 2;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeDamage(1);
            this.upgradeMagicNumber(5);
            this.upgradeBlock(1);
            this.initializeDescription();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
        stanceChoices.add(new Surprise());
        NahidaCard card = new Sweet();
        card.rawDescription = card.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + ((AbstractDungeon.player.maxHealth * this.magicNumber) / 100) + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
        card.initializeDescription();
        stanceChoices.add(card);
        stanceChoices.add(new Memory());
        if (this.upgraded) {
            Iterator var4 = stanceChoices.iterator();

            while(var4.hasNext()) {
                AbstractCard c = (AbstractCard)var4.next();
                c.upgrade();
            }
        }

        this.addToBot(new ChooseOneAction(stanceChoices));
    }

    public void applyPowers() {
    }

    public void calculateCardDamage(AbstractMonster mo) {
    }
}
