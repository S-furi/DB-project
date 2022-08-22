package db_project.view.adminPanelController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class SubsController {
  private final DBGenerator dbGenerator;
  private final QueryParser parser;
  private final Logger logger;
  private final ObservableList<Subscriber> subscribers;

  public SubsController(final DBGenerator dbGenerator) {
    this.dbGenerator = dbGenerator;
    this.parser =
        new ArrayQueryParser(this.dbGenerator.getConnectionProvider().getMySQLConnection());
    this.subscribers = FXCollections.observableArrayList();
    this.logger = Logger.getLogger("SubsController");
    this.logger.setLevel(Level.WARNING);
  }

  public boolean findSubscriber(final String passengerCode) {
    this.findAllSubscribers();
    final var sub =
        this.subscribers.stream().filter(t -> t.getPassengerCode().equals(passengerCode)).findAny();
    this.subscribers.clear();
    sub.ifPresent(t -> this.subscribers.add(t));
    return !this.subscribers.isEmpty();
  }

  public boolean findAllSubscribers() {
    final String query =
        "SELECT p.codPasseggero, c.codCarta, p.nome, p.cognome, c.punti, c.percentualeSconto,"
            + " p.email, p.residenza, s.dataSottoscrizione from LOYALTY_CARD c, PASSEGGERO p,"
            + " SOTTOSCRIZIONE s where c.codCarta = s.codCarta and s.codPasseggero ="
            + " p.codPasseggero; ";
    this.parser.computeSqlQuery(query, null);

    this.subscribers.clear();
    this.subscribers.addAll(
        this.getSubscriberFromQueryResult(this.parser.getQueryResult()).stream()
            .sorted(
                (t1, t2) ->
                    Integer.compare(
                        Integer.parseInt(t1.getPassengerCode()),
                        Integer.parseInt(t2.getPassengerCode())))
            .collect(Collectors.toList()));

    return !this.subscribers.isEmpty();
  }

  private List<Subscriber> getSubscriberFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    final List<Subscriber> subscribers = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              this.logger.info(row.toString());
              final String passengerCode = (String) row.get("codPasseggero");
              final String cardId = (String) row.get("codCarta");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final int points = (int) row.get("punti");
              final int discountPercentage = (int) row.get("percentualeSconto");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              final Date date = (Date) row.get("dataSottoscrizione");

              subscribers.add(
                  new Subscriber(
                      cardId,
                      passengerCode,
                      points,
                      discountPercentage,
                      firstName,
                      lastName,
                      email,
                      residence,
                      date));
            });
    return subscribers;
  }

  public List<TableColumn<Subscriber, ?>> getSubscribersTableViewColumns() {
    final TableColumn<Subscriber, String> passengerCodeColumn =
        new TableColumn<>("Codice Passeggero");
    passengerCodeColumn.setCellValueFactory(new PropertyValueFactory<>("passengerCode"));
    final TableColumn<Subscriber, String> cardIdColumn = new TableColumn<>("Codice Carta");
    cardIdColumn.setCellValueFactory(new PropertyValueFactory<>("cardId"));
    final TableColumn<Subscriber, String> firstNameColumn = new TableColumn<>("Nome");
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    final TableColumn<Subscriber, String> lastNameColumn = new TableColumn<>("Cognome");
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    final TableColumn<Subscriber, Integer> pointsColumn = new TableColumn<>("Punti");
    pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
    final TableColumn<Subscriber, Integer> discountPercentageColumn =
        new TableColumn<>("Percentuale Sconto");
    discountPercentageColumn.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
    final TableColumn<Subscriber, String> emailColumn = new TableColumn<>("Email");
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    final TableColumn<Subscriber, String> residenceColumn = new TableColumn<>("Residenza");
    residenceColumn.setCellValueFactory(new PropertyValueFactory<>("residence"));
    final TableColumn<Subscriber, Date> dateColumn = new TableColumn<>("Data Sottoscrizione");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionDate"));

    final List<TableColumn<Subscriber, ?>> lst =
        List.of(
            passengerCodeColumn,
            cardIdColumn,
            firstNameColumn,
            lastNameColumn,
            pointsColumn,
            discountPercentageColumn,
            emailColumn,
            residenceColumn,
            dateColumn);

    lst.forEach(t -> t.setStyle("-fx-alignment: CENTER;"));
    return lst;
  }

  /**
   * Fins all subscribers in DB and returns them
   *
   * @return
   */
  public ObservableList<Subscriber> getAllSubscribers() {
    this.subscribers.clear();
    this.findAllSubscribers();
    return this.subscribers;
  }

  /**
   * @return the subscribers computed by the query made before calling this method.
   */
  public ObservableList<Subscriber> getSubscribers() {
    return this.subscribers;
  }

  public class Subscriber {
    private String passengerCode;
    private String cardId;
    private String firstName;
    private String lastName;
    private int points;
    private int discountPercentage;
    private String email;
    private String residence;
    private Date subscriptionDate;

    public Subscriber(
        String cardId,
        String passengerCode,
        int points,
        int discountPercentage,
        String firstName,
        String lastName,
        String email,
        String residence,
        Date subscriptionDate) {
      this.cardId = cardId;
      this.passengerCode = passengerCode;
      this.points = points;
      this.discountPercentage = discountPercentage;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.residence = residence;
      this.subscriptionDate = subscriptionDate;
    }

    public String getCardId() {
      return cardId;
    }

    public String getPassengerCode() {
      return passengerCode;
    }

    public int getPoints() {
      return points;
    }

    public int getDiscountPercentage() {
      return discountPercentage;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public String getEmail() {
      return email;
    }

    public String getResidence() {
      return residence;
    }

    public void setCardId(String cardId) {
      this.cardId = cardId;
    }

    public void setPassengerCode(String passengerCode) {
      this.passengerCode = passengerCode;
    }

    public void setPoints(int points) {
      this.points = points;
    }

    public void setDiscountPercentage(int discountPercentage) {
      this.discountPercentage = discountPercentage;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public void setResidence(String residence) {
      this.residence = residence;
    }

    public Date getSubscriptionDate() {
      return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
      this.subscriptionDate = subscriptionDate;
    }

    @Override
    public String toString() {
      return "Subscriber [cardId="
          + cardId
          + ", discountPercentage="
          + discountPercentage
          + ", email="
          + email
          + ", firstName="
          + firstName
          + ", lastName="
          + lastName
          + ", passengerCode="
          + passengerCode
          + ", points="
          + points
          + ", residence="
          + residence
          + ", subscriptionDate="
          + subscriptionDate
          + "]";
    }
  }
}
