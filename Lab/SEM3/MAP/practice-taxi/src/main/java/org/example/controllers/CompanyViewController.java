package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.service.OrderServiceI;


public class CompanyViewController {
    private OrderServiceI orderService;


    @FXML private TextField pickupField;
    @FXML private TextField destinationField;
    @FXML private TextField clientField;

    public void setOrderService(OrderServiceI orderService) {
        this.orderService = orderService;
    }

    @FXML
    private void handleAdd(){
        try{
            String pickup = pickupField.getText();
            String destination = destinationField.getText();
            String client = clientField.getText();

            orderService.add(pickup, destination, client);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            pickupField.setText("");
            destinationField.setText("");
            clientField.setText("");
        }
    }
}
