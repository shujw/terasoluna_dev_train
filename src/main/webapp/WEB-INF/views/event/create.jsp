<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="imui" uri="http://www.intra-mart.co.jp/taglib/imui" %>
<%@ taglib prefix="imart" uri="http://www.intra-mart.co.jp/taglib/core/standard" %>
<%@ taglib prefix="im" uri="http://www.intra-mart.co.jp/taglib/im-tenant" %>
<%@ taglib prefix="im-master" uri="http://www.intra-mart.co.jp/taglib/im-master" %>
<%@ taglib prefix="f" uri="http://terasoluna.org/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<imui:head>
  <title>イベントマスタメンテナンス- JavaEE開発モデル -</title>

  <script src="ui/libs/jquery-validation-1.9.0/jquery.validate.js"></script>
  <im-master:imACMSearch />

  <script type="text/javascript">
    jQuery(function(){
      /* 「4.1 登録画面」 入力チェックのルールとメッセージを実装しましょう */
      // 入力検証ルールのオブジェクト
      var rules = {
        eventName : {
          required  : true,
          maxlength : 25
        },
        eventDetail : {
          maxlength : 50
        },
        eventDate : {
          date : true,
        },
        entryUser : {
          maxlength : 200
        }
      };
      // エラーメッセージのセット
      var messages = {
        eventName : {
          required  : 'イベント名が未入力です',
          maxlength : 'イベント名は25文字以下で入力してください'
        },
        eventDetail : {
          maxlength: 'イベント内容は50文字以下で入力してください'
        },
        eventDate : {
          date   : '開催日は(yyyy/MM/dd)の形式で入力してください'
        },
        entryUser : {
          maxlength : '参加者は200文字以下で入力して下さい'
        }
      };

      /* 「4.1 登録画面」 登録ボタンクリック時の処理を実装しましょう */
      $('#create').click(function(){
        if (!imuiValidate('#eventForm', rules, messages)) return;
        $('#eventForm').submit();
        return false;
      });
      $('#entryUserSelector').click(function(){
        var parameter = {
          tabs : [{
            id    : 'jp.co.intra_mart.master.app.search.tabs.user.tree_department',
            title : '会社組織ツリー'
          }],
          prop : {
            'jp.co.intra_mart.master.app.search.tabs.user.tree_department' : ['user_cd', 'user_name']
          },
          callback_function  : 'setUser',
          basic_area         : 'jp.co.intra_mart.master.app.search.headers.readonly',
          wnd_title          : 'ユーザ検索',
          message            : 'ユーザ検索',
          wnd_close          : true,
          // type               : 'single',
          type               : 'multiple',
          deleted_data       : false,
          target_locale      : 'ja',
          additional_disp    : false
        };

        // 検索画面を開く
        imACMSearch.open(parameter);
      });
    });

    function setUser(object){
      if(object.length == 0){
        return;
      }
      // var user = object[0].data;
      // $('#entryUserName').val(user.user_name);
      // $('#entryUser').val(user.user_cd);
      var userNameList = new Array();
      var userCdList =  new Array();
      for(var i = 0; i < object.length; i++) {
        var user = object[i].data;
        userNameList.push(user.user_name);
        userCdList.push(user.user_cd);
      }
      $('#entryUserName').val(userNameList);
      $('#entryUser').val(userCdList);
    }

  </script>
</imui:head>

<!-- タイトルバー -->
<div class="imui-title-small-window">
  <h1>イベントマスタメンテナンス  - JavaEE開発モデル -</h1>
</div>

<!-- ツールバー -->
<div class="imui-toolbar-wrap">
  <div class="imui-toolbar-inner">
    <ul class="imui-list-toolbar">
      <li>
        <a href="event/list" id="back">
          <span class="im-ui-icon-common-16-back"></span>
        </a>
      </li>
    </ul>
  </div>
</div>

<div class="imui-form-container">
  <header class="imui-chapter-title">
    <h2>イベント情報詳細</h2>
  </header>
  <form:form name="eventForm" modelAttribute="eventForm" action="event/create" method="POST">
    <font color="#FF0000"><b><form:errors path="*" element="div" /></b></font>
    <table class="imui-form">
      <tbody>
        <!-- 「4.1 登録画面」 登録項目を実装しましょう -->
        <tr>
          <th><label class="imui-required">イベント名</label></th>
          <td><imui:textbox id="eventName" name="eventName" value="${eventForm.eventName}" size="50"/></td>
        </tr>
        <tr>
          <th><label>イベント内容</label></th>
          <td>
            <imui:textArea id="eventDetail" name="eventDetail" value="${eventForm.eventDetail}" rows="3" cols="40"/>
          </td>
        </tr>
        <tr>
          <th><label>開催日</label></th>
          <td>
            <imui:textbox id="eventDate" name="eventDate" value="${eventForm.eventDate}" size="50"/>
            <im:calendar floatable="true" altField="#eventDate" changeYear="true" changeMonth="true"/>
          </td>
        </tr>
        <tr>
          <th><label>参加者</label></th>
          <td>
            <!-- 
            <imui:textbox id="entryUser" name="entryUser" value="${eventForm.entryUser}" size="50" />
             -->
            <imui:textbox id="entryUserName" name="entryUserName" size="50" value="${eventForm.entryUserName}" readonly />
            <input type="hidden" id="entryUser" name="entryUser" value="${f:h(eventForm.entryUser)}"/>
            <imui:button value="検索" id="entryUserSelector" name="entryUserSelector" class="imui-small-button" />
          </td>
        </tr>
      </tbody>
    </table>
    <div class="imui-operation-parts">
      <imui:button value="登録" id="create" name="create" class="imui-large-button" escapeXml="true" escapeJs="false" />
    </div>
  </form:form>
</div>
