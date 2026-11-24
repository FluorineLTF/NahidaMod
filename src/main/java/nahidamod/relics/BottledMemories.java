package nahidamod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.Necronomicon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nahidamod.actions.JinnAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

public class BottledMemories extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(BottledMemories.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "NahidaResources/images/relics/TestRelic.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    private boolean cardSelected = true;

    public NahidaCard card = null;

    public BottledMemories() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);

    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BottledMemories();
    }

    public AbstractCard getCard() {
        return this.card.makeCopy();
    }


    public void atBattleStart() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    public void onEquip() {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof NahidaCard){
                if (((NahidaCard) c).inactives > 0){
                    temp.addToTop(c);
                }
            }
        }
        if (!temp.isEmpty()){
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(temp, 1, this.DESCRIPTIONS[1], false, false, false, false);
        }
    }

    public void onUnequip() {
        if (this.card != null) {
            NahidaCard cardInDeck = (NahidaCard) AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            if (cardInDeck != null) {
                cardInDeck.misc += 2;
                cardInDeck.inactives += 2;
                cardInDeck.updateDescription();
            }
        }

    }

    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            this.card = (NahidaCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.player.masterDeck.removeCard(this.card);
            NahidaCard card = (NahidaCard) this.card.makeStatEquivalentCopy();
            if (this.card.inactives == 1){
                card.misc = card.inactives = 0;
            }else {
                card.misc = card.inactives -= 2;
            }
            card.updateDescription();
            this.card = card;
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card,(float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }

    }
}
