package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class DeepRooted extends NahidaCard {
    public static final String ID = ModHelper.makePath("DeepRooted");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/DeepRooted.png";
    private static final int COST = 5;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public DeepRooted() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.RARE, CardTarget.SELF, 0);
        this.block = this.baseBlock = 30;
    }

    private void updateCostByWisdom() {
        if (AbstractDungeon.player == null) {
            return;
        }
        AbstractPower wp = AbstractDungeon.player.getPower(WisdomPower.POWER_ID);
        int wisdom = wp != null ? Math.max(0, wp.amount) : 0;
        int newCost = Math.max(0, this.cost - wisdom);
        if (this.costForTurn != newCost) {
            this.costForTurn = newCost;
            this.isCostModifiedForTurn = (newCost != this.cost);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateCostByWisdom();
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        updateCostByWisdom();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(12);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
    }
}
