package com.yc.education.controller.sale;

import com.github.pagehelper.PageInfo;
import com.yc.education.controller.BaseController;
import com.yc.education.model.sale.SaleOnlineOrder;
import com.yc.education.service.sale.ISaleOnlineOrderService;
import com.yc.education.util.AppConst;
import com.yc.education.util.StageManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class OnlineOrderQueryMiniController extends BaseController implements Initializable {

    @Autowired
    ISaleOnlineOrderService iSaleOnlineOrderService;

    @FXML VBox menu_first;           // 第一页
    @FXML VBox menu_prev;            // 上一页
    @FXML VBox menu_next;            // 下一页
    @FXML VBox menu_last;            // 最后一页


    @FXML CheckBox che_recently;
    @FXML TextField num;
    @FXML TextField text_query;

    @FXML Button client_sure;

    @FXML TableView tableView;

    @FXML TableColumn id;
    @FXML TableColumn order_no;
    @FXML TableColumn order_date;
    @FXML TableColumn customer_no;
    @FXML TableColumn customer_shortname;
    @FXML TableColumn order_people;
    @FXML TableColumn contact;
    @FXML TableColumn phone;



    // 订单编号
    private static String  orderid = "";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMenuValue(1);
    }

    /**
     * @Description 模糊查询
     * @Author BlueSky
     * @Date 15:59 2019/4/11
     **/
    @FXML
    public void textQuery(){
        setMenuValue(1);
    }

    /**
     * 给翻页菜单赋值
     * @param page
     */
    private void setMenuValue(int page){
        int rows = pageRows(che_recently,num);
        String text = text_query.getText();
        List<SaleOnlineOrder> saleOnlineOrderList = iSaleOnlineOrderService.listSaleOnlineOrderByPage(text,"",page, rows);
        if(saleOnlineOrderList != null && saleOnlineOrderList.size() >0){
            PageInfo<SaleOnlineOrder> pageInfo = new PageInfo<>(saleOnlineOrderList);
            menu_first.setUserData(pageInfo.getFirstPage());
            menu_prev.setUserData(pageInfo.getPrePage());
            menu_next.setUserData(pageInfo.getNextPage());
            menu_last.setUserData(pageInfo.getLastPage());
            loadData(saleOnlineOrderList);
        }else {
            tableView.setItems(null);
        }
    }


    /**
     * 分页
     * @param event
     */
    public void pages(MouseEvent event){
        Node node =(Node)event.getSource();
        if(node.getUserData() != null){
            int page =Integer.parseInt(String.valueOf(node.getUserData()));
            setMenuValue(page);
        }
    }

    /**
     * 初始化加载数据
     */
    private void loadData(List<SaleOnlineOrder> list){
        if(list != null){
            list.forEach(p->{
                p.setOrderDateStr(new SimpleDateFormat("yyyy-MM-dd").format(p.getOrderDate()));
            });
        }

        // 查询客户集合
        final ObservableList<SaleOnlineOrder> data = FXCollections.observableArrayList(list);
        id.setCellValueFactory(new PropertyValueFactory("id"));
        order_no.setCellValueFactory(new PropertyValueFactory("orderNo"));
        order_date.setCellValueFactory(new PropertyValueFactory("orderDateStr"));//映射
        customer_no.setCellValueFactory(new PropertyValueFactory("customerNo"));
        customer_shortname.setCellValueFactory(new PropertyValueFactory("customerNoStr"));
        order_people.setCellValueFactory(new PropertyValueFactory("orderPeople"));
        contact.setCellValueFactory(new PropertyValueFactory("contact"));
        phone.setCellValueFactory(new PropertyValueFactory("phone"));

        tableView.setItems(data);

        // 选择行 保存数据
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SaleOnlineOrder>() {
            @Override
            public void changed(ObservableValue<? extends SaleOnlineOrder> observableValue, SaleOnlineOrder oldItem, SaleOnlineOrder newItem) {
                if(newItem.getId() != null && !("".equals(newItem.getId()))){
                    OnlineOrderQueryMiniController.orderid = newItem.getId()+"";
                }
            }
        });

        tableView.setOnMouseClicked((MouseEvent t) -> {
            if (t.getClickCount() == 2) {
                closeSureWin();
            }
        });
    }

    //关闭当前窗体
    @FXML
    public void closeWin(){
        Stage stage=(Stage)client_sure.getScene().getWindow();
        StageManager.CONTROLLER.remove("OnlineOrderControllerNo");
        stage.close();
    }

    //确定并关闭当前窗体
    @FXML
    public void closeSureWin(){
        // 报价单
        OnlineOrderController online = (OnlineOrderController) StageManager.CONTROLLER.get("OnlineOrderControllerNo");
        if(online != null && orderid != null && !"".equals(orderid)){
            SaleOnlineOrder saleOnlineOrder = iSaleOnlineOrderService.selectByKey(Long.valueOf(orderid));
            if(saleOnlineOrder != null){
                online.setBasicVal(saleOnlineOrder);
            }
        }
        closeWin();
    }
}
