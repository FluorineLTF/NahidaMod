package nahidamod.card.Basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

public class ForbiddenKnowledge extends NahidaCard {
    public static final String ID = ModHelper.makePath("ForbiddenKnowledge");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings NO_WISDOM_UI = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("NoWisdomUI"));
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/ForbiddenKnowledge.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int TEMP_STRENGTH = 3;
    private static final int TEMP_DEXTERITY = 3;

    public ForbiddenKnowledge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.SELF, 0);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (p == null || !p.hasPower(WisdomPower.POWER_ID)
                || p.getPower(WisdomPower.POWER_ID).amount < 1) {
            this.cantUseMessage = (NO_WISDOM_UI != null && NO_WISDOM_UI.TEXT != null
                    && NO_WISDOM_UI.TEXT.length > 0)
                    ? NO_WISDOM_UI.TEXT[0] : "我没有足够的智慧！";
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(WisdomPower.POWER_ID)
                && AbstractDungeon.player.getPower(WisdomPower.POWER_ID).amount >= 1) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p == null) {
            return;
        }

        AbstractPower wisdom = p.getPower(WisdomPower.POWER_ID);
        if (wisdom != null && wisdom.amount >= 1) {
            wisdom.amount--;
            wisdom.updateDescription();
            if (wisdom.amount <= 0) {
                p.powers.remove(wisdom);
            }
        }

        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, TEMP_STRENGTH), TEMP_STRENGTH));
        this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, TEMP_STRENGTH), TEMP_STRENGTH));
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, TEMP_DEXTERITY), TEMP_DEXTERITY));
        this.addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, TEMP_DEXTERITY), TEMP_DEXTERITY));

        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!p.discardPile.isEmpty()) {
                    AbstractCard target = p.discardPile.getRandomCard(AbstractDungeon.cardRandomRng);
                    if (target != null) {
                        InactiveHelper.addInactive(target, 1);
                        target.superFlash();
                    }
                }
                this.isDone = true;
            }
        });
    }
}
