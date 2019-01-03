package com.ihwdz.android.hwapp.base.app;

import com.ihwdz.android.hwapp.ui.guide.GuideActivity;
import com.ihwdz.android.hwapp.ui.guide.GuideModule;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail.ClientDetailActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail.ClientDetailModule;
import com.ihwdz.android.hwapp.ui.home.clientseek.filter.ClientFilterActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.filter.ClientFilterModule;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientModule;
import com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate.UpdateActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate.UpdateModule;
import com.ihwdz.android.hwapp.ui.home.detail.NewsDetailActivity;
import com.ihwdz.android.hwapp.ui.home.detail.NewsDetailModule;
import com.ihwdz.android.hwapp.ui.home.index.IndexActivity;
import com.ihwdz.android.hwapp.ui.home.index.IndexModule;
import com.ihwdz.android.hwapp.ui.home.index.IndexWebActivity;
import com.ihwdz.android.hwapp.ui.home.index.IndexWebActivityModule;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoDayActivity;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoDayModule;
import com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialActivity;
import com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialModule;
import com.ihwdz.android.hwapp.ui.home.moodindex.MoodIndexActivity;
import com.ihwdz.android.hwapp.ui.home.moodindex.MoodIndexModule;
import com.ihwdz.android.hwapp.ui.home.more.MoreActivity;
import com.ihwdz.android.hwapp.ui.home.more.MoreModule;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryActivity;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryActivityModule;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.collections.PriceCollectionActivity;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.collections.PriceCollectionModule;
import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.ui.login.LoginPageModule;
import com.ihwdz.android.hwapp.ui.login.eula.EulaActivity;
import com.ihwdz.android.hwapp.ui.login.eula.EulaModule;
import com.ihwdz.android.hwapp.ui.login.login.LoginActivity;
import com.ihwdz.android.hwapp.ui.login.login.LoginModule;
import com.ihwdz.android.hwapp.ui.login.login.pwdforgot.PwdForgotActivity;
import com.ihwdz.android.hwapp.ui.login.login.pwdforgot.PwdForgotModule;
import com.ihwdz.android.hwapp.ui.login.register.RegisterActivity;
import com.ihwdz.android.hwapp.ui.login.register.RegisterModule;
import com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositModule;
import com.ihwdz.android.hwapp.ui.orders.confirm.OrderConfirmActivity;
import com.ihwdz.android.hwapp.ui.orders.confirm.OrderConfirmModule;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailActivity;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailModule;
import com.ihwdz.android.hwapp.ui.orders.extension.ExtensionActivity;
import com.ihwdz.android.hwapp.ui.orders.extension.ExtensionModule;
import com.ihwdz.android.hwapp.ui.orders.filter.CityFilterActivity;
import com.ihwdz.android.hwapp.ui.orders.filter.CityFilterModule;
import com.ihwdz.android.hwapp.ui.orders.logistics.LogisticActivity;
import com.ihwdz.android.hwapp.ui.orders.logistics.LogisticModule;
import com.ihwdz.android.hwapp.ui.orders.payment.PaymentActivity;
import com.ihwdz.android.hwapp.ui.orders.payment.PaymentModule;
import com.ihwdz.android.hwapp.ui.orders.query.QueryResultActivity;
import com.ihwdz.android.hwapp.ui.orders.query.QueryResultModule;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivityModule;
import com.ihwdz.android.hwapp.ui.main.advertisement.AdvertisementActivity;
import com.ihwdz.android.hwapp.ui.main.advertisement.AdvertisementModule;
import com.ihwdz.android.hwapp.ui.me.HwVIPCN.HwVipCNActivity;
import com.ihwdz.android.hwapp.ui.me.HwVIPCN.HwVipCNModule;
import com.ihwdz.android.hwapp.ui.me.aboutus.AboutActivity;
import com.ihwdz.android.hwapp.ui.me.aboutus.AboutModule;
import com.ihwdz.android.hwapp.ui.me.businessvip.myquote.QuoteActivity;
import com.ihwdz.android.hwapp.ui.me.businessvip.myquote.QuoteModule;
import com.ihwdz.android.hwapp.ui.me.collections.CollectionModule;
import com.ihwdz.android.hwapp.ui.me.collections.CollectionsActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist.PurchaseListActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist.PurchaseListModule;
import com.ihwdz.android.hwapp.ui.me.feedback.FeedbackActivity;
import com.ihwdz.android.hwapp.ui.me.feedback.FeedbackModule;
import com.ihwdz.android.hwapp.ui.me.improveinfo.ImproveInfoActivity;
import com.ihwdz.android.hwapp.ui.me.improveinfo.ImproveInfoModule;
import com.ihwdz.android.hwapp.ui.me.infoupdate.InfoUpdateActivity;
import com.ihwdz.android.hwapp.ui.me.infoupdate.InfoUpdateModule;
import com.ihwdz.android.hwapp.ui.me.infovip.searchadmin.SearchActivity;
import com.ihwdz.android.hwapp.ui.me.infovip.searchadmin.SearchModule;
import com.ihwdz.android.hwapp.ui.me.messages.MessagesActivity;
import com.ihwdz.android.hwapp.ui.me.messages.MessagesModule;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressModule;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.QuotaActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.QuotaModule;
import com.ihwdz.android.hwapp.ui.me.records.RecordsActivity;
import com.ihwdz.android.hwapp.ui.me.records.RecordsModule;
import com.ihwdz.android.hwapp.ui.me.records.invoice.InvoiceActivity;
import com.ihwdz.android.hwapp.ui.me.records.invoice.InvoiceModule;
import com.ihwdz.android.hwapp.ui.me.settings.SettingsActivity;
import com.ihwdz.android.hwapp.ui.me.settings.SettingsModule;
import com.ihwdz.android.hwapp.ui.me.usernotes.UserNotesActivity;
import com.ihwdz.android.hwapp.ui.me.usernotes.UserNotesModule;
import com.ihwdz.android.hwapp.ui.me.vipinformation.VipInfoActivity;
import com.ihwdz.android.hwapp.ui.me.vipinformation.VipInfoModule;
import com.ihwdz.android.hwapp.ui.me.vipinformation.updatepic.PicUpdateActivity;
import com.ihwdz.android.hwapp.ui.me.vipinformation.updatepic.PicUpdateModule;
import com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseActivity;
import com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseModule;
import com.ihwdz.android.hwapp.ui.orders.warehouse.choose.ChooseWHActivity;
import com.ihwdz.android.hwapp.ui.orders.warehouse.choose.ChooseWHModule;
import com.ihwdz.android.hwapp.ui.publish.address.AddressActivity;
import com.ihwdz.android.hwapp.ui.publish.address.AddressModule;
import com.ihwdz.android.hwapp.ui.publish.purchase.PurchaseActivity;
import com.ihwdz.android.hwapp.ui.publish.purchase.PurchaseModule;
import com.ihwdz.android.hwapp.ui.purchase.purchasedetail.PurchaseDetailActivity;
import com.ihwdz.android.hwapp.ui.purchase.purchasedetail.PurchaseDetailModule;
import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailActivity;
import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailModule;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewActivity;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewModule;
import com.ihwdz.android.hwslimcore.Base.SlimCoreModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/14
 * desc :
 * version: 1.0
 * </pre>
 */

