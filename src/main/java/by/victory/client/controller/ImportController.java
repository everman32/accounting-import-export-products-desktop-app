package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.validation.RegexValidateBehavior;
import by.victory.client.model.Import;
import by.victory.client.validation.Pattern;
import by.victory.client.validation.TextFieldListener;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ImportController implements SocketBehavior, ComponentBehavior, ResponseBehavior, RegexValidateBehavior {
    private static final byte[] REQUEST_GET_IMPORT = "{\"reqCode\":\"getImport\"}".getBytes();
    private static final byte[] REQUEST_GET_ASSESSMENTS = "{\"reqCode\":\"getAssessments\"}".getBytes();
    @FXML
    private TableView<Import> table;
    @FXML
    private TableColumn<Import, Integer> idColumn;
    @FXML
    private TableColumn<Import, String> tripColumn;
    @FXML
    private TableColumn<Import, String> addressSourceColumn;
    @FXML
    private TableColumn<Import, String> productColumn;
    @FXML
    private TableColumn<Import, String> arrivalDateColumn;
    @FXML
    private TableColumn<Import, String> importAssessmentColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private ComboBox<JsonObject> tripComboBox;
    @FXML
    private ComboBox<JsonObject> importAssessmentComboBox;
    @FXML
    private ComboBox<JsonObject> tripComboBoxUpdate;
    @FXML
    private ComboBox<JsonObject> importAssessmentComboBoxUpdate;
    @FXML
    private DatePicker arrivalDatePicker;
    @FXML
    private DatePicker arrivalDateUpdatePicker;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefresh;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonClearForm;
    @FXML
    private Button buttonTripUpdate;
    @FXML
    private Button buttonImportAssessmentUpdate;
    @FXML
    private Button buttonArrivalDateUpdate;
    @FXML
    private Button buttonImportReport;
    @FXML
    private Tab tabTrip;
    @FXML
    private Tab tabImportAssessment;
    @FXML
    private Tab tabArrivalDate;

    @FXML
    void initialize() {
        initTableColumns();

        arrivalDatePicker.getEditor().textProperty().addListener(new TextFieldListener(
                arrivalDatePicker.getEditor(), Pattern.DATE));

        arrivalDateUpdatePicker.getEditor().textProperty().addListener(new TextFieldListener(
                arrivalDateUpdatePicker.getEditor(), Pattern.DATE));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonImportReport.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isNull(tripComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(importAssessmentComboBox.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isEmpty(arrivalDatePicker.getEditor().textProperty()))
        );
        buttonTripUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(tripComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonImportAssessmentUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(importAssessmentComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonArrivalDateUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(arrivalDateUpdatePicker.getEditor().textProperty()))
        );
    }

    @FXML
    public void tabReadSelect() {
        refreshTable();
    }

    @FXML
    public void tabCreateSelect() {
        refreshCreatingForm();
    }

    @FXML
    public void tabUpdateSelect() {
        refreshUpdatingForm();
    }

    @FXML
    public void tabTripSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        tripComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabImportAssessmentSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        importAssessmentComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabArrivalDateSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        arrivalDateUpdatePicker.getEditor().setText("");
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonImportReportClick(){
        byte[] request = String.format("{\"reqCode\":\"getImportReport\",\"%s\":%d}"
                , Import.ID, table.getSelectionModel().getSelectedItem().getId()).getBytes();
        JsonArray array;
        if ((array = call(request)) != null) {
            for (JsonValue e : array) {
                JsonObject object = (JsonObject) e;
                createImportReport(object);
            }
        }
    }

    @FXML
    public void buttonDeleteClick() {
        String request = String.format("{\"reqCode\":\"deleteImport\",\"%s\":%d}"
                , Import.ID, table.getSelectionModel().getSelectedItem().getId()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
            refreshTable();
        }
    }

    @FXML
    public void buttonCreateClick() {
        if (isValid(arrivalDatePicker.getEditor(), Pattern.DATE)) {
            String request = String.format("{\"reqCode\":\"createImport\"," +
                            "\"%s\":%d,\"%s\":%d,\"%s\":\"%s\"}"
                    , Import.IMPORT_TRIP_ID, tripComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Import.IMPORT_ASSESSMENT_ID, importAssessmentComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Import.ARRIVAL_DATE, arrivalDatePicker.getEditor().getText()
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void buttonClearFormClick() {
        refreshCreatingForm();
    }

    @FXML
    public void buttonTripUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateImport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Import.IMPORT_TRIP_ID
                , "value", tripComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Import.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonImportAssessmentUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateImport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Import.IMPORT_ASSESSMENT_ID
                , "value", importAssessmentComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Import.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonArrivalDateUpdateClick() {
        if (isValid(arrivalDateUpdatePicker.getEditor(),Pattern.DATE)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = (arrivalDateUpdatePicker.getValue()).format(formatter);

            String request = String.format("{\"reqCode\":\"updateImport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Import.ARRIVAL_DATE
                    , "value", formattedDate
                    , Import.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_IMPORT, recordComboBox);
    }

    @FXML
    public void tripComboBoxChange() {
        refreshComboBox(TripController.REQUEST_GET_TRIPS, tripComboBox);
    }

    @FXML
    public void importAssessmentComboBoxChange() {
        refreshComboBox(REQUEST_GET_ASSESSMENTS, importAssessmentComboBox);
    }

    @FXML
    public void tripComboBoxUpdateChange() {
        refreshComboBox(TripController.REQUEST_GET_TRIPS, tripComboBoxUpdate);
    }

    @FXML
    public void importAssessmentComboBoxUpdateChange() {
        refreshComboBox(REQUEST_GET_ASSESSMENTS, importAssessmentComboBoxUpdate);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        addressSourceColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressSourceProperty());
        productColumn.setCellValueFactory(cellData -> cellData.getValue().getProductProperty());
        arrivalDateColumn.setCellValueFactory(cellData -> cellData.getValue().getArrivalDateProperty());
        importAssessmentColumn.setCellValueFactory(cellData -> cellData.getValue().getImportAssessmentProperty());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_IMPORT)) != null) {
            ObservableList<Import> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new Import()
                        .setId(json.getInt(Import.ID))
                        .setAddress_source(json.getString(Import.ADDRESS_SOURCE))
                        .setProduct(json.getString(Import.PRODUCT))
                        .setArrival_date(json.getString(Import.ARRIVAL_DATE))
                        .setImport_assessment(json.getString(Import.IMPORT_ASSESSMENT))
                );
            }
        }
    }

    public void refreshComboBox(byte[] request, ComboBox<JsonObject> comboBox) {
        JsonArray array;
        if ((array = call(request)) != null) {
            ObservableList<JsonObject> items = comboBox.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(json);
            }
        }
    }

    public void refreshCreatingForm() {
        tripComboBox.getSelectionModel().clearSelection();
        arrivalDatePicker.getEditor().setText("");
        importAssessmentComboBox.getSelectionModel().clearSelection();
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        tripComboBoxUpdate.getSelectionModel().clearSelection();
        arrivalDateUpdatePicker.getEditor().setText("");
        importAssessmentComboBoxUpdate.getSelectionModel().clearSelection();
    }

    public void createImportReport(JsonObject object){
        final String FONT_NAME="./src/main/resources/font/Arial-Black.ttf";
        try {
            Document document = new Document();
            BaseFont bf=BaseFont.createFont(FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font=new Font(bf,12,Font.NORMAL);

            final String FILE_NAME="importReport"+ object.getInt(Import.ID) +".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(FILE_NAME));
            document.open();

            Paragraph title = new Paragraph("Отчёт об импортной поставке "+"№"+
                    object.getInt(Import.ID),font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Идентификатор импортной поставки: "+
                    object.getInt(Import.ID),font));
            document.add(new Paragraph("Адрес источника: "+
                    object.getString(Import.ADDRESS_SOURCE),font));
            document.add(new Paragraph("Продукт: "+
                    object.getString(Import.PRODUCT),font));
            document.add(new Paragraph("Водитель: "+
                    object.getString(Import.DRIVER),font));
            document.add(new Paragraph("Транспорт: "+
                    object.getString(Import.TRANSPORT),font));
            document.add(new Paragraph("Расстояние: "+
                    object.getString(Import.DISTANCE),font));
            document.add(new Paragraph("Дата прибытия: "+
                    object.getString(Import.ARRIVAL_DATE),font));
            document.add(new Paragraph("Оценка импортной поставки: "+
                    object.getString(Import.IMPORT_ASSESSMENT),font));

            document.close();
            showAlert("Отчёт об импортной поставке составлен. " +
                    "Файл в формате PDF хранится в корневом каталоге");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
    }
}