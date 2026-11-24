package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import java.util.Iterator;
import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class CalmlyRespond extends NahidaCard {
    public static final String ID = ModHelper.makePath(CalmlyRespond.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public CalmlyRespond() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.block = this.baseBlock = 8;

    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {

            for (AbstractCard c : p.hand.group) {
                if (c.type == CardType.ATTACK) {
                    canUse = false;
                    this.cantUseMessage = CardCrawlGame.languagePack.getUIString("NahidaMod:UnCalmMessage").TEXT[0];
                }
            }

            return canUse;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
    }
    @Override
    public void upgrade() {
        // 升级时不改变效果
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(4);
        }
    }
}
