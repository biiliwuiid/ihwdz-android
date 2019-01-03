package com.ihwdz.android.hwapp.common;

import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.ihwdz.android.hwapp.utils.FileUtils;

import rx.Subscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class Constant {

    public static final int REALM_VERSION = 0;
    public static final String REALM_NAME = "hw";
    public static final String SP_NAME = "hw";
    public static final String ExternalStorageDirectory =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    public static String DATABASE_FILE_PATH_FOLDER = "HwDataBase";
    public static String systemTime = null;

    /**
     *  个人中心　会员类型 VIP_TYPE （+ -认证 -锁定 ）
     *  会员类型：-1 用户; 0 资讯; 1 交易; 2 商家; (4 种类型)  // 3 交易未认证; 4 商家未认证; 5 交易失效; 6 商家失效; (8 种类型)
     * VIP_TYPE: 100 未登录
     * VIP_TYPE: -1 普通用户
     * VIP_TYPE: 0　资讯会员
     * VIP_TYPE: 1　交易会员     //-  3 交易未认证;  -  5 交易失效;
     * VIP_TYPE: 2　商家会员     //-  4 商家未认证;  -  6 商家失效;
     */
    public static int VIP_TYPE = 100;                // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员  // + 3 交易未认证; 4 商家未认证; 5 交易失效; 6 商家失效; (8 种状态)

    /**
     * 会员认证 状态： 0 -未认证, 1 -已认证
     */
    public static boolean VIP_AUTHENTIC = false;     // 会员认证 状态： 0 -未认证, 1 -已认证

    /**
     * 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
     */
    public static int VIP_LOCK_STATUS = 0;     	     // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销

    public static boolean isAuthenticated = false; // 交易会员是否已认证

    public static String totalAmount = null;         // 总额度
    public static String usedAmount = null;          // 已用额度
    public static String availableAmount = null;     // 可用额度

    public static String depositAmount = null;       // 保证金


    public static int APPLY_STATUS = 0;            // 开通交易会员进度:  0未申请 1申请中 2申请失败 3完善资料

    public static boolean LOGOUT  = true;          // 默认是未登录状态

    public static boolean FIRST_LOGIN  = true;
    public static boolean FIRST_LOAD  = true;


    public static String token = "";
    public static String user_account = "";
    public static String user_pwd = "";

    public static String goodsName = "";   // VIP level : 凤凰/喜鹊/麻雀
    public static String endDateStr = "";  // VIP 过期时间
    public static String endDate = "";     // VIP level   凤凰/喜鹊/麻雀 + vip过期时间
    public static long expireTime = 0;     // token 有效期

    public static String name = "";
    public static String tel = "";
    public static String email = "";

    // 会员信息 & 仓库信息 - 历史搜索保存在本地 （最多3组）
    public static String provinceName = "";
    public static String provinceCode = "";
    public static String cityName = "";
    public static String cityCode = "";
    public static String districtName = "";
    public static String districtCode = "";

    //  仓库信息 - 历史搜索保存在本地 （最多3组）
    public static String id = null;
    public static String warehouse = null;        // 仓库名称
    public static String warehouseAddress = null; // 仓库详细地址
    public static String warehousePhone = null;
    public static String warehouseContact = null;


    public static String address = "";          // 详细地址

    public static String adminId = "";
    public static String adminName = "";

    public static String companyNature = "";    // 企业性质
    public static String companyType = "";      // 企业类型
    public static String companyFullName = "";  // 公司全名




    public static boolean updateOptionDone = false;  // 会员权益支付
    public static boolean orderOptionDone = false;  // 订单按钮已操作 （支付手续费 申请展期  申请开票）
    public static String orderOptionId = null;      // 返回订单详情参数
    public static String orderOptionStatus = null;  // 返回订单详情参数
    public static int orderOption;                  // 返回订单详情参数

    public static boolean weChatPayFailed = false;  // 微信支付成功

    /**
     * 支付类型
     */
    public interface PayMode{
        int PAY_CHARGE = 0;              // 订单手续费
        int PAY_UPDATE = 1;           // 会员升级费 （找客户-升级）
    }


    /**
     * 申请开通交易会员: startApplyProgressActivity  from
     *   普通用户& 资讯会员的 会员中心 - 我的交易会员
     *   求购池 - 我的求购
     *   发布
     */
    public interface ApplyFrom{
        int DEAL_USER = 0;              // 会员中心 - 我的交易会员
        int PURCHASE_POOL = 1;          // 求购池 - 我的求购
        int PUBLISH_PURCHASE = 2;                // 发布（我要采购）

    }


    /**
     *   修改信息: startInfoUpdateActivity  修改- 保存
     *   用户信息
     *   报价-单价
     */
    public interface InfoUpdate{
        int INFO_USER = 0;              // 用户信息
        int INFO_QUOTE_PRICE = 1;       // 报价-单价

    }
    public static String quotePrice = "";  // 报价 -单价


    /**
     * 备注: startReviewActivity
     *   交易会员:
     *   价格复议（求购报价-价格复议）“确认复议”
     *   申请展期 备注（订单列表-申请展期）“提交申请”
     *   授信额度 （一键下单-结算方式:部分授信）“确认”
     *
     *   商家会员：
     *   复议报价 （我的报价-复议报价）“确认报价”
     */
    public interface Remarks{
        int PRICE_REVIEW = 0;     // 价格复议 备注 - 交易会员
        int EXTENSION_APPLY = 1;  // 申请展期 备注 - 交易会员
        int CREDIT_LINE = 2;      // 授信额度 - 交易会员

        int REVIEW_PRICE = 3;     // 复议报价  - 商家
    }
    public static double creditLine = 0.00d;             // 授信额度
    public static String extensionRemarks = null;        // 申请展期 备注



    // 仓库数据 历史搜索保存在本地 （最多3组）
    public static String warehouseListJson = null;
    // 仓库数据 （当前选中）
    public static String warehouseJson = null;

    /**
     * 发布求购 选择收货地址
     */
    public static String addressSelected_province = null;
    public static String addressSelected_provinceCode = null;
    public static String addressSelected_city = null;
    public static String addressSelected_cityCode = null;
    public static String addressSelected_district = null;
    public static String addressSelected_districtCode = null;
    public static String addressSelected_address = null;
    public static String addressSelected_mobile = null;
    public static String addressSelected_contact = null;


    // 查价格
    public interface priceType{
        int typeMarket = 0;     // 市场价
        int typeFactory = 1;    // 出厂价
    }

    public interface loadStatus{
        int STATUS_PREPARE = 0;    //
        int STATUS_LOADING = 1;    // （等待加载）上拉加载更多
        int STATUS_EMPTY = 2;      // （没有数据了）我也是有底线的
        int STATUS_ERROR = 3;      // （error）
        int STATUS_DISMISS = 4;
    }

    public interface BreedList{
        String ALL = "ABS,PP,PC";
    }

    public interface NewsDetailViewType{
        int typeDetail = 1;              // 详情
        int typeCommentTitle = 2;        // 精彩评论
        int typeComment = 3;             // 精彩评论
        int typeRecommendTitle = 4;      // 热门推荐
        int typeRecommend = 5;           // 热门推荐
        int typeFooter = 6;              // 尾布局
    }

    public interface viewType{
        int typeBanner = 1;            // 轮播图 （1）
        int typeGv = 2;                // 九宫格 tabs 一排 5 个 （2）
        int typeMarquee = 3;           // 跑马灯 （3）
        int typeTitle_deal = 4;        // 标题 实时成交（现款价）（4）
        int typeDealCard = 5;          // 交易卡（5）
        int typeTitle_hw24 = 6;        // 标题 鸿网24小时  （6）
        int typeList_hw24 = 7;         // 鸿网24小时 （7）
        int typeTitle_recommend = 8;   // 标题 推荐 (8)
        int typeList_recommend = 9;    // 推荐 (9)
        int getTypeTitle_news = 10;             // 新闻 title(10)
        int typeNews_CurrentNews = 11;             // 新闻 (10)
        int typeNews_IndustryFocused = 13;             // 新闻 (10)
        int typeNews_MarketComment = 14;             // 新闻 (10)
        int typeFooter = 12;             // footer (11)

    }

    // 登录注册验证码
    public interface VerificationCodeType{
        String type_Register = "0";
        String type_Login = "1";
        String type_ChangePwd = "2";

//        int type_Register = 0;
//        int type_Login = 1;
//        int type_ChangePwd = 2;
    }


    /**
     * logistics from_address & destination
     *  1.物流 需要 xxFrom & xxTo 两组地址
     *  2.会员信息 & 添加仓库 只需要  一组地址
     */
    public static String provFrom = "省";            // 出发地 省
    public static String cityFrom = "市";            // 出发地 市
    public static String distinctFrom = "始发地";    // 出发地 区

    public static String provTo = "省";           // 目的地 省
    public static String cityTo = "市";           // 目的地 市
    public static String distinctTo = "目的地";   // 目的地 区

    /**
     *  client filter - params
     */
    public static String hasMobile = null;          // "1":选择;  "0": 未选择
    public static String hasEmail = null;           // "1":选择;  "0": 未选择
    public static String startRegMoney = null;      // 注册资本 - start
    public static String endRegMoney = null;        // 注册资本 - end
    public static String startCompanyCreated = null;// 注册时间 - start
    public static String endCompanyCreated = null;  // 注册时间 - end

    public static String selectedCityCodes = null;  // 选择的 cityCode,cityCode1




    public class status{
        public static final int success = 200;
        public static final int error = -1;
    }


    public static String PATH_DATA = FileUtils.createRootPath(Utils.getApp()) + "/cache";
    public static String PATH_COLLECT = FileUtils.createRootPath(Utils.getApp()) + "/collect";



    /**-------------------------------------键-------------------------------------------------**/
    //Sp键
    public static final String KEY_FIRST_SPLASH = "first_splash";                 //是否第一次启动
    public static final String KEY_IS_LOGIN = "is_login";                         //登录



    /**-------------------------------------腾讯x5页面-------------------------------------------------**/
    public static final String SP_NO_IMAGE = "no_image";
    public static final String SP_AUTO_CACHE = "auto_cache";

}
