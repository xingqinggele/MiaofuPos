package com.example.miaofupos.net;

import com.example.miaofupos.bean.OrderBean;

import java.util.List;

/**
 * 作者：zb.
 * <p>
 * 时间：On 2019-05-05.
 * <p>
 * 描述：所有的请求接口
 */
public class HttpRequest {

    /**
     * 登录接口
     *
     * @param params
     * @param callback
     */
    public static void getLogin(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "login", params, token, callback, null);
    }

    /**
     * 首页数据接口
     *
     * @param params
     * @param callback
     */
    public static void getHomeDate(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/homepage/data", params, token, callback, null);
    }

    /**
     * 获取用户终端统计数据
     *
     * @param params
     * @param callback
     */
    public static void getEquipment(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/counts", params, token, callback, null);
    }

    /**
     * 获取用户终端列表
     *
     * @param params
     * @param callback
     */
    public static void getEquipmentList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/list", params, token, callback, null);
    }

    /**
     * 获取用户划拨任务
     *
     * @param params
     * @param callback
     */
    public static void getTypeList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/pos/type/list", params, token, callback, null);
    }
    /**
     * 终端查询筛选条件
     *
     * @param params
     * @param callback
     */
    public static void getConditions(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/filter/conditions", params, token, callback, null);
    }

    /**
     * 获取直接子商户字典
     *
     * @param params
     * @param callback
     */
    public static void getMerchantsList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/direct/child/dict", params, token, callback, null);
    }

    /**
     * 终端划拔回调操作
     *
     * @param params
     * @param callback
     */
    public static void updPosListFrom(RequestParams params, String token, int[] data, ResponseCallback callback) {
        RequestMode.postRequest2(Urls.commUrls + "pos/api/v2/terminal/operations", params, token, data, callback, null);
    }

    /**
     * 区间查询用户终端列表
     *
     * @param params
     * @param callback
     */
    public static void updPosintervalList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/interval/list", params, token, callback, null);
    }

    /**
     * 获取我的伙伴列表
     *
     * @param params
     * @param callback
     */
    public static void updMypartnerList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/mypartner/list", params, token, callback, null);
    }

    /**
     * 获取我的伙伴详情
     *
     * @param params
     * @param callback
     */
    public static void updMypartnerDetail(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/mypartner/detail", params, token, callback, null);
    }

    /**
     * 终端划拔回调简要记录
     *
     * @param params
     * @param callback
     */
    public static void getRecords(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/operations/brief/records", params, token, callback, null);
    }

    /**
     * 获取省市区
     *
     * @param params
     * @param callback
     */
    public static void getCity(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/common/dictdata/list", params, token, callback, null);
    }

    /**
     * 报件获取省市区
     *
     * @param params
     * @param callback
     */
    public static void getCityB(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1/code", params, token, callback, null);
    }

    /**
     * 商户入驻，实名认证
     *
     * @param params
     * @param callback
     */
    public static void getIn(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/register", params, token, callback, null);
    }

    /**
     * 获取验证码接口
     *
     * @param params
     * @param callback
     */
    public static void getRegister_Code(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.getRequest1(Urls.commUrls + "pos/api/v2/common/verifyCode/sender", params, token, callback, null);
    }

    /**
     * 获取验证码接口
     *
     * @param params
     * @param callback
     */
    public static void getRegister_Code1(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.getRequest1(Urls.commUrls + "noauth/verifyCode/sender", params, token, callback, null);
    }

    /**
     * 获取商户入驻信息
     *
     * @param params
     * @param callback
     */
    public static void getCurrent(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/current", params, token, callback, null);
    }

    /**
     * 获取我的银行卡信息
     *
     * @param params
     * @param callback
     */
    public static void getBankInfo(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/bankcard/homepage", params, token, callback, null);
    }

    /**
     * 银行卡变更
     *
     * @param params
     * @param callback
     */
    public static void getBankChange(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/bankcard/alter", params, token, callback, null);
    }

    /**
     * 修改密码
     *
     * @param params
     * @param callback
     */
    public static void getPass(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/password/reset", params, token, callback, null);
    }

    /**
     * 忘记 密码
     *
     * @param params
     * @param callback
     */
    public static void getPass1(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/password/reset", params, token, callback, null);
    }

    /**
     * 邀请伙伴
     *
     * @param params
     * @param callback
     */
    public static void getInvitationPartner(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/mypartner/invite", params, token, callback, null);
    }

    /**
     * 支付密码，验证是否身份证后六位相同
     *
     * @param params
     * @param callback
     */
    public static void getPay_password1(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/payment/validate", params, token, callback, null);
    }

    /**
     * 支付密码，设置
     *
     * @param params
     * @param callback
     */
    public static void getPay_password2(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/payment/updatePassword", params, token, callback, null);
    }

    /**
     * 支付密码，修改，比对原密码是否正确
     *
     * @param params
     * @param callback
     */
    public static void getPay_ModifyPassword(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/payment/validatePassUpdate", params, token, callback, null);
    }

    /**
     * 更换头像
     *
     * @param params
     * @param callback
     */
    public static void getUpdatePortrait(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/updatePortrait", params, token, callback, null);
    }

    /**
     * 获取数据页，数据
     *
     * @param params
     * @param callback
     */
    public static void getTransAmount(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/homepage/transAmountStatistics", params, token, callback, null);
    }

    /**
     * 钱包余额
     *
     * @param params
     * @param callback
     */
    public static void getBalanceOf(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/wallet/homepage", params, token, callback, null);
    }

    /**
     * 账单列表
     *
     * @param params
     * @param callback
     */
    public static void getBillList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/wallet/bill/list", params, token, callback, null);
    }

    /**
     * 账单列表详情
     *
     * @param params
     * @param callback
     */
    public static void getBillDetails(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/message/billDetails", params, token, callback, null);
    }


    /**
     * 账单类型
     *
     * @param params
     * @param callback
     */
    public static void getBillType(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/wallet/bill/type", params, token, callback, null);
    }

    /**
     * 消息列表
     *
     * @param params
     * @param callback
     */
    public static void getMessageList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/message/list", params, token, callback, null);
    }

    /**
     * 消息列表详情
     *
     * @param params
     * @param callback
     */
    public static void getMessageDetail(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/message/msgTypeDetail", params, token, callback, null);
    }


    /**
     * 我的积分首页数据
     *
     * @param params
     * @param callback
     */
    public static void getTotal_score(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/homepage", params, token, callback, null);
    }


    /**
     * 我的积分详细列表
     *
     * @param params
     * @param callback
     */
    public static void getTotal_scoreList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/list", params, token, callback, null);
    }

    /**
     * 提交兑换积分订单
     *
     * @param params
     * @param callback
     */
    public static void getSubmit_orders(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/order", params, token, callback, null);
    }
    /**
     * 提交兑换积分订单
     *
     * @param params
     * @param callback
     */
    public static void getSubmit_orderList(RequestParams params, String token, List<OrderBean> list,ResponseCallback callback) {
        RequestMode.postRequest3(Urls.commUrls + "pos/api/v2/integral/cart/order", params, token, list, callback, null);
    }




    /**
     * 获取订单列表
     *
     * @param params
     * @param callback
     */
    public static void getOrderList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/order/list/type", params, token, callback, null);
    }


    /**
     * 获取订单列表详情
     *
     * @param params
     * @param callback
     */
    public static void getOrderList_detail(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/order/detail/type", params, token, callback, null);
    }

    /**
     * 判断身份证号是否唯一
     *
     * @param params
     * @param callback
     */
    public static void getIsIdNumber(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/idCordIsExist", params, token, callback, null);
    }


    /**
     * 获取我的商户列表
     *
     * @param params
     * @param callback
     */
    public static void getMeMerchants_list(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v1/merchant/my/merch", params, token, callback, null);
    }



    /**
     * 获取我的商户列表详情 -- 设备
     *
     * @param params
     * @param callback
     */
    public static void getMeMerchants_detailEquipment(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/queryEquipmentInfo", params, token, callback, null);
    }


    /**
     * 获取我的商户列表详情 -- 交易
     *
     * @param params
     * @param callback
     */
    public static void getMeMerchants_detailTrading(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/queryMerchantDealInfo", params, token, callback, null);
    }


    /**
     * 获取提现界面数据
     *
     * @param params
     * @param callback
     */
    public static void getPayData(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/payment/toCashOut", params, token, callback, null);
    }

    /**
     * 核对密码接口
     *
     * @param params
     * @param callback
     */
    public static void getPayPassWord(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/payment/confirmPassword", params, token, callback, null);
    }

    /**
     * 提交提现接口
     *
     * @param params
     * @param callback
     */
    public static void getPayWithdrawal(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/payment/doCashOut", params, token, callback, null);
    }


    /**
     * 提交新提现接口
     *
     * @param params
     * @param callback
     */
    public static void getNewPayWithdrawal(RequestParams params, String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "xinLong/signed/withdrawal/submitGrantDetail", params,token,callback, null);
    }



    /**
     * 个人业绩 ----- 日交易量、月交易量
     *
     * @param params
     * @param callback
     */
    public static void getDayAmount(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/getMerchantTransInfo", params, token, callback, null);
    }

    /**
     * 团队业绩 -----日交易量、月交易量
     *
     * @param params
     * @param callback
     */
    public static void getManthAmount(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/getTeamTransInfo", params, token, callback, null);
    }

      /**
     * 总业绩 -----日交易量、月交易量
     *
     * @param params
     * @param callback
     */
    public static void getTotalAmount(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/getAllTransInfo", params, token, callback, null);
    }



    /**
     * 我的收益
     *
     * @param params
     * @param callback
     */
    public static void getEarnings(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/message/earnings", params, token, callback, null);
    }


    /**
     * 请求可转移的伙伴
     *
     * @param params
     * @param callback
     */
    public static void getTransferList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/transfer/list", params, token, callback, null);
    }

    /**
     * 转移商户
     *
     * @param params
     * @param callback
     */
    public static void getTransferMerchants(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/transfer/updateTransfer", params, token, callback, null);
    }

    /**
     * 请求交易线形图数据
     *
     * @param params
     * @param callback
     */
    public static void getTradingDataList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/mypartner/amount/statistical", params, token, callback, null);
    }


    /**
     * 请求设备线形图数据
     *
     * @param params
     * @param callback
     */
    public static void getEquipmentDataList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/mypartner/machines/statistical", params, token, callback, null);
    }


    /**
     * 请求设备线形图数据
     *
     * @param params
     * @param callback
     */
    public static void getObtainSuperior(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/homepage/my/referrer", params, token, callback, null);
    }


    /**
     * 请求设备线形图数据
     *
     * @param params
     * @param callback
     */
    public static void getTeamPersonList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/teamList", params, token, callback, null);
    }

    /**
     * 新增收货地址
     *
     * @param params
     * @param callback
     */
    public static void getSaveAddress(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/address/save", params, token, callback, null);
    }

    /**
     * 收货地址列表
     *
     * @param params
     * @param callback
     */
    public static void getAddressList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/address/list", params, token, callback, null);
    }

    /**
     * 收货地址修改默认
     *
     * @param params
     * @param callback
     */
    public static void getAddressType(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/address/getType", params, token, callback, null);
    }

    /**
     * 删除收货地址
     *
     * @param params
     * @param callback
     */
    public static void DeleteAddress(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/address/del", params, token, callback, null);
    }

    /**
     * 编辑收货地址
     *
     * @param params
     * @param callback
     */
    public static void EditorAddress(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/address/edit", params, token, callback, null);
    }



    /**
     * 获取一条收货地址
     *
     * @param params
     * @param callback
     */
    public static void orderAddress(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/orderAddress", params, token, callback, null);
    }


    /**
     * 广告位
     *
     * @param params
     * @param callback
     */
    public static void getAdvertising(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/advertising/getAdvertising", params, token, callback, null);
    }

    /**
     * 排行榜
     *
     * @param params
     * @param callback
     */
    public static void getRanking(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/homepage/ranking", params, token, callback, null);
    }

    /**
     * 我的
     *
     * @param params
     * @param callback
     */
    public static void getMeData(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/homepage/dataInfo", params, token, callback, null);
    }


    /**
     * 请求回调设备列表
     *
     * @param params
     * @param callback
     */
    public static void getPosList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/posList", params, token, callback, null);
    }

    /**
     * 积分设备列表
     *
     * @param params
     * @param callback
     */
    public static void getMostList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/get/posBrandType", params, token, callback, null);
    }

  /**
     * 获取设备类型
     *
     * @param params
     * @param callback
     */
    public static void getPosType(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/get/posType", params, token, callback, null);
    }
    /**
     * 终端查询设备类型
     *
     * @param params
     * @param callback
     */
    public static void getPosBrandTypeAll(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/integral/get/posBrandTypeAll", params, token, callback, null);
    }


 /**
     * 获订单类型
     *
     * @param params
     * @param callback
     */
    public static void getOrderType(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/order/status", params, token, callback, null);
    }



 /**
     *
     *
     * @param params
     * @param callback
     */
    public static void getTransfer(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/terminal/pos/transfer", params, token, callback, null);
    }
    /**
     * 加入购物车
     *
     * @param params
     * @param callback
     */
    public static void getTrolley(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/shop/save/trolley", params, token, callback, null);
    }

 /**
     * 购物车列表
     *
     * @param params
     * @param callback
     */
    public static void getCarList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/shop/cart/list", params, token, callback, null);
    }

    /**
     * 删除购物车商品
     *
     * @param params
     * @param callback
     */
    public static void getDeleteList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/shop/del/comm", params, token, callback, null);
    }


    /**
     * 商户报件类型选择
     *
     * @param params
     * @param callback
     */
    public static void getAddMerchantMessageType(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1/AddMerchantMessageType", params, token, callback, null);
    }
 /**
     * 商户报件查询
     *
     * @param params
     * @param callback
     */
    public static void getEntry(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v1/merchant/query/entry", params, token, callback, null);
    }