@Module(
        includes = SlimCoreModule.class
)
public abstract class AllActivitiesModule {

    @ContributesAndroidInjector(modules = GuideModule.class)
    abstract GuideActivity contributeGuideActivityInjector();

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector(modules = AdvertisementModule.class)
    abstract AdvertisementActivity contributeAdvertisementActivityInjector();

    @ContributesAndroidInjector(modules = PriceInquiryActivityModule.class)
    abstract PriceInquiryActivity contributePriceInquiryActivityInjector();

    @ContributesAndroidInjector(modules = PriceCollectionModule.class)
    abstract PriceCollectionActivity contributePriceCollectionActivityInjector();

    @ContributesAndroidInjector(modules = InfoDayModule.class)
    abstract InfoDayActivity contributeInfoDayActivityInjector();

    @ContributesAndroidInjector(modules = IndexModule.class)
    abstract IndexActivity contributeIndexActivityInjector();

    @ContributesAndroidInjector(modules = IndexWebActivityModule.class)
    abstract IndexWebActivity contributeIndexWebActivityInjector();

    @ContributesAndroidInjector(modules = MoodIndexModule.class)
    abstract MoodIndexActivity contributeMoodIndexActivityInjector();

    @ContributesAndroidInjector(modules = ClientModule.class)
    abstract ClientActivity contributeClientActivityInjector();

    @ContributesAndroidInjector(modules = ClientDetailModule.class)
    abstract ClientDetailActivity contributeClientDetailActivityInjector();

    @ContributesAndroidInjector(modules = ClientFilterModule.class)
    abstract ClientFilterActivity contributeClientFilterActivityInjector();

    @ContributesAndroidInjector(modules = MaterialModule.class)
    abstract MaterialActivity contributeMaterialActivityInjector();

    @ContributesAndroidInjector(modules = MoreModule.class)
    abstract MoreActivity contributeMoreActivityInjector();

    @ContributesAndroidInjector(modules = NewsDetailModule.class)
    abstract NewsDetailActivity contributeNewsDetailActivityInjector();

    @ContributesAndroidInjector(modules = LoginPageModule.class)
    abstract LoginPageActivity contributeLoginPageActivityInjector();

    @ContributesAndroidInjector(modules = RegisterModule.class)
    abstract RegisterActivity contributeRegisterActivityInjector();

    @ContributesAndroidInjector(modules = EulaModule.class)
    abstract EulaActivity contributeEulaActivityInjector();

    @ContributesAndroidInjector(modules = UserNotesModule.class)
    abstract UserNotesActivity contributeUserNotesActivityInjector();

    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity contributeLoginActivityInjector();

