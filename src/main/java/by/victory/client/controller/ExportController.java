package by.victory.client.controller;

import by.victory.client.behavior.ComponentBehavior;
import by.victory.client.behavior.ResponseBehavior;
import by.victory.client.behavior.SocketBehavior;
import by.victory.client.validation.RegexValidateBehavior;
import by.victory.client.model.Export;
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

public class ExportController implements SocketBehavior, ComponentBehavior, ResponseBehavior, RegexValidateBehavior {
    private static final byte[] REQUEST_GET_EXPORT = "{\"reqCode\":\"getExport\"}".getBytes();
    private static final byte[] REQUEST_GET_ASSESSMENTS = "{\"reqCode\":\"getAssessments\"}".getBytes();
    @FXML
    private TableView<Export> table;
    @FXML
    private TableColumn<Export, Integer> idColumn;
    @FXML
    private TableColumn<Export, String> tripColumn;
    @FXML
    private TableColumn<Export, String> addressDestinationColumn;
    @FXML
    private TableColumn<Export, String> productColumn;
    @FXML
    private TableColumn<Export, String> departureDateColumn;
    @FXML
    private TableColumn<Export, String> exportAssessmentColumn;
    @FXML
    private ComboBox<JsonObject> recordComboBox;
    @FXML
    private ComboBox<JsonObject> tripComboBox;
    @FXML
    private ComboBox<JsonObject> exportAssessmentComboBox;
    @FXML
    private ComboBox<JsonObject> tripComboBoxUpdate;
    @FXML
    private ComboBox<JsonObject> exportAssessmentComboBoxUpdate;
    @FXML
    private DatePicker departureDatePicker;
    @FXML
    private DatePicker departureDateUpdatePicker;
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
    private Button buttonExportAssessmentUpdate;
    @FXML
    private Button buttonDepartureDateUpdate;
    @FXML
    private Button buttonExportReport;
    @FXML
    private Tab tabTrip;
    @FXML
    private Tab tabExportAssessment;
    @FXML
    private Tab tabDepartureDate;

