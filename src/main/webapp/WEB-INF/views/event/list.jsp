<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="imui" uri="http://www.intra-mart.co.jp/taglib/imui" %>
<%@ taglib prefix="f" uri="http://terasoluna.org/functions" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<imui:head>
  <title>イベントマスタメンテナンス - JavaEE開発モデル -</title>

  <script type="text/javascript">

  function editFormSubmit(eventId) {
    $('#editForm').attr("action","event/detail/" + eventId );
    $('#editForm').submit();
    return false;
  };

  </script>
</imui:head>

<!-- タイトルバー -->
<div class="imui-title-small-window">
  <h1>イベント情報一覧</h1>
</div>

<!-- ツールバー -->
<div class="imui-toolbar-wrap">
  <div class="imui-toolbar-inner">
    <ul class="imui-list-toolbar">
      <li>
        <a href="event/createForm" id="create">
          <span class="im-ui-icon-common-16-new mr-5"></span>新規登録
        </a>
      </li>
    </ul>
  </div>
</div>

<div class="imui-form-container">
  <header class="imui-chapter-title">
    <h2>イベント情報</h2>
  </header>

  <!-- 「3 一覧画面」を実装しましょう -->
  <imui:listTable id="searchList" process="java" 
                  target="jp.co.tutorial.app.event.EventListTableProcessor" 
                  viewRecords="true" page="${f:h(eventListForm.page)}" 
                  sortName="${f:h(eventListForm.sortName)}" 
                  sortOrder="${f:h(eventListForm.sortOrder)}" 
                  autoEncode="true" height="100%">
    <pager rowNum="${f:h(eventListForm.rowNum)}" 
           rowList="${f:h(eventListForm.pagerRowList)}" />
    <cols>
      <col name="detail" caption="詳細" sortable="false" width="40" align="center">
        <callFunction name="editFormSubmit" />
      </col>
      <col name="eventId" caption="イベントID" key="true" hidden="true" />
      <col name="eventName" caption="イベント名" />
      <col name="eventDate" caption="開催日" />
    </cols>
  </imui:listTable>
</div>

<!-- 詳細アイコンが押下された場合に利用されるformです -->
<form name="editForm" id="editForm" method="POST" action="event/createForm">
</form>
