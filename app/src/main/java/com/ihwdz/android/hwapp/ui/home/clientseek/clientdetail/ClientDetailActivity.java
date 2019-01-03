package com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.model.bean.ClientDetailData;
import com.ihwdz.android.hwapp.ui.home.clientseek.filter.OnItemClickListener;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ClientDetailActivity extends BaseActivity implements ClientDetailContract.View {

    String TAG = "ClientDetailActivity";

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.iv_right)
    ImageView rightBt;

    @BindView(R.id.tv_company)
    TextView tvCompanyName;

    @BindView(R.id.star1)
    ImageView star1;
    @BindView(R.id.star2)
    ImageView star2;
    @BindView(R.id.star3)
    ImageView star3;
    @BindView(R.id.star4)
    ImageView star4;
    @BindView(R.id.star5)
    ImageView star5;
    @BindView(R.id.star_linear)
    LinearLayout starLinearLayout;          // 星级评定布局 - maybe hide
    @BindView(R.id.tv_linker)
    TextView tvLinker;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.switch_business_info)
    TextView switchBusinessInfo;     // 工商信息 开关
    @BindView(R.id.linear_business_info)
    LinearLayout linearBusinessInfo; // 工商信息 标题
    @BindView(R.id.business_info_layout)
    LinearLayout businessInfoLayout; // 工商信息
    @BindView(R.id.companyEmployeeNumber)
    TextView companyEmployeeNumber;
    @BindView(R.id.companyRegisterMoney)
    TextView companyRegisterMoney;
    @BindView(R.id.companyCreatedTimeStr)
    TextView companyCreatedTimeStr;

    @BindView(R.id.switch_company_intro)
    TextView switchCompanyIntro;     // 公司简介 开关
    @BindView(R.id.linear_company_intro)
    LinearLayout linearCompanyIntro; // 公司简介 标题 - maybe hide
    @BindView(R.id.company_intro_layout)
    LinearLayout companyIntroLayout; // 公司简介
    @BindView(R.id.tv_company_intro)
    TextView tvCompanyIntro;

    @BindView(R.id.switch_business_scope)
    TextView switchBusinessScope;     // 经营范围 开关
    @BindView(R.id.linear_business_scope)
    LinearLayout linearBusinessScope; // 经营范围 标题 - maybe hide
    @BindView(R.id.business_scope_layout)
    LinearLayout businessScopeLayout; // 经营范围
    @BindView(R.id.tv_business_scope)
    TextView tvBusinessScope;

    @BindView(R.id.switch_product_info)
    TextView switchProductInfo;         // 生产信息 开关
    @BindView(R.id.linear_product_info)
    LinearLayout linearProductInfo;     // 生产信息 标题
    @BindView(R.id.product_table_layout)
    TableLayout productTableLayout;    // 生产信息 表格布局
    @BindView(R.id.yearSaleAmount)
    TextView yearSaleAmount;
    @BindView(R.id.factoryArea)
    TextView factoryArea;
    @BindView(R.id.kgRate)
    TextView kgRate;
    @BindView(R.id.materialType)
    TextView materialType;
    @BindView(R.id.productTpe)
    TextView productTpe;
    @BindView(R.id.marketFlow)
    TextView marketFlow;


    @BindView(R.id.switch_material_info)
    TextView switchMaterialInfo;       // 使用原料 开关
    @BindView(R.id.linear_material_info)
    LinearLayout linearMaterialInfo;   // 使用原料 标题
    @BindView(R.id.material_table_layout)
    TableLayout materialTableLayout;  // 使用原料 表格布局
    @BindView(R.id.materialName)
    TextView materialName;
    @BindView(R.id.materialManufactory)
    TextView materialManufactory;
    @BindView(R.id.materialGrade)
    TextView materialGrade;

    @BindView(R.id.switch_device_list)
    TextView switchDeviceList;         // 设备清单 开关
    @BindView(R.id.linear_device_list)
    LinearLayout linearDeviceList;     // 设备清单 标题 - maybe hide
    @BindView(R.id.device_table_layout)
    TableLayout deviceTableLayout;    // 设备清单 表格布局
    @BindView(R.id.row_device_no)
    TableRow deviceRow;                     // 设备清单 无
    @BindView(R.id.recyclerView_device)
    RecyclerView deviceRecyclerView;  // 设备清单
    @BindView(R.id.deviceName)
    TextView deviceName;
    @BindView(R.id.deviceBrand)
    TextView deviceBrand;
    @BindView(R.id.deviceGrade)
    TextView deviceGrade;
    @BindView(R.id.deviceQuantity)
    TextView deviceQuantity;
    @BindView(R.id.purchaseDate)
    TextView purchaseDate;

    @BindView(R.id.switch_product_device)
    TextView switchProductDevice;       // 产品设备展示 开关
    @BindView(R.id.linear_product_device)
    LinearLayout linearProductDevice;   // 产品设备展示 标题 - maybe hide
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;                   // 产品设备展示

    String unfolded;     // 展开
    String folded;       // 收起
    String nothing;      // 无
    String person;       // 人
    String tenThousand;  // 万元

    boolean isBusinessInfoFolded = false; // 工商信息 开关状态
    boolean isCompanyIntroFolded = false; // 公司简介 开关状态
    boolean isBusinessScopeFolded = false;// 经营范围 开关状态
    boolean isProductInfoFolded = false;  // 生产信息 开关状态
    boolean isMaterialInfoFolded = false; // 使用原料 开关状态
    boolean isDeviceListFolded = false;   // 设备清单 开关状态
    boolean isProductDeviceFolded = false;// 设备清单 开关状态

    String id;        // 当前客户id  352718
    boolean isFollowed = false;
    Drawable unFollowIcon;
    Drawable followIcon;

    Drawable starIcon;
    Drawable halfStarIcon;
    Drawable blankStarIcon;

    @Inject
    ClientDetailContract.Presenter mPresenter;

    @BindView(R.id.business_info_title)
    TextView businessInfoTitle;
    @BindView(R.id.company_intro_title)
    TextView companyIntroTitle;
    @BindView(R.id.business_scope_title)
    TextView businessScopeTitle;
    @BindView(R.id.product_info_title)
    TextView productInfoTitle;
    @BindView(R.id.material_info_title)
    TextView materialInfoTitle;
    @BindView(R.id.materialName_title)
    TextView materialNameTitle;
    @BindView(R.id.materialManufactory_title)
    TextView materialManufactoryTitle;
    @BindView(R.id.materialGrade_title)
    TextView materialGradeTitle;
    @BindView(R.id.device_list_title)
    TextView deviceListTitle;
    @BindView(R.id.deviceName_title)
    TextView deviceNameTitle;
    @BindView(R.id.deviceBrand_title)
    TextView deviceBrandTitle;
    @BindView(R.id.deviceGrade_title)
    TextView deviceGradeTitle;
    @BindView(R.id.deviceQuantity_title)
    TextView deviceQuantityTitle;
    @BindView(R.id.purchaseDate_title)
    TextView purchaseDateTitle;
    @BindView(R.id.product_device_title)
    TextView productDeviceTitle;


    @Override
    public int getContentView() {
        return R.layout.activity_client_detail;
    }

    @Override
    public void initView() {
        LogUtils.printInfo(TAG, "initView");
        if (getIntent() != null) {
            id = getIntent().getStringExtra("ID");
            mPresenter.setClientId(id);
            Log.d(TAG, "===================== ID: " + id);
        }

        initToolBar();
        initBoldText();
        //initDeviceRecyclerView();  // 设备清单
        //initProductRecyclerView(); // 产品设备展示
    }

    private void initBoldText() {
        tvCompanyName.getPaint().setFakeBoldText(true);
        tvLinker.getPaint().setFakeBoldText(true);

        businessInfoTitle.getPaint().setFakeBoldText(true);
        companyIntroTitle.getPaint().setFakeBoldText(true);
        businessScopeTitle.getPaint().setFakeBoldText(true);
        productInfoTitle.getPaint().setFakeBoldText(true);
        materialInfoTitle.getPaint().setFakeBoldText(true);
        materialNameTitle.getPaint().setFakeBoldText(true);
        materialManufactoryTitle.getPaint().setFakeBoldText(true);
        materialGradeTitle.getPaint().setFakeBoldText(true);
        deviceListTitle.getPaint().setFakeBoldText(true);
        deviceNameTitle.getPaint().setFakeBoldText(true);
        deviceBrandTitle.getPaint().setFakeBoldText(true);
        deviceGradeTitle.getPaint().setFakeBoldText(true);
        deviceQuantityTitle.getPaint().setFakeBoldText(true);
        purchaseDateTitle.getPaint().setFakeBoldText(true);
        productDeviceTitle.getPaint().setFakeBoldText(true);


        // 生产信息
        yearSaleAmount.getPaint().setFakeBoldText(true);
        factoryArea.getPaint().setFakeBoldText(true);
        kgRate.getPaint().setFakeBoldText(true);
        materialType.getPaint().setFakeBoldText(true);
        productTpe.getPaint().setFakeBoldText(true);
        marketFlow.getPaint().setFakeBoldText(true);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        LogUtils.printInfo(TAG, "initData");
        mPresenter.getClientDetailData();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.iv_right)
    public void onRightBtClicked() {
        LogUtils.printInfo(TAG, "onRightBtClicked");
        Log.d(TAG, "===================== onRightBtClicked");
        if (isFollowed) {
            isFollowed = false;
        } else {
            isFollowed = true;
        }
        updateFollowIcon(isFollowed);
        if (isFollowed) {
            mPresenter.setFollow("1");
        } else {
            mPresenter.setFollow("0");
        }
        mPresenter.updateClientFollowData();
    }

    private void initToolBar() {

        unfolded = getResources().getString(R.string.unfold);
        folded = getResources().getString(R.string.fold);
        nothing = getResources().getString(R.string.nothing);
        person = getResources().getString(R.string.person);
        tenThousand = getResources().getString(R.string.ten_thousand);

        unFollowIcon = getResources().getDrawable(R.drawable.unfollow);
        followIcon = getResources().getDrawable(R.drawable.followed);

        starIcon = getResources().getDrawable(R.drawable.star);
        halfStarIcon = getResources().getDrawable(R.drawable.star_helf);
        blankStarIcon = getResources().getDrawable(R.drawable.star_no);

        backBt.setVisibility(View.VISIBLE);
        rightBt.setImageDrawable(unFollowIcon);
        rightBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_cl));
    }


    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    @Override
    public void setFollowStarClickable(boolean clickable) {
        rightBt.setEnabled(clickable);
        rightBt.setClickable(clickable);
    }

    // 设备清单 空信息
    @Override
    public void showEmptyDeviceLayout() {
        deviceRecyclerView.setVisibility(View.GONE);
        deviceRow.setVisibility(View.VISIBLE);
    }

    // 隐藏 产品设备展示
    @Override
    public void hideProductLayout() {
        linearProductDevice.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showProductLayout() {
        linearProductDevice.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateFollowIcon(boolean followed) {
        isFollowed = followed;
        if (followed) {
            rightBt.setImageDrawable(followIcon);
        } else {
            rightBt.setImageDrawable(unFollowIcon);
        }
    }

    @Override
    public void updateView(ClientDetailData data) {

        tvCompanyName.setText(getNonNullString(data.data.companyName));
        // star
        String s = data.data.companyRank;
        double d = Double.parseDouble(s);
        int rankInt = (int) d;
        //LogUtils.printInfo(TAG, "d: " + d);
        LogUtils.printInfo(TAG, "rankInt: " + rankInt);
        initStar(rankInt);

        tvLinker.setText(getNonNullString(data.data.linker));
        tvTel.setText(getNonNullString(data.data.linkerMobile));
        tvEmail.setText(getNonNullString(data.data.linkerEmail));
        tvAddress.setText(getNonNullString(data.data.address));

        // 工商信息
        companyEmployeeNumber.setText(getNonNullString(data.data.companyEmployeeNumber + person));
        companyRegisterMoney.setText(getNonNullString(data.data.companyRegisterMony + tenThousand));
        companyCreatedTimeStr.setText(getNonNullString(data.data.companyCreatedTimeStr));

        // 公司简介
        if (data.data.companyDesc != null && data.data.companyDesc.length() > 0) {
            tvCompanyIntro.setText(data.data.companyDesc);
        } else {
            linearCompanyIntro.setVisibility(View.GONE);
            companyIntroLayout.setVisibility(View.GONE);
        }


        // 经营范围
        if (data.data.companyMajor != null && data.data.companyMajor.length() > 0) {
            tvBusinessScope.setText(data.data.companyMajor);
        } else {
            linearBusinessScope.setVisibility(View.GONE);
            businessScopeLayout.setVisibility(View.GONE);
        }

        // 生产信息
        yearSaleAmount.setText(getNonNullString(data.data.plantVO.yearSaleAmount));
        factoryArea.setText(getNonNullString(data.data.plantVO.factoryArea));
        kgRate.setText(getNonNullString(data.data.plantVO.kgRate));
        materialType.setText(getNonNullString(data.data.plantVO.materialType));
        productTpe.setText(getNonNullString(data.data.plantVO.productTpe));
        marketFlow.setText(getNonNullString(data.data.plantVO.marketFlow));

        // 使用原料 list
        String materialNameStr = "";            // 常用品种
        String materialManufactoryStr = "";     // 常用商家
        String materialGradeStr = "";           // 常用型号

        if (data.data.materialVOs != null && data.data.materialVOs.size() > 0) {
            List<ClientDetailData.MaterialVOs> materialVOs = data.data.materialVOs;

            for (int i = 0; i < materialVOs.size(); i++) {
                materialNameStr = materialVOs.get(i).materialName;
                materialManufactoryStr = materialVOs.get(i).materialManufactory;
                materialGradeStr = materialVOs.get(i).materialGrade;
            }
        } else {
            materialNameStr = nothing;
            materialManufactoryStr = nothing;
            materialGradeStr = nothing;
        }
        materialName.setText(materialNameStr);
        materialManufactory.setText(materialManufactoryStr);
        materialGrade.setText(materialGradeStr);

//        // 设备清单 list（null -> hide）
//        if (data.data.deviceVOs != null && data.data.deviceVOs.size() > 0){
//
//            mPresenter.getDeviceAdapter().clear();
//            mPresenter.getDeviceAdapter().addDataList(data.data.deviceVOs);
//        }else {
//            linearDeviceList.setVisibility(View.GONE);
//            deviceTableLayout.setVisibility(View.GONE);
//            deviceRecyclerView.setVisibility(View.GONE);
//        }
    }

    private String getNonNullString(String data) {
        return (data == null || data.length() < 1) ? "--" : data;
    }


    // 设备清单
    @Override
    public void initDeviceRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        deviceRecyclerView.setLayoutManager(layoutManager);
        deviceRecyclerView.setAdapter(mPresenter.getDeviceAdapter());
    }

    // 产品设备展示
    @Override
    public void initProductRecyclerView() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getProductAdapter());

        mPresenter.getProductAdapter().addItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(String string) {
                // 获取到点击的图片的url
                LogUtils.printInfo(TAG, "------------image url: " + string);
            }
        });
    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }


    // 展开/收起 点击事件
    @OnClick({R.id.switch_business_info, R.id.switch_company_intro, R.id.switch_business_scope, R.id.switch_product_info, R.id.switch_material_info, R.id.switch_device_list, R.id.switch_product_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_business_info:
                if (isBusinessInfoFolded) {
                    // 当前为折叠状态: 折叠 -> 展开
                    isBusinessInfoFolded = false;
                    switchBusinessInfo.setText(unfolded);
                    businessInfoLayout.setVisibility(View.GONE);

                } else {
                    // 当前为展开状态 - 收起
                    isBusinessInfoFolded = true;
                    switchBusinessInfo.setText(folded);
                    businessInfoLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.switch_company_intro:
                if (isCompanyIntroFolded) {
                    // 当前为折叠状态 - 展开
                    isCompanyIntroFolded = false;
                    switchCompanyIntro.setText(unfolded);
                    companyIntroLayout.setVisibility(View.GONE);

                } else {
                    // 当前为展开状态 - 收起
                    isCompanyIntroFolded = true;
                    switchCompanyIntro.setText(folded);
                    companyIntroLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.switch_business_scope:
                if (isBusinessScopeFolded) {
                    // 当前为折叠状态 - 展开
                    isBusinessScopeFolded = false;
                    switchBusinessScope.setText(unfolded);
                    businessScopeLayout.setVisibility(View.GONE);

                } else {
                    // 当前为展开状态 - 收起
                    isBusinessScopeFolded = true;
                    switchBusinessScope.setText(folded);
                    businessScopeLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.switch_product_info:
                if (isProductInfoFolded) {
                    // 当前为折叠状态 - 展开
                    isProductInfoFolded = false;
                    switchProductInfo.setText(unfolded);
                    productTableLayout.setVisibility(View.GONE);

                } else {
                    // 当前为展开状态 - 收起
                    isProductInfoFolded = true;
                    switchProductInfo.setText(folded);
                    productTableLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.switch_material_info:
                if (isMaterialInfoFolded) {
                    // 当前为折叠状态 - 展开
                    isMaterialInfoFolded = false;
                    switchMaterialInfo.setText(unfolded);
                    materialTableLayout.setVisibility(View.GONE);

                } else {
                    // 当前为展开状态 - 收起
                    isMaterialInfoFolded = true;
                    switchMaterialInfo.setText(folded);
                    materialTableLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.switch_device_list:
                if (isDeviceListFolded) {
                    // 当前为折叠状态 - 展开
                    isDeviceListFolded = false;
                    switchDeviceList.setText(unfolded);
                    deviceTableLayout.setVisibility(View.GONE);
                    deviceRecyclerView.setVisibility(View.GONE);

                } else {
                    // 当前为展开状态 - 收起
                    isDeviceListFolded = true;
                    switchDeviceList.setText(folded);
                    deviceTableLayout.setVisibility(View.VISIBLE);
                    deviceRecyclerView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.switch_product_device:
                if (isProductDeviceFolded) {
                    // 当前为折叠状态 - 展开
                    isProductDeviceFolded = false;
                    switchProductDevice.setText(unfolded);
                    recyclerView.setVisibility(View.GONE);

                } else {
                    // 当前为展开状态 - 收起
                    isProductDeviceFolded = true;
                    switchProductDevice.setText(folded);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    /**
     * -1: 不展示 星
     * 0:  全空心
     * 1:  2 星
     * 2： 3 星
     * 3： 3.5 星
     * 4： 4 星
     * 其他：4.5 星
     */
    private void initStar(String rank) {
        switch (rank) {
            case "-1":
                starLinearLayout.setVisibility(View.GONE);
                break;
            case "0.0":
                setStarIcon(blankStarIcon, blankStarIcon, blankStarIcon, blankStarIcon, blankStarIcon);
                break;
            case "1.0":
                setStarIcon(starIcon, starIcon, blankStarIcon, blankStarIcon, blankStarIcon);
                break;
            case "2.0":
                setStarIcon(starIcon, starIcon, starIcon, blankStarIcon, blankStarIcon);
                break;
            case "3.0":
                setStarIcon(starIcon, starIcon, starIcon, halfStarIcon, blankStarIcon);
                break;
            case "4.0":
                setStarIcon(starIcon, starIcon, starIcon, starIcon, blankStarIcon);
                break;
            default:
                setStarIcon(starIcon, starIcon, starIcon, starIcon, halfStarIcon);
                break;

        }
    }
    private void initStar(int rank) {
        switch (rank) {
            case -1:
                starLinearLayout.setVisibility(View.GONE);
                break;
            case 0:
                setStarIcon(blankStarIcon, blankStarIcon, blankStarIcon, blankStarIcon, blankStarIcon);
                break;
            case 1:
                setStarIcon(starIcon, starIcon, blankStarIcon, blankStarIcon, blankStarIcon);
                break;
            case 2:
                setStarIcon(starIcon, starIcon, starIcon, blankStarIcon, blankStarIcon);
                break;
            case 3:
                setStarIcon(starIcon, starIcon, starIcon, halfStarIcon, blankStarIcon);
                break;
            case 4:
                setStarIcon(starIcon, starIcon, starIcon, starIcon, blankStarIcon);
                break;
            default:
                setStarIcon(starIcon, starIcon, starIcon, starIcon, halfStarIcon);
                break;

        }
    }

    private void setStarIcon(Drawable starIcon1, Drawable starIcon2, Drawable starIcon3, Drawable starIcon4, Drawable starIcon5) {
        star1.setImageDrawable(starIcon1);
        star2.setImageDrawable(starIcon2);
        star3.setImageDrawable(starIcon3);
        star4.setImageDrawable(starIcon4);
        star5.setImageDrawable(starIcon5);
    }

}
