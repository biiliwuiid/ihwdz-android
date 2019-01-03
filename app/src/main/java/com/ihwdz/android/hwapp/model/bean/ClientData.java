package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class ClientData {

    public String code;
    public String msg;
    public ClientEntity data;

    public static class ClientEntity{
        public String beginPageIndex;
        public String countResultMap;
        public String currentPage;
        public String endPageIndex;
        public String numPerPage;
        public String pageCount;
        public String totalCount;
        public List<ClientModel> recordList;
    }

    public static class ClientModel{
        public String city;
        public String companyMajor;
        public String companyName;
        public String hasEmail;
        public String hasMobile;
        public String id;
        public String linker;
        public String memberId;
        public String province;
        public String update;
    }
}