/**
     * 商户报件上传
     *
     * @param params
     * @param callback
     */
    public static void getOperation(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1/Operation", params, token, callback, null);
    }


    /**
     * 新的商户报件上传
     *
     * @param params
     * @param callback
     */
    public static void getNewOperation(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1/insertMerchantEntry", params, token, callback, null);
    }


 /**
     * 提醒后台商户报件
     *
     * @param params
     * @param callback
     */
    public static void getNewOutOperation(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1/new/Operation", params, token, callback, null);
    }



    /**
     * 商户报件修改
     *
     * @param params
     * @param callback
     */
    public static void getUpdate(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v1/merchant/update", params, token, callback, null);
    }
    /**
     * 获取二维码生成的选项接口
     *
     * @param params
     * @param callback
     */
    public static void getBizTerminalRateList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1terminallist/getBizTerminalRateList", params, token, callback, null);
    }

    /**
     * 新增二维码生成接口
     *
     * @param params
     * @param callback
     */
    public static void postOpenAccount(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1terminallist/postOpenAccount", params, token, callback, null);
    }
    /**
     * 查看二维码生成接口
     *
     * @param params
     * @param callback
     */
    public static void getBizTerminalRateLists(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1terminallist/getBizTerminalRateLists", params, token, callback, null);
    }

    /**
     * 修改二维码生成接口
     *
     * @param params
     * @param callback
     */
    public static void putOpenAccount(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/posv1terminallist/putOpenAccount", params, token, callback, null);
    }


    /**
     * 修改伙伴费率生成接口
     *
     * @param params
     * @param callback
     */
    public static void putModifyRate(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/modifyRate", params, token, callback, null);
    }


 /**
     * 交易记录列表
     *
     * @param params
     * @param callback
     */
    public static void putRecordList(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "merchant/transactions/record", params, token, callback, null);
    }

 /**
     * 交易记录列表
     *
     * @param params
     * @param callback
     */
    public static void getv1terminallist(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.getRequest1(Urls.commUrls + "noauth/posv1terminallist", params, token, callback, null);
    }


 /**
     * 交易记录列表
     *
     * @param params
     * @param callback
     */
    public static void getEchoServer(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "noauth/echoServer", params, token, callback, null);
    }