    @ContributesAndroidInjector(modules = PwdForgotModule.class)
    abstract PwdForgotActivity contributePwdForgotActivityInjector();

    @ContributesAndroidInjector(modules = AboutModule.class)
    abstract AboutActivity contributeAboutActivityInjector();

    @ContributesAndroidInjector(modules = HwVipCNModule.class)
    abstract HwVipCNActivity contributeHwVipCNActivityInjector();

    @ContributesAndroidInjector(modules = SettingsModule.class)
    abstract SettingsActivity contributeSettingsActivityInjector();

    @ContributesAndroidInjector(modules = MessagesModule.class)
    abstract MessagesActivity contributeMessagesActivityInjector();

    @ContributesAndroidInjector(modules = RecordsModule.class)
    abstract RecordsActivity contributeRecordsActivityInjector();

    @ContributesAndroidInjector(modules = InvoiceModule.class)
    abstract InvoiceActivity contributeInvoiceActivityInjector();

    @ContributesAndroidInjector(modules = CollectionModule.class)
    abstract CollectionsActivity contributeCollectionsActivityInjector();

    @ContributesAndroidInjector(modules = QuoteModule.class)
    abstract QuoteActivity contributeQuoteActivityInjector();

    @ContributesAndroidInjector(modules = QuotaModule.class)
    abstract QuotaActivity contributeQuotaActivityInjector();

    @ContributesAndroidInjector(modules = PurchaseListModule.class)
    abstract PurchaseListActivity contributePurchaseListActivityInjector();

    @ContributesAndroidInjector(modules = ApplyProgressModule.class)
    abstract ApplyProgressActivity contributeApplyProgressActivityInjector();

    @ContributesAndroidInjector(modules = DepositModule.class)
    abstract DepositActivity contributeDepositActivityInjector();

    @ContributesAndroidInjector(modules = SearchModule.class)
    abstract SearchActivity contributeSearchActivityInjector();

    @ContributesAndroidInjector(modules = FeedbackModule.class)
    abstract FeedbackActivity contributeFeedbackActivityInjector();

    @ContributesAndroidInjector(modules = VipInfoModule.class)
    abstract VipInfoActivity contributeVipInfoActivityInjector();

    @ContributesAndroidInjector(modules = PicUpdateModule.class)
    abstract PicUpdateActivity contributePicUpdateActivityInjector();

    @ContributesAndroidInjector(modules = ImproveInfoModule.class)
    abstract ImproveInfoActivity contributeImproveInfoActivityInjector();

    @ContributesAndroidInjector(modules = InfoUpdateModule.class)
    abstract InfoUpdateActivity contributeInfoUpdateActivityInjector();

    @ContributesAndroidInjector(modules = QueryResultModule.class)
    abstract QueryResultActivity contributeQueryResultActivityInjector();

    @ContributesAndroidInjector(modules = PurchaseModule.class)
    abstract PurchaseActivity contributePurchaseActivityInjector();

    @ContributesAndroidInjector(modules = AddressModule.class)
    abstract AddressActivity contributeAddressActivityInjector();

    @ContributesAndroidInjector(modules = PurchaseDetailModule.class)
    abstract PurchaseDetailActivity contributePurchaseDetailActivityInjector();

    @ContributesAndroidInjector(modules = QuoteDetailModule.class)
    abstract QuoteDetailActivity contributeQuoteDetailActivityInjector();

    @ContributesAndroidInjector(modules = ReviewModule.class)
    abstract ReviewActivity contributeReviewActivityInjector();

    @ContributesAndroidInjector(modules = LogisticModule.class)
    abstract LogisticActivity contributeLogisticActivityInjector();

    @ContributesAndroidInjector(modules = ChooseWHModule.class)
    abstract ChooseWHActivity contributeChooseWHActivityInjector();

    @ContributesAndroidInjector(modules = WarehouseModule.class)
    abstract WarehouseActivity contributeWarehouseActivityInjector();

    @ContributesAndroidInjector(modules = OrderConfirmModule.class)
    abstract OrderConfirmActivity contributeOrderConfirmActivityInjector();

    @ContributesAndroidInjector(modules = OrderDetailModule.class)
    abstract OrderDetailActivity contributeOrderDetailActivityInjector();

    @ContributesAndroidInjector(modules = ExtensionModule.class)
    abstract ExtensionActivity contributeExtensionActivityInjector();

    @ContributesAndroidInjector(modules = PaymentModule.class)
    abstract PaymentActivity contributePaymentActivityInjector();

    @ContributesAndroidInjector(modules = CityFilterModule.class)
    abstract CityFilterActivity contributeCityFilterActivityInjector();

    @ContributesAndroidInjector(modules = UpdateModule.class)
    abstract UpdateActivity contributeUpdateActivityInjector();

//    @ContributesAndroidInjector(modules = AppSettingsModule.class)
//    abstract SlimAppSettings contributeSlimAppSettingsInjector();
//
//    @ContributesAndroidInjector(modules = SlimCoreModule.class)
//    abstract ISlimMainController contributeISlimMainControllerInjector();


}
