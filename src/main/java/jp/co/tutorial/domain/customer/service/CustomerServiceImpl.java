package jp.co.tutorial.domain.customer.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import jp.co.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import jp.co.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import jp.co.intra_mart.common.platform.log.Logger;
import jp.co.intra_mart.foundation.BaseUrl;
import jp.co.intra_mart.foundation.context.Contexts;
import jp.co.intra_mart.foundation.context.model.AccountContext;
import jp.co.intra_mart.foundation.exception.BizApiException;
import jp.co.intra_mart.foundation.i18n.datetime.format.AccountDateTimeFormatter;
import jp.co.intra_mart.foundation.i18n.datetime.format.DateTimeFormatIds;
import jp.co.intra_mart.foundation.i18n.datetime.format.DateTimeFormatterException;
import jp.co.intra_mart.foundation.master.user.UserManager;
import jp.co.intra_mart.foundation.master.user.model.User;
import jp.co.intra_mart.foundation.master.user.model.UserBizKey;
import jp.co.intra_mart.foundation.security.exception.AccessSecurityException;
import jp.co.intra_mart.foundation.security.message.MessageManager;
import jp.co.intra_mart.imbox.exception.IMBoxException;
import jp.co.intra_mart.imbox.model.Entry4NoticeMessage;
import jp.co.intra_mart.imbox.service.ApplicationBoxService;
import jp.co.intra_mart.imbox.service.Services;
import jp.co.tutorial.domain.customer.model.CustomerDto;
import jp.co.tutorial.domain.customer.model.MSampleCustomer;
import jp.co.tutorial.domain.customer.file.repository.CustomerFileRepository;
import jp.co.tutorial.domain.customer.repository.MSampleCustomerRepository;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.SystemException;

