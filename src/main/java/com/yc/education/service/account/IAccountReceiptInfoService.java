package com.yc.education.service.account;


import com.yc.education.model.account.AccountReceiptInfo;
import com.yc.education.service.IService;

import java.util.List;

/**
 * @Description 收款单-详情
 * @Author BlueSky
 * @Date 2019-01-09 11:47
 */
public interface IAccountReceiptInfoService extends IService<AccountReceiptInfo> {
    /**
     * 根据 收款单id 查询
     * @param otherid
     * @return
     */
    List<AccountReceiptInfo> listAccountReceiptInfo(String otherid);

    /**
     * 根据外键id删除
     * @param otherid
     * @return
     */
    int deleteAccountReceiptInfoByParentId( String otherid);

}
