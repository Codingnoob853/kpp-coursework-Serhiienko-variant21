package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {

    private final HttpDeliveryClient apiClient = new HttpDeliveryClient();

    // Таблиці
    private TableView<Client> clientTable;
    private TableView<Courier> courierTable;
    private TableView<Shipment> shipmentTable;
    private TableView<DeliveryOrder> orderTable;

    // Поля форми Клієнтів
    private TextField clientNameField;
    private TextField clientPhoneField;
    private TextField clientEmailField;
    private TextField clientAddressField;
    private Long selectedClientId = null;

    // Поля форми Кур'єрів
    private TextField courierNameField;
    private TextField courierPhoneField;
    private TextField courierVehicleField;
    private TextField courierStatusField;
    private Long selectedCourierId = null;

    // Поля форми Відправлень
    private TextField shipmentTrackingField;
    private TextField shipmentWeightField;
    private TextField shipmentDescField;
    private TextField shipmentValueField;
    private Long selectedShipmentId = null;

    // Поля форми Замовлень
    private TextField orderClientIdField;
    private TextField orderCourierIdField;
    private TextField orderShipmentIdField;
    private TextField orderDateField;
    private TextField orderCostField;
    private Long selectedOrderId = null;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Courier Delivery Management Information System");

        TabPane tabPane = new TabPane();

        Tab clientTab = new Tab("Клієнти");
        clientTab.setContent(createClientView());
        clientTab.setClosable(false);

        Tab courierTab = new Tab("Кур'єри");
        courierTab.setContent(createCourierView());
        courierTab.setClosable(false);

        Tab shipmentTab = new Tab("Відправлення");
        shipmentTab.setContent(createShipmentView());
        shipmentTab.setClosable(false);

        Tab orderTab = new Tab("Замовлення");
        orderTab.setContent(createOrderView());
        orderTab.setClosable(false);

        tabPane.getTabs().addAll(clientTab, courierTab, shipmentTab, orderTab);

        Scene scene = new Scene(tabPane, 1000, 700);
        stage.setScene(scene);
        stage.show();

        // Початкове завантаження даних
        loadClients();
        loadCouriers();
        loadShipments();
        loadOrders();
    }

    // --- Вкладка: Клієнти ---
    private VBox createClientView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        clientTable = new TableView<>();
        TableColumn<Client, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Client, String> nameCol = new TableColumn<>("ПІБ");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<Client, String> phoneCol = new TableColumn<>("Телефон");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<Client, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Client, String> addressCol = new TableColumn<>("Адреса");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        clientTable.getColumns().addAll(idCol, nameCol, phoneCol, emailCol, addressCol);

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedClientId = newVal.getId();
                clientNameField.setText(newVal.getFullName());
                clientPhoneField.setText(newVal.getPhone());
                clientEmailField.setText(newVal.getEmail());
                clientAddressField.setText(newVal.getAddress());
            }
        });

        clientNameField = new TextField(); clientNameField.setPromptText("ПІБ");
        clientPhoneField = new TextField(); clientPhoneField.setPromptText("Телефон");
        clientEmailField = new TextField(); clientEmailField.setPromptText("Email");
        clientAddressField = new TextField(); clientAddressField.setPromptText("Адреса");

        Button addBtn = new Button("Додати"); addBtn.setOnAction(e -> addClient());
        Button updateBtn = new Button("Змінити"); updateBtn.setOnAction(e -> updateClient());
        Button deleteBtn = new Button("Видалити"); deleteBtn.setOnAction(e -> deleteClient());
        Button refreshBtn = new Button("Оновити"); refreshBtn.setOnAction(e -> loadClients());

        HBox buttonsBox = new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn);
        VBox formBox = new VBox(5, new Label("Форма клієнта:"), clientNameField, clientPhoneField, clientEmailField, clientAddressField, buttonsBox);

        vbox.getChildren().addAll(clientTable, formBox);
        return vbox;
    }

    private void loadClients() {
        try { clientTable.getItems().setAll(apiClient.getClients()); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void addClient() {
        try { apiClient.addClient(clientNameField.getText(), clientPhoneField.getText(), clientEmailField.getText(), clientAddressField.getText()); clearClientForm(); loadClients(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void updateClient() {
        if (selectedClientId == null) return;
        try { apiClient.updateClient(selectedClientId, clientNameField.getText(), clientPhoneField.getText(), clientEmailField.getText(), clientAddressField.getText()); clearClientForm(); loadClients(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void deleteClient() {
        if (selectedClientId == null) return;
        try { apiClient.deleteClient(selectedClientId); clearClientForm(); loadClients(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void clearClientForm() { selectedClientId = null; clientNameField.clear(); clientPhoneField.clear(); clientEmailField.clear(); clientAddressField.clear(); }


    // --- Вкладка: Кур'єри ---
    private VBox createCourierView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        courierTable = new TableView<>();
        TableColumn<Courier, Long> idCol = new TableColumn<>("ID"); idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Courier, String> nameCol = new TableColumn<>("ПІБ"); nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<Courier, String> phoneCol = new TableColumn<>("Телефон"); phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<Courier, String> vehicleCol = new TableColumn<>("Транспорт"); vehicleCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        TableColumn<Courier, String> statusCol = new TableColumn<>("Статус"); statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        courierTable.getColumns().addAll(idCol, nameCol, phoneCol, vehicleCol, statusCol);

        courierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCourierId = newVal.getId();
                courierNameField.setText(newVal.getFullName());
                courierPhoneField.setText(newVal.getPhone());
                courierVehicleField.setText(newVal.getVehicleType());
                courierStatusField.setText(newVal.getStatus());
            }
        });

        courierNameField = new TextField(); courierNameField.setPromptText("ПІБ кур'єра");
        courierPhoneField = new TextField(); courierPhoneField.setPromptText("Телефон");
        courierVehicleField = new TextField(); courierVehicleField.setPromptText("Тип транспорту");
        courierStatusField = new TextField(); courierStatusField.setPromptText("Статус");

        Button addBtn = new Button("Додати"); addBtn.setOnAction(e -> addCourier());
        Button deleteBtn = new Button("Видалити"); deleteBtn.setOnAction(e -> deleteCourier());
        Button refreshBtn = new Button("Оновити"); refreshBtn.setOnAction(e -> loadCouriers());

        HBox buttonsBox = new HBox(10, addBtn, deleteBtn, refreshBtn);
        VBox formBox = new VBox(5, new Label("Форма кур'єра:"), courierNameField, courierPhoneField, courierVehicleField, courierStatusField, buttonsBox);

        vbox.getChildren().addAll(courierTable, formBox);
        return vbox;
    }

    private void loadCouriers() {
        try { courierTable.getItems().setAll(apiClient.getCouriers()); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void addCourier() {
        try { apiClient.addCourier(courierNameField.getText(), courierPhoneField.getText(), courierVehicleField.getText(), courierStatusField.getText()); clearCourierForm(); loadCouriers(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void deleteCourier() {
        if (selectedCourierId == null) return;
        try { apiClient.deleteCourier(selectedCourierId); clearCourierForm(); loadCouriers(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void clearCourierForm() { selectedCourierId = null; courierNameField.clear(); courierPhoneField.clear(); courierVehicleField.clear(); courierStatusField.clear(); }


    // --- Вкладка: Відправлення ---
    private VBox createShipmentView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        shipmentTable = new TableView<>();
        TableColumn<Shipment, Long> idCol = new TableColumn<>("ID"); idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Shipment, String> trackingCol = new TableColumn<>("Трекінг-номер"); trackingCol.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        TableColumn<Shipment, Double> weightCol = new TableColumn<>("Вага (кг)"); weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        TableColumn<Shipment, String> descCol = new TableColumn<>("Опис"); descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Shipment, Double> valueCol = new TableColumn<>("Оголошена вартість"); valueCol.setCellValueFactory(new PropertyValueFactory<>("declaredValue"));

        shipmentTable.getColumns().addAll(idCol, trackingCol, weightCol, descCol, valueCol);

        shipmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedShipmentId = newVal.getId();
                shipmentTrackingField.setText(newVal.getTrackingNumber());
                shipmentWeightField.setText(String.valueOf(newVal.getWeight()));
                shipmentDescField.setText(newVal.getDescription());
                shipmentValueField.setText(String.valueOf(newVal.getDeclaredValue()));
            }
        });

        shipmentTrackingField = new TextField(); shipmentTrackingField.setPromptText("Трекінг-номер");
        shipmentWeightField = new TextField(); shipmentWeightField.setPromptText("Вага (кг)");
        shipmentDescField = new TextField(); shipmentDescField.setPromptText("Опис");
        shipmentValueField = new TextField(); shipmentValueField.setPromptText("Оголошена вартість");

        Button addBtn = new Button("Додати"); addBtn.setOnAction(e -> addShipment());
        Button deleteBtn = new Button("Видалити"); deleteBtn.setOnAction(e -> deleteShipment());
        Button refreshBtn = new Button("Оновити"); refreshBtn.setOnAction(e -> loadShipments());

        HBox buttonsBox = new HBox(10, addBtn, deleteBtn, refreshBtn);
        VBox formBox = new VBox(5, new Label("Форма відправлення:"), shipmentTrackingField, shipmentWeightField, shipmentDescField, shipmentValueField, buttonsBox);

        vbox.getChildren().addAll(shipmentTable, formBox);
        return vbox;
    }

    private void loadShipments() {
        try { shipmentTable.getItems().setAll(apiClient.getShipments()); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void addShipment() {
        try {
            apiClient.addShipment(shipmentTrackingField.getText(),
                    Double.parseDouble(shipmentWeightField.getText()),
                    shipmentDescField.getText(),
                    Double.parseDouble(shipmentValueField.getText()));
            clearShipmentForm(); loadShipments();
        } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void deleteShipment() {
        if (selectedShipmentId == null) return;
        try { apiClient.deleteShipment(selectedShipmentId); clearShipmentForm(); loadShipments(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }
    private void clearShipmentForm() { selectedShipmentId = null; shipmentTrackingField.clear(); shipmentWeightField.clear(); shipmentDescField.clear(); shipmentValueField.clear(); }


    // --- Вкладка: Замовлення ---
    private VBox createOrderView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        orderTable = new TableView<>();
        TableColumn<DeliveryOrder, Long> idCol = new TableColumn<>("ID"); idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<DeliveryOrder, Long> clientCol = new TableColumn<>("ID Клієнта"); clientCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        TableColumn<DeliveryOrder, Long> courierCol = new TableColumn<>("ID Кур'єра"); courierCol.setCellValueFactory(new PropertyValueFactory<>("courierId"));
        TableColumn<DeliveryOrder, Long> shipmentCol = new TableColumn<>("ID Відправлення"); shipmentCol.setCellValueFactory(new PropertyValueFactory<>("shipmentId"));
        TableColumn<DeliveryOrder, String> dateCol = new TableColumn<>("Дата"); dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        TableColumn<DeliveryOrder, Double> costCol = new TableColumn<>("Вартість"); costCol.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        TableColumn<DeliveryOrder, String> statusCol = new TableColumn<>("Статус"); statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTable.getColumns().addAll(idCol, clientCol, courierCol, shipmentCol, dateCol, costCol, statusCol);

        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedOrderId = newVal.getId();
                orderClientIdField.setText(String.valueOf(newVal.getClientId()));
                orderCourierIdField.setText(String.valueOf(newVal.getCourierId()));
                orderShipmentIdField.setText(String.valueOf(newVal.getShipmentId()));
                orderDateField.setText(newVal.getOrderDate());
                orderCostField.setText(String.valueOf(newVal.getTotalCost()));
            }
        });

        orderClientIdField = new TextField(); orderClientIdField.setPromptText("ID Клієнта");
        orderCourierIdField = new TextField(); orderCourierIdField.setPromptText("ID Кур'єра");
        orderShipmentIdField = new TextField(); orderShipmentIdField.setPromptText("ID Відправлення");
        orderDateField = new TextField(); orderDateField.setPromptText("Дата (наприклад, 2026-11-04)");
        orderCostField = new TextField(); orderCostField.setPromptText("Загальна вартість");

        Button addBtn = new Button("Додати"); addBtn.setOnAction(e -> addOrder());
        Button deleteBtn = new Button("Видалити"); deleteBtn.setOnAction(e -> deleteOrder());
        Button refreshBtn = new Button("Оновити"); refreshBtn.setOnAction(e -> loadOrders());

        // Кнопка для тестування послідовного CSV-імпорту (Пункти 10.3 - 10.9)
        Button importCsvBtn = new Button("Імпорт CSV");
        importCsvBtn.setOnAction(e -> {
            apiClient.importDeliveriesCsv();
            loadOrders(); // Оновлюємо таблицю замовлень після імпорту
        });

        HBox buttonsBox = new HBox(10, addBtn, deleteBtn, refreshBtn, importCsvBtn);
        VBox formBox = new VBox(5, new Label("Форма замовлення:"), orderClientIdField, orderCourierIdField, orderShipmentIdField, orderDateField, orderCostField, buttonsBox);

        vbox.getChildren().addAll(orderTable, formBox);
        return vbox;
    }

    private void loadOrders() {
        try { orderTable.getItems().setAll(apiClient.getOrders()); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }

    private void addOrder() {
        try {
            apiClient.addOrder(
                    Long.parseLong(orderClientIdField.getText()),
                    Long.parseLong(orderCourierIdField.getText()),
                    Long.parseLong(orderShipmentIdField.getText()),
                    orderDateField.getText(),
                    Double.parseDouble(orderCostField.getText())
            );
            clearOrderForm(); loadOrders();
        } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }

    private void deleteOrder() {
        if (selectedOrderId == null) return;
        try { apiClient.deleteOrder(selectedOrderId); clearOrderForm(); loadOrders(); } catch (Exception ex) { showError("Помилка", ex.getMessage()); }
    }

    private void clearOrderForm() {
        selectedOrderId = null;
        orderClientIdField.clear();
        orderCourierIdField.clear();
        orderShipmentIdField.clear();
        orderDateField.clear();
        orderCostField.clear();
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}