/**
 * customerService実装クラス.
 *
 * @author intra-mart
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    /** リポジトリの定義. */
    @Inject
    private MSampleCustomerRepository customerRepository;
    @Inject
    private CustomerFileRepository customerFileRepository;

    /** ロガーの生成. */
    private static final Logger LOGGER_CONSOLE = Logger.getLogger("CUSTOMER_LOG_CONSOLE");
    private static final Logger LOGGER_FILE = Logger.getLogger("CUSTOMER_LOG_FILE");

    /**
     * 顧客数の取得.
     *
     * @return 顧客数
     */
    @Override
    public final int countCustomer() {
        return (int) customerRepository.getCount();
    }

    /**
     * 顧客情報リストの取得.
     *
     * @param sortIndex ソート対象
     * @param sortOrder 昇順/降順
     * @param offset 取得開始レコード
     * @param limit 取得件数
     * @return 顧客情報リスト
     */
    @Override
    public final List<CustomerDto> getCustomerList(
        final String sortIndex, final String sortOrder,
        final int offset, final int limit) {

        RowBounds rowBounds = new RowBounds(offset, limit);
        final List<MSampleCustomer> resultList
            = customerRepository.getCustomerData(sortIndex + "  " + sortOrder, rowBounds);
        if (resultList == null) {
            return Collections.emptyList();
        }

        final List<CustomerDto> customerList = new ArrayList<CustomerDto>();
        // 顧客情報をDtoに設定
        for (final MSampleCustomer customer : resultList) {
            final CustomerDto customerDto = entityToDto(customer);
            customerList.add(customerDto);
        }
        return customerList;
    }

    /**
     * 顧客CDを条件に顧客情報を取得.
     *
     * @param customerCd 顧客CD
     * @return 顧客情報DTO
     */
    @Override
    public final CustomerDto searchCustomer(final String customerCd) {

        final MSampleCustomer mSampleCustomer =
                customerRepository.findByCustomerCd(customerCd);

        if (mSampleCustomer != null) {

            // 顧客情報をDtoに設定
            final CustomerDto customerDto = entityToDto(mSampleCustomer);

            return customerDto;
        }
        return null;
    }

    /**
     * 顧客情報の登録.
     *
     * @param customerDto 顧客情報
     * @param file 参考資料
     */
    @Override
    @Transactional
    public final void createCustomer(final CustomerDto customerDto, final MultipartFile file) {

        // 顧客情報をEntityに設定
        final MSampleCustomer mSampleCustomer = dtoToEntity(customerDto);

        // 登録
        customerRepository.insert(mSampleCustomer);

        if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")) {
            // ファイルアップロード
            customerFileRepository.uploadFile(customerDto.getCustomerCd(), file);
        }

        // IMBOX連携
        linkIMBox(customerDto);

        // LOGGER_CONSOLEでログの出力
        // infoレベルで出力
        LOGGER_CONSOLE.info("顧客コード:{}の顧客情報を登録しました。", customerDto.getCustomerCd());
        // debugレベルで出力
        LOGGER_CONSOLE.debug("エラー:なし");

        // CUSTOMER_LOG_FILEでログの出力
        // infoレベルで出力
        LOGGER_FILE.info("顧客コード:{}の顧客情報を登録しました。", customerDto.getCustomerCd());
        // debugレベルで出力
        LOGGER_FILE.debug("エラー:なし");

        return;
    }

    /**
     * 顧客情報の更新.
     *
     * @param customerDto　顧客情報
     * @param file 参考資料
     */
    @Override
    @Transactional
    public final void updateCustomer(final CustomerDto customerDto, final MultipartFile file) {

        // 顧客情報をEntityに設定
        final MSampleCustomer mSampleCustomer = dtoToEntity(customerDto);

        // 更新
        customerRepository.update(mSampleCustomer);

        if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")) {
            // アップロード済みファイルの削除
            customerFileRepository.deleteFile(customerDto.getCustomerCd());
            // ファイルアップロード
            customerFileRepository.uploadFile(customerDto.getCustomerCd(), file);
        }
    }

    /**
     * 顧客情報の削除.
     *
     * @param customerCd 顧客コード
     */
    @Override
    @Transactional
    public final void deleteCustomer(final String customerCd) {

        final MSampleCustomer mSampleCustomer = new MSampleCustomer();
        mSampleCustomer.setCustomerCd(customerCd);

        // 削除
        customerRepository.delete(mSampleCustomer);

        // ファイルの削除
        customerFileRepository.deleteFile(customerCd);
    }

    /**
     * 担当者の名前を取得.
     *
     * @param chargeStfCd 担当者コード
     * @return 担当者名
     */
    @Override
    public final String getChargeStfName(final String chargeStfCd) {
        String chargeStfName = "";
        if (chargeStfCd != null && !chargeStfCd.equals("")) {
            try {
                final AccountContext accountContext = Contexts.get(AccountContext.class);
                final UserBizKey bizKey = new UserBizKey();
                bizKey.setUserCd(chargeStfCd);
                final UserManager userManager = new UserManager();
                final User user = userManager.getUser(bizKey, new Date(), accountContext.getLocale());
                if (user != null) {
                    chargeStfName = user.getUserName();
                }
            } catch (final BizApiException e) {
                throw new BusinessException(null, e);
            }
        }
        return chargeStfName;
    }

    /**
     * 新規登録した顧客情報をIMBOXへ送信.
     * @param customerDto 顧客情報
     */
    @Override
    public final void linkIMBox(final CustomerDto customerDto) {

        Entry4NoticeMessage entry4NoticeMessage = new Entry4NoticeMessage();
        entry4NoticeMessage.setMessageText("新規顧客を登録しました."
                 + "顧客コード：" + customerDto.getCustomerCd());
        entry4NoticeMessage.setMessageTypeCd("MESSAGE_TYPE_MESSAGE");
        entry4NoticeMessage.setSendUserCd(Contexts.get(AccountContext.class).getUserCd());
        entry4NoticeMessage.setApplicationCd("tutorial");
        entry4NoticeMessage.setUri(getBaseUrl() + "/customer/editForm/"
                + customerDto.getCustomerCd());
        entry4NoticeMessage.setUriTitle("顧客詳細");
        String[] userCds = {"ueda"};
        ApplicationBoxService applicationBoxService = Services.get(ApplicationBoxService.class);
        try {
            applicationBoxService.sendNoticeMessage(entry4NoticeMessage, userCds);
        } catch (final IMBoxException e) {
            System.out.println("IMBOXの登録に失敗しました");
        }
    }

    /**
     * 顧客情報をDTOからEntityに入れ替え.
     *
     * @param customerDto 顧客情報
     * @return MSampleCustomer 顧客情報
     */
    private MSampleCustomer dtoToEntity(final CustomerDto customerDto) {

        final MSampleCustomer mSampleCustomer = new MSampleCustomer();

        // 顧客CD
        mSampleCustomer.setCustomerCd(customerDto.getCustomerCd());
        // 顧客名
        mSampleCustomer.setCustomerName(customerDto.getCustomerName());
        // 電話番号
        mSampleCustomer.setCustomerTelno(customerDto.getCustomerTelno());
        // 住所
        mSampleCustomer.setCustomerAddress(customerDto.getCustomerAddress());
        // 性別
        mSampleCustomer.setCustomerSex(customerDto.getCustomerSex());
        // 生年月日
        if (customerDto.getCustomerBirthday() != null
                && !customerDto.getCustomerBirthday().equals("")) {
            try {
                mSampleCustomer.setCustomerBirthday(
                                AccountDateTimeFormatter.parse(
                                customerDto.getCustomerBirthday(), Date.class,
                                DateTimeFormatIds.IM_DATETIME_FORMAT_DATE_INPUT));
            } catch (DateTimeFormatterException e) {
                e.printStackTrace();
            }
        }
        // 添付ファイル名
        mSampleCustomer.setAttachmentFile(customerDto.getAttachmentFile());
        // 担当者
        mSampleCustomer.setChargeStfCd(customerDto.getChargeStfCd());
        // 更新日付
        mSampleCustomer.setUpdateDate(new Timestamp(System.currentTimeMillis()));

        return mSampleCustomer;

    }

    /**
     * 顧客情報をEntityからDTOに入れ替え.
     *
     * @param mSampleCustomer 顧客情報
     * @return CustomerDto 顧客情報
     */
    private CustomerDto entityToDto(final MSampleCustomer mSampleCustomer) {

        final CustomerDto customerDto = new CustomerDto();

        // 顧客CD
        customerDto.setCustomerCd(mSampleCustomer.getCustomerCd());
        // 顧客名
        customerDto.setCustomerName(mSampleCustomer.getCustomerName());
        // 電話番号
        customerDto.setCustomerTelno(mSampleCustomer.getCustomerTelno());
        // 住所
        customerDto.setCustomerAddress(mSampleCustomer.getCustomerAddress());
        // 性別
        customerDto.setCustomerSex(mSampleCustomer.getCustomerSex());
        // 生年月日
        if (mSampleCustomer.getCustomerBirthday() != null) {
            customerDto.setCustomerBirthday(AccountDateTimeFormatter.format(
                        mSampleCustomer.getCustomerBirthday(),
                        DateTimeFormatIds.IM_DATETIME_FORMAT_DATE_INPUT));
        }
        // 添付ファイル
        customerDto.setAttachmentFile(mSampleCustomer.getAttachmentFile());
        // 担当者
        customerDto.setChargeStfCd(mSampleCustomer.getChargeStfCd());

        return customerDto;

    }

    /**
     * メッセージの取得.
     *
     * @param key メッセージキー
     * @return メッセージ
     */
    @Override
    public final String getMessage(final String key) {

        try {
            return MessageManager.getInstance().getMessage(key);
        } catch (final AccessSecurityException e) {
            throw new SystemException(null, e);
        }
    }

    /**
     * ベースURLの取得.
     *
     * @return ベースURL
     */
    public final String getBaseUrl() {

        HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
        HttpServletRequest request = httpContext.getRequest();
        String baseUrl = BaseUrl.get(request);

        return baseUrl;
    }

}