    @FXML
    void initialize() {
        initTableColumns();

        departureDatePicker.getEditor().textProperty().addListener(new TextFieldListener(
                departureDatePicker.getEditor(), Pattern.DATE));

        departureDateUpdatePicker.getEditor().textProperty().addListener(new TextFieldListener(
                departureDateUpdatePicker.getEditor(), Pattern.DATE));

        buttonDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonExportReport.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        buttonCreate.disableProperty().bind(Bindings.isNull(tripComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(exportAssessmentComboBox.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isEmpty(departureDatePicker.getEditor().textProperty()))
        );
        buttonTripUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(tripComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonExportAssessmentUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(exportAssessmentComboBoxUpdate.getSelectionModel().selectedItemProperty()))
        );
        buttonDepartureDateUpdate.disableProperty().bind(Bindings.isNull(recordComboBox.getSelectionModel().selectedItemProperty())
                .or(Bindings.isEmpty(departureDateUpdatePicker.getEditor().textProperty()))
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
    public void tabExportAssessmentSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        exportAssessmentComboBoxUpdate.getSelectionModel().clearSelection();
    }

    @FXML
    public void tabDepartureDateSelect() {
        recordComboBox.getSelectionModel().clearSelection();
        departureDateUpdatePicker.getEditor().setText("");
    }

    @FXML
    public void buttonRefreshClick() {
        refreshTable();
    }

    @FXML
    public void buttonExportReportClick(){
        byte[] request = String.format("{\"reqCode\":\"getExportReport\",\"%s\":%d}"
                , Export.ID, table.getSelectionModel().getSelectedItem().getId()).getBytes();
        JsonArray array;
        if ((array = call(request)) != null) {
            for (JsonValue e : array) {
                JsonObject object = (JsonObject) e;
                createExportReport(object);
            }
        }
    }

    @FXML
    public void buttonDeleteClick() {
        String request = String.format("{\"reqCode\":\"deleteExport\",\"%s\":%d}"
                , Export.ID, table.getSelectionModel().getSelectedItem().getId()
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
            refreshTable();
        }
    }

    @FXML
    public void buttonCreateClick() {
        if (isValid(departureDatePicker.getEditor(),Pattern.DATE)) {
            String request = String.format("{\"reqCode\":\"createExport\"," +
                            "\"%s\":%d,\"%s\":%d,\"%s\":\"%s\"}"
                    , Export.EXPORT_TRIP_ID, tripComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Export.EXPORT_ASSESSMENT_ID, exportAssessmentComboBox.getSelectionModel().getSelectedItem().getInt("id")
                    , Export.DEPARTURE_DATE, departureDatePicker.getEditor().getText()
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
        String request = String.format("{\"reqCode\":\"updateExport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Export.EXPORT_TRIP_ID
                , "value", tripComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Export.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonExportAssessmentUpdateClick() {
        String request = String.format("{\"reqCode\":\"updateExport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                , "column_name", Export.EXPORT_ASSESSMENT_ID
                , "value", exportAssessmentComboBoxUpdate.getSelectionModel().getSelectedItem().getInt("id")
                , Export.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
        );
        JsonObject object;
        if ((object = call(request)) != null) {
            showContentResponse(object);
        }
    }

    @FXML
    public void buttonDepartureDateUpdateClick() {
        if (isValid(departureDateUpdatePicker.getEditor(),Pattern.DATE)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = (departureDateUpdatePicker.getValue()).format(formatter);

            String request = String.format("{\"reqCode\":\"updateExport\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":%d}"
                    , "column_name", Export.DEPARTURE_DATE
                    , "value", formattedDate
                    , Export.ID, recordComboBox.getSelectionModel().getSelectedItem().getInt("id")
            );
            JsonObject object;
            if ((object = call(request)) != null) {
                showContentResponse(object);
            }
        }
    }

    @FXML
    public void recordComboBoxChange() {
        refreshComboBox(REQUEST_GET_EXPORT, recordComboBox);
    }

    @FXML
    public void tripComboBoxChange() {
        refreshComboBox(TripController.REQUEST_GET_TRIPS, tripComboBox);
    }

    @FXML
    public void exportAssessmentComboBoxChange() {
        refreshComboBox(REQUEST_GET_ASSESSMENTS, exportAssessmentComboBox);
    }

    @FXML
    public void tripComboBoxUpdateChange() {
        refreshComboBox(TripController.REQUEST_GET_TRIPS, tripComboBoxUpdate);
    }

    @FXML
    public void exportAssessmentComboBoxUpdateChange() {
        refreshComboBox(REQUEST_GET_ASSESSMENTS, exportAssessmentComboBoxUpdate);
    }

    public void initTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        addressDestinationColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressDestinationProperty());
        productColumn.setCellValueFactory(cellData -> cellData.getValue().getProductProperty());
        departureDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDepartureDateProperty());
        exportAssessmentColumn.setCellValueFactory(cellData -> cellData.getValue().getExportAssessmentProperty());
    }

    public void refreshTable() {
        JsonArray array;
        if ((array = call(REQUEST_GET_EXPORT)) != null) {
            ObservableList<Export> items = table.getItems();
            items.clear();
            for (JsonValue e : array) {
                JsonObject json = (JsonObject) e;
                items.add(new Export()
                        .setId(json.getInt(Export.ID))
                        .setAddress_destination(json.getString(Export.ADDRESS_DESTINATION))
                        .setProduct(json.getString(Export.PRODUCT))
                        .setDeparture_date(json.getString(Export.DEPARTURE_DATE))
                        .setExport_assessment(json.getString(Export.EXPORT_ASSESSMENT))
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
        departureDatePicker.getEditor().setText("");
        exportAssessmentComboBox.getSelectionModel().clearSelection();
    }

    public void refreshUpdatingForm() {
        recordComboBox.getSelectionModel().clearSelection();
        tripComboBoxUpdate.getSelectionModel().clearSelection();
        departureDateUpdatePicker.getEditor().setText("");
        exportAssessmentComboBoxUpdate.getSelectionModel().clearSelection();
    }

    public void createExportReport(JsonObject object){
        final String FONT_NAME="./src/main/resources/font/Arial-Black.ttf";
        try {
            Document document = new Document();
            BaseFont bf=BaseFont.createFont(FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font=new Font(bf,12,Font.NORMAL);

            final String FILE_NAME="exportReport"+ object.getInt(Export.ID) +".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(FILE_NAME));
            document.open();

            Paragraph title = new Paragraph("Отчёт об экспортной поставке "+"№"+
                    object.getInt(Export.ID),font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Идентификатор экспортной поставки: "+
                    object.getInt(Export.ID),font));
            document.add(new Paragraph("Адрес назначения: "+
                    object.getString(Export.ADDRESS_DESTINATION),font));
            document.add(new Paragraph("Продукт: "+
                    object.getString(Export.PRODUCT),font));
            document.add(new Paragraph("Водитель: "+
                    object.getString(Export.DRIVER),font));
            document.add(new Paragraph("Транспорт: "+
                    object.getString(Export.TRANSPORT),font));
            document.add(new Paragraph("Расстояние: "+
                    object.getString(Export.DISTANCE),font));
            document.add(new Paragraph("Дата отправления: "+
                    object.getString(Export.DEPARTURE_DATE),font));
            document.add(new Paragraph("Оценка экспортной поставки: "+
                    object.getString(Export.EXPORT_ASSESSMENT),font));

            document.close();
            showAlert("Отчёт об экспортной поставке составлен. " +
                    "Файл в формате PDF хранится в корневом каталоге");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
    }
}
