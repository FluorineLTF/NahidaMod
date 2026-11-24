package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import nahidamod.helpers.DendroCoreHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class RagingLotus extends NahidaCard {
    public static final String ID = ModHelper.makePath("RagingLotus");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("NoDendroCoreUI"));
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/RagingLotus.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public RagingLotus() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY, 0);
        this.hasUPGRADE_DESCRIPTION = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (DendroCoreHelper.getDendroCoreCount() <= 0) {
            this.cantUseMessage = UI_STRINGS != null ? UI_STRINGS.TEXT[0] : "场上没有草原核！";
            return false;
        }
        return true;
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
        final boolean isUpgraded = this.upgraded;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int cores = DendroCoreHelper.consumeAllDendroCores();
                for (int i = 0; i < cores; i++) {
                    this.addToBot(new DamageAllEnemiesAction(
                            p,
                            DamageInfo.createDamageMatrix(12, true),
                            DamageInfo.DamageType.THORNS,
                            AttackEffect.FIRE
                    ));
                    if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
                        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (!mo.isDeadOrEscaped()) {
                                this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, 1, false), 1));
                            }
                        }
                    }
                }
                if (cores > 0) {
                    DendroCoreHelper.spawnDendroCore(isUpgraded ? cores : 1);
                }
                this.isDone = true;
            }
        });
    }
}
