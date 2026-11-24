package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;
import nahidamod.actions.TriggerNahidaMarksAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.NahidaMarkPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class E extends NahidaCard {
    public static final String ID = ModHelper.makePath(E.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public E() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
        }
        this.addToBot(new ApplyPowerAction(m, p, new NahidaMarkPower(m, this.magicNumber), this.magicNumber));
        this.addToBot(new TriggerNahidaMarksAction(this));
    }
    @Override
    public void upgrade() {
        // 升级时不改变效果
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
            this.initializeDescription();
        }
    }
}