/**
     *伙伴费率 回显
     *
     * @param params
     * @param callback
     */
    public static void getParntEchoServer(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v2/merchant/echoRate", params, token, callback, null);
    }

/**
     *商戶详情
     *
     * @param params
     * @param callback
     */
    public static void getQueryMyCommercialTenant(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/api/v1/merchant/queryMyCommercialTenant", params, token, callback, null);
    }

    /**
     * 查询是否签约接口
     *
     * @param params
     * @param callback
     */
    public static void getSigning(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/receiver/echoReceiver", params, token,callback, null);
    }

    /**
     * 签约接口
     *
     * @param params
     * @param callback
     */
    public static void getSigningTo(RequestParams params, String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/receiver/addReceiver", params, token,callback, null);
    }

    /**
     * 签约新接口接口
     *
     * @param params
     * @param callback
     */
    public static void postAddReceiverNew(RequestParams params, String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/receiver/addReceiverNew", params, token,callback, null);
    }


    /**
     * 签约-----新签约
     *
     * @param params
     * @param callback
     */
    public static void postSubmitCreateUser(RequestParams params,String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "xinLong/signed/withdrawal/submitCreateUser",params,token,callback, null);
    }

    /**
     * 获取新商户列表
     *
     * @param params
     * @param callback
     */
    public static void postFyMerch(RequestParams params,String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/get/myMerch", params,token,callback, null);
    }

    /**
     * 获取新费率
     *
     * @param params
     * @param callback
     */
    public static void posEchoFeeId(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/echoFeeId", params,token, callback, null);
    }

    /**
     * 获取省市区
     *
     * @param params
     * @param callback
     */
    public static void getArea(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.getRequest2(Urls.commUrls + "pos/merchantInfo/getArea", params, token,callback, null);
    }

    /**
     * PosP报件上传
     *
     * @param params
     * @param callback
     */
    public static void getPosPOperation(RequestParams params, String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/add/merchant/info", params,token,callback, null);
    }

    /**
     * 银行编码
     *
     * @param params
     * @param callback
     */
    public static void getBanks(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.getRequest2(Urls.commUrls + "pos/merchantInfo/banks", params,token, callback, null);
    }

    /**
     * 支行信息
     *
     * @param params
     * @param callback
     */
    public static void getBranchs(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.getRequest2(Urls.commUrls + "pos/merchantInfo/branchs", params, token,callback, null);
    }
    /**
     *  判断设备是否有押
     *
     * @param params
     * @param callback
     */
    public static void posJudgeCashPledge(RequestParams params,String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/judgeCashPledge", params,token, callback, null);
    }

    /**
     *  查询文件是否上传
     *
     * @param params
     * @param callback
     */
    public static void posWhetherPhotoVideo(RequestParams params,String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/whetherPhotoVideo", params,token,callback, null);
    }

    /**
     * 绑定设备
     *
     * @param params
     * @param callback
     */
    public static void posBindCode(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/binding/posCode", params,token,callback, null);
    }

    /**
     * 签名上传
     *
     * @param params
     * @param callback
     */
    public static void posPhotoUpload(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/photoUpload", params, token, callback, null);
    }

    /**
     * 签字提示语句
     *
     * @param params
     * @param callback
     */
    public static void posSignature(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/nuclearbody/signature", params, token, callback, null);
    }
    /**
     * 获取FaceID
     *
     * @param params
     * @param callback
     */
    public static void postFaceID(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/nuclearbody/getFaceID", params, token, callback, null);
    }
    /**
     * 视频上传
     *
     * @param params
     * @param callback
     */
    public static void posVideoUpload(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/videoUpload", params, token,callback, null);
    }
    /**
     * 获取详情信息
     *
     * @param params
     * @param callback
     */
    public static void posMerchantInfo(RequestParams params,String token, ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/getMerchantInfo", params, token,callback, null);
    }

    /**
     * 修改报件信息
     *
     * @param params
     * @param callback
     */
    public static void posMerchantEdit(RequestParams params,String token,ResponseCallback callback) {
        RequestMode.postRequest(Urls.commUrls + "pos/merchantInfo/update/merchant/info", params, token,callback, null);
    }
}
