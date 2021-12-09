package by.victory.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Export extends Identity {
    public final static String EXPORT_TRIP_ID = "export_trip_id";
    public final static String ADDRESS_DESTINATION = "address_destination";
    public final static String PRODUCT = "product";
    public final static String DRIVER = "driver";
    public final static String TRANSPORT = "transport";
    public final static String DISTANCE = "distance";
    public final static String EXPORT_ASSESSMENT_ID = "export_assessment_id";
    public final static String EXPORT_ASSESSMENT = "export_assessment";
    public final static String DEPARTURE_DATE = "departure_date";

    private StringProperty address_destination;
    private StringProperty product;
    private StringProperty export_assessment;
    private StringProperty departure_date;

    public Export() {
        super();
        this.address_destination = new SimpleStringProperty();
        this.product = new SimpleStringProperty();
        this.departure_date = new SimpleStringProperty();
        this.export_assessment = new SimpleStringProperty();
    }

    public Export(int id, String address_destination, String product,
                  String departure_date, String export_assessment) {
        super(id);
        this.address_destination.set(address_destination);
        this.product.set(product);
        this.departure_date.set(departure_date);
        this.export_assessment.set(export_assessment);
    }

    public String getAddress_destination() {
        return address_destination.get();
    }

    public Export setAddress_destination(String address_destination) {
        this.address_destination.set(address_destination);
        return this;
    }

    public String getProduct() {
        return product.get();
    }

    public Export setProduct(String product) {
        this.product.set(product);
        return this;
    }

    public String getDeparture_date() {
        return departure_date.get();
    }

    public Export setDeparture_date(String departure_date) {
        this.departure_date.set(departure_date);
        return this;
    }

    public String getExport_assessment() {
        return export_assessment.get();
    }

    public Export setExport_assessment(String export_assessment) {
        this.export_assessment.set(export_assessment);
        return this;
    }

    public StringProperty getAddressDestinationProperty() {
        return address_destination;
    }

    public StringProperty getProductProperty() {
        return product;
    }

    public StringProperty getDepartureDateProperty() {
        return departure_date;
    }

    public StringProperty getExportAssessmentProperty() {
        return export_assessment;
    }

    public Export setId(int id) {
        this.id.set(id);
        return this;
    }
}
