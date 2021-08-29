package com.ropes.controller;

import com.ropes.RopesMain;
import com.ropes.db.SqliteDAO;
import com.ropes.db.SqliteMain;
import com.ropes.models.PostsModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;
import javafx.event.Event;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    AnchorPane sideMenuArea;

    @FXML
    VBox sideMenuVBox;

    @FXML
    AnchorPane sideMenuButtonPane;

    @FXML
    VBox sideMenuButtonPaneVBox;

    @FXML
    TextField titleArea;

    @FXML
    TextArea contentArea;

    @FXML
    TextField contentAreaId;

    @FXML
    AnchorPane contentSearchArea;

    @FXML
    TextField searchTextField;

    //検索結果のキャレット位置のリスト
    public static List<Integer> indexList =  new ArrayList<Integer>();
    //前後ボタン用のインデックスキーを格納
    public static Integer targetIndexKey = 0;

    //1行コピー用の定数
    KeyEvent HOME = new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.HOME, false, false, false, false);
    KeyEvent SHIFT_END = new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.END, true, false, false, false);

    //記事全件取得
    @FXML
    public ObservableList getAllPosts() throws SQLException, ClassNotFoundException{
        ObservableList<PostsModel> posts = SqliteMain.getPosts();
        return posts;
    }

    //新規作成
    @FXML
    public void handleInsertPost() throws SQLException, ClassNotFoundException{
        String title = "no title";
        Integer res = SqliteMain.insertPost(title);
        if(res != null){
            loadSideMenu();
        }
    }

    //保存
    public void handleSavePost() throws SQLException, ClassNotFoundException{
        String rawPostId = contentAreaId.getText();
        if(rawPostId != ""){
            String title = titleArea.getText();
            String content = contentArea.getText();
            Integer postId = Integer.parseInt(rawPostId);
            SqliteDAO.updatePost(postId, title, content);
            loadSideMenu();
            addClassCurrentButton(postId);
        }
    }

    public void handleDeletePost() throws SQLException, ClassNotFoundException{
        Integer result = null;
        ObservableList<PostsModel> post = null;
        if(contentAreaId.getId() == null){
            return;
        }
        Integer postId = Integer.parseInt(contentAreaId.getId());
        if(postId != null){
            result = SqliteMain.removePost(postId);
        }
        if(result != null){
            loadSideMenu();
            titleArea.clear();
            contentAreaId.clear();
            contentArea.clear();
        }
        //何らかの先頭が削除されたら一つ前の記事をセット
        if(postId - 1 != 0){
            Integer previousId = postId - 1;
            post = SqliteMain.getPost(previousId);
            for(PostsModel p: post){
                titleArea.setText(p.getTitle());
                contentAreaId.setId(String.valueOf(p.getId()));
                contentAreaId.setText(String.valueOf(p.getId()));
                contentArea.setText(p.getContent());
            }
        }
    }

    //日時をショートカット入力
    public void handleInsertDate() throws SQLException, ClassNotFoundException {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
        String formattedDate = dateFormat.format(date);
        contentArea.appendText(formattedDate + "\n");
    }

    //1行コピー
    public void handleClip() throws SQLException, ClassNotFoundException{
        String str = contentArea.getSelectedText();
        if(str.isEmpty()){
            int anchor = contentArea.getAnchor();
            int caret = contentArea.getCaretPosition();
            Event.fireEvent(contentArea, HOME);
            Event.fireEvent(contentArea, SHIFT_END);
            contentArea.copy();
            contentArea.selectRange(anchor, caret);
        } else {
        }
    }

    //インデックスキーをボタン押下で取得
    private Integer getTargetIndexKey(Integer action){
        Integer index = null;
        if(action == 1){
            index = MainController.targetIndexKey + 1;
        } else if(action == -1){
            index = MainController.targetIndexKey - 1;
        }
        return index;
    }

    //クラス変数にインデックスキーを格納
    private void setTargetIndexKey(Integer indexKey){
        MainController.targetIndexKey = indexKey;
    }

    //テキストエリアにフォーカスしてキャレットを移動
    private void moveCaret(Integer caret){
        contentArea.requestFocus();
        contentArea.positionCaret(caret);
    }

    public void handlePrevIndex() {
        if(MainController.indexList != null){
            Integer indexKey = getTargetIndexKey(-1);
            if(indexKey <= 0 || indexKey == 0){
                Integer indexEnd = MainController.indexList.size() - 1;
                moveCaret(MainController.indexList.get(indexEnd));
                setTargetIndexKey(MainController.indexList.size());
            } else {
                moveCaret(MainController.indexList.get(indexKey));
                setTargetIndexKey(indexKey);
            }
        }
    }

    public void handleNextIndex() {
        if(MainController.indexList != null){
            Integer indexKey = getTargetIndexKey(1);
            if(MainController.indexList.size() > indexKey){
                moveCaret(MainController.indexList.get(indexKey));
                setTargetIndexKey(indexKey);
            } else {
                moveCaret(MainController.indexList.get(0));
                setTargetIndexKey(0);
            }
        }
    }

    //記事内検索
    //検索結果のindexのListをクラス変数に格納する
    //
    public void handleSearchContent() throws SQLException, ClassNotFoundException{
        MainController.indexList.clear();
        String searchWord = searchTextField.getText();
        String content = contentArea.getText();

        //indexOfで一致したインデックスを格納して一致しつづけるまで処理する
        if(content != null){
            Integer searchTextCount = searchWord.length();
            Integer flagCount = 0;
            Integer indexOfSub = 0;
            while(flagCount != -1){
                int ind = content.indexOf(searchWord, indexOfSub);
                if(ind != -1){
                    MainController.indexList.add(ind);
                    indexOfSub = (ind+searchTextCount - 1);
                } else {
                    flagCount = -1;
                }
            }
        }
        if(MainController.indexList != null){
            moveCaret(MainController.indexList.get(0));
            MainController.targetIndexKey = 0;
        }
    }

    public void handleVisibleSearchArea(){
        Boolean isVisible = contentSearchArea.isVisible();
        if(isVisible != true){
            contentSearchArea.setVisible(true);
        } else {
            contentSearchArea.setVisible(false);
        }
    }

    public void loadSideMenu(){
        sideMenuButtonPaneVBox.getChildren().clear();
        ObservableList<PostsModel> posts = null;
        try {
            posts = getAllPosts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //右クリック時のメニュー用
        ContextMenu sideNaviCon = new ContextMenu();
        MenuItem menuitem = new MenuItem("削除");
        menuitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleDeletePost();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        sideNaviCon.getItems().add(menuitem);

        if(posts != null){
            for(PostsModel post: posts){
                Button btn = new Button();
                btn.getStyleClass().add("leftnavi-title-button");
                btn.setText(post.getTitle());
                btn.setId(String.valueOf(post.getId()));
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        titleArea.setText(post.getTitle());
                        contentAreaId.setId(String.valueOf(post.getId()));
                        contentAreaId.setText(String.valueOf(post.getId()));
                        contentArea.setText(post.getContent());
                        addClassCurrentButton(post.getId());
                    }
                });
                btn.setContextMenu(sideNaviCon); //右クリック時のメニュー追加
                sideMenuButtonPaneVBox.getChildren().add(btn);
            }
        }
    }

    public void addClassCurrentButton(Integer id){
        //ボタン選択済みのクラスを当てるために一度全ボタンのクラスを除去
        ObservableList<Node> rawPane = sideMenuButtonPaneVBox.getChildren();
        for(Node p: rawPane){
            p.getStyleClass().remove("leftnavi-current-button");
        }
        for(Node p: rawPane){
            if(Integer.valueOf(p.getId()) == id ){
                p.getStyleClass().add("leftnavi-current-button");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        loadSideMenu();
        final Font f = Font.loadFont(RopesMain.class.getResource("font/NotoSansCJKjp-Thin.ttf").toExternalForm(), 15);
        titleArea.getStyleClass().add("titleArea");
        sideMenuArea.getStyleClass().add("sideMenuArea");
        searchTextField.getStyleClass().add("searchTextField");
    }
}
