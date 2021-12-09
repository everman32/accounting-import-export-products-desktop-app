package by.victory.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Import extends Identity {
    public final static String IMPORT_TRIP_ID = "import_trip_id";
    public final static String ADDRESS_SOURCE = "address_source";
    public final static String PRODUCT = "product";
    public final static String DRIVER = "driver";
    public final static String TRANSPORT = "transport";
    public final static String DISTANCE = "distance";
    public final static String IMPORT_ASSESSMENT_ID = "import_assessment_id";
    public final static String IMPORT_ASSESSMENT = "import_assessment";
    public final static String ARRIVAL_DATE = "arrival_date";

    private StringProperty address_source;
    private StringProperty product;
    private StringProperty arrival_date;
    private StringProperty import_assessment;

    public Import() {
        super();
        this.address_source = new SimpleStringProperty();
        this.product = new SimpleStringProperty();
        this.arrival_date = new SimpleStringProperty();
        this.import_assessment = new SimpleStringProperty();
    }

    public Import(int id, String address_source, String product,
                  String import_assessment, String arrival_date) {
        super(id);
        this.address_source.set(address_source);
        this.product.set(product);
        this.import_assessment.set(import_assessment);
        this.arrival_date.set(arrival_date);
    }

    public String getAddress_source() {
        return address_source.get();
    }

    public Import setAddress_source(String address_source) {
        this.address_source.set(address_source);
        return this;
    }

    public String getProduct() {
        return product.get();
    }

    public Import setProduct(String product) {
        this.product.set(product);
        return this;
    }

    public String getImport_assessment() {
        return import_assessment.get();
    }

    public Import setImport_assessment(String import_assessment) {
        this.import_assessment.set(import_assessment);
        return this;
    }

    public String getArrival_date() {
        return arrival_date.get();
    }

    public Import setArrival_date(String arrival_date) {
        this.arrival_date.set(arrival_date);
        return this;
    }

    public StringProperty getAddressSourceProperty() {
        return address_source;
    }

    public StringProperty getProductProperty() {
        return product;
    }

    public StringProperty getImportAssessmentProperty() {
        return import_assessment;
    }

    public StringProperty getArrivalDateProperty() {
        return arrival_date;
    }

    public Import setId(int id) {
        this.id.set(id);
        return this;
    }
}
