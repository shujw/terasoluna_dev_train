<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="imui" uri="http://www.intra-mart.co.jp/taglib/imui" %>
<%@ taglib prefix="f" uri="http://terasoluna.org/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<imui:head>
  <title>顧客マスタメンテナンス - JavaEE開発モデル -</title>

  <script type="text/javascript">
    function editFormSubmit(customerCd) {
      $('#editForm').attr("action","customer/editForm/" + customerCd );
      $('#editForm').submit();
      return false;
    };
  </script>
</imui:head>

<!-- タイトルバー -->
<div class="imui-title-small-window">
  <h1>顧客情報一覧</h1>
</div>

<!-- ツールバー -->
<div class="imui-toolbar-wrap">
  <div class="imui-toolbar-inner">
    <ul class="imui-list-toolbar">
      <li>
        <a href="customer/createForm" id="create">
          <span class="im-ui-icon-common-16-new mr-5"></span>新規登録
        </a>
      </li>
    </ul>
  </div>
</div>

<div class="imui-form-container">
  <header class="imui-chapter-title">
    <h2>顧客情報</h2>
  </header>

  <imui:listTable id="searchList" process="java" 
                  target="jp.co.tutorial.app.customer.CustomerListTableProcessor" 
                  viewRecords="true" page="${customerListForm.page}" 
                  sortName="${customerListForm.sortName}" 
                  sortOrder="${customerListForm.sortOrder}" 
                  autoEncode="true" height="100%">
    <pager rowNum="${customerListForm.rowNum}" 
           rowList="${customerListForm.pagerRowList}" />
    <cols>
      <col name="edit" caption="編集" sortable="false" width="40" align="center">
        <callFunction name="editFormSubmit" />
      </col>
      <col name="customerCd" caption="顧客コード" key="true" />
      <col name="customerName" caption="顧客名" />
      <col name="chargeStf" caption="担当者" sortable="false" />
    </cols>
  </imui:listTable>
</div>

<!-- 編集アイコンが押下された場合に利用されるformです -->
<form name="editForm" id="editForm" method="POST" action="">
</form>
